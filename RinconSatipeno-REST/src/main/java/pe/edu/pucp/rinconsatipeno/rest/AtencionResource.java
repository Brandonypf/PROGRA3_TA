package pe.edu.pucp.rinconsatipeno.rest;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.rest.dto.AsignacionDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.CuentaDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.DetalleConsumoDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.MesaDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.MozoDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.PagoDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.PedidoDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.PlatoDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.SesionDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/atencion")
@Produces("application/json")
@Consumes("application/json")
public class AtencionResource {

    @GET
    @Path("/mesas")
    public Response listarMesas(@HeaderParam("Authorization") MysqlxDatatypes.Scalar.String authorization) {
        SesionDTO sesion = SesionActiva.obtener(String.valueOf(authorization));
        if (sesion == null) return noAutorizado();

        String sql = "SELECT m.id_mesa, m.numero, m.capacidad, m.zona, m.estado, " +
                "mm.id_mozo, mo.nombre AS nombre_mozo " +
                "FROM mesa m LEFT JOIN mesa_mozo mm ON mm.id_mesa = m.id_mesa " +
                "LEFT JOIN mozo mo ON mo.id_mozo = mm.id_mozo " +
                (sesion.getRol().equals("Mozo") ? "WHERE mm.id_mozo = ? " : "") +
                "ORDER BY m.numero";

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (sesion.getRol().equals("Mozo")) ps.setInt(1, sesion.getIdEmpleado());
            try (ResultSet rs = ps.executeQuery()) {
                List<MesaDTO> mesas = new ArrayList<>();
                while (rs.next()) {
                    MesaDTO mesa = new MesaDTO();
                    mesa.setId(rs.getInt("id_mesa"));
                    mesa.setNumero(rs.getInt("numero"));
                    mesa.setCapacidad(rs.getInt("capacidad"));
                    mesa.setZona(rs.getString("zona"));
                    mesa.setEstado(rs.getString("estado"));
                    int idMozo = rs.getInt("id_mozo");
                    mesa.setIdMozo(rs.wasNull() ? null : idMozo);
                    mesa.setMozo(rs.getString("nombre_mozo"));
                    mesas.add(mesa);
                }
                return Response.ok(mesas).build();
            }
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @GET
    @Path("/platos")
    public Response listarPlatos(@HeaderParam("Authorization") String authorization) {
        if (SesionActiva.obtener(authorization) == null) return noAutorizado();
        String sql = "SELECT id_plato, nombre, precio FROM plato ORDER BY categoria, nombre";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<PlatoDTO> platos = new ArrayList<>();
            while (rs.next()) platos.add(new PlatoDTO(rs.getInt("id_plato"), rs.getString("nombre"), rs.getBigDecimal("precio")));
            return Response.ok(platos).build();
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @GET
    @Path("/mozos")
    public Response listarMozos(@HeaderParam("Authorization") String authorization) {
        SesionDTO sesion = SesionActiva.obtener(authorization);
        if (sesion == null) return noAutorizado();
        if (!sesion.getRol().equals("Administrador")) return prohibido();

        String sql = "SELECT id_mozo, nombre FROM mozo ORDER BY nombre";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<MozoDTO> mozos = new ArrayList<>();
            while (rs.next()) mozos.add(new MozoDTO(rs.getInt("id_mozo"), rs.getString("nombre")));
            return Response.ok(mozos).build();
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @PUT
    @Path("/mesas/{id}/asignacion")
    public Response asignarMozo(@HeaderParam("Authorization") String authorization,
                                @PathParam("id") int idMesa, AsignacionDTO asignacion) {
        SesionDTO sesion = SesionActiva.obtener(authorization);
        if (sesion == null) return noAutorizado();
        if (!sesion.getRol().equals("Administrador")) return prohibido();
        if (idMesa <= 0 || asignacion == null || asignacion.getIdMozo() < 0) return solicitudInvalida("Selecciona una mesa y un mozo válidos");

        String sql = "INSERT INTO mesa_mozo(id_mesa, id_mozo) VALUES(?, ?) " +
                "ON DUPLICATE KEY UPDATE id_mozo = VALUES(id_mozo)";
        try (Connection con = DBManager.getInstance().getConnection()) {
            if (!existe(con, "SELECT 1 FROM mesa WHERE id_mesa = ?", idMesa)) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encontró la mesa")).build();
            }
            if (asignacion.getIdMozo() == 0) {
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM mesa_mozo WHERE id_mesa = ?")) {
                    ps.setInt(1, idMesa);
                    ps.executeUpdate();
                }
                return Response.noContent().build();
            }
            if (!existe(con, "SELECT 1 FROM mozo WHERE id_mozo = ?", asignacion.getIdMozo())) {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encontró el mozo")).build();
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMesa);
                ps.setInt(2, asignacion.getIdMozo());
                ps.executeUpdate();
            }
            return Response.noContent().build();
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @POST
    @Path("/mesas/{id}/iniciar")
    public Response iniciarAtencion(@HeaderParam("Authorization") String authorization,
                                    @PathParam("id") int idMesa) {
        SesionDTO sesion = SesionActiva.obtener(authorization);
        if (sesion == null) return noAutorizado();
        if (!sesion.getRol().equals("Mozo")) return prohibido();
        try (Connection con = DBManager.getInstance().getConnection()) {
            if (!mesaAsignada(con, idMesa, sesion.getIdEmpleado())) return prohibido();
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE mesa SET estado = 'OCUPADA' WHERE id_mesa = ? AND estado IN ('LIBRE', 'RESERVADA')")) {
                ps.setInt(1, idMesa);
                if (ps.executeUpdate() == 0) {
                    return solicitudInvalida("La mesa ya tiene una atención activa o no está disponible");
                }
            }
            return Response.noContent().build();
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @POST
    @Path("/mesas/{id}/pedidos")
    public Response agregarPedido(@HeaderParam("Authorization") String authorization,
                                  @PathParam("id") int idMesa, PedidoDTO pedido) {
        SesionDTO sesion = SesionActiva.obtener(authorization);
        if (sesion == null) return noAutorizado();
        if (!sesion.getRol().equals("Mozo")) return prohibido();
        if (pedido == null || pedido.getIdPlato() <= 0 || pedido.getCantidad() < 1 || pedido.getCantidad() > 20) {
            return solicitudInvalida("Selecciona un producto y una cantidad entre 1 y 20");
        }

        try (Connection con = DBManager.getInstance().getConnection()) {
            con.setAutoCommit(false);
            try {
                if (!mesaAsignada(con, idMesa, sesion.getIdEmpleado())) {
                    con.rollback();
                    return prohibido();
                }
                String estadoMesa = texto(con, "SELECT estado FROM mesa WHERE id_mesa = ?", idMesa);
                if (!"OCUPADA".equals(estadoMesa)) {
                    con.rollback();
                    return solicitudInvalida("Solo se pueden registrar pedidos en una mesa ocupada");
                }

                int idCuenta = obtenerCuentaAbierta(con, idMesa);
                if (idCuenta == 0) {
                    String insertCuenta = "INSERT INTO cuenta_consumo(fecha_apertura, estado, monto_total_pagar, id_mesa, id_mozo) VALUES(NOW(), 'ABIERTA', 0, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(insertCuenta, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, idMesa);
                        ps.setInt(2, sesion.getIdEmpleado());
                        ps.executeUpdate();
                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (!keys.next()) throw new IllegalStateException("No se generó la cuenta");
                            idCuenta = keys.getInt(1);
                        }
                    }
                }

                BigDecimal precio;
                try (PreparedStatement ps = con.prepareStatement("SELECT precio FROM plato WHERE id_plato = ?")) {
                    ps.setInt(1, pedido.getIdPlato());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encontró el plato")).build();
                        }
                        precio = rs.getBigDecimal("precio");
                    }
                }

                int idPedido;
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO pedido(fecha_hora, id_cuenta_consumo) VALUES(NOW(), ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idCuenta);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new IllegalStateException("No se generó el pedido");
                        idPedido = keys.getInt(1);
                    }
                }

