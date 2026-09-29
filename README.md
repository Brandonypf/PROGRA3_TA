# Migración del proyecto al modelo POO actualizado

Esta versión adapta el proyecto antiguo al dominio y SQL actuales.

## Cambios principales

- Se reemplazó la capa Modelo antigua por el modelo POO actualizado.
- Los valores decimales del dominio usan `double`.
- `Empleado` es abstracta y `Administrador`/`Mozo` heredan de ella.
- `CuentaAcceso` se modela por composición dentro de `Empleado`.
- `DatosContacto` ahora incluye `idContacto`, porque `datos_contacto` es una tabla del SQL final.
- Las relaciones del dominio se representan con objetos (`Mesa`, `Mozo`, `Reserva`, `Receta`, etc.) y el DAO traduce estas relaciones a FK.
- Se eliminó `EmpleadoDao`, ya que no existe una tabla `empleado`.
- Se añadieron contratos DAO para `Administrador`, `Mozo`, `CuentaAcceso`, `DatosContacto`, `Reserva`, `Receta`, `DetallePedido` y `RecetaInsumo`.
- `Reserva.java` de AccesoDatos fue sustituido por `ReservaDao.java`.
- `DaoGeneral.update` ahora recibe el objeto completo que se desea actualizar.
- `CuentaConsumoDaoImplement` fue reescrito para trabajar con las columnas y relaciones del SQL actual.
- `CuentaConsumoimpl` fue adaptado al nuevo contrato DAO y valida la coherencia entre estado, pago y cierre.
- `Programa.java` fue actualizado para usar la API actual del modelo.
- El SQL incluido es la versión final con `datos_contacto` y `cuenta_consumo.id_reserva` nullable.
- Los `db.properties` conservan la configuración de conexión original; solo se alineó el nombre del esquema a `rincon_satipeno`.

## Verificación realizada

Se compilaron conjuntamente los módulos Java en este orden:

1. RinconSatipeno-Modelo
2. RinconSatipeno-DBManager
3. RinconSatipeno-AccesoDatos
4. RinconSatipeno-Negocio
5. RinconSatipeno-app

La compilación terminó sin errores.

La conexión real a MySQL no fue validada en el entorno de generación porque el driver JDBC se obtiene mediante Maven y no está cargado en el classpath manual usado para la prueba. El `pom.xml` de DBManager mantiene la dependencia de MySQL Connector/J del proyecto.
