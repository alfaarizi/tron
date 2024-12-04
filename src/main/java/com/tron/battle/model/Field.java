/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;


/**
 *
 * @author zizi
 */
public class Field {
    private boolean occupied;
    private boolean hasBonus;

    public Field(){
        this.occupied = false;
        this.hasBonus = false;
    }
    
    public boolean isOccupied(){
        return this.occupied;
    }
    
    public boolean hasBonus(){
        return this.hasBonus;
    }
    
    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }
    
    public void setHasBonus(boolean hasBonus){
        this.hasBonus = hasBonus;
    }
}
