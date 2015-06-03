package entidades;

import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Requerimiento {

    private int idRequerimiento;
    private String origen;
    private String tipo;
    private String valor;
    private String valorAsociado;

    public Requerimiento(String origen, String tipo, String valor, String valorAsociado) {
        this.origen = origen;
        this.tipo = tipo;
        this.valor = valor;
        this.valorAsociado = valorAsociado;
    }

    public Requerimiento(int id, String origen, String tipo, String valor, String valorAsociado) {
        this.idRequerimiento = id;
        this.origen = origen;
        this.tipo = tipo;
        this.valor = valor;
        this.valorAsociado = valorAsociado;
    }

    public int getIdRequerimiento() {
        return idRequerimiento;
    }

    public void setIdRequerimiento(int idRequerimiento) {
        this.idRequerimiento = idRequerimiento;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValorAsociado() {
        return valorAsociado;
    }

    public void setValorAsociado(String valorAsociado) {
        this.valorAsociado = valorAsociado;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Requerimiento other = (Requerimiento) obj;
        if ((this.origen == null) ? (other.origen != null) : !this.origen.equals(other.origen)) {
            return false;
        }
        if ((this.tipo == null) ? (other.tipo != null) : !this.tipo.equals(other.tipo)) {
            return false;
        }
        if ((this.valor == null) ? (other.valor != null) : !this.valor.equals(other.valor)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.origen != null ? this.origen.hashCode() : 0);
        hash = 47 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        hash = 47 * hash + (this.valor != null ? this.valor.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.valor;
    }

    public String buscaRequerimiento() {
        String query = "SELECT * FROM datagest.requerimiento WHERE "
                + "idOrigen=" + Datagest.entrecomillar(this.origen)
                + "tipo=" + Datagest.entrecomillar(this.tipo)
                + " and valor=" + Datagest.entrecomillar(this.valor) + ";";
        return query;
    }

    public String creaRequerimiento() {
        String query = "INSERT into datagest.requerimiento (idOrigen, tipo, valor, valorAsociado) values("
                + Datagest.entrecomillar(this.origen) + ","
                + Datagest.entrecomillar(this.tipo) + ","
                + Datagest.entrecomillar(this.valor) + ","
                + Datagest.entrecomillar(this.valorAsociado)
                + ")";
        return query;
    }

    public String borraRequerimiento() {
        String query = "DELETE FROM datagest.requerimiento WHERE idRequerimiento=" + this.idRequerimiento + ";";
        return query;
    }

    public String editaRequerimiento() {
        String query = "UPDATE datagest.requerimiento SET "
                + "idOrigen=" + Datagest.entrecomillar(this.origen) + ","
                + "tipo=" + Datagest.entrecomillar(this.tipo) + ","
                + "valor=" + Datagest.entrecomillar(this.valor) + ","
                + "valorAsociado=" + Datagest.entrecomillar(this.valorAsociado)
                + "WHERE idRequerimiento=" + this.idRequerimiento;
        return query;
    }
}
