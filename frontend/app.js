/**
 * RetroVideo - Lógica interactiva del Frontend (SPA + OpenAPI 3.0 Simulator)
 * Sincronizado con el modelo de dominio de Java/JPA y database/videoclub.sql
 */

const STORAGE_KEY = 'retrovideo_state_v15';

const INITIAL_STATE = {
    peliculas: [
        { id: 1, titulo: 'Inception', director: 'Christopher Nolan', anio: 2010, valoracion: 9 },
        { id: 2, titulo: 'The Matrix', director: 'Lana Wachowski', anio: 1999, valoracion: 10 },
        { id: 3, titulo: 'Pulp Fiction', director: 'Quentin Tarantino', anio: 1994, valoracion: 9 },
        { id: 4, titulo: 'Interstellar', director: 'Christopher Nolan', anio: 2014, valoracion: 9 },
        { id: 5, titulo: 'El Padrino', director: 'Francis Ford Coppola', anio: 1972, valoracion: 10 },
        { id: 10, titulo: 'The Dark Knight', director: 'Christopher Nolan', anio: 2008, valoracion: 10 },
        { id: 16, titulo: 'Parasite', director: 'Bong Joon-ho', anio: 2019, valoracion: 10 },
        { id: 17, titulo: 'The Whale', director: 'Darren Aronofsky', anio: 2022, valoracion: 8 }
    ],
    videojuegos: [
        { id: 1, titulo: 'The Legend of Zelda: TotK', plataforma: 'Nintendo Switch', anio: 2023, valoracion: 10 },
        { id: 2, titulo: 'Elden Ring', plataforma: 'PS5 / PC', anio: 2022, valoracion: 10 },
        { id: 3, titulo: 'God of War Ragnarok', plataforma: 'PS5', anio: 2022, valoracion: 9 },
        { id: 4, titulo: 'Red Dead Redemption 2', plataforma: 'PC / Xbox / PS4', anio: 2018, valoracion: 10 }
    ],
    socios: [
        { id: 1, dni: '12345678Z', nombre: 'Adrián Martínez (Socio Admin)' },
        { id: 2, dni: '00000000E', nombre: 'Pepe Pérez' }
    ],
    alquileres: [
        {
            id: 1,
            dni_socio: '00000000E',
            nombre_socio: 'Pepe Pérez',
            id_pelicula: 1,
            id_videojuego: null,
            nombre_producto: 'Inception',
            tipo_producto: 'Película',
            fecha_alquiler: '2026-05-07',
            fecha_devolucion: '2026-05-10'
        }
    ]
};

let state = cargarEstado();

function cargarEstado() {
    try {
        const guardado = localStorage.getItem(STORAGE_KEY);
        return guardado ? JSON.parse(guardado) : structuredClone(INITIAL_STATE);
    } catch {
        return structuredClone(INITIAL_STATE);
    }
}

function guardarEstado() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function formatearFechaISO(diasExtra = 0) {
    const fecha = new Date();
    fecha.setDate(fecha.getDate() + diasExtra);
    return fecha.toISOString().split('T')[0];
}

function mostrarToast(mensaje) {
    const toast = document.getElementById('toast');
    toast.textContent = mensaje;
    toast.classList.remove('hidden');
    clearTimeout(toast._timer);
    toast._timer = setTimeout(() => toast.classList.add('hidden'), 3200);
}

function actualizarInspectorAPI(metodo, endpoint, statusText, statusClass, payload) {
    const methodBadge = document.getElementById('api-method-badge');
    const statusBadge = document.getElementById('api-status-badge');
    const output = document.getElementById('api-json-output');

    methodBadge.textContent = `${metodo} ${endpoint}`;
    statusBadge.textContent = statusText;
    statusBadge.className = `badge-status ${statusClass}`;
    output.textContent = JSON.stringify(payload, null, 2);
}

function estaAlquilado(tipo, id) {
    return state.alquileres.some(a =>
        tipo === 'pelicula' ? a.id_pelicula === id : a.id_videojuego === id
    );
}

// === RENDERIZADO PRINCIPAL ===
function renderTodo() {
    renderKPIs();
    renderCatalogo();
    renderAlquileres();
    renderSocios();
}

function renderKPIs() {
    document.getElementById('stat-peliculas').textContent = state.peliculas.length;
    document.getElementById('stat-videojuegos').textContent = state.videojuegos.length;
    document.getElementById('stat-socios').textContent = state.socios.length;
    document.getElementById('stat-alquileres').textContent = state.alquileres.length;
}

