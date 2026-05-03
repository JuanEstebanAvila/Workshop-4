package co.edu.udistrital.actividades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de arranque de la aplicación BackEnd-Actividades.
 *
 * <p>Esta clase es el punto de entrada de la aplicación Spring Boot que expone
 * los microservicios REST sobre la entidad {@code Actividad} para el Taller
 * No 4 de Programación Avanzada (Universidad Distrital Francisco José de
 * Caldas).</p>
 *
 * <p>La anotación {@link SpringBootApplication} habilita simultáneamente:</p>
 * <ul>
 *   <li>{@code @Configuration} — la clase puede declarar beans.</li>
 *   <li>{@code @EnableAutoConfiguration} — Spring Boot configura componentes
 *       automáticamente según las dependencias del classpath.</li>
 *   <li>{@code @ComponentScan} — escanea componentes en este paquete y
 *       sub-paquetes (model, repository, service, controller, config).</li>
 * </ul>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 * @since 2026-05-01
 */
@SpringBootApplication
public class ActividadesApplication {

    /**
     * Constructor por defecto.
     * <p>No realiza inicialización adicional; Spring Boot se encarga del
     * ciclo de vida del contenedor de beans.</p>
     */
    public ActividadesApplication() {
        // Constructor vacío. Spring gestiona la instancia.
    }

    /**
     * Punto de entrada de la JVM. Delega en {@link SpringApplication#run}
     * para inicializar el contexto de Spring Boot y arrancar el servidor
     * embebido (Tomcat por defecto en el puerto 8080).
     *
     * @param args argumentos de línea de comandos pasados al iniciar la
     *             aplicación. Pueden usarse para sobrescribir propiedades
     *             (por ejemplo, {@code --server.port=9090}).
     */
    public static void main(final String[] args) {
        SpringApplication.run(ActividadesApplication.class, args);
    }
}
