using RinconSatipenoWeb.Models.Atencion;

namespace RinconSatipenoWeb.Servicios.Rest;

public sealed class SesionEmpleadoService
{
    public SesionEmpleadoDTO? Sesion { get; private set; }
    public bool EstaAutenticado => Sesion is not null && !string.IsNullOrWhiteSpace(Sesion.Token);
    public bool EsAdministrador => EstaAutenticado && Sesion!.Rol == "Administrador";
    public bool EsMozo => EstaAutenticado && Sesion!.Rol == "Mozo";

    public void Iniciar(SesionEmpleadoDTO sesion)
    {
        Sesion = sesion;
    }

    public void Cerrar()
    {
        Sesion = null;
    }
}
