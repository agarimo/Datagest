package entidades;

import java.util.Date;
import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Edicto extends Boletin {

    private String idEdicto;
    private int idDescarga;
    private int estado;
    private String tipo;
    private String fase;
    private String rutaEdicto;
    private String datos;

    public Edicto() {
    }

    public Edicto(String idEdicto) {
        this.idEdicto = idEdicto;
    }

    public Edicto(String idEdicto, int idDescarga, String origen, Date fecha, String tipo) {
        super(fecha, origen);
        this.idEdicto = idEdicto;
        this.idDescarga = idDescarga;
        this.tipo = tipo;
        this.rutaEdicto = generaRuta();
    }

    public Edicto(String idEdicto, int idDescarga, String origen, Date fecha, String tipo, String fase) {
        super(fecha, origen);
        this.idEdicto = idEdicto;
        this.idDescarga = idDescarga;
        this.tipo = tipo;
        this.fase = fase;
        this.rutaEdicto = generaRuta();
    }

    public Edicto(String idEdicto, int idDescarga, String origen, Date fecha, String tipo, String fase, String datos) {
        this(idEdicto, idDescarga, origen, fecha, tipo, fase);
        this.datos = datos;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getIdEdicto() {
        return idEdicto;
    }

    public void setIdEdicto(String idEdicto) {
        this.idEdicto = idEdicto;
    }

    public int getIdDescarga() {
        return idDescarga;
    }

    public void setIdDescarga(int idDescarga) {
        this.idDescarga = idDescarga;
    }

    public String getRutaEdicto() {
        return rutaEdicto;
    }

    public void setRutaEdicto(String rutaEdicto) {
        this.rutaEdicto = rutaEdicto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDatos() {
        return datos;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edicto other = (Edicto) obj;
        if ((this.idEdicto == null) ? (other.idEdicto != null) : !this.idEdicto.equals(other.idEdicto)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.idEdicto != null ? this.idEdicto.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.idEdicto;
    }

    private String generaRuta() {
        return super.getRuta() + this.getIdEdicto() + ".pdf";
    }

    public String creaEdicto() {
        String query = "INSERT into datagest.edicto (idBoletin, idEdicto, idDescarga, estado, tipo) values("
                + Datagest.entrecomillar(getIdBoletin()) + ","
                + Datagest.entrecomillar(getIdEdicto()) + ","
                + getIdDescarga() + ","
                + getEstado() + ","
                + Datagest.entrecomillar(getTipo())
                + ")";
        return query;
    }

    public String editaEdicto() {
        String query = "UPDATE datagest.edicto SET "
                + "estado=" + getEstado() + ","
                + "fase=" + Datagest.entrecomillar(getFase()) + ","
                + "tipo=" + Datagest.entrecomillar(getTipo()) + " "
                + "WHERE idEdicto=" + Datagest.entrecomillar(getIdEdicto());
        return query;
    }

    public String borraEdicto() {
        String query = "DELETE FROM datagest.edicto WHERE idEdicto=" + Datagest.entrecomillar(getIdEdicto());
        return query;
    }

    public String buscaEdicto() {
        String query = "SELECT * FROM datagest.edicto WHERE idEdicto=" + Datagest.entrecomillar(getIdEdicto()) + ";";
        return query;
    }

    public String cargaEdicto() {
        String query = "SELECT * FROM datagest.listaedicto WHERE idEdicto=" + Datagest.entrecomillar(getIdEdicto()) + ";";
        return query;
    }

    public static String listaEdicto(int estado) {
        String query = "SELECT * FROM datagest.edictos WHERE estado=" + estado + " limit " + 10;
        return query;
    }
}