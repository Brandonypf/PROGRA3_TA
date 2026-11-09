package pe.edu.pucp.rinconsatipeno.app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.modelo.empleados.Administrador;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.EstadoEmpleado;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.Insumo;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.MovimientoInventario;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.TipoMovimiento;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Zona;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.EstadoMesa;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.DatosContacto;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.mesas.MesaBL;
import pe.edu.pucp.rinconsatipeno.negocio.mesas.impl.MesaBLImpl;
import pe.edu.pucp.rinconsatipeno.negocio.inventario.InsumoBL;
import pe.edu.pucp.rinconsatipeno.negocio.inventario.impl.InsumoBLImpl;
import pe.edu.pucp.rinconsatipeno.negocio.reservas.DatosContactoBL;
import pe.edu.pucp.rinconsatipeno.negocio.reservas.impl.DatosContactoBLImpl;
import pe.edu.pucp.rinconsatipeno.negocio.empleados.DatosAccesoBL;
import pe.edu.pucp.rinconsatipeno.negocio.empleados.impl.DatosAccesoBLImpl;
import pe.edu.pucp.rinconsatipeno.negocio.empleados.AdministradorBL;
import pe.edu.pucp.rinconsatipeno.negocio.empleados.impl.AdministradorBLImpl;
import pe.edu.pucp.rinconsatipeno.negocio.inventario.MovimientoInventarioBL;
import pe.edu.pucp.rinconsatipeno.negocio.inventario.impl.MovimientoInventarioBLImpl;

public class Programa {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final long RUN = System.currentTimeMillis();

    private static final MesaBL mesaLN = new MesaBLImpl();
    private static final InsumoBL insumoLN = new InsumoBLImpl();
    private static final DatosContactoBL contactoLN = new DatosContactoBLImpl();
    private static final DatosAccesoBL cuentaLN = new DatosAccesoBLImpl();
    private static final AdministradorBL administradorLN = new AdministradorBLImpl();
    private static final MovimientoInventarioBL movimientoLN = new MovimientoInventarioBLImpl();

