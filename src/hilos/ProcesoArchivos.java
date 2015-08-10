package hilos;

import entidades.Boletin;
import entidades.Edicto;
import entidades.Origen;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;
import main.Sql;
import util.Archivo;
import vista.Main;

/**
 *
 * @author Agárimo
 */
public class ProcesoArchivos extends Thread {

    List<Boletin> listaBoletin;
    File directorio;
    Date fecha;
    Edicto edicto;
    Boletin boletin;
    Origen origen;
    private int indice = 1;
    private int total;

    public ProcesoArchivos(Date fecha) {
        this.fecha = fecha;
        File file = new File("edictos\\" + Datagest.imprimeFecha(fecha));
        if (!file.exists()) {
            file.mkdirs();
        }
        this.directorio = file;
        listaBoletin = util.Listados.boletines(fecha);
        total = listaBoletin.size();
    }

    @Override
    public void run() {
        try {
            proceso();
        } catch (SQLException ex) {
            vista.JOption.sql(Datagest.ventana, ex.getMessage());
            Logger.getLogger(ProcesoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getTotal() {
        return listaBoletin.size();
    }

    private List<Edicto> getEdictos(String idBoletin) {
        List<Edicto> list;
        list = util.Listados.edictosCompleto(idBoletin);
        return list;
    }

    private void proceso() throws SQLException {
        Datagest.ventana.setEspera();
        Iterator it = listaBoletin.iterator();

        while (it.hasNext()) {
            boletin = (Boletin) it.next();
            origen = util.GetEntidad.origen(new Origen(boletin.getOrigen()));
            try {
                creaArchivo(boletin, origen);
            } catch (IOException ex) {
                Logger.getLogger(ProcesoArchivos.class.getName()).log(Level.SEVERE, null, ex);
                vista.JOption.ioe(Datagest.ventana, ex.getMessage());
            }
            Main.proceso.setValor((indice * 100) / total);
            indice++;
        }
        Datagest.ventana.setDefault();
        Main.proceso.close();
        vista.JOption.completado(Datagest.ventana, "Se han generado los archivos");
    }

    private void creaArchivo(Boletin boletin, Origen origen) throws IOException {
        String str, datos = "";
        String separador = "------------------------\n";

        File archivo = new File(directorio, origen.getIdOrigen() + "-" + getNombreArchivo(origen, boletin) + ".txt");

        if (!archivo.exists()) {
            archivo.createNewFile();
        }
        List<Edicto> list = getEdictos(boletin.getIdBoletin());
        Iterator it = list.iterator();

        while (it.hasNext()) {
            edicto = (Edicto) it.next();
            str = "BCN2 " + getEnlace(edicto) + " \n"
                    + getCabecera(edicto.getTipo(), origen.getCodigo()) + "\n"
                    + "TESTRA" + "\n"
                    + "BCN5 " + nombreOrigen(origen.getNombre()) + " \n"
                    + edicto.getDatos();
            datos = datos + str + separador;
            datos = limpiar(datos);
            datos = datos.replace(str, "\n\r" + str + "\n\r");
        }
        Archivo.escribeArchivo(archivo, datos);
    }

    private String getEnlace(Edicto edicto) {
        String str = "";
        try {
            Sql bd = new Sql(Datagest.con);
            str = bd.getEnlaceDescarga(edicto);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProcesoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    private String nombreOrigen(String str) {
        String aux = str.toUpperCase();
        aux = aux.replace("AY", "AYUNTAMIENTO DE");
        return aux;
    }

    private String getNombreArchivo(Origen or, Boletin bo) {
        String str = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(bo.getFecha());
        String ori = or.getIdOrigen();

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia < 10) {
            str = str + "0" + dia;
        } else {
            str = str + dia;
        }

        str = str + "0";

        String anno = Integer.toString(cal.get(Calendar.YEAR));
        str = str + anno.charAt(3);

        str = str + ori.substring(0, 2);

        int mes = cal.get(Calendar.MONTH);
        mes++;
        if (mes < 10) {
            str = str + mes + "P-";
        } else {
            if (mes == 10) {
                str = str + "XP-";
            }
            if (mes == 11) {
                str = str + "YP-";
            }
            if (mes == 12) {
                str = str + "ZP-";
            }
        }

        if (dia < 10) {
            str = str + "0" + dia + ".";
        } else {
            str = str + dia + ".";
        }

        if (mes < 10) {
            str = str + "0" + mes + ".";
        } else {
            str = str + mes + ".";
        }

        return str;
    }

    private String getCabecera(String tipo, String codigo) {
        String cadena;
        tipo = tipo.replace("*", "");
        tipo = tipo.toUpperCase();
        if ("".equals(codigo) || codigo == null) {
            codigo = "BCN1";
        }
        if ("".equals(edicto.getFase()) || edicto.getFase() == null) {
            cadena = codigo + "-(" + tipo + ")-";
        } else {
            cadena = codigo + "-" + edicto.getFase();
        }

        return cadena;
    }

    private String limpiar(String str) {
        String a = System.getProperty("line.separator");
        String string = str.replaceAll("CSV: [A-Z0-9]{6}-[A-Z0-9]{6}-[A-Z0-9]{6}-[A-Z0-9]{6}", "");
        string = string.replaceAll("https://sede.dgt.gob.es TABLÓN EDICTAL DE SANCIONES DE TRÁFICO", "");
        string = string.replaceAll("TABLÓN EDICTAL DE SANCIONES DE TRÁFICO", "");
        string = string.replaceAll("   ", "");
        string = string.replaceAll("\n\r", "");
        return string;
    }
}
