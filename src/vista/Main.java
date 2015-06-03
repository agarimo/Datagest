package vista;

import entidades.*;
import hilos.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import main.ControlDatos;
import main.Datagest;
import main.Sql;
import modeloTablas.modeloEdicto;
import util.Listados;
import util.ParametrosEdicto;
import util.Url;

/**
 *
 * @author Agárimo
 */
public final class Main extends javax.swing.JFrame {

    public static boolean run = false;
    public static boolean coorelacion = false;
    private boolean req = false;
    private boolean fas = false;
    private boolean nuevoReq = false;
    private boolean nuevaFas = false;
    public static Proceso proceso;

    public Main(boolean estado, String con) {
        initComponents();
        setEstadoCon(estado, con);
        setFecha();
        inicio();
        procesoDescarga();
    }

    private void procesoDescarga() {
        ProcesoComprobarDescargas pc = new ProcesoComprobarDescargas();
        pc.start();
    }

    public void inicio() {
        mostrarPanel("panelInicio");
        iniciaWeb();
        notificacion.setVisible(false);
        setLabelTareas("cargando", "cargando", "cargando");
        ProcesoInicio pi = new ProcesoInicio();
        pi.start();
    }

    private void iniciaWeb() {
        Url url;
        comboWeb.removeAllItems();
        Iterator it = Datagest.listUrl.iterator();

        while (it.hasNext()) {
            url = (Url) it.next();
            comboWeb.addItem(url);
        }
    }

    private void setFecha() {
        Calendar cal = Calendar.getInstance();
        Date fecha = cal.getTime();
        labelFecha.setText("Fecha: " + Datagest.imprimeFechaCompleta(fecha) + "   " + System.getProperty("os.name"));
    }

    public void setEstadoCon(boolean estado, String con) {
        if (estado) {
            this.labelInicioEstadoCon.setText("CONECTADO");
            this.labelInicioEstadoCon.setForeground(Color.GREEN);
            this.labelInicioNombreCon.setText(con);
        } else {
            this.labelInicioEstadoCon.setText("NO CONECTADO");
            this.labelInicioEstadoCon.setForeground(Color.RED);
            this.labelInicioNombreCon.setText("...");
        }
    }

    public void setLabelTareas(String edictos, String origenes, String fases) {
        labelEdictos.setText(edictos);
        labelOrigenes.setText(origenes);
        labelFases.setText(fases);
    }

    public void setLabelInfoBase(String str) {
        infoBase.setText(str);
    }

    public void setLabelDescargas(String str) {
        if (str.equalsIgnoreCase("en espera")) {
            labelDescargas.setForeground(Color.orange);
        } else if (str.equals("descargando")) {
            labelDescargas.setForeground(Color.green);
        } else if (str.equalsIgnoreCase("bloqueado")) {
            labelDescargas.setForeground(Color.red);
        } else {
            labelDescargas.setForeground(Color.black);
        }
        labelDescargas.setText(str);
    }

    private void mostrarPanel(String panel) {
        CardLayout cl = (CardLayout) paneles.getLayout();
        cl.show(paneles, panel);
    }

    public void verEdicto() {
        int fila = tablaEdicto.getSelectedRow();
        Edicto edicto = new Edicto(String.valueOf(tablaEdicto.getValueAt(fila, 0)));
        Visual vl = new Visual(edicto);
        vl.setLocationRelativeTo(null);
        vl.setVisible(true);
    }

    public void verEdictos() {
        Edicto edicto;
        List<Edicto> lista = new ArrayList<Edicto>();

        for (int i = 0; i < tablaEdicto.getRowCount(); i++) {
            edicto = new Edicto(String.valueOf(tablaEdicto.getValueAt(i, 0)));
            lista.add(edicto);
        }

        Visual vl = new Visual(lista);
        vl.setLocationRelativeTo(null);
        vl.setVisible(true);
    }

    public void setEspera() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void setDefault() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void ocultar() {
        this.setVisible(false);
    }

    public void mostrar() {
        this.setVisible(true);
    }

