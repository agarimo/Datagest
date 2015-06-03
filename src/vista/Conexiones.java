package vista;

import entidades.Conexion;
import main.ControlDatos;

/**
 *
 * @author Agárimo
 */
public class Conexiones extends javax.swing.JDialog {
    
    int modo;

    /**
     * Creates new form Fases
     *
     * @param parent
     * @param modal modo Caso 0 = nueva fase, Caso 1 =Editar fase;
     */
    public Conexiones(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        modo = 0;
        setTitulo();
    }
    
    public Conexiones(java.awt.Frame parent, boolean modal, Conexion conexion) {
        this(parent, modal);
        modo = 1;
        setTitulo();
        setDatos(conexion);
    }
    
    private void setTitulo() {
        switch (modo) {
            case 0:
                this.setTitle("Nueva Conexion");
                break;
            case 1:
                this.setTitle("Editar Conexion");
                break;
        }
    }
    
    private void setDatos(Conexion conexion) {
        this.textNombre.setText(conexion.getNombre());
        this.textRuta.setText(conexion.getDireccion());
        this.textPuerto.setText(conexion.getPuerto());
        this.textUsuario.setText(conexion.getUsuario());
        this.textContraseña.setText(conexion.getPass());
    }
    
    private void guardarNuevo() {
        String nombre = this.textNombre.getText();
        String direccion = this.textRuta.getText();
        String puerto = this.textPuerto.getText();
        String usuario = this.textUsuario.getText();
        String contraseña = this.textContraseña.getText();
        
        Conexion con = new Conexion(nombre, direccion, puerto, usuario, contraseña);
        ControlDatos cd = new ControlDatos(" ");
        cd.nuevaCon(con);
        cd.actualizaLista();
    }
    
    private void guardarEditado() {
        String nombre = this.textNombre.getText();
        String direccion = this.textRuta.getText();
        String puerto = this.textPuerto.getText();
        String usuario = this.textUsuario.getText();
        String contraseña = this.textContraseña.getText();
        
        Conexion con = new Conexion(nombre, direccion, puerto, usuario, contraseña);
        ControlDatos cd = new ControlDatos(" ");
        cd.modificaCon(con);
        cd.actualizaLista();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textNombre = new javax.swing.JTextField();
        textRuta = new javax.swing.JTextField();
        textPuerto = new javax.swing.JTextField();
        textUsuario = new javax.swing.JTextField();
        textContraseña = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);

        jLabel1.setText("Nombre:");

        jLabel2.setText("Ruta:");

        jLabel3.setText("Puerto:");

        jLabel4.setText("Usuario:");

        jLabel5.setText("Contraseña:");

        textNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        textRuta.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        textPuerto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        textUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        textContraseña.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/aceptar32.png"))); // NOI18N
        jButton1.setToolTipText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/cancel32.png"))); // NOI18N
        jButton2.setToolTipText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textContraseña))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textRuta, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                            .addComponent(textNombre)
                            .addComponent(textPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textUsuario))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(textContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {textContraseña, textPuerto, textRuta, textUsuario});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        this.dispose();
        
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        switch (modo) {
            case 0:
                guardarNuevo();
                this.dispose();
                break;
            case 1:
                guardarEditado();
                this.dispose();
                break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField textContraseña;
    private javax.swing.JTextField textNombre;
    private javax.swing.JTextField textPuerto;
    private javax.swing.JTextField textRuta;
    private javax.swing.JTextField textUsuario;
    // End of variables declaration//GEN-END:variables
}
