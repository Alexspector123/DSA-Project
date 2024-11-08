package Mino;

import java.awt.Color;

import Mino.Mino;

public class Mino_Bar extends Mino {
    
    public Mino_Bar(){
        create(Color.cyan);
    }

    public void setXY(int x, int y){
        // o o o o b1 b0 b2 b3
        block[0].x = x;
        block[0].y = y;
        block[1].x = block[0].x - Block.SIZE;
        block[1].y = block[0].y;
        block[2].x = block[0].x + Block.SIZE;
        block[2].y = block[0].y;
        block[3].x = block[0].x + Block.SIZE*2;
        block[3].y = block[0].y;
    }
    public void getDirection1(){ 
        // o o o o
        tempBlock[0].x = block[0].x;
        tempBlock[0].y = block[0].y;
        tempBlock[1].x = block[0].x - Block.SIZE;
        tempBlock[1].y = block[0].y;
        tempBlock[2].x = block[0].x + Block.SIZE;
        tempBlock[2].y = block[0].y;
        tempBlock[3].x = block[0].x + Block.SIZE*2;
        tempBlock[3].y = block[0].y;

        updateXY(1);
    }
    public void getDirection2(){
        // o
        // o
        // o
        // o
        tempBlock[0].x = block[0].x;
        tempBlock[0].y = block[0].y;
        tempBlock[1].x = block[0].x;
        tempBlock[1].y = block[0].y - Block.SIZE;
        tempBlock[2].x = block[0].x;
        tempBlock[2].y = block[0].y + Block.SIZE;
        tempBlock[3].x = block[0].x;
        tempBlock[3].y = block[0].y + Block.SIZE*2;

        updateXY(2);
    }
    public void getDirection3(){
        getDirection1();
    }
    public void getDirection4(){
        getDirection2();
    }
}
