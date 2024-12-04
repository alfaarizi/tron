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
    // extends GameEntity
    // KnightTournament (gui)
    
    private final HighScoreDB database;
    private final MenuGUI gui;
    
    public TronBattle() throws SQLException {       
        super("Tron", Date.valueOf(
                ZonedDateTime.of(1982, 8, 9, 0, 0, 0, 0, ZoneId.of("UTC")).toLocalDate()
        ));
        this.database = new HighScoreDB(this);
        this.gui = new MenuGUI();
    }    
    
    public static void main(String[] args){
        try {
            TronBattle game = new TronBattle();
        } catch (SQLException e){
            System.out.println(e + "");
        }
        
    }
}
