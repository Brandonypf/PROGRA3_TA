# El Rincón Satipeño — P2 integrado

Se amplió la solución original: un único proyecto Blazor Web App, sin crear otro proyecto.

## 1. Estructura y rutas

Todas las ubicaciones siguientes son relativas a `RinconSatipenoWeb/`.

| Archivo | Ruta / finalidad |
|---|---|
| Components/Layout/AdminLayout.razor | Layout administrativo único, barra superior y contenido |
| Components/Layout/AdminNavMenu.razor | Menú lateral compartido |
| Components/Pages/Mesas/CalendarioReservas.razor | /admin/reservas/calendario |
| Components/Pages/Mesas/EstadoMesas.razor | /admin/mesas/estado y /admin |
| Components/Pages/Mesas/GestionMesas.razor | /admin/mesas |
| Components/Pages/Mesas/_Imports.razor | Aplica AdminLayout a las tres páginas |
| Components/Pages/Mesas/DatosMesasDemo.cs | Registros ficticios de presentación compartidos |
| Components/Pages/Inventario/_Imports.razor | Reutiliza AdminLayout en las páginas existentes |
| wwwroot/css/admin.css | Paleta, layout y ajustes responsive |
| wwwroot/lib/bootstrap/ | Bootstrap 5.3.8 CSS y JS locales |
| wwwroot/lib/bootstrap-icons/ | Bootstrap Icons 1.11.3, CSS y fuentes locales |
| Components/App.razor | Referencias locales a Bootstrap, iconos y estilos |
| Components/Layout/ClienteLayout.razor | Enlace añadido a Administración (demo) |

Las páginas de Cliente e Inventario conservan sus nombres, rutas y código. Se conservan Program.cs, Routes.razor, el .csproj y el .sln originales. El layout de cliente sigue asignado a Cliente; Inventario y Mesas reciben AdminLayout mediante sus propios _Imports.razor. El menú lateral se define una sola vez.


## Código completo de los archivos

El CSS/JS oficial de Bootstrap y las fuentes de iconos están en wwwroot/lib; no se reproducen sus versiones minificadas aquí.

### AdminLayout.razor

Ubicación: `RinconSatipenoWeb/Components/Layout/AdminLayout.razor`

```razor
@inherits LayoutComponentBase
<div class="admin-shell">
    <aside class="admin-sidebar">
        <a class="admin-brand" href="/admin"><i class="bi bi-tree-fill" aria-hidden="true"></i><span>El Rincón Satipeño<small>Administración</small></span></a>
        <details class="admin-navigation" open>
            <summary>Menú de administración</summary>
            <AdminNavMenu />
        </details>
        <div class="admin-sidebar-note">Sabores que nos reúnen<br /><small>Proyecto universitario · Lab 9</small></div>
    </aside>
    <div class="admin-workspace">
        <header class="admin-topbar">
            <span class="fw-semibold">El Rincón Satipeño</span>
            <div class="d-flex align-items-center gap-2"><span class="admin-avatar" aria-hidden="true">AD</span><span>Administrador<small class="d-block text-secondary">Usuario de demostración</small></span></div>
        </header>
        <main id="contenido-admin" class="admin-content">
            <div class="alert alert-light border small mb-4"><i class="bi bi-info-circle me-2" aria-hidden="true"></i>Vista de demostración. Los datos son ficticios y los cambios no se guardan.</div>
            @Body
        </main>
        <footer class="admin-footer">El Rincón Satipeño · Programación 3 · Capa de presentación</footer>
    </div>
</div>
```

### AdminNavMenu.razor

Ubicación: `RinconSatipenoWeb/Components/Layout/AdminNavMenu.razor`

