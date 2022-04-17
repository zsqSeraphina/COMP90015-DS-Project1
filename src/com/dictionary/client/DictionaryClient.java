package com.dictionary.client;

import com.dictionary.utils.DictRequest;
import com.dictionary.utils.DictResponse;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class DictionaryClient {
    private static String address;
    private static int port;
    public static void main(String[] args) {
        if (2 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter server address and port number");
        }

        /* get the address and port from arguments */
        try {
            address = args[0];
            port = Integer.parseInt(args[1]);
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e + ", Please enter correct server address and port number");
        }

        /* create the client GUI */
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.initialise();


    }

    /**
     * a common method sends all kinds of requests to the server side
     *
     * @param request DictRequest contains all required information
     * @return result of the request
     */
    public DictResponse sendRequest(DictRequest request) {
        DictResponse response = null;
        try {

            /* use TCP/IP socket */
            Socket socket = new Socket(address, port);

            ObjectOutputStream output
                    = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input
                    = new ObjectInputStream(socket.getInputStream());

            output.writeObject(request);
            output.flush();

            response = (DictResponse) input.readObject();

            /* informs the client whether the action is succeeded */
            if (!response.getSuccess()) {
                JOptionPane.showMessageDialog
                        (null, response.getMessage(),
                                "Action Invalid", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e + ", please try again",
                            "Action Invalid", JOptionPane.INFORMATION_MESSAGE);
        }
        return response;
    }
}
