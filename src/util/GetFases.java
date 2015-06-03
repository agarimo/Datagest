package util;

import entidades.Fase;
import entidades.Origen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Agárimo
 */
public class GetFases {
    private List<Fase> listaCompleta;
    
    public GetFases(){
        listaCompleta=util.Listados.fasesCompleto();
    }
    
    public List<Fase> get(Origen origen){
        List<Fase> aux = new ArrayList<Fase>();
        Fase fase;
        Iterator it=listaCompleta.iterator();
        
        while(it.hasNext()){
            fase=(Fase) it.next();
            
            if(fase.getOrigen().equals(origen.getIdOrigen())){
                aux.add(fase);
            }
        }
        
        return aux;
    }
}
