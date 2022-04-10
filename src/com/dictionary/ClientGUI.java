package com.dictionary;

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
        JButton delete = new JButton("Delete");
        delete.setPreferredSize(new Dimension(100, 35));
        inputText.add(query);
        inputText.add(delete);

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
                if(word.isBlank()) {
                    JOptionPane.showMessageDialog
                            (null, "Please enter a word!",
                                    "Action Invalid", JOptionPane.INFORMATION_MESSAGE);
                }
                DictRequest request = new DictRequest(RequestType.QUERY, word, "");
                String queryResult = client.query(request);
                meanField.setText(queryResult);
            }
        });
    }

}
