package vista;

import entidades.Edicto;
import entidades.Origen;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Agárimo
 */
public class Visual extends javax.swing.JFrame {
    
    public Visual(String str){
        initComponents();
        text.setText(str);
        text.setCaretPosition(0);
    }

    public Visual(Edicto aux) {
        initComponents();
        this.setTitle(aux.getIdEdicto());
        text.setText(getDatos(aux));
        text.setCaretPosition(0);
    }

    public Visual(List<Edicto> aux) {
        initComponents();
        this.setTitle("TODOS");
        text.setText(getDatos(aux));
        text.setCaretPosition(0);
    }

    private String getDatos(Edicto aux) {
        Edicto edicto = util.GetEntidad.edictoCompleto(aux);

        return edicto.getDatos();
    }

    private String getDatos(List<Edicto> lista) {
        String str, datos = "";
        String separador = "\n ------------------------ \n";

        Edicto edicto;
        Origen origen;
        Iterator it = lista.iterator();

        while (it.hasNext()) {
            edicto = util.GetEntidad.edictoCompleto((Edicto) it.next());
            origen = util.GetEntidad.origen(new Origen(edicto.getOrigen()));
            str = getCabecera(edicto.getTipo(), origen.getCodigo()) + "\n" + edicto.getDatos();
            datos = datos + str + separador;
        }
        return datos;
    }

    private String getCabecera(String tipo, String codigo) {
        String cadena;
        tipo = tipo.replace("*", "");
        tipo = tipo.toUpperCase();
        if ("".equals(codigo) || codigo == null) {
            codigo = "BCN1";
        }
        cadena = codigo + "-(" + tipo + ")-";

        return cadena;
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("ico/multas32.png"));

        return retValue;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visualizador de Edictos");
        setIconImage(getIconImage());
        setName("Visual");

        jScrollPane2.setViewportView(text);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane text;
    // End of variables declaration//GEN-END:variables
}
