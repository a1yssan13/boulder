package org.cis120.boulder;

/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 *
 * @version 2.1, Apr 2017
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // Gameboard size constants
    public static final int COURT_PIXEL = 4; //number of pixels per unit
    public static final int COURT_X = 200;
    public static final int COURT_Y = 160;
    public static final int COURT_WIDTH = COURT_X * COURT_PIXEL;
    public static final int COURT_HEIGHT = COURT_Y * COURT_PIXEL;
    //constants for drawing the hill
    public static final int BOTTOM_HILL = 20; //in game units
    public static final int TOP_HILL = 100; //in game units
    public static final int FLAT_AREA = 20;
    public static final double SLOPE = ((double) TOP_HILL - BOTTOM_HILL)
            / (COURT_X - 2 * FLAT_AREA);
    //boulder initial conditions
    public static final int ROCK_SIZE = 13;
    public static final double MAX_ROCKSPEED = 1;
    //SISYPHUS initial size
    public static final int SISYPHUS_WIDTH = ROCK_SIZE / 2 * COURT_PIXEL;
    public static final int SISPHYUS_HEIGHT = ROCK_SIZE * COURT_PIXEL;
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 50;
    public static final int INTERVAL_CHECK = 20; //time to change velocity of ball
    public static final int THRESHOLD = 6; //amount of key presses / time interval
    //background
    private final int[][] landscape = generateLandscape();
    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."
    //variable rock items
    private int rockX = FLAT_AREA - ROCK_SIZE;
    private int rockY = BOTTOM_HILL + ROCK_SIZE;
    private double rockSpeed = 0; //speed the rock is moving at
    private boolean forward = true; //whether or not the rock is moving forward
    private boolean end = false; //whether the game is showing end scene
    //game states
    private int daysLived = 0;
    private int keyPressed = 0;
    private LinkedList<Integer> totalPresses = new LinkedList<>();
    private int timePassed = 0;
    private boolean showMenu = false;
    private final Menu endMenu = new Menu(COURT_WIDTH / 4, COURT_HEIGHT / 3,
            COURT_WIDTH / 2, COURT_HEIGHT / 3);
    private Sisyphus person = new Sisyphus(COURT_PIXEL * (rockX) - SISYPHUS_WIDTH,
            (COURT_Y - rockY) * COURT_PIXEL, SISYPHUS_WIDTH, SISPHYUS_HEIGHT);
    private Weather background;
    private String message = "";

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();
        // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!showMenu) {
                    if (e.getKeyCode() == KeyEvent.VK_X) {
                        keyPressed++;
                    }
                }
            }
        });
        this.status = status;

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (showMenu) {
                    Point p = e.getPoint();
                    if (endMenu.isYes(e.getX(), p.getY())) {
                        endMenu.pressYes();
                        repaint();
                    } else if (endMenu.isNo(p.getX(), p.getY())) {
                        endMenu.pressNo();
                        repaint();
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (showMenu) {
                    if (endMenu.isYes(p.getX(), p.getY())) {
                        endMenu.resetYes();
                        showMenu = false;
                        daysLived++;
                        reset();
                    } else if (endMenu.isNo(p.getX(), p.getY())) {
                        endMenu.resetNo();
                        showMenu = false;
                        end = true; //could do some distortion view
                    }
                }
            }
        });
    }

    private Weather generateWeather() {
        Random rand = new Random();
        if (rand.nextInt(2) >= 1) {
            return new Sun();
        } else {
            return new Rain();
        }
    }

    private int[][] generateLandscape() {
        int[][] landscape = new int[COURT_X][COURT_Y];
        for (int i = 0; i < COURT_X; i++) {
            for (int j = 0; j < BOTTOM_HILL; j++) {
                landscape[i][COURT_Y - j - 1] = 1;
            }
            if (i >= FLAT_AREA && i < COURT_X - FLAT_AREA) {
                for (int j = 0; j < (i - FLAT_AREA) * SLOPE; j++) {
                    landscape[i][COURT_Y - j - 1 - BOTTOM_HILL] = 1;
                }
            }
            if (i >= COURT_X - FLAT_AREA) {
                for (int j = BOTTOM_HILL; j < TOP_HILL; j++) {
                    landscape[i][COURT_Y - j - 1] = 1;
                }
            }
        }
        return landscape;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        playing = true;
        timePassed = 0;
        totalPresses = new LinkedList<>();
        forward = true;
        end = false;
        person = new Sisyphus(COURT_PIXEL * (rockX) - SISYPHUS_WIDTH,
                (COURT_Y - rockY) * COURT_PIXEL, SISYPHUS_WIDTH, SISPHYUS_HEIGHT);
        background = generateWeather();
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * Moving the boulder up the hill.
     */
    private void move() {
        if (forward && !end) {
            //obtain the total presses of the last few time intervals
            rockSpeed = calculateRockSpeed(totalPresses);
            person.moveSisyphus(rockSpeed);
            moveRock();
            person.walk(rockX, rockY);
        } else {
            moveDown(MAX_ROCKSPEED);
            person.changeState(State.DOWN);
            if (rockX < (COURT_X / 3 * 2) && person.getx() > rockX
                    + ROCK_SIZE * COURT_PIXEL + SISYPHUS_WIDTH) {
                person.walkDown(landscape);
            }
        }
        if (end) {
            endAnimation();
        }
    }

    /**
     * Calculates the rockspeed based on the keypresses
     * @param totalPresses an ArrayList of the presses
     * @return the rockSpeed that is associated
     */
    public double calculateRockSpeed(LinkedList<Integer> totalPresses) {
        int size = 0;
        int avgKeyPresses = 0;
        if (totalPresses != null) {
            size = totalPresses.size();
            //checks for rockSpeed
        }
        if (size >= INTERVAL_CHECK) {
            //obtain the collective total of the keypresses
            for (int i = size - INTERVAL_CHECK; i < size; i++) {
                avgKeyPresses += totalPresses.get(i);
            }
            //calculate the velocity based off of the total
            return getSpeed(avgKeyPresses);
        } else {
            return 0;
        }
    }

    /**
     * Move rock according to the rock speed.
     */
    private void moveRock() {
        if (rockSpeed >= 0) {
            moveUp(rockSpeed);
        } else {
            moveDown(-1 * rockSpeed);
        }
    }

    /**
     * Returns the determined rock speed based on
     * the average number of keypresses
     * @param avgKeyPresses the total avg sum of the last interval
     *                      of keypresses
     * @return Rockspeed of the rock
     */
    public double getSpeed(int avgKeyPresses) {
        if (avgKeyPresses > THRESHOLD) {
            return 1 * MAX_ROCKSPEED;
        } else if (avgKeyPresses > THRESHOLD / 1.5) {
            return 0.8 * MAX_ROCKSPEED;
        } else if (avgKeyPresses > THRESHOLD / 2.0) {
            return 0.7 * MAX_ROCKSPEED;
        } else if (avgKeyPresses > THRESHOLD / 3.0) {
            return 0.5 * MAX_ROCKSPEED;
        } else if (avgKeyPresses > THRESHOLD / 4.0) {
            return 0.3 * MAX_ROCKSPEED;
        } else if (avgKeyPresses > THRESHOLD / 5.0) {
            return 0.2 * MAX_ROCKSPEED;
        } else if (avgKeyPresses > THRESHOLD / 5.5) {
            return 0;
        } else if (avgKeyPresses > THRESHOLD / 7.0) {
            return -1 * MAX_ROCKSPEED / 3.0;
        } else {
            return -1.0 * MAX_ROCKSPEED / 2.0;
        }
    }

    /**
     * When the game is "ending" - runs through process/animation of person banging head.
     */
    public void endAnimation() {
        if (person.getx() >= rockX + ROCK_SIZE * COURT_PIXEL + ROCK_SIZE / 3 * COURT_PIXEL) {
            person.walkDown(landscape);
        } else {
            person.changeState(State.KILL);
            if (timePassed % (8 * INTERVAL) == 0) {
                boolean dying = person.die();
                if (dying) {
                    endGame();
                }
            }
        }
    }

    /**
     * End game
     */
    public void endGame() {
        playing = false;
        repaint();
    }

    /**
     * this method moves up the rock at indicated speed
     * @param speed speed to which to move rock up if the rock is not
     *              add the top of hill.
     *              else switch direction of rock.
     */
    public void moveUp(double speed) {
        if (rockX <= COURT_X - FLAT_AREA + ROCK_SIZE / 2) {
            rockX += speed;
            int xcoord = (int) (rockX + ROCK_SIZE / 2.0);
            while (landscape[xcoord][COURT_Y - rockY + ROCK_SIZE - 1] == 1) {
                rockY += 1;
            }
        } else {
            forward = false;
            //insert fireworks when it's rolled to the top
        }
    }

    /**
     * this method moves down the rock at indicated speed
     * @param speed speed to which to move rock down if the rock is not
     *              add the bottom of hill.
     *              else end the turn
     */
    public void moveDown(double speed) {
        if (rockX >= FLAT_AREA - ROCK_SIZE) {
            rockX -= speed;
            int xcoord = (int) (rockX + ROCK_SIZE / 2.0);
            while (landscape[xcoord][COURT_Y - rockY + ROCK_SIZE] != 1) {
                rockY -= 1;
            }
        } else {
            if (!forward && !end) {
                showMenu = true;
            }
        }
    }

    /**
     * getting accessor methods for the sake of testing
     */
    public String getWeather() {
        return background.getClass().getName();
    }

    public boolean getDirection() {
        return forward;
    }

    public void setDirection(boolean forward) {
        this.forward = forward;
    }

    public int getRockX() {
        return rockX;
    }

    public void setRockX(int x) {
        rockX = x;
    }

    public int getRockY() {
        return rockY;
    }

    public void setRockY(int y) {
        rockY = y;
    }

    public boolean getMenu() {
        return showMenu;
    }

    /**
     * Draw the initial screen
     * NOTE: can rewrite code to make landscape & make all landscape pixels black
     */
    public void drawCanvas(Graphics g) {
        if (playing) {
            //draw the initial conditions of the grid for playing on
            g.setColor(Color.white);
            for (int i = 0; i < COURT_Y; i++) {
                g.drawLine(0, i * COURT_PIXEL, COURT_WIDTH, i * COURT_PIXEL);
            }
            for (int i = 0; i < COURT_X; i++) {
                g.drawLine(i * COURT_PIXEL, 0, i * COURT_PIXEL, COURT_HEIGHT);
            }
            g.setColor(Color.black);

            //draw the landscape
            for (int i = 0; i < landscape.length; i++) {
                for (int j = 0; j < landscape[0].length; j++) {
                    if (landscape[i][j] == 1) {
                        g.fillRect(i * COURT_PIXEL, j * COURT_PIXEL,
                                COURT_PIXEL, COURT_PIXEL);
                    }
                }
            }

            //draw the background weather
            background.draw(g);

            //draw the rock itself
            g.setColor(Color.gray);
            g.fillOval(COURT_PIXEL * rockX, (COURT_Y - rockY) * COURT_PIXEL,
                    ROCK_SIZE * COURT_PIXEL, ROCK_SIZE * COURT_PIXEL);

            //draw Sisyphus
            person.draw(g);

            //display the statistics of the game
            g.setColor(Color.black);
            g.drawString("height: " + (COURT_HEIGHT - person.gety() - SISPHYUS_HEIGHT -
                    BOTTOM_HILL * COURT_PIXEL), COURT_WIDTH - 120, 40);
            g.drawString("days lived: " + daysLived, COURT_WIDTH - 120, 60);
            g.drawString("press X rapidly to push boulder up hill.",
                    COURT_WIDTH / 20, COURT_HEIGHT / 20);

            if (showMenu) {
                endMenu.draw(g);
            }

        } else {
            g.setColor(Color.black);
            g.fillRect(0, 0, COURT_WIDTH, COURT_HEIGHT);
            g.setColor(Color.white);

            try {
                File file = new File("files/corrupted.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str;
                while ((str = br.readLine()) != null) {
                    message = message + str;
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }

            //create a glitchy effect in the aftereffects screen!
            int length = 0;
            if (timePassed % (2 * INTERVAL) != 0) {
                length += 2;
            }
            for (int i = 0; i < COURT_HEIGHT; i += 20) {
                if (message.length() > length + COURT_WIDTH / 3) {
                    String msg = message.substring(length, length + COURT_WIDTH);
                    g.drawString(msg, 0, i);
                } else {
                    length = 0;
                    g.drawString(message.substring(length, length + COURT_WIDTH), 0, i);
                }
                length += COURT_WIDTH / 10;
            }
            //add on the creepy text
            EndScene end = new EndScene();
            end.draw(g);
        }

    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            // advance the square and snitch in their current direction.
            timePassed += INTERVAL; //tracks time
            totalPresses.add(keyPressed);
            keyPressed = 0;
            move();
            if (timePassed % (12 * INTERVAL) == 0) {
                background.change();
            }
        } else {
            timePassed += INTERVAL;
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCanvas(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}