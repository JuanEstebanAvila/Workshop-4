package co.edu.udistrital.actividades.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Estructura de respuesta para errores generados por el backend.
 *
 * <p>Cuando ocurre una excepción durante el procesamiento de una
 * petición HTTP (validación fallida, recurso no encontrado, error
 * inesperado), el manejador global de excepciones la transforma en una
 * instancia de esta clase y la devuelve al frontend como JSON. Esto
 * permite que el frontend muestre un mensaje claro al usuario en
 * lugar de tener que adivinar qué pasó.</p>
 *
 * <p>Estructura del JSON devuelto al frontend:</p>
 * <pre>
 * {
 *   "marcaTiempo": "2026-05-02T14:35:12",
 *   "codigo":      404,
 *   "estado":      "Not Found",
 *   "mensaje":     "No existe la actividad con id 99",
 *   "ruta":        "/api/actividades/99",
 *   "errores":     ["..."]   // sólo si hay varios (validación)
 * }
 * </pre>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /** Marca de tiempo en que ocurrió el error. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime marcaTiempo;

    /** Código numérico HTTP (404, 400, 500, etc.). */
    private int codigo;

    /** Texto descriptivo del estado HTTP (Not Found, Bad Request...). */
    private String estado;

    /** Mensaje principal entendible para el usuario. */
    private String mensaje;

    /** Ruta del endpoint donde ocurrió el error. */
    private String ruta;

    /**
     * Lista de errores adicionales (por ejemplo, cuando hay múltiples
     * campos inválidos en una validación de DTO). Se omite del JSON si
     * está vacía gracias a {@code @JsonInclude(NON_NULL)}.
     */
    private List<String> errores;

    /**
     * Constructor sin argumentos requerido por Jackson.
     */
    public ErrorResponse() {
        // Requerido por Jackson.
    }

    /**
     * Constructor de conveniencia para errores simples.
     *
     * @param codigo  código HTTP.
     * @param estado  texto del estado HTTP.
     * @param mensaje mensaje principal.
     * @param ruta    endpoint donde ocurrió el error.
     */
    public ErrorResponse(final int codigo,
                         final String estado,
                         final String mensaje,
                         final String ruta) {
        this.marcaTiempo = LocalDateTime.now();
        this.codigo = codigo;
        this.estado = estado;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }

    /**
     * Agrega un error específico a la lista (útil para validaciones
     * de campos múltiples).
     *
     * @param error mensaje específico de error a agregar.
     */
    public void agregarError(final String error) {
        if (this.errores == null) {
            this.errores = new ArrayList<>();
        }
        this.errores.add(error);
    }

    // ---------------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------------

    /** @return marca de tiempo del error. */
    public LocalDateTime getMarcaTiempo() {
        return marcaTiempo;
    }

    /** @param marcaTiempo nueva marca de tiempo. */
    public void setMarcaTiempo(final LocalDateTime marcaTiempo) {
        this.marcaTiempo = marcaTiempo;
    }

    /** @return código HTTP. */
    public int getCodigo() {
        return codigo;
    }

    /** @param codigo nuevo código HTTP. */
    public void setCodigo(final int codigo) {
        this.codigo = codigo;
    }

    /** @return estado HTTP en texto. */
    public String getEstado() {
        return estado;
    }

    /** @param estado nuevo estado HTTP. */
    public void setEstado(final String estado) {
        this.estado = estado;
    }

    /** @return mensaje principal. */
    public String getMensaje() {
        return mensaje;
    }

    /** @param mensaje nuevo mensaje principal. */
    public void setMensaje(final String mensaje) {
        this.mensaje = mensaje;
    }

    /** @return ruta del endpoint. */
    public String getRuta() {
        return ruta;
    }

    /** @param ruta nueva ruta del endpoint. */
    public void setRuta(final String ruta) {
        this.ruta = ruta;
    }

    /** @return lista de errores adicionales (puede ser nula). */
    public List<String> getErrores() {
        return errores;
    }

    /** @param errores nueva lista de errores adicionales. */
    public void setErrores(final List<String> errores) {
        this.errores = errores;
    }
}
