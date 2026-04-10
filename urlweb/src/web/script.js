let currentCode = "";

async function shortenUrl() {
    const urlInput = document.getElementById("urlInput").value.trim();

    hideError();
    document.getElementById("statsBox").style.display = "none";

    if (urlInput === "") {
        showError("Por favor ingresa una URL.");
        return;
    }

    try {
        const response = await fetch("/shorten", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({url: urlInput})
        });

        const data = await response.json();

        if (!response.ok) {
            showError(data.message || "Ocurrió un error.");
            return;
        }

        currentCode = data.shortCode;

        document.getElementById("originalUrl").textContent = data.originalUrl;
        document.getElementById("shortCode").textContent = data.shortCode;

        const shortUrlLink = document.getElementById("shortUrl");
        shortUrlLink.textContent = data.shortUrl;
        shortUrlLink.href = data.shortUrl;

        document.getElementById("resultBox").style.display = "block";

    } catch (error) {
        showError("No se pudo conectar con el servidor.");
    }
}

async function loadStats() {
    if (currentCode === "") {
        return;
    }

    hideError();

    try {
        const response = await fetch("/stats/" + currentCode);
        const data = await response.json();

        if (!response.ok) {
            showError(data.message || "No se pudieron cargar las estadísticas.");
            return;
        }

        document.getElementById("statsCode").textContent = data.shortCode;
        document.getElementById("statsOriginalUrl").textContent = data.originalUrl;
        document.getElementById("statsClicks").textContent = data.clicks;

        document.getElementById("statsBox").style.display = "block";

    } catch (error) {
        showError("Error al obtener estadísticas.");
    }
}

function showError(message) {
    document.getElementById("errorMessage").textContent = message;
    document.getElementById("errorBox").style.display = "block";
}

function hideError() {
    document.getElementById("errorBox").style.display = "none";
}