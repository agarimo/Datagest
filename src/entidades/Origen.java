package entidades;

import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Origen {

    private String idOrigen;
    private String nombre;
    private String codigo;
    private String observaciones;
    private String articulo;

    public Origen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public Origen(String idOrigen, String nombre, String codigo, String observaciones, String articulo) {
        this(idOrigen);
        this.nombre = nombre;
        this.codigo = codigo;
        this.observaciones = observaciones;
        this.articulo = articulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Origen other = (Origen) obj;
        if ((this.idOrigen == null) ? (other.idOrigen != null) : !this.idOrigen.equals(other.idOrigen)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.idOrigen != null ? this.idOrigen.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.idOrigen;
    }

    public String creaOrigen() {
        String query = "INSERT into datagest.origen (idOrigen, nombre,codigo,observaciones,articulo) values("
                + Datagest.entrecomillar(getIdOrigen()) + ","
                + Datagest.entrecomillar(getNombre()) + ","
                + Datagest.entrecomillar(getCodigo()) + ","
                + Datagest.entrecomillar(getObservaciones()) + ","
                + Datagest.entrecomillar((getArticulo()))
                + ")";
        return query;
    }

    public String editaOrigen() {
        String query = "UPDATE datagest.origen SET "
                + "nombre=" + Datagest.entrecomillar(getNombre()) + ","
                + "codigo=" + Datagest.entrecomillar(getCodigo()) + ","
                + "observaciones=" + Datagest.entrecomillar(getObservaciones()) + ","
                + "articulo=" + Datagest.entrecomillar(getArticulo())
                + "WHERE idOrigen=" + Datagest.entrecomillar(getIdOrigen());
        return query;
    }

    public String borraOrigen() {
        String query = "DELETE FROM datagest.origen WHERE idOrigen=" + Datagest.entrecomillar(getIdOrigen());
        return query;
    }

    public String buscaOrigen() {
        String query = "SELECT * FROM datagest.origen WHERE idOrigen=" + Datagest.entrecomillar(getIdOrigen()) + ";";
        return query;
    }
}
