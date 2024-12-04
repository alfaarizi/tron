/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import com.tron.battle.view.BoardGUI;

import javax.swing.JFrame;

/**
 *
 * @author zizi
 */
public class TronBattleGUI {
    private final JFrame frame;
//    private BoardGUI boardGUI;
    
    public TronBattleGUI() {
        frame = new JFrame("Tron");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        
        
    }
    
}
