package modeloTablas;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Agárimo
 */
public class modeloMultas extends AbstractTableModel {

    private int numColumnas = 13;
    private String[] nombresDeColumnas = {"idEdicto", "Expediente","Nombre","DNI","Localidad","Fecha","Matrícula",
        "Cuantía","Precepto","Artículo","Puntos","Obs","Fase"};
    private ArrayList<String[]> ResultSets;

    public modeloMultas(ResultSet rs) {
        ResultSets = new ArrayList<String[]>();
        try {
            while (rs.next()) {
                String[] fila = {rs.getString("idEdicto"),rs.getString("expediente"),rs.getString("idSancionado"),rs.getString("nombre"),
                    rs.getString("localidad"),rs.getString("fechaInfraccion"),rs.getString("matricula"),Double.toString(rs.getDouble("cuantia")),
                    rs.getString("precepto"),rs.getString("art"),Integer.toString(rs.getInt("puntos")),rs.getString("obs"),rs.getString("fase")};
                ResultSets.add(fila);
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
