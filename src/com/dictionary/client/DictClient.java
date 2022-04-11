package com.dictionary.client;

import com.dictionary.util.DictRequest;
import com.dictionary.util.DictResponse;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class DictClient {
    private static String address;
    private static int port;
    public static void main(String[] args) {
        if (2 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter server address and port number");
        }
        try {
            address = args[0];
            port = Integer.parseInt(args[1]);
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e + ", Please enter correct server address and port number");
        }

        ClientGUI clientGUI = new ClientGUI();
        clientGUI.initialise();


    }

    public String sendRequest(DictRequest request) {
        String result = "";
        try {
            Socket socket = new Socket(address, port);

            ObjectOutputStream output
                    = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input
                    = new ObjectInputStream(socket.getInputStream());

            output.writeObject(request);
            output.flush();

            DictResponse response = (DictResponse) input.readObject();
            if (response.getSuccess()) {
                result = response.getResult();
                JOptionPane.showMessageDialog
                        (null, "Success!",
                                "Action Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog
                        (null, response.getErrorMessage(),
                                "Action Invalid", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog
                    (null, "Error: " + e + ", please try again",
                            "Action Invalid", JOptionPane.INFORMATION_MESSAGE);
        }
        return result;
    }
}