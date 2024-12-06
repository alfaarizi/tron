/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database.entity;

/**
 *
 * @author zizi
 */
public class HighScoreEntity {
    
    private final PlayerEntity player;
    private final int score;
    
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

    @Override
    public String toString() {
        return "HighScore{" + "Player=" + player.getName() + ", score=" + score + '}';
    }
    
}
