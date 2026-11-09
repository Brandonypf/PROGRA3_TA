namespace RinconSatipenoWeb.Models.Atencion;

public sealed class DetalleConsumoDTO
{
    public string Nombre { get; set; } = "";
    public int Cantidad { get; set; }
    public decimal PrecioUnitario { get; set; }
    public decimal Importe { get; set; }
}
