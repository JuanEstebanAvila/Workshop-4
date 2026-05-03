# Backend · Microservicios de Actividades del Hogar

Proyecto Spring Boot que expone los microservicios CRUD sobre la entidad **Actividad** para el Taller No 4 de Programación Avanzada (Universidad Distrital Francisco José de Caldas).

## Requisitos

- JDK 17 o superior
- Maven 3.8+ (o usar `./mvnw` si se incluye el wrapper)
- NetBeans 17+ (opcional; el proyecto Maven se importa directamente)

## Cómo ejecutar

```bash
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8080`.

Al primer arranque se cargan **7 actividades de ejemplo** en la base de datos H2, para poder probar todos los endpoints.

## Endpoints REST

| Método  | URL                          | Acción                        |
|---------|------------------------------|-------------------------------|
| POST    | `/api/actividades`           | Insertar actividad            |
| GET     | `/api/actividades`           | Consultar todas               |
| GET     | `/api/actividades/{id}`      | Consultar por id              |
| PUT     | `/api/actividades/{id}`      | Modificar (parcial)           |
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

## Estructura de paquetes (MVC + SOLID)

```
co.edu.udistrital.actividades
├── ActividadesApplication.java     · Punto de entrada
├── config/
│   ├── CorsConfig.java             · Política CORS expresada
│   └── DataInitializer.java        · Carga inicial de datos
├── model/
│   ├── Actividad.java              · Entidad JPA
│   └── TipoActividad.java          · Enum de tipos
├── repository/
│   └── ActividadRepository.java    · JpaRepository (capa Repositorio)
├── service/
│   ├── IActividadService.java      · Contrato (capa Servicio - DIP)
│   └── ActividadServiceImpl.java   · Implementación
└── controller/
    ├── ActividadRestController.java · API REST (capa Controlador)
    └── ActividadViewController.java · Vistas Thymeleaf
```

## Documentación

Todo el código está anotado con JavaDoc. Para generar el sitio HTML:

```bash
mvn javadoc:javadoc
```

El resultado queda en `target/site/apidocs/index.html`.

## Notas del taller

- El frontend HTML se construye en un proyecto **separado** (`frontend-actividades/`).
- Las políticas CORS están declaradas tanto a nivel de configuración global (`CorsConfig`) como a nivel de controlador (`@CrossOrigin`).
- La capa de servicio depende de una **interfaz** (no de una clase concreta), respetando el principio de Inversión de Dependencias.
