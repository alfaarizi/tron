/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database.entity;

import java.sql.Date;

/**
 *
 * @author zizi
 */
public class HighScore {
    
    private final Player player;
    private final int score;
    
    public HighScore(Player player, int score) {
        this.player = player;
        this.score = score;
    }

    public Player getPlayer() {
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
