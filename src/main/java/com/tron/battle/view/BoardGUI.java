/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import com.tron.database.entity.PlayerEntity;
import com.tron.battle.model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author zizi
 */
public class BoardGUI {
    // a,k.a BoardGUI (buttons, board, boardPanel, turnLabel, timeLabel, startTime, timer)
    private JFrame frame;
    private Board board;

    public BoardGUI(PlayerEntity player1, Color player1Color, PlayerEntity player2, Color player2Color) {
        
        frame = new JFrame("Tron Game");
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        board = new Board(this, player1, player1Color, player2, player2Color);
        board.setBackground(Color.BLACK);

        frame.add(board);
        frame.setVisible(true);
        board.requestFocusInWindow();

        board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                board.keyPressed(e);
            }
        });
    }
    
    public void reset(){
        this.board = null;
        this.frame.dispose();
    }
    
}
