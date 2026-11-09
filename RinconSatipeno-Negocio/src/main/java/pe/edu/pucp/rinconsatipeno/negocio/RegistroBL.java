package pe.edu.pucp.rinconsatipeno.negocio;
import java.sql.SQLException;
import java.util.List;


public interface RegistroBL <T, ID> {
    List<T> findAll() throws BLException;
    T findBy(ID id) throws BLException;
    void insert(T objetoAuxiliar) throws BLException;
    void update(T aux) throws BLException;
    void delete(ID id) throws BLException;
}
