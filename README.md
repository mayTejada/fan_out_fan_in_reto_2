# fan_out_fan_in_reto_2

Aplicación Spring Boot que expone una API para calcular el cuadrado de una lista de números o generar números aleatorios y procesarlos concurrentemente.
Utiliza el patrón fan-out/fan-in para distribuir el trabajo entre varios hilos y retornar los resultados ordenados.

Interpetación de la solución: https://medium.com/@tejadamayra93/patr%C3%B3n-fan-worker-sink-0a582db4f54b

## Requisitos previos

- Java 17 o superior instalado
- Git instalado
- (Opcional) Gradle instalado
- Postman/curl para probar la API

## Instalación

### 1. Clona el repositorio
```bash
git clone https://github.com/mayTejada/fan_out_fan_in_reto_2.git
```
### 2. Construye el proyecto
- Windows
Abre CMD o PowerShell en la carpeta del proyecto y ejecuta:
```bash
.\gradlew.bat build
```
- Mac/Linux
Abre Terminal en la carpeta del proyecto y ejecuta:
```bash
./gradlew build
```
### 3. Ejecuta la aplicación
- Windows
```bash
.\gradlew.bat bootRun
 ```
- Mac/Linux
```bash
./gradlew bootRun
 ```
La aplicación estará disponible en http://localhost:8080.
## Probar la API
Puedes usar curl o utilizar Postman.

## Detalles de la API

### Endpoint: `/api/square`

- **Método HTTP:** `POST`
- **Descripción:** Calcula el cuadrado de una lista de números proporcionada o genera números aleatorios si no se envían.
- **Content-Type:** `application/json`

### Parámetros de entrada

| Campo                  | Tipo     | Obligatorio | Descripción                                                                 |
|------------------------|----------|-------------|-----------------------------------------------------------------------------|
| `numbers`              | `List<Long>` | No          | Lista de números a procesar.                                               |
| `rangeMinGenerateRandom` | `Long`   | No          | Valor mínimo para generar números aleatorios (si `numbers` está vacío).    |
| `rangeMaxGenerateRandom` | `Long`   | No          | Valor máximo para generar números aleatorios (si `numbers` está vacío).    |
| `sizeListNumbers`      | `Long`   | No          | Cantidad de números aleatorios a generar (si `numbers` está vacío).        |

> **Nota:** Si no se proporciona `numbers`, los campos `rangeMinGenerateRandom`, `rangeMaxGenerateRandom` y `sizeListNumbers` son obligatorios.

### Probar la API con Postman
1. Abre Postman y crea una nueva solicitud `POST`.
2. URL: `http://localhost:8080/api/square`
3. En la pestaña **Headers**, agrega:
   - Key: `Content-Type`
   - Value: `application/json`
4. En la pestaña **Body**, selecciona `raw` y elige `JSON`.

### Ejemplo 1: Enviar lista de números en el body

```json
{
"numbers": [3, 1639, 211, 1027, 463, 980, 526, 1923, 198, 944, 427, 1302, 505, 1767, 1697, 1886, 1270, 681, 1661, 51, 850, 1936, 1519, 1981, 263, 398, 1406, 1739, 985, 442, 491, 843, 1774, 1291, 1920, 1325, 880, 1666, 710, 1710, 1919, 1294, 1762, 1502,1, 1178, 1141, 1306, 423, 643, 1113, 80, 416, 648, 1371, -15, 667, 1073, 546, 1380, 1522, 845, 1180, 1045, 1752, 322, 1885, 1898, 467, 1581, 39, 582, 1782, 890, 1720, 385, 203, -18, 1805, 359, 1125, 1327, 484, 1705, 57, 1484, 67, 1195, 1396, 1608, 1863, 1514, 301, -4, 1503, 1454, 729, 61, 520, 1402, 111, 1076,2]
}
```
### Ejemplo 2: Generar números aleatorios ( Enviar en el body los parámetros)
```json
{
  "rangeMinGenerateRandom": -4560,
  "rangeMaxGenerateRandom": 4560,
  "sizeListNumbers": 100
}
```
En ambos ejemplos (1 y 2), haz clic en 'Send' para ver la respuesta en la parte inferior, ya sea basada en la lista enviada en el cuerpo del mensaje o en la que se genere.

#### Respuesta exitosa (Ejemplo 1):
```json
[1, 4, 9, 16, 225, 324, 1521, 2601, 3249, 3721, 4489, 6400, 12321, 39204, 41209, 44521, 69169, 90601, 103684, 128881, 148225, 158404, 173056, 178929, 182329, 195364, 214369, 218089, 234256, 241081, 255025, 270400, 276676, 298116, 338724, 413449, 419904, 444889, 463761, 504100, 531441, 710649, 714025, 722500, 774400, 792100, 891136, 960400, 970225, 1054729, 1092025, 1151329, 1157776, 1238769, 1265625, 1301881, 1387684, 1392400, 1428025, 1612900, 1666681, 1674436, 1695204, 1705636, 1755625, 1760929, 1879641, 1904400, 1948816, 1965604, 1976836, 2114116, 2202256, 2256004, 2259009, 2292196, 2307361, 2316484, 2499561, 2585664, 2686321, 2758921, 2775556, 2879809, 2907025, 2924100, 2958400, 3024121, 3069504, 3104644, 3122289, 3147076, 3175524, 3258025, 3470769, 3553225, 3556996, 3602404, 3682561, 3686400, 3697929, 3748096, 3924361]
```

## Notas adicionales
Asegúrate de que la aplicación esté corriendo en http://localhost:8080 antes de probar los endpoints.

Los números generados aleatoriamente estarán dentro del rango especificado.
