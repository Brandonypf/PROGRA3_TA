package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.Pedido;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.PedidoLN;
import pe.edu.pucp.rinconSatipeno.dao.CuentaConsumoDao;
import pe.edu.pucp.rinconSatipeno.dao.PedidoDao;
import pe.edu.pucp.rinconSatipeno.dao.PlatoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.CuentaConsumoDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.impl.PedidoDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.impl.PlatoDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.transacciones.TransactionsManager;

public class Pedidoimpl implements PedidoLN {

    //Nos aseguramos que la clase solo sea asignada una vez (durante su creación)
    private final PedidoDao pedidoDAO = new PedidoDaoImplement();
    private final CuentaConsumoDao cuentaDAO = new CuentaConsumoDaoImplement();
    private final PlatoDao platoDAO = new PlatoDaoImplement();

    @Override
    public List<Pedido> findAll() throws BLException {
        try {
            return pedidoDAO.findAll();
        } catch (SQLException variableException) {
            throw new BLException("No se pudo listar todos los pedidos", variableException);
        }
    }

    @Override
    public Pedido findBy(Integer id) throws BLException {
        try {
            return pedidoDAO.findBy(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo encontrar el pedido", variableException);
        }
    }

    @Override
    public void insert(Pedido pedido) throws BLException {
        validarYCalcular(pedido);

        TransactionsManager.iniciar();
        try {
            pedidoDAO.insert(pedido);
            TransactionsManager.commit();
        } catch (SQLException variableException) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo insertar el pedido", variableException);
        }
    }

    @Override
    public void update(Pedido aux) throws BLException {
        Pedido existente = validarExiste(aux.getIdPedido());
        validarCuentaAbierta(existente.getCuentaConsumo());   // no se edita un pedido de una cuenta ya cerrada/pagada
        validarYCalcular(aux);

        TransactionsManager.iniciar();
        try {
            pedidoDAO.update(aux);
            TransactionsManager.commit();
        } catch (SQLException variableException) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo actualizar el pedido", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        Pedido pedido = validarExiste(id);
        validarCuentaAbierta(pedido.getCuentaConsumo());

        TransactionsManager.iniciar();
        try {
            pedidoDAO.delete(id);
            TransactionsManager.commit();
        } catch (SQLException variableException) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo eliminar el pedido", variableException);
        }
    }

    // Devuelve el pedido si existe; si no, lanza BLException
    public Pedido validarExiste(int id) throws BLException {
        Pedido pedido = findBy(id);
        if (pedido == null) {
            throw new BLException("No se encontró la existencia del pedido");
        }
        return pedido;
    }

    private void validarYCalcular(Pedido pedido) throws BLException {
        validarCuentaAbierta(pedido.getCuentaConsumo());

        if (pedido.getDetalles().isEmpty()) {
            throw new BLException("El pedido debe tener al menos un detalle");
        }

        // El precio y el subtotal no se confían al cliente: se recalculan con el precio actual del plato
        List<DetallePedido> detallesCalculados = new ArrayList<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getCantidadPlatos() < 1) {
                throw new BLException("La cantidad de cada detalle debe ser al menos 1");
            }

            Plato plato = buscarPlato(detalle.getPlato().getIdPlato());
            double precio = plato.getPrecio();

            DetallePedido detalleCalculado = new DetallePedido(detalle);
            detalleCalculado.setPrecioUnitario(precio);
            detalleCalculado.setSubtotal(precio * detalle.getCantidadPlatos());
            detallesCalculados.add(detalleCalculado);
        }
        pedido.setDetalles(detallesCalculados);
    }

    // Un pedido solo puede pertenecer a una cuenta de consumo existente y ABIERTA
    private void validarCuentaAbierta(CuentaConsumo cuenta) throws BLException {
        if (cuenta == null) {
            throw new BLException("El pedido debe tener una cuenta de consumo");
        }
        CuentaConsumo actual;
        try {
            actual = cuentaDAO.findBy(cuenta.getIdCuenta());
        } catch (SQLException variableException) {
            throw new BLException("No se pudo verificar la cuenta de consumo", variableException);
        }
        if (actual == null) {
            throw new BLException("La cuenta de consumo del pedido no existe");
        }
        if (actual.getEstado() != EstadoCuenta.ABIERTA) {
            throw new BLException("Solo se pueden registrar o modificar pedidos en una cuenta ABIERTA");
        }
    }

    private Plato buscarPlato(int idPlato) throws BLException {
        try {
            Plato plato = platoDAO.findBy(idPlato);
            if (plato == null) {
                throw new BLException("No existe un plato con id " + idPlato);
            }
            return plato;
        } catch (SQLException variableException) {
            throw new BLException("No se pudo recuperar el plato del detalle", variableException);
        }
    }
}