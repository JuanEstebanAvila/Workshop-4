package co.edu.udistrital.actividades.exception;

/**
 * Excepción lanzada por la capa de servicio cuando los datos
 * suministrados no cumplen las reglas de negocio.
 *
 * <p>Ejemplos de uso:</p>
 * <ul>
 *   <li>La fecha de terminación es anterior a la de inicio.</li>
 *   <li>Se intenta operar con un id nulo o inválido.</li>
 *   <li>Algún campo obligatorio está ausente al modificar.</li>
 * </ul>
 *
 * <p>Cuando esta excepción se lanza, el manejador global responde con
 * un código HTTP {@code 400 Bad Request} y un cuerpo JSON estructurado.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
public class DatosInvalidosException extends RuntimeException {

    /** UID para serialización. */
    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con un mensaje descriptivo del error.
     * @param mensaje explicación del problema.
     */
    public DatosInvalidosException(final String mensaje) {
        super(mensaje);
    }

    /**
     * Construye la excepción con un mensaje y la causa original.
     * @param mensaje explicación del problema.
     * @param causa   excepción original que se está envolviendo.
     */
    public DatosInvalidosException(final String mensaje, final Throwable causa) {
        super(mensaje, causa);
    }
}
