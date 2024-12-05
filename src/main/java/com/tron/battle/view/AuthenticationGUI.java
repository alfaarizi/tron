/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import com.tron.battle.model.Player;
import com.tron.battle.controller.TronBattle;
import com.tron.database.HighScoreDB;

import com.tron.database.entity.HighScoreEntity;
import com.tron.database.entity.PlayerEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author zizi
 */
public class AuthenticationGUI {

    private final JFrame frame;
    private final JTextField player1Username, player2Username;
    private final JPasswordField player1Password, player2Password;
    private final JComboBox<String> player1Color, player2Color;
    private final JLabel player1Status, player2Status;
    private final AtomicBoolean player1Ready, player2Ready;

    private static Player player1 = null;
    private static Player player2 = null;
    
    private HighScoreDB database;
    
    public AuthenticationGUI() {
        frame = new JFrame("Tron - Player Authentication");
        frame.setLayout(new GridLayout(1, 2, 10, 10));
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        player1Ready = new AtomicBoolean(false);
        player2Ready = new AtomicBoolean(false);

        JPanel player1Panel = createPlayerPanel("Player 1", player1Ready);
        player1Username = (JTextField) player1Panel.getComponent(0);
        player1Password = (JPasswordField) player1Panel.getComponent(1);
        player1Color = (JComboBox<String>) player1Panel.getComponent(3);
        player1Status = (JLabel) player1Panel.getComponent(5);

        JPanel player2Panel = createPlayerPanel("Player 2", player2Ready);
        player2Username = (JTextField) player2Panel.getComponent(0);
        player2Password = (JPasswordField) player2Panel.getComponent(1);
        player2Color = (JComboBox<String>) player2Panel.getComponent(3);
        player2Status = (JLabel) player2Panel.getComponent(5);

        frame.add(player1Panel);
        frame.add(player2Panel);

        try {
            this.database = TronBattle.getDatabase();
            frame.setVisible(true);
        } catch (IllegalStateException ignored){
            this.database = null;
            frame.setVisible(false);
            frame.dispose();
            new ErrorGUI("UNABLE TO RETRIEVE DATABASE!");
        }
        
    }
    
    private JPanel createPlayerPanel(String title,  AtomicBoolean readyFlag){
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JTextField usernameField = createTextField("Username");
        JPasswordField passwordField = createPasswordField("Password");
        JComboBox<String> colorBox = new JComboBox<>(new String[]{"Red", "Blue", "Green", "Yellow"});
        JButton startButton = new JButton("Start");
        JLabel statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);

        
        startButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Attempt to retrieve or create a player            
            Player player;
            
            PlayerEntity playerByNamePassword = database.getPlayer(username, password);
            if (playerByNamePassword == null) {
                
                PlayerEntity playerByName = database.getPlayer(username);
                if (playerByName == null){
                     player = new Player(username, password, Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
                } else {
                    player = new Player(playerByName.getName(), playerByName.getPasswordHash(), playerByName.getRegisterDate(), PlayerEntity.PasswordType.HASHED); 
                }
            
            } else {
                player = new Player(playerByNamePassword.getName(), playerByNamePassword.getPasswordHash(), playerByNamePassword.getRegisterDate(), PlayerEntity.PasswordType.HASHED);
            }
            
            
            if (player.checkPassword(password)) {
                if (title.equals("Player 1")) player1 = player;
                if (title.equals("Player 2")) player2 = player;
                
                readyFlag.set(true); 
                String message = playerByNamePassword == null ? "New Player - Ready" : "Registered Player - Ready";
                statusLabel.setText(message);
                
                usernameField.setEditable(false);
                usernameField.setFocusable(false);
                passwordField.setEditable(false);
                passwordField.setFocusable(false);
                colorBox.setEnabled(false);
                startButton.setEnabled(false);
                                
                this.database.putHighScore(new HighScoreEntity(player, 1));
                
                checkReady();
            } else {
                statusLabel.setText("Registered Player - Invalid Credentials");
            }
        });
        
        
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(new JLabel("Choose Color:"));
        panel.add(colorBox);
        panel.add(startButton);
        panel.add(statusLabel);

        return panel;
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        setPlaceholder(textField, placeholder);
        return textField;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        setPlaceholder(passwordField, placeholder);
        return passwordField;
    }

    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void setPlaceholder(JPasswordField passwordField, String placeholder) {
        passwordField.setEchoChar((char) 0);
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });
    }

    private void checkReady() {
        if (player1Ready.get() && player2Ready.get()) {                    
            if (player1 != null && player2 != null){
                Timer timer = new Timer(1500, e -> {
                    new TronBattleGUI(player1, player2); 
                    frame.dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
}