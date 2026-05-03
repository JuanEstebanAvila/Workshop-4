package co.edu.udistrital.actividades.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.actividades.model.Actividad;
import co.edu.udistrital.actividades.service.IActividadService;
import jakarta.validation.Valid;

/**
 * Controlador REST que expone los microservicios CRUD sobre la entidad
 * {@link Actividad}.
 *
 * <p>Esta clase pertenece a la <em>capa de Controlador</em> dentro de la
 * arquitectura MVC. Su única responsabilidad es:</p>
 * <ol>
 *   <li>Recibir las peticiones HTTP entrantes.</li>
 *   <li>Validar los datos de entrada (delegando en Bean Validation).</li>
 *   <li>Delegar la lógica de negocio en la capa de Servicio.</li>
 *   <li>Retornar la respuesta apropiada con el código HTTP correcto.</li>
 * </ol>
 *
 * <p>NO contiene lógica de negocio. NO accede directamente a la base de
 * datos. Cumple así con el principio SOLID de Responsabilidad Única.</p>
 *
 * <p>Anotaciones clave:</p>
 * <ul>
 *   <li>{@code @RestController} — combina {@code @Controller} y
 *       {@code @ResponseBody}; los retornos se serializan a JSON.</li>
 *   <li>{@code @RequestMapping} — define el prefijo común de todas las
 *       rutas de este controlador.</li>
 *   <li>{@code @CrossOrigin} — declara la política CORS aplicable; por
 *       requerimiento del taller se EXPRESA aunque no se ponga en uso
 *       todavía. Permite peticiones desde cualquier origen.</li>
 * </ul>
 *
 * <h2>Endpoints expuestos</h2>
 * <table border="1">
 *   <caption>Tabla de endpoints REST</caption>
 *   <tr><th>Método</th><th>URL</th><th>Acción</th></tr>
 *   <tr><td>POST</td>   <td>/api/actividades</td>      <td>Insertar actividad.</td></tr>
 *   <tr><td>GET</td>    <td>/api/actividades</td>      <td>Consultar todas.</td></tr>
 *   <tr><td>GET</td>    <td>/api/actividades/{id}</td> <td>Consultar por id.</td></tr>
 *   <tr><td>PUT</td>    <td>/api/actividades/{id}</td> <td>Modificar.</td></tr>
 *   <tr><td>DELETE</td> <td>/api/actividades/{id}</td> <td>Borrar.</td></tr>
 * </table>
 *
 * @author  Taller 4 - Programación Avanzada
 * @version 1.0.1
 */
@RestController
@RequestMapping(value = "/api/actividades", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", methods = {
        org.springframework.web.bind.annotation.RequestMethod.GET,
        org.springframework.web.bind.annotation.RequestMethod.POST,
        org.springframework.web.bind.annotation.RequestMethod.PUT,
        org.springframework.web.bind.annotation.RequestMethod.DELETE,
        org.springframework.web.bind.annotation.RequestMethod.OPTIONS
})
public class ActividadRestController {

    /** Servicio que encapsula la lógica de negocio. Inyectado por constructor. */
    private final IActividadService actividadService;

    /**
     * Constructor con inyección de dependencias.
     * @param actividadService servicio de actividades inyectado por Spring.
     */
    public ActividadRestController(final IActividadService actividadService) {
        this.actividadService = actividadService;
    }

    /**
     * Microservicio: <strong>Insertar actividad</strong>.
     *
     * <p>Recibe una actividad en formato JSON, la valida y la persiste.</p>
     *
     * @param actividad datos de la actividad a crear (sin id).
     * @return {@code 201 Created} con la actividad persistida y su id;
     *         {@code 400 Bad Request} si los datos son inválidos.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertar(@Valid @RequestBody final Actividad actividad) {
        try {
            final Actividad creada = actividadService.insertar(actividad);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Microservicio: <strong>Consultar todas las actividades</strong>.
     *
     * @return {@code 200 OK} con la lista (posiblemente vacía).
     */
    @GetMapping
    public ResponseEntity<List<Actividad>> consultarTodas() {
        return ResponseEntity.ok(actividadService.consultarTodas());
    }

    /**
     * Microservicio: <strong>Consultar una actividad por id</strong>.
     *
     * @param id identificador de la actividad.
     * @return {@code 200 OK} con la actividad si existe;
     *         {@code 404 Not Found} si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Actividad> consultarPorId(@PathVariable final Long id) {
        return actividadService.consultarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Microservicio: <strong>Modificar una actividad</strong>.
     *
     * <p>Realiza una actualización parcial: sólo se modifican los campos
     * presentes (no nulos) en el cuerpo de la petición.</p>
     *
     * @param id          identificador de la actividad a modificar.
     * @param datosNuevos campos a actualizar.
     * @return {@code 200 OK} con la actividad ya modificada;
     *         {@code 404 Not Found} si no existe;
     *         {@code 400 Bad Request} si los datos son inválidos.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@PathVariable final Long id,
                                       @RequestBody final Actividad datosNuevos) {
        try {
            return actividadService.modificar(id, datosNuevos)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Microservicio: <strong>Borrar una actividad</strong>.
     *
     * @param id identificador de la actividad a eliminar.
     * @return {@code 204 No Content} si se eliminó con éxito;
     *         {@code 404 Not Found} si no existía.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable final Long id) {
        if (actividadService.borrar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
