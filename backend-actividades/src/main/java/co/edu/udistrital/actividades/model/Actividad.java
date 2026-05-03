package co.edu.udistrital.actividades.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de dominio que representa una <strong>Actividad</strong> dentro
 * de un quehacer del hogar.
 *
 * <p>Esta clase está mapeada a la tabla {@code ACTIVIDAD} de la base de
 * datos H2 mediante anotaciones JPA. Cada instancia corresponde a una
 * tarea concreta asignada por un tutor a un hijo, dentro del marco de un
 * quehacer doméstico (por ejemplo, "doblar la ropa" dentro del quehacer
 * "lavandería").</p>
 *
 * <p><strong>Importante:</strong> esta clase contiene EXCLUSIVAMENTE
 * anotaciones de persistencia (JPA). Las anotaciones de validación
 * (Bean Validation) viven en {@code dto.ActividadDTO}, que es la clase
 * que recibe los datos del frontend. Esta separación cumple con el
 * principio SOLID de Responsabilidad Única: la entidad sólo se ocupa
 * de la persistencia.</p>
 *
 * <p>Atributos exigidos por el enunciado del Taller No 4:</p>
 * <ul>
 *   <li>{@code id} — identificador único auto-generado.</li>
 *   <li>{@code titulo} — nombre breve de la actividad.</li>
 *   <li>{@code descripcion} — explicación detallada de qué se debe hacer.</li>
 *   <li>{@code fechaInicio} — fecha en la que la actividad puede comenzar.</li>
 *   <li>{@code fechaTerminacion} — fecha límite para completarla.</li>
 *   <li>{@code tipoActividad} — categoría según {@link TipoActividad}.</li>
 *   <li>{@code idQuehacer} — quehacer del hogar al que pertenece.</li>
 *   <li>{@code idTutor} — tutor que asigna la actividad.</li>
 *   <li>{@code idHijo} — hijo a cargo de ejecutarla.</li>
 * </ul>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.1.0
 */
@Entity
@Table(name = "ACTIVIDAD")
public class Actividad {

    /** Identificador único de la actividad, generado automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ACTIVIDAD")
    private Long id;

    /** Título corto y descriptivo de la actividad. */
    @Column(name = "TITULO", nullable = false, length = 120)
    private String titulo;

    /** Descripción detallada de lo que implica realizar la actividad. */
    @Column(name = "DESCRIPCION", nullable = false, length = 500)
    private String descripcion;

    /** Fecha en que la actividad puede comenzar a ser ejecutada. */
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    /** Fecha límite para que la actividad sea completada. */
    @Column(name = "FECHA_TERMINACION", nullable = false)
    private LocalDate fechaTerminacion;

    /** Tipo de actividad según la enumeración {@link TipoActividad}. */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ACTIVIDAD", nullable = false, length = 30)
    private TipoActividad tipoActividad;

    /** Identificador del quehacer del hogar al que pertenece la actividad. */
    @Column(name = "ID_QUEHACER", nullable = false)
    private Long idQuehacer;

    /** Identificador del tutor que asigna la actividad. */
    @Column(name = "ID_TUTOR", nullable = false)
    private Long idTutor;

    /** Identificador del hijo a cargo de ejecutar la actividad. */
    @Column(name = "ID_HIJO", nullable = false)
    private Long idHijo;

    /**
     * Constructor sin argumentos requerido por JPA.
     */
    public Actividad() {
        // Requerido por JPA.
    }

    /**
     * Constructor parametrizado para crear actividades en código (por
     * ejemplo desde el inicializador de datos de prueba).
     *
     * @param titulo           título corto de la actividad.
     * @param descripcion      explicación detallada.
     * @param fechaInicio      fecha de inicio.
     * @param fechaTerminacion fecha límite.
     * @param tipoActividad    categoría de la actividad.
     * @param idQuehacer       identificador del quehacer asociado.
     * @param idTutor          identificador del tutor que asigna.
     * @param idHijo           identificador del hijo a cargo.
     */
    public Actividad(final String titulo,
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

    /** @return identificador único. */
    public Long getId() { return id; }

    /** @param id identificador a asignar. */
    public void setId(final Long id) { this.id = id; }

    /** @return título. */
    public String getTitulo() { return titulo; }

    /** @param titulo nuevo título. */
    public void setTitulo(final String titulo) { this.titulo = titulo; }

    /** @return descripción. */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion nueva descripción. */
    public void setDescripcion(final String descripcion) { this.descripcion = descripcion; }

    /** @return fecha de inicio. */
    public LocalDate getFechaInicio() { return fechaInicio; }

    /** @param fechaInicio nueva fecha de inicio. */
    public void setFechaInicio(final LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    /** @return fecha de terminación. */
    public LocalDate getFechaTerminacion() { return fechaTerminacion; }

    /** @param fechaTerminacion nueva fecha de terminación. */
    public void setFechaTerminacion(final LocalDate fechaTerminacion) { this.fechaTerminacion = fechaTerminacion; }

    /** @return tipo de actividad. */
    public TipoActividad getTipoActividad() { return tipoActividad; }

    /** @param tipoActividad nuevo tipo. */
    public void setTipoActividad(final TipoActividad tipoActividad) { this.tipoActividad = tipoActividad; }

    /** @return identificador del quehacer. */
    public Long getIdQuehacer() { return idQuehacer; }

    /** @param idQuehacer nuevo id del quehacer. */
    public void setIdQuehacer(final Long idQuehacer) { this.idQuehacer = idQuehacer; }

    /** @return identificador del tutor. */
    public Long getIdTutor() { return idTutor; }

    /** @param idTutor nuevo id del tutor. */
    public void setIdTutor(final Long idTutor) { this.idTutor = idTutor; }

    /** @return identificador del hijo. */
    public Long getIdHijo() { return idHijo; }

    /** @param idHijo nuevo id del hijo. */
    public void setIdHijo(final Long idHijo) { this.idHijo = idHijo; }

    /**
     * Compara entidades por id.
     * @param o otro objeto.
     * @return true si comparten id no nulo.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Actividad)) return false;
        final Actividad that = (Actividad) o;
        return id != null && id.equals(that.id);
    }

    /** @return hash basado en el id. */
    @Override
    public int hashCode() { return Objects.hash(id); }

    /** @return representación textual legible. */
    @Override
    public String toString() {
        return "Actividad{id=" + id + ", titulo='" + titulo + "', tipo=" + tipoActividad + "}";
    }
}