```razor
<nav aria-label="Navegación administrativa" class="admin-menu">
    <span class="admin-menu-label">MESAS Y RESERVAS</span>
    <NavLink href="/admin/mesas/estado"><i class="bi bi-grid" aria-hidden="true"></i> Estado de mesas</NavLink>
    <NavLink href="/admin/reservas/calendario"><i class="bi bi-calendar3" aria-hidden="true"></i> Calendario de reservas</NavLink>
    <NavLink href="/admin/mesas" Match="NavLinkMatch.All"><i class="bi bi-layout-text-window" aria-hidden="true"></i> Gestión de mesas</NavLink>
    <span class="admin-menu-label">CARTA E INVENTARIO</span>
    <NavLink href="/admin/platos"><i class="bi bi-egg-fried" aria-hidden="true"></i> Platos</NavLink>
    <NavLink href="/admin/recetas"><i class="bi bi-journal-text" aria-hidden="true"></i> Recetas</NavLink>
    <NavLink href="/admin/insumos"><i class="bi bi-box-seam" aria-hidden="true"></i> Insumos</NavLink>
    <NavLink href="/admin/inventario/movimientos"><i class="bi bi-arrow-left-right" aria-hidden="true"></i> Movimientos</NavLink>
    <NavLink href="/admin/inventario/alertas"><i class="bi bi-exclamation-circle" aria-hidden="true"></i> Alertas de stock</NavLink>
    <span class="admin-menu-label">VISTA DEL CLIENTE</span>
    <NavLink href="/" Match="NavLinkMatch.All"><i class="bi bi-house" aria-hidden="true"></i> Inicio del restaurante</NavLink>
    <NavLink href="/carta"><i class="bi bi-book" aria-hidden="true"></i> Carta digital</NavLink>
</nav>
```

### _Imports.razor

Ubicación: `RinconSatipenoWeb/Components/Pages/Mesas/_Imports.razor`

```razor
@layout AdminLayout
@using RinconSatipenoWeb.Components.Pages.Mesas
```

### DatosMesasDemo.cs

Ubicación: `RinconSatipenoWeb/Components/Pages/Mesas/DatosMesasDemo.cs`

```csharp
namespace RinconSatipenoWeb.Components.Pages.Mesas;

// Datos de presentación: no son entidades de negocio ni acceden a una base de datos.
public record MesaDemo(int Numero, int Capacidad, string Zona, string Estado, string Detalle);
public record ReservaDemo(int Dia, string Hora, string Cliente, int Personas, int Mesa);

public static class DatosMesasDemo
{
    // Foto ficticia del salón: 26 de octubre de 2026, 13:00.
    public static IReadOnlyList<MesaDemo> Mesas { get; } = new MesaDemo[]
    {
        new(1, 2, "Salón principal", "Libre", "Lista para recibir clientes"),
        new(2, 4, "Salón principal", "Ocupada", "Atención A-021 · 3 personas · desde 12:20"),
        new(3, 4, "Salón principal", "Reservada", "María Rojas · 13:30 · 4 personas"),
        new(4, 6, "Salón principal", "Libre", "Ideal para grupos"),
        new(5, 2, "Terraza", "Ocupada", "Atención A-022 · 2 personas · desde 12:45"),
        new(6, 4, "Terraza", "Libre", "Mesa al aire libre"),
        new(7, 6, "Terraza", "Reservada", "Luis Torres · 14:00 · 5 personas"),
        new(8, 8, "Salón privado", "Libre", "Ambiente para reuniones")
    };
    public static IReadOnlyList<ReservaDemo> Reservas { get; } = new ReservaDemo[]
    {
        new(3, "13:00", "Ana Salazar", 4, 3),
        new(10, "19:00", "Jorge Díaz", 6, 4),
        new(17, "14:00", "Lucía Pérez", 2, 1),
        new(24, "13:30", "Carlos Huamán", 4, 6),
        new(26, "13:30", "María Rojas", 4, 3),
        new(26, "14:00", "Luis Torres", 5, 7),
        new(26, "19:00", "Rosa Mendoza", 6, 8),
        new(28, "13:00", "Pedro Quispe", 2, 1),
        new(31, "20:00", "Elena Soto", 4, 3)
    };
    public static string ClaseEstado(string estado) => estado switch
    {
        "Libre" => "estado-libre",
        "Reservada" => "estado-reservada",
        _ => "estado-ocupada"
    };
}
```

### CalendarioReservas.razor

Ubicación: `RinconSatipenoWeb/Components/Pages/Mesas/CalendarioReservas.razor`