function renderCatalogo() {
    const grid = document.getElementById('catalogo-grid');
    const texto = document.getElementById('input-busqueda').value.trim().toLowerCase();
    const tipoFiltro = document.getElementById('filtro-tipo').value;
    const valMinima = parseInt(document.getElementById('filtro-valoracion').value, 10);
    const estadoFiltro = document.getElementById('filtro-estado').value;

    const items = [
        ...state.peliculas.map(p => ({ ...p, tipo: 'pelicula', subtitulo: `Dir: ${p.director}` })),
        ...state.videojuegos.map(v => ({ ...v, tipo: 'videojuego', subtitulo: `Plataforma: ${v.plataforma}` }))
    ];

    const filtrados = items.filter(item => {
        const alquilado = estaAlquilado(item.tipo, item.id);
        const coincideTexto =
            item.titulo.toLowerCase().includes(texto) ||
            item.subtitulo.toLowerCase().includes(texto) ||
            String(item.anio).includes(texto);
        const coincideTipo = tipoFiltro === 'todos' || item.tipo === tipoFiltro;
        const coincideVal = item.valoracion >= valMinima;
        const coincideEstado =
            estadoFiltro === 'todos' ||
            (estadoFiltro === 'disponible' && !alquilado) ||
            (estadoFiltro === 'alquilado' && alquilado);

        return coincideTexto && coincideTipo && coincideVal && coincideEstado;
    });

    if (filtrados.length === 0) {
        grid.innerHTML = `<p style="grid-column: 1/-1; text-align:center; color: var(--text-muted); padding: 30px;">No se encontraron productos con esos filtros.</p>`;
        return;
    }

    grid.innerHTML = filtrados.map(item => {
        const alquilado = estaAlquilado(item.tipo, item.id);
        const icono = item.tipo === 'pelicula' ? 'fa-film' : 'fa-gamepad';
        const etiquetaTipo = item.tipo === 'pelicula' ? 'Película' : 'Videojuego';

        return `
            <article class="card">
                <div>
                    <div class="card-top">
                        <span class="badge-type ${item.tipo}"><i class="fa-solid ${icono}"></i> ${etiquetaTipo}</span>
                        <span class="badge-rating"><i class="fa-solid fa-star"></i> ${item.valoracion}/10</span>
                    </div>
                    <h3>${item.titulo}</h3>
                    <p class="meta">${item.subtitulo} · Año: ${item.anio}</p>
                    <div class="status-pill ${alquilado ? 'alquilado' : 'disponible'}">
                        <i class="fa-solid ${alquilado ? 'fa-clock' : 'fa-circle-check'}"></i>
                        ${alquilado ? 'Actualmente Alquilado' : 'Disponible en Videoclub'}
                    </div>
                </div>
                <div class="card-actions">
                    <button class="btn-outline"
                        ${alquilado ? 'disabled' : ''}
                        onclick="abrirModalAlquiler('${item.tipo}', ${item.id})">
                        <i class="fa-solid fa-key"></i> ${alquilado ? 'No Disponible' : 'Alquilar'}
                    </button>
                    <button class="btn-danger-icon" title="Eliminar del catálogo"
                        onclick="eliminarProducto('${item.tipo}', ${item.id})">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </div>
            </article>
        `;
    }).join('');
}

