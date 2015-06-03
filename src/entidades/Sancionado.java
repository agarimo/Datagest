package entidades;

import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Sancionado {

    private String id;
    private String nombre;

    public Sancionado(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sancionado other = (Sancionado) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 53 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.id;
    }

    public String creaSancionado() {
        String query = "INSERT into datagest.sancionado (idSancionado, nombre) values("
                + Datagest.entrecomillar(getId()) + ","
                + Datagest.entrecomillar(getNombre())
                + ")";
        return query;
    }

    public String editaSancionado() {
        String query = "UPDATE datagest.sancionado SET "
                + "nombre=" + Datagest.entrecomillar(getNombre()) + " "
                + "WHERE idSancionado=" + Datagest.entrecomillar(getId());
        return query;
    }

    public String borraSancionado() {
        String query = "DELETE FROM datagest.sancionado WHERE idSancionado=" + Datagest.entrecomillar(getId());
        return query;
    }

    public String buscaSancionado() {
        String query = "SELECT * FROM datagest.sancionado WHERE idSancionado=" + Datagest.entrecomillar(getId());
        return query;
    }
}
