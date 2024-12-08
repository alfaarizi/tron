/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.database;

import com.tron.database.entity.GameEntity;
import com.tron.database.entity.PlayerEntity;
import com.tron.database.entity.HighScoreEntity;

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
 * Manages high score data in the database for a specified game
 * Includes CRUD operation: inserting, retrieving, and updating player scores
 * 
 * @author zizi
 */
public class HighScoreDB {
    
    private final GameEntity game; 
    private final int gameId; 
    
    private final Connection connection;
    private final Timestamp start_time;
    private Timestamp end_time;
    
    /**
     * Initializes the database connection and game-related information
     * 
     * @param game the game entity associated with high scores
     * @throws SQLException if a database connection error occurs
     */
    public HighScoreDB(GameEntity game) throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "AlFarizi");
        connectionProps.put("serverTimezone", "UTC");

        String dbURL = "jdbc:mysql://localhost:3306/highscore_db";
        this.connection = DriverManager.getConnection(dbURL, connectionProps);
        
        this.game = game;
        this.gameId = insertGame(this.game);
        this.start_time = Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        this.end_time = null;

    }
    
    /**
     * Adds a high score for a player
     * 
     * @param highScore the high score entity to be added
     */
    public void putHighScore(HighScoreEntity highScore) {
        try {
            insertPlayer(highScore.getPlayer());
            insertSessions(highScore);
            insertLeaderboard(highScore);
        } catch (SQLException e) {
            System.out.println("there was an incomplete highscore insertion");
        }
    }
    
    /**
     * Retrieves the current high score of a player
     * 
     * @param highScore the high score entity of the player
     * @return the player's current high score
     */
    public int getHighScore(HighScoreEntity highScore) {
        return retrieveHighScoreFromLeaderboard(highScore);
    }
    
    /**
     * Retrieves the list of all high scores for the game
     * 
     * @return a list of high scores for the game
     */
    public List<HighScoreEntity> getHighScores() {
        List<HighScoreEntity> highScores = new ArrayList<>();
        
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
                    
                    PlayerEntity player = new PlayerEntity(playerName, passwordHash, registerDate, PlayerEntity.PasswordType.HASHED);
                    highScores.add(new HighScoreEntity(player, highScore)); 
                }
            }
        } catch (SQLException e) {}
        
        return highScores;
    }
    
    /**
     * Retrieves a player by name and password
     * 
     * @param name the player's name
     * @param password the player's plain password
     * @return the player's entity if found and password matches, otherwise null
     */
    public PlayerEntity getPlayer(String name, String password) {
        PlayerEntity player = null;
        
        String query = """
                       SELECT phash, registerdate 
                       FROM Player
                       WHERE pname=?
                       """;

        try (PreparedStatement playerQuery = connection.prepareStatement(query)){
            playerQuery.setString(1, name);
            try (ResultSet result = playerQuery.executeQuery()){
                while (result.next()) {
                    String passwordHash = result.getString("phash");
                    Date registerDate = result.getDate("registerdate");
                    
                    PlayerEntity currPlayer = new PlayerEntity(name, passwordHash, registerDate, PlayerEntity.PasswordType.HASHED);
                    if (currPlayer.checkPassword(password)){
                        player = currPlayer;
                        break;
                    }
                }
            }            
        } catch (SQLException e) {}
        
        return player;
    }
    
    /**
     * Retrieves a player by name
     * 
     * @param name the player's name
     * @return the player's entity if found, otherwise null
     */
    public PlayerEntity getPlayer(String name) {
        PlayerEntity player = null;
        
        String query = """
                       SELECT phash, registerdate 
                       FROM Player
                       WHERE pname=?
                       """;

         try (PreparedStatement playerQuery = connection.prepareStatement(query)){
            playerQuery.setString(1, name);
            try (ResultSet result = playerQuery.executeQuery()){
                if (result.next()) {
                    String passwordHash = result.getString("phash");
                    Date registerDate = result.getDate("registerdate");
                    player = new PlayerEntity(name, passwordHash, registerDate, PlayerEntity.PasswordType.HASHED);
                }
            }            
        } catch (SQLException e) {}
        
        return player;
    }
    
    
    /**
     * Inserts a new game into the database if it does not already exists
     * 
     * @param game the game entity to be inserted
     * @return the game id of the inserted game
     * @throws SQLException if a database error occurs
     */
    private int insertGame(GameEntity game) throws SQLException {
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
    
    /**
     * Inserts a new player into the database if it does not already exist
     * 
     * @param player the player entity to be inserted
     * @return the player number of the inserted player
     * @throws SQLException if a database error occurs
     */
    private int insertPlayer(PlayerEntity player) throws SQLException {
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
    
    /**
     * Inserts a new session into the database for a player
     * 
     * @param highScore the high score entity associated with the session
     * @return the session id of the inserted session
     * @throws SQLException if a database error occurs
     */
    private int insertSessions(HighScoreEntity highScore) throws SQLException {
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
    
    
    /**
     * Inserts or updates the leader board with the player's high score for the game
     * @param highScore the high score entity to be inserted or updated
     * @throws SQLException if a database error occurs
     */
    private void insertLeaderboard(HighScoreEntity highScore) throws SQLException {
        int playerNo;
        if ((playerNo = retrievePlayerNo(highScore.getPlayer())) < 0) return;
        
        int highScoreFromLeaderboard = retrieveHighScoreFromLeaderboard(highScore);
        if (highScoreFromLeaderboard < 0){
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
                updateLeaderboardStatement.setInt(1, retrieveHighScoreFromSessions(highScore));
                updateLeaderboardStatement.setInt(2, playerNo);
                updateLeaderboardStatement.setInt(3, this.gameId);
                if (updateLeaderboardStatement.executeUpdate() == 0)
                    throw new SQLException("Failed to update leaderboard.");
            }
        }
    }
    
    /**
     * Retrieves the player number (id) based on the player's game and password hash
     * @param player the player's entity
     * @return the player number if found, otherwise -1
     */
    private int retrievePlayerNo (PlayerEntity player) {
        int playerNo = -1;
        
        String query = """
                       SELECT playerno FROM Player WHERE pname=? AND phash=?
                       """;
        
        try (PreparedStatement playerNoQuery = connection.prepareStatement(query)){
            playerNoQuery.setString(1, player.getName());
            playerNoQuery.setString(2, player.getPasswordHash());
            try (ResultSet result = playerNoQuery.executeQuery()){
                if (result.next()) playerNo = result.getInt("playerno");
            }            
        } catch (SQLException e) {}
        
        return playerNo;
    }
    
    /**
     * Retrieves the game id based on the game's name
     * 
     * @param game the game entity
     * @return the game id if found, otherwise -1
     */
    private int retrieveGameId (GameEntity game) {
        int gameId = -1;
        
        String query = """
                       SELECT gameid FROM Game WHERE gname=?
                       """;
        
        try (PreparedStatement gameIdQuery = connection.prepareStatement(query)){
            gameIdQuery.setString(1, game.getName());
            try (ResultSet result = gameIdQuery.executeQuery()){
                if (result.next()) gameId = result.getInt("gameid"); 
            }
        } catch (SQLException e) {}
        
        return gameId;
    }
    
    /**
     * Retrieves the high score from the sessions for a specific player and game.
     * 
     * @param highScore the high score entity
     * @return the high score from the sessions if found, otherwise -1
     */
    private int retrieveHighScoreFromSessions (HighScoreEntity highScore) {
        int highScoreFromSessions = -1;
        
        String query = """
                       SELECT MAX(score) AS maxi
                       FROM sessions
                       WHERE playerno=? AND gameid=?
                       """;
        
        try (PreparedStatement highScoreFromSessionsQuery = connection.prepareStatement(query)){
            highScoreFromSessionsQuery.setInt(1, retrievePlayerNo(highScore.getPlayer()));
            highScoreFromSessionsQuery.setInt(2, this.gameId);
            try (ResultSet result = highScoreFromSessionsQuery.executeQuery()){
                if (result.next()) highScoreFromSessions = result.getInt("maxi");
            }            
        } catch (SQLException e) {}
        
        return highScoreFromSessions;
    }
    
    
    /**
     * Retrieves the high score from the leader board for a specific player and game.
     * 
     * @param highScore the high score entity
     * @return the high score from the leader board if found, otherwise -1
     */
    private int retrieveHighScoreFromLeaderboard (HighScoreEntity highScore) {
        String selectQuery = """
                             SELECT highscore FROM Leaderboard WHERE playerno=? AND gameid=?
                             """;
       
        int highScoreFromLeaderboard = -1;
        try(PreparedStatement highScoreFromLeaderboardQuery = connection.prepareStatement(selectQuery)){
                highScoreFromLeaderboardQuery.setInt(1, retrievePlayerNo(highScore.getPlayer()));
                highScoreFromLeaderboardQuery.setInt(2, this.gameId);
                try (ResultSet result = highScoreFromLeaderboardQuery.executeQuery()){
                    if (result.next()) highScoreFromLeaderboard = result.getInt("highscore");
                }    
        } catch (SQLException e) {}
        
        return highScoreFromLeaderboard;
    }
   
    
}
