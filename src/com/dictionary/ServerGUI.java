package com.dictionary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class ServerGUI {
    private JTextField textField;

    public ServerGUI() {


    }

    public void initialise() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(200, 300, 200, 300));
        panel.setLayout(new GridLayout(0, 1));

        textField = new JTextField(16);
        textField.setSize(100, 50);
        textField.setToolTipText("Please enter a word");
        panel.add(textField);

        JButton button = new JButton("enquire");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if (command.equals("enquire")) {
                    System.out.println(textField.getText());
                }
            }
        });
        button.setSize(100, 50);
        panel.add(button);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Dictionary server");
        frame.setSize(300, 200);
        frame.pack();
        frame.setVisible(true);
    }

}
