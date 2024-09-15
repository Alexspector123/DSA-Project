package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PlayManager {
    
    // Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    public PlayManager(){

        //Main Play Area Frame
        left_x = (GamePanel.Width/2) - (WIDTH/2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;
    
    }
    public void update(){


    }
    public void draw(Graphics2D graphics2D){

        //Draw Play Area Frame
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(4f));
        graphics2D.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        // Draw Next Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        graphics2D.drawRect(x, y, 200, 200);
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 30));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString("NEXT", x+60, y+60);
    }
}
