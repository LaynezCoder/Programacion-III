let sensors = [];
let lots = [];
let chain = [];

const $ = (id) => document.getElementById(id);

async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    headers: { "Content-Type": "application/json" },
    ...options
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.error || data.reason || "Error en la solicitud");
  }

  return data;
}

function setNavigation() {
  document.querySelectorAll(".nav-btn").forEach((button) => {
    button.addEventListener("click", async () => {
      document.querySelectorAll(".nav-btn").forEach((b) => b.classList.remove("active"));
      button.classList.add("active");

      document.querySelectorAll(".view").forEach((view) => view.classList.remove("active"));
      $(button.dataset.view).classList.add("active");

      await refreshAll();
    });
  });
}

async function refreshAll() {
  await loadSensors();
  await loadLots();
  await loadBlockchain();
  await validateBlockchain(false);
}

async function loadSensors() {
  sensors = await requestJson("/api/sensors");

  ["lotSensor", "readingSensor"].forEach((selectId) => {
    const select = $(selectId);
    if (!select) return;

    select.innerHTML = sensors
      .map((sensor) => `<option value="${sensor.id}">${sensor.id} - ${sensor.nombre}</option>`)
      .join("");
  });

  const container = $("sensorsList");
  if (container) {
    container.innerHTML = sensors
      .map((sensor) => `
        <div class="item">
          <strong>${sensor.id}</strong> — ${sensor.nombre}<br>
          Tipo: ${sensor.tipo}<br>
          Ubicación: ${sensor.ubicacion}<br>
          Estado: <span class="ok">${sensor.estado}</span>
        </div>
      `)
      .join("");
  }
}

async function loadLots() {
  lots = await requestJson("/api/lots");

  const readingLot = $("readingLot");

  if (readingLot) {
    if (lots.length === 0) {
      readingLot.innerHTML = `<option value="">Primero registra un lote</option>`;
    } else {
      readingLot.innerHTML = lots
        .map((lot) => `<option value="${lot.id}">${lot.name} - ${lot.id}</option>`)
        .join("");
    }
  }

  const container = $("lotsList");
  if (container) {
    if (lots.length === 0) {
      container.innerHTML = "<p>Aún no hay lotes registrados.</p>";
      return;
    }

    container.innerHTML = lots
      .map((lot) => `
        <div class="item">
          <strong>${lot.name}</strong> — ${lot.type}<br>
          ID: ${lot.id}<br>
          Rango permitido: ${lot.minTemp}°C a ${lot.maxTemp}°C<br>
          Sensor asignado: ${lot.sensorId}<br>
          Ubicación: ${lot.location}<br>
          Responsable: ${lot.responsible}
        </div>
      `)
      .join("");
  }
}

async function createLot() {
  try {
    const body = {
      name: $("lotName").value.trim(),
      type: $("lotType").value,
      minTemp: Number($("minTemp").value),
      maxTemp: Number($("maxTemp").value),
      sensorId: $("lotSensor").value,
      location: $("location").value.trim(),
      responsible: $("responsible").value.trim()
    };

    await requestJson("/api/lots", {
      method: "POST",
      body: JSON.stringify(body)
    });

    alert("Lote registrado correctamente.");
    clearLotForm();
    await refreshAll();
  } catch (error) {
    alert(error.message);
  }
}

function clearLotForm() {
  $("lotName").value = "";
  $("lotType").value = "Vacuna";
  $("minTemp").value = "2";
  $("maxTemp").value = "8";
  $("location").value = "";
  $("responsible").value = "";
}

async function registerReading() {
  try {
    if (lots.length === 0) {
      alert("Primero debes registrar un lote.");
      return;
    }

    const lotId = $("readingLot").value;
    const selectedLot = lots.find((lot) => lot.id === lotId);

    const body = {
      lotId,
      sensorId: $("readingSensor").value,
      temperature: Number($("readingTemp").value)
    };

    const result = await requestJson("/api/readings", {
      method: "POST",
      body: JSON.stringify(body)
    });

    $("currentTemp").textContent = `${body.temperature}°C`;
    $("currentStatus").textContent = result.contractResult.status;
    $("currentStatus").className = result.contractResult.status === "CORRECTO" ? "ok" : "alert";
    $("currentLotLabel").textContent = selectedLot ? `${selectedLot.name} - ${selectedLot.id}` : "Lote monitoreado";

    alert("Lectura registrada en blockchain.");
    await refreshAll();
  } catch (error) {
    alert(error.message);
  }
}

