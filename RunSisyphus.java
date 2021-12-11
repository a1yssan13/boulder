package org.cis120.boulder;
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 *
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunSisyphus implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("The Myth of Sisyphus");
        status_panel.add(status);
        JOptionPane.showMessageDialog(frame, "Welcome to my game! \n" +
                "The instructions are simple. You're playing as Sisyphus, condemned \n" +
                "by the Gods to roll the boulder up the hill every day. \n" +
                "To play, hit X as fast as you can to roll the boulder up, \n" +
                "and \"check-in\" after each day. There is no goal - roll the boulder \n " +
                "upwards as many days as you please. \n \n" +
                "This game is based off of the essay \"The Myth of Sisyphus\" \n" +
                "by Albert Camus - highly recommend reading this if you're confused why\n" +
                "it is the way it is.");

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        // Start game
        court.reset();
    }
}