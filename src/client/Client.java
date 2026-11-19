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

    private JTextField campoConta;
    private JPasswordField campoSenha;
    private JTextField campoValor;

    private JButton botaoEntrar;
    private JButton botaoSacar;
    private JButton botaoDepositar;
    private JButton botaoSair;

    public Client() {
        setTitle("Caixa Eletrônico");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelConexao = new JPanel(new FlowLayout());
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

        painelConexao.add(textoIp);
        painelConexao.add(campoIp);
        painelConexao.add(botaoConectar);
        add(painelConexao, BorderLayout.NORTH);

        areaRespostas = new JTextArea();
        areaRespostas.setEditable(false);

        add(new JScrollPane(areaRespostas), BorderLayout.CENTER);

        JPanel painelOperacoes = new JPanel(new GridLayout(0, 2, 5, 5));

        painelOperacoes.add(new JLabel("Conta:"));
        campoConta = new JTextField();
        painelOperacoes.add(campoConta);

        painelOperacoes.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painelOperacoes.add(campoSenha);

        botaoEntrar = new JButton("Entrar");
        botaoEntrar.addActionListener(e -> enviar("LOGIN " + campoConta.getText() + " " + new String(campoSenha.getPassword())));
        painelOperacoes.add(botaoEntrar);

        botaoSaldo = new JButton("Saldo");
        botaoSaldo.addActionListener(e -> enviar("SALDO"));
        painelOperacoes.add(botaoSaldo);

        painelOperacoes.add(new JLabel("Valor:"));
        campoValor = new JTextField();
        painelOperacoes.add(campoValor);

        botaoSacar = new JButton("Sacar");
        botaoSacar.addActionListener(e -> enviar("SAQUE " + campoValor.getText()));
        painelOperacoes.add(botaoSacar);

        botaoDepositar = new JButton("Depositar");
        botaoDepositar.addActionListener(e -> enviar("DEPOSITO " + campoValor.getText()));
        painelOperacoes.add(botaoDepositar);

        botaoSair = new JButton("Sair");
        botaoSair.addActionListener(e -> {
            if (out != null) {
                enviar("SAIR");
                try {
                    socket.close();
                } catch (IOException ex) {
                }
            }
            System.exit(0);
        });

        JPanel painelInferior = new JPanel(new BorderLayout(5, 5));
        painelInferior.add(painelOperacoes, BorderLayout.CENTER);
        painelInferior.add(botaoSair, BorderLayout.SOUTH);

        add(painelInferior, BorderLayout.SOUTH);

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