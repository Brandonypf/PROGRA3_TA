package pe.edu.pucp.rinconSatipeno.dao;

import java.sql.SQLException;
import java.util.List;

//Utilizamos la sintaxis genérica para poner asignar cualquier clase e ID posteriormente al resto de daos.
public interface DaoGeneral <T,ID> {
    List<T> findAll() throws SQLException;
    T findBy(ID id) throws SQLException;
    void insert(T modelo) throws SQLException;
    void update(ID id) throws SQLException;
    void delete(ID id) throws SQLException;
}
