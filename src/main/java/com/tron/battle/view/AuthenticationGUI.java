/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author zizi
 */
public class AuthenticationGUI {
    private final JFrame frame;
    private final JTextField player1Username, player1Password, player2Username, player2Password;
    private final JButton player1Start, player2Start;
    private final JComboBox<String> player1Color, player2Color;
    private final AtomicBoolean player1Ready, player2Ready;

    public AuthenticationGUI() {
        frame = new JFrame("Player Authentication");
        frame.setLayout(new GridLayout(1, 2, 10, 10));
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel player1Panel = new JPanel();
        player1Panel.setLayout(new GridLayout(6, 1, 10, 10));
        player1Panel.setBorder(BorderFactory.createTitledBorder("Player 1"));

        player1Username = new JTextField("Username");
        player1Password = new JPasswordField("Password");
        player1Color = new JComboBox<>(new String[]{"Red", "Blue", "Green", "Yellow"});
        player1Start = new JButton("Start");
        player1Ready = new AtomicBoolean(false);

        player1Panel.add(player1Username);
        player1Panel.add(player1Password);
        player1Panel.add(new JLabel("Choose Color:"));
        player1Panel.add(player1Color);
        player1Panel.add(player1Start);

        JPanel player2Panel = new JPanel();
        player2Panel.setLayout(new GridLayout(6, 1, 10, 10));
        player2Panel.setBorder(BorderFactory.createTitledBorder("Player 2"));

        player2Username = new JTextField("Username");
        player2Password = new JPasswordField("Password");
        player2Color = new JComboBox<>(new String[]{"Red", "Blue", "Green", "Yellow"});
        player2Start = new JButton("Start");
        player2Ready = new AtomicBoolean(false);

        player2Panel.add(player2Username);
        player2Panel.add(player2Password);
        player2Panel.add(new JLabel("Choose Color:"));
        player2Panel.add(player2Color);
        player2Panel.add(player2Start);

        frame.add(player1Panel);
        frame.add(player2Panel);

        player1Start.addActionListener(e -> {
            player1Ready.set(true);
            checkReady();
        });

        player2Start.addActionListener(e -> {
            player2Ready.set(true);
            checkReady();
        });

        frame.setVisible(true);
    }

    private void checkReady() {
        if (player1Ready.get() && player2Ready.get()) {
            String player1Data = "Player 1 - Username: " + player1Username.getText() +
                    ", Color: " + player1Color.getSelectedItem();
            String player2Data = "Player 2 - Username: " + player2Username.getText() +
                    ", Color: " + player2Color.getSelectedItem();

            System.out.println(player1Data);
            System.out.println(player2Data);

            frame.dispose();
            new TronBattleGUI(); // Pass player data if needed
        }
    }
}