/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.controller;

import com.tron.database.HighScoreDB;
import com.tron.database.entity.GameEntity;
import com.tron.battle.view.MenuGUI;

import java.sql.SQLException;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author zizi
 */
public class TronBattle extends GameEntity {
    
    private static HighScoreDB database;
    private final MenuGUI gui;
    
    public TronBattle() {       
        super("Tron", Date.valueOf(
                ZonedDateTime.of(1982, 8, 9, 0, 0, 0, 0, ZoneId.of("UTC")).toLocalDate()
        ));
        
        try{
            TronBattle.database = new HighScoreDB(this);
        } catch (SQLException e) {
            System.out.println("database not connected");
        }
        
        this.gui = new MenuGUI();
    }    
    
    public static HighScoreDB getDatabase() {
        if (database == null) throw new IllegalStateException("database not initialized properly");
        return database;
    }
    
    public static void main(String[] args){
        new TronBattle();
    }
}
