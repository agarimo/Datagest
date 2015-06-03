package main.capturador;

import entidades.Descarga;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;
import main.Sql;
import util.Archivo;

/**
 *
 * @author Agárimo
 */
public final class Capturador {

    private CapturadorGui cg;
    private String sesion;
    private List<Descarga> listaCapturados = new ArrayList();
    private List<Descarga> listTotal = new ArrayList();
    private StringBuilder listaString = new StringBuilder();
    private Date fecha;
    private File archivo;
    private File archivoExterno;
    private boolean remoto;
    private boolean init = false;

    public Capturador() {
    }

    public Capturador(File archivo) {
        this.archivoExterno = archivo;
        //TODO introduce el archivo en la BBDD.
    }

    public void iniciaGui() {
        cg = new CapturadorGui();
        cg.setVisible(true);
        cg.setLocationRelativeTo(Datagest.ventana);
    }

    public void iniciaCapturador() {
        creaCarpeta();
        cargaListDescargas();
        initClipboard();
        cg.rePaintLabels();
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public int getListaCapturados() {
        return listaCapturados.size();
    }

    public int getListTotal() {
        return listTotal.size();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
        this.sesion = Datagest.imprimeFecha(fecha);
    }

    public File getArchivoExterno() {
        return archivoExterno;
    }

    public void setArchivoExterno(File archivoExterno) {
        this.archivoExterno = archivoExterno;
    }

    public boolean isRemoto() {
        return remoto;
    }

    public void setRemoto(boolean remoto) {
        this.remoto = remoto;
    }

    public int getTotalCsv() {
        int total = -1;
        String query = "SELECT idDescarga from datagest.descarga where fecha=" + Datagest.entrecomillar(Datagest.imprimeFecha(fecha)) +" group by csv;";

        try {
            Sql bd = new Sql(Datagest.con);
            total = bd.totalRegistros(query);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Capturador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return total;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ClipBoard">
    private void initClipboard() {
        final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        processClipboard(cb);
        cb.addFlavorListener(new FlavorListener() {
            @Override
            public void flavorsChanged(FlavorEvent e) {
                processClipboard(cb);
            }
        });
    }

    public void restartClipboard() {
        initClipboard();
    }

    public void processClipboard(Clipboard cb) {
        String cabecera = "/WEB_TTRA_CONSULTA/VisualizacionEdicto.faces";
        Transferable trans;
        trans = cb.getContents(null);
        if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String s = (String) trans.getTransferData(DataFlavor.stringFlavor);

                if (s.contains(cabecera)) {
                    creaDescarga(s);
                } else {
                    System.out.println("Link no válido");
                }

                StringSelection ss = new StringSelection(s);
                cb.setContents(ss, ss);
            } catch (UnsupportedFlavorException e2) {
                initClipboard();
            } catch (IOException e2) {
                initClipboard();
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Gestion descarga">
    private void cargaListDescargas() {
        String query = "SELECT idDescarga,parametros,fecha FROM datagest.descarga where fecha=" + Datagest.entrecomillar(Datagest.imprimeFecha(fecha));
        try {
            Sql bd = new Sql(Datagest.con);
            listTotal = bd.listaDescarga(query);
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(Capturador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void creaDescarga(String str) {
        Descarga aux = new Descarga(str, getFecha());

        if (!buscaDescarga(aux) && init) {
            listaCapturados.add(aux);
            cg.rePaintLabel();
        } else {
            init = true;
        }
    }

    private boolean buscaDescarga(Descarga aux) {
        boolean is = false;

        if (buscaCapturados(aux) || buscaTotal(aux)) {
            is = true;
        }

        return is;
    }

    private boolean buscaCapturados(Descarga aux) {
        boolean is = false;
        Iterator it = listaCapturados.iterator();
        while (it.hasNext()) {
            if (aux.equals((Descarga) it.next())) {
                is = true;
            }
        }
        return is;
    }

    private boolean buscaTotal(Descarga aux) {
        boolean is = false;
        Iterator it = listTotal.iterator();
        while (it.hasNext()) {
            if (aux.equals((Descarga) it.next())) {
                is = true;
            }
        }
        return is;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Insercion en BBDD y generación de registro">
    public void procesarList() throws SQLException {
        Sql bd;
        Descarga aux;
        Iterator it = listaCapturados.iterator();
        bd = new Sql(Datagest.con);

        while (it.hasNext()) {
            aux = (Descarga) it.next();
            if (bd.buscar(aux.buscaDescarga()) < 0) {
                bd.ejecutar(aux.creaDescarga());
                listTotal.add(aux);
            }
            listaString.append(Datagest.imprimeFecha(aux.getFecha()));
            listaString.append("||");
            listaString.append(aux.getParametros());
            listaString.append("\r\n");
        }
        listaCapturados.clear();
        escribeArchivo();
    }

    private void escribeArchivo() {
        String datos;
        creaArchivo();
        datos = listaString.toString();
        Archivo.anexaArchivo(archivo, datos);
    }

    private void creaCarpeta() {
        File file = new File("rutas");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void creaArchivo() {
        archivo = new File("rutas\\" + sesion + ".cap");

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Capturador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //</editor-fold>
}
