package TCP.hilos;

import TCP.clases.ComunHilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HiloChat extends Thread {
    DataInputStream flujoEntrada;
    Socket socket;
    ComunHilos comun;

    public HiloChat(Socket s, ComunHilos comun) {
        this.socket = s;
        this.comun = comun;
        try {
            flujoEntrada = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("ERROR DE E/S");
            System.err.println(e.getMessage());
        }
    }

    public void run() {
        System.out.println("NUMERO DE CONEXIONES ACTUALES: " + comun.getActuales());

        String texto = comun.getMensajes();
        EnviarMensajesaTodos(texto);

        while (true) {
            String cadena = "";
            try {
                cadena = flujoEntrada.readUTF();
                if (cadena.trim().equals("*")) {
                    comun.setActuales(comun.getActuales() - 1);
                    System.out.println("NUMERO DE CONEXIONES ACTUALES: " + comun.getActuales());
                    break;
                }
                comun.setMensajes(comun.getMensajes() + cadena + "\n");
                EnviarMensajesaTodos(comun.getMensajes());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    private void EnviarMensajesaTodos(String texto) {
        int i;
        for (i = 0; i < comun.getConexiones(); i++) {
            Socket s1 = comun.getElementoTabla(i);
            if (!s1.isClosed()) {
                try {
                    DataOutputStream fsalida = new DataOutputStream(s1.getOutputStream());
                    fsalida.writeUTF(texto);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }
}