/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database.entity;

/**
 * Represents a high score entry in the Tron game database
 * Stores a player and their score
 * 
 * @author zizi
 */
public class HighScoreEntity {
    
    private final PlayerEntity player;
    private final int score;
    
    /**
     * Creates a high score entity with specified player and score
     * 
     * @param player the player associated with the score
     * @param score the score of the player
     */
    public HighScoreEntity(PlayerEntity player, int score) {
        this.player = player;
        this.score = score;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public int getScore() {
        return this.score;
    }
    
    /**
     * Returns a string representation of the high score entry
     * @return 
     */
    @Override
    public String toString() {
        return "HighScore{" + "Player=" + player.getName() + ", score=" + score + '}';
    }
    
}
