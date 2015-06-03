package entidades;

import java.util.Date;
import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Descarga {

    private int idDescarga;
    private String parametros;
    private Date fecha;
    private String datos;
    private int estado;
    
    public Descarga(int idDescarga){
        this.idDescarga=idDescarga;
    }

    public Descarga(String parametros, Date fecha) {
        this.parametros = split(parametros);
        this.fecha = fecha;
        this.estado = 0;
        this.datos = "";
    }
    
    public Descarga(int idDescarga, String parametros, Date fecha){
        this.idDescarga=idDescarga;
        this.parametros=parametros;
        this.fecha=fecha;
    }

    public Descarga(int idDescarga,String parametros, Date fecha, String datos, int estado) {
        this.idDescarga=idDescarga;
        this.parametros = parametros;
        this.fecha = fecha;
        this.estado = estado;
        this.datos = datos;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
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

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public int getIdDescarga() {
        return idDescarga;
    }

    public void setIdDescarga(int idDescarga) {
        this.idDescarga = idDescarga;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Descarga other = (Descarga) obj;
        if ((this.parametros == null) ? (other.parametros != null) : !this.parametros.equals(other.parametros)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.parametros != null ? this.parametros.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.parametros;
    }

    private static String split(String str) {
        String link = "";
        String cabecera = "/WEB_TTRA_CONSULTA/VisualizacionEdicto.faces";
        String fin = "%0D%0A%26subidioma";
        int a = 0, b = 0;
        String split;

        while (a != -1) {
            a = str.indexOf(cabecera, a + 1);
            b = str.indexOf(fin, b + 1);

            if (a != -1) {
                split = str.substring(a, b + 6);
                String[] aux = split.split("=");
                link = aux[1];
            }
        }
        return link;
    }

    public String creaDescarga() {
        String query = "INSERT into datagest.descarga (parametros, fecha, estado) values("
                + Datagest.entrecomillar(getParametros()) + ","
                + Datagest.entrecomillar(Datagest.imprimeFecha(getFecha())) + ","
                + getEstado()
                + ")";
        return query;
    }

    public String borraDescarga() {
        String query = "DELETE FROM datagest.descarga WHERE idDescarga=" + getIdDescarga();
        return query;
    }

    public String buscaDescarga() {
        String query = "SELECT * FROM datagest.descarga WHERE parametros=" + Datagest.entrecomillar(getParametros());
        return query;
    }
    
    public String cargaDescarga(){
        String query="SELECT * FROM datagest.descarga WHERE idDescarga="+getIdDescarga();
        return query;
    }
    
    public static String listaDescarga(int estado){
        String query="SELECT * FROM datagest.descarga WHERE estado="+estado+" limit "+10;
        return query;
    }
    
    
}

