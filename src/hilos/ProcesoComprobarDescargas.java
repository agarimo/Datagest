package hilos;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;
import main.Sql;

/**
 *
 * @author Agárimo
 */
public class ProcesoComprobarDescargas extends Thread {

    public static boolean runProcesoDescarga=false;
    boolean continuar = true;
    int edictoActivo = 0;
    int descargaActiva = 0;

    @Override
    public void run() {
        runProcesoDescarga=true;
        while (continuar) {
            comprobar();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Datagest.ventana.setLabelDescargas("bloqueado");
                Logger.getLogger(ProcesoComprobarDescargas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getDatos() {
        Datagest.ventana.setLabelDescargas("comprobando");
        String edicto = "SELECT count(*) FROM datagest.edicto WHERE estado=-1";
        String descarga = "SELECT count(*) FROM datagest.descarga WHERE estado=-1";
        try {
            Sql bd = new Sql(Datagest.con);
            edictoActivo = bd.contadorRegistros(edicto);
            descargaActiva = bd.contadorRegistros(descarga);
            bd.close();

        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
            Logger.getLogger(ProcesoComprobarDescargas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void resetear() {
        Datagest.ventana.setLabelDescargas("reiniciando");
        String query = "UPDATE datagest.edicto SET estado=1 WHERE estado=-1";
        String query1 = "UPDATE datagest.descarga SET estado=0 WHERE estado=-1";
        Sql.ejecutar(query, Datagest.con);
        Sql.ejecutar(query1, Datagest.con);
        Datagest.ventana.setLabelDescargas("reiniciado");
    }

    public void comprobar() {
        getDatos();
        if (edictoActivo > 0 || descargaActiva > 0) {
            Datagest.ventana.setLabelDescargas("comprobando");
            espera();
        } else {
            if (descargaPendiente() > 0) {
                Datagest.ventana.setLabelDescargas("descargando");
            } else {
                Datagest.ventana.setLabelDescargas("en espera");
            }
        }
    }

    private int descargaPendiente() {
        int aux = -1;
        String query = ("SELECT count(*) FROM datagest.descarga WHERE estado=0");
        Sql bd;
        try {
            bd = new Sql(Datagest.con);
            aux = bd.contadorRegistros(query);
            bd.close();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
            Logger.getLogger(ProcesoComprobarDescargas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    private void espera() {
        int ed = edictoActivo;
        int de = descargaActiva;
        int contador = 60;

        while (contador > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcesoComprobarDescargas.class.getName()).log(Level.SEVERE, null, ex);
            }
            contador--;
        }
        getDatos();

        if (de == descargaActiva) {
            Datagest.ventana.setLabelDescargas("bloqueado");
            resetear();
            comprobar();
        } else {
            Datagest.ventana.setLabelDescargas("descargando");
        }
    }
}
