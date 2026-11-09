package pe.edu.pucp.rinconsatipeno.dao.reservas;

import pe.edu.pucp.rinconsatipeno.dao.RegistroDAO;

import pe.edu.pucp.rinconsatipeno.modelo.reservas.Reserva;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.ReservaCalendarioDTO;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.sql.SQLException;

public interface ReservaDAO extends RegistroDAO<Reserva, Integer> {
    List<ReservaCalendarioDTO> listarCalendario(LocalDate fecha) throws SQLException;
    List<Mesa> buscarMesasDisponibles(LocalDate fecha, LocalTime inicio,
                                     LocalTime fin, int personas) throws SQLException;
}