function renderAlquileres() {
    const tbody = document.getElementById('tabla-alquileres');
    if (state.alquileres.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color: var(--text-muted);">No hay alquileres activos en este momento.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.alquileres.map(a => `
        <tr>
            <td>#${a.id}</td>
            <td><strong>${a.nombre_producto}</strong></td>
            <td><span class="badge-type ${a.tipo_producto === 'Película' ? 'pelicula' : 'videojuego'}">${a.tipo_producto}</span></td>
            <td>${a.nombre_socio} <span class="socio-dni">${a.dni_socio}</span></td>
            <td>${a.fecha_alquiler}</td>
            <td>${a.fecha_devolucion}</td>
            <td>
                <button class="btn-outline btn-sm" onclick="devolverAlquiler(${a.id})">
                    <i class="fa-solid fa-rotate-left"></i> Devolver
                </button>
            </td>
        </tr>
    `).join('');
}

function renderSocios() {
    const contenedor = document.getElementById('lista-socios');
    contenedor.innerHTML = state.socios.map(s => `
        <div class="socio-item">
            <div>
                <strong>${s.nombre}</strong>
            </div>
            <span class="socio-dni">${s.dni}</span>
        </div>
    `).join('');
}

// === GESTIÓN DE ALQUILERES ===
window.abrirModalAlquiler = function(tipo, id) {
    const lista = tipo === 'pelicula' ? state.peliculas : state.videojuegos;
    const item = lista.find(p => p.id === id);
    if (!item) return;

    document.getElementById('alquiler-item-id').value = item.id;
    document.getElementById('alquiler-item-tipo').value = tipo;
    document.getElementById('alquiler-item-titulo').value = `${item.titulo} (${tipo === 'pelicula' ? 'Película' : 'Videojuego'})`;
    document.getElementById('alquiler-fecha-inicio').value = formatearFechaISO(0);
    document.getElementById('alquiler-fecha-fin').value = formatearFechaISO(3);

    const selectSocio = document.getElementById('alquiler-socio-dni');
    selectSocio.innerHTML = state.socios.map(s =>
        `<option value="${s.dni}">${s.nombre} (${s.dni})</option>`
    ).join('');

    document.getElementById('modal-alquiler').classList.remove('hidden');
};

window.devolverAlquiler = function(idAlquiler) {
    const alquiler = state.alquileres.find(a => a.id === idAlquiler);
    if (!alquiler) return;

    state.alquileres = state.alquileres.filter(a => a.id !== idAlquiler);
    guardarEstado();
    renderTodo();

    actualizarInspectorAPI(
        'DELETE',
        `/api/v1/alquileres/${idAlquiler}`,
        '200 OK',
        'ok',
        {
            mensaje: 'Devolución registrada con éxito',
            alquiler_finalizado: alquiler
        }
    );
    mostrarToast(`✅ Devolución completada: ${alquiler.nombre_producto}`);
};

window.eliminarProducto = function(tipo, id) {
    if (estaAlquilado(tipo, id)) {
        mostrarToast('⚠️ No se puede eliminar un producto con alquiler activo.');
        return;
    }
    if (tipo === 'pelicula') {
        state.peliculas = state.peliculas.filter(p => p.id !== id);
    } else {
        state.videojuegos = state.videojuegos.filter(v => v.id !== id);
    }
    guardarEstado();
    renderTodo();
    mostrarToast('🗑️ Producto eliminado del catálogo.');
};

// === EVENT LISTENERS ===
document.addEventListener('DOMContentLoaded', () => {
    renderTodo();
    actualizarInspectorAPI('GET', '/api/v1/alquileres', '200 OK', 'ok', state.alquileres);

    // Filtros en tiempo real
    ['input-busqueda', 'filtro-tipo', 'filtro-valoracion', 'filtro-estado'].forEach(id => {
        document.getElementById(id).addEventListener('input', renderCatalogo);
        document.getElementById(id).addEventListener('change', renderCatalogo);
    });

    // Cerrar modales
    document.querySelectorAll('[data-close]').forEach(btn => {
        btn.addEventListener('click', () => {
            document.getElementById(btn.dataset.close).classList.add('hidden');
        });
    });

    // Abrir modal socio
    const abrirSocio = () => {
        document.getElementById('form-socio').reset();
        document.getElementById('error-socio').classList.add('hidden');
        document.getElementById('modal-socio').classList.remove('hidden');
    };
    document.getElementById('btn-registro').addEventListener('click', abrirSocio);
    document.getElementById('btn-registro-inline').addEventListener('click', abrirSocio);

    // Abrir modal producto
    document.getElementById('btn-nuevo-producto').addEventListener('click', () => {
        document.getElementById('form-producto').reset();
        document.getElementById('label-autor-plat').textContent = 'Director';
        document.getElementById('prod-extra').placeholder = 'Ej: Christopher Nolan';
        document.getElementById('modal-producto').classList.remove('hidden');
    });

    document.getElementById('prod-tipo').addEventListener('change', (e) => {
        const esPeli = e.target.value === 'pelicula';
        document.getElementById('label-autor-plat').textContent = esPeli ? 'Director' : 'Plataforma';
        document.getElementById('prod-extra').placeholder = esPeli ? 'Ej: Denis Villeneuve' : 'Ej: PS5 / Switch';
    });

    // Confirmar Alquiler
    document.getElementById('form-alquiler').addEventListener('submit', (e) => {
        e.preventDefault();
        const idItem = parseInt(document.getElementById('alquiler-item-id').value, 10);
        const tipo = document.getElementById('alquiler-item-tipo').value;
        const dniSocio = document.getElementById('alquiler-socio-dni').value;

        const socio = state.socios.find(s => s.dni === dniSocio);
        const lista = tipo === 'pelicula' ? state.peliculas : state.videojuegos;
        const prod = lista.find(p => p.id === idItem);

        const nuevoId = state.alquileres.length > 0 ? Math.max(...state.alquileres.map(a => a.id)) + 1 : 1;
        const nuevoAlquiler = {
            id: nuevoId,
            dni_socio: socio.dni,
            nombre_socio: socio.nombre,
            id_pelicula: tipo === 'pelicula' ? prod.id : null,
            id_videojuego: tipo === 'videojuego' ? prod.id : null,
            nombre_producto: prod.titulo,
            tipo_producto: tipo === 'pelicula' ? 'Película' : 'Videojuego',
            fecha_alquiler: formatearFechaISO(0),
            fecha_devolucion: formatearFechaISO(3)
        };

        state.alquileres.push(nuevoAlquiler);
        guardarEstado();
        renderTodo();
        document.getElementById('modal-alquiler').classList.add('hidden');

        actualizarInspectorAPI('POST', '/api/v1/alquileres', '201 Created', 'created', {
            requestBody: {
                dni_socio: nuevoAlquiler.dni_socio,
                id_pelicula: nuevoAlquiler.id_pelicula,
                id_videojuego: nuevoAlquiler.id_videojuego
            },
            responseBody: nuevoAlquiler
        });

        mostrarToast(`✅ Alquiler registrado: ${prod.titulo}`);
    });

    // Guardar Socio
    document.getElementById('form-socio').addEventListener('submit', (e) => {
        e.preventDefault();
        const dniInput = document.getElementById('socio-dni').value.trim().toUpperCase();
        const nombreInput = document.getElementById('socio-nombre').value.trim();
        const errorEl = document.getElementById('error-socio');

        if (!/^[0-9]{8}[A-Z]$/.test(dniInput)) {
            errorEl.textContent = 'Formato inválido: el DNI debe tener 8 números y 1 letra (ej: 12345678Z).';
            errorEl.classList.remove('hidden');
            return;
        }

        if (state.socios.some(s => s.dni === dniInput)) {
            errorEl.textContent = 'Restricción UNIQUE: Ya existe un socio registrado con este DNI.';
            errorEl.classList.remove('hidden');
            return;
        }

        const nuevoSocio = {
            id: state.socios.length + 1,
            dni: dniInput,
            nombre: nombreInput
        };
        state.socios.push(nuevoSocio);
        guardarEstado();
        renderTodo();
        document.getElementById('modal-socio').classList.add('hidden');

        actualizarInspectorAPI('POST', '/api/v1/socios', '201 Created', 'created', nuevoSocio);
        mostrarToast(`👤 Socio registrado: ${nombreInput}`);
    });

    // Guardar Producto
    document.getElementById('form-producto').addEventListener('submit', (e) => {
        e.preventDefault();
        const tipo = document.getElementById('prod-tipo').value;
        const titulo = document.getElementById('prod-titulo').value.trim();
        const extra = document.getElementById('prod-extra').value.trim();
        const anio = parseInt(document.getElementById('prod-anio').value, 10);
        const valoracion = parseInt(document.getElementById('prod-valoracion').value, 10);

        if (tipo === 'pelicula') {
            const nuevoId = state.peliculas.length > 0 ? Math.max(...state.peliculas.map(p => p.id)) + 1 : 1;
            const nuevaPeli = { id: nuevoId, titulo, director: extra, anio, valoracion };
            state.peliculas.push(nuevaPeli);
            actualizarInspectorAPI('POST', '/api/v1/peliculas', '201 Created', 'created', nuevaPeli);
        } else {
            const nuevoId = state.videojuegos.length > 0 ? Math.max(...state.videojuegos.map(v => v.id)) + 1 : 1;
            const nuevoJuego = { id: nuevoId, titulo, plataforma: extra, anio, valoracion };
            state.videojuegos.push(nuevoJuego);
            actualizarInspectorAPI('POST', '/api/v1/videojuegos', '201 Created', 'created', nuevoJuego);
        }

        guardarEstado();
        renderTodo();
        document.getElementById('modal-producto').classList.add('hidden');
        mostrarToast(`✨ Añadido al catálogo: ${titulo}`);
    });

    // Restaurar datos SQL
    document.getElementById('btn-reset-demo').addEventListener('click', () => {
        state = structuredClone(INITIAL_STATE);
        guardarEstado();
        renderTodo();
        actualizarInspectorAPI('GET', '/api/v1/alquileres', '200 OK', 'ok', state.alquileres);
        mostrarToast('🔄 Datos restaurados al estado inicial de videoclub.sql');
    });
});

