package com.proyecto;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Main {

    private static UrlService service = new UrlService();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new MainHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Servidor iniciado en: http://localhost:8080");
    }

    static class MainHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            System.out.println(method + " " + path);

            if ("OPTIONS".equalsIgnoreCase(method)) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("/".equals(path)) {
                serveFile(exchange, "web/index.html", "text/html");
                return;
            }

            if ("/styles.css".equals(path)) {
                serveFile(exchange, "web/styles.css", "text/css");
                return;
            }

            if ("/script.js".equals(path)) {
                serveFile(exchange, "web/script.js", "application/javascript");
                return;
            }

            if ("/shorten".equals(path) && "POST".equalsIgnoreCase(method)) {
                handleShorten(exchange);
                return;
            }

            if (path.startsWith("/stats/") && "GET".equalsIgnoreCase(method)) {
                handleStats(exchange, path);
                return;
            }

            if ("GET".equalsIgnoreCase(method) && path.length() > 1) {
                handleRedirect(exchange, path);
                return;
            }

            sendText(exchange, 404, "404 - No encontrado", "text/plain");
        }
    }

    private static void handleShorten(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);

        String body = readRequestBody(exchange);
        String url = extractUrlFromJson(body);

        if (url == null || url.trim().isEmpty()) {
            sendJson(exchange, 400, "{\"message\":\"URL no válida\"}");
            return;
        }

        url = url.trim();

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        String code = service.shortenUrl(url);

        if (code == null) {
            sendJson(exchange, 400, "{\"message\":\"No se pudo generar el código\"}");
            return;
        }

        String shortUrl = "http://localhost:8080/" + code;

        String json = "{"
                + "\"originalUrl\":\"" + escapeJson(url) + "\","
                + "\"shortCode\":\"" + escapeJson(code) + "\","
                + "\"shortUrl\":\"" + escapeJson(shortUrl) + "\""
                + "}";

        sendJson(exchange, 200, json);
    }

    private static void handleStats(HttpExchange exchange, String path) throws IOException {
        addCorsHeaders(exchange);

        String code = path.substring("/stats/".length());

        if (code == null || code.trim().isEmpty()) {
            sendJson(exchange, 400, "{\"message\":\"Código no válido\"}");
            return;
        }

        String originalUrl = service.getOriginalUrl(code);

        if (originalUrl == null) {
            sendJson(exchange, 404, "{\"message\":\"Código no encontrado\"}");
            return;
        }

        int clicks = service.getClicks(code);

        String json = "{"
                + "\"shortCode\":\"" + escapeJson(code) + "\","
                + "\"originalUrl\":\"" + escapeJson(originalUrl) + "\","
                + "\"clicks\":" + clicks
                + "}";

        sendJson(exchange, 200, json);
    }

    private static void handleRedirect(HttpExchange exchange, String path) throws IOException {
        String code = path.substring(1);

        if (code.contains("/")) {
            sendText(exchange, 404, "404 - Enlace no encontrado", "text/plain");
            return;
        }

        String originalUrl = service.getOriginalUrl(code);

        if (originalUrl == null) {
            sendText(exchange, 404, "404 - Enlace no encontrado", "text/plain");
            return;
        }

        service.registerClick(code);

        Headers headers = exchange.getResponseHeaders();
        headers.add("Location", originalUrl);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    private static void serveFile(HttpExchange exchange, String filePath, String contentType) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            sendText(exchange, 404, "Archivo no encontrado", "text/plain");
            return;
        }

        byte[] bytes = Files.readAllBytes(file.toPath());
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
        );

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        return sb.toString();
    }

    // Esto es simple a propósito, para no meter librerías JSON
    private static String extractUrlFromJson(String json) throws UnsupportedEncodingException {
        if (json == null) {
            return null;
        }

        json = json.trim();

        int keyIndex = json.indexOf("\"url\"");
        if (keyIndex == -1) {
            return null;
        }

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) {
            return null;
        }

        int firstQuote = json.indexOf("\"", colonIndex + 1);
        if (firstQuote == -1) {
            return null;
        }

        int secondQuote = json.indexOf("\"", firstQuote + 1);
        if (secondQuote == -1) {
            return null;
        }

        String value = json.substring(firstQuote + 1, secondQuote);

        return URLDecoder.decode(value, "UTF-8");
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static void sendText(HttpExchange exchange, int statusCode, String text, String contentType) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static void addCorsHeaders(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
