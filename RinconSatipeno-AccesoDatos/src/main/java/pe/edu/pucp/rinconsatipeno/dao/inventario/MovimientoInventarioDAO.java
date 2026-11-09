package pe.edu.pucp.rinconsatipeno.dao.inventario;

import pe.edu.pucp.rinconsatipeno.dao.RegistroDAO;

import pe.edu.pucp.rinconsatipeno.modelo.inventario.MovimientoInventario;
import java.sql.SQLException;
import java.util.List;

public interface MovimientoInventarioDAO extends RegistroDAO<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByInsumoId(int idInsumo) throws SQLException;
}
