package pe.edu.pucp.rinconsatipeno.negocio.reservas;

import pe.edu.pucp.rinconsatipeno.negocio.GeneralBL;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;

import pe.edu.pucp.rinconsatipeno.modelo.reservas.Reserva;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.ReservaCalendarioDTO;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaBL extends GeneralBL<Reserva, Integer> {
    List<ReservaCalendarioDTO> listarCalendario(LocalDate fecha) throws BLException;
    List<Mesa> buscarMesasDisponibles(LocalDate fecha, LocalTime inicio,
                                     LocalTime fin, int personas) throws BLException;

}