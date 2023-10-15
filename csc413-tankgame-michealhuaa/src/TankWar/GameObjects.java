package TankWar;

import java.awt.*;

public abstract class GameObjects {


    int x;
    int y;
    int vx;
    int vy;
    int angle;
    int height;
    int width;

    Rectangle rectangle;

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

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public abstract void update();

    public abstract void drawImage(Graphics2D graphics);

    public abstract void collision();

}
