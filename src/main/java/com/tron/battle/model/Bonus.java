/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import java.awt.*;
import java.util.Random;

/**
 *
 * @author zizi
 */
public class Bonus {
    private int x;
    private int y;
    private final Color color;
    private final Board board;

    public Bonus(Color color, Board board) {
        this.color = color;
        this.board = board;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g) {
        randomPosition();
        g.setColor(this.color);
        g.fillRect(x, y, 10, 10);
    }

    public void randomPosition() {

        boolean notPossible = true;

        while (notPossible) {
            Random rand = new Random();
            this.x = rand.nextInt(700) + 50;
            this.y = rand.nextInt(500) + 50;

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (notPossible) notPossible = board.getTaken(x + i, y + j);
                }
            }
        }
    }
}