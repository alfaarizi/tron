/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import com.tron.database.entity.PlayerEntity;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

/**
 *
 * @author zizi
 */
public class Player extends PlayerEntity {
    public int score;
    private final List<Tron> trons;
    private Tron currentTron;
    
    public Player(
        PlayerEntity playerEntity, 
        Color color, 
        int[][] initialPositions, 
        int up, int down, int left, int right, int dx, int dy, int speed
    ) {
        super(playerEntity.getName(), playerEntity.getPasswordHash(), playerEntity.getRegisterDate(), PlayerEntity.PasswordType.HASHED);

        this.score = 0;
        this.trons = new ArrayList<>();
        
        // Ensure exactly two trons are added for each player
        for (int[] pos : initialPositions) {
            int x = pos[0];
            int y = pos[1];
            Movement movement = new Movement(x, y, up, down, left, right, dx, dy, speed);
            trons.add(new Tron(color, movement)); // Tron will use the player's color
        }
        
        this.currentTron = trons.getFirst();
}


    public List<Tron> getTrons() {
        return this.trons;
    }
    
    public Tron getCurrentTron(){
        return this.currentTron;
    }
    
    public Tron nextCurrentTron(){
        this.currentTron = trons.get((trons.indexOf(this.currentTron) + 1) % trons.size());
        return this.currentTron;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void incrementScore() {
        this.score++;
    }
    
    
}