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
        int k = in.readInt();
        String s = in.readUTF();
        System.out.println("Int: " + k);
        System.out.println("String: " + s);
        out.writeInt(1);
        out.writeUTF("Olá sou uma msg do servidor!");

        // Passo 5 - Fechar fluxos e socket do cliente
        in.close();
        out.close();
        socket.close();

        // Passo 6 - Fechar o socket servidor
        serverSocket.close();
    }
}