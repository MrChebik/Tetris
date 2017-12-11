package model;

import java.awt.*;

public class Cube {
    private int x;
    private int y;
    private int color;
    private Color origColor;

    public Cube(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;

        this.origColor = color == 0 ? Color.decode("#077E84") : color == 1 ? Color.decode("#ff5500") : color == 2 ? Color.decode("#5555ff") : color == 3 ? Color.decode("#d60e21") : Color.decode("#55aa7f");
    }

    public Cube(Cube cube) {
        this.x = cube.getX();
        this.y = cube.getY();
        this.color = cube.getColor();

        this.origColor = color == 0 ? Color.decode("#077E84") : color == 1 ? Color.decode("#ff5500") : color == 2 ? Color.decode("#5555ff") : color == 3 ? Color.decode("#d60e21") : Color.decode("#55aa7f");
    }

    public void setXYOfPlus(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;

        this.origColor = color == 0 ? Color.decode("#077E84") : color == 1 ? Color.decode("#ff5500") : color == 2 ? Color.decode("#5555ff") : color == 3 ? Color.decode("#d60e21") : Color.decode("#55aa7f");
    }

    public Color getOrigColor() {
        return origColor;
    }

    public void setOrigColor(Color origColor) {
        this.origColor = origColor;
    }
}
