package entidades;

import java.util.Date;
import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Multa {

    private String idEdicto;
    private String expediente;
    private String idSancionado;
    private String localidad;
    private Date fechaInfraccion;
    private String matricula;
    private String cuantia;
    private String precepto;
    private String art;
    private String puntos;
    private String obs;
    private String fase;
    private String linea;
    private String organismo;
    private boolean valido = true;

    public Multa(String idEdicto) {
        this.idEdicto = idEdicto;
    }

    public Multa(String idEdicto, String fase, String linea) {
        this(idEdicto);
        this.linea = linea;
        this.fase = fase;
        this.linea = linea;
    }

    public Multa(String idEdicto, String expediente, String idSancionado, String localidad, Date fechaInfraccion, String matricula,
            String cuantia, String precepto, String art, String puntos, String obs, String fase, String organismo, String linea) {
        this(idEdicto, fase, linea);
        this.expediente = expediente;
        this.idSancionado = idSancionado;
        this.localidad = localidad;
        this.fechaInfraccion = fechaInfraccion;
        this.matricula = matricula;
        this.cuantia = cuantia;
        this.precepto = precepto;
        this.art = art;
        this.puntos = puntos;
        this.obs = obs;
        this.fase = fase;
        this.organismo = organismo;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = cuantia;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Date getFechaInfraccion() {
        return fechaInfraccion;
    }

    public void setFechaInfraccion(Date fechaInfraccion) {
        this.fechaInfraccion = fechaInfraccion;
    }

    public String getIdEdicto() {
        return idEdicto;
    }

    public void setIdEdicto(String idEdicto) {
        this.idEdicto = idEdicto;
    }

    public String getIdSancionado() {
        return idSancionado;
    }

    public void setIdSancionado(String idSancionado) {
        this.idSancionado = idSancionado;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPrecepto() {
        return precepto;
    }

    public void setPrecepto(String precepto) {
        this.precepto = precepto;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Multa other = (Multa) obj;
        if ((this.idEdicto == null) ? (other.idEdicto != null) : !this.idEdicto.equals(other.idEdicto)) {
            return false;
        }
        if ((this.expediente == null) ? (other.expediente != null) : !this.expediente.equals(other.expediente)) {
            return false;
        }
        if ((this.linea == null) ? (other.linea != null) : !this.linea.equals(other.linea)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.idEdicto != null ? this.idEdicto.hashCode() : 0);
        hash = 73 * hash + (this.expediente != null ? this.expediente.hashCode() : 0);
        hash = 73 * hash + (this.linea != null ? this.linea.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Multa{" + "idEdicto=" + idEdicto + ", expediente=" + expediente + '}';
    }

    public String creaMulta() {
        String query = "INSERT into datagest.multa (idEdicto, expediente, idSancionado, localidad, fechaInfraccion, matricula, cuantia, precepto, art, puntos,"
                + " obs, fase,organismo) values("
                + Datagest.entrecomillar(getIdEdicto()) + ","
                + Datagest.entrecomillar(getExpediente()) + ","
                + Datagest.entrecomillar(getIdSancionado()) + ","
                + Datagest.entrecomillar(getLocalidad()) + ","
                + Datagest.entrecomillar(Datagest.imprimeFecha(getFechaInfraccion())) + ","
                + Datagest.entrecomillar(getMatricula()) + ","
                + Datagest.entrecomillar(getCuantia()) + ","
                + Datagest.entrecomillar(getPrecepto()) + ","
                + Datagest.entrecomillar(getArt()) + ","
                + getPuntos() + ","
                + Datagest.entrecomillar(getObs()) + ","
                + Datagest.entrecomillar(getFase()) + ","
                + Datagest.entrecomillar(getOrganismo())
                + ")";
        return query;
    }

    public String editaMulta() {
        String query = "UPDATE datagest.multa SET "
                + "localidad=" + Datagest.entrecomillar(getLocalidad()) + ","
                + "fechaInfraccion=" + Datagest.entrecomillar(Datagest.imprimeFecha(getFechaInfraccion())) + ","
                + "matricula=" + Datagest.entrecomillar(getMatricula()) + ","
                + "cuantia=" + Datagest.entrecomillar(getCuantia()) + ","
                + "precepto=" + Datagest.entrecomillar(getPrecepto()) + ","
                + "art=" + Datagest.entrecomillar(getArt()) + ","
                + "puntos=" + getPuntos() + ","
                + "obs=" + Datagest.entrecomillar(getObs()) + ","
                + "fase=" + Datagest.entrecomillar(getFase()) + ","
                + "organismo=" + Datagest.entrecomillar(getOrganismo()) + " "
                + "WHERE idEdicto=" + Datagest.entrecomillar(getIdEdicto()) + " and expediente=" + Datagest.entrecomillar(getExpediente());
        return query;
    }

    public String borraMulta() {
        String query = "DELETE FROM datagest.multa WHERE idEdicto=" + Datagest.entrecomillar(getIdEdicto()) + " and expediente=" + Datagest.entrecomillar(getExpediente());
        return query;
    }

    public String buscaMulta() {
        String query = "SELECT * FROM datagest.multa WHERE idEdicto=" + Datagest.entrecomillar(getIdEdicto()) + " and expediente=" + Datagest.entrecomillar(getExpediente());
        return query;
    }
}
