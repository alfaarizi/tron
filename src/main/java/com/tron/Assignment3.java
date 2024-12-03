/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tron;

import com.tron.database.HighScoreDB;
import com.tron.database.entity.Game;
import com.tron.database.entity.HighScore;
import com.tron.database.entity.Player;

import java.sql.SQLException;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.List;
/**
 *
 * @author zizi
 */
public class Assignment3 {

        public static void main(String[] args) {
        try {
            Game tronGame = new Game("Tron", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()));
            Game sampleGame = new Game("Sample", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()));
            
            Player player1 = new Player("alfaarizi","AlFarizi", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), Player.PasswordType.PLAIN);
            Player player2 = new Player("farah","@Farah", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), Player.PasswordType.PLAIN);
            Player player3 = new Player("root","@rootpass", Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate()), Player.PasswordType.PLAIN);
            HighScoreDB database1 = new HighScoreDB(tronGame);
            HighScoreDB database2 = new HighScoreDB(sampleGame);
            
            List<HighScore> highscores = List.of(
                    new HighScore(player1, 1),
                    new HighScore(player1, 0),
                    new HighScore(player1, 0),
                    new HighScore(player3, 1),
                    new HighScore(player3, 0),
                    new HighScore(player3, 1),
                    new HighScore(player2, 1),
                    new HighScore(player2, 1),
                    new HighScore(player1, 1),
                    new HighScore(player1, 1),
                    new HighScore(player1, 1),
                    new HighScore(player2, 0),
                    new HighScore(player2, 1)
            );
            
            for(HighScore highScore: highscores){
                database1.putHighScore(highScore);
                database2.putHighScore(highScore);
                System.out.println("pass:" + highScore);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e){}
            }
            
        } catch (SQLException e){
            System.out.println(e + "");
        }
    }
}
