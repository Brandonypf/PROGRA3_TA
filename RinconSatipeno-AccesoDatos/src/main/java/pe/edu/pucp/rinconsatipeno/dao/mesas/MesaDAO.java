package pe.edu.pucp.rinconsatipeno.dao.mesas;

import pe.edu.pucp.rinconsatipeno.dao.RegistroDAO;

import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import java.sql.SQLException;

public interface MesaDAO extends RegistroDAO<Mesa, Integer>{
    boolean existeNumero(int numero, int excluirId) throws SQLException;

}
