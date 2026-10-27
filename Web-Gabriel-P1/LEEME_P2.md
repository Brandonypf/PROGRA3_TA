# El Rincón Satipeño — P2 integrado

Se amplió la solución original: un único proyecto Blazor Web App, sin crear otro proyecto.

## 1. Estructura y rutas

Todas las ubicaciones siguientes son relativas a `RinconSatipenoWeb/`.

| Archivo | Ruta / finalidad |
|---|---|
| Components/Layout/AdminLayout.razor | Layout administrativo único, barra superior y contenido |
| Components/Layout/AdminNavMenu.razor | Menú lateral compartido |
| Components/Pages/Mesas/CalendarioReservas.razor | /admin/reservas/calendario |
| Components/Pages/Mesas/EstadoMesas.razor | /admin/mesas/estado y /admin |
| Components/Pages/Mesas/GestionMesas.razor | /admin/mesas |
| Components/Pages/Mesas/_Imports.razor | Aplica AdminLayout a las tres páginas |
| Components/Pages/Mesas/DatosMesasDemo.cs | Registros ficticios de presentación compartidos |
| Components/Pages/Inventario/_Imports.razor | Reutiliza AdminLayout en las páginas existentes |
| wwwroot/css/admin.css | Paleta, layout y ajustes responsive |
| wwwroot/lib/bootstrap/ | Bootstrap 5.3.8 CSS y JS locales |
| wwwroot/lib/bootstrap-icons/ | Bootstrap Icons 1.11.3, CSS y fuentes locales |
| Components/App.razor | Referencias locales a Bootstrap, iconos y estilos |
| Components/Layout/ClienteLayout.razor | Enlace añadido a Administración (demo) |

Las páginas de Cliente e Inventario conservan sus nombres, rutas y código. Se conservan Program.cs, Routes.razor, el .csproj y el .sln originales. El layout de cliente sigue asignado a Cliente; Inventario y Mesas reciben AdminLayout mediante sus propios _Imports.razor. El menú lateral se define una sola vez.

## 2. Abrir en Visual Studio Community

1. Extraer todo el ZIP antes de abrirlo.
2. Abrir `Web-Gabriel-P1/RinconSatipenoWeb.sln`.
3. Usar Visual Studio Community 2026 (18.0 o superior), con la carga de trabajo **Desarrollo de ASP.NET y web** y el SDK .NET 10. El ZIP original ya apuntaba a `net10.0`; no se cambió su versión.
4. Establecer `RinconSatipenoWeb` como proyecto de inicio si Visual Studio lo solicita.
5. Compilar la solución (Ctrl+Mayús+B). La primera restauración del SDK puede requerir internet.
6. Ejecutar el perfil `http` (Ctrl+F5). URL configurada: `http://localhost:5194`.
7. Pulsar **Administración (demo)** o abrir `http://localhost:5194/admin/mesas/estado`.

Desde terminal, dentro de la carpeta que contiene la solución:

```bash
dotnet restore RinconSatipenoWeb.sln
dotnet build RinconSatipenoWeb.sln
dotnet run --project RinconSatipenoWeb --launch-profile http
```

Referencia de compatibilidad: https://learn.microsoft.com/en-us/dotnet/core/install/windows#supported-versions

No es necesario crear otro proyecto, instalar Bootstrap desde NuGet ni configurar una base de datos. No cambiar solamente net10.0 por otra versión: la plantilla original usa características de .NET 10.

## 3. Comportamiento de cada pantalla

- **Calendario:** cuadrícula mensual de lunes a domingo, botones anterior/siguiente, selección de día y tabla de reservas. Octubre de 2026 contiene los ejemplos. Otros meses muestran una vista vacía intencional. El botón Mes de ejemplo vuelve al 26/10/2026.
- **Estado:** ocho mesas, cuatro libres, dos ocupadas y dos reservadas; filtros en memoria por ambiente y estado, tarjetas y detalle. Se muestra explícitamente una foto ficticia del 26/10/2026 a las 13:00.
- **Gestión:** listado, búsqueda local por número o ambiente, filtro de estado, formulario de registro y edición precargada y confirmación visual de eliminación. Guardar y Eliminar solo muestran mensajes. Ni siquiera se cambia la colección de ejemplo.
- **Layout:** enlaces reales a las páginas existentes de inventario y cliente. En pantallas pequeñas el menú se sitúa arriba y puede plegarse pulsando Menú de administración. Tablas y calendario tienen desplazamiento horizontal interno.

RF001, RF004 y RF005 se cubren mediante las tres pantallas solicitadas; no se atribuye una numeración individual sin el texto original de cada requisito.

## 4. Límites del laboratorio

Solo presentación: no se agregan bases de datos, DAO, servicios de negocio, API, login, correos o pagos. `DatosMesasDemo.cs` contiene únicamente registros ficticios y una función para elegir clases CSS. Los filtros, selección de fecha y apertura de tarjetas son estado de interfaz en memoria.

InteractiveServer viene de la plantilla original. Su conexión Blazor mantiene la interfaz interactiva con el servidor local de la aplicación; no es una integración con servicios de negocio externos. No se añade @rendermode a los layouts, pues Routes ya aplica InteractiveServer globalmente.

P2 y sus estilos/iconos funcionan sin CDN. Las fotos remotas de Unsplash que traían las páginas de cliente se conservaron y necesitan internet; son ajenas a P2. No se incorporaron pantallas de empleados, pagos o atención que no estuvieran en el ZIP, ni enlaces a destinos inexistentes.

## 5. Cómo integrar el trabajo del equipo

Para otra carpeta administrativa, agregar un _Imports.razor con `@layout AdminLayout`, o esa directiva en una página aislada. Agregar su NavLink en AdminNavMenu.razor cuando exista su ruta. No copiar la barra lateral en cada página.

Los cambios transversales son App.razor (recursos locales y admin.css), ClienteLayout.razor (enlace administrativo) y el nuevo _Imports de Inventario. Las otras páginas originales no se reescribieron. Se quitó la referencia al CSS aislado RinconSatipenoWeb.styles.css porque el proyecto no contiene archivos .razor.css que lo generen; si el equipo añade CSS aislado, debe volver a incluir esa referencia.

## 6. Código completo

Abrir `CODIGO_COMPLETO_P2.md`: contiene la estructura seguida por el código íntegro de cada archivo nuevo o modificado, en el mismo orden de integración. Los archivos reales de la solución ya contienen ese código; no hace falta copiarlo manualmente.
