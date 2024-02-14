package TCP.clases;

import java.net.Socket;
import java.util.ArrayList;

public class ComunHilos {
    int conexiones;
    int actuales;
    int maximo;
    Socket[] tabla;
    String mensajes;
    ArrayList<String> nombres;

    public ComunHilos(int maximo, int actuales, int conexiones, Socket[] tabla) {
        this.maximo = maximo;
        this.actuales = actuales;
        this.conexiones = conexiones;
        this.tabla = tabla;
        mensajes = "";
        nombres = new ArrayList<>();
    }

    public boolean nombreEnUso(String nombre) {
        return nombres.contains(nombre);
    }

    public synchronized void agregarNombre(String nombre) {
        nombres.add(nombre);
    }

    public int getConexiones() { return conexiones;	}
    public synchronized void setConexiones(int conexiones) {
        this.conexiones = conexiones;
    }

    public String getMensajes() { return mensajes; }
    public synchronized void setMensajes(String mensajes) {
        this.mensajes = mensajes;
    }

    public int getActuales() { return actuales; }
    public synchronized void setActuales(int actuales) {
        this.actuales = actuales;
    }

    public synchronized void addTabla(Socket s, int i) {
        tabla[i] = s;
    }
    public Socket getElementoTabla(int i) { return tabla[i]; }

}