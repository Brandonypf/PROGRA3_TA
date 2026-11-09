package pe.edu.pucp.rinconsatipeno.modelo.reservas;

import java.time.LocalDate;
import java.time.LocalTime;

/** Datos necesarios para mostrar una reserva en el calendario. */
public record ReservaCalendarioDTO(int idReserva, String cliente, int numeroMesa,
                                   LocalDate fecha, LocalTime horaInicio,
                                   int cantidadPersonas, EstadoReserva estado) { }
