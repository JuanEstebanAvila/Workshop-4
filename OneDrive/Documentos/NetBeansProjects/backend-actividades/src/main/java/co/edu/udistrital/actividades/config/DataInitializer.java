package co.edu.udistrital.actividades.config;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.udistrital.actividades.model.Actividad;
import co.edu.udistrital.actividades.model.TipoActividad;
import co.edu.udistrital.actividades.repository.ActividadRepository;

/**
 * Componente de inicialización de datos.
 *
 * <p>El enunciado del Taller No 4 indica:
 * <em>"desarrolle la inclusión de varias actividades a la base de datos,
 * para poder realizar las funciones de los demás microservicios"</em>.</p>
 *
 * <p>Esta clase se ejecuta una sola vez al arrancar la aplicación e
 * inserta un conjunto inicial de actividades de ejemplo, basadas en las
 * sugerencias por edad descritas en el documento del taller (guardar
 * juguetes, hacer la cama, doblar ropa, lavar el auto, etc.).</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
@Configuration
public class DataInitializer {

    /** Logger SLF4J para registrar el proceso de carga inicial. */
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Constructor por defecto.
     */
    public DataInitializer() {
        // Sin estado interno; el bean se construye al arrancar Spring.
    }

    /**
     * Define un {@link CommandLineRunner} que inserta actividades de
     * ejemplo si la base de datos está vacía.
     *
     * @param repositorio repositorio inyectado para persistir los datos.
     * @return el runner ejecutado tras el arranque del contexto Spring.
     */
    @Bean
    public CommandLineRunner cargarDatosIniciales(final ActividadRepository repositorio) {
        return args -> {
            if (repositorio.count() > 0) {
                LOG.info("La base de datos ya contiene actividades; se omite la carga inicial.");
                return;
            }

            LOG.info("Cargando actividades de ejemplo en la base de datos H2...");

            repositorio.save(new Actividad(
                    "Guardar los juguetes",
                    "Recoger los juguetes del salón y guardarlos en su caja correspondiente.",
                    LocalDate.now(),
                    LocalDate.now().plusDays(1),
                    TipoActividad.FISICA,
                    1L, 100L, 200L));

            repositorio.save(new Actividad(
                    "Hacer la cama",
                    "Tender la cama todas las mañanas antes de salir al colegio.",
                    LocalDate.now(),
                    LocalDate.now().plusDays(7),
                    TipoActividad.FISICA,
                    2L, 100L, 201L));

            repositorio.save(new Actividad(
                    "Doblar la ropa limpia",
                    "Doblar la ropa que sale de la lavadora y organizarla por tipo.",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(2),
                    TipoActividad.FISICA,
                    3L, 100L, 201L));

            repositorio.save(new Actividad(
                    "Acompañar a regar las plantas",
                    "Ayudar a su hermano menor a regar las plantas del balcón con cuidado.",
                    LocalDate.now(),
                    LocalDate.now().plusDays(3),
                    TipoActividad.ACOMPANAMIENTO,
                    4L, 101L, 200L));

            repositorio.save(new Actividad(
                    "Supervisar la mochila escolar",
                    "Verificar cada noche que la mochila contenga todos los útiles para el día siguiente.",
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    TipoActividad.SUPERVISION,
                    5L, 101L, 202L));

            repositorio.save(new Actividad(
                    "Decorar la mesa para la cena",
                    "Diseñar una decoración temática para la mesa familiar del fin de semana.",
                    LocalDate.now().plusDays(2),
                    LocalDate.now().plusDays(4),
                    TipoActividad.CREATIVIDAD,
                    6L, 101L, 202L));

            repositorio.save(new Actividad(
                    "Lavar el auto familiar",
                    "Lavar el exterior del auto y aspirar el interior con ayuda del tutor.",
                    LocalDate.now().plusDays(3),
                    LocalDate.now().plusDays(4),
                    TipoActividad.FISICA,
                    7L, 100L, 203L));

            LOG.info("Carga inicial completada: {} actividades insertadas.", repositorio.count());
        };
    }
}
