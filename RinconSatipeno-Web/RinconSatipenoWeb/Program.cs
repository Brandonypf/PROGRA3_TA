using RinconSatipenoWeb.Components;
using RinconSatipenoWeb.Components.Pages.Atencion;
using RinconSatipenoWeb.Components.Data;

var builder = WebApplication.CreateBuilder(args);

// Igual que en la plantilla utilizada en clase: Blazor Web App con servidor interactivo.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddScoped<EstadoAtencion>();
var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}

app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();
app.UseAntiforgery();
app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();
app.Run();
