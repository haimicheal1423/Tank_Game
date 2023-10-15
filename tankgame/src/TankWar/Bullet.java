package TankWar;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Bullet extends GameObjects{

    private String ownerOfBullet;
    private boolean isInactive = false;
    private boolean small_explosion = false;
    boolean collision = false;
    static private BufferedImage bulletImg;
    static private BufferedImage smallExplosionImg;
    static private BufferedImage bigExplosionImg;
    private int afterCollision = 0;

    public String getOwnerOfBullet() {
        return ownerOfBullet;
    }

    public void setOwnerOfBullet(String owner) {
        this.ownerOfBullet = owner;
    }

    public boolean isInactive() {
        return isInactive;
    }

    public void setInactive(boolean inactive) {
        isInactive = inactive;
    }

    public boolean isSmall_explosion() {
        return small_explosion;
    }

    public void setSmall_explosion(boolean small_explosion) {
        this.small_explosion = small_explosion;
    }

    static void setBulletImg(BufferedImage bullet){
        bulletImg = bullet;
    }

    static void setSmallExplosionImg(BufferedImage smallExplosion) {
        smallExplosionImg = smallExplosion;
    }

    static void setBigExplosionImg(BufferedImage bigExplosion) {
        bigExplosionImg = bigExplosion;
    }

    Bullet(int x, int y, int angle){
        this.x = x;
        this.y = y;
        this.vx = (int) Math.round(7 * Math.cos(Math.toRadians(angle))); // 7 to have the bullet go faster than the tank
        this.vy = (int) Math.round(7 * Math.sin(Math.toRadians(angle)));
        this.angle = angle;
        this.rectangle = new Rectangle(x, y, bulletImg.getWidth(), bulletImg.getHeight());
    }

    public void drawImage(Graphics2D g2d){
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), bulletImg.getWidth() / 2.0, bulletImg.getHeight() / 2.0);

        if(collision && small_explosion){
            g2d.drawImage(smallExplosionImg, rotation, null);
            if(afterCollision >= 5){
                this.isInactive = true;
            }
        }
        else if(collision && !small_explosion){
            g2d.drawImage(bigExplosionImg, rotation, null);
            if(afterCollision >= 5){
                this.isInactive = true;
            }
        }
        else g2d.drawImage(bulletImg, rotation, null);

    }

    public void update(){
        if(!collision){
            this.x = x + vx;
            this.y = y + vy;
            this.checkBorder();
        }
        else{
            afterCollision++;
        }
        this.rectangle.setLocation(x, y);
    }

    public void collision(){
        collision = true;
    }

    private void checkBorder(){
        int leftLimit = 30;
        int rightLimit = GameWorld.WORLD_WIDTH -65;
        int lowerLimit = 40;
        int upperLimit = GameWorld.WORLD_WIDTH -60;

        if(x < leftLimit){
            this.isInactive = true;
        }
        if(x >= rightLimit){
            this.isInactive = true;
        }
        if(y < lowerLimit){
            this.isInactive = true;
        }
        if(y >= upperLimit){
            this.isInactive = true;
        }

    }
}
