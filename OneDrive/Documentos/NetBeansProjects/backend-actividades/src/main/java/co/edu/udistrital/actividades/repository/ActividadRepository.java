package co.edu.udistrital.actividades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.actividades.model.Actividad;
import co.edu.udistrital.actividades.model.TipoActividad;

/**
 * Repositorio Spring Data JPA para la entidad {@link Actividad}.
 *
 * <p>Esta interfaz constituye la <em>capa de Repositorio</em> dentro de la
 * arquitectura MVC del proyecto. Su responsabilidad EXCLUSIVA es la
 * interacción con la base de datos H2; abstrae la tecnología de
 * persistencia del resto de la aplicación, cumpliendo así con el
 * principio SOLID de Inversión de Dependencias (DIP).</p>
 *
 * <p>Al extender {@link JpaRepository}, se obtienen automáticamente los
 * métodos CRUD de uso común:</p>
 * <ul>
 *   <li>{@code save(Actividad)} — insertar o actualizar.</li>
 *   <li>{@code findAll()} — listar todas las actividades.</li>
 *   <li>{@code findById(Long)} — buscar por identificador.</li>
 *   <li>{@code deleteById(Long)} — eliminar por identificador.</li>
 *   <li>{@code existsById(Long)} — verificar existencia.</li>
 * </ul>
 *
 * <p>Adicionalmente se declaran consultas derivadas por nombre de método
 * (Spring Data JPA construye el SQL automáticamente). Estas consultas son
 * útiles para futuras funcionalidades de filtrado.</p>
 * 
 * Spring genera el SQL automáticamente solo con el nombre del método. 
 * Esta capa no contiene lógica de negocio.
 *
 * @author Taller 4 - Programación Avanzada
 * @version 1.0.1
 */
@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    /**
     * Busca actividades por tipo.
     * @param tipoActividad categoría a filtrar.
     * @return lista de actividades que pertenecen al tipo dado.
     */
    List<Actividad> findByTipoActividad(TipoActividad tipoActividad);

    /**
     * Busca actividades asignadas a un hijo concreto.
     * @param idHijo identificador del hijo.
     * @return lista de actividades a su cargo.
     */
    List<Actividad> findByIdHijo(Long idHijo);

    /**
     * Busca actividades asignadas por un tutor concreto.
     * @param idTutor identificador del tutor.
     * @return lista de actividades creadas por el tutor.
     */
    List<Actividad> findByIdTutor(Long idTutor);

    /**
     * Busca actividades pertenecientes a un quehacer concreto.
     * @param idQuehacer identificador del quehacer del hogar.
     * @return lista de actividades dentro del quehacer.
     */
    List<Actividad> findByIdQuehacer(Long idQuehacer);
}
