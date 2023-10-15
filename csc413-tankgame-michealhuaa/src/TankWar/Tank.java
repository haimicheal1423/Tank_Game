package TankWar;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import TankWar.Bullet;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObjects{

    private int vx;
    private int vy;
    private int angle;

    private float R = 3;
    private float ROTATIONSPEED = 3.0f;

    private int health = 100;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    private boolean shootPressed;

    private GameWorld gameWorld;

    private long lastShotFired = 0;
    private long speedBoost = 0;
    private boolean isSpeedBoosted;
    private int fireRate = 500;


    private String name;


    private boolean idle = false;

    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.height =50;
        this.width = 50;
        this.rectangle = new Rectangle(x,y,img.getWidth(),img.getHeight());
    }

    void setGameWorld(GameWorld setGameWorld){
        this.gameWorld = setGameWorld;
    }

    void setHealth(int health){this.health = health;}



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAngle(int angle){this.angle = angle;}
    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void shootPressed() { this.shootPressed = true;}

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void unToggleShootPressed() { this.shootPressed = false;}


    public void update() {

        this.rectangle.setLocation(x,y);

        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if(this.shootPressed && (System.currentTimeMillis() - lastShotFired > fireRate)){
            this.placeBullet(x, y, vx, vy, angle, gameWorld);
            lastShotFired = System.currentTimeMillis();
        }

        this.idle = false;


    }


    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        if (System.currentTimeMillis() - speedBoost < 5000 && this.isSpeedBoosted) {
            vx = (int) Math.round(3 * R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(3 * R * Math.sin(Math.toRadians(angle)));

        } else if (this.isSpeedBoosted() && (System.currentTimeMillis() - speedBoost < 5000)) {
            speedBoost = 0;
            this.isSpeedBoosted = false;
        }
        if(!idle){
            x -= vx;
            y -= vy;

        }
       checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        if (System.currentTimeMillis() - speedBoost < 5000 && isSpeedBoosted) {
            vx = (int) Math.round(3 * R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(3 * R * Math.sin(Math.toRadians(angle)));
        } else if (this.isSpeedBoosted() && (System.currentTimeMillis() - speedBoost < 5000)) {
            speedBoost = 0;
            this.isSpeedBoosted = false;
        }
        if(!idle){
            x += vx;
            y += vy;
        }
        checkBorder();
    }


    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameWorld.WORLD_WIDTH - 88) {
            x = GameWorld.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameWorld.WORLD_HEIGHT - 80) {
            y = GameWorld.WORLD_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics2D g2d) {  //might change
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
        if(this.health != 0){
            g2d.drawImage(this.img, rotation, null);
        }

    }

    private void placeBullet(int x, int y, int vx, int vy, int angle, GameWorld gWorld){
        Bullet bullet = new Bullet(x, y, angle);
        bullet.setOwnerOfBullet(name);
        gameWorld.addGameObjects(bullet);
    }

    public void collision(){
        this.removeHealth(20);
    }

    public void addHealth(int value){
        if(health + value > 100){
            health = 100;
        }
        else
            health += value;
    }

    private void removeHealth(int value){
        if(health  - value < 0){
            health = 0;
        }
        else
            health -= value;
    }

    public int getHealth() {
        return this.health;
    }

    Rectangle getOffSetBounds(){
        return new Rectangle(x + vx, y + vy, 50, 50);
    }

    long getSpeedBoost(){
        return this.speedBoost;
    }

    public void setSpeedBoost(long speedBoost) {
        this.speedBoost = speedBoost;
    }

    private boolean isSpeedBoosted() {
        return isSpeedBoosted;
    }

     void setSpeedBoosted(boolean speedBoosted) {
        isSpeedBoosted = speedBoosted;
    }

     boolean isIdle() {
        return idle;
    }

     void setIdle(boolean idle) {
        this.idle = idle;
    }
}
