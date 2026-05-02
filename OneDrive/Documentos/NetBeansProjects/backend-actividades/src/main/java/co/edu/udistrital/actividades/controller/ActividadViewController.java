package co.edu.udistrital.actividades.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.udistrital.actividades.service.IActividadService;

/**
 * Controlador MVC con renderización dinámica de HTML mediante Thymeleaf.
 *
 * <p>Permite probar el backend directamente desde un navegador (sin
 * requerir el frontend separado), tal como exige el enunciado del taller:
 * <em>"el backend se probará haciendo uso de cada uno de los endpoint,
 * consultados en un navegador"</em>.</p>
 *
 * <p>Las vistas Thymeleaf renderizan páginas HTML en {@code src/main/
 * resources/templates/}. Los datos del modelo se inyectan al contexto y
 * se acceden con la sintaxis {@code th:*} en las plantillas.</p>
 *
 * @author Grupo Taller 4 - Programación Avanzada
 * @version 1.0.0
 */
@Controller
@RequestMapping("/vista")
public class ActividadViewController {

    /** Servicio de actividades. */
    private final IActividadService actividadService;

    /**
     * Constructor con inyección de dependencias.
     * @param actividadService servicio inyectado por Spring.
     */
    public ActividadViewController(final IActividadService actividadService) {
        this.actividadService = actividadService;
    }

    /**
     * Renderiza la página de inicio con la lista completa de actividades.
     *
     * @param model modelo expuesto a la plantilla.
     * @return nombre lógico de la plantilla {@code index.html}.
     */
    @GetMapping
    public String paginaInicio(final Model model) {
        model.addAttribute("actividades", actividadService.consultarTodas());
        return "index";
    }

    /**
     * Renderiza la lista completa de actividades.
     *
     * @param model modelo expuesto a la plantilla.
     * @return nombre lógico de la plantilla {@code lista.html}.
     */
    @GetMapping("/actividades")
    public String listarActividades(final Model model) {
        model.addAttribute("actividades", actividadService.consultarTodas());
        return "lista";
    }

    /**
     * Renderiza el detalle de una actividad concreta.
     *
     * @param id    identificador de la actividad.
     * @param model modelo expuesto a la plantilla.
     * @return nombre lógico de la plantilla {@code detalle.html} si la
     *         actividad existe, o {@code no-encontrada.html} en caso
     *         contrario.
     */
    @GetMapping("/actividades/{id}")
    public String verDetalle(@PathVariable final Long id, final Model model) {
        return actividadService.consultarPorId(id)
                .map(act -> {
                    model.addAttribute("actividad", act);
                    return "detalle";
                })
                .orElseGet(() -> {
                    model.addAttribute("idBuscado", id);
                    return "no-encontrada";
                });
    }
}
