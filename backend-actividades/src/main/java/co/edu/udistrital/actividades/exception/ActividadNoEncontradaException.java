package co.edu.udistrital.actividades.exception;

/**
 * Excepción lanzada cuando se solicita una actividad por su id y no
 * existe en la base de datos.
 *
 * <p>Esta excepción es <em>no comprobada</em> (extiende {@link RuntimeException})
 * para no obligar a declararla en cada firma de método. La captura y el
 * envío al frontend son responsabilidad del manejador global
 * {@code ManejadorGlobalExcepciones}.</p>
 *
 * <p>Cuando esta excepción se lanza, el manejador global responde con
 * un código HTTP {@code 404 Not Found} y un cuerpo JSON estructurado
 * con los detalles del error.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
public class ActividadNoEncontradaException extends RuntimeException {

    /** UID para serialización. */
    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con el id que no se encontró.
     * @param id identificador buscado.
     */
    public ActividadNoEncontradaException(final Long id) {
        super("No existe ninguna actividad con el id " + id + " en la base de datos.");
    }

    /**
     * Construye la excepción con un mensaje personalizado.
     * @param mensaje texto del error.
     */
    public ActividadNoEncontradaException(final String mensaje) {
        super(mensaje);
    }
}
