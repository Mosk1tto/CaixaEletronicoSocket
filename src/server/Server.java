package server;

import java.io.*;
import java.net.*;

public class Server {

    public void run() throws Exception {

        // Passo 1 - Criar o socket servidor na porta 1024
        ServerSocket serverSocket = new ServerSocket(1024);
        System.out.println("Servidor aguardando conexão ...");

        // Passo 2 - Aguardar uma conexão (bloqueia até um cliente conectar)
        Socket socket = serverSocket.accept();
        System.out.println("Conexao Recebida de "
                + socket.getInetAddress().getHostName());

        // Passo 3 - Obter os fluxos de entrada e saída
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Passo 4 - Comunicação com o cliente
        String comando = "";
        while (!comando.equalsIgnoreCase("SAIR")) {
            try {
                comando = in.readUTF();
            } catch (EOFException e) {
                System.out.println("Cliente desconectou.");
                break;
            }
            System.out.println("Comando recebido: " + comando);

            if (comando.equalsIgnoreCase("SAIR")) {
                out.writeUTF("Encerrando conexao. Até logo!");
            } else {
                out.writeUTF("Servidor recebeu: " + comando);
            }
            out.flush();
        }

        // Passo 5 - Fechar fluxos e socket do cliente
        in.close();
        out.close();
        socket.close();

        // Passo 6 - Fechar o socket servidor
        serverSocket.close();
    }
}