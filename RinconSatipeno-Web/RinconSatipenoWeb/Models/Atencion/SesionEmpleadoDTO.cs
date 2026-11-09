using System.Text.Json.Serialization;

namespace RinconSatipenoWeb.Models.Atencion;

public sealed class SesionEmpleadoDTO
{
    [JsonPropertyName("token")]
    public string Token { get; set; } = "";

    [JsonPropertyName("rol")]
    public string Rol { get; set; } = "";

    [JsonPropertyName("idEmpleado")]
    public int IdEmpleado { get; set; }

    [JsonPropertyName("nombre")]
    public string Nombre { get; set; } = "";

    [JsonPropertyName("correo")]
    public string Correo { get; set; } = "";
}