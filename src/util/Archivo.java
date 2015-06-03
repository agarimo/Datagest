package util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class Archivo {

    public static void escribeArchivo(File archivo, String datos) {
        FileWriter fw = null;
        PrintWriter pw;

        try {
            fw = new FileWriter(archivo);
            pw = new PrintWriter(fw);

            pw.print(datos);
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
            vista.JOption.ioe(Datagest.ventana, ex.getMessage());
        } finally {
            try {
                if (null != fw) {
                    fw.close();
                }
            } catch (Exception e) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e);
                vista.JOption.ioe(Datagest.ventana, e.getMessage());
            }
        }
    }

    public static void anexaArchivo(File archivo, String datos) {
        BufferedWriter out;
        
        try {
            out = new BufferedWriter(new FileWriter(archivo,true));
            out.append(datos);
            out.close();
        } catch (IOException e) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static String leeArchivo(File archivo) {
        String datos = "";
        FileReader fr = null;
        BufferedReader br;
        try {
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;
            while ((linea = br.readLine()) != null) {
                datos = datos + linea + System.getProperty("line.separator");
            }
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return datos;
    }
}
