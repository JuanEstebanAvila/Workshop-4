package co.edu.udistrital.actividades.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de las políticas CORS (Cross-Origin Resource
 * Sharing) de la aplicación.
 *
 * <p>El enunciado del Taller No 4 indica explícitamente:
 * <em>"se deben expresar las políticas CORS a manejar (aunque aún no se
 * pongan en uso)"</em>. Esta clase EXPRESA dichas políticas para que
 * cuando se conecte el frontend HTML externo, las peticiones desde otro
 * origen (otro puerto u otra máquina) sean aceptadas.</p>
 *
 * <p>Política definida:</p>
 * <ul>
 *   <li>Aplica a todas las rutas bajo {@code /api/**}.</li>
 *   <li>Permite cualquier origen ({@code *}) durante desarrollo. En
 *       producción debería restringirse a los dominios del frontend.</li>
 *   <li>Permite los métodos GET, POST, PUT, DELETE y OPTIONS.</li>
 *   <li>Permite cualquier cabecera HTTP.</li>
 *   <li>Tiempo de cacheo de la respuesta pre-flight: 1 hora.</li>
 * </ul>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
@Configuration
public class CorsConfig {

    /** Tiempo de vida de la respuesta pre-flight en segundos (1 hora). */
    private static final long MAX_AGE_PREFLIGHT = 3600L;

    /**
     * Constructor por defecto.
     */
    public CorsConfig() {
        // Sin estado interno; sólo expone el bean de configuración CORS.
    }

    /**
     * Define el bean {@link WebMvcConfigurer} que configura las políticas
     * CORS para los endpoints REST de la aplicación.
     *
     * @return un {@link WebMvcConfigurer} con la configuración CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Aplica las reglas CORS al registro central.
             * @param registry registro de mapeos CORS.
             */
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(MAX_AGE_PREFLIGHT);
            }
        };
    }
}
