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

import co.edu.udistrital.actividades.dto.ActividadDTO;
import co.edu.udistrital.actividades.dto.ActividadResponse;
import co.edu.udistrital.actividades.service.IActividadService;
import jakarta.validation.Valid;

/**
 * Controlador REST que expone los microservicios CRUD sobre la entidad
 * Actividad.
 *
 * <p>Esta clase pertenece a la <em>capa de Controlador</em> dentro de la
 * arquitectura MVC. Su única responsabilidad es:</p>
 * <ol>
 *   <li>Recibir las peticiones HTTP entrantes y mapear el JSON al DTO
 *       correspondiente.</li>
 *   <li>Disparar la validación con {@code @Valid} sobre el DTO.</li>
 *   <li>Delegar la lógica de negocio en la capa de Servicio.</li>
 *   <li>Retornar al frontend un {@link ActividadResponse} (o lista de
 *       ellos) con el código HTTP correcto.</li>
 * </ol>
 *
 * <p><strong>Manejo de errores:</strong> este controlador NO contiene
 * bloques {@code try/catch}. Cualquier excepción lanzada por la capa de
 * servicio (por ejemplo, {@code ActividadNoEncontradaException} o
 * {@code DatosInvalidosException}) es capturada por el componente
 * {@code ManejadorGlobalExcepciones}, que la convierte en una respuesta
 * HTTP estructurada que llega al frontend para mostrarse en pantalla.</p>
 *
 * <p>Anotaciones clave:</p>
 * <ul>
 *   <li>{@code @RestController} — combina {@code @Controller} y
 *       {@code @ResponseBody}; los retornos se serializan a JSON.</li>
 *   <li>{@code @RequestMapping} — define el prefijo común de todas las
 *       rutas.</li>
 *   <li>{@code @CrossOrigin} — declara la política CORS aplicable.</li>
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
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.1.0
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

    /** Servicio que encapsula la lógica de negocio. */
    private final IActividadService actividadService;

    /**
     * Constructor con inyección de dependencias.
     * @param actividadService servicio inyectado por Spring.
     */
    public ActividadRestController(final IActividadService actividadService) {
        this.actividadService = actividadService;
    }

    /**
     * Microservicio: <strong>Insertar actividad</strong>.
     *
     * <p>Recibe un {@link ActividadDTO} validado mediante {@code @Valid}.
     * Si la validación falla, el manejador global devolverá un 400 con
     * los detalles. Si los datos están correctos, devuelve un 201 con
     * el {@link ActividadResponse} de la actividad creada.</p>
     *
     * @param dto datos de entrada (validados automáticamente).
     * @return {@code 201 Created} con el response del recurso creado.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActividadResponse> insertar(
            @Valid @RequestBody final ActividadDTO dto) {
        final ActividadResponse creada = actividadService.insertar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**
     * Microservicio: <strong>Consultar todas las actividades</strong>.
     * @return {@code 200 OK} con la lista de respuestas.
     */
    @GetMapping
    public ResponseEntity<List<ActividadResponse>> consultarTodas() {
        return ResponseEntity.ok(actividadService.consultarTodas());
    }

    /**
     * Microservicio: <strong>Consultar una actividad por id</strong>.
     *
     * <p>Si no existe, el servicio lanza
     * {@code ActividadNoEncontradaException} y el manejador global
     * devuelve un 404 estructurado al frontend.</p>
     *
     * @param id identificador de la actividad.
     * @return {@code 200 OK} con el response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> consultarPorId(
            @PathVariable final Long id) {
        return ResponseEntity.ok(actividadService.consultarPorId(id));
    }

    /**
     * Microservicio: <strong>Modificar una actividad</strong>.
     *
     * @param id  identificador de la actividad.
     * @param dto nuevos datos validados.
     * @return {@code 200 OK} con el response actualizado.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActividadResponse> modificar(
            @PathVariable final Long id,
            @Valid @RequestBody final ActividadDTO dto) {
        return ResponseEntity.ok(actividadService.modificar(id, dto));
    }

    /**
     * Microservicio: <strong>Borrar una actividad</strong>.
     *
     * <p>Si no existe, el servicio lanza
     * {@code ActividadNoEncontradaException} y el manejador global
     * devuelve un 404 estructurado.</p>
     *
     * @param id identificador de la actividad a eliminar.
     * @return {@code 204 No Content} si se eliminó con éxito.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable final Long id) {
        actividadService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
