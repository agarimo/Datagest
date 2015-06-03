package util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Agárimo
 */
public class Regex {
    public static int fecha=0;
    public static int dni=1;
    public static int matricula=2;
    public static int precepto=3;
    public static int articulo=4;
    
    private static List<String> listPrecepto=new ArrayList<String>();
    private static Pattern pt;
    private static Matcher mt;
    
    private static String[] fechas = {"[\\s]\\d{2}[/-]\\d{2}[/-]\\d{4}[\\s]",
                                     "[\\s]\\d{4}[/-]\\d{2}[/-]\\d{2}[\\s]",
                                     "NO CONSTA"};
    
    private static String[] identificacion = {"[0-9]{4,8}[A-Za-z]{1}",
                                              "[A-Za-z]{1}[0-9]{8,9}",
                                              "[A-Za-z]{1}[0-9]{8}[A-Za-z]{1}",
                                              "[A-Za-z]{1}[0-9]{7}[A-Za-z]{1}",
                                              "[A-Z]{1,3}[0-9]{6,8}",
                                              "NO CONSTA"};
    
    private static String[] matriculas = {"[0-9]{4}[\\s-]{0,1}[A-Z]{2,3}",
                                         "[A-Z]{1,2}[\\s-]{0,1}[0-9]{4,5}[\\s-]{0,1}[A-Z]{1,2}",
                                         "POKET BIKE",
                                         "EX",
                                         "CARECE"};
    
    private static String[] preceptos = {"","[//s][0-9]{4}","[\\s][0-9]{1,4}[//s/][0-9]{1,4}","[0-9]{1,2}[//s]"};

    private static String[] articulos = {"[0-9]{1,3}[.][A-Za-z0-9-]{1,2}", "[0-9]{1,3}"};
    
    
    public static boolean buscar(String str, int tipo){
        boolean existe=false;
        
        switch(tipo){
            case 0:
                if(getFecha(str)!=null){
//                    System.out.println("fecha encontrada: "+getFecha(str));
                    existe=true;
                }else{
//                    System.out.println("fecha no encontrada: "+getFecha(str));
                }
                break;
            case 1:
                if(getDni(str)!=null){
                    existe=true;
//                    System.out.println("dni encontrado: "+getDni(str));
                }else{
//                    System.out.println("dni no encontrado: "+getDni(str));
                }
                break;
            case 2:
                if(getMatricula(str)!=null){
                    existe=true;
                }
                break;
            case 3:
                if(getPrecepto(str)!=null){
                    existe = true;
                }
                break;
            case 4:
                if(getArticulo(str)!=null){
                    existe = true;
                }
                break;
        }
        return existe;
    }
    
    public static String getFecha(String str){
        String aux = null;
        for (int i = 0; i < fechas.length; i++) {
            pt = Pattern.compile(fechas[i]);
            mt = pt.matcher(str);

            if (mt.find()) {
                aux = mt.group();
                break;
            }
        }
        return aux;
    }
    
    public static String getDni(String str){
        String aux = null;

        for (int i = 0; i < identificacion.length; i++) {
            pt = Pattern.compile(identificacion[i]);
            mt = pt.matcher(str);

            if (mt.find()) {
                System.out.println("Encontrado patrón: "+identificacion[i]);
                aux = mt.group();
                break;
            }
        }
        return aux;
    }
    
    public static String getMatricula(String str){
        String aux = null;

        for (int i = 0; i < matriculas.length; i++) {
            pt = Pattern.compile(matriculas[i]);
            mt = pt.matcher(str);

            if (mt.find()) {
                aux = mt.group();
                break;
            }
        }
        return aux;
    }
    
    public static String getPrecepto(String str){
        String aux = null;

        for (int i = 0; i < listPrecepto.size(); i++) {
            for(int j=0; j< preceptos.length;j++){
                pt = Pattern.compile(listPrecepto.get(i) +preceptos[j]);
                mt = pt.matcher(str);

                if (mt.find()) {
                    aux = mt.group();
                }
            }
            //meter en cada ciclo una comprobación con puntear() (mira el siguiente método después de este).
            
        }
        return aux;
    }
    
    public static String getArticulo(String str){
        String aux = null;
        String[] split = str.split(getPrecepto(str));

        for (int i = 0; i < articulos.length; i++) {
            pt = Pattern.compile(articulos[i]);
            mt = pt.matcher(split[1]);

            if (mt.find()) {
                aux = mt.group();
                break;
            }
        }
        return aux;
    }
    
    public static List<String> cargaPreceptos(){
        List<String> aux=new ArrayList<String>();
        return aux;
    }
    
}
