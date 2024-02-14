package UDP.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serial;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Chat extends JFrame implements ActionListener, Runnable {
    @Serial
    private static final long serialVersionUID = 1L;

    static MulticastSocket socket = null;
    static byte[] buf = new byte[1000];
    static InetAddress grupo = null;
    static int Puerto = 6404;

    static JTextField mensaje = new JTextField();
    private JScrollPane scrollpane1;
    static JTextArea textarea1;
    JButton btnEnviar = new JButton("Enviar");
    JButton btnSalir = new JButton("Salir");
    boolean repetir = true;
    static String usuario;
    static Set<String> userSet = new HashSet<>();

    public Chat(String nom) {
        super(" VENTANA DE CHAT UDP - Nick: " + nom);
        setLayout(null);
        this.usuario = nom;
        mensaje.setBounds(10, 10, 400, 30); add(mensaje);
        textarea1 = new JTextArea();
        scrollpane1 = new JScrollPane(textarea1);
        scrollpane1.setBounds(10, 50, 400, 300);
        add(scrollpane1);
        btnEnviar.setBounds(420, 10, 100, 30); 	add(btnEnviar);
        btnSalir.setBounds(420, 50, 100, 30);
        add(btnSalir);

        textarea1.setEditable(false);
        btnEnviar.addActionListener(this);
        btnSalir.addActionListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEnviar) {
            String texto = usuario + ">> " + mensaje.getText();
            try {
                DatagramPacket paquete = new DatagramPacket(texto.getBytes(), texto.length(), grupo, Puerto);
                socket.send(paquete);
            } catch (IOException e1) {
                System.err.println(e1.getMessage());
            }
        }
        if (e.getSource() == btnSalir) {
            String texto = "*** Abandona el chat: " + usuario + " ***";
            try {
                DatagramPacket paquete = new DatagramPacket(texto.getBytes(), texto.length(), grupo, Puerto);
                socket.send(paquete);
                socket.close();
                repetir = false;
                System.out.println("Abandona el chat: "+ usuario);
                System.exit(0);

            } catch (IOException e1) {
                System.err.println(e1.getMessage());
            }
        }
    }

    public void run() {
        while (repetir) {
            try {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                socket.receive(p);
                String texto = new String(p.getData(), 0, p.getLength());
                textarea1.append(texto + "\n");
            }catch (SocketException s) {
                System.out.println(s.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String args[]) throws IOException {
        String usuario = JOptionPane.showInputDialog("Introduce tu nombre o nick:");
        socket = new MulticastSocket(Puerto);
        grupo = InetAddress.getByName("225.0.0.1");// Grupo
        socket.joinGroup(grupo);
        if (!usuario.trim().isEmpty() && !userSet.contains(usuario)) {
            userSet.add(usuario);
            Chat server = new Chat(usuario);
            server.setBounds(0, 0, 540, 400);
            server.setVisible(true);
            new Thread(server).start();
        } else {
            System.out.println("El nombre esta vacio....");
        }
    }
}