using System;
using System.Collections.Generic;
using System.Linq;
using RinconSatipenoWeb.Components.Pages.Mesas;

namespace RinconSatipenoWeb.Components.Data;

public sealed class ReservaCliente
{
    public string Codigo { get; set; } = "";
    public string Acceso { get; set; } = "consulta";
    public string Nombre { get; set; } = "";
    public string Correo { get; set; } = "";
    public string Telefono { get; set; } = "";
    public DateTime Fecha { get; set; }
    public string Hora { get; set; } = "";
    public int Personas { get; set; }
    public int NumeroMesa { get; set; }
    public string Zona { get; set; } = "";
    public string Estado { get; set; } = "Confirmada";
    public string Observaciones { get; set; } = "";
}

// Información de ejemplo que se muestra en las pantallas de reservas.
public sealed class EstadoReservas
{
    public static ReservaCliente Ejemplo => new()
    {
        Codigo = "SAT-2026-001", Nombre = "María López",
        Correo = "maria.lopez@gmail.com", Telefono = "987654321",
        Fecha = DateTime.Today.AddDays(1), Hora = "19:00", Personas = 2,
        NumeroMesa = 1, Zona = "Salón principal"
    };
}
