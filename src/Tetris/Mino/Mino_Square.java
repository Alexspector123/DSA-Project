package Tetris.Mino;

import java.awt.Color;

public class Mino_Square extends Mino {

    public Mino_Square(){
        create(Color.yellow);
    }

    public void setXY(int x, int y){
        // o o b0 b2
        // o o b1 b3
        block[0].x = x;
        block[0].y = y;
        block[1].x = block[0].x;
        block[1].y = block[0].y + Block.SIZE;
        block[2].x = block[0].x + Block.SIZE;
        block[2].y = block[0].y;
        block[3].x = block[0].x + Block.SIZE;
        block[3].y = block[0].y + Block.SIZE;
    }
    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}
}
