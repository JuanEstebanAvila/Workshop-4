package co.edu.udistrital.actividades.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import co.edu.udistrital.actividades.dto.ErrorResponse;

/**
 * Manejador global de excepciones para todos los controladores REST.
 *
 * <p>Esta clase implementa el requerimiento del taller:
 * <em>"el mensaje de los errores lo producen las clases del backend...
 * deberia generar un error que vaya del backend al frontend para que
 * salga en la pagina"</em>.</p>
 *
 * <p>La anotación {@link RestControllerAdvice} permite que un único
 * componente intercepte excepciones lanzadas por cualquier controlador
 * REST de la aplicación. Cada método anotado con {@link ExceptionHandler}
 * atiende un tipo concreto de excepción y la transforma en una respuesta
 * estructurada {@link ErrorResponse} con el código HTTP apropiado.</p>
 *
 * <p>Beneficios de centralizar el manejo de errores aquí:</p>
 * <ul>
 *   <li>Los controladores quedan limpios, sin {@code try/catch} repetidos.</li>
 *   <li>El formato de las respuestas de error es consistente.</li>
 *   <li>Es fácil agregar nuevos tipos de error sin tocar los controladores.</li>
 *   <li>Cumple con el principio de Responsabilidad Única (cada clase tiene
 *       una sola razón para cambiar).</li>
 * </ul>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    /** Logger SLF4J para registrar los errores en la bitácora del servidor. */
    private static final Logger LOG = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    /**
     * Constructor por defecto.
     */
    public ManejadorGlobalExcepciones() {
        // Sin estado interno; sólo expone métodos de manejo.
    }

    /**
     * Maneja la excepción lanzada cuando no se encuentra una actividad
     * en la base de datos.
     *
     * @param ex      la excepción lanzada.
     * @param request información de la petición HTTP que la disparó.
     * @return respuesta HTTP 404 con cuerpo JSON estructurado.
     */
    @ExceptionHandler(ActividadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrada(
            final ActividadNoEncontradaException ex,
            final WebRequest request) {

        LOG.warn("Recurso no encontrado: {}", ex.getMessage());

        final ErrorResponse cuerpo = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                extraerRuta(request));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
    }

    /**
     * Maneja la excepción lanzada cuando los datos suministrados no
     * cumplen las reglas de negocio.
     *
     * @param ex      la excepción lanzada.
     * @param request información de la petición HTTP que la disparó.
     * @return respuesta HTTP 400 con cuerpo JSON estructurado.
     */
    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<ErrorResponse> manejarDatosInvalidos(
            final DatosInvalidosException ex,
            final WebRequest request) {

        LOG.warn("Datos inválidos: {}", ex.getMessage());

        final ErrorResponse cuerpo = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                extraerRuta(request));

        return ResponseEntity.badRequest().body(cuerpo);
    }

    /**
     * Maneja las violaciones de Bean Validation (anotaciones {@code @NotBlank},
     * {@code @NotNull}, {@code @Size}, etc.) detectadas por {@code @Valid}.
     *
     * <p>Recopila todos los mensajes de los campos inválidos y los
     * incluye en el campo {@code errores} de la respuesta.</p>
     *
     * @param ex      excepción de validación lanzada por Spring.
     * @param request petición HTTP que la disparó.
     * @return respuesta HTTP 400 con la lista de campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(
            final MethodArgumentNotValidException ex,
            final WebRequest request) {

        LOG.warn("Validación fallida: {} campos inválidos",
                 ex.getBindingResult().getFieldErrorCount());

        final ErrorResponse cuerpo = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Algunos campos del formulario son inválidos",
                extraerRuta(request));

        for (final FieldError campo : ex.getBindingResult().getFieldErrors()) {
            cuerpo.agregarError(campo.getField() + ": " + campo.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(cuerpo);
    }

    /**
     * Maneja las peticiones cuyo cuerpo JSON está malformado o no se
     * puede convertir al tipo esperado (por ejemplo, una cadena donde
     * se esperaba un número, o un valor de enum desconocido).
     *
     * @param ex      excepción lanzada por Spring al deserializar.
     * @param request petición HTTP que la disparó.
     * @return respuesta HTTP 400 con un mensaje claro.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(
            final HttpMessageNotReadableException ex,
            final WebRequest request) {

        LOG.warn("JSON inválido en la petición: {}", ex.getMostSpecificCause().getMessage());

        final ErrorResponse cuerpo = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "El cuerpo de la petición tiene un formato JSON inválido o "
                        + "algún campo no coincide con el tipo esperado.",
                extraerRuta(request));

        return ResponseEntity.badRequest().body(cuerpo);
    }

    /**
     * Maneja cualquier otra excepción no contemplada arriba. Sirve como
     * red de seguridad: cualquier fallo inesperado del backend se
     * convierte en un mensaje legible para el frontend en lugar de un
     * stacktrace.
     *
     * @param ex      excepción inesperada.
     * @param request petición HTTP que la disparó.
     * @return respuesta HTTP 500 con un mensaje genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarCualquierError(
            final Exception ex,
            final WebRequest request) {

        LOG.error("Error inesperado en el backend", ex);

        final ErrorResponse cuerpo = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un error inesperado en el servidor: " + ex.getMessage(),
                extraerRuta(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
    }

    // ---------------------------------------------------------------------
    // Métodos privados
    // ---------------------------------------------------------------------

    /**
     * Extrae la ruta del endpoint a partir del objeto WebRequest.
     * @param request petición HTTP.
     * @return ruta sin el prefijo {@code uri=} que añade Spring.
     */
    private String extraerRuta(final WebRequest request) {
        final String descripcion = request.getDescription(false);
        if (descripcion != null && descripcion.startsWith("uri=")) {
            return descripcion.substring(4);
        }
        return descripcion;
    }
}
