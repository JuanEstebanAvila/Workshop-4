# Frontend · Quehaceres del Hogar

Proyecto **HTML/CSS/JavaScript puro** para gestionar actividades de los quehaceres del hogar. Es el frontend del Taller No 4 de Programación Avanzada (Universidad Distrital Francisco José de Caldas) y se construye en un proyecto **separado** del backend Spring Boot, tal como lo exige el enunciado.

## Cómo abrir en NetBeans

1. NetBeans → **File → New Project → HTML5/JS Application → HTML5/JS Application with Existing Sources**.
2. Selecciona la carpeta `frontend-actividades/`.
3. Acepta el wizard. NetBeans lo registrará como proyecto HTML.
4. Click derecho sobre `index.html` → **Run File** y se abrirá en el navegador.

(Alternativamente, basta con abrir `index.html` directamente en cualquier navegador moderno.)

## Estructura del proyecto

```
frontend-actividades/
├── index.html        · Portada con navegación a las 5 operaciones
├── registrar.html    · Formulario para crear actividades (POST)
├── listar.html       · Cuadrícula con todas las actividades (GET)
├── consultar.html    · Formulario de consulta por id (GET por id)
├── modificar.html    · Búsqueda + edición (PUT)
├── borrar.html       · Búsqueda + confirmación visual + borrado (DELETE)
├── css/
│   └── styles.css    · Hoja de estilos externa (única)
├── js/
│   └── cliente-api.js  · Cliente fetch del backend
└── Docs/
    └── integrantes.txt
```

## Cumplimiento del enunciado

| Requisito                                                                 | Estado |
|---------------------------------------------------------------------------|--------|
| Proyecto HTML separado del backend                                        | ✓      |
| Hojas de estilo externas                                                  | ✓ (`css/styles.css`) |
| Múltiples páginas conectadas vía hipervínculos                            | ✓ (header con `<nav>`) |
| Formularios para todas las operaciones CRUD                               | ✓      |
| **Sin** `alert()`, `confirm()`, `prompt()` o ventanas emergentes          | ✓      |
| **Sin** `console.log` para mensajes al usuario                            | ✓      |
| Feedback siempre en HTML (paneles `.panel-mensaje`)                       | ✓      |
| Diseño deslumbrante y agradable                                           | ✓ (paleta editorial cálida con tipografías Fraunces + Manrope) |
| Comunicación expuesta hacia el backend                                    | ✓ (fetch a `http://localhost:8080/api/actividades`) |

## Conexión con el backend

El cliente JavaScript (`js/cliente-api.js`) apunta por defecto a:

```
http://localhost:8080/api/actividades
```

Si el backend Spring Boot corre en otro puerto, basta con editar la constante `URL_API` al principio del archivo.

> **Nota del taller:** Aunque el enunciado dice que "entre estos dos no tendrán comunicación entre ellos" durante el Taller 4, el cliente JS ya está preparado para la conexión (con CORS expresado en el backend), de modo que pueda usarse de inmediato sin retrabajar el código.

## Accesibilidad

- Estructura semántica con `<header>`, `<main>`, `<footer>`, `<nav>`, `<form>`, `<article>`, `<section>`.
- `skip-link` para usuarios de teclado.
- Etiquetas `<label>` asociadas a cada input mediante `for`.
- Mensajes de retroalimentación con `role="alert"` y `aria-live="polite"`.
- Foco visible (`:focus-visible`).
- Contraste cumple WCAG AA en toda la paleta.
