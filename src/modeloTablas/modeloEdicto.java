package modeloTablas;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo Edictos
 *
 * @author Agárimo
 */
public class modeloEdicto extends AbstractTableModel {

    private int numColumnas = 5;
    private String[] nombresDeColumnas = {"idEdicto", "Origen", "Fecha", "Tipo", "Estado"};
    private ArrayList<String[]> ResultSets;

    public modeloEdicto() {
        ResultSets = new ArrayList<String[]>();
    }

    public modeloEdicto(ResultSet rs) {
        ResultSets = new ArrayList<String[]>();

        try {
            while (rs.next()) {
                String[] fila = {rs.getString("idEdicto"), rs.getString("origen"), rs.getString("fecha"), rs.getString("tipo"), getEstado(rs.getInt("estado"))};
                ResultSets.add(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "¡ERROR: Excepción en Modelo.!" + e.getMessage(),
                    "¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getEstado(int estado) {
        if (estado == -1) {
            return "En proceso";
        } else {
            String[] lista = {"Sin descargar", "Descargado", "Procesado","Bloqueado"};
            return lista[estado - 1];
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