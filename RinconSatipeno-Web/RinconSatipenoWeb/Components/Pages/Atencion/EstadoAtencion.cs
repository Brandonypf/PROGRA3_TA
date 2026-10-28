using System;
using System.Collections.Generic;
using System.Linq;
using RinconSatipenoWeb.Components.Data;

namespace RinconSatipenoWeb.Components.Pages.Atencion;

public sealed class MesaAtencion
{
    public int Numero { get; set; }
    public string Zona { get; set; } = "";
    public int Capacidad { get; set; }
    public int Personas { get; set; }
    public string Estado { get; set; } = "";
    public string Mozo { get; set; } = "";
    public string Hora { get; set; } = "";
}

public sealed class PlatoAtencion
{
    public int Id { get; set; }
    public string Nombre { get; set; } = "";
    public string Categoria { get; set; } = "";
    public decimal Precio { get; set; }
}

public sealed class DetalleAtencion
{
    public string Nombre { get; set; } = "";
    public int Cantidad { get; set; }
    public decimal PrecioUnitario { get; set; }
    public string Estado { get; set; } = "Servido";
    public decimal Importe => Cantidad * PrecioUnitario;
}

public sealed class PagoAtencion
{
    public int Mesa { get; set; }
    public string Referencia { get; set; } = "";
    public string Mozo { get; set; } = "";
    public string Comprobante { get; set; } = "";
    public string Metodo { get; set; } = "";
    public string? Ruc { get; set; }
    public string? RazonSocial { get; set; }
    public decimal Total { get; set; }
    public DateTime Fecha { get; set; }
}

// Datos de ejemplo para mostrar las pantallas de atención.
public sealed class EstadoAtencion
{
    public string RolActual { get; private set; } = "";
    public string MozoActual { get; private set; } = "Lucía Ramos";

    public void Ingresar(string rol, string mozo)
    {
        RolActual = rol == "Mozo" ? "Mozo" : "Administrador";
        MozoActual = Mozos.Contains(mozo) ? mozo : Mozos[0];
    }

    public void CerrarSesion() => RolActual = "";

    public bool PuedeAtender(int numero) => BuscarMesa(numero) is not null;

    public IReadOnlyList<string> Mozos { get; } = new List<string>
    {
        "Lucía Ramos", "José Vega", "María Soto"
    };

    public List<MesaAtencion> Mesas { get; } = new()
    {
        new() { Numero = 1, Zona = "Salón principal", Capacidad = 2, Personas = 0, Estado = "Libre", Mozo = "Lucía Ramos", Hora = "—" },
        new() { Numero = 2, Zona = "Salón principal", Capacidad = 4, Personas = 3, Estado = "Ocupada", Mozo = "Lucía Ramos", Hora = "12:20" },
        new() { Numero = 3, Zona = "Salón principal", Capacidad = 4, Personas = 0, Estado = "Reservada", Mozo = "Lucía Ramos", Hora = "13:30" },
        new() { Numero = 4, Zona = "Salón principal", Capacidad = 6, Personas = 0, Estado = "Libre", Mozo = "José Vega", Hora = "—" },
        new() { Numero = 5, Zona = "Terraza", Capacidad = 2, Personas = 2, Estado = "Ocupada", Mozo = "Lucía Ramos", Hora = "12:45" },
        new() { Numero = 6, Zona = "Terraza", Capacidad = 4, Personas = 0, Estado = "Libre", Mozo = "José Vega", Hora = "—" },
        new() { Numero = 7, Zona = "Terraza", Capacidad = 6, Personas = 0, Estado = "Reservada", Mozo = "José Vega", Hora = "14:00" },
        new() { Numero = 8, Zona = "Salón principal", Capacidad = 8, Personas = 0, Estado = "Libre", Mozo = "María Soto", Hora = "—" }
    };

    public IReadOnlyList<PlatoAtencion> Platos { get; } = CartaRestaurante.Platos
        .Select(p => new PlatoAtencion { Id = p.Id, Nombre = p.Nombre, Categoria = p.Categoria, Precio = p.Precio })
        .ToList();

    private readonly Dictionary<int, List<DetalleAtencion>> consumos = new()
    {
        [2] = new()
        {
            new() { Nombre = "Arroz con pollo", Cantidad = 2, PrecioUnitario = 25m, Estado = "Servido" },
            new() { Nombre = "Lomo saltado — especialidad de la casa", Cantidad = 1, PrecioUnitario = 39m, Estado = "Servido" },
            new() { Nombre = "Limonada con hierbabuena", Cantidad = 3, PrecioUnitario = 8m, Estado = "En preparación" }
        },
        [5] = new()
        {
            new() { Nombre = "Lomo saltado — especialidad de la casa", Cantidad = 1, PrecioUnitario = 39m, Estado = "Servido" },
            new() { Nombre = "Jugo de maracuyá", Cantidad = 2, PrecioUnitario = 10m, Estado = "Servido" }
        }
    };

    public MesaAtencion? BuscarMesa(int numero) => Mesas.FirstOrDefault(m => m.Numero == numero);

    public IReadOnlyList<DetalleAtencion> ObtenerConsumo(int numero) =>
        consumos.TryGetValue(numero, out var items) ? items : Array.Empty<DetalleAtencion>();

    public decimal Total(int numero) => ObtenerConsumo(numero).Sum(d => d.Importe);

    public PagoAtencion? ObtenerPago(int numero) => null;

    public static string ClaseEstado(string estado) => estado switch
    {
        "Libre" => "rs-atn-free",
        "Reservada" => "rs-atn-reserved",
        "Ocupada" => "rs-atn-busy",
        _ => "rs-atn-neutral"
    };
}
