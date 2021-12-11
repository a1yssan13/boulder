package org.cis120.boulder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sun implements Weather {
    public static final String IMG_FILE0 = "files/sun0.png";
    public static final String IMG_FILE1 = "files/sun1.png";
    //variables for where the sun goes
    public static final int WIDTH = GameCourt.COURT_WIDTH;
    public static final int HEIGHT = 500;

    private final int xCoord = 0;
    private final int yCoord = 0;
    private final BufferedImage[] img = new BufferedImage[2];
    private int index;

    public Sun() {
        try {
            if (img[0] == null) {
                img[0] = ImageIO.read(new File(IMG_FILE0));
                img[1] = ImageIO.read(new File(IMG_FILE1));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public void change() {
        if (index == 0) {
            index++;
        } else {
            index = 0;
        }
    }

    public int getIndex() {
        return index;
    }

    public void draw(Graphics g) {
        g.drawImage(img[index], xCoord, yCoord,
                WIDTH, HEIGHT, null);
    }
}