    private static void abrirCarpeta(String nombre) {
        try {
            File a = new File(".");
            String ruta = a.getCanonicalPath() + "\\" + nombre;
            Runtime r = Runtime.getRuntime();
            Process p = null;
            p = r.exec("explorer.exe " + ruta);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Panel Origenes">
    //<editor-fold defaultstate="collapsed" desc="Origenes">
    private void iniciaOrigenes() {
        int desc = 0;
        String or = "";

        setEspera();
        List<Origen> list = util.Listados.origenes();
        Iterator it = list.iterator();
        Origen aux;
        DefaultListModel modelo = new DefaultListModel();

        while (it.hasNext()) {
            aux = (Origen) it.next();
            if ("desconocido".equals(aux.getNombre())) {
                desc++;
                or = or + "'" + aux.getIdOrigen() + "'  ";
            }
            modelo.addElement(aux);
        }

        if (desc > 0) {
            infoOrigenes.setText("Hay " + desc + " origen/es por comprobar.");
            infoOrigenesAdd.setText(or);
        } else {
            infoOrigenes.setText("");
            infoOrigenesAdd.setText("");
        }
        origenId.setText("");
        origenNombre.setText("");
        origenCodigo.setText("");
        origenObservaciones.setText("");
        origenArticulo.setText("");
        listaOrigenes.removeAll();
        listaOrigenes.setModel(modelo);
        validarRequerimiento.setVisible(false);
        requerimientoTipoAsociado.setEnabled(false);
        setDefault();
    }

    private void verOrigen() {
        Origen aux = (Origen) listaOrigenes.getSelectedValue();
        origenId.setText(aux.getIdOrigen());
        origenNombre.setText(aux.getNombre());
        origenCodigo.setText(aux.getCodigo());
        origenObservaciones.setText(aux.getObservaciones());
        origenArticulo.setText(aux.getArticulo());
        iniciaRequerimientos();
    }

    private void actualizaOrigen() {
        Origen aux = (Origen) listaOrigenes.getSelectedValue();
        aux.setNombre(origenNombre.getText());
        aux.setCodigo(origenCodigo.getText());
        aux.setObservaciones(origenObservaciones.getText());
        aux.setArticulo(origenArticulo.getText());
        Sql.ejecutar(aux.editaOrigen(), Datagest.con);
        iniciaOrigenes();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="requerimientos">
    private void iniciaRequerimientos() {
        setEspera();
        List<Tipo> list = util.Listados.tipos();
        Tipo aux;
        Iterator it = list.iterator();
        DefaultListModel modelo = new DefaultListModel();

        listaRequerimientos.setModel(modelo);
        requerimientoId.setText("");

        requerimientoTipo.removeAllItems();
        requerimientoTipo.addItem(new Tipo("Sin seleccionar"));
        requerimientoTipoAsociado.removeAllItems();
        requerimientoTipoAsociado.addItem(new Tipo("Sin seleccionar"));
        listaRequerimientos.setEnabled(true);
        requerimientoTipo.setEnabled(true);

        while (it.hasNext()) {
            aux = (Tipo) it.next();
            requerimientoTipo.addItem(aux);
            requerimientoTipoAsociado.addItem(aux);
        }
        setDefault();
    }

    private void verRequerimiento() {
        Requerimiento rq = (Requerimiento) listaRequerimientos.getSelectedValue();
        requerimientoId.setText(rq.getValor());
        requerimientoTipoAsociado.setSelectedItem(new Tipo(rq.getValorAsociado()));
    }

    private void cargaRequerimientos() {
        setEspera();
        Requerimiento reque;
        DefaultListModel modelo = new DefaultListModel();
        Origen aux = (Origen) listaOrigenes.getSelectedValue();
        Tipo tipo = (Tipo) requerimientoTipo.getSelectedItem();
        List<Requerimiento> list = util.Listados.requerimientos(aux, tipo);
        Iterator it = list.iterator();

        while (it.hasNext()) {
            reque = (Requerimiento) it.next();
            modelo.addElement(reque);
        }
        listaRequerimientos.setModel(modelo);
        requerimientoId.setText("");
        requerimientoTipoAsociado.setSelectedIndex(0);
        setDefault();
    }

    private void nuevoRequerimiento() {
        Origen aux = (Origen) listaOrigenes.getSelectedValue();
        Tipo tipo = (Tipo) requerimientoTipo.getSelectedItem();
        if (aux != null) {
            if (tipo != null) {
                req = true;
                validarRequerimiento.setVisible(true);
                requerimientoId.setEditable(true);
                requerimientoId.setText("");
                requerimientoTipoAsociado.setEnabled(true);
                requerimientoTipoAsociado.setSelectedIndex(0);
                requerimientoTipo.setSelectedIndex(0);
                nuevoReq = true;
            } else {
                vista.JOption.error(this, "Debes seleccionar un tipo");
            }
        } else {
            vista.JOption.error(this, "Debes seleccionar un origen");
        }
    }

    private void borraRequerimiento() {
        Requerimiento requer = (Requerimiento) listaRequerimientos.getSelectedValue();
        Sql.ejecutar(requer.borraRequerimiento(), Datagest.con);
        cargaRequerimientos();
    }

    private void editaRequerimiento() {
        Requerimiento requer = (Requerimiento) listaRequerimientos.getSelectedValue();
        if (requer != null) {
            req = true;
            validarRequerimiento.setVisible(true);
            requerimientoId.setEditable(true);
            requerimientoTipoAsociado.setEnabled(true);
            nuevoReq = false;
        } else {
            vista.JOption.error(this, "Debes seleccionar un requerimiento");
        }
    }

    private void validarRequerimiento() {
        Requerimiento requer;
        Tipo tipo, tipoAsociado;
        Origen origen;

        if (nuevoReq) {
            origen = (Origen) listaOrigenes.getSelectedValue();
            tipoAsociado = (Tipo) requerimientoTipoAsociado.getSelectedItem();
            tipo = (Tipo) requerimientoTipo.getSelectedItem();

            if (tipo.equals(new Tipo("Sin seleccionar"))) {
                vista.JOption.error(this, "Debes seleccionar un tipo asociado");
            } else if (tipoAsociado.equals(new Tipo("Sin seleccionar"))) {
                vista.JOption.error(this, "Debes seleccionar un tipo");
            } else {
                requer = new Requerimiento(origen.getIdOrigen(), tipo.getIdTipo(), requerimientoId.getText(), tipoAsociado.getIdTipo());
                Sql.ejecutar(requer.creaRequerimiento(), Datagest.con);
            }
        } else {
            requer = (Requerimiento) listaRequerimientos.getSelectedValue();
            tipo = (Tipo) requerimientoTipo.getSelectedItem();
            tipoAsociado = (Tipo) requerimientoTipoAsociado.getSelectedItem();

            if (tipo.equals(new Tipo("Sin seleccionar"))) {
                vista.JOption.error(this, "Debes seleccionar un tipo");
            } else if (tipoAsociado.equals(new Tipo("Sin seleccionar"))) {
                vista.JOption.error(this, "Debes seleccionar un tipo asociado");
            } else {
                requer.setValor(requerimientoId.getText());
                requer.setValorAsociado(tipoAsociado.getIdTipo());
                Sql.ejecutar(requer.editaRequerimiento(), Datagest.con);
            }
        }

        validarRequerimiento.setVisible(false);
        requerimientoId.setEditable(false);
        requerimientoTipoAsociado.setEnabled(false);
        req = false;
        cargaRequerimientos();
    }
    //</editor-fold>

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Panel Edictos">
    public void cargaEdictos(boolean modo) {
        if (!run) {
            run = true;
            ProcesoEdictos he = new ProcesoEdictos(modo);
            he.start();
        }
    }

    private void iniciaEdictos() {
        setEspera();
        fechaEdicto.setDate(null);
        labelEdictoTotal.setText("0");
        comboEdictoBoletin.removeAllItems();
        comboEdictoBoletin.addItem(new Boletin("Todos"));
        tablaEdicto.setModel(new modeloEdicto());
        setDefault();
    }

    public ParametrosEdicto getParametrosEdicto() {
        ParametrosEdicto pe;
        try {
            pe = new ParametrosEdicto(fechaEdicto.getDate(), (Boletin) comboEdictoBoletin.getSelectedItem(), comboEdictoEstado.getSelectedIndex());
        } catch (ClassCastException e) {
            pe = null;
        }
        return pe;
    }

    public void setModelEdicto(modeloEdicto modelo) {
        tablaEdicto.setModel(modelo);
    }

    public void setComboBoletin(List<Boletin> lista) {
        Iterator it = lista.iterator();
        comboEdictoBoletin.removeAllItems();
        comboEdictoBoletin.addItem(new Boletin("Todos"));

        while (it.hasNext()) {
            comboEdictoBoletin.addItem(it.next());
        }
    }

    private void procesaFases() {
        proceso = new Proceso(this, false);
        try {
            ProcesoFases pf = new ProcesoFases(fechaEdicto.getDate());
            proceso.setVisible(true);
            proceso.setLocationRelativeTo(this);
            pf.start();
        } catch (NullPointerException ex) {
            vista.JOption.error(this, "Debes seleccionar una fecha");
        }
    }

    private void procesaFasesPendientes() {
        proceso = new Proceso(this, false);
        try {
            ProcesoFases pf = new ProcesoFases();
            proceso.setVisible(true);
            proceso.setLocationRelativeTo(this);
            pf.start();
        } catch (NullPointerException ex) {
            vista.JOption.error(this, "Debes seleccionar una fecha");
        }
    }

    private void verWeb(Edicto aux) {
        String url;
        String pre = "https://sedeapl.dgt.gob.es/WEB_TTRA_CONSULTA/ServletVisualizacion?params=";
        String post = "%26subidioma%3Des&formato=PDF";

        aux = util.GetEntidad.edicto(aux);
        Descarga des = new Descarga(aux.getIdDescarga());
        des = util.GetEntidad.descarga(des);

        url = pre + des.getParametros() + post;
        Datagest.abrirWeb(url);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Panel Fases">

    private void iniciaFases() {
        Origen aux;
        Tipo auxTipo;
        setEspera();
        List<Origen> lista = Listados.origenes();
        List<Tipo> listaTipo = Listados.tipos();
        Iterator it = lista.iterator();

        origenFases.setText("");
        listaOrigenFases.removeAllItems();
        listaOrigenFases.addItem(new Origen(" "));

        while (it.hasNext()) {
            aux = (Origen) it.next();
            listaOrigenFases.addItem(aux);
        }

        it = listaTipo.iterator();
        codigoFases.removeAllItems();
        codigoFases.addItem(new Tipo("Sin seleccionar"));

        while (it.hasNext()) {
            auxTipo = (Tipo) it.next();
            codigoFases.addItem(auxTipo);
        }
        validarFases.setVisible(false);
        validarFases1.setVisible(false);
        setDefault();
    }

    private void cargaFasesOrigen() {
        setEspera();
        Fase fase;
        List<Fase> lista;
        Iterator it;
        DefaultListModel modelo = new DefaultListModel();
        try {
            Origen aux = (Origen) listaOrigenFases.getSelectedItem();

            if (!aux.equals(new Origen(" "))) {
                origenFases.setText(aux.getNombre());
                lista = Listados.fases(aux);
                it = lista.iterator();

                while (it.hasNext()) {
                    fase = (Fase) it.next();
                    modelo.addElement(fase);
                }
                listaFases.setModel(modelo);
            } else {
                origenFases.setText("");
                listaFases.setModel(modelo);
            }
        } catch (NullPointerException ex) {
            //
        }
        limpiaFases();
        setDefault();
    }

    private void cargaFase() {
        setEspera();
        try {
            Fase fase = (Fase) listaFases.getSelectedValue();
            codigoFases.setSelectedItem(new Tipo(fase.getCodigo()));
            tipoFases.setSelectedIndex(fase.getTipo());
            diasFases.setText(Integer.toString(fase.getDias()));
            texto1Fases.setText(fase.getTexto1());
            texto2Fases.setText(fase.getTexto2());
            texto3Fases.setText(fase.getTexto3());
        } catch (NullPointerException ex) {
            //
        }
        setDefault();
    }

    private void nuevaFase() {
        Origen origen = (Origen) listaOrigenFases.getSelectedItem();

        if (origen.equals(new Origen(" "))) {
            vista.JOption.error(this, "Debes seleccionar un origen");
        } else {
            activarFases(true);
            limpiaFases();
            fas = true;
            nuevaFas = true;
        }
    }

    private void editaFase() {
        listaOrigenFases.setEnabled(false);
        Fase fase = (Fase) listaFases.getSelectedValue();

        if (fase == null) {
            vista.JOption.error(this, "Debes seleccionar una Fase");
            listaOrigenFases.setEnabled(true);
        } else {
            activarFases(true);
            fas = true;
            nuevaFas = false;
        }
    }

    private void borraFase() {
        Fase fase = (Fase) listaFases.getSelectedValue();

        if (fase == null) {
            vista.JOption.error(this, "Debes seleccionar una Fase");
        } else {
            Sql.ejecutar(fase.borraFase(), Datagest.con);
            cargaFasesOrigen();
        }
    }

    private void validaFase() {
        Fase fase;

        if (nuevaFas) {
            fase = construyeFase();
            if (fase != null) {
                if (Sql.comprobarFase(fase, Datagest.con) < 0) {
                    Sql.ejecutar(fase.creaFase(), Datagest.con);
                } else {
                    vista.JOption.sql(this, "Ya existe una Fase con esos parámetros");
                }
            }
        } else {
            fase = (Fase) listaFases.getSelectedValue();
            if (fase == null) {
                vista.JOption.error(this, "Debes seleccionar una Fase");
            } else {
                Fase aux = construyeFase();
                if (aux != null) {
                    fase.setCodigo(aux.getCodigo());
                    fase.setTipo(aux.getTipo());
                    fase.setDias(aux.getDias());
                    fase.setTexto1(aux.getTexto1());
                    fase.setTexto2(aux.getTexto2());
                    fase.setTexto3(aux.getTexto3());
                    if (Sql.comprobarFase(fase, Datagest.con) < 0) {
                        Sql.ejecutar(fase.editaFase(), Datagest.con);
                    } else {
                        vista.JOption.sql(this, "Ya existe una Fase con esos parámetros");
                    }
                }
            }
        }
        activarFases(false);
        fas = false;
        nuevaFas = false;
        listaOrigenFases.setEnabled(true);
        cargaFasesOrigen();
    }

    private Fase construyeFase() {
        boolean valido = true;
        Fase fase = new Fase();

        Origen origen = (Origen) listaOrigenFases.getSelectedItem();
        fase.setOrigen(origen.getIdOrigen());
        Tipo tipo = (Tipo) codigoFases.getSelectedItem();

        if (tipo.equals(new Tipo("Sin selecionar"))) {
            vista.JOption.error(this, "Debes seleccionar un código");
            valido = false;
        } else {
            fase.setCodigo(tipo.getIdTipo());

            if (tipoFases.getSelectedIndex() == 0) {
                vista.JOption.error(this, "Debes seleccionar un tipo");
                valido = false;
            } else {
                fase.setTipo(tipoFases.getSelectedIndex());
                try {
                    int dias = Integer.parseInt(diasFases.getText());
                    fase.setDias(dias);
                } catch (NumberFormatException e) {
                    vista.JOption.error(this, "Los días deben ser un número");
                    valido = false;
                }

                String texto = texto1Fases.getText();
                if (texto.length() > 100) {
                    vista.JOption.error(this, "El Texto 1 es demasiado largo");
                    valido = false;
                } else {
                    fase.setTexto1(texto);
                }

                texto = texto2Fases.getText();
                if (texto.length() > 100) {
                    vista.JOption.error(this, "El Texto 2 es demasiado largo");
                    valido = false;
                } else {
                    fase.setTexto2(texto);
                }

                texto = texto3Fases.getText();
                if (texto.length() > 100) {
                    vista.JOption.error(this, "El Texto 3 es demasiado largo");
                    valido = false;
                } else {
                    fase.setTexto3(texto);
                }
            }
        }
        if (!valido) {
            fase = null;
        }
        return fase;
    }

    private void activarFases(boolean a) {
        validarFases.setVisible(a);
        validarFases1.setVisible(a);
        codigoFases.setEnabled(a);
        tipoFases.setEnabled(a);
        diasFases.setEditable(a);
        texto1Fases.setEditable(a);
        texto2Fases.setEditable(a);
        texto3Fases.setEditable(a);
        borraFase.setVisible(!a);
        editaFase.setVisible(!a);
        nuevaFase.setVisible(!a);
        fas = !a;
    }

    private void limpiaFases() {
        codigoFases.setSelectedIndex(0);
        tipoFases.setSelectedIndex(0);
        diasFases.setText("");
        texto1Fases.setText("");
        texto2Fases.setText("");
        texto3Fases.setText("");
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        paneles = new javax.swing.JPanel();
        panelInicio = new javax.swing.JLayeredPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        labelEdictos = new javax.swing.JLabel();
        labelOrigenes = new javax.swing.JLabel();
        labelFases = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        labelFecha = new javax.swing.JLabel();
        notificacion = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        labelDescargas = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        labelInicioEstadoCon = new javax.swing.JLabel();
        labelInicioNombreCon = new javax.swing.JLabel();
        infoBase = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        comboWeb = new javax.swing.JComboBox();
        jButton9 = new javax.swing.JButton();
        panelEdictos = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        botonVer = new javax.swing.JButton();
        botonTodos = new javax.swing.JButton();
        labelEdictoTotal = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        botonTodos1 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        fechaEdicto = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        comboEdictoBoletin = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        comboEdictoEstado = new javax.swing.JComboBox();
        botonActualizarEdicto = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaEdicto = new javax.swing.JTable();
        panelOrigenes = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaOrigenes = new javax.swing.JList();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        origenId = new javax.swing.JTextField();
        origenNombre = new javax.swing.JTextField();
        origenCodigo = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        origenObservaciones = new javax.swing.JTextArea();
        jButton14 = new javax.swing.JButton();
        infoOrigenes = new javax.swing.JLabel();
        infoOrigenesAdd = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        origenArticulo = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaRequerimientos = new javax.swing.JList();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        requerimientoId = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        requerimientoTipoAsociado = new javax.swing.JComboBox();
        validarRequerimiento = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        requerimientoTipo = new javax.swing.JComboBox();
        panelFases = new javax.swing.JPanel();
        listaOrigenFases = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        listaFases = new javax.swing.JList();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        texto3Fases = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        texto2Fases = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        texto1Fases = new javax.swing.JTextArea();
        origenFases = new javax.swing.JTextField();
        codigoFases = new javax.swing.JComboBox();
        tipoFases = new javax.swing.JComboBox();
        diasFases = new javax.swing.JTextField();
        editaFase = new javax.swing.JButton();
        nuevaFase = new javax.swing.JButton();
        borraFase = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        validarFases = new javax.swing.JButton();
        validarFases1 = new javax.swing.JButton();
        panelMultas = new javax.swing.JPanel();
        panelProcesado = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jToolBar1 = new javax.swing.JToolBar();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        panelVistaEdictos = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        vistaOrigen = new javax.swing.JComboBox();
        jScrollPane10 = new javax.swing.JScrollPane();
        vistaTabla = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        vistaEmisor = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Datagest 2.1");
        setIconImages(getIconImages());
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setResizable(false);

        paneles.setLayout(new java.awt.CardLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tareas pendientes"));

        jLabel8.setText("Edictos por procesar:");

        jLabel9.setText("Origenes por comprobar:");

        jLabel10.setText("Fases por comprobar:");

        labelEdictos.setText("0");

        labelOrigenes.setText("0");

        labelFases.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelOrigenes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelEdictos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(197, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(labelEdictos)
                        .addGap(11, 11, 11)
                        .addComponent(labelOrigenes)
                        .addGap(11, 11, 11)
                        .addComponent(labelFases))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel9)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel10)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel3.setBounds(380, 10, 351, 110);
        panelInicio.add(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Testra"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/edictos32.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 31, -1, -1));

        jLabel1.setText("Vista de Edictos");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 31, 238, 41));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/multas32.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 78, -1, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado32.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 125, -1, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/capturador32.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 177, -1, -1));

        jLabel3.setText("Vista de Multas");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 78, 238, 41));

        jLabel2.setText("Procesado de Multas");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 125, 238, 41));

        jLabel4.setText("Iniciar Capturador");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 177, 238, 41));

        jPanel1.setBounds(20, 10, 340, 240);
        panelInicio.add(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Historico"));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/buscar32.png"))); // NOI18N

        jLabel5.setText("Búsqueda");

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/historico32.png"))); // NOI18N

        jLabel6.setText("Introducir registros en el Historico");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBounds(20, 260, 339, 140);
        panelInicio.add(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelFecha.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelFecha.setText("Fecha: ");

        notificacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/gifAlerta.gif"))); // NOI18N
        notificacion.setDoubleBuffered(true);
        notificacion.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 568, Short.MAX_VALUE)
                .addComponent(notificacion))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(notificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBounds(20, 410, 970, 40);
        panelInicio.add(jPanel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Herramientas"));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/configuracion32.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/origenes32.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel7.setText("Configuración");

        jLabel11.setText("Vista de Origenes y Requerimientos");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/fases32.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel13.setText("Vista de Fases");

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/validador32.png"))); // NOI18N
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel38.setText("Inicia Validador");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );

        jPanel5.setBounds(380, 150, 350, 250);
        panelInicio.add(jPanel5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Descarga"));

        jLabel14.setText("Estado del módulo de descarga:");

        labelDescargas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelDescargas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDescargas.setText("no comprobado");

        jButton11.setText("...");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(labelDescargas, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jButton11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDescargas, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBounds(750, 90, 240, 160);
        panelInicio.add(jPanel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Conexión"));

        labelInicioEstadoCon.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelInicioEstadoCon.setForeground(new java.awt.Color(255, 0, 0));
        labelInicioEstadoCon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelInicioEstadoCon.setText("NO CONECTADO");

        labelInicioNombreCon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelInicioNombreCon.setText("...");

        infoBase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoBase.setText("...");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelInicioEstadoCon, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(labelInicioNombreCon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(infoBase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(labelInicioEstadoCon, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInicioNombreCon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(infoBase)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBounds(750, 280, 240, 120);
        panelInicio.add(jPanel7, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Abrir Web"));

        comboWeb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/ir16.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboWeb, 0, 175, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(comboWeb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addContainerGap())
        );

        jPanel14Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {comboWeb, jButton9});

        jPanel14.setBounds(750, 10, 240, 60);
        panelInicio.add(jPanel14, javax.swing.JLayeredPane.DEFAULT_LAYER);

        paneles.add(panelInicio, "panelInicio");

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        botonVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/buscar32.png"))); // NOI18N
        botonVer.setText("Ver");
        botonVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerActionPerformed(evt);
            }
        });

        botonTodos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/archivos32.png"))); // NOI18N
        botonTodos.setText("Archivos");
        botonTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTodosActionPerformed(evt);
            }
        });

        labelEdictoTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelEdictoTotal.setText("0");

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Total Registros:");

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/open32.png"))); // NOI18N
        jButton18.setText("Abrir carpeta");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        botonTodos1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado32.png"))); // NOI18N
        botonTodos1.setText("Fases");
        botonTodos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTodos1ActionPerformed(evt);
            }
        });

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/browser.png"))); // NOI18N
        jButton20.setText("Ver Web");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(botonVer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelEdictoTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(botonTodos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton18)
            .addComponent(botonTodos1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel10Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton18, jButton20});

        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(botonVer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonTodos1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonTodos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelEdictoTotal)
                .addContainerGap())
        );

        jPanel10Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {botonTodos, jButton18, jButton20});

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel22.setText("Fecha:");

        fechaEdicto.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaEdictoPropertyChange(evt);
            }
        });

        jLabel23.setText("Boletín: ");

        comboEdictoBoletin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos" }));
        comboEdictoBoletin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEdictoBoletinActionPerformed(evt);
            }
        });

        jLabel24.setText("Estado: ");

        comboEdictoEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Sin descargar", "Descargado", "Procesado", "Bloqueado" }));
        comboEdictoEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEdictoEstadoActionPerformed(evt);
            }
        });

        botonActualizarEdicto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/refrescar32.png"))); // NOI18N
        botonActualizarEdicto.setToolTipText("Actualizar");
        botonActualizarEdicto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizarEdictoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaEdicto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboEdictoBoletin, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboEdictoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                .addComponent(botonActualizarEdicto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fechaEdicto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboEdictoBoletin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(comboEdictoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonActualizarEdicto))
                .addContainerGap())
        );

        tablaEdicto.setModel(new modeloEdicto());
        jScrollPane2.setViewportView(tablaEdicto);

        javax.swing.GroupLayout panelEdictosLayout = new javax.swing.GroupLayout(panelEdictos);
        panelEdictos.setLayout(panelEdictosLayout);
        panelEdictosLayout.setHorizontalGroup(
            panelEdictosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEdictosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEdictosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelEdictosLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelEdictosLayout.setVerticalGroup(
            panelEdictosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEdictosLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEdictosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        paneles.add(panelEdictos, "panelEdictos");

        panelOrigenes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Origenes"));

        listaOrigenes.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listaOrigenes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaOrigenesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaOrigenes);

        jLabel20.setText("idOrigen:");

        jLabel21.setText("Nombre:");

        jLabel25.setText("Código:");

        jLabel27.setText("Observaciones:");

        origenId.setEditable(false);
        origenId.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        origenNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        origenCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        origenObservaciones.setColumns(20);
        origenObservaciones.setLineWrap(true);
        origenObservaciones.setRows(5);
        jScrollPane3.setViewportView(origenObservaciones);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/aceptar32.png"))); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        infoOrigenes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoOrigenes.setText("Info");

        infoOrigenesAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoOrigenesAdd.setText("Info add");

        jLabel37.setText("Artículo:");

        origenArticulo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(origenId)
                    .addComponent(origenNombre)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton14))
                    .addComponent(infoOrigenes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoOrigenesAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(origenArticulo)
                    .addComponent(origenCodigo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel25)
                            .addComponent(jLabel37)
                            .addComponent(jLabel27))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(9, 9, 9)
                        .addComponent(origenId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21)
                        .addGap(4, 4, 4)
                        .addComponent(origenNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(origenCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(origenArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(infoOrigenes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoOrigenesAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        panelOrigenes.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, 450));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Requerimientos"));

        listaRequerimientos.setEnabled(false);
        listaRequerimientos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaRequerimientosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(listaRequerimientos);

        jLabel28.setText("Valor del requerimiento:");

        jLabel29.setText("Tipo asociado:");

        requerimientoId.setEditable(false);
        requerimientoId.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/add32.png"))); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/borrar32.png"))); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/edit32.png"))); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        validarRequerimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/aceptar32.png"))); // NOI18N
        validarRequerimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validarRequerimientoActionPerformed(evt);
            }
        });

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));

        requerimientoTipo.setEnabled(false);
        requerimientoTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requerimientoTipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(requerimientoTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(requerimientoTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel29))
                                .addGap(0, 282, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(requerimientoTipoAsociado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(requerimientoId)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addComponent(jButton16)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton17)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton15))
                                            .addComponent(validarRequerimiento))))
                                .addContainerGap())))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(requerimientoId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(requerimientoTipoAsociado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(validarRequerimiento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton16, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton17, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );

        panelOrigenes.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 510, 450));

        paneles.add(panelOrigenes, "panelOrigenes");

        listaOrigenFases.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        listaOrigenFases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaOrigenFasesActionPerformed(evt);
            }
        });

        listaFases.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listaFases.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaFasesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(listaFases);

        jLabel30.setText("Origen:");

        jLabel31.setText("Código:");

        jLabel32.setText("Tipo:");

        jLabel33.setText("Texto 1:");

        jLabel34.setText("Texto 2:");

        jLabel35.setText("Texto 3:");

        jLabel36.setText("Días:");

        texto3Fases.setColumns(20);
        texto3Fases.setEditable(false);
        texto3Fases.setLineWrap(true);
        texto3Fases.setRows(5);
        jScrollPane6.setViewportView(texto3Fases);

        texto2Fases.setColumns(20);
        texto2Fases.setEditable(false);
        texto2Fases.setLineWrap(true);
        texto2Fases.setRows(5);
        jScrollPane7.setViewportView(texto2Fases);

        texto1Fases.setColumns(20);
        texto1Fases.setEditable(false);
        texto1Fases.setLineWrap(true);
        texto1Fases.setRows(5);
        jScrollPane8.setViewportView(texto1Fases);

        origenFases.setEditable(false);
        origenFases.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        codigoFases.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        codigoFases.setEnabled(false);

        tipoFases.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sin seleccionar", "ND", "RS", "RR" }));
        tipoFases.setEnabled(false);

        diasFases.setEditable(false);

        editaFase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/edit32.png"))); // NOI18N
        editaFase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editaFaseActionPerformed(evt);
            }
        });

        nuevaFase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/add32.png"))); // NOI18N
        nuevaFase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaFaseActionPerformed(evt);
            }
        });

        borraFase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/borrar32.png"))); // NOI18N
        borraFase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borraFaseActionPerformed(evt);
            }
        });

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/buscar32.png"))); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        validarFases.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/aceptar32.png"))); // NOI18N
        validarFases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validarFasesActionPerformed(evt);
            }
        });

        validarFases1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/cancel32.png"))); // NOI18N
        validarFases1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validarFases1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFasesLayout = new javax.swing.GroupLayout(panelFases);
        panelFases.setLayout(panelFasesLayout);
        panelFasesLayout.setHorizontalGroup(
            panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFasesLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(listaOrigenFases, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFasesLayout.createSequentialGroup()
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(codigoFases, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(tipoFases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(diasFases, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFasesLayout.createSequentialGroup()
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFasesLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(origenFases, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel30)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel33)
                                .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel35)
                                    .addComponent(jScrollPane8)
                                    .addComponent(jScrollPane7)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(10, 10, 10)
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(borraFase)
                            .addComponent(editaFase)
                            .addComponent(jButton21)
                            .addComponent(nuevaFase)
                            .addGroup(panelFasesLayout.createSequentialGroup()
                                .addComponent(validarFases)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(validarFases1)))))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        panelFasesLayout.setVerticalGroup(
            panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFasesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listaOrigenFases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(origenFases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFasesLayout.createSequentialGroup()
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(jLabel32)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(codigoFases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tipoFases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(diasFases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFasesLayout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(panelFasesLayout.createSequentialGroup()
                                .addComponent(jButton21)
                                .addGap(81, 81, 81)))
                        .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(validarFases1)
                            .addGroup(panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(panelFasesLayout.createSequentialGroup()
                                    .addComponent(validarFases)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(borraFase)
                                    .addGap(18, 18, 18)
                                    .addComponent(editaFase)
                                    .addGap(18, 18, 18)
                                    .addComponent(nuevaFase))
                                .addGroup(panelFasesLayout.createSequentialGroup()
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel35)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        paneles.add(panelFases, "panelFases");

        javax.swing.GroupLayout panelMultasLayout = new javax.swing.GroupLayout(panelMultas);
        panelMultas.setLayout(panelMultasLayout);
        panelMultasLayout.setHorizontalGroup(
            panelMultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1016, Short.MAX_VALUE)
        );
        panelMultasLayout.setVerticalGroup(
            panelMultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
        );

        paneles.add(panelMultas, "panelMultas");

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane9.setViewportView(jTextPane1);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton12.setText("Subir Lineas");
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton12);

        jButton13.setText("jButton13");
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton13);

        javax.swing.GroupLayout panelProcesadoLayout = new javax.swing.GroupLayout(panelProcesado);
        panelProcesado.setLayout(panelProcesadoLayout);
        panelProcesadoLayout.setHorizontalGroup(
            panelProcesadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProcesadoLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProcesadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)))
        );
        panelProcesadoLayout.setVerticalGroup(
            panelProcesadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelProcesadoLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        paneles.add(panelProcesado, "panelProcesado");

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setText("Emisor: ");

        vistaOrigen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        vistaTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "idEdicto", "Fecha"
            }
        ));
        jScrollPane10.setViewportView(vistaTabla);

        jLabel15.setText("Origen:");

        vistaEmisor.setEditable(false);
        vistaEmisor.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(vistaOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(vistaEmisor))))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vistaOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(9, 9, 9)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(vistaEmisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel16.setText("idEdicto:");

        jLabel17.setText("Origen:");

        jLabel18.setText("jLabel18");

        jLabel19.setText("jLabel19");

        jLabel39.setText("jLabel39");

        jLabel40.setText("jLabel40");

        jLabel41.setText("jLabel41");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41))
                .addContainerGap(664, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel41)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelVistaEdictosLayout = new javax.swing.GroupLayout(panelVistaEdictos);
        panelVistaEdictos.setLayout(panelVistaEdictosLayout);
        panelVistaEdictosLayout.setHorizontalGroup(
            panelVistaEdictosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVistaEdictosLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelVistaEdictosLayout.setVerticalGroup(
            panelVistaEdictosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        paneles.add(panelVistaEdictos, "panelVistaEdictos");

        jMenu1.setText("Datagest");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/inicio16.png"))); // NOI18N
        jMenuItem1.setText("Inicio");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/testra16.png"))); // NOI18N
        jMenu3.setText("TESTRA");

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/edictos16.png"))); // NOI18N
        jMenuItem6.setText("Edictos");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/multas16.png"))); // NOI18N
        jMenuItem7.setText("Multas");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado16.png"))); // NOI18N
        jMenuItem14.setText("Procesado de Multas");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem14);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/origenes16.png"))); // NOI18N
        jMenuItem8.setText("Origenes y Requerimientos");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/fases16.png"))); // NOI18N
        jMenuItem10.setText("Fases");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado16.png"))); // NOI18N
        jMenu5.setText("Tareas");

        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado16.png"))); // NOI18N
        jMenuItem16.setText("Procesar Multas pendientes");
        jMenu5.add(jMenuItem16);

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado16.png"))); // NOI18N
        jMenuItem12.setText("Procesar Edictos pendientes");
        jMenu5.add(jMenuItem12);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado16.png"))); // NOI18N
        jMenuItem9.setText("Procesar todas las fases pendientes");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem9);

        jMenu3.add(jMenu5);

        jMenu1.add(jMenu3);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/menuHistorico16.png"))); // NOI18N
        jMenu4.setText("HISTORICO");

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/buscar16.png"))); // NOI18N
        jMenuItem5.setText("Búsqueda");
        jMenu4.add(jMenuItem5);

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/historico16.png"))); // NOI18N
        jMenuItem11.setText("Introduce BBDD");
        jMenu4.add(jMenuItem11);

        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado16.png"))); // NOI18N
        jMenu7.setText("Tareas");

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/procesado16.png"))); // NOI18N
        jMenuItem13.setText("Introducir registros pendientes");
        jMenu7.add(jMenuItem13);

        jMenu4.add(jMenu7);

        jMenu1.add(jMenu4);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/cerrar16.png"))); // NOI18N
        jMenuItem2.setText("Salir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Herramientas");

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/capturador16.png"))); // NOI18N
        jMenuItem3.setText("Capturador");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/validador16.png"))); // NOI18N
        jMenuItem4.setText("Validador");
        jMenu2.add(jMenuItem4);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico/configuracion16.png"))); // NOI18N
        jMenuItem15.setText("Configuración");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem15);

        jMenuBar1.add(jMenu2);

        jMenu6.setText("Acerca de...");
        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
