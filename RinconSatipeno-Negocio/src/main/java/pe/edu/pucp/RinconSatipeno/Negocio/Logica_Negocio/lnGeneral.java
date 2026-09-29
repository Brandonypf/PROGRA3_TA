package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio;
import java.sql.SQLException;
import java.util.List;


public interface lnGeneral <T, ID> {
    List<T> findAll() throws BLException;
    T findBy(ID id) throws BLException;
    void insert(T objetoAuxiliar) throws BLException;
    void update(T aux) throws BLException;
    void delete(ID id) throws BLException;
}
