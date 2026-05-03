package co.edu.udistrital.actividades.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.edu.udistrital.actividades.model.TipoActividad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) de entrada para la entidad Actividad.
 *
 * <p>Esta clase representa los datos que llegan desde el frontend al
 * backend para crear o modificar una actividad. Su responsabilidad es
 * <strong>validar</strong> que los datos recibidos cumplen las reglas
 * antes de pasar a la capa de servicio.</p>
 *
 * <p>Por qué un DTO separado de la entidad:</p>
 * <ul>
 *   <li>La entidad {@code Actividad} se queda exclusivamente con las
 *       anotaciones de JPA (no se mezcla con anotaciones de validación
 *       web), respetando el principio SOLID de Responsabilidad Única.</li>
 *   <li>El DTO no expone el campo {@code id} para entrada porque éste
 *       lo asigna automáticamente la base de datos.</li>
 *   <li>Si en el futuro la entidad cambia (campos calculados, columnas
 *       internas), el contrato con el frontend no se rompe.</li>
 *   <li>Permite recibir formatos distintos a los de la entidad (por
 *       ejemplo, fechas como cadena en formato {@code yyyy-MM-dd}).</li>
 * </ul>
 *
 * <p>Las anotaciones de Bean Validation aquí presentes ({@code @NotBlank},
 * {@code @NotNull}, {@code @Size}, {@code @Positive}) son evaluadas por
 * Spring cuando el controlador recibe la petición con la anotación
 * {@code @Valid}; si alguna falla, se lanza una excepción que el
 * manejador global convierte en un mensaje de error para el frontend.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
public class ActividadDTO {

    /** Título corto y descriptivo de la actividad. */
    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 120, message = "El título no puede exceder 120 caracteres")
    private String titulo;

    /** Descripción detallada de lo que implica realizar la actividad. */
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    /** Fecha en que la actividad puede comenzar a ser ejecutada. */
    @NotNull(message = "La fecha de inicio es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    /** Fecha límite para que la actividad sea completada. */
    @NotNull(message = "La fecha de terminación es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaTerminacion;

    /** Tipo de actividad según la enumeración {@link TipoActividad}. */
    @NotNull(message = "El tipo de actividad es obligatorio (FISICA, ACOMPANAMIENTO, SUPERVISION o CREATIVIDAD)")
    private TipoActividad tipoActividad;

    /** Identificador del quehacer del hogar al que pertenece la actividad. */
    @NotNull(message = "El id del quehacer es obligatorio")
    @Positive(message = "El id del quehacer debe ser un número positivo")
    private Long idQuehacer;

    /** Identificador del tutor que asigna la actividad. */
    @NotNull(message = "El id del tutor es obligatorio")
    @Positive(message = "El id del tutor debe ser un número positivo")
    private Long idTutor;

    /** Identificador del hijo a cargo de ejecutar la actividad. */
    @NotNull(message = "El id del hijo es obligatorio")
    @Positive(message = "El id del hijo debe ser un número positivo")
    private Long idHijo;

    /**
     * Constructor sin argumentos requerido por Jackson para deserializar
     * el JSON de la petición HTTP.
     */
    public ActividadDTO() {
        // Requerido por Jackson.
    }

    /**
     * Constructor parametrizado, útil en pruebas.
     *
     * @param titulo           título corto.
     * @param descripcion      descripción detallada.
     * @param fechaInicio      fecha de inicio.
     * @param fechaTerminacion fecha límite.
     * @param tipoActividad    categoría.
     * @param idQuehacer       quehacer asociado.
     * @param idTutor          tutor que asigna.
     * @param idHijo           hijo a cargo.
     */
    public ActividadDTO(final String titulo,
                        final String descripcion,
                        final LocalDate fechaInicio,
                        final LocalDate fechaTerminacion,
                        final TipoActividad tipoActividad,
                        final Long idQuehacer,
                        final Long idTutor,
                        final Long idHijo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaTerminacion = fechaTerminacion;
        this.tipoActividad = tipoActividad;
        this.idQuehacer = idQuehacer;
        this.idTutor = idTutor;
        this.idHijo = idHijo;
    }

    // ---------------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------------

    /** @return título de la actividad. */
    public String getTitulo() {
        return titulo;
    }

    /** @param titulo nuevo título. */
    public void setTitulo(final String titulo) {
        this.titulo = titulo;
    }

    /** @return descripción detallada. */
    public String getDescripcion() {
        return descripcion;
    }

    /** @param descripcion nueva descripción. */
    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return fecha de inicio. */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /** @param fechaInicio nueva fecha de inicio. */
    public void setFechaInicio(final LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /** @return fecha de terminación. */
    public LocalDate getFechaTerminacion() {
        return fechaTerminacion;
    }

    /** @param fechaTerminacion nueva fecha de terminación. */
    public void setFechaTerminacion(final LocalDate fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    /** @return tipo de actividad. */
    public TipoActividad getTipoActividad() {
        return tipoActividad;
    }

    /** @param tipoActividad nuevo tipo. */
    public void setTipoActividad(final TipoActividad tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    /** @return identificador del quehacer asociado. */
    public Long getIdQuehacer() {
        return idQuehacer;
    }

    /** @param idQuehacer nuevo identificador del quehacer. */
    public void setIdQuehacer(final Long idQuehacer) {
        this.idQuehacer = idQuehacer;
    }

    /** @return identificador del tutor que asigna. */
    public Long getIdTutor() {
        return idTutor;
    }

    /** @param idTutor nuevo identificador del tutor. */
    public void setIdTutor(final Long idTutor) {
        this.idTutor = idTutor;
    }

    /** @return identificador del hijo a cargo. */
    public Long getIdHijo() {
        return idHijo;
    }

    /** @param idHijo nuevo identificador del hijo. */
    public void setIdHijo(final Long idHijo) {
        this.idHijo = idHijo;
    }

    /**
     * Representación textual del DTO para depuración.
     * @return cadena con los campos principales.
     */
    @Override
    public String toString() {
        return "ActividadDTO{"
                + "titulo='" + titulo + '\''
                + ", tipoActividad=" + tipoActividad
                + ", fechaInicio=" + fechaInicio
                + ", fechaTerminacion=" + fechaTerminacion
                + '}';
    }
}
