using RinconSatipenoWeb.Components;

var builder = WebApplication.CreateBuilder(args);

// Igual que en la plantilla utilizada en clase: Blazor Web App con servidor interactivo.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Laboratorio 9: no hay servicios de negocio, DAO, API ni base de datos.
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
