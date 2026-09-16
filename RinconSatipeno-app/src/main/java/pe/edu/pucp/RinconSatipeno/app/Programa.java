package pe.edu.pucp.RinconSatipeno.app;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Empleado;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.EstadoEmpleado;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Administrador;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Turno;

import pe.edu.pucp.RinconSatipeno.Modelo.inventario.TipoMovimiento;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.Insumo;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.MovimientoInventario;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.EstadoMesa;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Receta;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.RecetaInsumo;

import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.Pedido;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;

public class Programa {

    public static void main(String[] args) {
        System.out.println("Probar conexión");
        probarConexion();



        System.out.println("\nPrueba del modelo de dominio");

        // 1. Insumo
        Insumo insumo = new Insumo();
        insumo.setId(1);
        insumo.setNombre("Cecina");
        insumo.setUnidadMedida("Kg");
        insumo.setStockActual(15.0);
        insumo.setStockMinimo(3.0);

        // 2. Plato
        Plato plato = new Plato();
        plato.setIdPlato(101);
        plato.setNombre("Tacacho con Cecina");
        plato.setPrecio(32.00);
        plato.setCategoria("Platos Fuertes");
        plato.setDescripcion("Tacacho tradicional con cecina de la selva.");

        // 3. Mesa
        Mesa mesa = new Mesa();
        mesa.setId(1);
        mesa.setNumero(4);
        mesa.setCapacidad(4);
        mesa.setEstado(EstadoMesa.OCUPADA);

        // 4. Cuenta Consumo
        CuentaConsumo cuenta = new CuentaConsumo();
        cuenta.setIdCuenta(500);
        cuenta.setEstado(EstadoCuenta.ABIERTA);

        // Asignación explícita de la fecha.
        if (cuenta != null) {
            cuenta.setFechaApertura(LocalDate.now());
        }

        // 5. Pedido
        Pedido pedido = new Pedido();
        pedido.setId(1001);
        pedido.setMesa(mesa);
        pedido.setCuentaConsumo(cuenta);
        pedido.setPrecioTotal(32.00);

        // Impresión de resultados
        if (pedido.getMesa() != null) {
            System.out.println("Pedido " + pedido.getId() + " registrado en Mesa N° " + pedido.getMesa().getNumero());
        } else {
            System.out.println("Pedido " + pedido.getId() + " registrado sin mesa asignada.");
        }
        System.out.println("Total: S/ " + pedido.getPrecioTotal());
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