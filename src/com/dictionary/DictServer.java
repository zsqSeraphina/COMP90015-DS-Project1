package com.dictionary;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.util.Scanner;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class DictServer {
    private static String dictionary;
    public static void main(String[] args) throws IOException {

        // check the arguments and get the port and dictionary path
        if (2 != args.length) {
            throw new IllegalArgumentException("Please enter port number and dictionary file path");
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
            dictionary = args[1];
        } catch(Exception e) {
            throw new IllegalArgumentException("Error:" + e + ", Please enter correct port number and dictionary file path");
        }

        ServerGUI serverGUI = new ServerGUI();
        serverGUI.initialise();

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
                ObjectInputStream input
                        = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output
                        = new ObjectOutputStream(clientSocket.getOutputStream());
                    DictRequest request = (DictRequest) input.readObject();

                switch(request.getRequestType()) {
                    case QUERY:
                        DictResponse queryResult = handleQuery(request);
                        output.writeObject(queryResult);
                        output.flush();
                        break;
                    case ADD:
                        break;
                    case DELETE:
                        break;
                    case UPDATE:
                        break;
                    default:
                        JOptionPane.showMessageDialog
                                (null, "Cannot resolve request type",
                                        "Request Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog
                        (null, "Error: " + e + "Please try again later",
                                "Request Failed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static DictResponse handleQuery(DictRequest request) throws FileNotFoundException {
        Scanner read = new Scanner(new File(dictionary));
        String line;
        DictResponse response = new DictResponse();
        while (read.hasNext()) {
            line = read.nextLine();
            System.out.println(line);
            String[] words = line.split(":");
            if (words[0].equals(request.getWord())) {
                response.setResult(words[1]);
                response.setSuccess(true);
            }
        }
        if (!response.getSuccess()) {
            response.setErrorMessage("Query failed, word does not exist!");
        }
        return response;
    }
}
