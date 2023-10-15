package TankWar;

import java.awt.*;
import java.util.ArrayList;

public class CollisionHandle { // grabs all the game objects to respond to various collision situations

    ArrayList<GameObjects> Handler(ArrayList<GameObjects> gameObjects){
        for(int i = 0; i < gameObjects.size(); i++){
            for(int j = i; j < gameObjects.size(); j++){
                GameObjects objectAti = gameObjects.get(i);
                GameObjects objectAtj = gameObjects.get(j);

                if(i!=j){

                    if(objectAti instanceof Bullet && objectAtj instanceof Tank && !(((Bullet) objectAti).getOwnerOfBullet().equals(((Tank) objectAtj).getName())) && !((Bullet) objectAti).collision){
                        if(objectAti.rectangle.intersects(objectAtj.rectangle)){
                            objectAti.collision();
                            ((Bullet) objectAti).setSmall_explosion(false);
                            objectAtj.collision();
                        }
                    }
                    if(objectAti instanceof Tank && objectAtj instanceof Bullet && !((Bullet) objectAtj).getOwnerOfBullet().equals(((Tank) objectAti).getName()) && !((Bullet) objectAtj).collision){
                        if(objectAti.rectangle.intersects(objectAtj.rectangle)){
                            ((Bullet) objectAtj).setSmall_explosion(false);
                            objectAtj.collision();
                            objectAti.collision();
                        }
                    }
                    if(((objectAtj instanceof Bullet && objectAti instanceof BreakableWall && !((Bullet) objectAtj).collision))){
                        if(objectAti.rectangle.intersects(objectAtj.rectangle)){
                            objectAtj.collision();
                            objectAti.collision();
                        }
                    }
                    if (objectAti instanceof Tank && objectAtj instanceof BreakableWall){
                        Rectangle rectangle = ((Tank) objectAti).getOffSetBounds();
                        if(rectangle.intersects(objectAtj.rectangle)){
                            ((Tank) objectAti).setIdle(true);
                        }
                    }
                    if (objectAti instanceof BreakableWall && objectAtj instanceof Tank){
                        Rectangle rectangle = ((Tank) objectAtj).getOffSetBounds();
                        if(rectangle.intersects(objectAti.rectangle)){
                            ((Tank) objectAtj).setIdle(true);
                        }
                    }
                    if(objectAti instanceof Tank && objectAtj instanceof PowerUps){
                        if(objectAti.rectangle.intersects(objectAtj.rectangle)){
                            if(((PowerUps) objectAtj).healthBoost){
                                ((Tank) objectAti).setHealth(100);
                                System.out.println("Picked up health power up!!");
                                gameObjects.remove(j);
                            }
                            if(((PowerUps) objectAtj).speedBoost){
                                ((Tank) objectAti).setSpeedBoost((System.currentTimeMillis()));
                                ((Tank) objectAti).setSpeedBoosted(true);
                                System.out.println("Picked up speed power up!!");
                                gameObjects.remove(j);
                            }
                        }
                    }
                }
            }
        }

        return gameObjects;
    }
}
