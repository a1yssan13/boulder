package org.cis120.boulder;

import java.awt.*;

public class Menu {


    public static final int BORDER = 2;
    public static final String PROMPT = "are you happy?";

    /* Size of object, in pixels. */
    private final int width;
    private final int height;
    private final int px;
    private final int py;

    private boolean yesHover = false;
    private boolean noHover = false;

    public Menu(int px, int py, int width, int height) {
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
    }

    /**
     * @param coorx the coordinate x in which the button was pressed
     * @param coory the y coordinate in which the button was pressed
     */
    public void pressButton(int coorx, int coory) {
        //if in this button
    }

    private void drawBox(Graphics g, int px, int py, int width, int height) {
        g.fillRect(px - BORDER, py - BORDER, width + 2 * BORDER, BORDER);
        g.fillRect(px - BORDER, py - BORDER, BORDER, height + 2 * BORDER);
        g.fillRect(px + width,
                py - BORDER,
                BORDER,
                height + 2 * BORDER);
        g.fillRect(px - BORDER, py + height, width + 2 * BORDER, BORDER);
    }

    public boolean isYes(double x, double y) {
        return (x >= px + width / 6 && x <= px + width / 6 + width / 4
                && y >= py + height / 2 && y <= py + height / 2 + height / 6);
    }

    public boolean isNo(double x, double y) {
        return (x >= px + 7 * width / 12 && x <= px + 7 * width / 12 + width / 4
                && y >= py + height / 2 && y <= py + height / 2 + height / 6);
    }

    public void pressYes() {
        yesHover = true;
    }

    public void resetYes() {
        yesHover = false;
    }

    public void pressNo() {
        noHover = true;
    }

    public void resetNo() {
        noHover = false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(px, py, width, height);
        g.setColor(Color.black);

        //draw borders;
        drawBox(g, px, py, width, height);
        Font myFont = new Font("Courier New", 1, 15);
        g.setFont(myFont);
        g.drawString(PROMPT, px + width / 2 - 60, py + height / 2 - 40);

        //draw the yes button
        drawBox(g, px + width / 6, py + height / 2, width / 4, height / 6);
        if (yesHover) {
            g.setColor(Color.black);
            g.fillRect(px + width / 6, py + height / 2, width / 4, height / 6);
            g.setColor(Color.white);
        }
        g.drawString("yes.", px + width / 6 + width / 8 - 15,
                py + height / 2 + height / 12 + 5);
        g.setColor(Color.black);
        drawBox(g, px + 7 * width / 12, py + height / 2,
                width / 4, height / 6);
        if (noHover) {
            g.setColor(Color.black);
            g.fillRect(px + 7 * width / 12, py + height / 2,
                    width / 4, height / 6);
            g.setColor(Color.white);
        }
        g.drawString("no.", px + 7 * width / 12 + width / 8 - 13,
                py + height / 2 + height / 12 + 5);
    }


}
