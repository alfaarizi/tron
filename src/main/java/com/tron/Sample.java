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
        try {
            GameEntity tronGame = new GameEntity("Tron", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()));
            GameEntity sampleGame = new GameEntity("Sample", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()));
            
            PlayerEntity player1 = new PlayerEntity("alfaarizi","samplepassword", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
            PlayerEntity player2 = new PlayerEntity("farah","@fefefe", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
            PlayerEntity player3 = new PlayerEntity("root","@rootpass", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), PlayerEntity.PasswordType.PLAIN);
            HighScoreDB database1 = new HighScoreDB(tronGame);
            HighScoreDB database2 = new HighScoreDB(sampleGame);
            
            List<HighScoreEntity> highscores = List.of(
                    new HighScoreEntity(player1, 1),
                    new HighScoreEntity(player1, 0),
                    new HighScoreEntity(player1, 0),
                    new HighScoreEntity(player3, 1),
                    new HighScoreEntity(player3, 0),
                    new HighScoreEntity(player3, 1),
                    new HighScoreEntity(player2, 1),
                    new HighScoreEntity(player2, 1),
                    new HighScoreEntity(player1, 1),
                    new HighScoreEntity(player1, 1),
                    new HighScoreEntity(player1, 1),
                    new HighScoreEntity(player2, 0),
                    new HighScoreEntity(player2, 1)
            );
            
            for(HighScoreEntity highScore: highscores){
                database1.putHighScore(highScore);
                database2.putHighScore(highScore);
                System.out.println("pass:" + highScore);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e){}
            }

              System.out.println("pass");
        } catch (SQLException e){
            System.out.println(e + "");
        }
    }
}
