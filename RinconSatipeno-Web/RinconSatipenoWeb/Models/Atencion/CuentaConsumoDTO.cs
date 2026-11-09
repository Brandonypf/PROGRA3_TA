namespace RinconSatipenoWeb.Models.Atencion;

public sealed class CuentaConsumoDTO
{
    public int IdCuenta { get; set; }
    public int Mesa { get; set; }
    public string Zona { get; set; } = "";
    public string? Mozo { get; set; }
    public string Estado { get; set; } = "";
    public string? Referencia { get; set; }
    public string? Token { get; set; }
    public string? MedioPago { get; set; }
    public string? TipoComprobante { get; set; }
    public string? Ruc { get; set; }
    public string? RazonSocial { get; set; }
    public string? Fecha { get; set; }
    public decimal Subtotal { get; set; }
    public decimal Igv { get; set; }
    public decimal Total { get; set; }
    public List<DetalleConsumoDTO> Detalles { get; set; } = new();
}
