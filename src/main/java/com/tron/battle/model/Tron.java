/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author zizi
 */
public class Tron {
    private final Color color;
    public Movement movement;
    private int activeBonus;

    public Tron(Color color, Movement movement) {
        this.color = color;
        this.movement = movement;
        this.activeBonus = 1000;
    }
    
    public String toString(){
        return this.color.toString();
    }

    public Color getColor() {
        return color;
    }
    
    public Movement getMovement() {
        return movement;
    }

    public boolean isDead() {
        if (movement.getX() <= movement.getSpeed() || movement.getX() >= 784 - movement.getSpeed() || movement.getY() <= movement.getSpeed() || movement.getY() >= 584 - movement.getSpeed()) {
            movement.setDX(0);
            movement.setDY(0);
            return true;
        }
        return false;
    }

    public void incementScore(Player player) {
        player.incrementScore();
    }

    public void draw(Graphics g, int x, int y, Player player, String color) {
        g.setColor(getColor());
        if (movement.getDY() == 0) {
            g.fillRect(movement.getX(), movement.getY(), movement.getSpeed() - 1, 3);
            g.drawRect(movement.getX(), movement.getY(), movement.getSpeed() - 1, 3);
        } else {
            g.fillRect(movement.getX(), movement.getY(), 3, movement.getSpeed() - 1);
            g.drawRect(movement.getX(), movement.getY(), 3, movement.getSpeed() - 1);
        }
    }

    public void set(int x, int y, int dx, int dy, int speed) {
        movement.setX(x);
        movement.setY(y);
        movement.setDX(dx);
        movement.setDY(dy);
        movement.changeSpeed(speed);
    }

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