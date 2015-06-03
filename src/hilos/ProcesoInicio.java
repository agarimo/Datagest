package hilos;

import entidades.Edicto;
import entidades.Origen;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;
import main.Sql;
import vista.Main;
import vista.Visual;

/**
 *
 * @author Agárimo
 */
public class ProcesoInicio extends Thread {

    private int edictos;
    private int origenes;
    private int fases;
    List<Origen> listaOrigen;
    List<String> listaEdicto = new ArrayList();

    @Override
    public void run() {
        actualizaBase();
        cargaDatos();
        Datagest.ventana.setLabelTareas(Integer.toString(edictos), Integer.toString(origenes), Integer.toString(fases));
        if (!Main.coorelacion) {
            Main.coorelacion = true;
            compruebaCorelacion();
        }
    }

    private void actualizaBase() {
        try {
            Datagest.ventana.setLabelInfoBase("Actualizando Base");
            Sql.ejecutar("DELETE FROM datagest.descarga where datediff(curdate(),fecha)>60", Datagest.con);
            Datagest.ventana.setLabelInfoBase("Base actualizada");
        } catch (NullPointerException e) {
            actualizaBase();
        }
    }

    private void cargaDatos() {
        try {
            Sql bd = new Sql(Datagest.con);
            edictos = bd.contadorRegistros("SELECT count(*) FROM datagest.edicto WHERE estado<3");
            origenes = bd.contadorRegistros("SELECT count(*) FROM datagest.origen WHERE nombre='desconocido'");
            fases = bd.contadorRegistros("SELECT count(*) FROM datagest.edictos WHERE (tipo='*711*' or tipo='*3.x*' or tipo='*751*');");
        } catch (SQLException ex) {
            Logger.getLogger(ProcesoInicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void compruebaCorelacion() {
        Origen origen;
        List<Edicto> lista;
        listaOrigen = util.Listados.origenes();
        Iterator it = listaOrigen.iterator();

        while (it.hasNext()) {
            origen = (Origen) it.next();
            lista = util.Listados.edictos(origen);
            if (!lista.isEmpty()) {
                if (compruebaCorelacionEdictos(lista)) {
//                    System.out.println("Correcto");
                } else {
//                    System.out.println("Faltan");
                    buscaElemento(lista);
                }
            } else {
//                System.out.println("Origen sin edictos");
            }
        }
        if (!listaEdicto.isEmpty()) {
            if (vista.JOption.opcion(Datagest.ventana, "Existen errores en la coorelación de edictos \n ¿Desea verlos ahora?") == 0) {
                Visual vi = new Visual(convierteAString());
                vi.setSize(315, 250);
                vi.setTitle("Error en coorelación");
                vi.setVisible(true);
                vi.setLocationRelativeTo(null);
            }
        }
    }

    private boolean compruebaCorelacionEdictos(List<Edicto> lista) {
        Edicto edicto;
        int primero;
        int ultimo;
        int diferencia;

        edicto = lista.get(0);
        primero = split(edicto.getIdEdicto());

        edicto = lista.get(lista.size() - 1);
        ultimo = split(edicto.getIdEdicto());

        diferencia = ultimo - primero + 1;

        if (diferencia == lista.size()) {
            return true;
        } else {
            return false;
        }
    }

    private int split(String str) {
        int a;
        String[] split = str.split("-");
        a = Integer.parseInt(split[0]);
        return a;
    }

    private void buscaElemento(List<Edicto> lista) {
        String str;
        Edicto aux;
        int indice = split(lista.get(0).getIdEdicto());
        int num;
        Iterator it = lista.iterator();

        while (it.hasNext()) {
            aux = (Edicto) it.next();
            num = split(aux.getIdEdicto());

            if (num == indice) {
                indice++;
            } else {
                str = "Edicto: " + indice
                        + " |Origen: " + aux.getOrigen()
                        + " |Fecha próxima: " + Datagest.imprimeFecha(aux.getFecha());
                listaEdicto.add(str);
                indice++;
                indice++;
            }
        }
    }

    private String convierteAString() {
        String str = "";
        Iterator it = listaEdicto.iterator();

        while (it.hasNext()) {
            str = str + it.next() + "\n";
        }
        return str;
    }
}
