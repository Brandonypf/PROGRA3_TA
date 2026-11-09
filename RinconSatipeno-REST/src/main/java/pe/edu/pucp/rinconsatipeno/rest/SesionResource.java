package pe.edu.pucp.rinconsatipeno.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.rest.dto.LoginDTO;
import pe.edu.pucp.rinconsatipeno.rest.dto.SesionDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@Path("/sesion")
@Produces("application/json")
@Consumes("application/json")
public class SesionResource {

    @POST
    @Path("/login")
    public Response login(LoginDTO credenciales) {
        if (credenciales == null || credenciales.getCorreo() == null ||
                credenciales.getContrasenia() == null ||
                credenciales.getCorreo().isBlank() || credenciales.getContrasenia().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Ingresa tu correo y contraseña"))
                    .build();
        }

        String sql = "SELECT ca.id_cuenta_acceso, ca.email, ca.estado, " +
                "m.id_mozo, m.nombre AS nombre_mozo, a.id_administrador, a.nombre AS nombre_admin " +
                "FROM cuenta_acceso ca " +
                "LEFT JOIN mozo m ON m.id_cuenta_acceso = ca.id_cuenta_acceso " +
                "LEFT JOIN administrador a ON a.id_cuenta_acceso = ca.id_cuenta_acceso " +
                "WHERE ca.email = ? AND ca.contrasenia = ? AND ca.estado = 'ACTIVO'";

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, credenciales.getCorreo().trim());
            ps.setString(2, credenciales.getContrasenia());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Map.of("error", "Correo o contraseña incorrectos"))
                            .build();
                }

                SesionDTO sesion = new SesionDTO();
                sesion.setCorreo(rs.getString("email"));
                int idMozo = rs.getInt("id_mozo");
                int idAdmin = rs.getInt("id_administrador");

                if (idMozo > 0) {
                    sesion.setRol("Mozo");
                    sesion.setIdEmpleado(idMozo);
                    sesion.setNombre(rs.getString("nombre_mozo"));
                } else if (idAdmin > 0) {
                    sesion.setRol("Administrador");
                    sesion.setIdEmpleado(idAdmin);
                    sesion.setNombre(rs.getString("nombre_admin"));
                } else {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity(Map.of("error", "La cuenta no tiene un rol de acceso"))
                            .build();
                }

                SesionActiva.crear(sesion);
                return Response.ok(sesion).build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "No se pudo iniciar sesión"))
                    .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authorization) {
        SesionActiva.cerrar(authorization);
        return Response.noContent().build();
    }
}
