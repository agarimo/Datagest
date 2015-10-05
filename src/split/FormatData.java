package split;

import entidades.Edicto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Regex;

/**
 *
 * @author Agárimo
 */
public class FormatData {

    List<String> list = new ArrayList<String>();
    String datos;

    public FormatData() {
        Edicto edicto = util.GetEntidad.edictoCompleto(new Edicto("000000000076-010590"));
        datos = edicto.getDatos();
        datos = leer();
        datos = limpiar(datos);
        cargaList();
        limpiaFin();
        System.out.println("Inicio Documento");
        System.out.println(datos);
        System.out.println("Fin Documento");
    }

    private String leer() {
        String fin = "";
        boolean bol = false;
        BufferedReader bf = new BufferedReader(new StringReader(datos));
        String str;
        try {
            while ((str = bf.readLine()) != null) {

                if (str.contains("https://sede.dgt.gob.es")) {
                    bol = false;
                }

                if (bol) {
                    fin = fin + str + "\n";
                }

                if (str.contains("EXPEDIENTE DENUNCIADO/A") || str.contains("EXPEDIENTE SANCIONADO/A")) {
                    bol = true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FormatData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return limpiaTitulos(limpiar(fin));
    }

    private void format(String str) {
        BufferedReader bf = new BufferedReader(new StringReader(str));
        String aux;

        try {
            while ((aux = bf.readLine()) != null) {
                if (aux.length() < 25) {
                    datos = datos.replace(aux + "\n", aux);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FormatData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String limpiar(String str) {
        String a = System.getProperty("line.separator");
        String string = str.replaceAll("CSV: [A-Z0-9]{6}-[A-Z0-9]{6}-[A-Z0-9]{6}-[A-Z0-9]{6}", "");
        string = string.replaceAll("https://sede.dgt.gob.es TABLÓN EDICTAL DE SANCIONES DE TRÁFICO", "");
        string = string.replaceAll("TABLÓN EDICTAL DE SANCIONES DE TRÁFICO", "");
        string = string.replaceAll("\n\r", "");
        return string;
    }

    private String limpiaTitulos(String str) {
        String fin = "";
        BufferedReader bf = new BufferedReader(new StringReader(str));
        String aux;

        try {
            while ((aux = bf.readLine()) != null) {
                if (!aux.contains("EUROS PRECEPTO")) {
                    fin = fin + aux + "\n";
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FormatData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fin;
    }

    private void limpiaFin() {
        StringBuilder sb = new StringBuilder();
        Iterator it;
        String aux;
        boolean continua = true;

        while (continua) {
            aux = list.get(list.size() - 1);
            if (Regex.buscar(aux, Regex.FECHA) || Regex.buscar(aux, Regex.DNI)) {
                continua = false;
            } else {
                list.remove(list.size() - 1);
            }
        }
        it = list.iterator();
        while (it.hasNext()) {
            aux = (String) it.next();
            sb.append(aux);
            sb.append("\n");
        }
        datos = sb.toString().substring(0, sb.length() - 1);
    }

    private void cargaList() {
        BufferedReader bf = new BufferedReader(new StringReader(datos));
        String aux;

        try {
            while ((aux = bf.readLine()) != null) {
                list.add(aux);
            }
        } catch (IOException ex) {
            Logger.getLogger(FormatData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
