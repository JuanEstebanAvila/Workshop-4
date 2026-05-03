/* =========================================================================
   CLIENTE DEL BACKEND · FRONTEND ACTIVIDADES DEL HOGAR
   Taller No 4 - Programación Avanzada · Universidad Distrital
   --------------------------------------------------------------------------
   Reglas estrictas (enunciado del taller):
     - PROHIBIDO usar alert(), confirm(), prompt() o ventanas emergentes.
     - PROHIBIDO usar console.log para mensajes destinados al usuario.
     - TODO el feedback debe renderizarse en elementos HTML de la página.

   Manejo de errores:
     El backend devuelve los errores como JSON estructurado con la forma:
       {
         "marcaTiempo": "2026-05-02 14:35:12",
         "codigo":      404,
         "estado":      "Not Found",
         "mensaje":     "No existe la actividad con id 99...",
         "ruta":        "/api/actividades/99",
         "errores":     ["..."]
       }
     Este cliente parsea esa respuesta y la muestra en los paneles HTML.
   ========================================================================= */

(function () {
    'use strict';

    /** URL base del backend Spring Boot. */
    var URL_API = 'http://localhost:8080/api/actividades';

    // ---------------------------------------------------------------------
    // UTILIDADES DE RENDERIZADO DE MENSAJES
    // ---------------------------------------------------------------------

    /**
     * Muestra un panel de mensaje en el contenedor indicado.
     * @param {string} idContenedor id del elemento donde se renderiza.
     * @param {string} tipo - 'exito' | 'error' | 'info'
     * @param {string} titulo - encabezado breve.
     * @param {string} cuerpo - texto explicativo.
     * @param {Array<string>} [detalles] - lista opcional de errores
     *                                     adicionales a mostrar.
     */
    function mostrarPanel(idContenedor, tipo, titulo, cuerpo, detalles) {
        var contenedor = document.getElementById(idContenedor);
        if (!contenedor) { return; }
        var iconos = { exito: '✓', error: '!', info: 'i' };
        contenedor.className = 'panel-mensaje ' + tipo;
        contenedor.hidden = false;

        var html = '<span class="icono">' + iconos[tipo] + '</span>' +
                   '<div class="texto">' +
                       '<strong>' + escapar(titulo) + '</strong>' +
                       escapar(cuerpo);

        // Si el backend devolvió errores adicionales (validación de varios
        // campos), los agregamos como una lista.
        if (detalles && detalles.length > 0) {
            html += '<ul style="margin-top:0.5rem;padding-left:1.2rem;">';
            for (var i = 0; i < detalles.length; i++) {
                html += '<li>' + escapar(detalles[i]) + '</li>';
            }
            html += '</ul>';
        }

        html += '</div>';
        contenedor.innerHTML = html;

        // Llevar el foco al panel para que los lectores de pantalla lo lean.
        contenedor.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }

    /**
     * Oculta un panel de mensaje.
     * @param {string} idContenedor id del elemento a ocultar.
     */
    function ocultarPanel(idContenedor) {
        var contenedor = document.getElementById(idContenedor);
        if (contenedor) { contenedor.hidden = true; }
    }

    /**
     * Escapa caracteres especiales de HTML para evitar inyecciones.
     * @param {*} valor cualquier valor a serializar.
     * @returns {string} cadena segura para inyectar como HTML.
     */
    function escapar(valor) {
        if (valor === null || valor === undefined) { return ''; }
        return String(valor)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    /**
     * Convierte un mensaje de error de fetch en texto amigable.
     * @param {Error} error error capturado.
     * @returns {string} mensaje a mostrar al usuario.
     */
    function mensajeErrorConexion(error) {
        if (error && error.message && error.message.indexOf('Failed to fetch') !== -1) {
            return 'No se pudo contactar el backend. Verifica que esté ' +
                   'corriendo en ' + URL_API + '.';
        }
        return error.message || 'Ha ocurrido un error inesperado.';
    }

    /**
     * Procesa una respuesta HTTP errónea y muestra el panel adecuado en
     * la página, parseando el JSON estructurado que envía el backend.
     *
     * @param {Response} respuesta respuesta fetch con código de error.
     * @param {string} idPanel id del panel donde mostrar el mensaje.
     */
    function procesarErrorBackend(respuesta, idPanel) {
        return respuesta.json()
            .then(function (errorJson) {
                // El backend envía la estructura ErrorResponse.
                var titulo = 'Error ' + (errorJson.codigo || respuesta.status) +
                             ' — ' + (errorJson.estado || respuesta.statusText);
                var tipo = (respuesta.status === 404) ? 'info' : 'error';
                mostrarPanel(idPanel, tipo, titulo,
                             errorJson.mensaje || 'Sin detalles disponibles.',
                             errorJson.errores);
            })
            .catch(function () {
                // Si el cuerpo no es JSON válido (caso raro), mostrar
                // un mensaje genérico con el código HTTP.
                mostrarPanel(idPanel, 'error',
                    'Error ' + respuesta.status,
                    'El backend devolvió un error inesperado.');
            });
    }

    // ---------------------------------------------------------------------
    // PÁGINA: REGISTRAR ACTIVIDAD
    // ---------------------------------------------------------------------

    /**
     * Inicializa el formulario de registro y enlaza el envío al endpoint
     * POST /api/actividades.
     */
    function inicializarRegistro() {
        var formulario = document.getElementById('form-registrar');
        if (!formulario) { return; }

        // Sugerir fechas por defecto: hoy y mañana.
        var hoy = new Date().toISOString().substring(0, 10);
        var manana = new Date(Date.now() + 86400000).toISOString().substring(0, 10);
        formulario.fechaInicio.value = hoy;
        formulario.fechaTerminacion.value = manana;

        formulario.addEventListener('submit', function (evento) {
            evento.preventDefault();
            ocultarPanel('mensaje-form');

            var datos = {
                titulo:           formulario.titulo.value.trim(),
                descripcion:      formulario.descripcion.value.trim(),
                fechaInicio:      formulario.fechaInicio.value,
                fechaTerminacion: formulario.fechaTerminacion.value,
                tipoActividad:    formulario.tipoActividad.value,
                idQuehacer:       parseInt(formulario.idQuehacer.value, 10),
                idTutor:          parseInt(formulario.idTutor.value, 10),
                idHijo:           parseInt(formulario.idHijo.value, 10)
            };

            fetch(URL_API, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(datos)
            })
            .then(function (respuesta) {
                if (respuesta.ok) {
                    return respuesta.json().then(function (creada) {
                        mostrarPanel('mensaje-form', 'exito',
                            'Actividad registrada correctamente',
                            'Se creó la actividad con id ' + creada.id + '.');
                        formulario.reset();
                        formulario.fechaInicio.value = hoy;
                        formulario.fechaTerminacion.value = manana;
                    });
                }
                return procesarErrorBackend(respuesta, 'mensaje-form');
            })
            .catch(function (error) {
                mostrarPanel('mensaje-form', 'error',
                    'Error de conexión', mensajeErrorConexion(error));
            });
        });
    }

    // ---------------------------------------------------------------------
    // PÁGINA: LISTAR ACTIVIDADES
    // ---------------------------------------------------------------------

    /**
     * Carga y renderiza todas las actividades en la cuadrícula.
     */
    function inicializarListado() {
        var cuadricula = document.getElementById('cuadricula-actividades');
        if (!cuadricula) { return; }

        cuadricula.innerHTML =
            '<p style="color:#9CA3AF;padding:2rem;">Cargando actividades…</p>';

        fetch(URL_API)
            .then(function (respuesta) {
                if (respuesta.ok) {
                    return respuesta.json().then(function (lista) {
                        renderizarTarjetas(cuadricula, lista);
                    });
                }
                cuadricula.innerHTML = '';
                return procesarErrorBackend(respuesta, 'mensaje-form');
            })
            .catch(function (error) {
                cuadricula.innerHTML = '';
                mostrarPanel('mensaje-form', 'error',
                    'No se pudo cargar el listado', mensajeErrorConexion(error));
            });
    }

    /**
     * Renderiza una lista de actividades como tarjetas en el contenedor.
     * @param {HTMLElement} contenedor donde inyectar las tarjetas.
     * @param {Array} lista arreglo de actividades.
     */
    function renderizarTarjetas(contenedor, lista) {
        if (!lista || lista.length === 0) {
            contenedor.outerHTML =
                '<div class="vacio">' +
                '<span class="icono">∅</span>' +
                '<h3>Sin actividades registradas</h3>' +
                '<p>Aún no hay actividades en la base de datos.</p>' +
                '</div>';
            return;
        }
        var html = '';
        for (var i = 0; i < lista.length; i++) {
            var a = lista[i];
            html +=
                '<article class="tarjeta-actividad">' +
                    '<span class="id-actividad">id ' + escapar(a.id) + '</span>' +
                    '<span class="etiqueta-tipo ' + escapar(a.tipoActividad) + '">' +
                        escapar(a.tipoActividad) + '</span>' +
                    '<h3>' + escapar(a.titulo) + '</h3>' +
                    '<p>' + escapar(a.descripcion) + '</p>' +
                    '<div class="pie">' +
                        '<span class="fechas">' +
                            escapar(a.fechaInicio) + ' → ' + escapar(a.fechaTerminacion) +
                        '</span>' +
                        '<span>hijo · ' + escapar(a.idHijo) + '</span>' +
                    '</div>' +
                '</article>';
        }
        contenedor.innerHTML = html;
    }

    // ---------------------------------------------------------------------
    // PÁGINA: CONSULTAR POR ID
    // ---------------------------------------------------------------------

    /**
     * Inicializa el formulario de consulta por id.
     */
    function inicializarConsulta() {
        var formulario = document.getElementById('form-consultar');
        if (!formulario) { return; }

        formulario.addEventListener('submit', function (evento) {
            evento.preventDefault();
            ocultarPanel('mensaje-form');
            var detalle = document.getElementById('detalle-actividad');
            if (detalle) { detalle.hidden = true; }

            var id = formulario.id.value.trim();
            if (!id) {
                mostrarPanel('mensaje-form', 'error',
                    'Falta el identificador',
                    'Por favor ingresa el id de la actividad a consultar.');
                return;
            }

            fetch(URL_API + '/' + encodeURIComponent(id))
                .then(function (respuesta) {
                    if (respuesta.ok) {
                        return respuesta.json().then(function (actividad) {
                            renderizarDetalle(actividad);
                        });
                    }
                    return procesarErrorBackend(respuesta, 'mensaje-form');
                })
                .catch(function (error) {
                    mostrarPanel('mensaje-form', 'error',
                        'Error de conexión', mensajeErrorConexion(error));
                });
        });
    }

    /**
     * Renderiza el detalle de una actividad.
     * @param {Object} a actividad con todos los campos.
     */
    function renderizarDetalle(a) {
        var contenedor = document.getElementById('detalle-actividad');
        if (!contenedor) { return; }
        contenedor.hidden = false;
        contenedor.innerHTML =
            '<span class="etiqueta-tipo ' + escapar(a.tipoActividad) + '">' +
                escapar(a.tipoActividad) + '</span>' +
            '<h3 class="titulo-actividad">' + escapar(a.titulo) + '</h3>' +
            '<p class="descripcion-actividad">' + escapar(a.descripcion) + '</p>' +
            '<dl>' +
                '<dt>Id</dt><dd>' + escapar(a.id) + '</dd>' +
                '<dt>Fecha de inicio</dt><dd>' + escapar(a.fechaInicio) + '</dd>' +
                '<dt>Fecha de terminación</dt><dd>' + escapar(a.fechaTerminacion) + '</dd>' +
                '<dt>Quehacer</dt><dd>' + escapar(a.idQuehacer) + '</dd>' +
                '<dt>Tutor</dt><dd>' + escapar(a.idTutor) + '</dd>' +
                '<dt>Hijo a cargo</dt><dd>' + escapar(a.idHijo) + '</dd>' +
            '</dl>';
    }

    // ---------------------------------------------------------------------
    // PÁGINA: MODIFICAR ACTIVIDAD
    // ---------------------------------------------------------------------

    /**
     * Inicializa los formularios de búsqueda y modificación.
     */
    function inicializarModificacion() {
        var formularioBusqueda = document.getElementById('form-buscar-modificar');
        var formularioEdicion  = document.getElementById('form-modificar');
        if (!formularioBusqueda || !formularioEdicion) { return; }

        formularioBusqueda.addEventListener('submit', function (evento) {
            evento.preventDefault();
            ocultarPanel('mensaje-form');
            var id = formularioBusqueda.id.value.trim();
            if (!id) { return; }

            fetch(URL_API + '/' + encodeURIComponent(id))
                .then(function (respuesta) {
                    if (respuesta.ok) {
                        return respuesta.json().then(function (actividad) {
                            formularioEdicion.hidden = false;
                            formularioEdicion.id_oculto.value      = actividad.id;
                            formularioEdicion.titulo.value           = actividad.titulo;
                            formularioEdicion.descripcion.value      = actividad.descripcion;
                            formularioEdicion.fechaInicio.value      = actividad.fechaInicio;
                            formularioEdicion.fechaTerminacion.value = actividad.fechaTerminacion;
                            formularioEdicion.tipoActividad.value    = actividad.tipoActividad;
                            formularioEdicion.idQuehacer.value       = actividad.idQuehacer;
                            formularioEdicion.idTutor.value          = actividad.idTutor;
                            formularioEdicion.idHijo.value           = actividad.idHijo;
                            mostrarPanel('mensaje-form', 'info',
                                'Actividad cargada',
                                'Modifica los campos que necesites y pulsa "Guardar cambios".');
                        });
                    }
                    formularioEdicion.hidden = true;
                    return procesarErrorBackend(respuesta, 'mensaje-form');
                })
                .catch(function (error) {
                    formularioEdicion.hidden = true;
                    mostrarPanel('mensaje-form', 'error',
                        'Error de conexión', mensajeErrorConexion(error));
                });
        });

        formularioEdicion.addEventListener('submit', function (evento) {
            evento.preventDefault();
            ocultarPanel('mensaje-form');
            var id = formularioEdicion.id_oculto.value;
            var datos = {
                titulo:           formularioEdicion.titulo.value.trim(),
                descripcion:      formularioEdicion.descripcion.value.trim(),
                fechaInicio:      formularioEdicion.fechaInicio.value,
                fechaTerminacion: formularioEdicion.fechaTerminacion.value,
                tipoActividad:    formularioEdicion.tipoActividad.value,
                idQuehacer:       parseInt(formularioEdicion.idQuehacer.value, 10),
                idTutor:          parseInt(formularioEdicion.idTutor.value, 10),
                idHijo:           parseInt(formularioEdicion.idHijo.value, 10)
            };

            fetch(URL_API + '/' + encodeURIComponent(id), {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(datos)
            })
            .then(function (respuesta) {
                if (respuesta.ok) {
                    mostrarPanel('mensaje-form', 'exito',
                        'Actividad modificada',
                        'Los cambios fueron guardados correctamente.');
                    return;
                }
                return procesarErrorBackend(respuesta, 'mensaje-form');
            })
            .catch(function (error) {
                mostrarPanel('mensaje-form', 'error',
                    'Error de conexión', mensajeErrorConexion(error));
            });
        });
    }

    // ---------------------------------------------------------------------
    // PÁGINA: BORRAR ACTIVIDAD
    // ---------------------------------------------------------------------

    /**
     * Inicializa el flujo de borrado: 1) buscar, 2) confirmar, 3) eliminar.
     */
    function inicializarBorrado() {
        var formBusqueda      = document.getElementById('form-buscar-borrar');
        var seccionConfirmacion = document.getElementById('confirmacion-borrado');
        var btnConfirmar      = document.getElementById('btn-confirmar-borrar');
        var btnCancelar       = document.getElementById('btn-cancelar-borrar');
        if (!formBusqueda || !seccionConfirmacion) { return; }

        var idActual = null;

        formBusqueda.addEventListener('submit', function (evento) {
            evento.preventDefault();
            ocultarPanel('mensaje-form');
            seccionConfirmacion.hidden = true;
            var id = formBusqueda.id.value.trim();
            if (!id) { return; }

            fetch(URL_API + '/' + encodeURIComponent(id))
                .then(function (respuesta) {
                    if (respuesta.ok) {
                        return respuesta.json().then(function (actividad) {
                            idActual = actividad.id;
                            document.getElementById('borrar-titulo').textContent = actividad.titulo;
                            document.getElementById('borrar-descripcion').textContent = actividad.descripcion;
                            document.getElementById('borrar-id').textContent = actividad.id;
                            var spanTipo = document.getElementById('borrar-tipo');
                            spanTipo.textContent = actividad.tipoActividad;
                            spanTipo.className = 'etiqueta-tipo ' + actividad.tipoActividad;
                            seccionConfirmacion.hidden = false;
                        });
                    }
                    return procesarErrorBackend(respuesta, 'mensaje-form');
                })
                .catch(function (error) {
                    mostrarPanel('mensaje-form', 'error',
                        'Error de conexión', mensajeErrorConexion(error));
                });
        });

        btnCancelar.addEventListener('click', function () {
            seccionConfirmacion.hidden = true;
            idActual = null;
            mostrarPanel('mensaje-form', 'info',
                'Borrado cancelado',
                'No se eliminó ninguna actividad.');
        });

        btnConfirmar.addEventListener('click', function () {
            if (idActual === null) { return; }
            fetch(URL_API + '/' + encodeURIComponent(idActual), { method: 'DELETE' })
                .then(function (respuesta) {
                    if (respuesta.status === 204) {
                        mostrarPanel('mensaje-form', 'exito',
                            'Actividad eliminada',
                            'La actividad con id ' + idActual + ' fue borrada correctamente.');
                        seccionConfirmacion.hidden = true;
                        formBusqueda.reset();
                        idActual = null;
                        return;
                    }
                    return procesarErrorBackend(respuesta, 'mensaje-form');
                })
                .catch(function (error) {
                    mostrarPanel('mensaje-form', 'error',
                        'Error de conexión', mensajeErrorConexion(error));
                });
        });
    }

    // ---------------------------------------------------------------------
    // ENRUTAMIENTO LIGERO: cada página llama a su inicializador
    // ---------------------------------------------------------------------
    document.addEventListener('DOMContentLoaded', function () {
        inicializarRegistro();
        inicializarListado();
        inicializarConsulta();
        inicializarModificacion();
        inicializarBorrado();
    });

})();
