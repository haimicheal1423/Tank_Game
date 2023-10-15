package TankWar;

import java.awt.*;

public class Menu {

    public void drawImage(Graphics graphic){
        Font f = new Font("Courier New", Font.BOLD, 24);
        graphic.setColor(Color.ORANGE);
        graphic.setFont(f);
        graphic.drawString("BEGIN!", 420, 470);
        graphic.drawString("EXIT", 430, 560);
        graphic.setColor(Color.white);
        graphic.drawRoundRect(360, 416, 210, 70, 20, 20);
        graphic.drawRoundRect(360, 416 + 90, 210, 70, 20, 20);
    }
}
