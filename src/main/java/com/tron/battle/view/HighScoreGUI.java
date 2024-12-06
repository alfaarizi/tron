/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import com.tron.database.entity.HighScoreEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author zizi
 */
public class HighScoreGUI extends JFrame {

    private final JFrame frame;
    private final JPanel highScorePanel;
    private final JLabel header;
    private JLabel nameLabel;
    private JLabel scoreLabel;
    private final JScrollPane scrollPane;
    private final JPanel buttonPanel;
    private final JButton backButton;

    public HighScoreGUI(String title, List<HighScoreEntity> highScores, Color color) {
        frame = new JFrame(title);
        frame.setSize(400, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        highScorePanel = new JPanel();
        highScorePanel.setLayout(new GridLayout(highScores.size() + 1, 2));
        highScorePanel.setBackground(Color.BLACK);
        highScorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        header = new JLabel("High Scores", SwingConstants.CENTER);
        header.setForeground(color);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        highScorePanel.add(header);
        highScorePanel.add(new JLabel()); 

        for (HighScoreEntity score : highScores) {
            nameLabel = new JLabel(score.getPlayer().getName(), SwingConstants.LEFT); 
            nameLabel.setForeground(color);
            nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 60, 5, 10));  
            scoreLabel = new JLabel(String.valueOf(score.getScore()), SwingConstants.RIGHT); 
            scoreLabel.setForeground(color);
            scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 60));

            highScorePanel.add(nameLabel);
            highScorePanel.add(scoreLabel);
        }

        scrollPane = new JScrollPane(highScorePanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> {
            new MenuGUI(); 
            frame.dispose();
        });
        buttonPanel.add(backButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
