package main;

import entidades.Conexion;
import entidades.Tipo;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import main.capturador.Capturador;
import util.Url;
import vista.Inicio;

/**
 *
 * @author Agárimo
 */
public class Datagest {

    public static Inicio init;
    public static Conexion con;
    public static Tipo faseAsociada;
    public static List<Conexion> listCon;
    public static List<Url> listUrl;
    public static vista.Main ventana;
    public static Capturador capturador;
    public static int conect;

    public static void main(String[] args) {
        initCon();
        inicio();
    }

    private static void initCon() {
        String[] conexion = {"SERVIDOR", "SERVER ROJO"};

        String aux = (String) JOptionPane.showInputDialog(null,
                "A que servidor debo conectarme?",
                "Conexión",
                JOptionPane.QUESTION_MESSAGE,
                null,
                conexion,
                conexion[0]);

        if (aux.equals("SERVIDOR")) {
            conect = 0;
            System.out.println("Conectando a SERVIDOR");
        } else {
            conect = 1;
            System.out.println("Conectando a SERVER-ROJO");
        }
    }

    private static void inicio() {
        init = new Inicio();
        init.setBarra(0, "Cargando driver SQL");
        driver();
        init.setBarra(0, "Iniciando Look & Feel");
        lookAndFeel();
        init.setBarra(0, "Cargando configuración");
        configuracion();
        creaVentana();
        init.setBarra(0, "Conectado");
        init.dispose();
        init.setBarra(0, "Iniciando Datagest");
    }

    private static void compruebaListas() {
        if (listCon == null) {
            listCon = new ArrayList<Conexion>();
        }
        if (listUrl == null) {
            listUrl = new ArrayList<Url>();
        }
    }

    private static void creaVentana() {
        ventana = new vista.Main(conexion(), con.getNombre());
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private static void configuracion() {
        ControlDatos cd = new ControlDatos();

        File file = new File("configuracion.conf");
        if (!file.exists()) {
            try {
                file.createNewFile();
                cd.guardaDatos();
            } catch (IOException ex) {
                vista.JOption.ioe(ventana, "Error al crear el archivo");
            }
        } else {
            cargarDatos();
        }
        compruebaListas();
    }

    private static void cargarDatos() {
        ControlDatos cd = new ControlDatos();
        cd.cargaDatos();
    }

    public static void guardarDatos() {
        ControlDatos cd = new ControlDatos();
        cd.guardaDatos();
    }

    private static void driver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            vista.JOption.classNotFound(ventana, "Error al cargar el driver de MySql");
        }
    }

    private static void lookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Datagest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Datagest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Datagest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Datagest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public static boolean conexion() {
        boolean conectado = false;

        switch (conect) {
            case 0:
                con = new Conexion("SERVIDOR", "192.168.1.40", "3306", "admin", "IkuinenK@@m.s84");
                System.out.println("Conectando a SERVIDOR");
                break;
            case 1:
                con = new Conexion("SERVER-ROJO", "192.168.1.41", "3306", "admin", "IkuinenK@@m.s84");
                System.out.println("Conectando a SERVER-ROJO");
                break;
        }

        if (testCon(con)) {
            conectado = true;
        }

        return conectado;
    }

    private static boolean testCon(Conexion aux) {
        boolean bool;
        try {
            Connection conecta = DriverManager.getConnection(aux.getRuta(), aux.getUsuario(), aux.getPass());
            bool = true;
            conecta.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            bool = false;
        }
        return bool;
    }

    public static String entrecomillar(String contenido) {
        return "'" + contenido + "'";
    }

    public static String imprimeFecha(Date cal) {
        String date;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        date = formato.format(cal);

        return date;
    }

    public static String imprimeHora(Date cal) {
        String date;
        SimpleDateFormat formato = new SimpleDateFormat("HHmmss");
        date = formato.format(cal);

        return date;
    }

    public static String imprimeFechaCompleta(Date cal) {
        String date;
        SimpleDateFormat formato = new SimpleDateFormat("EEE, d MMM yyyy");
        date = formato.format(cal);

        return date;
    }

    public static Date curdate() {
        Calendar cal = Calendar.getInstance();
        Date fecha = cal.getTime();
        return fecha;
    }

    public static void iniciaCapturador() {
        ventana.ocultar();
        capturador = new Capturador();
        capturador.iniciaGui();
    }

    public static void cierraCapturador() {
        ventana.mostrar();
//        capturador=null;
    }

    public static void abrirWeb(String url) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
