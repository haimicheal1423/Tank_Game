package TankWar;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUps extends GameObjects{
    boolean healthBoost = false;
    boolean speedBoost = false;
    boolean isActive = true;
    static private BufferedImage healthImg;
    static private BufferedImage speedImg;

    public PowerUps(int x, int y, boolean isHealth, boolean isSpeed){
        this.x = x;
        this.y = y;
        this.healthBoost = isHealth;
        this.speedBoost = isSpeed;
        this.rectangle = new Rectangle(x, y, 40, 40);
    }

    public static void setHealthImg(BufferedImage Img) {
        healthImg = Img;
    }

    public static void setSpeedImg(BufferedImage Img) {
        speedImg = Img;
    }

    public void update(){}

    public void collision(){
        this.isActive = false;
    }

    public void drawImage(Graphics2D g2d){
        if(this.healthBoost){
            g2d.drawImage(healthImg, x, y, 40, 40, null);
        }
        if(this.speedBoost){
            g2d.drawImage(speedImg, x, y, 40, 40, null);
        }
    }
}
