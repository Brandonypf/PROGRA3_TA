using RinconSatipenoWeb.Components;
using RinconSatipenoWeb.Servicios.Rest;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddScoped<SesionEmpleadoService>();
builder.Services.AddHttpClient<AtencionApiService>(client =>
{
    var apiUrl = builder.Configuration["Servicios:AtencionApiUrl"]
        ?? "http://localhost:8080/RinconSatipeno-REST-1.0-SNAPSHOT/api/v1/";
    client.BaseAddress = new Uri(apiUrl.TrimEnd('/') + "/");
    client.Timeout = TimeSpan.FromSeconds(30);
});

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
