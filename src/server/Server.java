package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public void run() throws Exception {

        Map<String, Conta> contas = new HashMap<>();
        contas.put("1234", new Conta("1234", "1111", 500));
        contas.put("5678", new Conta("5678", "2222", 1000));

        Conta contaLogada = null;

        ServerSocket serverSocket = new ServerSocket(1024);
        System.out.println("Servidor aguardando conexão ...");

        Socket socket = serverSocket.accept();
        System.out.println("Conexao Recebida de " + socket.getInetAddress().getHostName());

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        String comando = "";
        while (!comando.equalsIgnoreCase("SAIR")) {
            try {
                comando = in.readUTF();
            } catch (EOFException e) {
                System.out.println("Cliente desconectou.");
                break;
            }
            System.out.println("Comando recebido: " + comando);

            String[] partes = comando.split(" ");
            String resposta;

            switch (partes[0].toUpperCase()) {
                case "LOGIN":
                    Conta conta = null;
                    if (partes.length == 3) {
                        conta = contas.get(partes[1]);
                    }
                    if (conta != null && conta.senha.equals(partes[2])) {
                        contaLogada = conta;
                        resposta = "OK Bem-vindo, conta " + conta.numero;
                    } else {
                        resposta = "ERRO Conta ou senha inválida";
                    }
                    break;

                case "SALDO":
                    if (contaLogada == null) {
                        resposta = "ERRO Faça login primeiro";
                    } else {
                        resposta = "OK Saldo: R$ " + String.format("%.2f", contaLogada.saldo);
                    }
                    break;

                case "SAQUE":
                    if (contaLogada == null) {
                        resposta = "ERRO Faça login primeiro";
                        break;
                    }
                    try {
                        double valor = Double.parseDouble(partes[1].replace(",", "."));
                        if (valor <= 0) {
                            resposta = "ERRO Valor inválido";
                        } else if (valor > contaLogada.saldo) {
                            resposta = "ERRO Saldo insuficiente";
                        } else {
                            contaLogada.saldo -= valor;
                            resposta = "OK Saque feito. Saldo: R$ " + String.format("%.2f", contaLogada.saldo);
                        }
                    } catch (Exception e) {
                        resposta = "ERRO Valor inválido";
                    }
                    break;

                case "DEPOSITO":
                    if (contaLogada == null) {
                        resposta = "ERRO Faça login primeiro";
                        break;
                    }
                    try {
                        double valor = Double.parseDouble(partes[1].replace(",", "."));
                        if (valor <= 0) {
                            resposta = "ERRO Valor inválido";
                        } else {
                            contaLogada.saldo += valor;
                            resposta = "OK Depósito feito. Saldo: R$ " + String.format("%.2f", contaLogada.saldo);
                        }
                    } catch (Exception e) {
                        resposta = "ERRO Valor inválido";
                    }
                    break;

                case "SAIR":
                    resposta = "OK Até logo";
                    break;

                default:
                    resposta = "ERRO Comando desconhecido";
            }

            out.writeUTF(resposta);
            out.flush();
        }

        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }
}