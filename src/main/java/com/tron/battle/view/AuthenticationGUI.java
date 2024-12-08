/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.view;

import com.tron.battle.controller.TronBattle;
import com.tron.database.HighScoreDB;
import com.tron.database.entity.PlayerEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles player authentication and game setup for the Tron game.
 * It allows players to log in with their credentials, choose their colors, 
 * and ensures they are ready to start the game.
 * 
 * The window includes:
 * - Username and password fields for each player.
 * - Color selection for each player.
 * - A button to start the game once both players are ready.
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

    private static PlayerEntity player1 = null;
    private static PlayerEntity player2 = null;
    
    private HighScoreDB database;
    
    /**
     * Constructs the Authentication GUI, initializing player panels, 
     * and establishing database connections. Displays the window 
     * for user authentication and game setup.
    */
    public AuthenticationGUI() {
        frame = new JFrame("Tron - Player Authentication");
        frame.setLayout(new GridLayout(1, 2, 10, 10));
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
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
        
        
        addEscapeKeyListenerToComponents(
            player1Username, 
            player1Password, 
            player2Username, 
            player2Password, 
            player1Color, 
            player2Color, 
            frame
        );

        try {
            this.database = TronBattle.getDatabase();
            frame.setVisible(true);
        } catch (IllegalStateException ignored){
            this.database = null;
            frame.setVisible(false);
            frame.dispose();
            new MessageGUI("Tron - Player Authentication", "UNABLE TO RETRIEVE DATABASE!", Color.RED);
        }
        
    }
    
    /**
     * Adds an escape key listener to all the specified components.
     * Pressing the Escape key will close the authentication window
     * and navigate to the main menu.
     * 
     * @param components Components to attach the Escape key listener to.
    */
    private void addEscapeKeyListenerToComponents(Component... components) {
        KeyAdapter escapeListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    new MenuGUI();
                    frame.dispose();
                }
            }
        };

        for (Component component : components) {
            component.addKeyListener(escapeListener);
        }

        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
    
    /**
     * Creates the individual player panels for player authentication, 
     * including username, password fields, color selection, and status label.
     * 
     * @param title The title of the panel (either "Player 1" or "Player 2").
     * @param readyFlag AtomicBoolean indicating if the player is ready.
     * @return A JPanel containing the player's authentication components.
    */
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
            
            boolean isTextFieldEmpty = username.isEmpty() || password.isEmpty();
            if (isTextFieldEmpty || 
                username.equals("Username") || 
                password.equals("Password")
            ) {
                statusLabel.setText("Username and Password cannot be empty.");
                return;
            }

            // attempts to retrieve or create a player            
            PlayerEntity player;
            
            PlayerEntity playerByNamePassword = database.getPlayer(username, password);
            if (playerByNamePassword == null) {
                
                PlayerEntity playerByName = database.getPlayer(username);
                if (playerByName == null){
                     player = new PlayerEntity(username, password, Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
                } else {
                    player = new PlayerEntity(playerByName.getName(), playerByName.getPasswordHash(), playerByName.getRegisterDate(), PlayerEntity.PasswordType.HASHED); 
                }
            
            } else {
                player = new PlayerEntity(playerByNamePassword.getName(), playerByNamePassword.getPasswordHash(), playerByNamePassword.getRegisterDate(), PlayerEntity.PasswordType.HASHED);
            }
            
            
            if (player.checkPassword(password)) {
                String player1SelectedUsername = (String) player1Username.getText().trim();
                String player2SelectedUsername = (String) player2Username.getText().trim();
                String player1SelectedColor = (String) player1Color.getSelectedItem();
                String player2SelectedColor = (String) player2Color.getSelectedItem();
                if (!isTextFieldEmpty && player1SelectedUsername.equalsIgnoreCase(player2SelectedUsername)) {
                    statusLabel.setText("Players cannot choose the same name.");
                    return; 
                } else if (!isTextFieldEmpty && player1SelectedColor.equalsIgnoreCase(player2SelectedColor)) {
                    statusLabel.setText("Players cannot choose the same color.");
                    return;   
                }
                
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
    
    /**
     * Creates a text field with a placeholder for user input.
     * 
     * @param placeholder The placeholder text displayed in the text field.
     * @return A JTextField with placeholder functionality.
    */
    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        setPlaceholder(textField, placeholder);
        return textField;
    }

    /**
     * Creates a password field with a placeholder for user input.
     * 
     * @param placeholder The placeholder text displayed in the password field.
     * @return A JPasswordField with placeholder functionality.
    */
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        setPlaceholder(passwordField, placeholder);
        return passwordField;
    }

    /**
     * Sets the placeholder functionality for text fields.
     * 
     * @param textField The JTextField to set the placeholder for.
     * @param placeholder The placeholder text.
    */
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

    /**
     * Sets the placeholder functionality for password fields.
     * 
     * @param passwordField The JPasswordField to set the placeholder for.
     * @param placeholder The placeholder text.
    */
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
    
    
    /**
     * Converts a string representing a color to its corresponding Color object.
     * 
     * @param colorName The name of the color.
     * @return The corresponding Color object, or black if the color name is invalid.
    */
    private Color getColorFromSelection(String colorName) {
        return switch (colorName.toLowerCase()) {
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            case "yellow" -> Color.YELLOW;
            default -> Color.BLACK;
        };
    }

    /**
     * Checks if both players are ready. If both are ready, the game board 
     * is initialized and the authentication window is closed.
     */
    private void checkReady() {
        if (player1Ready.get() && player2Ready.get()) {                    
            if (player1 != null && player2 != null){
                Color player1ChosenColor = (Color)getColorFromSelection((String) player1Color.getSelectedItem());
                Color player2ChosenColor = (Color)getColorFromSelection((String) player2Color.getSelectedItem());
                
                Timer timer = new Timer(500, e -> {
                    new BoardGUI(player1, player1ChosenColor, player2, player2ChosenColor); 
                    frame.dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
}