/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import com.tron.battle.controller.TronBattle;
import com.tron.battle.view.HighScoreGUI;
import com.tron.battle.view.BoardGUI;
import com.tron.database.entity.PlayerEntity;
import com.tron.database.entity.HighScoreEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author zizi
 */
public class Board extends JPanel implements ActionListener {
    private final Timer timer;
    private final Player player1;
    private final Player player2;
    private static final int DELAY = 10;

    private boolean player1Dead;
    private boolean player2Dead;

    private boolean[][] player1Taken;
    private boolean[][] player2Taken;
    private boolean[][] bonusTab;

    private boolean drawBonus;
    private boolean init;
    private boolean player1Bonus;
    private boolean player1ActiveBonus;
    private boolean player2Bonus;
    private boolean player2ActiveBonus;
    private boolean activateTheBonus;
    private boolean cleared;
    private boolean bonusDrawn;

    private final Music sounds;
    private final Bonus bonus;
    private final GraphicsImages graphics;
    private final BoardGUI gui;
    
    private boolean sessionSaved = false;

    public Board(BoardGUI gui, PlayerEntity player1Entity, Color player1Color, PlayerEntity player2Entity, Color player2Color) {
        this.gui = gui;
        player1 = new Player(
                player1Entity, player1Color,
                new int[][]{{70, 200}, {70, 400}}, 
                KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, 3, 0, 3
        );
        player2 = new Player(
                player2Entity, player2Color,
                new int[][]{{720, 200}, {720, 400}}, 
                KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, -3, 0, 3
        );
        setFocusable(true);

        cleared = false;
        bonusDrawn = false;
        player1Bonus = false;
        player2Bonus = false;
        player1ActiveBonus = false;
        player2ActiveBonus = false;
        drawBonus = false;
        init = true;
        sounds = new Music();
        player1Dead = false;
        player2Dead = false;
        player1Taken = new boolean[800][600];
        player2Taken = new boolean[800][600];
        bonusTab = new boolean[800][600];
        timer = new Timer(DELAY, this);
        timer.start();
        bonus = new Bonus(Color.green, this);
        activateTheBonus = true;
        graphics = new GraphicsImages("images/scoreboard.png", "images/logo.jpg");
    }

    @Override
    public void paintComponent(Graphics g) {
        if (init) {
            g.setColor(Color.black);
            g.fillRect(0, 0, 800, 650);
            init = false;
        }

        if ((player1ActiveBonus || player2ActiveBonus) && !cleared) {
            cleared = true;
            g.setColor(Color.black);
            g.fillRect(bonus.getX(), bonus.getY(), 10, 10);
            g.drawRect(bonus.getX(), bonus.getY(), 10, 10);
        }

        if (drawBonus && !bonusDrawn) {
            bonus.draw(g);
            bonusDrawn = true;
        }

        if (!player1Dead && !player2Dead) {
            g.setColor(Color.white);
            g.drawImage(graphics.getScoreBoard(), 4, 586, 123, 30, this);
            g.drawImage(graphics.getScoreBoard(), 670, 586, 123, 30, this);
            g.drawImage(graphics.getLogo(), 350, 586, 100, 30, this);
            g.drawRect(1, 1, 791, 581);
            
            g.setColor(player1.getCurrentTron().getColor());
            g.drawString(player1.getName() + " : " + player1.getScore(), 30, 605);
            
            g.setColor(player2.getCurrentTron().getColor());
            g.drawString(player2.getName() + " : " + player2.getScore(), 700, 605);
            
            // Highlights
            g.setColor(player1.getCurrentTron().getColor());
            Movement movementPlayer1 = player1.getCurrentTron().getMovement();
            g.fillOval(movementPlayer1.getX() - 5, movementPlayer1.getY() - 5, movementPlayer1.getSpeed() + 10, movementPlayer1.getSpeed() + 10);

            g.setColor(player2.getCurrentTron().getColor());
            Movement movementPlayer2 = player2.getCurrentTron().getMovement();
            g.fillOval(movementPlayer2.getX() - 5, movementPlayer2.getY() - 5, movementPlayer2.getSpeed() + 10, movementPlayer2.getSpeed() + 10);

            player1.getTrons().get(0).draw(g, 70, 200, this.player1, this.player1.getName() + " : ");
            player1.getTrons().get(1).draw(g, 70, 400, this.player1, this.player1.getName() + " : ");
            
            player2.getTrons().get(0).draw(g, 720, 200, this.player2, this.player2.getName() + " : ");
            player2.getTrons().get(1).draw(g, 720, 400, this.player2, this.player2.getName() + " : ");
            
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (activateTheBonus) {
            drawBonus = true;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    bonusTab[bonus.getX() + i][bonus.getY() + j] = true;
                }
            }
        }

        for (Tron tron : player1.getTrons()) {
            tron.getMovement().move();
        }
        for (Tron tron : player2.getTrons()) {
            tron.getMovement().move();
        }

        player1Dead = player1.getTrons().stream().anyMatch(Tron::isDead);
        player2Dead = player2.getTrons().stream().anyMatch(Tron::isDead);
    
        if (!player1Dead && !player2Dead) {
            player1Dead = checkTaken(player1);
            player2Dead = checkTaken(player2);
        }

        if (player2Dead) player1.incrementScore();
        if (player1Dead) player2.incrementScore();

        markTaken(player1);
        markTaken(player2);

