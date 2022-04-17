package com.dictionary.client;

import com.dictionary.utils.DictRequest;
import com.dictionary.utils.DictResponse;
import com.dictionary.utils.RequestType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class ClientGUI {

    DictionaryClient client = new DictionaryClient();
    ArrayList<JTextArea> meanList = new ArrayList<>();

    public void initialise() {

        JFrame frame = new JFrame("Dictionary Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBounds(120, 200, 1000, 600);

        JPanel inputText = new JPanel();
        inputText.setBorder(BorderFactory.createEmptyBorder(50, 90, 10, 120));
        JTextField wordField = new JTextField(60);
        JLabel wordLabel = new JLabel("Word: ");
        inputText.add(wordLabel);
        inputText.add(wordField);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 5, 5));
        buttonPanel.setLocation(10, 50);

        JButton query = new JButton("Query");
        query.setPreferredSize(new Dimension(100, 35));
        JButton remove = new JButton("Remove");
        remove.setPreferredSize(new Dimension(100, 35));
        JButton add = new JButton("Add");
        add.setPreferredSize(new Dimension(100, 35));
        JButton update = new JButton("Update");
        update.setPreferredSize(new Dimension(100, 35));
        JButton addMean = new JButton("Add Mean");
        addMean.setPreferredSize(new Dimension(100, 35));
        JButton cleanBox = new JButton("Clean Box");
        cleanBox.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(query);
        buttonPanel.add(remove);
        buttonPanel.add(add);
        buttonPanel.add(update);
        inputText.add(buttonPanel);

        JPanel meanText = new JPanel();
        meanText.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 100));
        JPanel meanPanel = new JPanel();
        meanPanel.setLayout(new GridLayout(1, 1));
        JTextArea meanArea = new JTextArea(3, 60);
        meanArea.setLineWrap(true);
        meanArea.setWrapStyleWord(true);
        meanList.add(meanArea);
        JLabel meanLabel = new JLabel("Mean: ");
        JScrollPane scroll = new JScrollPane(meanArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        meanText.add(meanLabel);
        meanPanel.add(scroll);
        meanText.add(meanPanel);
        meanText.add(addMean);
        meanText.add(cleanBox);

        JPanel outputText = new JPanel();
        outputText.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 250));
        JTextArea outputArea= new JTextArea(5, 62);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JLabel outputLabel = new JLabel("Output: ");
        outputText.add(outputLabel);
        outputText.add(outputArea);

        inputText.setBounds(120, 100, 600, 150);
        container.add(inputText);
        meanText.setBounds(120, 180, 800, 360);
        container.add(meanText);
        outputText.setBounds(120, 240, 600, 200);
        container.add(outputText);
        JScrollPane myJScrollPane = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(myJScrollPane);
        frame.pack();
        frame.setVisible(true);

        cleanBox.addActionListener(e -> {
            ArrayList<JTextArea> removeList = new ArrayList<>();
            if (meanList.size() > 1) {
                for (JTextArea area : meanList) {
                    if (area.getText().isBlank()) {
                        removeList.add(area);
                    }
                }
                for (Component component : meanPanel.getComponents()) {
                    meanPanel.remove(component);
                }
                // remove after loop to avoid ConcurrentModificationException
                meanList.removeAll(removeList);
                if (meanList.isEmpty()) {
                    JTextArea meanArea1 = new JTextArea(3, 60);
                    meanArea1.setLineWrap(true);
                    meanArea1.setWrapStyleWord(true);
                    JScrollPane scroll1 = new JScrollPane(meanArea1);
                    scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    meanPanel.setLayout(new GridLayout(1, 1));
                    meanPanel.add(scroll1);
                } else {
                    for (JTextArea area : meanList) {
                        JScrollPane scroll1 = new JScrollPane(area);
                        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        meanPanel.setLayout(new GridLayout(meanList.size() + 1, 1));
                        meanPanel.add(scroll1);
                    }
                }
                meanPanel.revalidate();
                meanPanel.repaint();

            }
        });

        addMean.addActionListener(e -> {
            JTextArea meanArea1 = new JTextArea(3, 60);
            meanArea1.setLineWrap(true);
            meanArea1.setWrapStyleWord(true);
            JScrollPane scroll1 = new JScrollPane(meanArea1);
            scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            meanList.add(meanArea1);
            meanPanel.setLayout(new GridLayout(meanList.size() + 1, 1));
            meanPanel.add(scroll1);
            SwingUtilities.updateComponentTreeUI(frame);
        });

        /* add action to the query button,
        and send the query request formatted from the client's input */
        query.addActionListener(e -> {
            String word = wordField.getText();
            DictRequest request = new DictRequest(RequestType.QUERY, word, new ArrayList<>(1));
            DictResponse response = client.sendRequest(request);
            List<String> resultMean = response.getResult();
            if (resultMean != null) {
                if (resultMean.size() > 1) {
                    for (int i = 0; i < resultMean.size(); i++) {
                        if (i < meanList.size()) {
                            meanList.get(i).setText(resultMean.get(i));
                        } else {
                            JTextArea meanArea1 = new JTextArea(3, 60);
                            meanArea1.setLineWrap(true);
                            meanArea1.setWrapStyleWord(true);
                            meanArea1.setText(resultMean.get(i));
                            meanList.add(meanArea1);
                            JScrollPane scroll1 = new JScrollPane(meanArea1);
                            scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                            meanPanel.setLayout(new GridLayout(meanList.size() + 1, 1));
                            meanPanel.add(scroll1);
                        }
                    }
                } else {
                    meanArea.setText(response.getResult().get(0));
                }
            }
            outputArea.setText(response.getMessage());
        });

        /* add action to the add button,
        and send the query request formatted from the client's input */
        add.addActionListener(e -> {
            String word = wordField.getText();
            ArrayList<String> meanStrings = new ArrayList<>(meanList.size());
            for (JTextArea meaning : meanList) {
                meanStrings.add(meaning.getText());
            }
            DictRequest request = new DictRequest(RequestType.ADD, word, meanStrings);
            DictResponse response = client.sendRequest(request);
            outputArea.setText(response.getMessage());
        });

        /* add action to the remove button,
        and send the query request formatted from the client's input */
        remove.addActionListener(e -> {
            String word = wordField.getText();
            DictRequest request = new DictRequest(RequestType.REMOVE, word, new ArrayList<>(1));
            DictResponse response = client.sendRequest(request);
            outputArea.setText(response.getMessage());
        });

        /* add action to the update button,
        and send the query request formatted from the client's input */
        update.addActionListener(e -> {
            String word = wordField.getText();
            ArrayList<String> meanStrings = new ArrayList<>(meanList.size());
            for (JTextArea meaning : meanList) {
                meanStrings.add(meaning.getText());
            }
            DictRequest request = new DictRequest(RequestType.UPDATE, word, meanStrings);
            DictResponse response = client.sendRequest(request);
            outputArea.setText(response.getMessage());
        });
    }

}
