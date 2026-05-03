package co.edu.udistrital.actividades.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.actividades.dto.ActividadDTO;
import co.edu.udistrital.actividades.dto.ActividadResponse;
import co.edu.udistrital.actividades.exception.ActividadNoEncontradaException;
import co.edu.udistrital.actividades.exception.DatosInvalidosException;
import co.edu.udistrital.actividades.model.Actividad;
import co.edu.udistrital.actividades.repository.ActividadRepository;

/**
 * Implementación por defecto de {@link IActividadService}.
 *
 * <p>Esta clase concentra toda la lógica de negocio relacionada con la
 * entidad Actividad. Es el <em>único</em> componente del sistema que
 * conoce las reglas; tanto controladores como repositorios desconocen
 * lo que aquí se aplica.</p>
 *
 * <p>Responsabilidades:</p>
 * <ol>
 *   <li>Convertir el DTO de entrada en entidad para la persistencia.</li>
 *   <li>Aplicar las reglas de negocio (validación de fechas, búsqueda,
 *       etc.).</li>
 *   <li>Convertir las entidades persistidas en Response para devolver
 *       al controlador.</li>
 *   <li>Lanzar excepciones tipadas cuando una operación no termina
 *       satisfactoriamente.</li>
 * </ol>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.1.0
 */
@Service
@Transactional
public class ActividadServiceImpl implements IActividadService {

    /** Repositorio para acceder a la persistencia. */
    private final ActividadRepository actividadRepository;

    /**
     * Constructor con inyección de dependencias.
     * @param actividadRepository repositorio JPA inyectado por Spring.
     */
    public ActividadServiceImpl(final ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActividadResponse insertar(final ActividadDTO dto) {
        if (dto == null) {
            throw new DatosInvalidosException("La actividad recibida está vacía.");
        }
        validarFechas(dto.getFechaInicio(), dto.getFechaTerminacion());

        final Actividad entidad = construirEntidadDesdeDTO(dto);
        final Actividad persistida = actividadRepository.save(entidad);
        return ActividadResponse.desdeEntidad(persistida);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActividadResponse> consultarTodas() {
        return actividadRepository.findAll().stream()
                .map(ActividadResponse::desdeEntidad)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ActividadResponse consultarPorId(final Long id) {
        if (id == null) {
            throw new DatosInvalidosException("El identificador no puede ser nulo.");
        }
        final Actividad entidad = actividadRepository.findById(id)
                .orElseThrow(() -> new ActividadNoEncontradaException(id));
        return ActividadResponse.desdeEntidad(entidad);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActividadResponse modificar(final Long id, final ActividadDTO dto) {
        if (id == null) {
            throw new DatosInvalidosException("El identificador no puede ser nulo.");
        }
        if (dto == null) {
            throw new DatosInvalidosException("Los datos a modificar no pueden estar vacíos.");
        }

        final Actividad existente = actividadRepository.findById(id)
                .orElseThrow(() -> new ActividadNoEncontradaException(id));

        // Aplicar cambios sólo en campos no nulos del DTO (modificación parcial).
        if (dto.getTitulo() != null)           { existente.setTitulo(dto.getTitulo()); }
        if (dto.getDescripcion() != null)      { existente.setDescripcion(dto.getDescripcion()); }
        if (dto.getFechaInicio() != null)      { existente.setFechaInicio(dto.getFechaInicio()); }
        if (dto.getFechaTerminacion() != null) { existente.setFechaTerminacion(dto.getFechaTerminacion()); }
        if (dto.getTipoActividad() != null)    { existente.setTipoActividad(dto.getTipoActividad()); }
        if (dto.getIdQuehacer() != null)       { existente.setIdQuehacer(dto.getIdQuehacer()); }
        if (dto.getIdTutor() != null)          { existente.setIdTutor(dto.getIdTutor()); }
        if (dto.getIdHijo() != null)           { existente.setIdHijo(dto.getIdHijo()); }

        validarFechas(existente.getFechaInicio(), existente.getFechaTerminacion());

        final Actividad guardada = actividadRepository.save(existente);
        return ActividadResponse.desdeEntidad(guardada);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void borrar(final Long id) {
        if (id == null) {
            throw new DatosInvalidosException("El identificador no puede ser nulo.");
        }
        if (!actividadRepository.existsById(id)) {
            throw new ActividadNoEncontradaException(id);
        }
        actividadRepository.deleteById(id);
    }

    // ---------------------------------------------------------------------
    // Métodos privados
    // ---------------------------------------------------------------------

    /**
     * Construye una entidad nueva a partir de los datos del DTO.
     * @param dto datos de entrada.
     * @return entidad lista para persistir.
     */
    private Actividad construirEntidadDesdeDTO(final ActividadDTO dto) {
        return new Actividad(
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getFechaInicio(),
                dto.getFechaTerminacion(),
                dto.getTipoActividad(),
                dto.getIdQuehacer(),
                dto.getIdTutor(),
                dto.getIdHijo());
    }

    /**
     * Verifica que la fecha de terminación no sea anterior a la de inicio.
     *
     * @param inicio fecha de inicio.
     * @param fin    fecha de terminación.
     * @throws DatosInvalidosException si las fechas son inconsistentes.
     */
    private void validarFechas(final java.time.LocalDate inicio,
                               final java.time.LocalDate fin) {
        if (inicio != null && fin != null && fin.isBefore(inicio)) {
            throw new DatosInvalidosException(
                    "La fecha de terminación (" + fin + ") no puede ser anterior "
                  + "a la de inicio (" + inicio + ").");
        }
    }
}