//INICIAR CONFIGURACIÓN
        Configuracion conf = new Configuracion(this, true);
        conf.setLocationRelativeTo(null);
        conf.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
//CERRAR APLICACIÓN
        ControlDatos cd = new ControlDatos();
        cd.guardaDatos();
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
//INICIAR CONFIGURACION (BOTON)    
        Configuracion conf = new Configuracion(this, true);
        conf.setLocationRelativeTo(null);
        conf.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
//PANEL INICIO
        mostrarPanel("panelInicio");
        inicio();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//PANEL EDICTOS (Botón)
        mostrarPanel("panelEdictos");
        iniciaEdictos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//PANEL MULTAS (Botón)
        mostrarPanel("panelMultas");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//PANEL PROCESADO (Botón)
        mostrarPanel("panelProcesado");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
//PANEL EDICTOS 
        mostrarPanel("panelEdictos");
        iniciaEdictos();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void botonActualizarEdictoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizarEdictoActionPerformed
//ACTUALIZAR EDICTOS
        cargaEdictos(true);
    }//GEN-LAST:event_botonActualizarEdictoActionPerformed

    private void fechaEdictoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaEdictoPropertyChange
//FECHA EDICTOS
        cargaEdictos(true);
    }//GEN-LAST:event_fechaEdictoPropertyChange

    private void comboEdictoEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEdictoEstadoActionPerformed
