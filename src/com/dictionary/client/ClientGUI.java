package com.dictionary.client;

import com.dictionary.utils.DictRequest;
import com.dictionary.utils.DictResponse;
import com.dictionary.utils.RequestType;

import javax.swing.*;
import java.awt.*;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class ClientGUI {

    DictionaryClient client = new DictionaryClient();

    public void initialise() {

        JFrame frame = new JFrame("Dictionary Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));

        JPanel inputText = new JPanel();
        inputText.setBorder(BorderFactory.createEmptyBorder(160, 50, 10, 50));
        JTextField wordField = new JTextField(60);
        JLabel wordLabel = new JLabel("Word: ");
        inputText.add(wordLabel);
        inputText.add(wordField);

        JButton query = new JButton("Query");
        query.setPreferredSize(new Dimension(100, 35));
        JButton remove = new JButton("Remove");
        remove.setPreferredSize(new Dimension(100, 35));
        inputText.add(query);
        inputText.add(remove);

        JPanel meanText = new JPanel();
        meanText.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        JTextArea meanArea = new JTextArea(10, 60);
        meanArea.setLineWrap(true);
        meanArea.setWrapStyleWord(true);
        JLabel meanLabel = new JLabel("Mean: ");
        JScrollPane scroll = new JScrollPane(meanArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        meanText.add(meanLabel);
        meanText.add(scroll);

        JButton add = new JButton("Add");
        add.setPreferredSize(new Dimension(100, 35));
        JButton update = new JButton("Update");
        update.setPreferredSize(new Dimension(100, 35));
        meanText.add(add);
        meanText.add(update);

        JPanel outputText = new JPanel();
        outputText.setBorder(BorderFactory.createEmptyBorder(10, 20, 100, 240));
        JTextArea outputArea= new JTextArea(5, 62);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JLabel outputLabel = new JLabel("Output: ");
        outputText.add(outputLabel);
        outputText.add(outputArea);

        frame.getContentPane().add(BorderLayout.NORTH, inputText);
        frame.getContentPane().add(BorderLayout.CENTER, meanText);
        frame.getContentPane().add(BorderLayout.SOUTH, outputText);
        frame.pack();
        frame.setVisible(true);

        /* add action to the query button,
        and send the query request formatted from the client's input */
        query.addActionListener(e -> {
            String word = wordField.getText();
            DictRequest request = new DictRequest(RequestType.QUERY, word, "");
            DictResponse response = client.sendRequest(request);
            meanArea.setText(response.getResult());
            outputArea.setText(response.getMessage());
        });

        /* add action to the add button,
        and send the query request formatted from the client's input */
        add.addActionListener(e -> {
            String word = wordField.getText();
            String mean = meanArea.getText();
            DictRequest request = new DictRequest(RequestType.ADD, word, mean);
            DictResponse response = client.sendRequest(request);
            meanArea.setText("");
            outputArea.setText(response.getMessage());
        });

        /* add action to the remove button,
        and send the query request formatted from the client's input */
        remove.addActionListener(e -> {
            String word = wordField.getText();
            DictRequest request = new DictRequest(RequestType.REMOVE, word, "");
            DictResponse response = client.sendRequest(request);
            meanArea.setText("");
            outputArea.setText(response.getMessage());
        });

        /* add action to the update button,
        and send the query request formatted from the client's input */
        update.addActionListener(e -> {
            String word = wordField.getText();
            String mean = meanArea.getText();
            DictRequest request = new DictRequest(RequestType.UPDATE, word, mean);
            DictResponse response = client.sendRequest(request);
            meanArea.setText("");
            outputArea.setText(response.getMessage());
        });
    }

}
