package util;

import java.io.Serializable;

/**
 *
 * @author Agárimo
 */
public class Url implements Serializable, Comparable<Url> {

    
    String nombre;
    String url;
    
    public Url(String nombre,String url){
        this.nombre=nombre;
        this.url=url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Url other = (Url) obj;
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }


    
    @Override
    public int compareTo(Url o) {
        String a=this.nombre;
        String b=o.getNombre();
        
        return a.compareTo(b);
    }

}
