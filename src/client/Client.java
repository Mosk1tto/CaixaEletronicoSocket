package client;

import java.io.*;
import java.net.*;

public class Client {

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