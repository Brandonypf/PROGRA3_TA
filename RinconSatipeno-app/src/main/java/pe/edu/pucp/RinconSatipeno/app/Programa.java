package pe.edu.pucp.RinconSatipeno.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.Insumo;
import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.DatosContacto;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.EstadoReserva;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;

public class Programa {

    public static void main(String[] args) {
        System.out.println("Probar conexión");
        probarConexion();

        System.out.println("\nPrueba del modelo de dominio");

        // 1. Insumo
        Insumo insumo = new Insumo();
        insumo.setId(1);
        insumo.setNombre("Cecina");
        insumo.setUnidadMedida("KG");
        insumo.setStockActual(15.0);
        insumo.setStockMinimo(3.0);

        // 2. Plato
        Plato plato = new Plato();
        plato.setIdPlato(101);
        plato.setNombre("Tacacho con Cecina");
        plato.setPrecio(32.00);
        plato.setCategoria("PLATO_FONDO");
        plato.setDescripcion("Tacacho tradicional con cecina de la selva.");

        // 3. Mozo
        Mozo mozo = new Mozo();
        mozo.setIdEmpleado(1);
        mozo.setNombre("Carlos Mendoza");

        // 4. DatosContacto y Reserva
        DatosContacto contactoOriginal = new DatosContacto();
        contactoOriginal.setIdContacto(1);
        contactoOriginal.setNombre("Ana Torres");
        contactoOriginal.setTelefono("987654321");
        contactoOriginal.setCorreo("ana.torres@example.com");
        DatosContacto nuevoContacto = new DatosContacto(contactoOriginal);

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1);
        reserva.setFecha(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(13, 0));
        reserva.setHoraFin(LocalTime.of(15, 0));
        reserva.setCantidadPersonas(4);
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reserva.setCodigoAcceso("RES-12345");
        reserva.setDatosContacto(nuevoContacto);

        // 5. CuentaConsumo
        CuentaConsumo cuenta = new CuentaConsumo();
        cuenta.setIdCuenta(500);
        cuenta.setFechaApertura(LocalDateTime.now());
        cuenta.setMontoTotalPagar(32.00);
        cuenta.setMozo(mozo);
        cuenta.setReserva(reserva);

        // 6. DetallePedido
        DetallePedido detalle = new DetallePedido();
        detalle.setIdDetalle(1);
        detalle.setCantidadPlatos(1);
        detalle.setPrecioUnitario(32.00);
        detalle.setSubtotal(32.00);
        detalle.setPlato(plato);

        // Verificación e Impresión
        System.out.println("Cuenta N° " + cuenta.getIdCuenta() + " registrada.");
        System.out.println("Mozo a cargo: " + cuenta.getMozo().getNombre());
        System.out.println("Cliente: " + reserva.getDatosContacto().getNombre());
        System.out.println("Plato ordenado: " + detalle.getPlato().getNombre());
        System.out.println("Insumo principal: " + insumo.getNombre());
        System.out.println("Monto total a pagar en cuenta: S/ " + cuenta.getMontoTotalPagar());
    }

    private static void probarConexion() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            if (con != null && !con.isClosed()) {
                System.out.println("Conexión establecida exitosamente desde DBManager");
            } else {
                System.out.println("No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al obtener la instancia de DBManager: " + e.getMessage());
        }
    }
}