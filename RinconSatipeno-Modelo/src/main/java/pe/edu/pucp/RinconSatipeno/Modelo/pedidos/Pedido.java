package pe.edu.pucp.RinconSatipeno.Modelo.pedidos;

import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Pedido{
    //Atributos
    private int idPedido;
    private double precioTotal;
    private Mesa mesa;
    private CuentaConsumo cuentaConsumo;
    private Reserva reserva;
    private List <DetallePedido> detalles;
    //Constructor sin parámetros
    public Pedido(){
        this.precioTotal=0;
        this.detalles=new ArrayList<>();
    }
    //Constructor copia
    public Pedido(final Pedido pedido){
        if(pedido==null){
            throw new IllegalArgumentException("pedido no puede ser nulo");
        }
        setId(pedido.getId());
        setMesa(pedido.getMesa());
        setCuentaConsumo(pedido.getCuentaConsumo());
        setReserva(pedido.getReserva());
        setPrecioTotal(pedido.getPrecioTotal());
        setDetalles(pedido.getDetalles());
    }
    //Getters y Setters
    public int getId(){
        return idPedido;
    }

    public void setId(int idPedido){
        if(idPedido<0){
            throw new IllegalArgumentException("idPedido no puede ser negativo");
        }
        this.idPedido=idPedido;
    }

    public Mesa getMesa(){
        return new Mesa(mesa);
    }

    public void setMesa(Mesa mesa){
        if (mesa==null){
            throw new IllegalArgumentException("mesa no puede ser nula");
        }
        this.mesa=new Mesa(mesa);
    }

    public CuentaConsumo getCuentaConsumo(){
        return new CuentaConsumo(cuentaConsumo);
    }

    public void setCuentaConsumo(CuentaConsumo cuentaConsumo){
        if (cuentaConsumo==null){
            throw new IllegalArgumentException("cuenta no puede ser nula");
        }
        this.cuentaConsumo =new CuentaConsumo(cuentaConsumo);
    }

    public Reserva getReserva(){
        if (reserva==null){
            return null;
        }
        return new Reserva(reserva);
    }

    public void setReserva(Reserva reserva){
        if (reserva==null){
            this.reserva=null;
            return;
        }
        this.reserva=new Reserva(reserva);
    }

    public double getPrecioTotal(){
        return precioTotal;
    }
    public void setPrecioTotal(double precioTotal){
        if(precioTotal<0){
            throw new IllegalArgumentException("precioTotal no puede ser negativo");
        }
        this.precioTotal=precioTotal;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }
    public void setDetalles(List<DetallePedido> detalles){
        if(detalles==null){
            throw new IllegalArgumentException("detalles no puede ser nulo");
        }
        this.detalles=new ArrayList<>(detalles);
    }
}
