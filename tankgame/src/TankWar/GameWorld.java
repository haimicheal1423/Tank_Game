package TankWar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.io.IOException;

public class GameWorld extends JPanel{
    private static final int SCREEN_HEIGHT = 672;
    private static final int SCREEN_WIDTH = 960;
    static final int WORLD_HEIGHT = 1920;
    static final int WORLD_WIDTH = 1536;
    private final int Tank1_Spawn_x = 200;
    private final int Tank1_Spawn_y = 150;
    private final int Tank1_Spawn_angle = 0;
    private final int Tank2_Spawn_x = 1200;
    private final int Tank2_Spawn_y = 1700;
    private final int Tank2_Spawn_angle = 180;
    private BufferedImage gameWorld;
    private Graphics2D buff;
    private JFrame jframe;
    private Tank t1;
    private Tank t2;
    private static BufferedImage tankImg;
    private static BufferedImage player1WinImg;
    private static BufferedImage player2WinImg;
    private static BufferedImage menuImg;
    //private static BufferedImage helpImg;  MIGHT DO HELP PAGE
    private boolean p1Win = false;
    private boolean p2Win = false;
    private int p1Lives = 3;
    private int p2Lives = 3;

    enum Game_State{
        menu, game, help, exit
    }

    private CollisionHandle cHandler;
    static private Menu menu;

    private ArrayList<GameObjects> gameObjects = new ArrayList<>();

    public void setGameObjects(ArrayList<GameObjects> gm){
        this.gameObjects = gm;
    }

    public void addGameObjects(GameObjects gameObj){
        this.gameObjects.add(gameObj);
    }

    static Game_State defaultState = Game_State.menu;

