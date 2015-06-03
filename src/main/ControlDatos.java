package main;

import entidades.Conexion;
import entidades.Tipo;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.Url;

/**
 *
 * @author Agárimo
 */
public final class ControlDatos {

    List<Conexion> con;
    List<Url> url;
    Tipo faseAsociada;

    public ControlDatos() {
        con = new ArrayList<Conexion>();
        url = new ArrayList<Url>();
    }

    public ControlDatos(String carga) {
        con = Datagest.listCon;
        url = Datagest.listUrl;
    }

    public void actualizaLista() {
        Collections.sort(url);
        Datagest.listCon = con;
        Datagest.listUrl = url;
    }

    public void cargaDatos() {
        cargar();
        Datagest.listCon = this.con;
        Datagest.listUrl = this.url;
        Datagest.faseAsociada = this.faseAsociada;
    }

    public void guardaDatos() {
        this.con = Datagest.listCon;
        this.url = Datagest.listUrl;
        this.faseAsociada = Datagest.faseAsociada;
        guardar();
    }

    public void nuevaCon(Conexion aux) {
        if (!con.contains(aux)) {
            con.add(aux);
        }
    }

    public void borraCon(Conexion aux) {
        if (con.contains(aux)) {
            con.remove(aux);
        }
    }

    public void modificaCon(Conexion aux) {
        int indice = con.indexOf(aux);
        if (indice != -1) {
            con.set(indice, aux);
        }
    }

    public boolean buscar(Conexion aux) {
        if (con.contains(aux)) {
            return true;
        } else {
            return false;
        }
    }

    public void nuevaUrl(Url aux) {
        if (!url.contains(aux)) {
            url.add(aux);
        }
    }

    public void borraUrl(Url aux) {
        if (url.contains(aux)) {
            url.remove(aux);
        }
    }

    public void modificaUrl(Url aux) {
        int indice = url.indexOf(aux);
        if (indice != -1) {
            url.set(indice, aux);
        }
    }
    
    public boolean buscar(Url aux){
        if (url.contains(aux)) {
            return true;
        } else {
            return false;
        }
    }

    private void guardar() {
        try {
            ObjectOutputStream flujoSalida = new ObjectOutputStream(new FileOutputStream(new File("configuracion.conf")));

            flujoSalida.writeObject(this.con);
            flujoSalida.writeObject(this.url);
            flujoSalida.writeObject(this.faseAsociada);
            System.out.println("Datos guardados correctamente.");

        } catch (IOException ioe) {
            vista.JOption.ioe(Datagest.ventana, "Error al intentar guardar los datos");
        }
    }

    private void cargar() {
        ObjectInputStream flujoEntrada = null;
        try {
            flujoEntrada = new ObjectInputStream(new FileInputStream(new File("configuracion.conf")));
            this.con = (List<Conexion>) flujoEntrada.readObject();
            this.url = (List<Url>) flujoEntrada.readObject();
            this.faseAsociada = (Tipo) flujoEntrada.readObject();
            System.out.println("Datos cargados correctamente.");
        } catch (ClassNotFoundException ex) {
            vista.JOption.classNotFound(Datagest.ventana, "Error al intentar cargar los datos");
        } catch (IOException ex) {
            vista.JOption.ioe(Datagest.ventana, "Error al intentar cargar los datos");
        } finally {
            try {
                flujoEntrada.close();
            } catch (IOException ex) {
            }
        }
    }
}
