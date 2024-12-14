package GameManage;
import java.awt.Graphics2D;

public abstract class Game {
    private int score;
    public void update(){};
    public void draw(Graphics2D graphics2D){};
    public void end(){};
    public void setScore(int score){};
    public int getScore(){
        return score;
    }
}
