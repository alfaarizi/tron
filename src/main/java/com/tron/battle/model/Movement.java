/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import java.awt.event.KeyEvent;

/**
 * The Movement class handles the movement logic for a player or object in the game.
 * It defines the player's position, direction of movement, speed, and responds to key
 * events for controlling the movement.
 * 
 * @author zizi
 */
public class Movement {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int speed;
    private final int up;
    private final int down;
    private final int left;
    private final int right;

    /**
     * Constructs a Movement object with initial position, speed, and direction keys.
     * 
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param up The key code for moving up.
     * @param down The key code for moving down.
     * @param left The key code for moving left.
     * @param right The key code for moving right.
     * @param dx The initial change in x-coordinate (horizontal speed).
     * @param dy The initial change in y-coordinate (vertical speed).
     * @param speed The speed at which the object moves.
     */
    public Movement(int x, int y, int up, int down, int left, int right, int dx, int dy, int speed) {
        this.x = x;
        this.y = y;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.speed = speed;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Moves the object based on its current direction and speed.
     * If dx is non-zero, the object moves horizontally. 
     * If dy is non-zero, the object moves vertically.
    */
    public void move() {
        if (dx == 0) y += dy;
        if (dy == 0) x += dx;
    }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDX(int dx) { this.dx = dx; }
    public void setDY(int dy) { this.dy = dy; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDX() { return dx; }
    public int getDY() { return dy; }
    public int getSpeed() { return speed; }

    public int getUp() { return up; }
    public int getDown() { return down; }
    public int getLeft() { return left; }
    public int getRight() { return right; }

    /**
     * Responds to a key press event and updates the movement direction accordingly.
     * The player can only change direction if there is no movement in the perpendicular axis.
     * 
     * @param e The KeyEvent representing the key press.
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == left && dx == 0) moveLeft();
        if (key == right && dx == 0) moveRight();
        if (key == up && dy == 0) moveUp();
        if (key == down && dy == 0) moveDown();
    }

    /**
     * Moves the object left by setting the appropriate horizontal and vertical speeds.
     */
    private void moveLeft() {
        dx = -speed;
        dy = 0;
        y += (dy > 0) ? speed - 4 : 0;
    }

    /**
     * Moves the object right by setting the appropriate horizontal and vertical speeds.
     */
    private void moveRight() {
        dx = speed;
        dy = 0;
        x -= (dy < 0) ? speed - 4 : 0;
        y += (dy >= 0) ? speed - 4 : 0;
    }

    /**
     * Moves the object up by setting the appropriate horizontal and vertical speeds.
     */
    private void moveUp() {
        dy = -speed;
        dx = 0;
        x += (dx < 0) ? speed - 4 : 0;
    }

    /**
     * Moves the object down by setting the appropriate horizontal and vertical speeds.
     */
    private void moveDown() {
        dy = speed;
        dx = 0;
        x += (dx > 0) ? speed - 4 : 0;
        y -= (dy > 0) ? speed - 4 : 0;
    }

    /**
     * Changes the speed of the object and adjusts its position based on the new speed.
     * If there is any movement (horizontal or vertical), the position is updated to
     * reflect the speed change.
     * 
     * @param speed The new speed of the object.
     */
    public void changeSpeed(int speed) {
        int oldSpeed = this.speed;
        this.speed = speed;
        if (dx != 0) x = (dx < 0) ? x - speed + oldSpeed : x + speed - oldSpeed;
        if (dy != 0) y = (dy < 0) ? y + speed - oldSpeed : y - speed + oldSpeed;
        dx = (dx != 0) ? dx : 0;
        dy = (dy != 0) ? dy : 0;
    }
}