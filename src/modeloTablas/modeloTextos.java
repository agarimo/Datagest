/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloTablas;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Agarimo
 */
public class modeloTextos extends AbstractTableModel {
   private int numColumnas = 2;
    private String[] nombresDeColumnas = {"Texto 1", "Texto 2"};
    private ArrayList<String[]> ResultSets;
    public static LinkedList listTipo;
    public static LinkedList listId;

    public modeloTextos(ResultSet rs) {
        ResultSets = new ArrayList<String[]>();
        listTipo=new LinkedList();
        listId=new LinkedList();
        try {
            while (rs.next()) {
                String[] fila = {rs.getString("texto1"),rs.getString("texto2"),rs.getString("codigo")};
                String aux = rs.getString("codigo");
                int id = rs.getInt("idFase");
                ResultSets.add(fila);
                listTipo.add(aux);
                listId.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "¡ERROR: Excepción en Modelo.!" + e.getMessage(),
                    "¡ERROR!", JOptionPane.ERROR_MESSAGE);
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