//COMBO ESTADO EDICTOS
        cargaEdictos(false);
    }//GEN-LAST:event_comboEdictoEstadoActionPerformed

    private void comboEdictoBoletinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEdictoBoletinActionPerformed
//COMBO BOLETIN EDICTOS
        cargaEdictos(false);
    }//GEN-LAST:event_comboEdictoBoletinActionPerformed

    private void botonVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerActionPerformed
//VER EDICTO INDIVIDUAL
        if (tablaEdicto.getSelectedRow() != -1) {
            setEspera();
            verEdicto();
            setDefault();
        } else {
            vista.JOption.error(this, "Debes seleccionar un edicto");
        }

    }//GEN-LAST:event_botonVerActionPerformed

    private void botonTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTodosActionPerformed
//VER EDICTOS
        proceso = new Proceso(this, false);
        try {
            ProcesoArchivos ha = new ProcesoArchivos(fechaEdicto.getDate());
            ha.start();
            proceso.setVisible(true);
            proceso.setLocationRelativeTo(this);
        } catch (NullPointerException e) {
            vista.JOption.error(this, "Debes seleccionar una fecha");
        }
    }//GEN-LAST:event_botonTodosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
//INICIA CAPTURADOR
        Datagest.iniciaCapturador();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
//INICIA ORIGENES
        mostrarPanel("panelOrigenes");
        iniciaOrigenes();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void listaOrigenesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaOrigenesMouseClicked
