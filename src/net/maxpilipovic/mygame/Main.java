package net.maxpilipovic.mygame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        //Create object gamepanel and set it to window
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        //Causes the window to be resized to fit preferred size and layouts of GamePanel
        window.pack();

        //Center of screen
        window.setLocationRelativeTo(null);

        //Set Visable
        window.setVisible(true);


    }
}