function formatBlockData(data) {
  if (!data) return "<span>Sin datos</span>";

  if (data.tipo === "GENESIS") {
    return `
      <div class="data-card genesis">
        <div class="data-title">Bloque Génesis</div>
        <div class="data-row">
          <span>Mensaje:</span>
          <strong>${data.mensaje}</strong>
        </div>
      </div>
    `;
  }

  if (data.tipo === "LECTURA_TEMPERATURA") {
    const statusClass = data.status === "CORRECTO" ? "ok" : "alert";

    return `
      <div class="data-card">
        <div class="data-title">Lectura de Temperatura</div>

        <div class="data-grid">
          <div>
            <span>Lote</span>
            <strong>${data.lotId}</strong>
          </div>

          <div>
            <span>Sensor</span>
            <strong>${data.sensorId}</strong>
          </div>

          <div>
            <span>Temperatura</span>
            <strong>${data.temperature}${data.unit || "°C"}</strong>
          </div>

          <div>
            <span>Estado</span>
            <strong class="${statusClass}">${data.status}</strong>
          </div>
        </div>

        <div class="contract-message">
          ${data.contractMessage}
        </div>
      </div>
    `;
  }

  return `
    <div class="data-card">
      <pre>${JSON.stringify(data, null, 2)}</pre>
    </div>
  `;
}

async function loadBlockchain() {
  chain = await requestJson("/api/blockchain");

  $("totalBlocks").textContent = chain.length.toLocaleString("es-GT");

  const table = $("blockchainTable");
  if (table) {
    table.innerHTML = chain
      .map((block) => `
        <tr>
          <td>${block.index}</td>
          <td>${new Date(block.timestamp).toLocaleString()}</td>
         <td>${formatBlockData(block.data)}</td>
          <td class="hash">${block.merkleRoot}</td>
          <td class="hash">${block.previousHash}</td>
          <td class="hash">${block.hash}</td>
        </tr>
      `)
      .join("");
  }

  loadAlertsFromChain();
}

function loadAlertsFromChain() {
  const alerts = chain.filter((block) => block.data && block.data.status === "ALERTA");

  const recent = $("recentAlerts");
  const list = $("alertsList");

  const html = alerts.length === 0
    ? "Sin alertas registradas."
    : alerts.slice().reverse().map((block) => `
      <div class="item">
        <strong class="alert">Alerta en bloque #${block.index}</strong><br>
        Sensor: ${block.data.sensorId}<br>
        Lote: ${block.data.lotId}<br>
        Temperatura: ${block.data.temperature}°C<br>
        Motivo: ${block.data.contractMessage}<br>
        Fecha: ${new Date(block.timestamp).toLocaleString()}
      </div>
    `).join("");

  if (recent) recent.innerHTML = html;
  if (list) list.innerHTML = html;
}

async function validateBlockchain(showAlert = false) {
  const result = await requestJson("/api/validate");

  $("chainStatus").textContent = result.valid ? "Válida" : "Alterada";
  $("chainStatus").className = result.valid ? "ok" : "alert";

  const box = $("validationResult");
  if (box) {
    if (result.valid) {
      box.textContent = `✅ Blockchain válida
Bloques verificados: ${result.totalBlocks}
Fecha de validación: ${new Date(result.checkedAt).toLocaleString()}`;
    } else {
      box.textContent = `❌ Blockchain alterada
Bloques verificados: ${result.totalBlocks}

Errores detectados:
${JSON.stringify(result.errors, null, 2)}`;
    }
  }

  if (showAlert) {
    alert(result.valid ? "Blockchain válida." : "Blockchain alterada. Revisa los detalles.");
  }

  return result;
}

async function tamperLastBlock() {
  try {
    await loadBlockchain();

    const readings = chain.filter((block) => block.data && block.data.tipo === "LECTURA_TEMPERATURA");

    if (readings.length === 0) {
      alert("Primero registra una lectura.");
      return;
    }

    const last = readings[readings.length - 1];

    await requestJson(`/api/tamper/${last.index}`, {
      method: "POST",
      body: JSON.stringify({ newTemperature: 5.5 })
    });

    alert(`Se alteró el bloque #${last.index}. Ahora valida la blockchain.`);
    await refreshAll();
  } catch (error) {
    alert(error.message);
  }
}

function setEvents() {
  $("createLotBtn").addEventListener("click", createLot);
  $("clearLotBtn").addEventListener("click", clearLotForm);
  $("registerReadingBtn").addEventListener("click", registerReading);
  $("coldBtn").addEventListener("click", () => {
    $("readingTemp").value = "5.2";
    registerReading();
  });
  $("hotBtn").addEventListener("click", () => {
    $("readingTemp").value = "10.5";
    registerReading();
  });
  $("reloadChainBtn").addEventListener("click", loadBlockchain);
  $("validateBtn").addEventListener("click", () => validateBlockchain(true));
  $("tamperBtn").addEventListener("click", tamperLastBlock);
}

setNavigation();
setEvents();
refreshAll();
