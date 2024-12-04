/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import com.tron.battle.view.TronBattleGUI;

import javax.swing.JFrame;
import javax.swing.JButton;

/**
 *
 * @author zizi
 */


public class MenuGUI {
    private final JFrame frame;
    private final JButton startButton, exitButton;
    
    public MenuGUI (){
        frame = new JFrame("Tron - Main Menu");
        
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
        
        frame.setLayout(null);
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        frame.setVisible(true);
    }
}
