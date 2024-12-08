/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a player (Tron) in the game
 * Manages the player's color, movement, score, and active bonuses
 * 
 * @author zizi
 */
public class Tron {
    private final Color color;
    private Movement movement;
    private int activeBonus;

    /**
     * Constructor for the Tron player.
     * 
     * @param color The color of the Tron player.
     * @param movement The movement object that defines the player's movement behavior.
     */
    public Tron(Color color, Movement movement) {
        this.color = color;
        this.movement = movement;
        this.activeBonus = 1000;
    }

    public Color getColor() {
        return color;
    }
    
    public Movement getMovement() {
        return movement;
    }

    /**
     * Checks if the Tron player is "dead", i.e., if it has gone out of bounds.
     * If the player goes out of bounds, the player's movement is stopped.
     * 
     * @return True if the player is dead (out of bounds), false otherwise.
     */
    public boolean isDead() {
        if (movement.getX() <= movement.getSpeed() || movement.getX() >= 784 - movement.getSpeed() || movement.getY() <= movement.getSpeed() || movement.getY() >= 584 - movement.getSpeed()) {
            movement.setDX(0);
            movement.setDY(0);
            return true;
        }
        return false;
    }

    /**
     * Increments the score of the specified player.
     * 
     * @param player The player whose score is to be incremented.
     */
    public void incementScore(Player player) {
        player.incrementScore();
    }

    /**
     * Draws the Tron player on the screen.
     * 
     * @param g The Graphics object used to draw the player.
     * @param player The player object associated with the Tron.
     */
    public void draw(Graphics g, Player player) {
        g.setColor(color);
        if (movement.getDY() == 0) {
            g.fillRect(movement.getX(), movement.getY(), movement.getSpeed() - 1, 3);
            g.drawRect(movement.getX(), movement.getY(), movement.getSpeed() - 1, 3);
        } else {
            g.fillRect(movement.getX(), movement.getY(), 3, movement.getSpeed() - 1);
            g.drawRect(movement.getX(), movement.getY(), 3, movement.getSpeed() - 1);
        }
    }

    /**
     * Sets the position and speed of the Tron player.
     * 
     * @param x The x-coordinate of the Tron player.
     * @param y The y-coordinate of the Tron player.
     * @param dx The change in x-coordinate (speed).
     * @param dy The change in y-coordinate (speed).
     * @param speed The speed of the Tron player.
     */
    public void set(int x, int y, int dx, int dy, int speed) {
        movement.setX(x);
        movement.setY(y);
        movement.setDX(dx);
        movement.setDY(dy);
        movement.changeSpeed(speed);
    }

    /**
     * Reduces the active bonus counter and applies the bonus effects (increasing speed) when necessary.
     * If the bonus expires, the player's speed is increased and the bonus counter resets.
     * 
     * @return True if the active bonus is still ongoing, false if the bonus has expired.
    */
    public boolean activeBonusPass() {
        activeBonus -= 10;
        if (activeBonus == 0) {
            movement.changeSpeed(2);
            activeBonus = 1000;
            return false;
        }
        return true;
    }
    
}