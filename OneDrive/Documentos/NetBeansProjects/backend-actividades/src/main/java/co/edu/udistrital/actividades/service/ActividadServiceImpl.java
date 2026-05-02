package co.edu.udistrital.actividades.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.actividades.model.Actividad;
import co.edu.udistrital.actividades.repository.ActividadRepository;

/**
 * Implementación por defecto de {@link IActividadService}.
 *
 * <p>Esta clase concentra toda la lógica de negocio relacionada con la
 * entidad {@link Actividad}. Es el <em>único</em> componente del sistema
 * que la conoce; tanto controladores como repositorios desconocen las
 * reglas que aquí se aplican.</p>
 *
 * <p>Se inyecta el {@link ActividadRepository} a través del constructor
 * (inyección por constructor, recomendada por Spring), lo que facilita
 * las pruebas unitarias y respeta el principio de Inversión de
 * Dependencias.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
@Service
@Transactional
public class ActividadServiceImpl implements IActividadService {

    /** Repositorio para acceder a la persistencia de actividades. */
    private final ActividadRepository actividadRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param actividadRepository repositorio JPA inyectado por Spring.
     */
    public ActividadServiceImpl(final ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Implementación: valida que la actividad no sea nula y que las
     * fechas sean coherentes; el id se fuerza a {@code null} para que la
     * base de datos genere uno nuevo.</p>
     */
    @Override
    public Actividad insertar(final Actividad actividad) {
        validarActividad(actividad);
        actividad.setId(null); // Se asegura inserción, no actualización.
        return actividadRepository.save(actividad);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Actividad> consultarTodas() {
        return actividadRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Actividad> consultarPorId(final Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return actividadRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Implementación: realiza una actualización parcial sólo sobre los
     * campos no nulos del objeto {@code datosNuevos}. De esa manera, el
     * cliente puede enviar únicamente los atributos que desea cambiar.</p>
     */
    @Override
    public Optional<Actividad> modificar(final Long id, final Actividad datosNuevos) {
        if (id == null || datosNuevos == null) {
            return Optional.empty();
        }
        return actividadRepository.findById(id).map(existente -> {
            if (datosNuevos.getTitulo() != null) {
                existente.setTitulo(datosNuevos.getTitulo());
            }
            if (datosNuevos.getDescripcion() != null) {
                existente.setDescripcion(datosNuevos.getDescripcion());
            }
            if (datosNuevos.getFechaInicio() != null) {
                existente.setFechaInicio(datosNuevos.getFechaInicio());
            }
            if (datosNuevos.getFechaTerminacion() != null) {
                existente.setFechaTerminacion(datosNuevos.getFechaTerminacion());
            }
            if (datosNuevos.getTipoActividad() != null) {
                existente.setTipoActividad(datosNuevos.getTipoActividad());
            }
            if (datosNuevos.getIdQuehacer() != null) {
                existente.setIdQuehacer(datosNuevos.getIdQuehacer());
            }
            if (datosNuevos.getIdTutor() != null) {
                existente.setIdTutor(datosNuevos.getIdTutor());
            }
            if (datosNuevos.getIdHijo() != null) {
                existente.setIdHijo(datosNuevos.getIdHijo());
            }
            // Tras los cambios, validamos que las fechas sigan siendo coherentes.
            validarFechas(existente);
            return actividadRepository.save(existente);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean borrar(final Long id) {
        if (id == null || !actividadRepository.existsById(id)) {
            return false;
        }
        actividadRepository.deleteById(id);
        return true;
    }

    // ---------------------------------------------------------------------
    // Métodos privados de validación
    // ---------------------------------------------------------------------

    /**
     * Verifica las invariantes mínimas de una actividad antes de
     * persistirla.
     *
     * @param actividad la actividad a validar.
     * @throws IllegalArgumentException si la actividad es {@code null} o
     *                                  las fechas son inconsistentes.
     */
    private void validarActividad(final Actividad actividad) {
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula");
        }
        validarFechas(actividad);
    }

    /**
     * Valida que la fecha de terminación no sea anterior a la de inicio.
     *
     * @param actividad actividad cuyas fechas se validan.
     * @throws IllegalArgumentException si las fechas son inconsistentes.
     */
    private void validarFechas(final Actividad actividad) {
        if (actividad.getFechaInicio() != null
                && actividad.getFechaTerminacion() != null
                && actividad.getFechaTerminacion().isBefore(actividad.getFechaInicio())) {
            throw new IllegalArgumentException(
                    "La fecha de terminación no puede ser anterior a la de inicio");
        }
    }
}
