# API de mobiliario de oficina

Backend REST para el catálogo, las citas de showroom, las cotizaciones y los favoritos.

## Índice

- [Inicio y configuración](#inicio-y-configuración)
- [Convenciones de la API](#convenciones-de-la-api)
- [Autenticación](#autenticación)
- [Productos](#productos)
- [Categorías](#categorías)
- [Citas](#citas)
- [Cotizaciones](#cotizaciones)
- [Favoritos](#favoritos)
- [Documentación interactiva y errores](#documentación-interactiva-y-errores)
- [Comportamientos actuales a tener en cuenta](#comportamientos-actuales-a-tener-en-cuenta)

## Inicio y configuración

- URL local: `http://localhost:8080`
- Puerto: `8080`
- Persistencia: MySQL
- Java configurado en Maven: `25`
- La configuración de conexión y autenticación se obtiene de variables de entorno:
  - `urlMySQL`
  - `userMySQL`
  - `passMySQL`
  - `claveJWT` (clave en Base64)
  - `idCliente` (client ID de Google)

La aplicación usa `spring.jpa.hibernate.ddl-auto=update`. Configura las variables anteriores antes de iniciar el backend.

## Convenciones de la API

- Las rutas de negocio están versionadas bajo `/api/v1`; autenticación usa `/auth/v1`.
- Los cuerpos y respuestas se intercambian en JSON.
- Para los endpoints protegidos, enviar el JWT así:

```http
Authorization: Bearer <token>
Content-Type: application/json
```

- Un usuario registrado o creado por Google tiene el rol `USER`. Las operaciones administrativas requieren `ADMIN`. No existe un rol `ASESOR_VENTAS` en la configuración actual.
- Los parámetros de fecha y hora usan el formato ISO-8601, por ejemplo `2026-11-20T14:30:00`.
- Los valores de enumeraciones distinguen los nombres definidos en la sección de cada recurso.

### Matriz de acceso

| Recurso | Público | Usuario autenticado | Solo `ADMIN` |
|---|---|---|---|
| Autenticación | `POST /auth/v1/login`, `POST /auth/v1/register`, `POST /auth/v1/google` | `GET /auth/v1/me` | — |
| Productos | `GET` colección y detalle | — | `POST`, `PUT`, `DELETE` |
| Categorías | `GET` colección | — | `POST`, `PUT`, `DELETE` |
| Citas | — | `POST`, `GET /mis-citas`, `PUT` y `DELETE` de una cita propia | `GET` colección y `PATCH` de estado |
| Cotizaciones | `POST` | `GET /mis-cotizaciones` | `GET` colección y `PATCH` de estado |
| Favoritos | — | Todas las operaciones | — |

## Autenticación

### Iniciar sesión

```http
POST /auth/v1/login
```

Público. Se puede enviar `username` o `email`; si se proporciona un `username` no vacío, se usa en lugar del correo.

```json
{
  "username": "maria",
  "email": "maria@example.com",
  "password": "contraseña"
}
```

Respuesta `200 OK`:

```json
{
  "token": "<jwt>"
}
```

### Registrar usuario

```http
POST /auth/v1/register
```

Público. El usuario nuevo recibe el rol `USER`.

```json
{
  "username": "maria",
  "email": "maria@example.com",
  "password": "contraseña"
}
```

Respuesta `201 Created`:

```json
{
  "token": "<jwt>"
}
```

Un username o correo ya registrado produce un error de credenciales.

### Iniciar sesión con Google

```http
POST /auth/v1/google
```

Público. `token` debe ser un ID token de Google válido para el `idCliente` configurado.

```json
{
  "token": "<google-id-token>"
}
```

Respuesta `200 OK`:

```json
{
  "token": "<jwt-del-sistema>"
}
```

Si el correo de Google no existe, se crea una cuenta `USER`.

### Obtener perfil propio

```http
GET /auth/v1/me
```

Requiere JWT. La respuesta prevista es `200 OK`:

```json
{
  "nombre": "maria",
  "email": "maria@example.com",
  "role": "USER"
}
```

## Productos

Valores admitidos para `tipoEspacio`: `OFICINA`, `CONSULTORIO`, `SALON_BELLEZA`, `OPERATIVO`, `GERENCIAL`, `HOGAR`.

### Listar productos

```http
GET /api/v1/productos
```

Público. Parámetros opcionales:

| Parámetro | Tipo | Comportamiento |
|---|---|---|
| `buscar` | texto | Busca coincidencias parciales en el nombre del producto. |
| `enShowroom` | boolean | `true` devuelve productos en showroom. El valor `false` no aplica un filtro. |
| `tipoEspacio` | texto | Filtra por uno de los valores admitidos de `TipoEspacio`. |

Los filtros no se combinan: se aplica primero `buscar` si tiene texto; si no, `enShowroom=true`; en caso contrario, `tipoEspacio` si tiene valor; y, si ninguno aplica, se devuelve el catálogo completo.

Ejemplos:

```http
GET /api/v1/productos?buscar=silla
GET /api/v1/productos?enShowroom=true
GET /api/v1/productos?tipoEspacio=OPERATIVO
```

Respuesta `200 OK`: lista de objetos `ProductoResponse`.

```json
[
  {
    "id": 1,
    "nombre": "Escritorio modular",
    "descripcion": "Estación para oficina",
    "categoriaId": 2,
    "categoriaNombre": "Escritorios",
    "tipoEspacio": "OPERATIVO",
    "precioReferencialUsd": 450.00,
    "enShowroom": true,
    "material": "Melamina y acero",
    "dimensionesEstandar": "240 x 120 x 75 cm",
    "imagenes": [
      "https://ejemplo.com/escritorio.jpg"
    ]
  }
]
```

### Obtener un producto

```http
GET /api/v1/productos/{id}
```

Público. Respuesta `200 OK`: un objeto `ProductoResponse` con la misma estructura del listado. El producto inexistente produce un error.

### Crear un producto

```http
POST /api/v1/productos
```

Solo `ADMIN`. Respuesta `201 Created`: el producto creado como `ProductoResponse`.

```json
{
  "nombre": "Silla gerencial",
  "descripcion": "Silla ergonómica",
  "categoriaId": 2,
  "tipoEspacio": "GERENCIAL",
  "precioReferencialUsd": 250.50,
  "enShowroom": true,
  "material": "Cuero sintético y aluminio",
  "dimensionesEstandar": "Ajustable",
  "imagenes": [
    "https://ejemplo.com/silla.jpg"
  ]
}
```

`nombre`, `categoriaId`, `tipoEspacio` y `precioReferencialUsd` son obligatorios; el nombre no puede estar vacío y el precio debe ser igual o mayor a cero. `imagenes`, `descripcion`, `enShowroom`, `material` y `dimensionesEstandar` son opcionales. Si se omite `enShowroom`, el valor predeterminado es `true`.

### Actualizar un producto

```http
PUT /api/v1/productos/{id}
```

Solo `ADMIN`. Recibe el mismo cuerpo que la creación y responde `200 OK` con el objeto actualizado. Se reemplaza la lista de imágenes con el valor enviado; omitir `imagenes` deja el producto sin imágenes.

### Eliminar un producto

```http
DELETE /api/v1/productos/{id}
```

Solo `ADMIN`. Respuesta `200 OK`:

```json
{
  "mensaje": "Producto eliminado con éxito"
}
```

## Categorías

### Listar categorías

```http
GET /api/v1/categorias
```

Público. Respuesta `200 OK`:

```json
[
  {
    "id": 1,
    "nombre": "Escritorios",
    "slug": "escritorios",
    "descripcion": "Mobiliario para espacios de trabajo"
  }
]
```

### Crear categoría

```http
POST /api/v1/categorias
```

Solo `ADMIN`. `nombre` es obligatorio y no puede estar vacío; `descripcion` es opcional. El `slug` se genera a partir del nombre.

```json
{
  "nombre": "Sillería ergonómica",
  "descripcion": "Sillas para oficina"
}
```

Respuesta `201 Created`: la categoría creada con `id`, `nombre`, `slug` y `descripcion`.

### Actualizar categoría

```http
PUT /api/v1/categorias/{id}
```

Solo `ADMIN`. Recibe el mismo cuerpo de creación; recalcula el `slug` cuando se actualiza el nombre. Respuesta `200 OK`: la categoría actualizada.

### Eliminar categoría

```http
DELETE /api/v1/categorias/{id}
```

Solo `ADMIN`. Respuesta `200 OK`:

```json
{
  "mensaje": "Categoría eliminada con éxito"
}
```

Responde `404 Not Found` si no existe y `409 Conflict` si todavía tiene productos asociados.

## Citas

Valores de `tipoAsesoria`: `COTIZACION_GENERAL`, `PROYECTO_CORPORATIVO`, `DISENO_DE_ESPACIOS`, `RETIRO_DE_COMPRA`, `VISITA_SHOWROOM`.

Valores de `estado`: `PENDIENTE`, `CONFIRMADA`, `COMPLETADA`, `CANCELADA`.

### Agendar una cita

```http
POST /api/v1/citas
```

Requiere usuario autenticado. El cuerpo validado es:

```json
{
  "nombreCliente": "María Pérez",
  "empresa": "Empresa Ejemplo",
  "telefono": "+584141234567",
  "email": "maria@example.com",
  "fechaHora": "2026-11-20T14:30:00",
  "tipoAsesoria": "VISITA_SHOWROOM",
  "notas": "Interés en mobiliario para sala de reuniones"
}
```

`nombreCliente`, `telefono`, `email`, `fechaHora` y `tipoAsesoria` son obligatorios. El email debe tener formato válido y la fecha debe estar en el futuro. `empresa` y `notas` son opcionales.

Respuesta `201 Created`:

```json
{
  "mensaje": "Cita agendada con éxito",
  "codigoCita": "CITA-A4F9B2"
}
```

### Consultar mis citas

```http
GET /api/v1/citas/mis-citas
```

Requiere usuario autenticado. Respuesta `200 OK`: lista de entidades de cita asociadas al usuario.

### Consultar todas las citas

```http
GET /api/v1/citas
```

Solo `ADMIN`. Respuesta `200 OK`: lista completa de entidades `CitaShowroom`, con datos de la cita y la asociación del usuario.

### Modificar una cita propia

```http
PUT /api/v1/citas/{codigoCita}
```

Requiere usuario autenticado y solo permite modificar una cita propia que no esté `COMPLETADA` ni `CANCELADA`. Recibe el mismo cuerpo validado de `POST /api/v1/citas`.

Respuesta `200 OK`:

```json
{
  "mensaje": "Cita modificada con éxito"
}
```

### Eliminar una cita propia

```http
DELETE /api/v1/citas/{codigoCita}
```

Requiere usuario autenticado y solo permite eliminar una cita propia que no esté `COMPLETADA` ni `CANCELADA`.

Respuesta `200 OK`:

```json
{
  "mensaje": "Cita eliminada con éxito"
}
```

### Cambiar el estado de una cita

```http
PATCH /api/v1/citas/{codigoCita}
```

Solo `ADMIN`. Cuerpo:

```json
{
  "estado": "CONFIRMADA"
}
```

Respuesta `200 OK`:

```json
{
  "mensaje": "Estado de la cita actualizado a CONFIRMADA"
}
```

## Cotizaciones

Valores admitidos para el estado: `PENDIENTE`, `CONTACTADO`, `CANCELADA`.

### Crear una cotización

```http
POST /api/v1/cotizaciones
```

Público. El servidor calcula el total con los precios actuales de los productos y crea la cotización con estado `PENDIENTE`.

```json
{
  "nombreCliente": "María Pérez",
  "empresa": "Empresa Ejemplo",
  "telefono": "+584141234567",
  "email": "maria@example.com",
  "notas": "Entrega en Caracas",
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 4,
      "cantidad": 1
    }
  ]
}
```

`nombreCliente`, `telefono`, `email` e `items` son obligatorios; email debe ser válido, `items` debe contener al menos un elemento y cada elemento requiere `productoId` y `cantidad` mayor que cero. `empresa` y `notas` son opcionales. Un ID de producto inexistente produce `400 Bad Request`.

Respuesta `201 Created`:

```json
{
  "codigoCotizacion": "COT-0001"
}
```

### Consultar mis cotizaciones

```http
GET /api/v1/cotizaciones/mis-cotizaciones
```

Requiere usuario autenticado. Respuesta `200 OK`: lista de cotizaciones asociadas al correo del usuario.

### Consultar todas las cotizaciones

```http
GET /api/v1/cotizaciones
```

Solo `ADMIN`. Respuesta `200 OK`: lista de entidades `Cotizacion`, incluyendo sus detalles, el total, la fecha de creación y el estado.

### Cambiar el estado de una cotización

```http
PATCH /api/v1/cotizaciones/{codigo}
```

Solo `ADMIN`. Cuerpo:

```json
{
  "estado": "CONTACTADO"
}
```

Respuesta `200 OK`:

```json
{
  "mensaje": "Estado de la cotización actualizado a CONTACTADO"
}
```

## Favoritos

Todos los endpoints requieren usuario autenticado y operan sobre la cuenta indicada por el JWT.

### Listar favoritos

```http
GET /api/v1/favoritos
```

Respuesta `200 OK`: arreglo de IDs de producto.

```json
[1, 4, 7]
```

### Agregar favorito

```http
POST /api/v1/favoritos/{productoId}
```

No requiere cuerpo. Respuesta `204 No Content`. Si el producto ya era favorito, la operación no crea duplicados. Si el producto no existe, responde `404 Not Found`.

### Eliminar favorito

```http
DELETE /api/v1/favoritos/{productoId}
```

No requiere cuerpo. Respuesta `204 No Content`. Si el producto existe pero no estaba guardado como favorito, no cambia el estado y devuelve igualmente `204`; si el producto no existe, responde `404 Not Found`.

## Documentación interactiva y errores

La configuración permite acceder sin autenticación a:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Especificación OpenAPI: `http://localhost:8080/v3/api-docs`

Respuestas de error habituales:

| Estado | Uso |
|---|---|
| `400 Bad Request` | JSON ilegible, enumeración o fecha con formato incorrecto, validación del cuerpo o producto inexistente en una cotización. |
| `401 Unauthorized` | Falta un JWT válido en una ruta protegida o se rechazan credenciales/token. |
| `403 Forbidden` | El usuario está autenticado pero no tiene el rol requerido. |
| `404 Not Found` | Recurso inexistente cuando el servicio define explícitamente ese estado. |
| `409 Conflict` | Intento de borrar una categoría con productos asociados. |

El formato de error no está normalizado: depende del manejador global, Spring Security y del tipo de excepción.

## Comportamientos actuales a tener en cuenta

- El cuerpo de cita exige `nombreCliente` y `email`, pero al guardar y modificar una cita esos campos no se usan: el sistema asocia la cita con la cuenta del JWT. El nombre y correo guardados no se reciben de ese cuerpo.
- `GET /api/v1/citas/mis-citas` y `GET /api/v1/cotizaciones/mis-cotizaciones` reciben como identificador del principal el username del JWT, pero consultan los registros por email. Por ello, pueden devolver una lista vacía cuando username y email difieren.
- `GET /auth/v1/me` declara un repositorio sin inyección en el controlador; puede fallar al ejecutarse hasta que esa dependencia se inyecte.
- Los endpoints administrativos de citas y cotizaciones devuelven entidades JPA, no DTOs de respuesta dedicados.
