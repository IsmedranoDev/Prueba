/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Tarea_4_2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author israelmedrano
 */
public class Cliente_PPT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        Scanner teclado = new Scanner(System.in);
        int opcion = -1;
        String resultado;
        
        do{
            do{
            System.out.println("Bienvenido al juego de Piedra, Papel o Tijera");
            System.out.println("---------------------------------------------");
            System.out.println("Piedra : Pulsa 1");
            System.out.println("Papel : Pulsa 2");
            System.out.println("Tijera : Pulsa 3");
            System.out.println("Salir : Pulsa 0");
            System.out.println("---------------------------------------------");
            System.out.println("Elige una opción: ");
            
            if(teclado.hasNextInt()==true) opcion = teclado.nextInt();
            else {System.out.println("La entrada es incorrecta");
            teclado.nextLine();
            
            }
            } while (opcion==-1 || opcion<0 || opcion>=4);
            
            if(opcion==0) System.exit(0);
            
            
            try(
            Socket misocket = new Socket("localhost", 12345);
            DataInputStream dis = new DataInputStream(misocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(misocket.getOutputStream())){
            
            
            dos.writeInt(opcion);
            resultado = dis.readUTF();
             int eleccionOtroJugador = dis.readInt();
            
            System.out.println("Has elegido : " + imprimeOpcion(opcion));
            System.out.println("El otro jugador ha elegido : " + imprimeOpcion(eleccionOtroJugador));
            
            System.out.println(resultado);
            System.out.println("Muchas gracias por jugar a este apasionante juego");
            System.out.println("-------------------------------------------------");
            
                    
           
            
            }catch (IOException ex){
                ex.printStackTrace();
            }   
            
        } while (opcion!=0);
        
        // TODO code application logic here
    } //// Fin del método principal
    
    
    public static String imprimeOpcion(int opcion){
        switch(opcion){
            case 1:
                return "Piedra";
            case 2:
                return "Papel";
            case 3:
                return "Tijeras";     
        }
        return null;
    }
} ///Fin de la clase
