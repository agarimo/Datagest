package entidades;

import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Precepto {

    String nombre;
    String descripcion;
    
    public Precepto(){
        
    }
    
    public Precepto(String nombre){
        this.nombre=nombre;
        this.descripcion="";
    }
    
    public Precepto(String nombre, String descripcion){
        this(nombre);
        this.descripcion=descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        final Precepto other = (Precepto) obj;
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
    
    public String creaPrecepto() {
        String query = "INSERT into datagest.precepto (nombre,descripcion) values("
                + Datagest.entrecomillar(getNombre()) + ","
                + Datagest.entrecomillar((getDescripcion()))
                + ")";
        return query;
    }

    public String editaPrecepto() {
        String query = "UPDATE datagest.precepto SET "
                + "descripcion=" + Datagest.entrecomillar(getDescripcion())
                + "WHERE nombre=" + Datagest.entrecomillar(getNombre());
        return query;
    }

    public String borraPrecepto() {
        String query = "DELETE FROM datagest.precepto WHERE nombre=" + Datagest.entrecomillar(getNombre());
        return query;
    }

    public String buscaPrecepto() {
        String query = "SELECT count(*) FROM datagest.precepto WHERE nombre=" + Datagest.entrecomillar(getNombre()) + ";";
        return query;
    }
    
}
