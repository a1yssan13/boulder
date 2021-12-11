package org.cis120.boulder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sisyphus {

    //get all the files for moving up
    public static final String MOVE_ONE = "files/1.png";
    public static final String MOVE_TWO = "files/2.png";
    public static final String MOVE_THREE = "files/3.png";
    public static final String MOVE_FOUR = "files/4.png";
    public static final String MOVE_FIVE = "files/5.png";
    public static final String MOVE_SIX = "files/6.png";
    public static final String MOVE_SEVEN = "files/7.png";
    //get all the files for moving down
    public static final String DOWN_ONE = "files/down0.png";
    public static final String DOWN_TWO = "files/down1.png";
    public static final String DOWN_THREE = "files/down2.png";
    public static final String DOWN_FOUR = "files/down3.png";
    public static final String DOWN_FIVE = "files/down4.png";
    public static final String DOWN_SIX = "files/down5.png";
    //files for dying
    public static final String KILL_ONE = "files/dead1.png";
    public static final String KILL_TWO = "files/dead2.png";
    public static final String KILL_THREE = "files/dead3.png";
    public static final String KILL_FOUR = "files/dead4.png";
    public static final String KILL_FIVE = "files/dead5.png";
    public static final String KILL_SIX = "files/dead6.png";
    public static final String KILL_SEVEN = "files/dead7.png";
    public static final String KILL_EIGHT = "files/dead8.png";
    public static final String KILL_NINE = "files/dead9.png";
    public static final String KILL_TEN = "files/dead10.png";
    private final int width;
    private final int height;
    private final BufferedImage[] walkingUp = new BufferedImage[7];
    private final BufferedImage[] walkingDown = new BufferedImage[6];
    private final BufferedImage[] endAnimation = new BufferedImage[10];
    private State status = State.UP;
    private int initX;
    private int initY;
    private int walkingState = 0;
    private int downState = 0;
    private int endState = 0;

    public Sisyphus(int initx, int inity, int width, int height) {
        this.initX = initx;
        this.initY = inity;
        this.width = width;
        this.height = height;
        try {
            if (walkingUp[0] == null) {
                walkingUp[0] = ImageIO.read(new File(MOVE_ONE));
                walkingUp[1] = ImageIO.read(new File(MOVE_TWO));
                walkingUp[2] = ImageIO.read(new File(MOVE_THREE));
                walkingUp[3] = ImageIO.read(new File(MOVE_FOUR));
                walkingUp[4] = ImageIO.read(new File(MOVE_FIVE));
                walkingUp[5] = ImageIO.read(new File(MOVE_SIX));
                walkingUp[6] = ImageIO.read(new File(MOVE_SEVEN));
            }
            if (walkingDown[0] == null) {
                walkingDown[0] = ImageIO.read(new File(DOWN_ONE));
                walkingDown[1] = ImageIO.read(new File(DOWN_TWO));
                walkingDown[2] = ImageIO.read(new File(DOWN_THREE));
                walkingDown[3] = ImageIO.read(new File(DOWN_FOUR));
                walkingDown[4] = ImageIO.read(new File(DOWN_FIVE));
                walkingDown[5] = ImageIO.read(new File(DOWN_SIX));
            }
            if (endAnimation[0] == null) {
                endAnimation[0] = ImageIO.read(new File(KILL_ONE));
                endAnimation[1] = ImageIO.read(new File(KILL_TWO));
                endAnimation[2] = ImageIO.read(new File(KILL_THREE));
                endAnimation[3] = ImageIO.read(new File(KILL_FOUR));
                endAnimation[4] = ImageIO.read(new File(KILL_FIVE));
                endAnimation[5] = ImageIO.read(new File(KILL_SIX));
                endAnimation[6] = ImageIO.read(new File(KILL_SEVEN));
                endAnimation[7] = ImageIO.read(new File(KILL_EIGHT));
                endAnimation[8] = ImageIO.read(new File(KILL_NINE));
                endAnimation[9] = ImageIO.read(new File(KILL_TEN));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

    }

    public void changeState(State s) {
        status = s;
    }

    public State getState() {
        return status;
    }

    public int getx() {
        return initX;
    }

    public int gety() {
        return initY;
    }

    public void walk(int rockX, int rockY) {
        //height difference between person and boulder
        int heightDiff = (int) (GameCourt.ROCK_SIZE / 1.2 * GameCourt.SLOPE
                * GameCourt.COURT_PIXEL);
        //where should sisyphus be positioned in relation to boulder
        initX = GameCourt.COURT_PIXEL * rockX - width;
        int flatArea = GameCourt.FLAT_AREA * GameCourt.COURT_PIXEL;
        if (initX > flatArea && initX < GameCourt.COURT_WIDTH - flatArea - 1.2 * width) {
            initX += width / 2;
            initY = (GameCourt.COURT_Y - rockY) * GameCourt.COURT_PIXEL + heightDiff;
        } else if (initX <= flatArea) {
            initY = (GameCourt.COURT_Y - GameCourt.BOTTOM_HILL
                    - GameCourt.ROCK_SIZE) * GameCourt.COURT_PIXEL;
        } else {
            initY = (GameCourt.COURT_Y - rockY) * GameCourt.COURT_PIXEL;
        }
    }

    public boolean die() {
        if (endState < 9) {
            endState++;
            return false;
        } else {
            return true;
        }
    }

    public void goDown() {
        if (downState < 5) {
            downState++;
        } else {
            downState = 0;
        }
    }

    public void walkDown(int[][] landscape) {
        goDown();
        initX -= 3 * GameCourt.MAX_ROCKSPEED;
        int xcoord = (int) ((initX + width / 2.0) / GameCourt.COURT_PIXEL);
        int ycoord = (initY + height) / GameCourt.COURT_PIXEL;
        while (landscape[xcoord][ycoord] != 1) {
            initY += GameCourt.COURT_PIXEL;
            ycoord = (initY + height) / GameCourt.COURT_PIXEL;
        }
    }

    public void moveSisyphus(double rockSpeed) {
        if (rockSpeed >= 0.2 * GameCourt.MAX_ROCKSPEED) {
            if (walkingState < 6) {
                walkingState++;
            } else {
                walkingState = 0;
            }
        } else if (rockSpeed <= -1 * GameCourt.MAX_ROCKSPEED / 3.0
                && initX > GameCourt.FLAT_AREA * GameCourt.COURT_PIXEL) {
            if (walkingState > 0) {
                walkingState--;
            } else {
                walkingState = 6;
            }
        }
    }

    public void draw(Graphics g) {
        switch (status) {
            case UP:
                g.drawImage(walkingUp[walkingState], initX, initY, width, height, null);
                break;
            case DOWN:
                g.drawImage(walkingDown[downState], initX, initY, width, height, null);
                break;
            case KILL:
                g.drawImage(endAnimation[endState], initX, initY, width, height, null);
                break;
            default:
        }
    }
}
