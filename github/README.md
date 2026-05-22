# ColdChain Blockchain MVP

Proyecto académico web para trazabilidad de cadena de frío usando blockchain.

## Caso elegido

Registro de sensores IoT de temperatura en blockchain para asegurar que vacunas, medicamentos o alimentos no perdieron refrigeración.

## Lenguajes usados

- Backend: Node.js + Express
- Frontend: HTML, CSS y JavaScript
- Base de datos simple: archivos JSON
- Hash: SHA-256 usando el módulo `crypto` de Node.js

## Por qué se eligió esta tecnología

Es la forma más fácil para hacerlo web porque todo usa JavaScript. No se necesita configurar MySQL, MongoDB ni frameworks pesados. La base de datos en JSON permite hacer la prueba de fuego fácilmente: abrir el archivo, modificar un bloque y validar que la cadena se rompe.

## Cómo ejecutar

1. Instalar Node.js.
2. Abrir terminal en esta carpeta.
3. Ejecutar:

```bash
npm install
npm start
```

4. Abrir en el navegador:

```bash
http://localhost:3000
```

## Flujo de prueba

1. Ir a "Registrar Lote".
2. Crear un lote, por ejemplo:
   - Producto: Vacuna COVID-19
   - Tipo: Vacuna
   - Temperatura mínima: 2
   - Temperatura máxima: 8
   - Sensor: SENSOR-001
   - Ubicación: Termo Stanley
   - Responsable: tu nombre
3. Ir a Dashboard.
4. Registrar una lectura normal, por ejemplo 5.2 °C.
5. Registrar una lectura de falla, por ejemplo 10.5 °C.
6. Ir a Historial y revisar los bloques.
7. Ir a Integridad y validar la blockchain.
8. Para la prueba de fuego:
   - Presionar "Alterar último bloque", o
   - Abrir `data/blockchain.json` y modificar manualmente una temperatura.
9. Presionar "Validar Blockchain".
10. El sistema detectará la alteración.

## Estructura de bloque

Cada bloque contiene:

```json
{
  "index": 1,
  "timestamp": "fecha y hora",
  "data": {
    "tipo": "LECTURA_TEMPERATURA",
    "lotId": "LOT-...",
    "sensorId": "SENSOR-001",
    "temperature": 5.2,
    "unit": "°C",
    "status": "CORRECTO",
    "contractMessage": "Temperatura dentro del rango permitido"
  },
  "previousHash": "hash del bloque anterior",
  "merkleRoot": "raíz Merkle de los datos",
  "nonce": 50,
  "hash": "hash del bloque actual"
}
```

## Smart Contract simulado

El contrato está en la función `coldChainContract()` dentro de `server.js`.

Reglas:

1. El lote debe existir.
2. El sensor debe estar autorizado para ese lote.
3. La temperatura debe ser numérica.
4. Si la temperatura está fuera del rango del lote, se registra como ALERTA.
5. Si está dentro del rango, se registra como CORRECTO.

## Prueba de inmutabilidad

El sistema recalcula el hash de cada bloque usando:

- index
- timestamp
- data
- previousHash
- merkleRoot
- nonce

Si alguien modifica `data.temperature` en la base de datos, el hash recalculado ya no coincide con el hash guardado. Por eso la validación falla.
