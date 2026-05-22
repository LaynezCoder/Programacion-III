
const express = require("express");
const cors = require("cors");
const fs = require("fs");
const path = require("path");
const crypto = require("crypto");

const app = express();
const PORT = 3000;

app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, "public")));

const DATA_DIR = path.join(__dirname, "data");
const CHAIN_FILE = path.join(DATA_DIR, "blockchain.json");
const LOTS_FILE = path.join(DATA_DIR, "lots.json");
const SENSORS_FILE = path.join(DATA_DIR, "sensors.json");

if (!fs.existsSync(DATA_DIR)) fs.mkdirSync(DATA_DIR);

function readJson(file, fallback) {
  if (!fs.existsSync(file)) {
    fs.writeFileSync(file, JSON.stringify(fallback, null, 2), "utf8");
    return fallback;
  }
  return JSON.parse(fs.readFileSync(file, "utf8"));
}

function writeJson(file, data) {
  fs.writeFileSync(file, JSON.stringify(data, null, 2), "utf8");
}

function sha256(text) {
  return crypto.createHash("sha256").update(text).digest("hex");
}

// Función hash del bloque.
// Incluye campos importantes para que cualquier cambio altere el hash.
function calculateHash(block) {
  return sha256(
    block.index +
    block.timestamp +
    JSON.stringify(block.data) +
    block.previousHash +
    block.merkleRoot +
    block.nonce
  );
}

// Merkle Root básico. Sirve para representar una o varias lecturas dentro del bloque.
function calculateMerkleRoot(records) {
  if (!records || records.length === 0) return sha256("");
  let hashes = records.map((record) => sha256(JSON.stringify(record)));

  while (hashes.length > 1) {
    const nextLevel = [];
    for (let i = 0; i < hashes.length; i += 2) {
      const left = hashes[i];
      const right = hashes[i + 1] || left;
      nextLevel.push(sha256(left + right));
    }
    hashes = nextLevel;
  }

  return hashes[0];
}

// Proof of Work
function mineBlock(block, difficulty = 2) {
  const target = "0".repeat(difficulty);

  while (!block.hash.startsWith(target)) {
    block.nonce++;
    block.hash = calculateHash(block);
  }

  return block;
}

function createGenesisBlock() {
  const data = {
    tipo: "GENESIS",
    mensaje: "Inicio de blockchain para trazabilidad de cadena de frío"
  };

  const block = {
    index: 0,
    timestamp: new Date().toISOString(),
    data,
    previousHash: "0",
    merkleRoot: calculateMerkleRoot([data]),
    nonce: 0,
    hash: ""
  };

  block.hash = calculateHash(block);
  return mineBlock(block);
}

function getChain() {
  let chain = readJson(CHAIN_FILE, []);

  if (chain.length === 0) {
    chain = [createGenesisBlock()];
    writeJson(CHAIN_FILE, chain);
  }

  return chain;
}

function getLots() {
  return readJson(LOTS_FILE, []);
}

function getSensors() {
  return readJson(SENSORS_FILE, [
    {
      id: "SENSOR-001",
      nombre: "DS18B20 - Termo Stanley",
      tipo: "Temperatura",
      estado: "Activo",
      ubicacion: "Termo Stanley"
    },
    {
      id: "SENSOR-002",
      nombre: "DS18B20 - Hielera A1",
      tipo: "Temperatura",
      estado: "Activo",
      ubicacion: "Hielera A1"
    }
  ]);
}

// Smart Contract simulado.
// Reglas de negocio aplicadas antes de crear el bloque.
function coldChainContract({ lotId, sensorId, temperature }) {
  const lots = getLots();
  const lot = lots.find((l) => l.id === lotId);

  if (!lot) {
    return {
      accepted: false,
      status: "RECHAZADO",
      reason: "El lote no existe"
    };
  }

  if (lot.sensorId !== sensorId) {
    return {
      accepted: false,
      status: "RECHAZADO",
      reason: "El sensor no está autorizado para este lote"
    };
  }

  const temp = Number(temperature);

  if (Number.isNaN(temp)) {
    return {
      accepted: false,
      status: "RECHAZADO",
      reason: "Temperatura inválida"
    };
  }

  if (temp < Number(lot.minTemp) || temp > Number(lot.maxTemp)) {
    return {
      accepted: true,
      status: "ALERTA",
      reason: `Temperatura fuera del rango permitido (${lot.minTemp}°C a ${lot.maxTemp}°C)`
    };
  }

  return {
    accepted: true,
    status: "CORRECTO",
    reason: "Temperatura dentro del rango permitido"
  };
}

