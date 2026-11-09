package pe.edu.pucp.rinconsatipeno.rest;

import java.util.Map;
import pe.edu.pucp.rinconsatipeno.rest.dto.SesionDTO;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SesionActiva {
    private static final Map<String, SesionDTO> SESIONES = new ConcurrentHashMap<>();

    public static String crear(SesionDTO sesion) {
        String token = UUID.randomUUID().toString();
        sesion.setToken(token);
        SESIONES.put(token, sesion);
        return token;
    }

    public static SesionDTO obtener(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return SESIONES.get(authorization.substring(7).trim());
    }

    public static void cerrar(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            SESIONES.remove(authorization.substring(7).trim());
        }
    }
}