//LISTA ORIGENES
        verOrigen();
    }//GEN-LAST:event_listaOrigenesMouseClicked

    private void listaRequerimientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaRequerimientosMouseClicked
//LISTA REQUERIMIENTOS
        try {
            if (!req) {
                verRequerimiento();
            }
        } catch (NullPointerException e) {
            //
        }
    }//GEN-LAST:event_listaRequerimientosMouseClicked

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
//EDITA REQUERIMIENTO
        editaRequerimiento();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
//BORRA REQUERIMIENTO
        borraRequerimiento();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
//NUEVO REQUERIMIENTO
        nuevoRequerimiento();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void validarRequerimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validarRequerimientoActionPerformed
//VALIDAR REQUERIMIENTO
        validarRequerimiento();
    }//GEN-LAST:event_validarRequerimientoActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
//ACTUALIZA ORIGEN
        actualizaOrigen();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
//INICIAR ORIGENES MENÚ
        mostrarPanel("panelOrigenes");
        iniciaOrigenes();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
//INICIAR FASES MENÚ
        mostrarPanel("panelFases");
        iniciaFases();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
//INICIAR FASES
        mostrarPanel("panelFases");
        iniciaFases();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void listaOrigenFasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaOrigenFasesActionPerformed
//CARGA FASES ORIGEN
        cargaFasesOrigen();
    }//GEN-LAST:event_listaOrigenFasesActionPerformed

    private void nuevaFaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaFaseActionPerformed
