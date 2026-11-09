using System.ComponentModel.DataAnnotations;

namespace RinconSatipenoWeb.ViewModels;

public sealed class PagoViewModel : IValidatableObject
{
    [Required(ErrorMessage = "Selecciona un medio de pago")]
    public string MedioPago { get; set; } = "Efectivo";

    [Required(ErrorMessage = "Selecciona el tipo de comprobante")]
    public string TipoComprobante { get; set; } = "Boleta";

    public string Ruc { get; set; } = "";
    public string RazonSocial { get; set; } = "";

    public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
    {
        if (TipoComprobante == "Factura")
        {
            if (string.IsNullOrWhiteSpace(Ruc) || Ruc.Length != 11 || !Ruc.All(char.IsDigit))
                yield return new ValidationResult("El RUC debe contener 11 dígitos", new[] { nameof(Ruc) });

            if (string.IsNullOrWhiteSpace(RazonSocial))
                yield return new ValidationResult("Ingresa la razón social", new[] { nameof(RazonSocial) });
        }
    }
}
