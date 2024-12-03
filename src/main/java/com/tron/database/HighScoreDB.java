/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database;

import com.tron.database.entity.Game;
import com.tron.database.entity.Player;
import com.tron.database.entity.HighScore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.Timestamp;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author zizi
 */
public class HighScoreDB {
    
    private final Connection connection;
    
    private final int gameId; 
    private final Timestamp start_time;
    private Timestamp end_time;
    
    public HighScoreDB(Game game) throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "AlFarizi");
        connectionProps.put("serverTimezone", "UTC");
        
        String dbURL = "jdbc:mysql://localhost:3306/highscore_db";
        this.connection = DriverManager.getConnection(dbURL, connectionProps);
            
        this.gameId = insertGame(game);
        this.start_time = Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        this.end_time = null;
    }
    
    public void putHighScore(HighScore highScore) throws SQLException {
        insertPlayer(highScore.getPlayer());
        insertSessions(highScore);
        insertLeaderboard(highScore);
    }
    
    public int getHighScore(HighScore highScore) throws SQLException {
        // overal highscore from DB
        return retrieveHighScore(highScore);
    }
    
    public List<HighScore> getHighScores() throws SQLException {
        List<HighScore> highScores = new ArrayList<>();
        
        String query = """
                       SELECT pname, phash, registerdate, highscore
                       FROM Player p, Leaderboard l
                       WHERE p.playerno=l.playerno AND gameid=?
                       ORDER BY highscore DESC
                       """;
        
        try(PreparedStatement highScoreQuery = connection.prepareStatement(query)){
            highScoreQuery.setInt(1, this.gameId);
            try (ResultSet result = highScoreQuery.executeQuery()){
                while (result.next()){
                    String playerName = result.getString("pname");
                    String passwordHash = result.getString("phash");
                    Date registerDate = result.getDate("registerdate");
                    int highScore = result.getInt("highscore");
                    
                    Player player = new Player(playerName, passwordHash, registerDate, Player.PasswordType.HASHED);
                    highScores.add(new HighScore(player, highScore)); 
                }
            }
        }
        return highScores;
    }
    
    
    private int insertGame(Game game) throws SQLException {
        int gameId;
        if ((gameId = retrieveGameId(game)) >= 0) return gameId;
        
        String query = """
                       INSERT INTO Game (gname, releasedate) VALUES (?, ?)
                       """;
        
        try(PreparedStatement insertGameStatement = connection.prepareStatement(query)){
            insertGameStatement.setString(1, game.getName());
            insertGameStatement.setDate(2, game.getReleaseDate());
            if (insertGameStatement.executeUpdate() == 0)
                throw new SQLException("Failed to insert new game.");

            // Retrieve new gameid
            gameId = retrieveGameId(game);
        }
        return gameId;
    }
    
    private int insertPlayer(Player player) throws SQLException {
        int playerNo;
        if ((playerNo = retrievePlayerNo(player)) >= 0) return playerNo;
        
        String query = """
                       INSERT INTO Player (pname, phash, registerdate) VALUES (?, ?, ?)
                       """;
        
        try(PreparedStatement insertPlayerStatement = connection.prepareStatement(query)){
            insertPlayerStatement.setString(1, player.getName());
            insertPlayerStatement.setString(2, player.getPasswordHash());
            insertPlayerStatement.setDate(3, player.getRegisterDate());
            if (insertPlayerStatement.executeUpdate() == 0)
                throw new SQLException("Failed to insert new player.");
            
            // Retrieve new playerno
            playerNo = retrievePlayerNo(player);
        }
        return playerNo;
    }
    
    private int insertSessions(HighScore highScore) throws SQLException {
        int sessionsid = -1;
        
        int playerNo;
        if ((playerNo = retrievePlayerNo(highScore.getPlayer())) < 0) return sessionsid;
        
        String query = """
                       INSERT INTO Sessions (playerno, gameid, score, start_time, end_time) VALUES (?, ?, ?, ?, ?)
                       """;
        
        try(PreparedStatement insertSessionsStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            this.end_time = Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
            
            insertSessionsStatement.setInt(1, playerNo);
            insertSessionsStatement.setInt(2, this.gameId);
            insertSessionsStatement.setInt(3, highScore.getScore());
            insertSessionsStatement.setTimestamp(4, this.start_time);
            insertSessionsStatement.setTimestamp(5, this.end_time);
            if (insertSessionsStatement.executeUpdate() == 0)
                throw new SQLException("Failed to insert new session.");
            
            // Retrieve new sessionsid
            try (ResultSet generatedSessionsid = insertSessionsStatement.getGeneratedKeys()){
                if (generatedSessionsid.next()) sessionsid = generatedSessionsid.getInt(1);
            }
            
        }   
        return sessionsid;
    }
    
    
    private void insertLeaderboard(HighScore highScore) throws SQLException {
        int playerNo;
        if ((playerNo = retrievePlayerNo(highScore.getPlayer())) < 0) return;
        
        int highScoreFromDB = retrieveHighScore(highScore);
        
        if (highScoreFromDB < 0){
            // Insert Leaderboard;
            String insertQuery = """
                                 INSERT INTO Leaderboard (playerno, gameid, highscore) VALUES (?, ?, ?)
                                 """;
            
            try(PreparedStatement insertLeaderboardStatement = connection.prepareStatement(insertQuery)){
                insertLeaderboardStatement.setInt(1, playerNo);
                insertLeaderboardStatement.setInt(2, this.gameId);
                insertLeaderboardStatement.setInt(3, highScore.getScore());
                if (insertLeaderboardStatement.executeUpdate() == 0)
                    throw new SQLException("Failed to insert new leaderboard.");
            }
        } else {
            // Update Leadeboard
            String updateQuery = """
                                 UPDATE Leaderboard SET highscore=? WHERE playerno=? AND gameid=?
                                 """;
            
            try (PreparedStatement updateLeaderboardStatement = connection.prepareStatement("UPDATE Leaderboard SET highscore=? WHERE playerno=? AND gameid=?")){
                updateLeaderboardStatement.setInt(1, highScoreFromDB+highScore.getScore());
                updateLeaderboardStatement.setInt(2, playerNo);
                updateLeaderboardStatement.setInt(3, this.gameId);
                if (updateLeaderboardStatement.executeUpdate() == 0)
                    throw new SQLException("Failed to update leaderboard.");
            }
        }
    }
    
    private int retrievePlayerNo (Player player) throws SQLException {
        int playerNo = -1;
        
        String query = """
                       SELECT playerno FROM Player WHERE pname=?
                       """;
        
        try (PreparedStatement playerNoQuery = connection.prepareStatement(query)){
            playerNoQuery.setString(1, player.getName());
            try (ResultSet result = playerNoQuery.executeQuery()){
                if (result.next()) playerNo = result.getInt("playerno");
            }            
        }
        return playerNo;
    }
    
    private int retrieveGameId (Game game) throws SQLException {
        int gameId = -1;
        
        String query = """
                       SELECT gameid FROM Game WHERE gname=?
                       """;
        
        try (PreparedStatement gameIdQuery = connection.prepareStatement(query)){
            gameIdQuery.setString(1, game.getName());
            try (ResultSet result = gameIdQuery.executeQuery()){
                if (result.next()) gameId = result.getInt("gameid"); 
            }
        }
        return gameId;
    }
    
    private int retrieveHighScore (HighScore highScore) throws SQLException {
        int highScoreFromDB = -1;
        
        String query = """
                       SELECT highscore
                       FROM Leaderboard
                       WHERE playerno=? AND gameid=?
                       """;
        
        try (PreparedStatement highScoreQuery = connection.prepareStatement(query)){
            highScoreQuery.setInt(1, retrievePlayerNo(highScore.getPlayer()));
            highScoreQuery.setInt(2, this.gameId);
            try (ResultSet result = highScoreQuery.executeQuery()){
                if (result.next()) highScoreFromDB = result.getInt("highscore");
            }            
        }
        return highScoreFromDB;
    }
   
    
}