```razor
@page "/admin/reservas/calendario"
@using System.Globalization
<PageTitle>Calendario de reservas | El Rincón Satipeño</PageTitle>
<div class="admin-page-heading"><div><span class="admin-eyebrow">ADMINISTRACIÓN / RESERVAS</span><h1>Calendario de reservas</h1><p>Consulta la distribución de reservas y el detalle de cada fecha.</p></div><a class="btn btn-primary" href="/admin/mesas/estado">Ver estado de mesas</a></div>
<div class="row g-3 mb-4">
    <div class="col-sm-4"><div class="card admin-stat"><div class="card-body"><span>Reservas del mes</span><strong>@ReservasMes.Count()</strong><small>@mes.ToString("MMMM yyyy", cultura)</small></div></div></div>
    <div class="col-sm-4"><div class="card admin-stat stat-gold"><div class="card-body"><span>Reservas del día seleccionado</span><strong>@ReservasDia.Count()</strong><small>@seleccion.ToString("dd/MM/yyyy")</small></div></div></div>
    <div class="col-sm-4"><div class="card admin-stat stat-teal"><div class="card-body"><span>Personas esperadas ese día</span><strong>@ReservasDia.Sum(r => r.Personas)</strong><small>Datos de demostración</small></div></div></div>
</div>
<section class="card mb-4"><div class="card-body">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3"><h2 class="h5 mb-0 text-capitalize">@mes.ToString("MMMM yyyy", cultura)</h2><div class="d-flex gap-2"><button class="btn btn-outline-secondary" aria-label="Mes anterior" @onclick="() => CambiarMes(-1)">←</button><button class="btn btn-outline-primary" @onclick="Restablecer">Mes de ejemplo</button><button class="btn btn-outline-secondary" aria-label="Mes siguiente" @onclick="() => CambiarMes(1)">→</button></div></div>
    <p class="small text-secondary">Selecciona un día. Las reservas de ejemplo corresponden a octubre de 2026.</p>
    <div class="admin-calendar-scroll"><div class="admin-calendar">
        @foreach (var nombre in new[] { "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom" }) { <div class="calendar-weekday">@nombre</div> }
        @for (int celda = 0; celda < 42; celda++)
        {
            var fecha = InicioCuadricula.AddDays(celda);
            var cantidad = fecha.Month == 10 && fecha.Year == 2026 ? DatosMesasDemo.Reservas.Count(r => r.Dia == fecha.Day) : 0;
            <button class="calendar-day @(fecha.Month != mes.Month ? "other-month" : "") @(fecha == seleccion ? "selected" : "")" @onclick="() => ElegirFecha(fecha)" aria-pressed="@(fecha == seleccion ? "true" : "false")" aria-label="@($"{fecha:dd/MM/yyyy}, {cantidad} reservas")"><span>@fecha.Day</span>@if (cantidad > 0) { <small>@cantidad reserva@(cantidad == 1 ? "" : "s")</small> }</button>
        }
    </div></div>
</div></section>
<section class="card"><div class="card-body border-bottom"><h2 class="h5 mb-0">Reservas · @seleccion.ToString("dd 'de' MMMM", cultura)</h2></div>
<div class="table-responsive"><table class="table table-hover align-middle mb-0"><caption class="px-3">Reservas ficticias del día seleccionado</caption><thead><tr><th scope="col">Hora</th><th scope="col">Cliente</th><th scope="col">Personas</th><th scope="col">Mesa</th><th scope="col">Estado</th></tr></thead><tbody>
@foreach (var reserva in ReservasDia) { <tr><td>@reserva.Hora</td><td>@reserva.Cliente</td><td>@reserva.Personas</td><td>@reserva.Mesa.ToString("00")</td><td><span class="admin-status estado-libre">Confirmada</span></td></tr> }
@if (!ReservasDia.Any()) { <tr><td colspan="5" class="text-center py-4 text-secondary">No hay reservas de ejemplo para esta fecha.</td></tr> }
</tbody></table></div></section>
<a class="btn btn-outline-secondary mt-4" href="/admin/mesas/estado">← Volver a mesas</a>
@code {
    private readonly CultureInfo cultura = CultureInfo.GetCultureInfo("es-PE");
    private DateTime mes = new(2026, 10, 1), seleccion = new(2026, 10, 26);
    private DateTime InicioCuadricula => mes.AddDays(-((int)mes.DayOfWeek + 6) % 7);
    private IEnumerable<ReservaDemo> ReservasMes => mes.Year == 2026 && mes.Month == 10 ? DatosMesasDemo.Reservas : Array.Empty<ReservaDemo>();
    private IEnumerable<ReservaDemo> ReservasDia => seleccion.Year == 2026 && seleccion.Month == 10 ? DatosMesasDemo.Reservas.Where(r => r.Dia == seleccion.Day).OrderBy(r => r.Hora) : Array.Empty<ReservaDemo>();
    private void CambiarMes(int cambio) { mes = mes.AddMonths(cambio); seleccion = mes; }
    private void ElegirFecha(DateTime fecha) { seleccion = fecha; mes = new(fecha.Year, fecha.Month, 1); }
    private void Restablecer() { mes = new(2026, 10, 1); seleccion = new(2026, 10, 26); }
}
```

