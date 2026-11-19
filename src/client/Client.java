package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {

    private JTextField campoIp;
    private JButton botaoConectar;

    public Client() {
        setTitle("Caixa Eletrônico");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new FlowLayout());
        JLabel textoIp = new JLabel("IP do servidor:");
        campoIp = new JTextField("127.0.0.1", 12);
        botaoConectar = new JButton("Conectar");
        painel.add(textoIp);
        painel.add(campoIp);
        painel.add(botaoConectar);
        add(painel, BorderLayout.NORTH);

        setVisible(true);
    }

    public void run() throws Exception {

        // Passo 1 - Abrir conexão com o servidor na porta 1024
        Socket client = new Socket("127.0.0.1", 1024);

        // Passo 2 - Obter os fluxos de entrada e saída
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        // Passo 3 - Comunicação com o servidor
        out.writeInt(2);
        out.writeUTF("Olá sou uma msg do cliente!");
        int k = in.readInt();
        String s = in.readUTF();
        System.out.println("Int: " + k);
        System.out.println("String: " + s);

        // Passo 4 - Fechar fluxos e conexão
        in.close();
        out.close();
        client.close();
    }
}