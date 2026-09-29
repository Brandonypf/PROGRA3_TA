package pe.edu.pucp.RinconSatipeno.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.MovimientoInventario;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.TipoMovimiento;
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
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.InsumoLN;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.MovimientoInventarioLN;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl.Insumoimpl;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl.MovimientoInventarioimpl;

public class Programa {
    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final long RUN = System.currentTimeMillis() % 1_000_000L;

    private static final InsumoLN insumoLN = new Insumoimpl();
    private static final MovimientoInventarioLN movimientoInventarioLN = new MovimientoInventarioimpl();
    public static void main(String[] args) {
        System.out.println("Probar conexión");
        probarConexion();

        System.out.println("\nPrueba del modelo de dominio\n");
        /*
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

         */
        try {
            probarInsumos();
            probarMovimientosInventario();
            probarReglasDeNegocio();
        } catch (BLException ex) {
            System.out.println("La prueba se detuvo por un error: " + ex.getMessage());
        }
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

    private static void probarInsumos() throws BLException {
        titulo("INSUMO");

        Insumo insumo = new Insumo();
        insumo.setNombre("Cecina");
        insumo.setUnidadMedida("KG");
        insumo.setStockActual(20.0);
        insumo.setStockMinimo(5.0);

        insumoLN.insert(insumo);
        System.out.println("Insertado:  " + describir(insumo));

        insumo = insumoLN.findBy(insumo.getId());
        System.out.println("Recuperado: " + describir(insumo));

        insumo.setNombre("Cecina Premium " + RUN);
        insumo.setStockActual(25.5);
        insumoLN.update(insumo);
        System.out.println("Actualizado: " + describir(insumoLN.findBy(insumo.getId())));

        insumoLN.delete(insumo.getId());
        System.out.println("Eliminado:   id " + insumo.getId());

        listarInsumos(insumoLN.findAll());
    }

    private static void probarMovimientosInventario() throws BLException {
        titulo("MOVIMIENTO DE INVENTARIO");

        Insumo insumo = new Insumo();
        insumo.setNombre("Juane " + RUN);
        insumo.setUnidadMedida("UND");
        insumo.setStockActual(50.0);
        insumo.setStockMinimo(10.0);
        insumoLN.insert(insumo);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setInsumo(insumo);
        movimiento.setTipo(TipoMovimiento.ENTRADA); // Ajusta a ENTRADA/SALIDA o el nombre de tu enum
        movimiento.setCantidad(15.0);
        movimiento.setFechaRegistro(LocalDateTime.now());

        movimientoInventarioLN.insert(movimiento);
        System.out.println("Insertado:  " + describir(movimiento));

        movimiento = movimientoInventarioLN.findBy(movimiento.getId());
        System.out.println("Recuperado: " + describir(movimiento));

        movimiento.setCantidad(20.0);
        movimientoInventarioLN.update(movimiento);

        MovimientoInventario movActualizado = movimientoInventarioLN.findBy(movimiento.getId());
        System.out.println("Actualizado: " + describir(movActualizado));

        movimientoInventarioLN.delete(movimiento.getId());
        System.out.println("Eliminado:   id " + movimiento.getId());

        listarMovimientos(movimientoInventarioLN.findAll());

        insumoLN.delete(insumo.getId());
    }

    private static void probarReglasDeNegocio() {
        titulo("REGLAS DE NEGOCIO");

        // Regla 1: Insumo con stock mínimo negativo
        try {
            Insumo insumo = new Insumo();
            insumo.setNombre("Insumo Inválido " + RUN);
            insumo.setUnidadMedida("KG");
            insumo.setStockActual(10.0);
            insumo.setStockMinimo(-5.0);
            insumoLN.insert(insumo);
            System.out.println("Insumo con stock mínimo negativo: NO se rechazó (falla la regla)");
        } catch (BLException ex) {
            System.out.println("Insumo con stock mínimo negativo -> Rechazado: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Insumo con stock mínimo negativo -> Rechazado por Modelo: " + ex.getMessage());
        }

        // Regla 2: Insumo con nombre nulo o vacío
        try {
            Insumo insumo = new Insumo();
            insumo.setNombre("");
            insumo.setUnidadMedida("KG");
            insumo.setStockActual(10.0);
            insumo.setStockMinimo(2.0);
            insumoLN.insert(insumo);
            System.out.println("Insumo sin nombre: NO se rechazó (falla la regla)");
        } catch (BLException ex) {
            System.out.println("Insumo sin nombre -> Rechazado: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Insumo sin nombre -> Rechazado por Modelo: " + ex.getMessage());
        }

        // Regla 3: Búsqueda con ID inválido
        try {
            insumoLN.findBy(-1);
            System.out.println("Búsqueda con ID negativo: NO se rechazó (falla la regla)");
        } catch (BLException ex) {
            System.out.println("Búsqueda con ID negativo -> Rechazado: " + ex.getMessage());
        }
    }

    private static void listarInsumos(List<Insumo> insumos) {
        System.out.println("Listado (" + insumos.size() + "):");
        for (Insumo insumo : insumos) {
            System.out.println("  " + describir(insumo));
        }
    }

    private static void listarMovimientos(List<MovimientoInventario> movimientos) {
        System.out.println("Listado (" + movimientos.size() + "):");
        for (MovimientoInventario movimiento : movimientos) {
            System.out.println("  " + describir(movimiento));
        }
    }

    private static void titulo(String nombre) {
        System.out.println();
        System.out.println("=== " + nombre + " ===");
    }

    private static String describir(Insumo insumo) {
        return String.format("[%d] %-28s %-5s stockAct=%.2f  stockMin=%.2f",
                insumo.getId(),
                insumo.getNombre(),
                insumo.getUnidadMedida(),
                insumo.getStockActual(),
                insumo.getStockMinimo());
    }

    private static String describir(MovimientoInventario mov) {
        String insumoNombre = mov.getInsumo() != null ? mov.getInsumo().getNombre() : "(sin insumo)";
        String fechaFormat = mov.getFechaRegistro() != null ? FORMATO_FECHA_HORA.format(mov.getFechaRegistro()) : "N/A";
        return String.format("[%d] insumo=%s  tipo=%s  cant=%.2f  fecha=%s",
                mov.getId(),
                insumoNombre,
                mov.getTipo(),
                mov.getCantidad(),
                fechaFormat);
    }

}