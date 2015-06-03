package hilos;

import entidades.Boletin;
import java.sql.SQLException;
import java.util.List;
import main.Datagest;
import main.Sql;
import modeloTablas.modeloEdicto;
import util.ParametrosEdicto;
import vista.Main;

/**
 *
 * @author Agárimo
 */
public class ProcesoEdictos extends Thread {

    private ParametrosEdicto pe;
    private Sql bd;
    private boolean modo;
    private boolean start = true;

    public ProcesoEdictos(boolean modo) {
        try {
            bd = new Sql(Datagest.con);
            pe = main.Datagest.ventana.getParametrosEdicto();
        } catch (SQLException ex) {
            start = false;
        } catch (NullPointerException e) {
            start = false;
        } catch (ArrayIndexOutOfBoundsException ei) {
            start = false;
        }
        this.modo = modo;
    }

    @Override
    public void run() {
        try {
            if (start) {
                Datagest.ventana.setEspera();
                proceso();
                Main.run = false;
            }
            bd.close();
            Datagest.ventana.setDefault();
        } catch (Exception e) {
            Main.run = false;
            try {
                Datagest.ventana.setDefault();
            } catch (Exception ex) {
                Main.run = false;
            }
        }
    }

    private void proceso() throws SQLException {
        modeloEdicto model = new modeloEdicto(bd.ejecutarQueryRs(getQuery()));
        if (modo) {
            Datagest.ventana.setComboBoletin(getBoletines());
        }
        Datagest.ventana.setModelEdicto(model);
        Datagest.ventana.labelEdictoTotal.setText(Integer.toString(model.getRowCount())+"/"+(Integer.toString(totalDescargas())));
    }
    
    private int totalDescargas() throws SQLException{
        String query="SELECT count(*) FROM datagest.descarga where fecha="+ Datagest.entrecomillar(Datagest.imprimeFecha(pe.getFecha()));
        return bd.contadorRegistros(query);
    }

    private List<Boletin> getBoletines() throws SQLException {
        String query = "SELECT * FROM datagest.boletines where fecha=" + Datagest.entrecomillar(Datagest.imprimeFecha(pe.getFecha()));
        return bd.listaBoletin(query);
    }

    private String getQuery() {
        String query = "SELECT * FROM datagest.vistaEdictos WHERE fecha=" + Datagest.entrecomillar(Datagest.imprimeFecha(pe.getFecha()));

        if (!pe.getBoletin().equals(new Boletin("Todos"))) {
            query = query + " AND origen=" + Datagest.entrecomillar(pe.getBoletin().getOrigen());
        }
        if (pe.getEstado() > 0) {
            query = query + " AND estado=" + pe.getEstado();
        }

        return query;
    }
}
