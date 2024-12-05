/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tron.battle.model;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author zizi
 */
public class GraphicsImages {
    private Image scoreBoard;
    private Image logo;

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

    private void loadScoreBoard(String scoreBoard) {
        ImageIcon ii = new ImageIcon(scoreBoard);
        this.scoreBoard = ii.getImage();
    }

    private void loadLogo(String logo) {
        ImageIcon ii = new ImageIcon(logo);
        this.logo = ii.getImage();
    }
}