/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import com.tron.database.entity.PlayerEntity;

import java.sql.SQLException;

import java.util.List;
import java.awt.Color;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author zizi
 */
public class Player extends PlayerEntity {
    // extends PlayerEntity 
    // a.k.a Player (trons, currentTron, colored, id)
//    private final List<Tron> trons;
//    private Tron currentTron;    
//
//    private Movement movement;
//    private final Color color;
//    private final String id;
    
    public Player(String name, String password, Date registerDate, PasswordType passwordType) {
        super(name, password, registerDate, passwordType);
    }

 
}