        player1Bonus = checkBonus(player1);
        player2Bonus = checkBonus(player2);

        if (player1Bonus && player1.getCurrentTron().getMovement().getSpeed() != 4) {
            player1.getCurrentTron().getMovement().changeSpeed(4);
            bonusDrawn = false;
            player1ActiveBonus = true;
            cleared = false;
        }
        if (player2Bonus && player1.getCurrentTron().getMovement().getSpeed() != 4) {
            player2.getCurrentTron().getMovement().changeSpeed(4);
            bonusDrawn = false;
            player2ActiveBonus = true;
            cleared = false;
        }

        if (player1ActiveBonus)
            player1ActiveBonus = player1.getCurrentTron().activeBonusPass();

        if (player2ActiveBonus)
            player2ActiveBonus = player2.getCurrentTron().activeBonusPass();

        if (player1ActiveBonus || player2ActiveBonus)
            bonusTab = new boolean[800][600];

        if (player1Dead || player2Dead) {
            sounds.playSound("audio/dead.wav");
            player1Taken = new boolean[800][600];
            player2Taken = new boolean[800][600];
            init = true;
            resetPlayer(player1, new int[][]{{70, 200}, {70, 400}}, 3, 0, 3);
            resetPlayer(player2, new int[][]{{720, 200}, {720, 400}}, -3, 0, 3);
            bonusDrawn = false;
            player1ActiveBonus = true;
            cleared = false;
        }
        repaint();
    }
    
    private void resetPlayer(Player player, int[][] positions, int dx, int dy, int speed) {
        for (int i = 0; i < player.getTrons().size(); i++) {
            Tron tron = player.getTrons().get(i);
            int[] pos = positions[i];
            tron.set(pos[0], pos[1], dx, dy, speed);
        }
    }

    private void markTaken(Player player) {
        int width, height;
        boolean[][] targetTaken = player.equals(player1) ? player1Taken : player2Taken;

        if (player.getCurrentTron().getMovement().getDY() == 0) {
            width = player.getCurrentTron().getMovement().getSpeed();
            height = 4;
        } else {
            width = 4;
            height = player.getCurrentTron().getMovement().getSpeed();
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    int x = player.getCurrentTron().getMovement().getX() + i;
                    int y = player.getCurrentTron().getMovement().getY() + j;
                    if (x < 800 && y < 600) {
                        targetTaken[x][y] = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
    }

    private boolean checkTaken(Player player) {
        int width, height;
        boolean[][] opponentTaken = player.equals(player1) ? player2Taken : player1Taken;

        if (player.getCurrentTron().getMovement().getDY() == 0) {
            width = player.getCurrentTron().getMovement().getSpeed();
            height = 4;
        } else {
            width = 4;
            height = player.getCurrentTron().getMovement().getSpeed();
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    int x = player.getCurrentTron().getMovement().getX() + i;
                    int y = player.getCurrentTron().getMovement().getY() + j;
                    if (x < 800 && y < 600 && opponentTaken[x][y]) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        return false;
    }

    private boolean checkBonus(Player player) {
        int width, height;

        if (player.getCurrentTron().getMovement().getDY() == 0) {
            width = player.getCurrentTron().getMovement().getSpeed();
            height = 4;
        } else {
            width = 4;
            height = player.getCurrentTron().getMovement().getSpeed();
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    int x = player.getCurrentTron().getMovement().getX() + i;
                    int y = player.getCurrentTron().getMovement().getY() + j;
                    if (bonusTab[x][y])
                        return true;
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        return false;
    }
    
    
    public boolean getTaken(int x, int y) {
        return player1Taken[x][y] || player2Taken[x][y];
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !sessionSaved)
            saveSession();
        
        if (isPlayerMovementKey(key, player1)) processPlayerMovement(player1, e);
        if (isPlayerMovementKey(key, player2)) processPlayerMovement(player2, e);
        
    }
    
    private void saveSession() {
        timer.stop();
        this.gui.reset();
        
        TronBattle.getDatabase().putHighScore(new HighScoreEntity(player1, player1.getScore()));
        TronBattle.getDatabase().putHighScore(new HighScoreEntity(player2, player2.getScore()));
        
        List<HighScoreEntity> highScores = TronBattle.getDatabase().getHighScores().stream().limit(10).collect(Collectors.toList());
        SwingUtilities.invokeLater(() -> new HighScoreGUI("Tron - Session Saved", highScores, Color.WHITE));

        sessionSaved = true;
    }
    
    private boolean isPlayerMovementKey(int key, Player player) {
    Movement movement = player.getCurrentTron().getMovement();
    return key == movement.getUp() ||
           key == movement.getDown() ||
           key == movement.getLeft() ||
           key == movement.getRight();
    }
    
    private void processPlayerMovement(Player player, KeyEvent e) {
        Movement movement = player.getCurrentTron().getMovement();
        if (isValidMovement(movement, e)) {
            player.nextCurrentTron();
        } 
        movement.keyPressed(e);
    }
      
    private boolean isValidMovement(Movement movement, KeyEvent e) {
        int key = e.getKeyCode();
        if (
            (key == movement.getLeft() && movement.getDX() != 0) ||
            (key == movement.getRight() && movement.getDX() != 0) ||
            (key == movement.getUp() && movement.getDY() != 0) ||
            (key == movement.getDown() && movement.getDY() != 0)
        ) {
            return false;
        } 
        return true;
    } 
   
    
}