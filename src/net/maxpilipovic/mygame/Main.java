package net.maxpilipovic.mygame;

import javax.swing.*;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");
        window.setUndecorated(true);

        //Create object gamepanel and set it to window
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        //Causes the window to be resized to fit preferred size and layouts of GamePanel
        window.pack();

        //Center of screen
        window.setLocationRelativeTo(null);

        //Set Visable
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}