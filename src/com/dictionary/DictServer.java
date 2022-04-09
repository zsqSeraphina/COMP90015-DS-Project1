package com.dictionary;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class DictServer {

    public static void main(String[] args) throws IOException {

        // check the arguments and get the port and dictionary path
        if (2 != args.length) {
            throw new IllegalArgumentException("Please enter port number and dictionary file path");
        }
        int port;
        String dictionary;

        try {
            port = Integer.parseInt(args[0]);
            dictionary = args[1];
        } catch(Exception e) {
            throw new IllegalArgumentException("Error:" + e + ", Please enter correct port number and dictionary file path");
        }

        ServerGUI sg = new ServerGUI();
        //sg.initialise();

        try {
            ServerSocket socket = new ServerSocket(port);
            while(true) {
                Socket client = socket.accept();
                System.out.println("Client" + client.getInetAddress().getHostName() + "connected");
                ClientServer clientServer = new ClientServer(client);
                new Thread(clientServer).start();
            }
        } catch (IOException e) {
            throw new IOException("Error:" + e + "Please try again later");
        }
    }

    private static class ClientServer implements Runnable {
        private final Socket clientSocket;

        public ClientServer(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
                String line;
                while((line = input.readLine()) != null) {
                    System.out.println(line);
                    output.println("received");
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
