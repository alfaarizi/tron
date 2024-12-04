/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tron;

import com.tron.database.HighScoreDB;
import com.tron.database.entity.GameEntity;
import com.tron.database.entity.HighScoreEntity;
import com.tron.database.entity.PlayerEntity;

import java.sql.SQLException;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.List;
/**
 *
 * @author zizi
 */
public class Sample {

    public static void main(String[] args) {
        GameEntity tronGame = new GameEntity("Tetris", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()));
        
        try{
            HighScoreDB database1 = new HighScoreDB(tronGame);

//            PlayerEntity player1 = new PlayerEntity(tronGame, "alfaarizi","AlFarizi", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
//            PlayerEntity player2 = new PlayerEntity(tronGame, "farah","@Farah", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
//            PlayerEntity player3 = new PlayerEntity(tronGame, "root","@rootpass", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);

            PlayerEntity player1, player2, player3 = null;
            
            player1 = new PlayerEntity("alfaarizi","AlFarizi", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
            if (player1 != null){
                database1.putHighScore(new HighScoreEntity(player1, 1));
                database1.putHighScore(new HighScoreEntity(player1, 0));
                database1.putHighScore(new HighScoreEntity(player1, 1)); 
            }

            player2 = database1.getPlayer("farah", "oopswrong");
            if (player2 != null){
                database1.putHighScore(new HighScoreEntity(player2, 1));
                database1.putHighScore(new HighScoreEntity(player2, 1));
                database1.putHighScore(new HighScoreEntity(player2, 1));
            }

            
            player3 = database1.getPlayer("alfaarizi", "AlFarizi");
            if (player3 != null){
                database1.putHighScore(new HighScoreEntity(player3, 1));
                database1.putHighScore(new HighScoreEntity(player3, 1));
                database1.putHighScore(new HighScoreEntity(player3, 1));
            }
            
        } catch (SQLException e) {}
        

        System.out.println("pass");
    }
}
