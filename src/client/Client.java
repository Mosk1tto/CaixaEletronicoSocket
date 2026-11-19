package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {

    private JTextField campoIp;
    private JButton botaoConectar;
    private JButton botaoSaldo;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private JTextArea areaRespostas;

    public Client() {
        setTitle("Caixa Eletrônico");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new FlowLayout());
        JLabel textoIp = new JLabel("IP do servidor:");
        campoIp = new JTextField("127.0.0.1", 12);
        botaoConectar = new JButton("Conectar");

        botaoConectar.addActionListener(e -> {
            String ip = campoIp.getText();
            try {
                socket = new Socket(ip, 1024);

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                JOptionPane.showMessageDialog(this, "Conectado em " + ip);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(textoIp);
        painel.add(campoIp);
        painel.add(botaoConectar);
        add(painel, BorderLayout.NORTH);

        areaRespostas = new JTextArea();
        areaRespostas.setEditable(false);

        add(new JScrollPane(areaRespostas), BorderLayout.CENTER);

        botaoSaldo = new JButton("Saldo");
        botaoSaldo.addActionListener(e -> enviar("SALDO"));

        add(botaoSaldo, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void enviar(String comando) {
        if (out == null) {
            JOptionPane.showMessageDialog(this, "Conecte ao servidor primeiro!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            out.writeUTF(comando);
            String resposta = in.readUTF();
            areaRespostas.append(resposta + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao se comunicar com o servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}