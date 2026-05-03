# Backend · Microservicios de Actividades del Hogar

Proyecto Spring Boot que expone los microservicios CRUD sobre la entidad **Actividad** para el Taller No 4 de Programación Avanzada (Universidad Distrital Francisco José de Caldas).

## Arquitectura

El proyecto sigue una arquitectura por capas con separación clara de responsabilidades (SOLID):

```
co.edu.udistrital.actividades
├── ActividadesApplication.java          · Punto de entrada de Spring Boot
├── config/
│   ├── CorsConfig.java                  · Política CORS expresada
│   └── DataInitializer.java             · Carga inicial de actividades
├── model/
│   ├── Actividad.java                   · Entidad JPA (solo persistencia)
│   └── TipoActividad.java               · Enum de tipos
├── dto/
│   ├── ActividadDTO.java                · Entrada (con anotaciones de validación)
│   ├── ActividadResponse.java           · Salida al frontend (sin validaciones)
│   └── ErrorResponse.java               · Estructura de errores devueltos
├── exception/
│   ├── ActividadNoEncontradaException.java
│   ├── DatosInvalidosException.java
│   └── ManejadorGlobalExcepciones.java  · @RestControllerAdvice global
├── repository/
│   └── ActividadRepository.java         · JpaRepository
├── service/
│   ├── IActividadService.java           · Contrato (interfaz, DIP)
│   └── ActividadServiceImpl.java        · Implementación con reglas de negocio
└── controller/
    ├── ActividadRestController.java     · API REST (microservicios)
    └── ActividadViewController.java     · Vistas Thymeleaf
```

### Patrón DTO + Response

Para evitar exponer la entidad de persistencia al frontend y separar claramente las preocupaciones:

| Tipo | Para qué se usa | Anotaciones que lleva |
|------|-----------------|------------------------|
| `Actividad` (entidad) | Persistencia en H2 | Sólo JPA (`@Entity`, `@Column`) |
| `ActividadDTO` | Recibir peticiones del frontend | Bean Validation (`@NotBlank`, `@NotNull`...) |
| `ActividadResponse` | Devolver respuestas al frontend | Sólo Jackson (`@JsonFormat`) |

### Manejo de errores

Cuando una operación no termina bien, las clases del backend lanzan **excepciones tipadas** (no devuelven `Optional` vacíos ni booleanos). El componente `@RestControllerAdvice` las captura y las transforma en respuestas JSON estructuradas que el frontend muestra al usuario.

```json
{
    "marcaTiempo": "2026-05-02 14:35:12",
    "codigo":      404,
    "estado":      "Not Found",
    "mensaje":     "No existe ninguna actividad con el id 99 en la base de datos.",
    "ruta":        "/api/actividades/99"
}
```

Excepciones manejadas:

| Excepción | Código HTTP | Cuándo se lanza |
|-----------|-------------|-----------------|
| `ActividadNoEncontradaException` | 404 | Al consultar/modificar/borrar un id inexistente |
| `DatosInvalidosException` | 400 | Al violar reglas de negocio (fechas inconsistentes...) |
| `MethodArgumentNotValidException` | 400 | Al fallar la validación del DTO con `@Valid` |
| `HttpMessageNotReadableException` | 400 | Al recibir un JSON malformado |
| `Exception` (cualquier otra) | 500 | Red de seguridad |

## Requisitos

- JDK 17 o superior
- Maven 3.8+ (o usar `./mvnw` si se incluye el wrapper)
- NetBeans 17+

## Cómo ejecutar

```bash
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8080`. Al primer arranque se cargan **7 actividades de ejemplo** en la base de datos H2.

## Endpoints REST

| Método  | URL                          | Acción                        |
|---------|------------------------------|-------------------------------|
| POST    | `/api/actividades`           | Insertar (recibe `ActividadDTO`) |
| GET     | `/api/actividades`           | Consultar todas               |
| GET     | `/api/actividades/{id}`      | Consultar por id              |
| PUT     | `/api/actividades/{id}`      | Modificar                     |
| DELETE  | `/api/actividades/{id}`      | Borrar                        |

### Ejemplo de cuerpo JSON para POST/PUT

```json
{
    "titulo": "Sacar la basura",
    "descripcion": "Sacar las bolsas a la calle los martes y viernes.",
    "fechaInicio": "2026-05-02",
    "fechaTerminacion": "2026-05-09",
    "tipoActividad": "FISICA",
    "idQuehacer": 8,
    "idTutor": 100,
    "idHijo": 200
}
```

`tipoActividad` admite: `FISICA`, `ACOMPANAMIENTO`, `SUPERVISION`, `CREATIVIDAD`.

## Vistas Thymeleaf de prueba

| Ruta                           | Descripción                            |
|--------------------------------|----------------------------------------|
| `/vista`                       | Portada con tabla de actividades       |
| `/vista/actividades`           | Listado completo en tarjetas           |
| `/vista/actividades/{id}`      | Detalle de una actividad concreta      |

## Consola H2

`http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:bd_actividades`
- Usuario: `sa`
- Contraseña: *(vacía)*

## Documentación

Todo el código está anotado con JavaDoc. Para generar el sitio HTML:

```bash
mvn javadoc:javadoc
```

El resultado queda en `target/site/apidocs/index.html`.
