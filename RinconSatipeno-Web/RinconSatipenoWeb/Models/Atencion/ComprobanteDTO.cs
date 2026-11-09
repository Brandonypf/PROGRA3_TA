namespace RinconSatipenoWeb.Models.Atencion;

public sealed class ComprobanteDTO
{
    public string Url { get; set; } = "";
    public string Token { get; set; } = "";
    public CuentaConsumoDTO Cuenta { get; set; } = new();
}
