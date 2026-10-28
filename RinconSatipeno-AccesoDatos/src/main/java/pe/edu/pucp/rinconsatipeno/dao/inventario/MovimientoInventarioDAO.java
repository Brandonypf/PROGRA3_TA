package pe.edu.pucp.rinconsatipeno.dao.inventario;

import pe.edu.pucp.rinconsatipeno.dao.GeneralDAO;

import pe.edu.pucp.rinconsatipeno.modelo.inventario.MovimientoInventario;
import java.sql.SQLException;
import java.util.List;

public interface MovimientoInventarioDAO extends GeneralDAO<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByInsumoId(int idInsumo) throws SQLException;
}
