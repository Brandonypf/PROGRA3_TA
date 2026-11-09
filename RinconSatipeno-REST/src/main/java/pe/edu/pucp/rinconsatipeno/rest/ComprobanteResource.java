package pe.edu.pucp.rinconsatipeno.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.rest.dto.CuentaDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.DetalleConsumoDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/comprobantes")
@Produces("application/json")
public class ComprobanteResource {

    @GET
    @Path("/{token}")
    public Response obtener(@PathParam("token") String token) {
        if (token == null || token.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Código de comprobante inválido")).build();
        }

        String sql = "SELECT cc.id_cuenta, cc.estado, cc.medio_pago, cc.tipo_comprobante, cc.ruc, cc.razon_social, cc.codigo_operacion, cc.fecha_pago, " +
                "m.numero, m.zona, mo.nombre FROM cuenta_consumo cc " +
                "JOIN mesa m ON m.id_mesa = cc.id_mesa LEFT JOIN mozo mo ON mo.id_mozo = cc.id_mozo " +
                "WHERE cc.enlace_pago = ? LIMIT 1";

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            CuentaDTO cuenta;
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encontró el comprobante")).build();
                cuenta = new CuentaDTO();
                cuenta.setIdCuenta(rs.getInt("id_cuenta"));
                cuenta.setMesa(rs.getInt("numero"));
                cuenta.setZona(rs.getString("zona"));
                cuenta.setMozo(rs.getString("nombre"));
                cuenta.setEstado(rs.getString("estado"));
                cuenta.setReferencia(rs.getString("codigo_operacion"));
                cuenta.setToken(token);
                cuenta.setMedioPago(rs.getString("medio_pago"));
                cuenta.setTipoComprobante(rs.getString("tipo_comprobante"));
                cuenta.setRuc(rs.getString("ruc"));
                cuenta.setRazonSocial(rs.getString("razon_social"));
                Timestamp fecha = rs.getTimestamp("fecha_pago");
                cuenta.setFecha(fecha == null ? null : fecha.toString());
            }

            List<DetalleConsumoDTO> detalles = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            String detalleSql = "SELECT pl.nombre, dp.cantidad_platos, dp.precio_unitario, dp.subtotal " +
                    "FROM pedido pe JOIN detalle_pedido dp ON dp.id_pedido = pe.id_pedido " +
                    "JOIN plato pl ON pl.id_plato = dp.id_plato WHERE pe.id_cuenta_consumo = ? ORDER BY pe.fecha_hora, dp.id_detalle";
            try (PreparedStatement psDetalle = con.prepareStatement(detalleSql)) {
                psDetalle.setInt(1, cuenta.getIdCuenta());
                try (ResultSet rs = psDetalle.executeQuery()) {
                    while (rs.next()) {
                        BigDecimal importe = rs.getBigDecimal("subtotal");
                        detalles.add(new DetalleConsumoDTO(rs.getString("nombre"), rs.getInt("cantidad_platos"), rs.getBigDecimal("precio_unitario"), importe));
                        total = total.add(importe);
                    }
                }
            }
            BigDecimal subtotal = total.divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);
            cuenta.setTotal(total.setScale(2, RoundingMode.HALF_UP));
            cuenta.setSubtotal(subtotal);
            cuenta.setIgv(total.subtract(subtotal).setScale(2, RoundingMode.HALF_UP));
            cuenta.setDetalles(detalles);
            return Response.ok(cuenta).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "No se pudo consultar el comprobante"))
                    .build();
        }
    }
}
