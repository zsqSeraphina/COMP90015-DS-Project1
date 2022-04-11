package com.dictionary.client;

import com.dictionary.util.DictRequest;
import com.dictionary.util.RequestType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {

    DictClient client = new DictClient();

    public void initialise() {

        JFrame frame = new JFrame("Dictionary Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel inputText = new JPanel();
        inputText.setBorder(BorderFactory.createEmptyBorder(200, 220, 10, 220));
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
        outputText.setBorder(BorderFactory.createEmptyBorder(10, 220, 200, 220));
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

        query.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();

                DictRequest request = new DictRequest(RequestType.QUERY, word, "");
                String queryResult = client.sendRequest(request);
                meanField.setText(queryResult);
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                String mean = meanField.getText();
                DictRequest request = new DictRequest(RequestType.ADD, word, mean);
                client.sendRequest(request);
            }
        });

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                DictRequest request = new DictRequest(RequestType.REMOVE, word, "");
                client.sendRequest(request);
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                String mean = meanField.getText();
                DictRequest request = new DictRequest(RequestType.UPDATE, word, mean);
                client.sendRequest(request);
            }
        });
    }

}
