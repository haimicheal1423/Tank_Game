package TankWar;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseDetect implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e){
        int mouse_x = e.getX();
        int mouse_y = e.getY();


        if (mouse_x >= 360 && mouse_x <= 360 + 210) {
            if (mouse_y >= 416 && mouse_y <= 416 + 70 && GameWorld.defaultState != GameWorld.Game_State.help) {
                GameWorld.defaultState = GameWorld.Game_State.game;
            } else if (mouse_y >= 416 + 90 && mouse_y <= 416 + 90 + 70 && GameWorld.defaultState == GameWorld.Game_State.menu) { //we check to ensure that we are in the menu state, because that's the only time that we can possibly invoke a shift to a help state
                GameWorld.defaultState = GameWorld.Game_State.help;
            } else if (mouse_y >= 416 + 90 + 90 && mouse_y <= 416 + 90 + 90 + 70 && GameWorld.defaultState == GameWorld.Game_State.menu) { // To process clicks of exit button, we must be in the menu state(which shows the exit button)
                GameWorld.defaultState = GameWorld.Game_State.exit;
            }
        }

        if (GameWorld.defaultState == GameWorld.Game_State.help) {   //this block of code, serves to handle the pressing of the "return to main menu" button on the help screen
            if (mouse_x >= 15 && mouse_x <= 308) {
                if (mouse_y >= 553 && mouse_y <= 659) {
                    GameWorld.defaultState = GameWorld.Game_State.menu;
                }
            }
        }

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
