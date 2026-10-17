# 📚 Documentación de API - Mobiliario de Oficina Backend

Esta es la especificación técnica de los endpoints del backend para el showroom de mobiliario corporativo.

**Base URL:** `http://localhost:8080`

---

## 🔐 1. Autenticación (Módulo Auth)

Todas las rutas protegidas requieren enviar el Token JWT en los encabezados de la petición (Headers).
* **Header:** `Authorization`
* **Value:** `Bearer <tu_token_jwt>`

> *Nota: Las rutas exactas dependen de tu AuthController (`/auth/login`, `/auth/register`, `/auth/google`), pero devuelven el token necesario para el resto de la aplicación.*

---

## 🛋️ 2. Módulo de Productos (Catálogo)

### Listar Productos (Catálogo Público)
Obtiene la lista de productos. Permite aplicar múltiples filtros por URL.
* **Método:** `GET`
* **Ruta:** `/api/v1/productos`
* **Acceso:** 🟢 Público
* **Query Parameters (Opcionales):**
  * `enShowroom` (boolean): Ej. `?enShowroom=true`
  * `tipoEspacio` (String): Ej. `?tipoEspacio=OPERATIVO` (Valores: OPERATIVO, GERENCIAL, REUNION, RECEPCION, ESPERA)
  * `buscar` (String): Ej. `?buscar=silla`
* **Respuesta Exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Escritorio Modular tipo Bench para 4 personas",
    "descripcion": "Estación de trabajo optimizada...",
    "categoriaNombre": "Estaciones Operativas",
    "tipoEspacio": "OPERATIVO",
    "precioReferencialUsd": 450.00,
    "enShowroom": true,
    "material": "Melamina 18mm con estructura metálica.",
    "dimensionesEstandar": "2.40m x 1.20m x 0.75m",
    "imagenes": [
      "https://ejemplo.com/fotos/escritorio-bench.jpg"
    ]
  }
]
```

### Obtener Detalle de Producto
* **Método:** `GET`
* **Ruta:** `/api/v1/productos/{id}`
* **Acceso:** 🟢 Público
* **Respuesta Exitosa (200 OK):** JSON del producto (igual a la estructura de arriba, pero un solo objeto).

### Crear Nuevo Producto
* **Método:** `POST`
* **Ruta:** `/api/v1/productos`
* **Acceso:** 🔴 Protegido (Roles: `ADMIN`, `ASESOR_VENTAS`)
* **Body (JSON):**
```json
{
  "nombre": "Silla Gerencial Premium",
  "descripcion": "Asiento ergonómico en cuero sintético.",
  "categoriaId": 2,
  "tipoEspacio": "GERENCIAL",
  "precioReferencialUsd": 250.50,
  "enShowroom": true,
  "material": "Cuero sintético y base de aluminio",
  "dimensionesEstandar": "Ajustable",
  "imagenesUrls": [
    "https://ejemplo.com/silla1.jpg"
  ]
}
```

*(Nota: Los endpoints de actualización `PUT` y eliminación `DELETE` usan la ruta `/api/v1/productos/{id}` y están protegidos).*

---

## 🗂️ 3. Módulo de Categorías

### Listar Categorías
Obtiene todas las categorías para pintar el menú de navegación o el sidebar de filtros.
* **Método:** `GET`
* **Ruta:** `/api/v1/categorias`
* **Acceso:** 🟢 Público
* **Respuesta Exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Estaciones Operativas",
    "slug": "estaciones-operativas",
    "descripcion": "Mobiliario modular para equipos de trabajo dinámicos."
  },
  {
    "id": 2,
    "nombre": "Sillería Ergonómica",
    "slug": "silleria-ergonomica",
    "descripcion": "Sillas diseñadas para el máximo confort y salud postural."
  }
]
```

---

## 📅 4. Módulo de Citas (Showroom)

### Agendar una Cita
Registra una nueva cita para visitar el showroom o solicitar asesoría. 
> **⚠️ Importante:** Como el usuario debe estar logueado, el backend extrae el nombre y correo directamente del Token JWT. El frontend no necesita enviarlos.
* **Método:** `POST`
* **Ruta:** `/api/v1/citas`
* **Acceso:** 🔴 Protegido (Cualquier usuario autenticado)
* **Body (JSON):**
```json
{
  "empresa": "Tech Solutions C.A.",
  "telefono": "+584141234567",
  "fechaHora": "2026-11-20T14:30:00",
  "tipoAsesoria": "VISITA_SHOWROOM",
  "notas": "Nos interesa remodelar nuestra sala de reuniones."
}
```
* **Respuesta Exitosa (201 Created):**
```json
{
  "mensaje": "Cita agendada con éxito",
  "codigoCita": "CITA-A4F9B2"
}
```

*(Nota: Los administradores y asesores de venta pueden consultar la lista completa de citas haciendo un `GET /api/v1/citas` y actualizar el estado a CONFIRMADA usando `PATCH /api/v1/citas/{id}`).*