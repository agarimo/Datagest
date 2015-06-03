package vista;

import entidades.Conexion;
import entidades.Precepto;
import entidades.Tipo;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import main.ControlDatos;
import main.Datagest;
import main.Sql;
import util.Listados;
import util.Url;

/**
 *
 * @author Agarimo
 */
public class Configuracion extends javax.swing.JDialog {

    private boolean editandoCon = false;
    private boolean editandoWeb = false;
    private boolean editandoPre = false;
    private boolean nuevaCon = false;
    private boolean nuevaWeb = false;
    private boolean nuevoPre = false;

    public Configuracion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        iniciaConfiguracion();
    }

    private void iniciaConfiguracion() {
        iniciaGeneral();
        iniciaConexiones();
        iniciaWeb();
        iniciaPreceptos();
    }

    //<editor-fold defaultstate="collapsed" desc="Inicio">
    private void iniciaGeneral() {
        List<Tipo> list = Listados.tipos();
        Iterator it = list.iterator();

        configuracionFaseAsociada.removeAllItems();
        configuracionFaseAsociada.addItem(new Tipo("Sin asignar"));

        while (it.hasNext()) {
            configuracionFaseAsociada.addItem((Tipo) it.next());
        }
        configuracionFaseAsociada.setSelectedItem(Datagest.faseAsociada);
    }

    private void iniciaConexiones() {
        botonAceptar.setVisible(false);
        botonCancelar.setVisible(false);
        DefaultListModel modelo = new DefaultListModel();
        Iterator it = Datagest.listCon.iterator();
        Conexion con;

        while (it.hasNext()) {
            con = (Conexion) it.next();
            modelo.addElement(con);
        }
        listaCon.setModel(modelo);
        limpiaCamposCon();
    }

    private void iniciaWeb() {
        aceptarWeb.setVisible(false);
        cancelaWeb.setVisible(false);
        DefaultListModel modelo = new DefaultListModel();
        Iterator it = Datagest.listUrl.iterator();
        Url url;

        while (it.hasNext()) {
            url = (Url) it.next();
            modelo.addElement(url);
        }
        listaWeb.setModel(modelo);
        limpiaCamposWeb();
    }

    private void iniciaPreceptos() {
        aceptarPrecepto.setVisible(false);
        cancelarPrecepto.setVisible(false);
        DefaultListModel modelo = new DefaultListModel();
        Iterator it = Listados.preceptos().iterator();
        Precepto precepto;

        while (it.hasNext()) {
            precepto = (Precepto) it.next();
            modelo.addElement(precepto);
        }
        listaPrecepto.setModel(modelo);
        limpiaCamposPrecepto();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Conexion">
    private void mostrarConexion(Conexion con) {
        if (!editandoCon) {
            nombreCon.setText(con.getNombre());
            direccionCon.setText(con.getDireccion());
            puertoCon.setText(con.getPuerto());
            usuarioCon.setText(con.getUsuario());
            passCon.setText(con.getPass());
        }
    }

    private void nuevaConexion() {
        editandoCon = true;
        habilitaBotonesCon(true);
        limpiaCamposCon();
        habilitaCamposCon(true);
    }

    private void editaConexion() {
        editandoCon = true;
        habilitaBotonesCon(true);
        habilitaCamposCon(true);
        nombreCon.setEditable(false);
    }

    private void borraConexion(Conexion con) {
        ControlDatos cd = new ControlDatos(" ");
        cd.borraCon(con);
        cd.actualizaLista();
        iniciaConexiones();
    }

    private void aceptarCon() {
        Conexion aux = new Conexion(nombreCon.getText(), direccionCon.getText(), puertoCon.getText(), usuarioCon.getText(), passCon.getText());
        ControlDatos cd = new ControlDatos("");
        if (cd.buscar(aux)) {
            if (nuevaCon) {
                JOption.error(this, "Ya existe una conexión con ese nombre");
            } else {
                cd.modificaCon(aux);
            }
        } else {
            cd.nuevaCon(aux);
        }
        cd.actualizaLista();
        iniciaConexiones();
    }

    private void limpiaCamposCon() {
        nombreCon.setText("");
        direccionCon.setText("");
        puertoCon.setText("");
        usuarioCon.setText("");
        passCon.setText("");
    }

    private void habilitaCamposCon(Boolean aux) {
        nombreCon.setEditable(aux);
        direccionCon.setEditable(aux);
        puertoCon.setEditable(aux);
        usuarioCon.setEditable(aux);
        passCon.setEditable(aux);
    }

    private void habilitaBotonesCon(Boolean aux) {
        botonAceptar.setVisible(aux);
        botonCancelar.setVisible(aux);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Web">
    private void mostrarWeb(Url url) {
        if (!editandoWeb) {
            webNombre.setText(url.getNombre());
            webUrl.setText(url.getUrl());
        }
    }

    private void nuevaWeb() {
        editandoWeb = true;
        habilitaBotonesWeb(true);
        limpiaCamposWeb();
        habilitaCamposWeb(true);
    }

    private void editaWeb() {
        editandoWeb = true;
        habilitaBotonesWeb(true);
        habilitaCamposWeb(true);
        webNombre.setEditable(false);
    }

    private void borraWeb(Url url) {
        ControlDatos cd = new ControlDatos("");
        cd.borraUrl(url);
        cd.actualizaLista();
        iniciaWeb();
    }

    private void aceptarWeb() {
        Url url = new Url(webNombre.getText(), webUrl.getText());
        ControlDatos cd = new ControlDatos("");
        if (cd.buscar(url)) {
            if (nuevaWeb) {
                JOption.error(this, "Ya existe una web con ese nombre");
            } else {
                cd.modificaUrl(url);
            }
        } else {
            cd.nuevaUrl(url);
        }
        cd.actualizaLista();
        iniciaWeb();
    }

    private void limpiaCamposWeb() {
        webNombre.setText("");
        webUrl.setText("");
    }

    private void habilitaCamposWeb(Boolean aux) {
        webNombre.setEditable(aux);
        webUrl.setEditable(aux);
    }

    private void habilitaBotonesWeb(Boolean aux) {
        aceptarWeb.setVisible(aux);
        cancelaWeb.setVisible(aux);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Preceptos">
    private void mostrarPrecepto(Precepto precepto) {
        if (!editandoPre) {
            preceptoNombre.setText(precepto.getNombre());
            preceptoDescripcion.setText(precepto.getDescripcion());
        }
    }

    private void nuevoPrecepto() {
        editandoPre = true;
        habilitaCamposPrecepto(true);
        habilitaBotonesPrecepto(true);
        limpiaCamposPrecepto();
    }

    private void editaPrecepto() {
        editandoPre = true;
        habilitaCamposPrecepto(true);
        preceptoNombre.setEditable(false);
        habilitaBotonesPrecepto(true);
    }

    private void borraPrecepto(Precepto precepto) {
        Sql.ejecutar(precepto.borraPrecepto(), Datagest.con);
        iniciaPreceptos();
    }

    private void aceptaPrecepto() {
        Precepto precepto = new Precepto(preceptoNombre.getText().toUpperCase(), preceptoDescripcion.getText().toUpperCase());
        Sql bd;
        try {
            bd = new Sql(Datagest.con);

            if (bd.buscar(precepto.buscaPrecepto()) > 0) {
                if (nuevoPre) {
                    vista.JOption.error(this, "Ya existe ese precepto");
                } else {
                    bd.ejecutar(precepto.editaPrecepto());
                }
            } else {
                bd.ejecutar(precepto.creaPrecepto());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.sql(this, "Error al aceptar creacion/edicion" + ex.getMessage());
        }
    }

    private void limpiaCamposPrecepto() {
        preceptoNombre.setText("");
        preceptoDescripcion.setText("");
    }

    private void habilitaCamposPrecepto(Boolean aux) {
        preceptoNombre.setEditable(aux);
        preceptoDescripcion.setEditable(aux);
    }

    private void habilitaBotonesPrecepto(Boolean aux) {
        aceptarPrecepto.setVisible(aux);
        cancelarPrecepto.setVisible(aux);
    }
    //</editor-fold>

    private Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("ico/run32.png"));
        return retValue;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        configuracionFaseAsociada = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaCon = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nombreCon = new javax.swing.JTextField();
        direccionCon = new javax.swing.JTextField();
        puertoCon = new javax.swing.JTextField();
        usuarioCon = new javax.swing.JTextField();
        passCon = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        botonAceptar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaWeb = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        webNombre = new javax.swing.JTextField();
        webUrl = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        aceptarWeb = new javax.swing.JButton();
        cancelaWeb = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaPrecepto = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        preceptoDescripcion = new javax.swing.JTextField();
        preceptoNombre = new javax.swing.JTextField();
        aceptarPrecepto = new javax.swing.JButton();
        cancelarPrecepto = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Configuración");
        setAlwaysOnTop(true);
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        setResizable(false);

        jLabel6.setText("Fase asociada a articulo: ");

        configuracionFaseAsociada.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sin asignar" }));

        jButton5.setText("Aplicar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(configuracionFaseAsociada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 280, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(configuracionFaseAsociada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );

        jTabbedPane1.addTab("General", jPanel5);

        listaCon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaConMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaCon);

        jLabel1.setText("Nombre:");

        jLabel2.setText("Dirección:");

        jLabel3.setText("Puerto:");

        jLabel4.setText("Usuario:");

        jLabel5.setText("Contraseña:");

        nombreCon.setEditable(false);

        direccionCon.setEditable(false);

        puertoCon.setEditable(false);

        usuarioCon.setEditable(false);

        passCon.setEditable(false);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/add32.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/edit32.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/borrar32.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        botonAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/aceptar32.png"))); // NOI18N
        botonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptarActionPerformed(evt);
            }
        });

        botonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/cancel32.png"))); // NOI18N
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombreCon)
                            .addComponent(direccionCon)
                            .addComponent(usuarioCon)
                            .addComponent(passCon)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(puertoCon, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 158, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(botonCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(botonAceptar)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(nombreCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(direccionCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(puertoCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(usuarioCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(passCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonAceptar)
                            .addComponent(botonCancelar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jButton4)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Conexión", jPanel1);

        listaWeb.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listaWeb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaWebMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(listaWeb);

        jLabel7.setText("Nombre:");

        jLabel8.setText("URL: ");

        webNombre.setEditable(false);
        webNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        webUrl.setEditable(false);
        webUrl.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/add32.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/edit32.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/borrar32.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        aceptarWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/aceptar32.png"))); // NOI18N
        aceptarWeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarWebActionPerformed(evt);
            }
        });

        cancelaWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/cancel32.png"))); // NOI18N
        cancelaWeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelaWebActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                        .addGap(311, 311, 311))
                    .addComponent(webUrl)
                    .addComponent(webNombre)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(aceptarWeb)
                                .addGap(18, 18, 18)
                                .addComponent(cancelaWeb))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton8)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(webNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(webUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(cancelaWeb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                                .addComponent(jButton6))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(aceptarWeb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Web´s", null, jPanel6, "");

        listaPrecepto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaPreceptoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(listaPrecepto);

        jLabel9.setText("Nombre");

        jLabel10.setText("Descripcion");

        preceptoDescripcion.setEditable(false);
        preceptoDescripcion.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        preceptoNombre.setEditable(false);
        preceptoNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        aceptarPrecepto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/aceptar32.png"))); // NOI18N
        aceptarPrecepto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarPreceptoActionPerformed(evt);
            }
        });

        cancelarPrecepto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/cancel32.png"))); // NOI18N
        cancelarPrecepto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarPreceptoActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/edit32.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/add32.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/borrar32.png"))); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(preceptoNombre)
                            .addComponent(preceptoDescripcion)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 175, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(cancelarPrecepto)
                                .addGap(18, 18, 18)
                                .addComponent(aceptarPrecepto))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jButton11)
                                .addGap(18, 18, 18)
                                .addComponent(jButton9)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(preceptoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(preceptoDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(aceptarPrecepto)
                                    .addComponent(cancelarPrecepto))
                                .addGap(0, 116, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton10)
                                    .addComponent(jButton9)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton11))))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Preceptos", jPanel7);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Validador", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Introduce BBDD", jPanel4);

        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//BOTÓN CERRAR.
        Datagest.ventana.inicio();
        this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//NUEVA CONEXIÓN
        nuevaConexion();
        nuevaCon = true;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//EDITAR CONEXIÓN
        Conexion con = (Conexion) listaCon.getSelectedValue();
        if (con != null) {
            editaConexion();
        } else {
            JOption.error(this, "Debes seleccionar una conexión");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
//ELIMINAR CONEXIÓN
        Conexion con = (Conexion) listaCon.getSelectedValue();
        if (con != null) {
            borraConexion(con);
        } else {
            JOption.error(this, "Debes seleccionar una conexión");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void listaConMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaConMouseClicked
//MOSTRAR ELEMENTO SELECCIONADO CONEXIÓN
        mostrarConexion((Conexion) listaCon.getSelectedValue());
    }//GEN-LAST:event_listaConMouseClicked

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
//CANCELA EDICION
        habilitaBotonesCon(false);
        habilitaCamposCon(false);
        limpiaCamposCon();
        editandoCon = false;
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void botonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptarActionPerformed
//ACEPTA CON
        aceptarCon();
        habilitaBotonesCon(false);
        habilitaCamposCon(false);
        limpiaCamposCon();
        editandoCon = false;
        nuevaCon = false;
    }//GEN-LAST:event_botonAceptarActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
