package util;

import entidades.Boletin;
import java.util.Date;

/**
 *
 * @author Agárimo
 */
public class ParametrosEdicto {
    
    Date fecha;
    Boletin boletin;
    int estado;
    
    public ParametrosEdicto(Date fecha, Boletin boletin,int estado){
        this.fecha=fecha;
        this.boletin=boletin;
        this.estado=estado;
    }

    public Boletin getBoletin() {
        return boletin;
    }

    public void setBoletin(Boletin boletin) {
        this.boletin = boletin;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
