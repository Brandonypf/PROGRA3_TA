namespace RinconSatipenoWeb.Models.Atencion;

public sealed class MesaAtencionDTO
{
    public int Id { get; set; }
    public int Numero { get; set; }
    public int Capacidad { get; set; }
    public string Zona { get; set; } = "";
    public string Estado { get; set; } = "";
    public int? IdMozo { get; set; }
    public string? Mozo { get; set; }
}
