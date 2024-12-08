/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database.entity;

import java.sql.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Represents a player in the Tron game database
 * Stores the name, hashed password, and registration date of the player
 * 
 * @author zizi
 */
public class PlayerEntity {
    
    private final String name;
    private final String passwordHash;
    private final Date registerDate;
    public static enum PasswordType { PLAIN, HASHED };
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    /**
     * Constructs a PlayerEntity with the specified name, password, registration date,
     * and password type (plain or hashed)
     * 
     * @param name the player's name
     * @param password the player's password
     * @param registerDate the player's registration date
     * @param passwordType the player's password type (plain or hashed)
     */
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

    /**
     * Checks if the given plain password matches the stored hash password
     * 
     * @param password the plain password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String password){
        return encoder.matches(password, this.passwordHash);
    }
    
    
    /**
     * Returns a string representation of the player
     * 
     * @return a string with player's details
     */
    @Override
    public String toString() {
        return String.format("Player(name=%s, passwordHash=%s, registerDate=%s)", name, passwordHash, registerDate);
    }
    
    
    
}
