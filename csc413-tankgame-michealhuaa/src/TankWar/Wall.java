package TankWar;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObjects{
    private boolean isBG;
    private static BufferedImage backgroundImg;
    private static BufferedImage unBreakWallImg;

    public Wall(){}

    public Wall(int x, int y, boolean isBG){
        this.x = x;
        this.y = y;
        this.isBG = isBG;
        this.rectangle = new Rectangle(x, y, 32, 32);
    }

    public static void setBackgroundImg(BufferedImage Img) {
        backgroundImg = Img;
    }

    public static void setUnBreakWallImg(BufferedImage Img) {
        unBreakWallImg = Img;
    }

    public void update(){} //for the abstract class

    public void collision(){} //for the abstract class

    public void drawImage(Graphics2D g2d){
        if(this.isBG){
            g2d.drawImage(backgroundImg, x, y, null);
        }
        else g2d.drawImage(unBreakWallImg, x, y, null);
    }


}