function createBlock(data) {
  const chain = getChain();
  const previousBlock = chain[chain.length - 1];

  const block = {
    index: previousBlock.index + 1,
    timestamp: new Date().toISOString(),
    data,
    previousHash: previousBlock.hash,
    merkleRoot: calculateMerkleRoot([data]),
    nonce: 0,
    hash: ""
  };

  block.hash = calculateHash(block);
  return mineBlock(block);
}

function validateChain() {
  const chain = getChain();
  const errors = [];

  for (let i = 0; i < chain.length; i++) {
    const current = chain[i];

    const recalculatedHash = calculateHash({
      index: current.index,
      timestamp: current.timestamp,
      data: current.data,
      previousHash: current.previousHash,
      merkleRoot: current.merkleRoot,
      nonce: current.nonce
    });

    if (current.hash !== recalculatedHash) {
      errors.push({
        blockIndex: current.index,
        type: "HASH_INVALIDO",
        message: "El hash guardado no coincide con el hash recalculado. El bloque fue alterado.",
        storedHash: current.hash,
        recalculatedHash
      });
    }

    if (i > 0) {
      const previous = chain[i - 1];

      if (current.previousHash !== previous.hash) {
        errors.push({
          blockIndex: current.index,
          type: "ENLACE_INVALIDO",
          message: "El previousHash no coincide con el hash del bloque anterior.",
          previousHashStored: current.previousHash,
          realPreviousHash: previous.hash
        });
      }
    }
  }

  return {
    valid: errors.length === 0,
    totalBlocks: chain.length,
    checkedAt: new Date().toISOString(),
    errors
  };
}

app.get("/api/sensors", (req, res) => {
  res.json(getSensors());
});

app.get("/api/lots", (req, res) => {
  res.json(getLots());
});

app.post("/api/lots", (req, res) => {
  const { name, type, minTemp, maxTemp, sensorId, location, responsible } = req.body;

  if (!name || !type || minTemp === undefined || maxTemp === undefined || !sensorId || !location || !responsible) {
    return res.status(400).json({ error: "Todos los campos son obligatorios" });
  }

  if (Number(minTemp) >= Number(maxTemp)) {
    return res.status(400).json({ error: "La temperatura mínima debe ser menor que la máxima" });
  }

  const lots = getLots();

  const newLot = {
    id: "LOT-" + Date.now(),
    name,
    type,
    minTemp: Number(minTemp),
    maxTemp: Number(maxTemp),
    sensorId,
    location,
    responsible,
    createdAt: new Date().toISOString()
  };

  lots.push(newLot);
  writeJson(LOTS_FILE, lots);

  res.status(201).json(newLot);
});

app.get("/api/blockchain", (req, res) => {
  res.json(getChain());
});

app.post("/api/readings", (req, res) => {
  const { lotId, sensorId, temperature } = req.body;

  const contractResult = coldChainContract({ lotId, sensorId, temperature });

  if (!contractResult.accepted) {
    return res.status(400).json(contractResult);
  }

  const data = {
    tipo: "LECTURA_TEMPERATURA",
    lotId,
    sensorId,
    temperature: Number(temperature),
    unit: "°C",
    status: contractResult.status,
    contractMessage: contractResult.reason
  };

  const block = createBlock(data);
  const chain = getChain();
  chain.push(block);
  writeJson(CHAIN_FILE, chain);

  res.status(201).json({
    message: "Lectura registrada en blockchain",
    contractResult,
    block
  });
});

app.get("/api/validate", (req, res) => {
  res.json(validateChain());
});

// Ruta para simular la prueba de fuego desde la interfaz.
// Cambia un bloque sin recalcular su hash.
app.post("/api/tamper/:index", (req, res) => {
  const index = Number(req.params.index);
  const { newTemperature } = req.body;

  const chain = getChain();
  const block = chain.find((b) => b.index === index);

  if (!block) return res.status(404).json({ error: "Bloque no encontrado" });

  if (!block.data || block.data.tipo !== "LECTURA_TEMPERATURA") {
    return res.status(400).json({ error: "Solo se pueden alterar bloques de lectura de temperatura" });
  }

  block.data.temperature = Number(newTemperature);
  block.data.status = "CORRECTO_MANIPULADO";

  writeJson(CHAIN_FILE, chain);

  res.json({
    message: "Bloque manipulado intencionalmente. Ahora valida la cadena.",
    block
  });
});

app.listen(PORT, () => {
  getChain();
  getSensors();
  console.log(`ColdChain Blockchain MVP ejecutándose en http://localhost:${PORT}`);
});
