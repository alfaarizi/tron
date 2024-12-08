/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import javax.swing.JFrame;
import javax.swing.JButton;

/**
 * Represents the main menu GUI for the Tron game. 
 * Provides options to start the game or exit the application.
 * 
 * @author zizi
 */
public class MenuGUI {
    private final JFrame frame;
    private final JButton startButton, exitButton;
    
    /**
     * Constructs the main menu window with Start and Exit buttons.
     * Initializes the frame layout, buttons, and action listeners.
    */
    public MenuGUI (){
        frame = new JFrame("Tron - Main Menu");
        frame.setLayout(null);
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        startButton = new JButton("Start");
        startButton.setBounds(100, 100, 200, 50);
        startButton.addActionListener(e -> {
            new AuthenticationGUI();
            frame.dispose();
        });
        
        exitButton = new JButton("Exit");
        exitButton.setBounds(100, 200, 200, 50);
        exitButton.addActionListener(e -> { 
            frame.dispose();
            System.exit(0);
        });
        
        frame.add(startButton);
        frame.add(exitButton);
        
        frame.setVisible(true);
    }
}
