package TankWar;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall{
    private int health = 100;
    private static BufferedImage breakableWallImg;
    private boolean isBroken = false;

    BreakableWall(int x, int y){
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, breakableWallImg.getWidth(), breakableWallImg.getHeight());
    }

    public int getHealth() {
        return health;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public static void setBreakableWallImg(BufferedImage Img) {
        breakableWallImg = Img;
    }

    private void removeHealth(int hitPoints){
        if(health - hitPoints < 0){
            health = 0;
            isBroken = true;
        }
        else health -= hitPoints;
    }

    public void update(){}

    public void collision(){this.removeHealth(50);}

    public void drawImage(Graphics2D g2d){
        if(!isBroken)
            g2d.drawImage(breakableWallImg, x, y, null);
    }

}
