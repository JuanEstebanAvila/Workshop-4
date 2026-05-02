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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
 * * <p>Se utiliza Lombok para reducir código repetitivo:</p>
 * <ul>
 *   <li>{@code @Data} — genera getters, setters y {@code toString()}.</li>
 *   <li>{@code @EqualsAndHashCode} — restringe la comparación al campo
 *       {@code id} para respetar la identidad de entidad JPA.</li>
 *   <li>{@code @NoArgsConstructor} — constructor vacío requerido por JPA.</li>
 *   <li>{@code @AllArgsConstructor} — constructor con todos los campos
 *       excepto {@code id} (usando {@code exclude}).</li>
 * </ul>
 *
 * @author Taller 4 - Programación Avanzada
 * @version 1.0.0
 */

// @Entity la registra como tabla
@Entity
@Table(name = "ACTIVIDAD")
public class Actividad {

    /** Identificador único de la actividad, generado automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //genera o asigna un id automatico al quehacer
    @Column(name = "ID_ACTIVIDAD")  //columna base de datos
    private Long id;

    /** titulo corto y descriptivo de la actividad. */
    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 120, message = "El título no puede exceder 120 caracteres")
    @Column(name = "TITULO", nullable = false, length = 120)
    private String titulo;

    /** Descripción detallada de lo que implica realizar la actividad. */
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(name = "DESCRIPCION", nullable = false, length = 500)
    private String descripcion;

    /** Fecha en que la actividad puede comenzar a ser ejecutada. */
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    /** Fecha límite para que la actividad sea completada. */
    @NotNull(message = "La fecha de terminación es obligatoria")
    @Column(name = "FECHA_TERMINACION", nullable = false)
    private LocalDate fechaTerminacion;

    /** Tipo de actividad según la enumeración {@link TipoActividad}. */
    @NotNull(message = "El tipo de actividad es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ACTIVIDAD", nullable = false, length = 30)
    private TipoActividad tipoActividad;

    /** Identificador del quehacer del hogar al que pertenece la actividad. */
    @NotNull(message = "El id del quehacer es obligatorio")
    @Column(name = "ID_QUEHACER", nullable = false)
    private Long idQuehacer;

    /** Identificador del tutor que asigna la actividad. */
    @NotNull(message = "El id del tutor es obligatorio")
    @Column(name = "ID_TUTOR", nullable = false)
    private Long idTutor;

    /** Identificador del hijo a cargo de ejecutar la actividad. */
    @NotNull(message = "El id del hijo es obligatorio")
    @Column(name = "ID_HIJO", nullable = false)
    private Long idHijo;

    /**
     * Constructor sin argumentos requerido por JPA.
     * <p>No debe usarse directamente en código de aplicación; los demás
     * objetos del dominio deben emplear el constructor parametrizado o
     * los métodos {@code set*}.</p>
     */
    public Actividad() {
        // Requerido por JPA.
    }

    /**
     * Constructor parametrizado para crear actividades en código.
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
    // Getters y Setters   "se descarta el uso de @data o similares por problemas de compatibilidad"
    // ---------------------------------------------------------------------

    /** @return identificador único de la actividad. */
    public Long getId() {
        return id;
    }

    /**
     * Asigna el identificador único.
     * <p>Normalmente NO se llama desde código de aplicación; JPA lo asigna
     * automáticamente al persistir.</p>
     * @param id identificador a asignar.
     */
    public void setId(final Long id) {
        this.id = id;
    }

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

    // ---------------------------------------------------------------------
    // Object overrides
    // ---------------------------------------------------------------------

    /**
     * Compara dos actividades por su identificador único.
     * @param o objeto a comparar.
     * @return {@code true} si comparten el mismo {@code id} no nulo.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actividad)) {
            return false;
        }
        final Actividad that = (Actividad) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Calcula el hash basado en el identificador.
     * @return hash de la entidad.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Representación textual legible de la actividad para depuración
     * y bitácora.
     * @return cadena con los campos principales.
     */
    @Override
    public String toString() {
        return "Actividad{"
                + "id=" + id
                + ", titulo='" + titulo + '\''
                + ", tipoActividad=" + tipoActividad
                + ", fechaInicio=" + fechaInicio
                + ", fechaTerminacion=" + fechaTerminacion
                + ", idQuehacer=" + idQuehacer
                + ", idTutor=" + idTutor
                + ", idHijo=" + idHijo
                + '}';
    }
}
