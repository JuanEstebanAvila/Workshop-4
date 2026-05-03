/* =========================================================================
   CLIENTE DEL BACKEND · FRONTEND ACTIVIDADES DEL HOGAR
   Taller No 4 - Programación Avanzada · Universidad Distrital
   --------------------------------------------------------------------------
   Reglas estrictas (enunciado del taller):
     - PROHIBIDO usar alert(), confirm(), prompt() o ventanas emergentes.
     - PROHIBIDO usar console.log para mensajes al usuario.
     - TODO el feedback se renderiza en elementos HTML de la página.
     - Cada endpoint tiene su sección ENTRADA (formulario) y SALIDA (tabla).
   ========================================================================= */

(function () {
    'use strict';

    var URL_API = 'http://localhost:8080/api/actividades';

    /* ------------------------------------------------------------------
       UTILIDADES GENERALES
    ------------------------------------------------------------------ */

    function escapar(v) {
        if (v === null || v === undefined) { return ''; }
        return String(v)
            .replace(/&/g, '&amp;').replace(/</g, '&lt;')
            .replace(/>/g, '&gt;').replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    function mostrarPanel(id, tipo, titulo, cuerpo, detalles) {
        var el = document.getElementById(id);
        if (!el) { return; }
        var iconos = { exito: '✓', error: '!', info: 'i' };
        el.className = 'panel-mensaje ' + tipo;
        el.hidden = false;
        var html = '<span class="icono">' + iconos[tipo] + '</span>' +
                   '<div class="texto"><strong>' + escapar(titulo) + '</strong>' +
                   escapar(cuerpo);
        if (detalles && detalles.length) {
            html += '<ul style="margin-top:0.5rem;padding-left:1.2rem;">';
            for (var i = 0; i < detalles.length; i++) {
                html += '<li>' + escapar(detalles[i]) + '</li>';
            }
            html += '</ul>';
        }
        html += '</div>';
        el.innerHTML = html;
        el.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }

    function ocultarPanel(id) {
        var el = document.getElementById(id);
        if (el) { el.hidden = true; }
    }

    function errorConexion(err) {
        if (err && err.message && err.message.indexOf('Failed to fetch') !== -1) {
            return 'No se pudo contactar el backend en ' + URL_API +
                   '. Verifica que Spring Boot esté corriendo en el puerto 8080.';
        }
        return err.message || 'Error inesperado.';
    }

    function procesarErrorBackend(respuesta, panelId) {
        return respuesta.json().then(function (err) {
            var titulo = 'Error ' + (err.codigo || respuesta.status) +
                         ' — ' + (err.estado || respuesta.statusText);
            var tipo = respuesta.status === 404 ? 'info' : 'error';
            mostrarPanel(panelId, tipo, titulo,
                         err.mensaje || 'Sin detalles.', err.errores);
        }).catch(function () {
            mostrarPanel(panelId, 'error',
                'Error ' + respuesta.status,
                'El backend devolvió un error sin cuerpo JSON.');
        });
    }

    /* ------------------------------------------------------------------
       CONSTRUCCIÓN DE FILAS DE TABLA (respuesta ActividadResponse)
       Muestra campo | tipo Java | valor retornado
    ------------------------------------------------------------------ */

    /**
     * Genera las filas HTML para un ActividadResponse completo.
     * @param {Object} a - objeto actividad retornado por el backend.
     * @returns {string} HTML de filas <tr>.
     */
    function filasActividadResponse(a) {
        var campos = [
            ['id',               'Long',          a.id],
            ['titulo',           'String',        a.titulo],
            ['descripcion',      'String',        a.descripcion],
            ['fechaInicio',      'LocalDate',     a.fechaInicio],
            ['fechaTerminacion', 'LocalDate',     a.fechaTerminacion],
            ['tipoActividad',    'TipoActividad', a.tipoActividad],
            ['idQuehacer',       'Long',          a.idQuehacer],
            ['idTutor',          'Long',          a.idTutor],
            ['idHijo',           'Long',          a.idHijo]
        ];
        return campos.map(function (c) {
            return '<tr class="fila-exito">' +
                '<td>' + escapar(c[0]) + '</td>' +
                '<td>' + escapar(c[1]) + '</td>' +
                '<td>' + escapar(c[2]) + '</td>' +
                '</tr>';
        }).join('');
    }

    function mostrarSeccionSalida(idSeccion, idCuerpo, htmlFilas) {
        var seccion = document.getElementById(idSeccion);
        var cuerpo  = document.getElementById(idCuerpo);
        if (seccion) { seccion.hidden = false; }
        if (cuerpo)  { cuerpo.innerHTML = htmlFilas; }
        if (seccion) { seccion.scrollIntoView({ behavior: 'smooth', block: 'start' }); }
    }

    /* ------------------------------------------------------------------
       PÁGINA: REGISTRAR (POST /api/actividades)
       ENTRADA: formulario ActividadDTO (sin id)
       SALIDA:  tabla con ActividadResponse (201 Created, incluye id)
    ------------------------------------------------------------------ */
    function inicializarRegistro() {
        var form = document.getElementById('form-registrar');
        if (!form) { return; }

        var hoy    = new Date().toISOString().substring(0, 10);
        var manana = new Date(Date.now() + 86400000).toISOString().substring(0, 10);
        form.fechaInicio.value      = hoy;
        form.fechaTerminacion.value = manana;

        form.addEventListener('submit', function (e) {
            e.preventDefault();
            ocultarPanel('mensaje-form');

            var datos = {
                titulo:           form.titulo.value.trim(),
                descripcion:      form.descripcion.value.trim(),
                fechaInicio:      form.fechaInicio.value,
                fechaTerminacion: form.fechaTerminacion.value,
                tipoActividad:    form.tipoActividad.value,
                idQuehacer:       parseInt(form.idQuehacer.value, 10),
                idTutor:          parseInt(form.idTutor.value, 10),
                idHijo:           parseInt(form.idHijo.value, 10)
            };

            fetch(URL_API, {
                method:  'POST',
                headers: { 'Content-Type': 'application/json' },
                body:    JSON.stringify(datos)
            })
            .then(function (resp) {
                if (resp.ok) {
                    return resp.json().then(function (creada) {
                        mostrarPanel('mensaje-form', 'exito',
                            'Actividad creada — HTTP 201 Created',
                            'El backend asignó el id ' + creada.id + ' a la actividad.');
                        mostrarSeccionSalida(
                            'seccion-respuesta-registro',
                            'cuerpo-respuesta-registro',
                            filasActividadResponse(creada));
                        form.reset();
                        form.fechaInicio.value      = hoy;
                        form.fechaTerminacion.value = manana;
                    });
                }
                return procesarErrorBackend(resp, 'mensaje-form');
            })
            .catch(function (err) {
                mostrarPanel('mensaje-form', 'error', 'Error de conexión', errorConexion(err));
            });
        });
    }

    /* ------------------------------------------------------------------
       PÁGINA: LISTAR (GET /api/actividades)
       ENTRADA: sin parámetros — solo botón que dispara el GET
       SALIDA:  tabla con todas las filas (List<ActividadResponse>)
    ------------------------------------------------------------------ */
    function inicializarListado() {
        var btn     = document.getElementById('btn-cargar-lista');
        var cuerpo  = document.getElementById('cuerpo-listado');
        if (!btn || !cuerpo) { return; }

        btn.addEventListener('click', function () {
            ocultarPanel('mensaje-form');
            cuerpo.innerHTML = '<tr><td colspan="8" class="celda-vacia">Cargando...</td></tr>';

            fetch(URL_API)
            .then(function (resp) {
                if (resp.ok) {
                    return resp.json().then(function (lista) {
                        if (!lista || lista.length === 0) {
                            cuerpo.innerHTML = '<tr><td colspan="8" class="celda-vacia">La base de datos no tiene actividades registradas.</td></tr>';
                            mostrarPanel('mensaje-form', 'info',
                                'HTTP 200 OK — Lista vacía',
                                'El backend respondió correctamente pero no hay actividades en la BD.');
                            return;
                        }
                        var html = lista.map(function (a) {
                            return '<tr>' +
                                '<td><strong>' + escapar(a.id) + '</strong></td>' +
                                '<td>' + escapar(a.titulo) + '</td>' +
                                '<td><span class="etiqueta-tipo ' + escapar(a.tipoActividad) + '">' + escapar(a.tipoActividad) + '</span></td>' +
                                '<td>' + escapar(a.fechaInicio) + '</td>' +
                                '<td>' + escapar(a.fechaTerminacion) + '</td>' +
                                '<td>' + escapar(a.idQuehacer) + '</td>' +
                                '<td>' + escapar(a.idTutor) + '</td>' +
                                '<td>' + escapar(a.idHijo) + '</td>' +
                                '</tr>';
                        }).join('');
                        cuerpo.innerHTML = html;
                        mostrarPanel('mensaje-form', 'exito',
                            'HTTP 200 OK — ' + lista.length + ' actividad(es) encontradas',
                            'El backend retornó List<ActividadResponse> con ' + lista.length + ' registros.');
                    });
                }
                return procesarErrorBackend(resp, 'mensaje-form');
            })
            .catch(function (err) {
                cuerpo.innerHTML = '<tr><td colspan="8" class="celda-vacia">Error de conexión.</td></tr>';
                mostrarPanel('mensaje-form', 'error', 'Error de conexión', errorConexion(err));
            });
        });
    }

    /* ------------------------------------------------------------------
       PÁGINA: CONSULTAR (GET /api/actividades/{id})
       ENTRADA: formulario con campo id (@PathVariable)
       SALIDA:  tabla con ActividadResponse (200) o error (404)
    ------------------------------------------------------------------ */
    function inicializarConsulta() {
        var form = document.getElementById('form-consultar');
        if (!form) { return; }

        form.addEventListener('submit', function (e) {
            e.preventDefault();
            ocultarPanel('mensaje-form');
            var secSalida = document.getElementById('seccion-respuesta-consulta');
            if (secSalida) { secSalida.hidden = true; }

            var id = form.id.value.trim();
            if (!id) {
                mostrarPanel('mensaje-form', 'error', 'Falta el id',
                    'Ingresa el identificador de la actividad a consultar.');
                return;
            }

            fetch(URL_API + '/' + encodeURIComponent(id))
            .then(function (resp) {
                if (resp.ok) {
                    return resp.json().then(function (a) {
                        mostrarPanel('mensaje-form', 'exito',
                            'HTTP 200 OK — Actividad encontrada',
                            'El backend retornó ActividadResponse para el id ' + id + '.');
                        mostrarSeccionSalida(
                            'seccion-respuesta-consulta',
                            'cuerpo-respuesta-consulta',
                            filasActividadResponse(a));
                    });
                }
                // Mostrar igualmente la sección salida con la información del error
                if (secSalida) { secSalida.hidden = false; }
                var cuerpo = document.getElementById('cuerpo-respuesta-consulta');
                if (cuerpo) {
                    cuerpo.innerHTML = '<tr class="fila-error">' +
                        '<td>—</td><td>ErrorResponse</td>' +
                        '<td>HTTP ' + resp.status + ' — ' + (resp.status === 404 ? 'No existe la actividad con id ' + id : 'Error del servidor') + '</td>' +
                        '</tr>';
                }
                return procesarErrorBackend(resp, 'mensaje-form');
            })
            .catch(function (err) {
                mostrarPanel('mensaje-form', 'error', 'Error de conexión', errorConexion(err));
            });
        });
    }

    /* ------------------------------------------------------------------
       PÁGINA: MODIFICAR (PUT /api/actividades/{id})
       ENTRADA: paso 1 = id (@PathVariable), paso 2 = ActividadDTO (body)
       SALIDA:  tabla con ActividadResponse ya modificado (200)
    ------------------------------------------------------------------ */
    function inicializarModificacion() {
        var formBuscar = document.getElementById('form-buscar-modificar');
        var formEditar = document.getElementById('form-modificar');
        if (!formBuscar || !formEditar) { return; }

        formBuscar.addEventListener('submit', function (e) {
            e.preventDefault();
            ocultarPanel('mensaje-form');
            formEditar.hidden = true;
            var secSalida = document.getElementById('seccion-respuesta-modificar');
            if (secSalida) { secSalida.hidden = true; }

            var id = formBuscar.id.value.trim();
            if (!id) { return; }

            fetch(URL_API + '/' + encodeURIComponent(id))
            .then(function (resp) {
                if (resp.ok) {
                    return resp.json().then(function (a) {
                        formEditar.hidden                   = false;
                        formEditar.id_oculto.value          = a.id;
                        formEditar['titulo'].value          = a.titulo;
                        formEditar['descripcion'].value     = a.descripcion;
                        formEditar['fechaInicio'].value     = a.fechaInicio;
                        formEditar['fechaTerminacion'].value = a.fechaTerminacion;
                        formEditar['tipoActividad'].value   = a.tipoActividad;
                        formEditar['idQuehacer'].value      = a.idQuehacer;
                        formEditar['idTutor'].value         = a.idTutor;
                        formEditar['idHijo'].value          = a.idHijo;
                        mostrarPanel('mensaje-form', 'info',
                            'Actividad cargada — id ' + a.id,
                            'Edita los campos que necesites y pulsa "Guardar cambios — PUT".');
                        formEditar.scrollIntoView({ behavior: 'smooth', block: 'start' });
                    });
                }
                return procesarErrorBackend(resp, 'mensaje-form');
            })
            .catch(function (err) {
                mostrarPanel('mensaje-form', 'error', 'Error de conexión', errorConexion(err));
            });
        });

        formEditar.addEventListener('submit', function (e) {
            e.preventDefault();
            ocultarPanel('mensaje-form');
            var id = formEditar.id_oculto.value;
            var datos = {
                titulo:           formEditar['titulo'].value.trim(),
                descripcion:      formEditar['descripcion'].value.trim(),
                fechaInicio:      formEditar['fechaInicio'].value,
                fechaTerminacion: formEditar['fechaTerminacion'].value,
                tipoActividad:    formEditar['tipoActividad'].value,
                idQuehacer:       parseInt(formEditar['idQuehacer'].value, 10),
                idTutor:          parseInt(formEditar['idTutor'].value, 10),
                idHijo:           parseInt(formEditar['idHijo'].value, 10)
            };

            fetch(URL_API + '/' + encodeURIComponent(id), {
                method:  'PUT',
                headers: { 'Content-Type': 'application/json' },
                body:    JSON.stringify(datos)
            })
            .then(function (resp) {
                if (resp.ok) {
                    return resp.json().then(function (modificada) {
                        mostrarPanel('mensaje-form', 'exito',
                            'HTTP 200 OK — Actividad modificada',
                            'El backend retornó ActividadResponse con los datos actualizados.');
                        mostrarSeccionSalida(
                            'seccion-respuesta-modificar',
                            'cuerpo-respuesta-modificar',
                            filasActividadResponse(modificada));
                    });
                }
                return procesarErrorBackend(resp, 'mensaje-form');
            })
            .catch(function (err) {
                mostrarPanel('mensaje-form', 'error', 'Error de conexión', errorConexion(err));
            });
        });
    }

    /* ------------------------------------------------------------------
       PÁGINA: BORRAR (DELETE /api/actividades/{id})
       ENTRADA: formulario con id + tabla de previsualización + confirmación
       SALIDA:  tabla mostrando HTTP 204 No Content (o error)
    ------------------------------------------------------------------ */
    function inicializarBorrado() {
        var formBuscar       = document.getElementById('form-buscar-borrar');
        var secConfirmacion  = document.getElementById('confirmacion-borrado');
        var btnConfirmar     = document.getElementById('btn-confirmar-borrar');
        var btnCancelar      = document.getElementById('btn-cancelar-borrar');
        if (!formBuscar || !secConfirmacion) { return; }

        var idActual = null;

        formBuscar.addEventListener('submit', function (e) {
            e.preventDefault();
            ocultarPanel('mensaje-form');
            secConfirmacion.hidden = true;
            var secSalida = document.getElementById('seccion-respuesta-borrar');
            if (secSalida) { secSalida.hidden = true; }
            idActual = null;

            var id = formBuscar.id.value.trim();
            if (!id) { return; }

            fetch(URL_API + '/' + encodeURIComponent(id))
            .then(function (resp) {
                if (resp.ok) {
                    return resp.json().then(function (a) {
                        idActual = a.id;
                        // Llenar la tabla de previsualización
                        var prev = document.getElementById('cuerpo-preview-borrar');
                        if (prev) { prev.innerHTML = filasActividadResponse(a); }
                        secConfirmacion.hidden = false;
                        mostrarPanel('mensaje-form', 'info',
                            'Actividad encontrada — id ' + a.id,
                            'Revisa los datos y confirma el borrado si estás seguro.');
                        secConfirmacion.scrollIntoView({ behavior: 'smooth', block: 'start' });
                    });
                }
                return procesarErrorBackend(resp, 'mensaje-form');
            })
            .catch(function (err) {
                mostrarPanel('mensaje-form', 'error', 'Error de conexión', errorConexion(err));
            });
        });

        btnCancelar.addEventListener('click', function () {
            secConfirmacion.hidden = true;
            idActual = null;
            mostrarPanel('mensaje-form', 'info', 'Borrado cancelado',
                'No se eliminó ninguna actividad.');
        });

        btnConfirmar.addEventListener('click', function () {
            if (idActual === null) { return; }
            var id = idActual;
            fetch(URL_API + '/' + encodeURIComponent(id), { method: 'DELETE' })
            .then(function (resp) {
                secConfirmacion.hidden = true;
                formBuscar.reset();

                var secSalida = document.getElementById('seccion-respuesta-borrar');
                var cuerpo    = document.getElementById('cuerpo-respuesta-borrar');

                if (resp.status === 204) {
                    mostrarPanel('mensaje-form', 'exito',
                        'HTTP 204 No Content — Actividad eliminada',
                        'El backend confirmó que la actividad con id ' + id + ' fue borrada. No hay cuerpo en la respuesta (204 No Content).');
                    if (secSalida) { secSalida.hidden = false; }
                    if (cuerpo) {
                        cuerpo.innerHTML = '<tr class="fila-exito">' +
                            '<td>204</td>' +
                            '<td>No Content (vacío)</td>' +
                            '<td>La actividad con id ' + escapar(id) + ' fue eliminada de la base de datos H2.</td>' +
                            '</tr>';
                    }
                    idActual = null;
                    return;
                }
                if (secSalida) { secSalida.hidden = false; }
                if (cuerpo) {
                    cuerpo.innerHTML = '<tr class="fila-error">' +
                        '<td>' + resp.status + '</td>' +
                        '<td>ErrorResponse</td>' +
                        '<td>' + (resp.status === 404 ? 'La actividad con id ' + id + ' no existe en la BD.' : 'Error inesperado del servidor.') + '</td>' +
                        '</tr>';
                }
                return procesarErrorBackend(resp, 'mensaje-form');
            })
            .catch(function (err) {
                mostrarPanel('mensaje-form', 'error', 'Error de conexión', errorConexion(err));
            });
        });
    }

    /* ------------------------------------------------------------------
       ARRANQUE
    ------------------------------------------------------------------ */
    document.addEventListener('DOMContentLoaded', function () {
        inicializarRegistro();
        inicializarListado();
        inicializarConsulta();
        inicializarModificacion();
        inicializarBorrado();
    });

})();
