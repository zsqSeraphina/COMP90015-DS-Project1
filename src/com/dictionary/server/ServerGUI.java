package com.dictionary.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class ServerGUI {
    private JTextArea actionLog;
    private JTextField portText;
    private DictionaryServer dictionaryServer = new DictionaryServer();

    public void initialise() {
        JFrame frame = new JFrame("Dictionary Server");

        JPanel logPanel = new JPanel();
        logPanel.setBorder(BorderFactory.createEmptyBorder(15, 100, 80, 150));

        JLabel logLabel = new JLabel("Action log");
        logLabel.setFont(new Font(null, Font.PLAIN, 20));

        actionLog = new JTextArea(16, 58);
        actionLog.setFont(new Font(null, Font.PLAIN, 12));
        actionLog.setLineWrap(true);
        actionLog.setWrapStyleWord(true);
        actionLog.setEnabled(false);
        actionLog.setDisabledTextColor(new Color(0, 0, 0));

        JScrollPane scroll = new JScrollPane(actionLog);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        logPanel.add(logLabel);
        logPanel.add(scroll);
        frame.add(logPanel);

        JPanel portPanel = new JPanel();
        portPanel.setBorder(BorderFactory.createEmptyBorder(80, 100, 15, 50));
        JLabel portLabel = new JLabel("Server is running on port");
        portLabel.setFont(new Font(null, Font.PLAIN, 20));
        portText = new JTextField(48);
        portText.setFont(new Font(null, Font.PLAIN, 18));
        portText.setHorizontalAlignment(0);
        portText.setPreferredSize(new Dimension(600, 65));
        portText.setEnabled(false);
        portText.setDisabledTextColor(new Color(0, 0, 0));
        portPanel.add(portLabel);
        portPanel.add(portText);

        frame.getContentPane().add(BorderLayout.SOUTH, logPanel);
        frame.getContentPane().add(BorderLayout.NORTH, portPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dictionaryServer.storeInDictionary();
            }
        });
    }

    public void setPort(String port) {
        portText.setText(port);
    }

    public void updateLog(String newLog) {
        actionLog.setText(actionLog.getText() + "\n" + newLog
                + "\n------------------------------------------------");
    }



}
