package org.cis120.boulder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


public class Rain implements Weather {
    public static final String IMG_FILE0 = "files/rain1.png";
    public static final String IMG_FILE1 = "files/rain2.png";
    public static final String IMG_FILE2 = "files/rain3.png";
    public static final String IMG_FILE3 = "files/rain4.png";
    //initial conditions of the rain
    public static final int RAIN_DROPS = 300;
    public static final int RAIN_WIDTH = 30;
    public static final int RAIN_HEIGHT = 2 * RAIN_WIDTH;
    public static final int X_VEL = -30;
    public static final int Y_VEL = 30;
    //amount of intervals in time until the weather will change
    public static final int CHANGE_HEAVY = 10;
    public static final int COURT_WIDTH = GameCourt.COURT_WIDTH;
    public static final int COURT_HEIGHT = GameCourt.COURT_HEIGHT;
    private BufferedImage[] img = new BufferedImage[4];
    private int timePassed = 0;
    private final LinkedList<Integer[]> rainstorm = new LinkedList<>();

    public Rain() {
        try {
            if (img[0] == null) {
                img[0] = ImageIO.read(new File(IMG_FILE0));
                img[1] = ImageIO.read(new File(IMG_FILE1));
                img[2] = ImageIO.read(new File(IMG_FILE2));
                img[3] = ImageIO.read(new File(IMG_FILE3));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        generateRain(RAIN_DROPS, COURT_WIDTH, COURT_HEIGHT);
    }

    private void generateRain(int numTimes, int width, int height) {
        for (int i = 0; i < numTimes; i++) {
            Random rand = new Random();
            int xcoord = rand.nextInt(width);
            int ycoord = rand.nextInt(height);
            int rainType = rand.nextInt(4);
            Integer[] raindrop = {xcoord, ycoord, rainType};
            rainstorm.add(raindrop);
        }
    }

    public int getRainSize() {
        return rainstorm.size();
    }

    public void change() {
        timePassed++;
        rainstorm.forEach(rain -> {
            Random rand = new Random();
            int velocity = rand.nextInt(2) + 1;
            rain[0] += X_VEL * velocity;
            rain[1] += Y_VEL * velocity;
            if (rain[0] < 0) {
                rain[0] += COURT_WIDTH;
            }
            if (rain[1] > COURT_HEIGHT) {
                rain[1] -= COURT_HEIGHT;
            }
        });
        if (timePassed >= CHANGE_HEAVY) {
            Random rand = new Random();
            if (rand.nextInt(2) >= 1 || rainstorm.size() <= RAIN_DROPS / 2) {
                generateRain(RAIN_DROPS / 2, COURT_WIDTH, COURT_HEIGHT);
            } else {
                rainstorm.subList(0, RAIN_DROPS / 2).clear();
            }
            timePassed = 0;
        }
    }

    public void draw(Graphics g) {
        rainstorm.forEach(rain -> {
            g.drawImage(img[rain[2]], rain[0], rain[1],
                    RAIN_WIDTH, RAIN_HEIGHT, null);
        });
    }
}
