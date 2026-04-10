package com.proyecto;

import java.util.HashMap;
import java.util.Map;

public class UrlService {

    // URL larga -> código corto
    private Map<String, String> urlToCode = new HashMap<String, String>();

    // código corto -> URL larga
    private Map<String, String> codeToUrl = new HashMap<String, String>();

    // clics por código
    private Map<String, Integer> clickCount = new HashMap<String, Integer>();

    // contador para generar códigos
    private long counter = 1;

    public synchronized String shortenUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return null;
        }

        originalUrl = originalUrl.trim();

        if (urlToCode.containsKey(originalUrl)) {
            return urlToCode.get(originalUrl);
        }

        String code;

        do {
            code = Base62Encoder.encode(counter);
            counter++;
        } while (codeToUrl.containsKey(code));

        urlToCode.put(originalUrl, code);
        codeToUrl.put(code, originalUrl);
        clickCount.put(code, 0);

        return code;
    }

    public synchronized String getOriginalUrl(String code) {
        return codeToUrl.get(code);
    }

    public synchronized void registerClick(String code) {
        if (clickCount.containsKey(code)) {
            clickCount.put(code, clickCount.get(code) + 1);
        }
    }

    public synchronized int getClicks(String code) {
        if (clickCount.containsKey(code)) {
            return clickCount.get(code);
        }
        return 0;
    }
}
