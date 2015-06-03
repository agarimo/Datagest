package modeloTablas;

import entidades.Multa;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Agárimo
 */
public class modeloMultasP extends AbstractTableModel {

    private int numColumnas = 15;
    private String[] nombresDeColumnas = {"id","idEdicto", "Expediente","Nombre","DNI","Localidad","Fecha","Matrícula",
        "Cuantía","Precepto","Artículo","Puntos","Obs","Fase","Valido"};
    private ArrayList<String[]> ResultSets;
    Multa multa;
    int contador=1;

    public modeloMultasP(LinkedList lk) {
        Iterator it=lk.iterator();
        ResultSets = new ArrayList<String[]>();
        try {
            while (it.hasNext()) {
                multa=(Multa)it.next();
                
//                String[] fila = {Integer.toString(contador),multa.getIdEdicto(),multa.getExpediente(),multa.getSancionado(),multa.getIdSancionado(),
//                    multa.getLocalidad(),multa.getFechaInfraccion(),multa.getMatricula(),multa.getCuantia(),
//                    multa.getPrecepto(),multa.getArt(),multa.getPuntos(),multa.getObs(),multa.getFase(),setValido(multa.getValido())};
//                ResultSets.add(fila);
                contador++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "¡ERROR: Excepción en Modelo.!" + e.getMessage(),
                    "¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String setValido(boolean aux){
        if(aux){
            return "SI";
        }else{
            return "NO";
        }
    }

    @Override
    public Object getValueAt(int indiceFila, int indiceColumna) {
        String[] fila = ResultSets.get(indiceFila);
        return fila[indiceColumna];
    }

    @Override
    public int getRowCount() {
        return ResultSets.size();
    }

    @Override
    public int getColumnCount() {
        return numColumnas;
    }

    @Override
    public String getColumnName(int numeroColumna) {

        return nombresDeColumnas[numeroColumna];
    }
}