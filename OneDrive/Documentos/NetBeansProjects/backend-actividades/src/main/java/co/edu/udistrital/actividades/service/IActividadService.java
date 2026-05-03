package co.edu.udistrital.actividades.service;

import java.util.List;
import java.util.Optional;

import co.edu.udistrital.actividades.model.Actividad;

/**
 * Contrato de la capa de Servicio para la entidad {@link Actividad}.
 *
 * <p>Esta interfaz define <em>qué</em> operaciones de negocio se ofrecen,
 * sin atarse a <em>cómo</em> se implementan. La separación contrato/
 * implementación es la materialización del principio SOLID de
 * <strong>Inversión de Dependencias</strong> (DIP) y permite a los
 * controladores depender de la abstracción y no de una clase concreta.</p>
 *
 * <p>La capa de Servicio orquesta las operaciones necesarias para cumplir
 * cada funcionalidad y es la única que debe contener lógica de negocio.
 * Se comunica con la capa de Repositorio para persistir o recuperar datos
 * y, en el futuro, podría comunicarse con otros microservicios.</p>
 *
 * @author Taller 4 - Programación Avanzada
 * @version 1.0.1
 */
public interface IActividadService {

    /**
     * Persiste una nueva actividad en la base de datos.
     *
     * <p>Reglas de negocio aplicadas:</p>
     * <ul>
     *   <li>La fecha de terminación NO puede ser anterior a la de inicio.</li>
     *   <li>Si el {@code id} viene asignado, se ignora (lo asigna la BD).</li>
     * </ul>
     *
     * @param actividad la actividad a guardar (sin id).
     * @return la actividad persistida con su {@code id} generado.
     * @throws IllegalArgumentException si la actividad es {@code null} o
     *                                  las fechas son inconsistentes.
     */
    Actividad insertar(Actividad actividad);

    /**
     * Recupera todas las actividades almacenadas.
     * @return lista (posiblemente vacía) con todas las actividades.
     */
    List<Actividad> consultarTodas();

    /**
     * Busca una actividad por su identificador único.
     * @param id identificador de la actividad.
     * @return {@link Optional} que contiene la actividad si existe;
     *         vacío en caso contrario.
     */
    Optional<Actividad> consultarPorId(Long id);

    /**
     * Modifica una actividad existente con los datos suministrados.
     *
     * <p>Sólo se actualizan los campos no nulos de {@code datosNuevos},
     * permitiendo modificaciones parciales sin sobrescribir información.</p>
     *
     * @param id           identificador de la actividad a modificar.
     * @param datosNuevos  objeto con los campos a actualizar.
     * @return {@link Optional} con la actividad ya modificada, o vacío
     *         si no se encontró ninguna actividad con ese id.
     * @throws IllegalArgumentException si las fechas resultantes son
     *                                  inconsistentes.
     */
    Optional<Actividad> modificar(Long id, Actividad datosNuevos);

    /**
     * Elimina una actividad por su identificador.
     * @param id identificador de la actividad a eliminar.
     * @return {@code true} si la actividad existía y fue eliminada;
     *         {@code false} si no se encontró.
     */
    boolean borrar(Long id);
}