### EstadoMesas.razor

Ubicación: `RinconSatipenoWeb/Components/Pages/Mesas/EstadoMesas.razor`

```razor
@page "/admin"
@page "/admin/mesas/estado"
<PageTitle>Estado de mesas | El Rincón Satipeño</PageTitle>
<div class="admin-page-heading">
    <div><span class="admin-eyebrow">ADMINISTRACIÓN / MESAS</span><h1>Estado de mesas</h1><p>Una vista rápida de los ambientes del restaurante.</p></div>
    <a class="btn btn-primary" href="/admin/mesas"><i class="bi bi-gear me-2" aria-hidden="true"></i>Gestionar mesas</a>
</div>
<p class="small text-secondary">Escenario de ejemplo · 26 de octubre de 2026, 13:00 · Sin actualización en tiempo real</p>
<div class="row g-3 mb-4">
    @foreach (var estado in new[] { "Libre", "Reservada", "Ocupada" })
    {
        <div class="col-sm-4"><div class="card admin-stat @DatosMesasDemo.ClaseEstado(estado)"><div class="card-body"><span>Mesas · @estado</span><strong>@DatosMesasDemo.Mesas.Count(m => m.Estado == estado)</strong><small>De @DatosMesasDemo.Mesas.Count mesas en total</small></div></div></div>
    }
</div>
<section class="card mb-4"><div class="card-body row g-3 align-items-end">
    <div class="col-md-5"><label for="zona" class="form-label">Ambiente</label><select id="zona" class="form-select" @bind="zona"><option value="">Todos los ambientes</option><option>Salón principal</option><option>Terraza</option><option>Salón privado</option></select></div>
    <div class="col-md-5"><label for="estado" class="form-label">Estado</label><select id="estado" class="form-select" @bind="filtroEstado"><option value="">Todos los estados</option><option>Libre</option><option>Reservada</option><option>Ocupada</option></select></div>
    <div class="col-md-2"><button class="btn btn-outline-secondary w-100" @onclick="Limpiar">Limpiar</button></div>
</div></section>
<div class="row g-3">
    @foreach (var mesa in MesasFiltradas)
    {
        <div class="col-sm-6 col-xl-3"><article class="card admin-mesa h-100 @DatosMesasDemo.ClaseEstado(mesa.Estado)">
            <div class="card-body"><div class="d-flex justify-content-between align-items-center mb-3"><i class="bi bi-grid-3x3-gap admin-table-icon" aria-hidden="true"></i><span class="admin-status @DatosMesasDemo.ClaseEstado(mesa.Estado)">@mesa.Estado</span></div>
            <h2 class="h5">Mesa @mesa.Numero.ToString("00")</h2><p class="mb-1">@mesa.Zona</p><p class="small text-secondary"><i class="bi bi-people me-1" aria-hidden="true"></i> @mesa.Capacidad personas</p>
            <button class="btn btn-outline-primary w-100" @onclick="() => seleccionada = mesa" aria-label="@($"Ver detalle de mesa {mesa.Numero}")">Ver detalle</button></div>
        </article></div>
    }
</div>
@if (!MesasFiltradas.Any()) { <div class="alert alert-info mt-3" role="status">No hay mesas con estos filtros.</div> }
@if (seleccionada is not null)
{
    <section class="card mt-4" aria-label="Detalle de mesa"><div class="card-body"><div class="d-flex justify-content-between gap-3"><h2 class="h5">Mesa @seleccionada.Numero.ToString("00") · @seleccionada.Estado</h2><button class="btn-close" aria-label="Cerrar detalle" @onclick="() => seleccionada = null"></button></div>
    <p>@seleccionada.Detalle</p><a class="btn btn-primary" href="/admin/reservas/calendario">Consultar reservas</a></div></section>
}
<div class="mt-4"><a class="btn btn-outline-secondary" href="/">← Volver al restaurante</a></div>
@code {
    private string zona = "", filtroEstado = "";
    private MesaDemo? seleccionada;
    private IEnumerable<MesaDemo> MesasFiltradas => DatosMesasDemo.Mesas.Where(m => (zona == "" || m.Zona == zona) && (filtroEstado == "" || m.Estado == filtroEstado));
    private void Limpiar() { zona = ""; filtroEstado = ""; seleccionada = null; }
}
```

