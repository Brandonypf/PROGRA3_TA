using System.Net;
using System.Net.Http.Headers;
using System.Net.Http.Json;
using RinconSatipenoWeb.Models.Atencion;

namespace RinconSatipenoWeb.Servicios.Rest;

public sealed class AtencionApiService
{
    private readonly HttpClient http;
    private readonly SesionEmpleadoService sesion;

    public AtencionApiService(HttpClient http, SesionEmpleadoService sesion)
    {
        this.http = http;
        this.sesion = sesion;
    }

    public async Task<SesionEmpleadoDTO> IniciarSesionAsync(string correo, string contrasenia)
    {
        http.DefaultRequestHeaders.Authorization = null;
        using var response = await http.PostAsJsonAsync("sesion/login", new { correo, contrasenia });
        await VerificarRespuesta(response);
        return (await response.Content.ReadFromJsonAsync<SesionEmpleadoDTO>())!;
    }

    public async Task CerrarSesionAsync()
    {
        PrepararSesion();
        using var response = await http.PostAsJsonAsync("sesion/logout", new { });
        await VerificarRespuesta(response);
        http.DefaultRequestHeaders.Authorization = null;
        sesion.Cerrar();
    }

    public async Task<List<MesaAtencionDTO>> ObtenerMesasAsync()
    {
        PrepararSesion();
        return await http.GetFromJsonAsync<List<MesaAtencionDTO>>("atencion/mesas") ?? new();
    }

    public async Task<List<MozoDTO>> ObtenerMozosAsync()
    {
        PrepararSesion();
        return await http.GetFromJsonAsync<List<MozoDTO>>("atencion/mozos") ?? new();
    }

    public async Task<List<ProductoAtencionDTO>> ObtenerPlatosAsync()
    {
        PrepararSesion();
        return await http.GetFromJsonAsync<List<ProductoAtencionDTO>>("atencion/platos") ?? new();
    }

    public async Task AsignarMozoAsync(int idMesa, int idMozo)
    {
        PrepararSesion();
        using var response = await http.PutAsJsonAsync($"atencion/mesas/{idMesa}/asignacion", new { idMozo });
        await VerificarRespuesta(response);
    }

    public async Task IniciarAtencionAsync(int idMesa)
    {
        PrepararSesion();
        using var response = await http.PostAsJsonAsync($"atencion/mesas/{idMesa}/iniciar", new { });
        await VerificarRespuesta(response);
    }

    public async Task AgregarPedidoAsync(int idMesa, int idPlato, int cantidad)
    {
        PrepararSesion();
        using var response = await http.PostAsJsonAsync($"atencion/mesas/{idMesa}/pedidos", new { idPlato, cantidad });
        await VerificarRespuesta(response);
    }

    public async Task<CuentaConsumoDTO> ObtenerCuentaAsync(int idMesa)
    {
        PrepararSesion();
        using var response = await http.GetAsync($"atencion/mesas/{idMesa}/cuenta");
        await VerificarRespuesta(response);
        return (await response.Content.ReadFromJsonAsync<CuentaConsumoDTO>())!;
    }

    public async Task<ComprobanteDTO> GenerarPagoAsync(int idMesa, string medioPago, string tipoComprobante, string ruc, string razonSocial)
    {
        PrepararSesion();
        using var response = await http.PostAsJsonAsync($"atencion/mesas/{idMesa}/pago", new
        {
            medioPago,
            tipoComprobante,
            ruc,
            razonSocial
        });
        await VerificarRespuesta(response);
        return (await response.Content.ReadFromJsonAsync<ComprobanteDTO>())!;
    }

    public async Task ConfirmarPagoAsync(int idMesa)
    {
        PrepararSesion();
        using var response = await http.PostAsJsonAsync($"atencion/mesas/{idMesa}/pago/confirmar", new { });
        await VerificarRespuesta(response);
    }

    public async Task<CuentaConsumoDTO> ObtenerComprobanteAsync(string token)
    {
        using var response = await http.GetAsync($"comprobantes/{Uri.EscapeDataString(token)}");
        await VerificarRespuesta(response);
        return (await response.Content.ReadFromJsonAsync<CuentaConsumoDTO>())!;
    }

    private void PrepararSesion()
    {
        if (!sesion.EstaAutenticado)
            throw new InvalidOperationException("La sesión ha finalizado. Vuelve a iniciar sesión.");

        http.DefaultRequestHeaders.Authorization =
            new AuthenticationHeaderValue("Bearer", sesion.Sesion!.Token);
    }

    private static async Task VerificarRespuesta(HttpResponseMessage response)
    {
        if (response.IsSuccessStatusCode) return;

        var mensaje = "No se pudo completar la operación.";
        try
        {
            var error = await response.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            if (error is not null && error.TryGetValue("error", out var detalle))
                mensaje = detalle;
        }
        catch
        {
        }

        if (response.StatusCode == HttpStatusCode.Unauthorized && mensaje == "No se pudo completar la operación.")
            mensaje = "La sesión no es válida. Vuelve a iniciar sesión.";

        throw new InvalidOperationException(mensaje);
    }
}
