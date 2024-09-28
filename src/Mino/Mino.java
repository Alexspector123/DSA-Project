package Mino;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;
import Main.KeyHandler;
import Main.PlayManager;

public class Mino {
    
    public Block block[] = new Block[4];
    public Block tempBlock[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1;  // The direction of the Mino. Number 1 is default, then the others is based on the clockwise direction
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactiveCounter = 0;

    public void create(Color c){
        block[0] = new Block(c);
        block[1] = new Block(c);
        block[2] = new Block(c);
        block[3] = new Block(c);
        tempBlock[0] = new Block(c);
        tempBlock[1] = new Block(c);
        tempBlock[2] = new Block(c);
        tempBlock[3] = new Block(c);
    }
    public void setXY(int x, int y){}
    public void updateXY(int direction){

        checkRotationCollision();

        if(leftCollision == false && rightCollision == false && bottomCollision == false){
            this.direction = direction;
            block[0].x = tempBlock[0].x;
            block[0].y = tempBlock[0].y;
            block[1].x = tempBlock[1].x;
            block[1].y = tempBlock[1].y;
            block[2].x = tempBlock[2].x;
            block[2].y = tempBlock[2].y;
            block[3].x = tempBlock[3].x;
            block[3].y = tempBlock[3].y;
        }
        

    }
    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}
    public void checkMovementCollision(){
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlocksCollision();

        // Check frame Collision
        // Left wall
        for(int i=0; i<block.length; i++){
            if(block[i].x == PlayManager.left_x){
                leftCollision = true;
            }
        }
        // Right wall
        for(int i=0; i<block.length; i++){
            if(block[i].x + Block.SIZE == PlayManager.right_x){
                rightCollision = true;
            }
        }
        // Bottom wall
        for(int i=0; i<block.length; i++){
            if(block[i].y + Block.SIZE == PlayManager.bottom_y){
                bottomCollision = true;
            }   
        }
    }
    public void checkRotationCollision(){
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlocksCollision();

        // Check frame Collision
        // Left wall
        for(int i=0; i<block.length; i++){
            if(tempBlock[i].x < PlayManager.left_x){
                leftCollision = true;
            }
        }
        // Right wall
        for(int i=0; i<block.length; i++){
            if(tempBlock[i].x + Block.SIZE > PlayManager.right_x){
                rightCollision = true;
            }
        }
        // Bottom wall
        for(int i=0; i<block.length; i++){
            if(tempBlock[i].y + Block.SIZE > PlayManager.bottom_y){
                bottomCollision = true;
            }
        }
    }
    public void checkStaticBlocksCollision(){

        for(int i=0; i<PlayManager.staticBlocks.size(); i++){

            int TargetX = PlayManager.staticBlocks.get(i).x;
            int TargetY = PlayManager.staticBlocks.get(i).y;

            for(int j=0; j<block.length; j++){
                if(block[j].x == TargetX && block[j].y + Block.SIZE == TargetY){
                    bottomCollision = true;
                }
            }
            for(int j=0; j<block.length; j++){
                if(block[j].x - Block.SIZE == TargetX && block[j].y == TargetY){
                    leftCollision = true;
                }
            }
            for(int j=0; j<block.length; j++){
                if(block[j].x + Block.SIZE == TargetX && block[j].y == TargetY){
                    rightCollision = true;
                }
            }
        }
    }
    public void pushingMechanic(){

        int distance = PlayManager.bottom_y - PlayManager.top_y;

        for(int i=0; i<PlayManager.staticBlocks.size(); i++){

            for(int j=0; j<block.length; j++){
                
                if(PlayManager.staticBlocks.get(i).y - block[j].y - Block.SIZE < distance){
                    distance = PlayManager.staticBlocks.get(i).y - block[j].y - Block.SIZE;
                }
            }
        }

        for(int i=0; i<block.length; i++){
            if(PlayManager.bottom_y - block[i].y - Block.SIZE < distance){
                distance = PlayManager.bottom_y - block[i].y - Block.SIZE;
            }
        }
        block[0].y += distance;
        block[1].y += distance;
        block[2].y += distance;
        block[3].y += distance;
    }
    public void update(){

        if(deactivating){
            deactivating();
        }
        // Move the Mino
        if(KeyHandler.upPressed){
            switch (direction) {
                case 1: getDirection2(); break;
                case 2: getDirection3(); break;
                case 3: getDirection4(); break;
                case 4: getDirection1(); break;
            }
            KeyHandler.upPressed = false;
        }

        checkMovementCollision();   // Check the mino hit the wall or not

        if(KeyHandler.downPressed){
            
            if(bottomCollision == false){
                block[0].y += Block.SIZE;
                block[1].y += Block.SIZE;
                block[2].y += Block.SIZE;
                block[3].y += Block.SIZE;
    
                autoDropCounter = 0;
    
                KeyHandler.downPressed = false;
            }
            
        }
        if(KeyHandler.leftPressed){

            if(leftCollision == false){

                block[0].x -= Block.SIZE;
                block[1].x -= Block.SIZE;
                block[2].x -= Block.SIZE;
                block[3].x -= Block.SIZE;
    
                KeyHandler.leftPressed = false;
            }
        }
        if(KeyHandler.rightPressed){

            if(rightCollision == false){

                block[0].x += Block.SIZE;
                block[1].x += Block.SIZE;
                block[2].x += Block.SIZE;
                block[3].x += Block.SIZE;
    
                KeyHandler.rightPressed = false;
            }
        }
        if(KeyHandler.spacePressed){
            
            pushingMechanic();
            
            KeyHandler.spacePressed = false;
        }
        if(bottomCollision)
        {
            deactivating = true;
        }
        else{
            autoDropCounter++;
            if(autoDropCounter == PlayManager.dropInterval){
                // The Mino goes down
                block[0].y += Block.SIZE;
                block[1].y += Block.SIZE;
                block[2].y += Block.SIZE;
                block[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
        
    }
    public void deactivating(){

        deactiveCounter++;

        // Wait 45 frames until deactive
        if(deactiveCounter == 45){
            
            deactiveCounter = 0;
            checkMovementCollision();    // check if the bottom is still hitting

            // if the bottom is still hitting after 45 frames, deactive the mino
            if(bottomCollision){
                active = false;
            }
        }
    }
    public void draw(Graphics2D graphics2D){

        int margin = 2;
        graphics2D.setColor(block[0].c);
        graphics2D.fillRect(block[0].x+margin, block[0].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        graphics2D.fillRect(block[1].x+margin, block[1].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        graphics2D.fillRect(block[2].x+margin, block[2].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        graphics2D.fillRect(block[3].x+margin, block[3].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
    }
}