### GestionMesas.razor

Ubicación: `RinconSatipenoWeb/Components/Pages/Mesas/GestionMesas.razor`

```razor
@page "/admin/mesas"
<PageTitle>Gestión de mesas | El Rincón Satipeño</PageTitle>
<div class="admin-page-heading"><div><span class="admin-eyebrow">ADMINISTRACIÓN / MESAS</span><h1>Gestión de mesas</h1><p>Organiza las mesas, capacidades y ambientes del restaurante.</p></div><button class="btn btn-primary" @onclick="Registrar"><i class="bi bi-plus-lg me-2" aria-hidden="true"></i>Registrar mesa</button></div>
@if (mensaje != "") { <div class="alert alert-info" role="status">@mensaje</div> }
<section class="card mb-4"><div class="card-body row g-3 align-items-end">
    <div class="col-md-6"><label for="buscarMesa" class="form-label">Buscar por número o ambiente</label><input id="buscarMesa" class="form-control" placeholder="Ej.: 03 o Terraza" @bind="busqueda" @bind:event="oninput" /></div>
    <div class="col-md-4"><label for="estadoGestion" class="form-label">Estado</label><select id="estadoGestion" class="form-select" @bind="estado"><option value="">Todos</option><option>Libre</option><option>Reservada</option><option>Ocupada</option></select></div>
    <div class="col-md-2"><button class="btn btn-outline-secondary w-100" @onclick="Limpiar">Limpiar</button></div>
</div></section>
@if (mostrarFormulario)
{
    <section class="card mb-4"><div class="card-body"><h2 class="h5">@(editando ? "Editar mesa" : "Registrar mesa")</h2><p class="small text-secondary">Formulario visual. Guardar muestra un aviso; no modifica la lista.</p>
        <form @onsubmit="Guardar" @onsubmit:preventDefault>
            <div class="row g-3">
                <div class="col-md-3"><label class="form-label" for="numeroMesa">Número de mesa</label><input id="numeroMesa" class="form-control" type="number" min="1" max="99" required @bind="numero" /></div>
                <div class="col-md-3"><label class="form-label" for="capacidadMesa">Capacidad (personas)</label><input id="capacidadMesa" class="form-control" type="number" min="1" max="20" required @bind="capacidad" /></div>
                <div class="col-md-3"><label class="form-label" for="ambienteMesa">Ambiente</label><select id="ambienteMesa" class="form-select" @bind="ambiente"><option>Salón principal</option><option>Terraza</option><option>Salón privado</option></select></div>
                <div class="col-md-3"><label class="form-label" for="situacionMesa">Estado</label><select id="situacionMesa" class="form-select" @bind="situacion"><option>Libre</option><option>Reservada</option><option>Ocupada</option></select></div>
                <div class="col-12"><label class="form-label" for="observacionMesa">Observaciones</label><textarea id="observacionMesa" class="form-control" rows="2" maxlength="250" @bind="observacion"></textarea></div>
            </div><div class="d-flex gap-2 mt-3"><button type="submit" class="btn btn-primary">Guardar (demo)</button><button type="button" class="btn btn-outline-secondary" @onclick="() => mostrarFormulario = false">Cancelar</button></div>
        </form>
    </div></section>
}
@if (porEliminar is not null)
{
    <section class="alert alert-warning" aria-labelledby="eliminarTitulo"><h2 id="eliminarTitulo" class="h5">Eliminar mesa @porEliminar.Numero.ToString("00")</h2><p>¿Deseas simular la eliminación? La mesa seguirá en la lista de demostración.</p><div class="d-flex gap-2"><button class="btn btn-danger" @onclick="Eliminar">Eliminar (demo)</button><button class="btn btn-outline-secondary" @onclick="() => porEliminar = null">Cancelar</button></div></section>
}
<section class="card"><div class="card-body border-bottom d-flex justify-content-between"><h2 class="h5 mb-0">Mesas del restaurante</h2><span class="text-secondary">@Filtradas.Count() resultados</span></div>
<div class="table-responsive"><table class="table table-striped table-hover align-middle mb-0"><caption class="px-3">Listado de mesas de demostración</caption><thead><tr><th scope="col">Mesa</th><th scope="col">Capacidad</th><th scope="col">Ambiente</th><th scope="col">Estado</th><th scope="col">Acciones</th></tr></thead><tbody>
@foreach (var mesa in Filtradas)
{
    <tr><td class="fw-semibold">@mesa.Numero.ToString("00")</td><td>@mesa.Capacidad personas</td><td>@mesa.Zona</td><td><span class="admin-status @DatosMesasDemo.ClaseEstado(mesa.Estado)">@mesa.Estado</span></td><td><div class="d-flex gap-2"><button class="btn btn-sm btn-secondary" @onclick="() => Editar(mesa)" aria-label="@($"Editar mesa {mesa.Numero}")"><i class="bi bi-pencil me-1" aria-hidden="true"></i>Editar</button><button class="btn btn-sm btn-outline-danger" @onclick="() => ConfirmarEliminar(mesa)" aria-label="@($"Eliminar mesa {mesa.Numero}")"><i class="bi bi-trash me-1" aria-hidden="true"></i>Eliminar</button></div></td></tr>
}
@if (!Filtradas.Any()) { <tr><td colspan="5" class="text-center py-4">No se encontraron mesas con estos filtros.</td></tr> }
</tbody></table></div></section>
<a class="btn btn-outline-secondary mt-4" href="/admin/mesas/estado">← Volver al estado de mesas</a>
@code {
    private string busqueda = "", estado = "", mensaje = "";
    private bool mostrarFormulario, editando;
    private int numero = 9, capacidad = 4;
    private string ambiente = "Salón principal", situacion = "Libre", observacion = "";
    private MesaDemo? porEliminar;
    private IEnumerable<MesaDemo> Filtradas => DatosMesasDemo.Mesas.Where(m =>
        (estado == "" || m.Estado == estado) && (m.Numero.ToString("00").Contains(busqueda.Trim()) || m.Zona.Contains(busqueda.Trim(), StringComparison.OrdinalIgnoreCase)));
    private void Limpiar() { busqueda = ""; estado = ""; }
    private void Registrar() { editando = false; numero = 9; capacidad = 4; ambiente = "Salón principal"; situacion = "Libre"; observacion = ""; mostrarFormulario = true; porEliminar = null; mensaje = ""; }
    private void Editar(MesaDemo mesa) { editando = true; numero = mesa.Numero; capacidad = mesa.Capacidad; ambiente = mesa.Zona; situacion = mesa.Estado; observacion = mesa.Detalle; mostrarFormulario = true; porEliminar = null; mensaje = ""; }
    private void Guardar() { mensaje = "Demostración completada. No se registraron ni modificaron mesas."; mostrarFormulario = false; }
    private void ConfirmarEliminar(MesaDemo mesa) { porEliminar = mesa; mostrarFormulario = false; mensaje = ""; }
    private void Eliminar() { mensaje = $"Eliminación simulada de la mesa {porEliminar?.Numero:00}. No se eliminó ningún dato."; porEliminar = null; }
}
```

