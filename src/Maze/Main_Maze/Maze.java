package Maze.Main_Maze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import GameManage.Game;
import Main.GamePanel;
import Main.KeyHandler;
import Main.UtilityTool;
import Maze.Bot.Bot_D;
import Maze.Map.MapFactory;

public class Maze extends Game {
    private MazeNode head;         
    private MazeNode currentMaze;   
    private int[][] maze;           
    private int[] exit;            

    private int playerX, playerY;  
    private int botX, botY;         
    private List<int[]> botPath;    
    private int tileSize = 60;      

    private BufferedImage up1, up2, up3;
    private BufferedImage down1, down2, down3;
    private BufferedImage left1, left2, left3;
    private BufferedImage right1, right2, right3;

    // Animation variables
    private String direction = "down";
    private int spriteNum = 1;
    private int spriteCounter = 0;

    GamePanel gamePanel;
    KeyHandler keyHandler;
    Bot_D bot;

    public Maze(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.keyHandler = gamePanel.keyHandler;
        this.bot = Bot_D.getInstance();

        // Initialize maze linked list using MapFactory
        head = MapFactory.createMazeLinkedList();

        // Set the current maze to the head of the list
        currentMaze = head;
        loadCurrentMaze();
        getBasePlayerImage();
    }

    private void loadCurrentMaze() {
        this.maze = currentMaze.maze;
        this.exit = currentMaze.exit;
        this.playerX = currentMaze.start[0];
        this.playerY = currentMaze.start[1];
        //this.botX = currentMaze.botStart[0];
        //this.botY = currentMaze.botStart[1];
        //this.botPath = bot.calculateShortestPath(botX, botY, maze, exit);
    }

    public void update(){
        int dx = 0, dy = 0;
        boolean playerMoved = false;
    
        if(KeyHandler.upPressed){
            dx = -1;
            dy = 0;
            direction = "up"; // Update direction
            playerMoved = true;
        } else if(KeyHandler.downPressed){
            dx = 1;
            dy = 0;
            direction = "down"; // Update direction
            playerMoved = true;
        } else if(KeyHandler.leftPressed){
            dx = 0;
            dy = -1;
            direction = "left"; // Update direction
            playerMoved = true;
        } else if(KeyHandler.rightPressed){
            dx = 0;
            dy = 1;
            direction = "right"; // Update direction
            playerMoved = true;
        }
    
        // Reset key presses after handling
        KeyHandler.upPressed = false;
        KeyHandler.downPressed = false;
        KeyHandler.leftPressed = false;
        KeyHandler.rightPressed = false;
    
        int newX = playerX + dx;
        int newY = playerY + dy;
        if(isValidMove(newX, newY)){
            playerX = newX;
            playerY = newY;
    
            // Update animation frames when the player moves
            spriteCounter++;
            if (spriteCounter > 8) {
                spriteNum++;
                if (spriteNum > 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
    
            // Check if player reaches exit
            if(playerX == exit[0] && playerY == exit[1]){
                // Move to next maze if available
                if(currentMaze.next != null){
                    currentMaze = currentMaze.next;
                    loadCurrentMaze();
                } else {
                    // Player wins the game
                    System.out.println("Player wins the game!");
                    gamePanel.gameState = gamePanel.gameOverState;
                    return;
                }
            }
            System.out.println("Player position before move: (" + playerX + ", " + playerY + ")");
            // Move bot only when player moves
            //moveBot();
        } else {
            // Reset animation if player cannot move
            spriteNum = 1;
        }
    }    

    private void moveBot(){
        if(botPath.isEmpty() || botX == playerX && botY == playerY){
            botPath = bot.calculateShortestPath(botX, botY, maze, exit);
        }

        if(!botPath.isEmpty()){
            int[] nextStep = botPath.remove(0);
            botX = nextStep[0];
            botY = nextStep[1];

            // Check if bot reaches exit
            if(botX == exit[0] && botY == exit[1]){
                System.out.println("Bot wins!");
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }

            // Check if bot meets player
            if(botX == playerX && botY == playerY){
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        // Draw maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 1) {
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                } else if (i == exit[0] && j == exit[1]) {
                    graphics2D.setColor(Color.GREEN);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                } else {
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }
    
        // Draw player with animation
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } else if (spriteNum == 2) {
                    image = up2;
                } else if (spriteNum == 3) {
                    image = up3;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else if (spriteNum == 2) {
                    image = down2;
                } else if (spriteNum == 3) {
                    image = down3;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } else if (spriteNum == 2) {
                    image = left2;
                } else if (spriteNum == 3) {
                    image = left3;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } else if (spriteNum == 2) {
                    image = right2;
                } else if (spriteNum == 3) {
                    image = right3;
                }
                break;
            default:
                image = down1;
                break;
        }

        if (image != null) {
            graphics2D.drawImage(image, playerY * tileSize, playerX * tileSize, tileSize, tileSize, null);
        } else {
            System.out.println("Image is null for direction: " + direction + ", spriteNum: " + spriteNum);
            // Fallback to drawing a simple circle if image is null
            graphics2D.setColor(Color.BLUE);
            graphics2D.fillOval(playerY * tileSize + 10, playerX * tileSize + 10, tileSize - 20, tileSize - 20);
        }

        // Draw bot
        graphics2D.setColor(Color.RED);
        graphics2D.fillOval(botY * tileSize + 10, botX * tileSize + 10, tileSize - 20, tileSize - 20);
    }
    

    public void end(){
    }

    private boolean isValidMove(int x, int y){
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }

    public BufferedImage getAnimationImages(){
        return null;
    }

    public void getBasePlayerImage(){
        // PLAYER WARRIOR IMAGES:
            down1 = setupPlayerWarrior("down_1");
            down2 = setupPlayerWarrior("down_2");
            down3 = setupPlayerWarrior("down_3");

            left1 = setupPlayerWarrior("left_1");
            left2 = setupPlayerWarrior("left_2");
            left3 = setupPlayerWarrior("left_3");

            right1 = setupPlayerWarrior("right_1");
            right2 = setupPlayerWarrior("right_2");
            right3 = setupPlayerWarrior("right_3");

            up1 = setupPlayerWarrior("up_1");
            up2 = setupPlayerWarrior("up_2");
            up3 = setupPlayerWarrior("up_3");

    }

    public BufferedImage setupPlayerWarrior(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        String filePath = "res/Entities/Player_Warrior/" + imagePath + ".png";
        File imageFile = new File(filePath);

        try (FileInputStream readImage = new FileInputStream(imageFile)) {
            image = ImageIO.read(readImage);
            image = uTool.scaleImage(image,gamePanel.tileSize + 16, gamePanel.tileSize + 16);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