                BigDecimal importe = precio.multiply(BigDecimal.valueOf(pedido.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                String insertDetalle = "INSERT INTO detalle_pedido(cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES(?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(insertDetalle)) {
                    ps.setInt(1, pedido.getCantidad());
                    ps.setBigDecimal(2, precio);
                    ps.setBigDecimal(3, importe);
                    ps.setInt(4, pedido.getIdPlato());
                    ps.setInt(5, idPedido);
                    ps.executeUpdate();
                }

                con.commit();
                return Response.status(Response.Status.CREATED).entity(Map.of("idPedido", idPedido)).build();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @GET
    @Path("/mesas/{id}/cuenta")
    public Response obtenerCuenta(@HeaderParam("Authorization") String authorization,
                                   @PathParam("id") int idMesa) {
        SesionDTO sesion = SesionActiva.obtener(authorization);
        if (sesion == null) return noAutorizado();
        try (Connection con = DBManager.getInstance().getConnection()) {
            if (!puedeVerMesa(con, sesion, idMesa)) return prohibido();
            CuentaDTO cuenta = cargarCuenta(con, idMesa);
            if (cuenta == null) return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "La mesa todavía no tiene una cuenta abierta")).build();
            return Response.ok(cuenta).build();
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @POST
    @Path("/mesas/{id}/pago")
    public Response generarPago(@HeaderParam("Authorization") String authorization,
                                @PathParam("id") int idMesa, PagoDTO pago) {
        SesionDTO sesion = SesionActiva.obtener(authorization);
        if (sesion == null) return noAutorizado();
        if (!sesion.getRol().equals("Mozo")) return prohibido();
        if (pago == null || pago.getMedioPago() == null || pago.getTipoComprobante() == null ||
                !List.of("Efectivo", "Yape", "Tarjeta").contains(pago.getMedioPago()) ||
                !List.of("Boleta", "Factura").contains(pago.getTipoComprobante())) {
            return solicitudInvalida("Selecciona un comprobante y un medio de pago válidos");
        }
        if ("Factura".equals(pago.getTipoComprobante()) &&
                (pago.getRuc() == null || !pago.getRuc().matches("\\d{11}") ||
                        pago.getRazonSocial() == null || pago.getRazonSocial().isBlank())) {
            return solicitudInvalida("Para emitir una factura ingresa un RUC de 11 dígitos y la razón social");
        }

        try (Connection con = DBManager.getInstance().getConnection()) {
            if (!mesaAsignada(con, idMesa, sesion.getIdEmpleado())) return prohibido();
            CuentaDTO cuenta = cargarCuenta(con, idMesa);
            if (cuenta == null || cuenta.getDetalles().isEmpty()) return solicitudInvalida("La cuenta no tiene consumos registrados");
            if ("PAGADA".equals(cuenta.getEstado())) return solicitudInvalida("La cuenta ya fue pagada");

            String token = UUID.randomUUID().toString();
            String codigo = "RS-" + idMesa + "-" + System.currentTimeMillis();
            String sql = "UPDATE cuenta_consumo SET monto_total_pagar = ?, enlace_pago = ?, medio_pago = ?, tipo_comprobante = ?, ruc = ?, razon_social = ?, codigo_operacion = ? WHERE id_cuenta = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setBigDecimal(1, cuenta.getTotal());
                ps.setString(2, token);
                ps.setString(3, pago.getMedioPago());
                ps.setString(4, pago.getTipoComprobante());
                ps.setString(5, "Factura".equals(pago.getTipoComprobante()) ? pago.getRuc() : null);
                ps.setString(6, "Factura".equals(pago.getTipoComprobante()) ? pago.getRazonSocial().trim() : null);
                ps.setString(7, codigo);
                ps.setInt(8, cuenta.getIdCuenta());
                ps.executeUpdate();
            }
            CuentaDTO actualizada = cargarCuenta(con, idMesa);
            actualizada.setReferencia(codigo);
            return Response.ok(new pe.edu.pucp.rinconsatipeno.rest.dto.ComprobanteDTO(token, "/cliente/comprobante/" + token, actualizada)).build();
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    @POST
    @Path("/mesas/{id}/pago/confirmar")
    public Response confirmarPago(@HeaderParam("Authorization") String authorization,
                                  @PathParam("id") int idMesa) {
        SesionDTO sesion = SesionActiva.obtener(authorization);
        if (sesion == null) return noAutorizado();
        if (!sesion.getRol().equals("Mozo")) return prohibido();
        try (Connection con = DBManager.getInstance().getConnection()) {
            if (!mesaAsignada(con, idMesa, sesion.getIdEmpleado())) return prohibido();
            int idCuenta = obtenerCuentaAbierta(con, idMesa);
            if (idCuenta == 0) return solicitudInvalida("No existe una cuenta pendiente de pago");
            if (!existe(con, "SELECT 1 FROM cuenta_consumo WHERE id_cuenta = ? AND enlace_pago IS NOT NULL AND codigo_operacion IS NOT NULL", idCuenta)) {
                return solicitudInvalida("Primero debe generarse el QR de pago");
            }
            try (PreparedStatement ps = con.prepareStatement("UPDATE cuenta_consumo SET estado = 'PAGADA', fecha_pago = NOW(), fecha_cierre = NOW() WHERE id_cuenta = ?")) {
                ps.setInt(1, idCuenta);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("UPDATE mesa SET estado = 'LIBRE' WHERE id_mesa = ?")) {
                ps.setInt(1, idMesa);
                ps.executeUpdate();
            }
            return Response.ok(Map.of("mensaje", "Pago simulado confirmado")).build();
        } catch (Exception ex) {
            return errorServidor();
        }
    }

    private CuentaDTO cargarCuenta(Connection con, int idMesa) throws Exception {
        String sql = "SELECT cc.id_cuenta, cc.estado, cc.enlace_pago, cc.medio_pago, cc.tipo_comprobante, cc.ruc, cc.razon_social, cc.codigo_operacion, cc.fecha_pago, m.numero, m.zona, mo.nombre " +
                "FROM cuenta_consumo cc JOIN mesa m ON m.id_mesa = cc.id_mesa " +
                "LEFT JOIN mozo mo ON mo.id_mozo = cc.id_mozo " +
                "WHERE cc.id_mesa = ? ORDER BY cc.id_cuenta DESC LIMIT 1";
        CuentaDTO cuenta;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                cuenta = new CuentaDTO();
                cuenta.setIdCuenta(rs.getInt("id_cuenta"));
                cuenta.setMesa(rs.getInt("numero"));
                cuenta.setZona(rs.getString("zona"));
                cuenta.setMozo(rs.getString("nombre"));
                cuenta.setEstado(rs.getString("estado"));
                cuenta.setReferencia(rs.getString("codigo_operacion"));
                cuenta.setToken(rs.getString("enlace_pago"));
                cuenta.setMedioPago(rs.getString("medio_pago"));
                cuenta.setTipoComprobante(rs.getString("tipo_comprobante"));
                cuenta.setRuc(rs.getString("ruc"));
                cuenta.setRazonSocial(rs.getString("razon_social"));
                Timestamp fecha = rs.getTimestamp("fecha_pago");
                cuenta.setFecha(fecha == null ? null : fecha.toString());
            }
        }

        List<DetalleConsumoDTO> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        String detalleSql = "SELECT pl.nombre, dp.cantidad_platos, dp.precio_unitario, dp.subtotal " +
                "FROM pedido pe JOIN detalle_pedido dp ON dp.id_pedido = pe.id_pedido " +
                "JOIN plato pl ON pl.id_plato = dp.id_plato WHERE pe.id_cuenta_consumo = ? ORDER BY pe.fecha_hora, dp.id_detalle";
        try (PreparedStatement ps = con.prepareStatement(detalleSql)) {
            ps.setInt(1, cuenta.getIdCuenta());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal importe = rs.getBigDecimal("subtotal");
                    BigDecimal precio = rs.getBigDecimal("precio_unitario");
                    detalles.add(new DetalleConsumoDTO(rs.getString("nombre"), rs.getInt("cantidad_platos"), precio, importe));
                    total = total.add(importe);
                }
            }
        }
        BigDecimal subtotal = total.divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);
        cuenta.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        cuenta.setSubtotal(subtotal);
        cuenta.setIgv(total.subtract(subtotal).setScale(2, RoundingMode.HALF_UP));
        cuenta.setDetalles(detalles);
        if ("ABIERTA".equals(cuenta.getEstado()) && cuenta.getReferencia() != null) cuenta.setEstado("PENDIENTE");
        return cuenta;
    }

    private int obtenerCuentaAbierta(Connection con, int idMesa) throws Exception {
        try (PreparedStatement ps = con.prepareStatement("SELECT id_cuenta FROM cuenta_consumo WHERE id_mesa = ? AND estado = 'ABIERTA' ORDER BY id_cuenta DESC LIMIT 1")) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    private boolean puedeVerMesa(Connection con, SesionDTO sesion, int idMesa) throws Exception {
        if (sesion.getRol().equals("Administrador")) return existe(con, "SELECT 1 FROM mesa WHERE id_mesa = ?", idMesa);
        return sesion.getRol().equals("Mozo") && mesaAsignada(con, idMesa, sesion.getIdEmpleado());
    }

    private boolean mesaAsignada(Connection con, int idMesa, int idMozo) throws Exception {
        try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM mesa_mozo WHERE id_mesa = ? AND id_mozo = ?")) {
            ps.setInt(1, idMesa);
            ps.setInt(2, idMozo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private boolean existe(Connection con, String sql, int id) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private String texto(Connection con, String sql, int id) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getString(1) : null; }
        }
    }

    private Response noAutorizado() {
        return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "Inicia sesión para continuar")).build();
    }

    private Response prohibido() {
        return Response.status(Response.Status.FORBIDDEN).entity(Map.of("error", "No tienes permisos para realizar esta operación")).build();
    }

    private Response solicitudInvalida(String mensaje) {
        return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", mensaje)).build();
    }

    private Response errorServidor() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", "No se pudo completar la operación")).build();
    }
}