//BOTON APLICAR GENERAL
        Tipo aux = (Tipo) configuracionFaseAsociada.getSelectedItem();
        Datagest.faseAsociada = aux;
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
//NUEVA WEB
        nuevaWeb();
        nuevaWeb = true;
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
//EDITA WEB
        Url url = (Url) listaWeb.getSelectedValue();
        if (url != null) {
            editaWeb();
        } else {
            JOption.error(this, "Debes seleccionar una Web");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
//BORRA WEB 
        Url url = (Url) listaWeb.getSelectedValue();
        if (url != null) {
            borraWeb(url);
        } else {
            JOption.error(this, "Debes seleccionar una Web");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void aceptarWebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarWebActionPerformed
//ACEPTA WEB
        aceptarWeb();
        habilitaBotonesWeb(false);
        habilitaCamposWeb(false);
        limpiaCamposWeb();
        editandoWeb = false;
        nuevaWeb = false;
    }//GEN-LAST:event_aceptarWebActionPerformed

    private void cancelaWebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelaWebActionPerformed
//CANCELA WEB     
        habilitaBotonesWeb(false);
        habilitaCamposWeb(false);
        limpiaCamposWeb();
        editandoWeb = false;
    }//GEN-LAST:event_cancelaWebActionPerformed

    private void listaWebMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaWebMouseClicked
