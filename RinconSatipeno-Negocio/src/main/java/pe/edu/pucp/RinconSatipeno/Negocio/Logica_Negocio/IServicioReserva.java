package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio;

import java.time.LocalDate;
import java.time.LocalTime;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;

public interface IServicioReserva {

    Reserva crearReserva(Reserva reserva);

    void cancelarReserva(int idReserva);

    boolean consultarDisponibilidad(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int numPersonas);
}
