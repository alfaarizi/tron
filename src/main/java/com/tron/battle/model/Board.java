/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import com.tron.battle.controller.TronBattle;
import com.tron.database.entity.PlayerEntity;
import com.tron.database.entity.HighScoreEntity;
import com.tron.battle.view.MessageGUI;
import com.tron.battle.view.BoardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *
 * @author zizi
 */
public class Board extends JPanel implements ActionListener {
    private Timer timer;
    private final Player player1;
    private final Player player2;
    private static final int DELAY = 10;

    private boolean player1Dead;
    private boolean player2Dead;

    public  boolean[][] taken;
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
                new int[][]{{70, 200}, {70, 400}}, // example initial positions for two trons
                KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, 2, 0, 2
        );
        player2 = new Player(
                player2Entity, player2Color,
                new int[][]{{720, 200}, {720, 400}}, // example initial positions for two trons
                KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, -2, 0, 2
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
        taken = new boolean[800][600];
        bonusTab = new boolean[800][600];
        timer = new Timer(DELAY, this);
        timer.start();
        bonus = new Bonus(Color.green, this);
        activateTheBonus = true;
        graphics = new GraphicsImages("images/scoreboard.png", "images/logo.jpg");
    }

    @Override
    public void paintComponent(Graphics g) {
        //at the beginning and after death we clear board
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
            player1.getCurrentTron().draw(g, 30, 605, this.player1, this.player1.getName() + " : ");
            player2.getCurrentTron().draw(g, 700, 605, this.player2, this.player2.getName() + " : ");
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

        player1.getCurrentTron().getMovement().move();
        player2.getCurrentTron().getMovement().move();

        player1Dead = player1.getCurrentTron().isDead();
        player2Dead = player2.getCurrentTron().isDead();
        if (!player1Dead && !player2Dead) {
            player1Dead = checkTaken(player1);
            player2Dead = checkTaken(player2);
        }

        if (player2Dead) player1.getCurrentTron().incementScore(player1);
        if (player1Dead) player2.getCurrentTron().incementScore(player2);

        markTaken(player1);
        markTaken(player2);

        player2Bonus = checkBonus(player2);
        player1Bonus = checkBonus(player1);

        if (player1Bonus && player1.getCurrentTron().movement.getSpeed() != 4) {
            player1.getCurrentTron().movement.changeSpeed(4);
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

        if (player1ActiveBonus) {
            player1ActiveBonus = player1.getCurrentTron().activeBonusPass();
        }

        if (player2ActiveBonus) {
            player2ActiveBonus = player2.getCurrentTron().activeBonusPass();
        }

        if (player1ActiveBonus || player2ActiveBonus) {
            bonusTab = new boolean[800][600];
        }

        if (player1Dead || player2Dead) {
            sounds.playSound("audio/dead.wav");
            taken = new boolean[800][600];
            init = true;
            player1.getCurrentTron().set(70, 200, 2, 0, 2);
            player2.getCurrentTron().set(720, 200, -2, 0, 2);
            bonusDrawn = false;
            player1ActiveBonus = true;
            cleared = false;
        }
        repaint();
    }

    private void markTaken(Player player) {
        int height;
        int width;

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
                    if (player.getCurrentTron().getMovement().getX() + i < 800 && player.getCurrentTron().getMovement().getY() + j < 600)
                        taken[player.getCurrentTron().getMovement().getX() + i][player.getCurrentTron().getMovement().getY() + j] = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkTaken(Player player) {
        int height;
        int width;

        if (player.getCurrentTron().getMovement().getDY() == 0) {
            width = player.getCurrentTron().getMovement().getSpeed();
            height = 4;
        } else {
            width = 4;
            height = player.getCurrentTron().getMovement().getSpeed();
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (player.getCurrentTron().getMovement().getX() + i <= 800 && player.getCurrentTron().getMovement().getY() + j <= 600) {
                    try {
                        if (taken[player.getCurrentTron().getMovement().getX() + i][player.getCurrentTron().getMovement().getY() + j]) return true;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private boolean checkBonus(Player player) {
        int height;
        int width;

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
                    if (bonusTab[player.getCurrentTron().getMovement().getX() + i][player.getCurrentTron().getMovement().getY() + j]) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public void keyPressed(KeyEvent e) {
        e.getKeyCode();
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!sessionSaved){
                timer.stop();
                this.gui.reset();
                SwingUtilities.invokeLater(() -> new MessageGUI("Tron - Session Saved", "SESSION SAVED!", Color.GREEN)); 
                TronBattle.getDatabase().putHighScore(new HighScoreEntity(player1, player1.getScore()));
                TronBattle.getDatabase().putHighScore(new HighScoreEntity(player2, player2.getScore()));
                
                System.out.println("Session saved: ");
                System.out.println(player1 + "\t" + player1.getScore());
                System.out.println(player2 + "\t" + player2.getScore());  
                
                sessionSaved = true;
            }
        }
        
        player1.getCurrentTron().movement.keyPressed(e);
        player2.getCurrentTron().movement.keyPressed(e);
    }

    public boolean getTaken(int x, int y) {
        return taken[x][y];
    }
}