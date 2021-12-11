package org.cis120.boulder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EndScene {
    private static final String WORDS = "files/end.png";
    private static BufferedImage words;

    public EndScene() {
        try {
            if (words == null) {
                words = ImageIO.read(new File(WORDS));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public void draw(Graphics g) {
        g.drawImage(words, 0, 0, 800, 640, null);
    }
}
