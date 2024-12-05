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
public class PlayerEntity {
    
    private final String name;
    private final String passwordHash;
    private final Date registerDate;
    public static enum PasswordType { PLAIN, HASHED };
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public PlayerEntity(String name, String password, Date registerDate, PasswordType passwordType){
        this.name = name;
        this.registerDate = registerDate;
        if (passwordType == PasswordType.PLAIN) {
            this.passwordHash = encoder.encode(password);
        } else {
            this.passwordHash = password;
        }
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
        return String.format("Player(name=%s, passwordHash=%s, registerDate=%s)", name, passwordHash, registerDate);
    }
    
    
    
}
