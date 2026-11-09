using System.ComponentModel.DataAnnotations;

namespace RinconSatipenoWeb.ViewModels;

public sealed class LoginViewModel
{
    [Required(ErrorMessage = "Ingresa tu correo")]
    [EmailAddress(ErrorMessage = "Ingresa un correo válido")]
    public string Correo { get; set; } = "";

    [Required(ErrorMessage = "Ingresa tu contraseña")]
    public string Contrasenia { get; set; } = "";
}