### _Imports.razor

Ubicación: `RinconSatipenoWeb/Components/Pages/Inventario/_Imports.razor`

```razor
@layout AdminLayout
```

### admin.css

Ubicación: `RinconSatipenoWeb/wwwroot/css/admin.css`

```css
/* Estilos compartidos y acotados al layout administrativo. Bootstrap es la base. */
.admin-shell{font-family:'Segoe UI',sans-serif;color:#303B35;background:#F8F5EF;min-height:100vh;display:flex}
.admin-sidebar{width:252px;flex-shrink:0;background:#264D3B;color:white;padding:28px 18px;position:sticky;top:0;height:100vh;overflow:auto}
.admin-brand{display:flex;gap:12px;align-items:center;color:#fff;text-decoration:none;font-size:1.1rem;font-weight:700;margin-bottom:30px}.admin-brand i{color:#E4A64B;font-size:1.8rem}.admin-brand small{display:block;font-size:.75rem;font-weight:400;color:#DCEBDD;margin-top:5px}
.admin-navigation summary{font-size:.85rem;color:#DCEBDD;cursor:pointer;margin-bottom:15px}.admin-menu{display:flex;flex-direction:column;gap:5px}.admin-menu-label{font-size:.65rem;letter-spacing:.08em;color:#c7d8cb;margin:20px 12px 5px}.admin-menu a{padding:11px 12px;color:#f4f7f3;text-decoration:none;border-radius:8px;display:flex;gap:10px;font-size:.9rem}.admin-menu a:hover{background:#ffffff14}.admin-menu a.active{background:#DCEBDD;color:#264D3B;font-weight:600}.admin-sidebar-note{font-size:.8rem;color:#DCEBDD;margin:35px 12px 0}
.admin-workspace{flex:1;min-width:0}.admin-topbar{background:white;padding:18px 30px;min-height:80px;display:flex;align-items:center;justify-content:space-between;gap:15px;border-bottom:1px solid #e8e4dc}.admin-avatar{background:#DCEBDD;color:#264D3B;border-radius:50%;padding:10px;font-weight:700}.admin-content{padding:30px;max-width:1600px;margin:auto}.admin-footer{padding:22px 30px;color:#626e65;font-size:.8rem}
.admin-page-heading{display:flex;justify-content:space-between;align-items:center;gap:16px;flex-wrap:wrap;margin-bottom:24px}.admin-page-heading h1{font-size:1.8rem;color:#264D3B;font-weight:700;margin:8px 0}.admin-page-heading p{color:#626e65;margin:0}.admin-eyebrow{font-size:.68rem;letter-spacing:.09em;font-weight:700;color:#69756c}
.admin-shell .card{border:1px solid #e6e8e2;border-radius:12px;box-shadow:0 3px 12px #264d3b06;overflow:hidden}.admin-shell .card-body{padding:22px}.admin-shell .btn{border-radius:7px}.admin-shell .btn-primary{--bs-btn-bg:#264D3B;--bs-btn-border-color:#264D3B;--bs-btn-hover-bg:#193b2c;--bs-btn-hover-border-color:#193b2c;--bs-btn-active-bg:#193b2c;--bs-btn-active-border-color:#193b2c}.admin-shell .btn-secondary{--bs-btn-bg:#E4A64B;--bs-btn-border-color:#E4A64B;--bs-btn-color:#303B35;--bs-btn-hover-bg:#d69637;--bs-btn-hover-border-color:#d69637;--bs-btn-hover-color:#303B35}.admin-shell .btn-outline-primary{--bs-btn-color:#264D3B;--bs-btn-border-color:#264D3B;--bs-btn-hover-bg:#264D3B;--bs-btn-hover-border-color:#264D3B;--bs-btn-active-bg:#264D3B;--bs-btn-active-border-color:#264D3B}.admin-shell .btn-danger{--bs-btn-bg:#a74735;--bs-btn-border-color:#a74735;--bs-btn-hover-bg:#8f3929;--bs-btn-hover-border-color:#8f3929}.admin-shell .btn-outline-danger{--bs-btn-color:#a74735;--bs-btn-border-color:#c96b52;--bs-btn-hover-bg:#a74735;--bs-btn-hover-border-color:#a74735}
.admin-shell .form-label{font-weight:600;font-size:.9rem}.admin-shell .form-control:focus,.admin-shell .form-select:focus{border-color:#4D8C85;box-shadow:0 0 0 .2rem #4d8c8522}.admin-shell .table th{background:#f1f4ef;color:#405448;font-size:.83rem;white-space:nowrap}.admin-shell .table td,.admin-shell .table th{padding:16px}.admin-shell .table caption{font-size:.8rem}.admin-stat{border-left:4px solid #4D8C85!important}.admin-stat strong{display:block;font-size:2rem;color:#264D3B;margin:6px 0}.admin-stat small{display:block;color:#626e65}.admin-stat.estado-libre{border-left-color:#508459!important}.admin-stat.estado-reservada,.stat-gold{border-left-color:#E4A64B!important}.admin-stat.estado-ocupada{border-left-color:#E27D60!important}.stat-teal{border-left-color:#4D8C85!important}
.admin-status{display:inline-block;border-radius:20px;padding:5px 10px;font-weight:600;font-size:.78rem;white-space:nowrap}.admin-status.estado-libre{background:#DCEBDD;color:#285435}.admin-status.estado-reservada{background:#fff0cd;color:#785212}.admin-status.estado-ocupada{background:#f9e0d9;color:#943f30}.admin-mesa{border-top:3px solid #508459!important}.admin-mesa.estado-reservada{border-top-color:#E4A64B!important}.admin-mesa.estado-ocupada{border-top-color:#E27D60!important}.admin-mesa:hover{box-shadow:0 5px 18px #264d3b10}.admin-table-icon{font-size:1.8rem;color:#4D8C85}.admin-calendar-scroll{overflow-x:auto}.admin-calendar{display:grid;grid-template-columns:repeat(7,minmax(0,1fr));gap:6px;min-width:490px}.calendar-weekday{text-align:center;font-size:.8rem;color:#626e65;font-weight:600;padding:8px}.calendar-day{border:1px solid #e3e7df;border-radius:7px;background:#fff;min-height:84px;text-align:left;padding:10px;color:#303B35;display:flex;flex-direction:column;gap:8px}.calendar-day small{font-size:.7rem;background:#DCEBDD;color:#264D3B;padding:4px;border-radius:4px}.calendar-day:hover{background:#F8F5EF}.calendar-day.other-month{background:#f3f3f0;color:#788078}.calendar-day.selected{border:2px solid #264D3B;background:#f1f7ef}.admin-shell a:focus-visible,.admin-shell button:focus-visible{outline:3px solid #E4A64B;outline-offset:3px}
@media(max-width:991px){.admin-shell{display:block}.admin-sidebar{position:static;width:100%;height:auto;padding:16px 20px}.admin-brand{margin-bottom:12px}.admin-sidebar-note{display:none}.admin-menu{display:grid;grid-template-columns:repeat(2,minmax(0,1fr))}.admin-menu-label{grid-column:1/-1}.admin-content{padding:22px}.admin-topbar{padding:15px 22px}.admin-navigation summary{margin-bottom:0}.admin-menu a{font-size:.85rem}}
@media(max-width:575px){.admin-content{padding:16px}.admin-topbar{flex-wrap:wrap;padding:16px}.admin-page-heading h1{font-size:1.5rem}.admin-shell .card-body{padding:16px}.admin-menu{grid-template-columns:1fr}.calendar-day{min-height:72px}.admin-footer{padding:16px}}
```

