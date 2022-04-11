package com.dictionary.server;

import com.dictionary.util.DictRequest;
import com.dictionary.util.DictResponse;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
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

                switch (request.getRequestType()) {
                    case QUERY -> {
                        DictResponse queryResult = handleQuery(request);
                        output.writeObject(queryResult);
                        output.flush();
                    }
                    case ADD -> {
                        DictResponse addResult = handleAdd(request);
                        output.writeObject(addResult);
                        output.flush();
                    }
                    case REMOVE -> {
                        DictResponse removeResult = handleRemove(request);
                        output.writeObject(removeResult);
                        output.flush();
                    }
                    case UPDATE -> {
                        DictResponse handleUpdate = handleUpdate(request);
                        output.writeObject(handleUpdate);
                        output.flush();
                    }
                    default -> JOptionPane.showMessageDialog
                            (null, "Cannot resolve request type",
                                    "Request Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException | ClassNotFoundException | ParseException e) {
                JOptionPane.showMessageDialog
                        (null, "Error: " + e,
                                "Request Failed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static JSONObject getDict() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new FileReader(dictionary));
    }

    private static DictResponse handleQuery(DictRequest request) throws IOException, ParseException {
        DictResponse response = new DictResponse();
        if(request.getWord().isBlank()) {
            response.setErrorMessage("Please enter a word!");
            return response;
        }
        String mean = (String) getDict().get(request.getWord());
        if (mean == null) {
            response.setErrorMessage("Failed, word does not exist in the dictionary!");
        } else {
            response.setResult(mean);
            response.setSuccess(true);
        }
        return response;
    }
    private static DictResponse handleAdd(DictRequest request) throws IOException, ParseException {
        DictResponse response = new DictResponse();
        JSONObject dict = getDict();
        if (dict.get(request.getWord()) != null) {
            response.setErrorMessage("Failed, word already exists in the dictionary!");
            return response;
        }

        if (request.getWord().isBlank() || request.getMean().isBlank()) {
            response.setErrorMessage("Failed, please enter word and meaning!");
            return response;
        }

        dict.put(request.getWord(), request.getMean());
        try {
            FileWriter writer = new FileWriter(dictionary);
            writer.append(dict.toJSONString());
            writer.flush();
            response.setSuccess(true);
            return response;
        } catch (IOException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        return response;
    }

    private static DictResponse handleRemove(DictRequest request) throws IOException, ParseException {
        DictResponse response = new DictResponse();
        if(request.getWord().isBlank()) {
            response.setErrorMessage("Please enter a word!");
            return response;
        }
        JSONObject dict = getDict();
        if (dict.get(request.getWord()) == null) {
            response.setErrorMessage("Failed, word does not exist in the dictionary!");
            return response;
        }
        dict.remove(request.getWord());
        try {
            FileWriter writer = new FileWriter(dictionary);
            writer.append(dict.toJSONString());
            writer.flush();
            response.setSuccess(true);
            return response;
        } catch (IOException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        return response;
    }

    private static DictResponse handleUpdate(DictRequest request) throws IOException, ParseException {
        DictResponse response = new DictResponse();
        if (request.getWord().isBlank() || request.getMean().isBlank()) {
            response.setErrorMessage("Failed, please enter word and meaning!");
            return response;
        }
        JSONObject dict = getDict();
        if (dict.get(request.getWord()) == null) {
            response.setErrorMessage("Failed, word does not exist in the dictionary!");
            return response;
        }
        dict.replace(request.getWord(), request.getMean());
        try {
            FileWriter writer = new FileWriter(dictionary);
            writer.append(dict.toJSONString());
            writer.flush();
            response.setSuccess(true);
            return response;
        } catch (IOException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        return response;
    }
}