//NUEVA FASE
        nuevaFase();
    }//GEN-LAST:event_nuevaFaseActionPerformed

    private void editaFaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editaFaseActionPerformed
//EDITA FASE
        editaFase();
    }//GEN-LAST:event_editaFaseActionPerformed

    private void borraFaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borraFaseActionPerformed
//BORRA FASE
        borraFase();
    }//GEN-LAST:event_borraFaseActionPerformed

    private void validarFasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validarFasesActionPerformed
//VALIDA FASE
        validaFase();
    }//GEN-LAST:event_validarFasesActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
//VER FASES
    }//GEN-LAST:event_jButton21ActionPerformed

    private void listaFasesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaFasesMouseClicked
//CARGA FASE
        if (!fas) {
            cargaFase();
        }
    }//GEN-LAST:event_listaFasesMouseClicked

    private void validarFases1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validarFases1ActionPerformed
//CANCELA EDICION FASES
        limpiaFases();
        activarFases(false);
        listaOrigenFases.setEnabled(true);
        fas = false;
        nuevaFas = false;
    }//GEN-LAST:event_validarFases1ActionPerformed

    private void requerimientoTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requerimientoTipoActionPerformed
//CARGA REQUERIMIENTOS
        Tipo tipo = (Tipo) requerimientoTipo.getSelectedItem();
        if (tipo != null) {
            if (!tipo.equals(new Tipo("Sin seleccionar"))) {
                cargaRequerimientos();
            }
        }
    }//GEN-LAST:event_requerimientoTipoActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
