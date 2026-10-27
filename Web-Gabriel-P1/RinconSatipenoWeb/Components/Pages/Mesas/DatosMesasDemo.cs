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
