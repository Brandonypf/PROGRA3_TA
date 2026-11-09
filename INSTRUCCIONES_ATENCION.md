# Atención de mesas y pago simulado

## Cambios incluidos

- Asignación de mesas a mozos desde el panel administrativo.
- Panel del mozo limitado a las mesas asignadas a su cuenta.
- Registro de pedidos y consumos mediante servicios REST.
- Resumen de cuenta con valor de venta, IGV y total.
- Selección de boleta o factura y medio de pago.
- Generación de un enlace único para consultar el comprobante.
- Visualización del enlace como código QR.
- Consulta pública del comprobante desde el teléfono del cliente.
- Confirmación del pago por parte del mozo.
- Validación de rol en las páginas y en los servicios REST.
- Enlace de gestión de reserva con código y correo en la URL.

## Base de datos

1. Ejecutar primero `SQL/CodigoSQL/Script.sql` si todavía no se ha creado la base de datos.
2. Ejecutar una sola vez `SQL/CodigoSQL/Atencion-Migracion.sql`.
3. La migración crea `mesa_mozo` y agrega a `cuenta_consumo` los datos del medio de pago y del comprobante.
4. Revisar las dos copias de `db.properties` en `RinconSatipeno-DBManager/src/main/resources/` y `RinconSatipeno-app/src/main/resources/`. Configurar en ambas el servidor, esquema, usuario y contraseña de MySQL. Se entregan con valores de ejemplo para evitar distribuir credenciales.

## Servicios REST Java

El nuevo módulo es `RinconSatipeno-REST`.

Rutas principales:

- `POST /api/v1/sesion/login`
- `POST /api/v1/sesion/logout`
- `GET /api/v1/atencion/mesas`
- `GET /api/v1/atencion/mozos`
- `GET /api/v1/atencion/platos`
- `PUT /api/v1/atencion/mesas/{id}/asignacion`
- `POST /api/v1/atencion/mesas/{id}/iniciar`
- `POST /api/v1/atencion/mesas/{id}/pedidos`
- `GET /api/v1/atencion/mesas/{id}/cuenta`
- `POST /api/v1/atencion/mesas/{id}/pago`
- `POST /api/v1/atencion/mesas/{id}/pago/confirmar`
- `GET /api/v1/comprobantes/{token}`

Los servicios internos requieren un token Bearer obtenido al iniciar sesión. La consulta del comprobante por token es pública para permitir que el cliente abra el QR sin una cuenta de empleado.

El módulo REST debe desplegarse en un servidor compatible con Jakarta REST. Con el nombre de artefacto actual, la URL local esperada es:

`http://localhost:8080/RinconSatipeno-REST/api/v1/`

## FrontEnd Blazor

El proyecto Blazor lee la dirección del servicio desde `RinconSatipeno-Web/RinconSatipenoWeb/appsettings.json`, en `Servicios:AtencionApiUrl`.

Si el servidor REST utiliza otro puerto o contexto, actualizar ese valor.

El flujo de pago es:

1. El mozo inicia la atención de una mesa libre o registra la llegada de una mesa reservada.
2. El mozo registra los consumos.
3. El mozo prepara la cuenta y selecciona el comprobante y medio de pago.
4. El sistema genera un token y muestra el QR.
5. El cliente escanea el QR y consulta el comprobante.
6. El mozo verifica la información y confirma el pago.
7. El sistema marca la cuenta como pagada y libera la mesa.

El QR utiliza un servicio externo para generar la imagen. El contenido codificado es la URL pública de consulta del comprobante. Para escanearlo desde otro teléfono, abrir Blazor mediante una dirección IP de la red local o un dominio accesible desde ese teléfono; una dirección `localhost` solo funciona en el mismo dispositivo.

## Seguridad y alcance

La aplicación no permite elegir un rol en el formulario de ingreso. El rol se determina a partir de la cuenta registrada en MySQL. Las páginas administrativas y de mozo verifican la sesión; además, los servicios REST verifican el rol y la asignación de la mesa antes de aceptar operaciones.

Las sesiones REST se mantienen en memoria del proceso Java. Si se reinicia el servidor REST, los empleados deben iniciar sesión nuevamente. El esquema actual almacena la contraseña en la columna existente y el servicio la compara con ese valor; para producción debe migrarse a contraseñas con hash y un mecanismo de sesión persistente.

El pago es una simulación: no se conecta con bancos, billeteras digitales ni procesadores de pago.

El enlace de gestión de reserva lleva el código y correo en la URL y permite preparar un correo mediante el cliente de correo del usuario. El envío se confirma desde ese cliente; no se configura un servidor SMTP.

## Requisitos para ejecutar

- JDK 26 y Maven para compilar el proyecto Java.
- Un servidor compatible con Jakarta REST para desplegar el WAR.
- MySQL con el esquema del proyecto y la migración aplicada.
- .NET SDK 10 para ejecutar el proyecto Blazor.
- URL del servicio REST configurada en `appsettings.json`.