//ABRIR CARPETA EDICTOS
        abrirCarpeta("edictos");
    }//GEN-LAST:event_jButton18ActionPerformed

    private void botonTodos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTodos1ActionPerformed
//BOTON FASES EDICTOS
        procesaFases();
    }//GEN-LAST:event_botonTodos1ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
       
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
//BOTON TAREA TODAS FASES
        procesaFasesPendientes();
        inicio();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
//COMPROBAR MÓDULO DESCARGA
        if (!ProcesoComprobarDescargas.runProcesoDescarga) {
            procesoDescarga();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
//VER WEB
        Url url = (Url) comboWeb.getSelectedItem();
        Datagest.abrirWeb(url.getUrl());
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
//VER MULTAS (MENU)
        this.mostrarPanel("panelMultas");
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
//PROCESADO DE MULTAS (MENU)
        this.mostrarPanel("panelProcesado");
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
//VER WEB
        int posicion = tablaEdicto.getSelectedRow();
        Edicto aux = new Edicto((String) tablaEdicto.getValueAt(posicion, 0));
        verWeb(aux);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
//INICIA CAPTURADOR (MENU)        
        Datagest.iniciaCapturador();
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton borraFase;
    private javax.swing.JToggleButton botonActualizarEdicto;
    private javax.swing.JButton botonTodos;
    private javax.swing.JButton botonTodos1;
    private javax.swing.JButton botonVer;
    private javax.swing.JComboBox codigoFases;
    private javax.swing.JComboBox comboEdictoBoletin;
    private javax.swing.JComboBox comboEdictoEstado;
    private javax.swing.JComboBox comboWeb;
    private javax.swing.JTextField diasFases;
    private javax.swing.JButton editaFase;
    public com.toedter.calendar.JDateChooser fechaEdicto;
    private javax.swing.JLabel infoBase;
    private javax.swing.JLabel infoOrigenes;
    private javax.swing.JLabel infoOrigenesAdd;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelDescargas;
    public javax.swing.JLabel labelEdictoTotal;
    private javax.swing.JLabel labelEdictos;
    private javax.swing.JLabel labelFases;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelInicioEstadoCon;
    private javax.swing.JLabel labelInicioNombreCon;
    private javax.swing.JLabel labelOrigenes;
    private javax.swing.JList listaFases;
    private javax.swing.JComboBox listaOrigenFases;
    private javax.swing.JList listaOrigenes;
    private javax.swing.JList listaRequerimientos;
    public javax.swing.JButton notificacion;
    private javax.swing.JButton nuevaFase;
    private javax.swing.JTextField origenArticulo;
    private javax.swing.JTextField origenCodigo;
    private javax.swing.JTextField origenFases;
    private javax.swing.JTextField origenId;
    private javax.swing.JTextField origenNombre;
    private javax.swing.JTextArea origenObservaciones;
    private javax.swing.JPanel panelEdictos;
    private javax.swing.JPanel panelFases;
    private javax.swing.JLayeredPane panelInicio;
    private javax.swing.JPanel panelMultas;
    private javax.swing.JPanel panelOrigenes;
    private javax.swing.JPanel panelProcesado;
    private javax.swing.JPanel panelVistaEdictos;
    private javax.swing.JPanel paneles;
    private javax.swing.JTextField requerimientoId;
    private javax.swing.JComboBox requerimientoTipo;
    private javax.swing.JComboBox requerimientoTipoAsociado;
    private javax.swing.JTable tablaEdicto;
    private javax.swing.JTextArea texto1Fases;
    private javax.swing.JTextArea texto2Fases;
    private javax.swing.JTextArea texto3Fases;
    private javax.swing.JComboBox tipoFases;
    private javax.swing.JButton validarFases;
    private javax.swing.JButton validarFases1;
    private javax.swing.JButton validarRequerimiento;
    private javax.swing.JTextField vistaEmisor;
    private javax.swing.JComboBox vistaOrigen;
    private javax.swing.JTable vistaTabla;
    // End of variables declaration//GEN-END:variables
}
