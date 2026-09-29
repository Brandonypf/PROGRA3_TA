package pe.edu.pucp.RinconSatipeno.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Zona;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.EstadoReserva;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.Insumo;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.EstadoMesa;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.Pedido;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.datosContacto;

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

        // 3. Mesa
        Mesa mesa = new Mesa();
        mesa.setId(1);
        mesa.setNumero(4);
        mesa.setCapacidad(4);
        mesa.setZona(Zona.SALON_PRINCIPAL);
        mesa.setEstado(EstadoMesa.OCUPADA);

        // 4. Mozo
        Mozo mozo = new Mozo();
        mozo.setIdEmpleado(1);
        mozo.setNombre("Carlos Mendoza");

        // 5. Datos de contacto y Reserva
        datosContacto contacto = new datosContacto(1, "Ana Torres", "987654321", "ana.torres@example.com");

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1);
        reserva.setFecha(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(13, 0));
        reserva.setHoraFin(LocalTime.of(15, 0));
        reserva.setCantidadPersonas(4);
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reserva.setCodigoAcceso("RES-12345");
        reserva.setMesa(mesa);
        reserva.setDatosContacto(contacto);

        // 6. Cuenta Consumo (Corregido a LocalDateTime y sus FKs)
        CuentaConsumo cuenta = new CuentaConsumo();
        cuenta.setIdCuenta(500);
        cuenta.setEstado(EstadoCuenta.ABIERTA);
        cuenta.setFechaApertura(LocalDateTime.now());
        cuenta.setMontoTotalPagar(32.00);
        cuenta.setMesa(mesa);
        cuenta.setMozo(mozo);
        cuenta.setReserva(reserva);

        // 7. Pedido
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1001); // Se corregió de .set(1001) a .setIdPedido(1001)
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setCuentaConsumo(cuenta);

        // 8. Detalle del Pedido
        DetallePedido detalle = new DetallePedido();
        detalle.setIdDetalle(1);
        detalle.setCantidadPlatos(1);
        detalle.setPrecioUnitario(32.00);
        detalle.setSubtotal(32.00);
        detalle.setPlato(plato);

        // Impresión de resultados
        if (cuenta.getMesa() != null) {
            System.out.println("Pedido N° " + pedido.getIdPedido() + " registrado en Mesa N° " + cuenta.getMesa().getNumero());
        } else {
            System.out.println("Pedido N° " + pedido.getIdPedido() + " registrado sin mesa asignada.");
        }
        System.out.println("Cliente: " + reserva.getDatosContacto().getNombre());
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