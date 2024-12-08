/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import com.tron.database.entity.PlayerEntity;

import java.util.List;
import java.util.ArrayList;
import java .awt.Color;

/**
 * Represents a player in the Tron-like game.
 * It extends the PlayerEntity class to manage player-related data
 * It also provides game-specific functionality, such as handling movement, score, and Trons.
 * @author zizi
 */
public class Player extends PlayerEntity {
    private int score;
    private final List<Tron> trons;
    private Tron currentTron;
    
    /**
     * Constructs a Player object with the provided details.
     * Initializes the player's score, list of Trons, and sets up the initial Tron.
     * 
     * @param playerEntity The PlayerEntity object containing basic player data.
     * @param color The color of the player's Tron.
     * @param initialPositions A 2D array of initial positions for each Tron.
     * @param up Key code for moving up.
     * @param down Key code for moving down.
     * @param left Key code for moving left.
     * @param right Key code for moving right.
     * @param dx The initial horizontal movement speed.
     * @param dy The initial vertical movement speed.
     * @param speed The movement speed.
     */
    public Player(
        PlayerEntity playerEntity, 
        Color color, 
        int[][] initialPositions, 
        int up, int down, int left, int right, int dx, int dy, int speed
    ) {
        super(playerEntity.getName(), playerEntity.getPasswordHash(), playerEntity.getRegisterDate(), PlayerEntity.PasswordType.HASHED);

        this.score = 0;
        this.trons = new ArrayList<>();
        
        for (int[] pos : initialPositions) {
            int x = pos[0];
            int y = pos[1];
            Movement movement = new Movement(x, y, up, down, left, right, dx, dy, speed);
            trons.add(new Tron(color, movement));
        }
        
        this.currentTron = trons.getFirst();
    }
        
    public int getScore() {
        return this.score;
    }
    
    public List<Tron> getTrons() {
        return this.trons;
    }
    
    public Tron getCurrentTron(){
        return this.currentTron;
    }
    
    /**
     * Switches to the next Tron in the list.
     * If the player has reached the last Tron, it loops back to the first Tron.
    */
    public void nextCurrentTron(){
        this.currentTron = trons.get((trons.indexOf(this.currentTron) + 1) % trons.size());
    }
    /**
     * Increments the player's score by one.
    */
    public void incrementScore() {
        this.score++;
    }
    
    
}