package co.edu.udistrital.actividades.model;

/**
 * Enumeración de los tipos de actividad contemplados en el dominio del
 * problema de los quehaceres del hogar.
 *
 * <p>Según el enunciado del Taller No 4, las actividades pueden clasificarse
 * en cuatro categorías principales:</p>
 * <ul>
 *   <li>{@link #FISICA} — Actividades que requieren esfuerzo físico
 *       (barrer, aspirar, lavar el auto).</li>
 *   <li>{@link #ACOMPANAMIENTO} — Actividades realizadas junto a otra
 *       persona (acompañar a un hermano menor, supervisar mascotas).</li>
 *   <li>{@link #SUPERVISION} — Actividades que implican vigilancia o
 *       control (revisar que la cocina quede ordenada).</li>
 *   <li>{@link #CREATIVIDAD} — Actividades que estimulan la creatividad
 *       (decorar, organizar de forma original, cocinar nuevas recetas).</li>
 * </ul>
 *
 * <p>Mantener este conjunto cerrado como enumeración garantiza la
 * consistencia de los datos almacenados y evita valores inválidos en la
 * base de datos.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
public enum TipoActividad {

    /** Actividad que requiere esfuerzo físico predominante. */
    FISICA,

    /** Actividad realizada en compañía o asistencia a otra persona. */
    ACOMPANAMIENTO,

    /** Actividad que implica supervisar o controlar otra labor. */
    SUPERVISION,

    /** Actividad que estimula la creatividad y la innovación. */
    CREATIVIDAD
}
