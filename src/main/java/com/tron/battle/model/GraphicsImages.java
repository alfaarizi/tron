/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import javax.swing.*;
import java.awt.*;

/**
 * The GraphicsImages class is used to load and manage images used in the game, such as
 * the scoreboard and logo
 * 
 * @author zizi
 */
public class GraphicsImages {
    private Image scoreBoard;
    private Image logo;

    /**
     * Constructs a GraphicsImages object and loads the scoreboard and logo images
     * from the given file paths.
     * 
     * @param scoreBoard Path to the image file for the scoreboard.
     * @param logo Path to the image file for the logo.
    */
    public GraphicsImages(String scoreBoard, String logo) {
        loadScoreBoard(scoreBoard);
        loadLogo(logo);
    }

    public Image getScoreBoard() {
        return scoreBoard;
    }

    public Image getLogo() {
        return logo;
    }
    
    /**
     * Loads the scoreboard image from the given file path.
     * 
     * @param scoreBoard Path to the image file for the scoreboard.
     */
    private void loadScoreBoard(String scoreBoard) {
        ImageIcon ii = new ImageIcon(scoreBoard);
        this.scoreBoard = ii.getImage();
    }

    /**
     * Loads the logo image from the given file path.
     * 
     * @param logo Path to the image file for the logo.
    */
    private void loadLogo(String logo) {
        ImageIcon ii = new ImageIcon(logo);
        this.logo = ii.getImage();
    }
}