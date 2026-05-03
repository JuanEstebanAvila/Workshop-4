package co.edu.udistrital.actividades.service;

import java.util.List;

import co.edu.udistrital.actividades.dto.ActividadDTO;
import co.edu.udistrital.actividades.dto.ActividadResponse;
import co.edu.udistrital.actividades.exception.ActividadNoEncontradaException;
import co.edu.udistrital.actividades.exception.DatosInvalidosException;

/**
 * Contrato de la capa de Servicio para la entidad Actividad.
 *
 * <p>Define <em>qué</em> operaciones de negocio se ofrecen, sin atarse a
 * <em>cómo</em> se implementan. La separación contrato/implementación
 * materializa el principio SOLID de <strong>Inversión de Dependencias</strong>
 * y permite a los controladores depender de la abstracción y no de una
 * clase concreta.</p>
 *
 * <p><strong>Política de errores:</strong> esta interfaz utiliza
 * excepciones personalizadas para informar de operaciones que no
 * terminaron bien:</p>
 * <ul>
 *   <li>{@link ActividadNoEncontradaException} cuando se busca o
 *       modifica/borra una actividad inexistente.</li>
 *   <li>{@link DatosInvalidosException} cuando los datos suministrados
 *       infringen reglas de negocio (por ejemplo, fechas inconsistentes).</li>
 * </ul>
 *
 * <p>El manejador global de excepciones del backend
 * ({@code ManejadorGlobalExcepciones}) intercepta estas excepciones y
 * las convierte en respuestas HTTP estructuradas que llegan al frontend.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.1.0
 */
public interface IActividadService {

    /**
     * Persiste una nueva actividad en la base de datos.
     *
     * @param dto datos validados de la actividad a crear.
     * @return el {@link ActividadResponse} con la actividad persistida y
     *         su id asignado.
     * @throws DatosInvalidosException si las reglas de negocio no se
     *                                 cumplen (por ejemplo, fechas
     *                                 inconsistentes).
     */
    ActividadResponse insertar(ActividadDTO dto);

    /**
     * Recupera todas las actividades almacenadas.
     * @return lista de respuestas (puede estar vacía).
     */
    List<ActividadResponse> consultarTodas();

    /**
     * Busca una actividad por su identificador único.
     * @param id identificador de la actividad.
     * @return el {@link ActividadResponse} correspondiente.
     * @throws ActividadNoEncontradaException si no existe ninguna
     *                                        actividad con ese id.
     */
    ActividadResponse consultarPorId(Long id);

    /**
     * Modifica una actividad existente con los datos suministrados.
     *
     * @param id  identificador de la actividad a modificar.
     * @param dto nuevos datos validados.
     * @return el {@link ActividadResponse} con la actividad ya modificada.
     * @throws ActividadNoEncontradaException si no existe la actividad.
     * @throws DatosInvalidosException        si las reglas de negocio
     *                                        fallan.
     */
    ActividadResponse modificar(Long id, ActividadDTO dto);

    /**
     * Elimina una actividad por su identificador.
     * @param id identificador de la actividad a eliminar.
     * @throws ActividadNoEncontradaException si no existe la actividad.
     */
    void borrar(Long id);
}
