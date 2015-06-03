package util;

import entidades.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;
import main.Sql;

/**
 *
 * @author Agárimo
 */
public class Listados {

    public static List<Origen> origenes() {
        List<Origen> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaOrigen("SELECT * FROM datagest.origen");
            bd.close();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Tipo> tipos() {
        List<Tipo> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaTipo("SELECT * FROM datagest.tipo WHERE tipo=" + 0);
            bd.close();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Requerimiento> requerimientos(Origen origen, Tipo tipo) {
        List<Requerimiento> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaRequerimiento("SELECT * FROM datagest.requerimiento WHERE "
                    + "idOrigen=" + Datagest.entrecomillar(origen.getIdOrigen())
                    + "and tipo=" + Datagest.entrecomillar(tipo.getIdTipo()));
            bd.close();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Fase> fases(Origen aux) {
        List<Fase> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaFase("SELECT * FROM datagest.fase WHERE origen=" + Datagest.entrecomillar(aux.getIdOrigen()));
            bd.close();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Fase> fasesCompleto() {
        List<Fase> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaFase("SELECT * FROM datagest.fase;");
            bd.close();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Boletin> boletines(Date fecha) {
        List<Boletin> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaBoletines("SELECT * from datagest.boletin WHERE fecha=" + Datagest.entrecomillar(Datagest.imprimeFecha(fecha)));
            bd.close();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Edicto> edictosCompleto(String idBoletin) {
        List<Edicto> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaEdictos("SELECT * FROM datagest.listaedicto WHERE idBoletin=" + Datagest.entrecomillar(idBoletin));
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Edicto> edictos(Date fecha) {
        List<Edicto> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaEdicto("SELECT * FROM datagest.edictos WHERE fecha=" + Datagest.entrecomillar(Datagest.imprimeFecha(fecha))
                    + " and (tipo='*711*' or tipo='*3.x*' or tipo='*751*');");
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Edicto> edictos(Origen origen) {
        List<Edicto> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaEdicto("SELECT * FROM datagest.edictos WHERE origen=" + Datagest.entrecomillar(origen.getIdOrigen()) + " order by idEdicto limit 10000");
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Edicto> edictos() {
        List<Edicto> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaEdicto("SELECT * FROM datagest.edictos WHERE (tipo='*711*' or tipo='*3.x*' or tipo='*751*') order by fecha;");
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }

    public static List<Descarga> descargas() {
        List<Descarga> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaDescargaCompleta("SELECT * FROM datagest.descarga limit 15");
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }
    


    public static List<Precepto> preceptos() {
        List<Precepto> list = new ArrayList();
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            list = bd.listaPreceptos("SELECT * FROM datagest.precepto");
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return list;
    }
}
