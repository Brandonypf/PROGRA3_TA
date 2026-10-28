package pe.edu.pucp.rinconsatipeno.dao.empleados;

import pe.edu.pucp.rinconsatipeno.dao.GeneralDAO;

import java.sql.SQLException;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;

public interface DatosAccesoDAO extends GeneralDAO<DatosAcceso, Integer> {
    DatosAcceso findByEmail(String email) throws SQLException;
}