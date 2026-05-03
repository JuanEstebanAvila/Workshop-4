package co.edu.udistrital.actividades.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.edu.udistrital.actividades.model.Actividad;
import co.edu.udistrital.actividades.model.TipoActividad;

/**
 * Objeto de respuesta (Response) para la entidad Actividad.
 *
 * <p>Esta clase representa los datos que el backend devuelve al frontend
 * después de procesar una operación. A diferencia del {@link ActividadDTO},
 * este objeto:</p>
 * <ul>
 *   <li>No tiene anotaciones de Bean Validation (porque son datos
 *       confiables, ya validados y persistidos).</li>
 *   <li>Incluye el campo {@code id} (que sí existe ya que la actividad
 *       fue persistida y la base de datos le asignó uno).</li>
 *   <li>Está listo para ser consumido por el frontend tanto en formato
 *       JSON (REST) como dentro de plantillas Thymeleaf.</li>
 * </ul>
 *
 * <p>Mantener un Response separado de la entidad protege al modelo de
 * persistencia: si la entidad cambia (por ejemplo, agregamos un campo
 * de auditoría como {@code creadoPor}), el contrato con el frontend no
 * se rompe automáticamente. Aquí se decide qué se expone.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
public class ActividadResponse {

    /** Identificador único asignado por la base de datos. */
    private Long id;

    /** Título de la actividad. */
    private String titulo;

    /** Descripción detallada. */
    private String descripcion;

    /** Fecha de inicio. */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    /** Fecha de terminación. */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaTerminacion;

    /** Categoría de la actividad. */
    private TipoActividad tipoActividad;

    /** Identificador del quehacer asociado. */
    private Long idQuehacer;

    /** Identificador del tutor que asignó la actividad. */
    private Long idTutor;

    /** Identificador del hijo a cargo. */
    private Long idHijo;

    /**
     * Constructor sin argumentos requerido por Jackson para serializar
     * el objeto a JSON.
     */
    public ActividadResponse() {
        // Requerido por Jackson y por Thymeleaf.
    }

    /**
     * Constructor parametrizado.
     *
     * @param id               identificador único.
     * @param titulo           título.
     * @param descripcion      descripción.
     * @param fechaInicio      fecha de inicio.
     * @param fechaTerminacion fecha de terminación.
     * @param tipoActividad    categoría.
     * @param idQuehacer       identificador del quehacer.
     * @param idTutor          identificador del tutor.
     * @param idHijo           identificador del hijo.
     */
    public ActividadResponse(final Long id,
                             final String titulo,
                             final String descripcion,
                             final LocalDate fechaInicio,
                             final LocalDate fechaTerminacion,
                             final TipoActividad tipoActividad,
                             final Long idQuehacer,
                             final Long idTutor,
                             final Long idHijo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaTerminacion = fechaTerminacion;
        this.tipoActividad = tipoActividad;
        this.idQuehacer = idQuehacer;
        this.idTutor = idTutor;
        this.idHijo = idHijo;
    }

    /**
     * Método factoría: construye un Response a partir de una entidad
     * persistida. Concentra en un solo lugar la conversión
     * Entidad → Response, evitando duplicación.
     *
     * @param entidad entidad recuperada de la base de datos.
     * @return un nuevo {@link ActividadResponse} con los mismos datos,
     *         o {@code null} si la entidad es {@code null}.
     */
    public static ActividadResponse desdeEntidad(final Actividad entidad) {
        if (entidad == null) {
            return null;
        }
        return new ActividadResponse(
                entidad.getId(),
                entidad.getTitulo(),
                entidad.getDescripcion(),
                entidad.getFechaInicio(),
                entidad.getFechaTerminacion(),
                entidad.getTipoActividad(),
                entidad.getIdQuehacer(),
                entidad.getIdTutor(),
                entidad.getIdHijo());
    }

    // ---------------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------------

    /** @return identificador único. */
    public Long getId() {
        return id;
    }

    /** @param id identificador a asignar. */
    public void setId(final Long id) {
        this.id = id;
    }

    /** @return título. */
    public String getTitulo() {
        return titulo;
    }

    /** @param titulo nuevo título. */
    public void setTitulo(final String titulo) {
        this.titulo = titulo;
    }

    /** @return descripción. */
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

    /** @return identificador del quehacer. */
    public Long getIdQuehacer() {
        return idQuehacer;
    }

    /** @param idQuehacer nuevo identificador del quehacer. */
    public void setIdQuehacer(final Long idQuehacer) {
        this.idQuehacer = idQuehacer;
    }

    /** @return identificador del tutor. */
    public Long getIdTutor() {
        return idTutor;
    }

    /** @param idTutor nuevo identificador del tutor. */
    public void setIdTutor(final Long idTutor) {
        this.idTutor = idTutor;
    }

    /** @return identificador del hijo. */
    public Long getIdHijo() {
        return idHijo;
    }

    /** @param idHijo nuevo identificador del hijo. */
    public void setIdHijo(final Long idHijo) {
        this.idHijo = idHijo;
    }

    /**
     * Representación textual del Response para depuración.
     * @return cadena con los campos principales.
     */
    @Override
    public String toString() {
        return "ActividadResponse{"
                + "id=" + id
                + ", titulo='" + titulo + '\''
                + ", tipoActividad=" + tipoActividad
                + '}';
    }
}
