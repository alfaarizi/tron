/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author zizi
 */
public class MessageGUI {
    
    private final JFrame frame;
    private final JLabel messageLabel;
    private final JPanel buttonPanel;
    private final JButton backButton;
    
    public MessageGUI(String title, String message, Color color){
        frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(color);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> { 
            new MenuGUI();
            frame.dispose();
        });

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        
        frame.add(messageLabel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH); 

        frame.setVisible(true);
    }
}
