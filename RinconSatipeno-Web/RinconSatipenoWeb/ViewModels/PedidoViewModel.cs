using System.ComponentModel.DataAnnotations;

namespace RinconSatipenoWeb.ViewModels;

public sealed class PedidoViewModel
{
    [Range(1, int.MaxValue, ErrorMessage = "Selecciona un plato")]
    public int IdPlato { get; set; }

    [Range(1, 20, ErrorMessage = "La cantidad debe estar entre 1 y 20")]
    public int Cantidad { get; set; } = 1;
}
