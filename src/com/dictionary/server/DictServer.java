package com.dictionary.server;

import com.dictionary.util.DictRequest;
import com.dictionary.util.DictResponse;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class DictServer {
    private static String dictionary;
    private static ServerGUI serverGUI;
    private static JSONObject dict;
    public static void main(String[] args) throws IOException {

        /* check the arguments and get the port and dictionary path */
        if (2 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter port number and dictionary file path");
        }
        int port;

        /* get the port and dictionary path from arguments */
        try {
            port = Integer.parseInt(args[0]);
            dictionary = args[1];
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e
                            + ", Please enter correct port number and dictionary file path");
        }

        /* load dictionary from file into memory */
        JSONParser parser = new JSONParser();
        try {
            dict = (JSONObject) parser.parse(new FileReader(dictionary));
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Error", JOptionPane.INFORMATION_MESSAGE);
        }

        serverGUI = new ServerGUI();
        serverGUI.initialise();
        serverGUI.setPort(String.valueOf(port));

        try {
            ServerSocket socket = new ServerSocket(port);
            ExecutorService executor = Executors.newFixedThreadPool(5);
            Socket client;

            /* thread per request */
            while((client = socket.accept()) != null) {
                InputStream inputStream = client.getInputStream();

                /* avoid conjunction of the thread-pool */
                synchronized(Thread.currentThread()) {
                    Thread.currentThread().wait(100);
                }

                /* only execute when there is request coming */
                if(inputStream.available() > 0) {
                    ClientServer clientServer = new ClientServer
                            (inputStream, client.getOutputStream());
                    executor.execute(clientServer);
                }
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static class ClientServer implements Runnable {
        InputStream inputStream;
        OutputStream outputStream;
        public ClientServer(InputStream inputStream, OutputStream outputStream)
                throws IOException, ClassNotFoundException {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        public void run() {

            try {

                ObjectOutputStream output
                        = new ObjectOutputStream(outputStream);
                ObjectInputStream input
                        = new ObjectInputStream(inputStream);
                DictRequest request = (DictRequest) input.readObject();

                DictResponse response = new DictResponse();

                /* handle the requests based on type */
                switch (request.getRequestType()) {
                    case QUERY -> {
                        response = handleQuery(request);
                        output.writeObject(response);
                        output.flush();
                    }
                    case ADD -> {
                        response = handleAdd(request);
                        output.writeObject(response);
                        output.flush();
                    }
                    case REMOVE -> {
                        response = handleRemove(request);
                        output.writeObject(response);
                        output.flush();
                    }
                    case UPDATE -> {
                        response = handleUpdate(request);
                        output.writeObject(response);
                        output.flush();
                    }
                    // all other types are unable to handle
                    default -> JOptionPane.showMessageDialog
                            (null, "Cannot resolve request type",
                                    "Request Failed", JOptionPane.INFORMATION_MESSAGE);
                }
                serverGUI.updateLog("Client connected on " +
                        Thread.currentThread().getName() +
                        "\nRequested for action: " + request.getRequestType().toString() +
                        "\nAction result: " + response.getMessage()
                        );
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog
                        (null, "Error: " + e,
                                "Request Failed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * handles the query request
     *
     * @param request DictRequest contains word to query
     * @return DictResponse contains word meaning and result of the action
     */
    private static DictResponse handleQuery(DictRequest request) {
        DictResponse response = new DictResponse();
        if(request.getWord().isBlank()) {
            response.setMessage("Failed, please enter a word!");
            return response;
        }
        String mean = (String) dict.get(request.getWord());
        if (mean == null) {
            response.setMessage("Failed, word does not exist in the dictionary!");
        } else {
            response.setResult(mean);
            response.setMessage("Success!");
            response.setSuccess(true);
        }
        return response;
    }

    /**
     * handles the add request
     *
     * @param request DictRequest contains word and meaning to add
     * @return DictResponse contains result of the action
     */
    private static DictResponse handleAdd(DictRequest request) {
        DictResponse response = new DictResponse();
        if (dict.get(request.getWord()) != null) {
            response.setMessage("Failed, word already exists in the dictionary!");
            return response;
        }

        if (request.getWord().isBlank() || request.getMean().isBlank()) {
            response.setMessage("Failed, please enter the word or meaning!");
            return response;
        }

        dict.put(request.getWord(), request.getMean());
        try {
            FileWriter writer = new FileWriter(dictionary);
            writer.append(dict.toJSONString());
            writer.flush();
            response.setSuccess(true);
            response.setMessage("Success!");
            return response;
        } catch (IOException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        return response;
    }

    /**
     * handles the remove request
     *
     * @param request DictRequest contains word to remove
     * @return DictResponse contains result of the action
     */
    private static DictResponse handleRemove(DictRequest request) {
        DictResponse response = new DictResponse();
        if(request.getWord().isBlank()) {
            response.setMessage("Failed, please enter a word!");
            return response;
        }
        if (dict.get(request.getWord()) == null) {
            response.setMessage("Failed, word does not exist in the dictionary!");
            return response;
        }
        dict.remove(request.getWord());
        try {
            FileWriter writer = new FileWriter(dictionary);
            writer.append(dict.toJSONString());
            writer.flush();
            response.setSuccess(true);
            response.setMessage("Success!");
            return response;
        } catch (IOException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        return response;
    }

    /**
     * handles the update request
     *
     * @param request DictRequest contains word and meaning to update
     * @return DictResponse contains result of the action
     */
    private static DictResponse handleUpdate(DictRequest request) {
        DictResponse response = new DictResponse();
        if (request.getWord().isBlank() || request.getMean().isBlank()) {
            response.setMessage("Failed, please enter the word or meaning!");
            return response;
        }
        if (dict.get(request.getWord()) == null) {
            response.setMessage("Failed, word does not exist in the dictionary!");
            return response;
        }
        dict.replace(request.getWord(), request.getMean());
        try {
            FileWriter writer = new FileWriter(dictionary);
            writer.append(dict.toJSONString());
            writer.flush();
            response.setSuccess(true);
            response.setMessage("Success!");
            return response;
        } catch (IOException e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e,
                            "Request Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        return response;
    }
}
