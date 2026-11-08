# ANÁLISIS DEL DOMINIO - SISTEMA DE ALQUILER RETROVIDEO

### 1. Tablas de la base de datos que usaré

Para el sistema de gestión de alquileres necesito estas tablas:
* **ALQUILER** (principal)
* **SOCIO** (relacionada por FK)
* **PELICULA** (relacionada por FK - opcional)
* **VIDEOJUEGO** (relacionada por FK - opcional)

### 2. Campos de la tabla ALQUILER y mapeo a la API

| Campo | Tipo | ¿En API? | Razón |
| :--- | :--- | :--- | :--- |
| **id** | BIGINT (PK) | SÍ | Para identificar cada alquiler de forma única. |
| **id_socio** | BIGINT (FK) | SÍ | Saber qué socio exacto realiza el alquiler. |
| **id_pelicula** | BIGINT (FK) | SÍ | Saber qué película se ha alquilado (si aplica). |
| **id_videojuego** | BIGINT (FK) | SÍ | Saber qué videojuego se ha alquilado (si aplica). |
| **fechaAlquiler** | DATE | SÍ | Cuándo se ha llevado el cliente el artículo. |
| **fechaDevolucion** | DATE | SÍ | Fecha límite para que el cliente devuelva el artículo. |

#### Campos auto-generados por el backend:

De estos campos, el backend (Java/Hibernate) generará automáticamente:
* **id**: Asignado por la base de datos (AUTO_INCREMENT / IDENTITY).
* **fechaAlquiler**: Se establece automáticamente a la fecha actual (`LocalDate.now()`) en el momento de crear la instancia del alquiler.
* **fechaDevolucion**: La lógica de negocio calcula automáticamente un plazo de 3 días a partir de la fecha de alquiler.

#### Información adicional que incluiré (Interacción con el Frontend):

Además de los campos estrictos de la tabla `ALQUILER`, en las respuestas GET de la API incluiré nombres legibles para que el frontend no tenga que hacer peticiones extra para cruzar datos. Expondré:
* **nombre_socio**: Para mostrar el nombre real del cliente en lugar de solo su ID.
* **titulo_item**: Para mostrar el título de la película o del videojuego alquilado.

### 3. Validaciones principales

Al registrar nuevos alquileres, el sistema validará lo siguiente:

#### Campos y referencias obligatorias:
* Es obligatorio proporcionar un identificador de socio válido (`id_socio`).
* La integridad referencial de MySQL bloquea la creación del alquiler si el ID del socio, película o videojuego no existen en sus respectivas tablas.

#### Lógica de negocio:
* **Exclusividad del ítem**: Un alquiler debe registrar una película **o** un videojuego. (No puede haber un registro de alquiler que no contenga ningún ítem físico).
* **Bloqueo de duplicados de identidad**: El DNI de la tabla `SOCIO` debe ser `UNIQUE` en la base de datos para evitar que se registren dos clientes con la misma identidad física.
