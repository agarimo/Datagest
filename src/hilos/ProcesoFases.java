package hilos;

import entidades.Edicto;
import entidades.Fase;
import entidades.Origen;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;
import main.Sql;
import util.GetFases;
import vista.Main;
import vista.Visual;

/**
 *
 * @author Agárimo
 */
public class ProcesoFases extends Thread {

    boolean todos = false;
    String str = "";
    List<Edicto> listaEdictos;
    List<Edicto> listaFinal;
    GetFases gf;
    Edicto edicto;
    Fase fase;
    private int total;
    private int indice = 1;

    public ProcesoFases() {
        todos = true;
        cargaEdictos();
        listaFinal = new ArrayList<Edicto>();
        total = listaEdictos.size();
    }

    public ProcesoFases(Date fecha) {
        todos = false;
        cargaEdictos(fecha);
        listaFinal = new ArrayList<Edicto>();
        total = listaEdictos.size();
    }

    @Override
    public void run() {
        if (todos) {
            Datagest.ventana.setEspera();
            gf = new GetFases();
            proceso();
            if (vista.JOption.opcion(Datagest.ventana, "¿Desea ver el registro de las fases pendientes?")==0) {
                Visual vi = new Visual(str);
                vi.setSize(275, 400);
                vi.setTitle("Fases pendientes");
                vi.setVisible(true);
                vi.setLocationRelativeTo(null);
            }
            Datagest.ventana.setDefault();
        } else {
            Datagest.ventana.setEspera();
            gf = new GetFases();
            proceso();
            Datagest.ventana.cargaEdictos(true);
            Datagest.ventana.setDefault();
        }
    }

    private void cargaEdictos() {
        listaEdictos = util.Listados.edictos();
    }

    private void cargaEdictos(Date fecha) {
        listaEdictos = util.Listados.edictos(fecha);
    }

    private void proceso() {
        Iterator it = listaEdictos.iterator();

        while (it.hasNext()) {
            edicto = (Edicto) it.next();
            compruebaFase(edicto);
            it.remove();

            if (fase != null) {
                edicto.setTipo(fase.getCodigo());
                edicto.setFase(fase.toString().toUpperCase());
                edicto.setEstado(2);
                listaFinal.add(edicto);
                fase = null;
            } else {
                str = str + edicto.getFecha() + " | " + edicto.getOrigen() + " | " + edicto.getIdEdicto() + "\n";
            }
            Main.proceso.setValor((indice * 100) / total);
            indice++;
        }
        cargaDatos();
        Main.proceso.close();
    }

    private void compruebaFase(Edicto edicto) {
        String str = descargaDatos(edicto);
        Iterator it = gf.get(new Origen(edicto.getOrigen())).iterator();

        while (it.hasNext()) {
            Fase aux = (Fase) it.next();
            boolean bol = aux.contiene(str);

            if (bol) {
                fase = aux;
            }
        }
    }

    private String descargaDatos(Edicto edicto) {
        String datos;
        Edicto aux = util.GetEntidad.edictoCompleto(edicto);

        datos = aux.getDatos();
        datos = datos.replace("\r\n", " ");

        return datos;
    }

    private void cargaDatos() {
        try {
            Edicto aux;
            Iterator it = listaFinal.iterator();
            Sql bd = new Sql(Datagest.con);
            while (it.hasNext()) {
                aux = (Edicto) it.next();
                bd.ejecutar(aux.editaEdicto());
            }
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProcesoFases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}