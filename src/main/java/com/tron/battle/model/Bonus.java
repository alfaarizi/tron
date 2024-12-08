/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * The Bonus class represents a bonus item in the game that can appear on the game board.
 * The bonus item is drawn with a specified color and can be randomly positioned on the board.
 * The class ensures that the bonus does not overlap with other taken areas of the board.
 * 
 * @author zizi
 */
public class Bonus {
    private int x;
    private int y;
    private final Color color;
    private final Board board;

    /**
     * Constructs a Bonus object with the specified color and board.
     * The position is randomly assigned within the draw method.
     * 
     * @param color The color of the bonus.
     * @param board The board where the bonus will be placed.
    */
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

    /**
     * Draws the bonus on the game board at a random position.
     * The position is recalculated every time this method is called to ensure
     * the bonus does not overlap with any other taken area on the board.
     * 
     * @param g The Graphics object used to draw the bonus.
    */
    public void draw(Graphics g) {
        randomPosition();
        g.setColor(this.color);
        g.fillRect(x, y, 10, 10);
    }
    
    /**
     * Randomly assigns a valid position for the bonus on the board.
     * This method ensures the bonus does not overlap with any taken area.
     * The position is recalculated until an available area is found.
    */
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