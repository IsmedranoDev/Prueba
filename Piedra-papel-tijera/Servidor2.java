import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Servidor2 extends ServerSocket {
    private ExecutorService ejecutor;

    public Servidor2() throws IOException {
        super(12345);
        ejecutor = Executors.newCachedThreadPool();
    }

    public void aceptarPeticiones() {
        while (true) {
            try {
                Socket jugador1 = accept();
                Socket jugador2 = accept();

                ejecutor.submit(() -> manejarJugadores(jugador1, jugador2));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void manejarJugadores(Socket jugador1, Socket jugador2) {
        try {
            DataInputStream dis1 = new DataInputStream(jugador1.getInputStream());
            DataOutputStream dos1 = new DataOutputStream(jugador1.getOutputStream());

            DataInputStream dis2 = new DataInputStream(jugador2.getInputStream());
            DataOutputStream dos2 = new DataOutputStream(jugador2.getOutputStream());

            Future<Integer> respuestaJugador1 = ejecutor.submit(() -> dis1.readInt());
            Future<Integer> respuestaJugador2 = ejecutor.submit(() -> dis2.readInt());

            int j1 = obtenerRespuesta(respuestaJugador1, dos1, dos2);
            int j2 = obtenerRespuesta(respuestaJugador2, dos2, dos1);

            if (j1 == -1 || j2 == -1) {
                return;
            }

            // lógica de juego
            if (j1 == j2) {
                dos1.writeUTF("Habeis quedado empate");
                dos2.writeUTF("Habeis quedado empate");
            } else if ((j1 == 1 && j2 == 3) || (j1 == 2 && j2 == 1) || (j1 == 3 && j2 == 2)) {
                dos1.writeUTF("Has ganado");
                dos2.writeUTF("Has perdido");
            } else if ((j2 == 1 && j1 == 3) || (j2 == 2 && j1 == 1) || (j2 == 3 && j1 == 2)) {
                dos1.writeUTF("Has perdido");
                dos2.writeUTF("Has ganado");
            }

            // Envio resultados
            dos1.writeInt(j2);
            dos2.writeInt(j1);

            jugador1.close();
            jugador2.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private int obtenerRespuesta(Future<Integer> respuesta, DataOutputStream dosGanador, DataOutputStream dosPerdedor) {
        try {
            return respuesta.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            try {
                dosGanador.writeUTF("Tiempo agotado. Has ganado");
                dosPerdedor.writeUTF("Tiempo agotado. Has perdido");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return -1;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        try {
            Servidor2 servidor = new Servidor2();
            servidor.aceptarPeticiones();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