//MOSTRAR WEB
        Url url = (Url) listaWeb.getSelectedValue();
        mostrarWeb(url);
    }//GEN-LAST:event_listaWebMouseClicked

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
//NUEVO PRECEPTO
        nuevoPre=true;
        nuevoPrecepto();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
//EDITA PRECEPTO
        nuevoPre=false;
        editaPrecepto();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
//BORRA PRECEPTO
        Precepto precepto = (Precepto) listaPrecepto.getSelectedValue();
        if (precepto != null) {
            borraPrecepto(precepto);
        } else {
            vista.JOption.error(this, "Debes seleccionar un precepto");
        }
        iniciaPreceptos();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void listaPreceptoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaPreceptoMouseClicked
//MOSTRAR PRECEPTO
        Precepto precepto = (Precepto) listaPrecepto.getSelectedValue();
        if (precepto != null) {
            mostrarPrecepto(precepto);
        } 
    }//GEN-LAST:event_listaPreceptoMouseClicked

    private void aceptarPreceptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarPreceptoActionPerformed
//ACEPTAR PRECEPTO
        aceptaPrecepto();
        habilitaBotonesPrecepto(false);
        habilitaCamposPrecepto(false);
        limpiaCamposPrecepto();
        editandoPre = false;
        nuevoPre = false;
        iniciaPreceptos();
    }//GEN-LAST:event_aceptarPreceptoActionPerformed

    private void cancelarPreceptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarPreceptoActionPerformed
//CANCELA PRECEPTO
        habilitaBotonesPrecepto(false);
        habilitaCamposPrecepto(false);
        limpiaCamposPrecepto();
        editandoPre = false;
        nuevoPre = false;
    }//GEN-LAST:event_cancelarPreceptoActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarPrecepto;
    private javax.swing.JButton aceptarWeb;
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton cancelaWeb;
    private javax.swing.JButton cancelarPrecepto;
    private javax.swing.JComboBox configuracionFaseAsociada;
    private javax.swing.JTextField direccionCon;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList listaCon;
    private javax.swing.JList listaPrecepto;
    private javax.swing.JList listaWeb;
    private javax.swing.JTextField nombreCon;
    private javax.swing.JTextField passCon;
    private javax.swing.JTextField preceptoDescripcion;
    private javax.swing.JTextField preceptoNombre;
    private javax.swing.JTextField puertoCon;
    private javax.swing.JTextField usuarioCon;
    private javax.swing.JTextField webNombre;
    private javax.swing.JTextField webUrl;
    // End of variables declaration//GEN-END:variables
}
