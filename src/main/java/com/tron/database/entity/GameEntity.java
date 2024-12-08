/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database.entity;

import java.sql.Date;


/**
 * Represents a game entity in the Tron game database
 * Stores the name and release date of the game
 * 
 * @author zizi
 */
public class GameEntity {
    
    private final String name;
    private final Date releaseDate;
    
    /**
     * Creates a game with specified name and released ate
     * 
     * @param name the name of the game
     * @param releaseDate the release date of the game
     */
    public GameEntity(String name, Date releaseDate){
        this.name = name;
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return this.name;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    /**
     * Returns a string representation of the game
     * 
     * @return a string with game details
     */
    @Override
    public String toString() {
        return "Game{" + "name=" + name + ", date=" + releaseDate + '}';
    }
    
}