    public static void main(String[] args) {
        try {
            probarMesas();
            probarInsumos();
            probarContactos();
            probarCuentas();
            probarAdministradores();
            probarMovimientos();
        } catch (BLException ex) {
            System.out.println("La prueba se detuvo por un error: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("La prueba se detuvo por un error: " + ex.getMessage());
        }
    }

    private static void probarMesas() throws BLException {
        titulo("MESA");

        int numero = 1;
        for (Mesa existente : mesaLN.findAll()) {
            if (existente.getNumero() >= numero) {
                numero = existente.getNumero() + 1;
            }
        }

        Mesa mesa = new Mesa();
        mesa.setNumero(numero);
        mesa.setCapacidad(4);
        mesa.setZona(Zona.SALON_PRINCIPAL);
        mesa.setEstado(EstadoMesa.LIBRE);
        mesaLN.insert(mesa);
        System.out.println("Insertado:   " + describir(mesa));

        mesa = mesaLN.findBy(mesa.getId());
        System.out.println("Recuperado:  " + describir(mesa));

        mesa.setCapacidad(6);
        mesa.setZona(Zona.TERRAZA);
        mesaLN.update(mesa);
        System.out.println("Actualizado: " + describir(mesaLN.findBy(mesa.getId())));

        mesaLN.delete(mesa.getId());
        System.out.println("Eliminado:   id " + mesa.getId());

        listarMesas(mesaLN.findAll());
    }

    private static void probarInsumos() throws BLException {
        titulo("INSUMO");

        Insumo insumo = new Insumo();
        insumo.setNombre("Cecina de prueba " + RUN);
        insumo.setUnidadMedida("KG");
        insumo.setStockActual(20.0);
        insumo.setStockMinimo(5.0);
        insumoLN.insert(insumo);
        System.out.println("Insertado:   " + describir(insumo));

        insumo = insumoLN.findBy(insumo.getId());
        System.out.println("Recuperado:  " + describir(insumo));

        insumo.setNombre("Cecina Premium " + RUN);
        insumo.setStockActual(25.5);
        insumo.setStockMinimo(7.0);
        insumoLN.update(insumo);
        System.out.println("Actualizado: " + describir(insumoLN.findBy(insumo.getId())));

        insumoLN.delete(insumo.getId());
        System.out.println("Eliminado:   id " + insumo.getId());

        listarInsumos(insumoLN.findAll());
    }

    private static void probarContactos() throws BLException {
        titulo("DATOS DE CONTACTO");

        DatosContacto contacto = new DatosContacto();
        contacto.setNombre("Ana de prueba " + RUN);
        contacto.setTelefono("987654321");
        contacto.setCorreo("contacto." + RUN + "@example.com");
        contactoLN.insert(contacto);
        System.out.println("Insertado:   " + describir(contacto));

        contacto = contactoLN.findBy(contacto.getIdContacto());
        System.out.println("Recuperado:  " + describir(contacto));

        contacto.setNombre("Ana Torres " + RUN);
        contacto.setTelefono("912345678");
        contacto.setCorreo("contacto.editado." + RUN + "@example.com");
        contactoLN.update(contacto);
        System.out.println("Actualizado: " + describir(contactoLN.findBy(contacto.getIdContacto())));

        contactoLN.delete(contacto.getIdContacto());
        System.out.println("Eliminado:   id " + contacto.getIdContacto());

        listarContactos(contactoLN.findAll());
    }

    private static void probarCuentas() throws BLException {
        titulo("CUENTA DE ACCESO");

        DatosAcceso cuenta = new DatosAcceso();
        cuenta.setEmail("cuenta." + RUN + "@example.com");
        cuenta.setContrasenia("demo123");
        cuenta.setEstado(EstadoEmpleado.ACTIVO);
        cuentaLN.insert(cuenta);
        System.out.println("Insertado:   " + describir(cuenta));

        cuenta = cuentaLN.findBy(cuenta.getIdCuentaAcceso());
        System.out.println("Recuperado:  " + describir(cuenta));

        cuenta.setEmail("cuenta.editada." + RUN + "@example.com");
        cuenta.setContrasenia("nueva456");
        cuenta.setEstado(EstadoEmpleado.INACTIVO);
        cuentaLN.update(cuenta);
        System.out.println("Actualizado: " + describir(cuentaLN.findBy(cuenta.getIdCuentaAcceso())));

        cuentaLN.delete(cuenta.getIdCuentaAcceso());
        System.out.println("Eliminado:   id " + cuenta.getIdCuentaAcceso());

        listarCuentas(cuentaLN.findAll());
    }

    private static void probarAdministradores() throws BLException {
        titulo("ADMINISTRADOR");

        DatosAcceso cuenta = new DatosAcceso();
        cuenta.setEmail("admin." + RUN + "@example.com");
        cuenta.setContrasenia("demo123");
        cuenta.setEstado(EstadoEmpleado.ACTIVO);

        Administrador administrador = new Administrador();
        administrador.setNombre("Administrador de prueba " + RUN);
        administrador.setTelefono("987654321");
        administrador.setFechaDeContratacion(LocalDate.of(2026, 1, 15));
        administrador.setDatosAcceso(cuenta);
        // AdministradorBL inserta tambien su cuenta de acceso.
        administradorLN.insert(administrador);
        System.out.println("Insertado:   " + describir(administrador));

        administrador = administradorLN.findBy(administrador.getIdEmpleado());
        System.out.println("Recuperado:  " + describir(administrador));

        administrador.setNombre("Administrador editado " + RUN);
        administrador.setTelefono("912345678");
        administrador.setFechaDeContratacion(LocalDate.of(2026, 2, 1));
        administrador.getDatosAcceso().setEstado(EstadoEmpleado.INACTIVO);
        administradorLN.update(administrador);
        System.out.println("Actualizado: " + describir(administradorLN.findBy(administrador.getIdEmpleado())));

        administradorLN.delete(administrador.getIdEmpleado());
        System.out.println("Eliminado:   id " + administrador.getIdEmpleado());

        listarAdministradores(administradorLN.findAll());
        // AdministradorBL elimina tambien su cuenta; no se elimina dos veces.
    }

    private static void probarMovimientos() throws BLException {
        titulo("MOVIMIENTO DE INVENTARIO");

        // El movimiento necesita un insumo propio de esta prueba.
        Insumo insumo = new Insumo();
        insumo.setNombre("Arroz de prueba " + RUN);
        insumo.setUnidadMedida("KG");
        insumo.setStockActual(50.0);
        insumo.setStockMinimo(10.0);
        insumoLN.insert(insumo);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setInsumo(insumo);
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setCantidad(15.0);
        movimiento.setFecha(LocalDate.now());
        movimientoLN.insert(movimiento);
        System.out.println("Insertado:   " + describir(movimiento));

        movimiento = movimientoLN.findBy(movimiento.getId());
        System.out.println("Recuperado:  " + describir(movimiento));

        movimiento.setCantidad(20.0);
        movimientoLN.update(movimiento);
        System.out.println("Actualizado: " + describir(movimientoLN.findBy(movimiento.getId())));

        movimientoLN.delete(movimiento.getId());
        System.out.println("Eliminado:   id " + movimiento.getId());

        listarMovimientos(movimientoLN.findAll());
        // Primero se elimina el movimiento y luego el insumo asociado.
        insumoLN.delete(insumo.getId());
    }

    private static void listarMesas(List<Mesa> lista) {
        System.out.println("Listado (" + lista.size() + "):");
        for (Mesa mesa : lista) {
            System.out.println("  " + describir(mesa));
        }
    }

    private static void listarInsumos(List<Insumo> lista) {
        System.out.println("Listado (" + lista.size() + "):");
        for (Insumo insumo : lista) {
            System.out.println("  " + describir(insumo));
        }
    }

    private static void listarContactos(List<DatosContacto> lista) {
        System.out.println("Listado (" + lista.size() + "):");
        for (DatosContacto contacto : lista) {
            System.out.println("  " + describir(contacto));
        }
    }

    private static void listarCuentas(List<DatosAcceso> lista) {
        System.out.println("Listado (" + lista.size() + "):");
        for (DatosAcceso cuenta : lista) {
            System.out.println("  " + describir(cuenta));
        }
    }

    private static void listarAdministradores(List<Administrador> lista) {
        System.out.println("Listado (" + lista.size() + "):");
        for (Administrador administrador : lista) {
            System.out.println("  " + describir(administrador));
        }
    }

    private static void listarMovimientos(List<MovimientoInventario> lista) {
        System.out.println("Listado (" + lista.size() + "):");
        for (MovimientoInventario movimiento : lista) {
            System.out.println("  " + describir(movimiento));
        }
    }

    private static void titulo(String nombre) {
        System.out.println();
        System.out.println("=== " + nombre + " ===");
    }

    private static String describir(Mesa mesa) {
        return String.format("[%d] numero=%d  capacidad=%d  zona=%s  estado=%s",
                mesa.getId(), mesa.getNumero(), mesa.getCapacidad(),
                mesa.getZona(), mesa.getEstado());
    }

    private static String describir(Insumo insumo) {
        return String.format("[%d] %s  unidad=%s  stock=%.2f  minimo=%.2f",
                insumo.getId(), insumo.getNombre(), insumo.getUnidadMedida(),
                insumo.getStockActual(), insumo.getStockMinimo());
    }

    private static String describir(DatosContacto contacto) {
        return String.format("[%d] %s  telefono=%s  correo=%s",
                contacto.getIdContacto(), contacto.getNombre(),
                contacto.getTelefono(), contacto.getCorreo());
    }

    private static String describir(DatosAcceso cuenta) {
        return String.format("[%d] email=%s  estado=%s",
                cuenta.getIdCuentaAcceso(), cuenta.getEmail(), cuenta.getEstado());
    }

    private static String describir(Administrador administrador) {
        return String.format("[%d] %s  telefono=%s  contratacion=%s  email=%s  estado=%s",
                administrador.getIdEmpleado(), administrador.getNombre(),
                administrador.getTelefono(),
                FORMATO_FECHA.format(administrador.getFechaDeContratacion()),
                administrador.getDatosAcceso().getEmail(),
                administrador.getDatosAcceso().getEstado());
    }

    private static String describir(MovimientoInventario movimiento) {
        return String.format("[%d] insumo=%s  tipo=%s  cantidad=%.2f  fecha=%s",
                movimiento.getId(), movimiento.getInsumo().getNombre(),
                movimiento.getTipo(), movimiento.getCantidad(),
                FORMATO_FECHA.format(movimiento.getFecha()));
    }
}