### App.razor

Ubicación: `RinconSatipenoWeb/Components/App.razor`

```razor
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <base href="/" />
    <ResourcePreloader />
    <link href="lib/bootstrap/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="@Assets["css/cliente.css"]" />
    <link rel="stylesheet" href="@Assets["css/reconnect.css"]" />
    <link rel="stylesheet" href="@Assets["css/admin.css"]" />
    <link rel="stylesheet" href="lib/bootstrap-icons/bootstrap-icons.min.css" />
    <ImportMap />
    <HeadOutlet @rendermode="InteractiveServer" />
</head>
<body>
    <Routes @rendermode="InteractiveServer" />
    <ReconnectModal />
    <script src="lib/bootstrap/bootstrap.bundle.min.js"></script>
    <script src="@Assets["_framework/blazor.web.js"]"></script>
</body>
</html>
```

### ClienteLayout.razor

Ubicación: `RinconSatipenoWeb/Components/Layout/ClienteLayout.razor`

```razor
@inherits LayoutComponentBase
@using Microsoft.AspNetCore.Components.Web


<div class="rs-site">
    <header class="rs-header">
        <nav class="navbar navbar-expand-lg navbar-light rs-nav">
            <div class="container">
                <a class="navbar-brand rs-brand" href="/">
                    <span class="rs-brand-icon">✦</span>
                    <span>El Rincón <strong>Satipeño</strong><small>Restaurante &amp; tradición</small></span>
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#rsNav"
                        aria-controls="rsNav" aria-expanded="false" aria-label="Abrir menú">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="rsNav">
                    <div class="navbar-nav ms-auto align-items-lg-center gap-lg-2">
                        <a class="nav-link" href="/">Inicio</a>
                        <a class="nav-link" href="/admin">Administración (demo)</a>
                        <a class="nav-link" href="/carta">Nuestra carta</a>
                        <a class="nav-link" href="/reservas/gestionar">Mi reserva</a>
                        <a class="btn rs-btn-primary ms-lg-2" href="/reservas/nueva">Reservar mesa</a>
                    </div>
                </div>
            </div>
        </nav>
    </header>

    <main class="rs-main">@Body</main>

    <footer class="rs-footer">
        <div class="container py-4">
            <div class="row align-items-center gy-3">
                <div class="col-md-7">
                    <div class="fw-semibold">✦ El Rincón Satipeño</div>
                    <div class="small rs-footer-muted">Sabores que nos reúnen</div>
                </div>
                <div class="col-md-5 text-md-end">
                    <a href="/carta">Carta</a><span class="mx-3">·</span><a href="/reservas/nueva">Reservar</a>
                    <span class="mx-3">·</span><a href="/reservas/gestionar">Mi reserva</a>
                </div>
            </div>
        </div>
    </footer>
</div>
```
