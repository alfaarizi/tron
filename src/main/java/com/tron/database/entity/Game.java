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
public class Game {
    
    private String name;
    private Date releaseDate;
    
    public Game(String name, Date releaseDate){
        this.name = name;
        this.releaseDate = releaseDate;
    }
    
    public String getName() {
        return this.name;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    @Override
    public String toString() {
        return "Game{" + "name=" + name + ", date=" + releaseDate + '}';
    }
    
}
