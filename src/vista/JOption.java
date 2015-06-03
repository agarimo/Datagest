package vista;

import java.awt.Container;
import javax.swing.JOptionPane;

/**
 *
 * @author Agarimo
 */
public class JOption {

    public static void error(Container cont, String error) {
        JOptionPane.showMessageDialog(cont, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void ioe(Container cont, String error) {
        JOptionPane.showMessageDialog(cont, error, "Error I/O", JOptionPane.ERROR_MESSAGE);
    }

    public static void sql(Container cont, String error) {
        JOptionPane.showMessageDialog(cont, error, "Error SQL", JOptionPane.ERROR_MESSAGE);
    }

    public static void classNotFound(Container cont, String error) {
        JOptionPane.showMessageDialog(cont, error, "Error Class Not Found", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void completado(Container cont, String error) {
        JOptionPane.showMessageDialog(cont, error, "Completado", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static int opcion(Container cont, String opcion){
        return JOptionPane.showConfirmDialog(cont, opcion,"Seleccionar opcion" , JOptionPane.YES_NO_OPTION);
    }
    
}
