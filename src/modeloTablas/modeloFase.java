package modeloTablas;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Agárimo
 */
public class modeloFase extends AbstractTableModel {

    private int numColumnas = 7;
    private String[] nombresDeColumnas = {"idFase", "Código", "Origen", "Tipo", "Texto 1", "Texto 2", "Dias"};
    private ArrayList<String[]> ResultSets;

    public modeloFase(ResultSet rs) {
        ResultSets = new ArrayList<String[]>();

        try {
            while (rs.next()) {
                String[] fila = {Integer.toString(rs.getInt("idFase")), rs.getString("codigo"), rs.getString("origen"), getTipo(rs.getInt("tipo")), rs.getString("texto1"),
                    rs.getString("texto2"), Integer.toString(rs.getInt("dias"))};
                ResultSets.add(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "¡ERROR: Excepción en Modelo.!" + e.getMessage(),
                    "¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTipo(int a) {
        if (a == 1) {
            return "ND";
        } else if (a == 2) {
            return "RS";
        } else if (a == 3) {
            return "RR";
        } else {
            return null;
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
