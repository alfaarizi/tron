/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author zizi
 */
public class Board {
    // a.k,a Board (board, boardSize, players, currentPlayer, currentMove)
    private final Field[][] board;
    private final int DEFAULT_BOARD_WIDTH = 800;
    private final int DEFAULT_BOARD_HEIGHT = 600;
    
//    private final List<Player> players;
//    private Player currentPlayer;
//    private int currentMove = 0;
    
    public Board(){
        board = new Field[DEFAULT_BOARD_WIDTH][DEFAULT_BOARD_HEIGHT];
        for (int i = 0; i < DEFAULT_BOARD_WIDTH; ++i) {
            for (int j = 0; j < DEFAULT_BOARD_HEIGHT; ++j) {
                board[i][j] = new Field();
            }
        }
        
//        this.players = new ArrayList<>();
//        players.add(new Player(1));
//        players.add(new Player(2, this.boardSize));

//        currentPlayer = players.getFirst();
    }
    
}
