/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database.entity;

import java.sql.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author zizi
 */
public class Player {
    
    private String name;
    private String passwordHash;
    private Date registerDate;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public static enum PasswordType { PLAIN, HASHED };
    
    public Player(String name, String password, Date registerDate, PasswordType passwordType){
        this.name = name;
        this.registerDate = registerDate;
        if (passwordType == PasswordType.PLAIN) this.passwordHash = encoder.encode(password);
        else this.passwordHash = password;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getPasswordHash(){
        return this.passwordHash;
    }
    
    public Date getRegisterDate(){
        return this.registerDate;
    }
    

    public boolean checkPassword(String password){
        return encoder.matches(password, this.passwordHash);
    }
    
    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", registerDate=" + registerDate + '}';
    }
    
    
    
}
