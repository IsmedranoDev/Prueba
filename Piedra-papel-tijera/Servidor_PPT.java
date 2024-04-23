/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Tarea_4_2;

import TCP_IP.ServidorSuma;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author israelmedrano
 */
public class Servidor_PPT extends ServerSocket {
    
   
    
  
    
    public Servidor_PPT() throws IOException{
    super(12345);
    }

    
    public void aceptarPeticiones(){
        ExecutorService ejecutor = Executors.newCachedThreadPool();
     
        
        while(true){
        try {
            Socket jugador1 = accept();            
            DataInputStream dis1 = new DataInputStream(jugador1.getInputStream());
            DataOutputStream dos1 = new DataOutputStream(jugador1.getOutputStream());
            
            Socket jugador2 = accept();            
            DataInputStream dis2 = new DataInputStream(jugador2.getInputStream());            
            DataOutputStream dos2 = new DataOutputStream(jugador2.getOutputStream());
            
            
            //Creamos un temporizador y lo establecemos para los jugadores
            
            Future<Integer> respuestaJugador1 = ejecutor.submit(() -> dis1.readInt());
            Future<Integer> respuestaJugador2 = ejecutor.submit(() -> dis2.readInt());
            
            int j1= -1;
            int j2=-1;
            
            try{
                j1= respuestaJugador1.get(5, TimeUnit.SECONDS);
                j2 = respuestaJugador2.get(5, TimeUnit.SECONDS);
                
                dos1.writeInt(j2);
    
            dos2.writeInt(j1);
                
            }catch(TimeoutException e ){
             
                if(j1!=-1){
                    dos1.writeUTF("Tiempo agotado. Has ganado");
                } else {
                    dos2.writeUTF("Tiempo agotado. Has perdido");
                }
                
                if(j2!=-1) dos2.writeUTF("Tiempo agotado. Has ganado");
                else dos1.writeUTF("Tiempo agotado. Has perdido");
                
                
                continue;
            }
     
      
            //Capturamos los datos que envian los jugadores
          
         
            
      
            //lógica de juego
            if(j1==j2){
                dos1.writeUTF("Habeis quedado empate");
                dos2.writeUTF("Habeis quedado empate");
            } else if ((j1==1 && j2==3) || (j1==2 && j2==1) || (j1==3 && j2==2)) {
                dos1.writeUTF("Has ganado");
                dos2.writeUTF("Has perdido");
            } else if ((j2==1 && j1==3) || (j2==2 && j1==1) || (j2==3 && j1==2)){
                dos1.writeUTF("Has perdido");
                dos2.writeUTF("Has ganado");
            }
            
            
            //Envio resultados
            
            
          
       
        
            jugador1.close();
            jugador2.close();
        } catch (IOException | InterruptedException | ExecutionException ex){
            ex.printStackTrace();
        
         }
        }
        
        
        
        
    }
    
    
    
       public static void main(String[] args) {
        // TODO code application logic here
        
        
        //Conectamos el servidor para escuchar solicitudes
        
        try{
            
            Servidor_PPT servidor = new Servidor_PPT();
            
            servidor.aceptarPeticiones();
            
            servidor.close();
            
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
