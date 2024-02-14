package TCP.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.net.Socket;

public class ClienteChat extends JFrame implements ActionListener, Runnable {
    @Serial
    private static final long serialVersionUID = 1L;
    Socket socket = null;

    DataInputStream flujoEntrada;
    DataOutputStream flujoSalida;
    String nombre;
    static JTextField mensaje = new JTextField();

    private JScrollPane scrollpane1;
    static JTextArea textarea1;
    JButton botonEnviar = new JButton("Enviar");
    JButton botonSalir = new JButton("Salir");
    boolean repetir = true;

    public ClienteChat(Socket s, String nombre) {
        super(" CONEXIÓN DEL CLIENTE CHAT: " + nombre);
        setLayout(null);

        mensaje.setBounds(10, 10, 400, 30);
        add(mensaje);

        textarea1 = new JTextArea();
        scrollpane1 = new JScrollPane(textarea1);
        scrollpane1.setBounds(10, 50, 400, 300);
        add(scrollpane1);

        botonEnviar.setBounds(420, 10, 100, 30);
        add(botonEnviar);
        botonSalir.setBounds(420, 50, 100, 30);
        add(botonSalir);

        textarea1.setEditable(false);
        botonEnviar.addActionListener(this);
        botonSalir.addActionListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        socket = s;
        this.nombre = nombre;
        try {
            flujoEntrada = new DataInputStream(socket.getInputStream());
            flujoSalida = new DataOutputStream(socket.getOutputStream());
            String texto = nombre + " entra en el Chat ...";
            flujoSalida.writeUTF(texto);
        } catch (IOException e) {
            System.out.println("ERROR DE E/S");
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonEnviar) {
            if (mensaje.getText().trim().isEmpty())
                return;
            String texto = nombre + "> " + mensaje.getText();

            try {
                mensaje.setText("");
                flujoSalida.writeUTF(texto);
            } catch (IOException e1) {
                System.err.println(e1.getMessage());
            }
        }
        if (e.getSource() == botonSalir) { // SE PULSA BOTON SALIR
            String texto = nombre + " abandona el Chat ...";
            try {
                flujoSalida.writeUTF(texto);
                flujoSalida.writeUTF("*");
                repetir = false;
            } catch (IOException e1) {
                System.err.println(e1.getMessage());
            }
        }
    }

    public void run() {
        String texto = "";
        while (repetir) {
            try {
                texto = flujoEntrada.readUTF();
                textarea1.setText(texto);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),
                        "<<MENSAJE DE ERROR:2>>", JOptionPane.ERROR_MESSAGE);
                repetir = false;
            }
        }

        try {
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args)  {
        int puerto = 6404;
        Socket s;

        String nombre = JOptionPane.showInputDialog("Introduce tu nombre o nick:");

        if (nombre.trim().isEmpty()) {
            System.out.println("El nombre está vacío....");
            return;
        }

        try {
            s = new Socket("localhost", puerto);
            ClienteChat cliente = new ClienteChat(s, nombre);
            cliente.setBounds(0, 0, 540, 400);
            cliente.setVisible(true);
            new Thread(cliente).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),
                    "<<MENSAJE DE ERROR:1>>", JOptionPane.ERROR_MESSAGE);
        }

    }
}