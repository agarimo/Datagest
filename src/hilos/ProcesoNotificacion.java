package hilos;

import java.awt.Color;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Datagest;

/**
 *
 * @author Agárimo
 */
public class ProcesoNotificacion extends Thread{
    List notificaciones;
    boolean continuar=true;
    
    
    public ProcesoNotificacion(){
        
    }
    
    @Override
    public void run(){
        Datagest.ventana.notificacion.setVisible(true);
        while(continuar){
            animacion();
        }
    }
    
    public void parar(){
        continuar=false;
    }
    
    private void animacion(){
        try {
            Datagest.ventana.notificacion.setBackground(Color.red);
            Datagest.ventana.notificacion.setForeground(Color.red);
            Thread.sleep(250);
            Datagest.ventana.notificacion.setBackground(Color.black);
            Datagest.ventana.notificacion.setForeground(Color.black);
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcesoNotificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
