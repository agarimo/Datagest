package util;

import entidades.Descarga;
import entidades.Edicto;
import entidades.Origen;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;
import main.Sql;

/**
 *
 * @author Agárimo
 */
public class GetEntidad {
    
    
    public static Edicto edicto(Edicto aux){
        Edicto edicto = null;
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            edicto=bd.getEdicto(aux);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return edicto;
    }
    
    public static Edicto edictoCompleto(Edicto aux){
        Edicto edicto = null;
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            edicto=bd.getEdictoCompleto(aux);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return edicto;
    }
    
    public static Origen origen(Origen aux){
        Origen origen = null;
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            origen=bd.getOrigen(aux);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return origen;
    }
    
    public static Descarga descarga(Descarga aux){
        Descarga descarga = null;
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            descarga=bd.getDescargaCompleta(aux);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Listados.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
        }
        return descarga;
    }
}
