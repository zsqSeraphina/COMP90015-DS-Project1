package com.dictionary.client;

import com.dictionary.util.DictRequest;
import com.dictionary.util.RequestType;

import javax.swing.*;
import java.awt.*;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class ClientGUI {

    DictClient client = new DictClient();

    public void initialise() {

        JFrame frame = new JFrame("Dictionary Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));

        JPanel inputText = new JPanel();
        inputText.setBorder(BorderFactory.createEmptyBorder(160, 100, 10, 120));
        JTextField wordField = new JTextField();
        wordField.setPreferredSize(new Dimension(500, 35));
        JLabel wordLabel = new JLabel("Word: ");
        inputText.add(wordLabel);
        inputText.add(wordField);
        JButton query = new JButton("Query");
        query.setPreferredSize(new Dimension(100, 35));
        JButton remove = new JButton("Remove");
        remove.setPreferredSize(new Dimension(100, 35));
        inputText.add(query);
        inputText.add(remove);

        JPanel outputText = new JPanel();
        outputText.setBorder(BorderFactory.createEmptyBorder(10, 100, 100, 120));
        JTextArea meanField = new JTextArea();
        meanField.setPreferredSize(new Dimension(500, 100));
        meanField.setLineWrap(true);
        JLabel meanLabel = new JLabel("Mean: ");
        outputText.add(meanLabel);
        outputText.add(meanField);
        JButton add = new JButton("Add");
        add.setPreferredSize(new Dimension(100, 35));
        JButton update = new JButton("Update");
        update.setPreferredSize(new Dimension(100, 35));
        outputText.add(add);
        outputText.add(update);

        frame.getContentPane().add(BorderLayout.NORTH, inputText);
        frame.getContentPane().add(BorderLayout.CENTER, outputText);
        frame.pack();
        frame.setVisible(true);

        /* add action to the query button,
        and send the query request formatted from the client's input */
        query.addActionListener(e -> {
            String word = wordField.getText();
            DictRequest request = new DictRequest(RequestType.QUERY, word, "");
            String queryResult = client.sendRequest(request);
            meanField.setText(queryResult);
        });

        /* add action to the add button,
        and send the query request formatted from the client's input */
        add.addActionListener(e -> {
            String word = wordField.getText();
            String mean = meanField.getText();
            DictRequest request = new DictRequest(RequestType.ADD, word, mean);
            client.sendRequest(request);
        });

        /* add action to the remove button,
        and send the query request formatted from the client's input */
        remove.addActionListener(e -> {
            String word = wordField.getText();
            DictRequest request = new DictRequest(RequestType.REMOVE, word, "");
            client.sendRequest(request);
        });

        /* add action to the update button,
        and send the query request formatted from the client's input */
        update.addActionListener(e -> {
            String word = wordField.getText();
            String mean = meanField.getText();
            DictRequest request = new DictRequest(RequestType.UPDATE, word, mean);
            client.sendRequest(request);
        });
    }

}