    public static void main(String[] args){
        Thread x;
        GameWorld tankWorld = new GameWorld();
        tankWorld.cHandler = new CollisionHandle();
        tankWorld.init();

        try{

            while(true){

                tankWorld.repaint();
                if(GameWorld.defaultState == Game_State.game){
                    for(int i = 0; i < tankWorld.gameObjects.size(); i++){
                        if(tankWorld.gameObjects.get(i) instanceof Bullet){
                            if(((Bullet) tankWorld.gameObjects.get(i)).isInactive()){
                                tankWorld.gameObjects.remove(i);
                                i--;
                            }
                            else{
                                tankWorld.gameObjects.get(i).update();
                            }
                        }
                        if(tankWorld.gameObjects.get(i) instanceof Tank){
                            if(((Tank) tankWorld.gameObjects.get(i)).getHealth() == 0){
                                if ((((Tank) tankWorld.gameObjects.get(i)).getName()).equals("Tank1")){ //Tank 1 is dead
                                    if(tankWorld.p1Lives > 1){
                                        tankWorld.p1Lives --;

                                        ((Tank) tankWorld.gameObjects.get(i)).setHealth(100);
                                        tankWorld.gameObjects.get(i).setX(tankWorld.Tank1_Spawn_x);
                                        tankWorld.gameObjects.get(i).setY(tankWorld.Tank1_Spawn_y);
                                        tankWorld.gameObjects.get(i).setAngle(tankWorld.Tank1_Spawn_angle);
                                    }
                                    else{ //p2 wins
                                        tankWorld.p1Lives = 0;
                                        tankWorld.p2Win = true;
                                        break;
                                    }
                                }
                                if ((((Tank) tankWorld.gameObjects.get(i)).getName()).equals("Tank2")){ //Tank 2 is dead
                                    if(tankWorld.p2Lives > 1){
                                        tankWorld.p2Lives --;

                                        ((Tank) tankWorld.gameObjects.get(i)).setHealth(100);
                                        tankWorld.gameObjects.get(i).setX(tankWorld.Tank2_Spawn_x);
                                        tankWorld.gameObjects.get(i).setY(tankWorld.Tank2_Spawn_y);
                                        tankWorld.gameObjects.get(i).setAngle(tankWorld.Tank2_Spawn_angle);
                                    }
                                    else{ //p1 wins
                                        tankWorld.p2Lives = 0;
                                        tankWorld.p1Win = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if(((tankWorld.gameObjects.get(i) instanceof BreakableWall) && ((BreakableWall) tankWorld.gameObjects.get(i)).getHealth() == 0)){
                            tankWorld.gameObjects.remove(i);
                        }
                    }

                    tankWorld.gameObjects = tankWorld.cHandler.Handler(tankWorld.gameObjects);

                    tankWorld.t1.update();
                    tankWorld.t2.update();

                    Thread.sleep(1000/144);
                }
                else if(defaultState == Game_State.exit){
                    tankWorld.jframe.dispose();
                    System.exit(0);
                }
            }

        }catch(InterruptedException e){

        }
    }

    private void init(){
        this.jframe = new JFrame("The Tank Game");
        this.gameWorld = new BufferedImage(GameWorld.WORLD_WIDTH, GameWorld.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage t1Imgs = null, bullet_image, background_image, unbreakable_wall_img, breakable_wall_img, exp_img, large_explosion_img;
        BufferedImage t2Imgs = null;

        try{
            t1Imgs = ImageIO.read(getClass().getResource("/resources/tank1.png"));
            t2Imgs = ImageIO.read(getClass().getResource("/resources/tank2.png"));

            unbreakable_wall_img = ImageIO.read(getClass().getResource("/resources/unbreak.jpg"));
            Wall.setUnBreakWallImg(unbreakable_wall_img);

            background_image = ImageIO.read(getClass().getResource("/resources/bg.bmp"));
            Wall.setBackgroundImg(background_image);

            breakable_wall_img =ImageIO.read(getClass().getResource("/resources/break1.jpg"));
            BreakableWall.setBreakableWallImg(breakable_wall_img);

            bullet_image = ImageIO.read(getClass().getResource("/resources/bullet1.gif"));
            Bullet.setBulletImg(bullet_image);
            System.out.println(bullet_image);

            exp_img = ImageIO.read(getClass().getResource("/resources/Explosion_small.gif"));
            Bullet.setSmallExplosionImg(exp_img);

            large_explosion_img = ImageIO.read(getClass().getResource("/resources/Explosion_large.gif"));
            Bullet.setBigExplosionImg(large_explosion_img);

            GameWorld.menuImg = ImageIO.read(getClass().getResource("/resources/title.png"));

            GameWorld.player1WinImg = ImageIO.read(getClass().getResource("/resources/player1_wins.png"));
            GameWorld.player2WinImg = ImageIO.read(getClass().getResource("/resources/player2_wins.png"));

            PowerUps.setHealthImg(ImageIO.read(getClass().getResource("/resources/health.png")));
            PowerUps.setSpeedImg(ImageIO.read(getClass().getResource("/resources/speed.png")));


        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        t1 = new Tank(Tank1_Spawn_x, Tank1_Spawn_y, 0, 0, Tank1_Spawn_angle, t1Imgs);
        t1.setName("Tank1");
        t2 = new Tank(Tank2_Spawn_x, Tank2_Spawn_y, 0, 0, Tank2_Spawn_angle, t2Imgs);
        t2.setName("Tank2");

        menu = new Menu();

        for(int i = 0; i < WORLD_WIDTH; i = i + 320){
            for(int j = 0; j < WORLD_HEIGHT; j = j + 240){
                gameObjects.add(new Wall(i, j, true));
            }
        }

        int[] game_map = { //width is 48, height is 60
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 2, 2, 2, 2, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        int column = 0;
        int index = 0;

        for(int i = 0; i < 60; i++){
            for(int j = 0; j < 48; j++){
                if(column == 60) {
                    column = 0;
                }

                int temp = game_map[index];
                if(temp != 0){
                    if(temp == 2){
                        gameObjects.add(new BreakableWall(j * 32, i * 32));
                    }
                    else{
                        gameObjects.add(new Wall(j * 32, i * 32, false));
                    }
                }
                column++;
                index++;
            }

        }

        gameObjects.add(t1);
        t1.setGameWorld(this);
        gameObjects.add(t2);
        t2.setGameWorld(this);

        PowerUps health1 = new PowerUps(780, 750, true, false);
        PowerUps health2 = new PowerUps(682, 750, true, false);
        PowerUps speed1 = new PowerUps(730, 852, false, true);
        gameObjects.add(health1);
        gameObjects.add(health2);
        gameObjects.add(speed1);

        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Q); //adding control
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);

        this.jframe.setLayout(new BorderLayout());
        this.jframe.add(this);

        this.jframe.addKeyListener(tc1);
        this.jframe.addKeyListener(tc2);
        this.addMouseListener(new MouseDetect());

        this.jframe.setSize(GameWorld.SCREEN_WIDTH + 20, GameWorld.SCREEN_HEIGHT + 40);
        this.jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);

        this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jframe.setVisible(true);

    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        buff = gameWorld.createGraphics();
        super.paintComponent(g2d);

        if(GameWorld.defaultState == Game_State.menu){
            (g).drawImage(menuImg, 0, 0, SCREEN_WIDTH + 2, SCREEN_HEIGHT, null);
            menu.drawImage(g);
        }
        else if(GameWorld.defaultState == Game_State.help){
            //(g).drawImage(menuImg, 0, 0, SCREEN_WIDTH + 2, SCREEN_HEIGHT, null);
        }
        else if(GameWorld.defaultState == Game_State.game){
            for(int i = 0; i < gameObjects.size(); i++){
                gameObjects.get(i).drawImage(buff);
            }

            int p1_x = t1.getX();
            int p2_x = t2.getX();
            int p1_y = t1.getY();
            int p2_y = t2.getY();


            if (p1_x < SCREEN_WIDTH / 4) {
                p1_x = SCREEN_WIDTH / 4;
            }
            if (p2_x < SCREEN_WIDTH / 4) {
                p2_x = SCREEN_WIDTH / 4;
            }
            if (p1_x > WORLD_WIDTH - SCREEN_WIDTH / 4) {
                p1_x = WORLD_WIDTH - SCREEN_WIDTH / 4;
            }
            if (p2_x > WORLD_WIDTH - SCREEN_WIDTH / 4) {
                p2_x = WORLD_WIDTH - SCREEN_WIDTH / 4;
            }
            if (p1_y < SCREEN_HEIGHT / 2) {
                p1_y = SCREEN_HEIGHT / 2;
            }
            if (p2_y < SCREEN_HEIGHT / 2) {
                p2_y = SCREEN_HEIGHT / 2;
            }
            if (p1_y > WORLD_HEIGHT - SCREEN_HEIGHT / 2) {
                p1_y = WORLD_HEIGHT - SCREEN_HEIGHT / 2;
            }
            if (p2_y > WORLD_HEIGHT - SCREEN_HEIGHT / 2) {
                p2_y = WORLD_HEIGHT - SCREEN_HEIGHT / 2;
            }

            BufferedImage leftSplitScreen = gameWorld.getSubimage(p1_x - SCREEN_WIDTH /4, p1_y - SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
            BufferedImage rightSplitScreen = gameWorld.getSubimage(p2_x - SCREEN_WIDTH /4, p2_y - SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2, SCREEN_HEIGHT);

            g2d.drawImage(leftSplitScreen, 0, 0 ,null);
            g2d.drawImage(rightSplitScreen, SCREEN_WIDTH / 2 + 5, 0 , null);

            g2d.drawImage(gameWorld, SCREEN_WIDTH / 2 - GameWorld.WORLD_WIDTH / 6 / 2, SCREEN_HEIGHT - GameWorld.WORLD_HEIGHT / 7, GameWorld.WORLD_WIDTH / 6, WORLD_HEIGHT / 7, null);
            g2d.setFont(new Font("Courier New", Font.BOLD, 30));

            g2d.setColor(Color.WHITE);
            g2d.drawString("Player1 lives: " + this.p1Lives, 10, 28);
            g2d.drawString("Player2 lives: " + this.p2Lives, SCREEN_WIDTH / 2 + 10, 28);

            g2d.drawString("[", 10, 58);
            g2d.drawString("[", SCREEN_WIDTH / 2 + 10, 58);
            g2d.drawString("]", 230, 58);
            g2d.drawString("]", SCREEN_WIDTH / 2 + 230, 58);
            g2d.setColor(Color.green);

            g2d.fillRect(25, 40, 2 * t1.getHealth(), 20);
            g2d.fillRect(SCREEN_WIDTH / 2 + 25, 40, 2 * t2.getHealth(), 20);

            if(p1Win){
                g2d.drawImage(player1WinImg, 0, 0, SCREEN_WIDTH + 10, SCREEN_HEIGHT, null);
            }
            if(p2Win){
                g2d.drawImage(player2WinImg, 0, 0, SCREEN_WIDTH + 10, SCREEN_HEIGHT, null);
            }
        }
    }

}
