package TCP.util;

import TCP.clases.ComunHilos;
import TCP.hilos.HiloChat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final int CONEXIONES_MAXIMAS = 10;

    public static void main(String args[]) throws IOException {
        int PUERTO = 6404;

        ServerSocket servidor = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado...");

        Socket[] tabla = new Socket[CONEXIONES_MAXIMAS];
        ComunHilos comun = new ComunHilos(CONEXIONES_MAXIMAS, 0, 0, tabla);

        while (comun.getConexiones() < CONEXIONES_MAXIMAS) {
            Socket socket;
            socket = servidor.accept();

            String nombre = obtenerNombreCliente(socket);
            if (comun.nombreEnUso(nombre)) {
                System.out.println("Nombre de usuario ya en uso: " + nombre);
                socket.close();
                continue;
            }

            comun.agregarNombre(nombre);

            comun.addTabla(socket, comun.getConexiones());
            comun.setActuales(comun.getActuales() + 1);
            comun.setConexiones(comun.getConexiones() + 1);

            HiloChat hilo = new HiloChat(socket, comun);
            hilo.start();
        }
        servidor.close();
    }

    private static String obtenerNombreCliente(Socket socket) throws IOException {
        DataInputStream flujoEntrada = null;
        flujoEntrada = new DataInputStream(socket.getInputStream());
        return flujoEntrada.readUTF();
    }
}
