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
import Maze.Bot.*;
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

    private int bot2X, bot2Y;       // Bot 2 position
    private List<int[]> bot2Path;

    private BufferedImage up1, up2, up3;
    private BufferedImage down1, down2, down3;
    private BufferedImage left1, left2, left3;
    private BufferedImage right1, right2, right3;

    // Animation variables
    private String direction = "down";
    private int spriteNum = 1;
    private int spriteCounter = 0;

    private int botMoveCounter = 0;
    private int botMoveDelay = 30; 

    GamePanel gamePanel;
    KeyHandler keyHandler;
    Bot_D botD;
    Bot_A botA;

    public Maze(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.keyHandler = gamePanel.keyHandler;
        this.botD = Bot_D.getInstance();
        this.botA = Bot_A.getInstance();

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
        List<int[]> botStarts = new ArrayList<>();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 4) {
                    botStarts.add(new int[]{i, j});
                    // Reset maze value to 0 to avoid treating it as wall
                    maze[i][j] = 0;
                }
            }
        }

        if (botStarts.size() >= 2) {
            this.botX = botStarts.get(0)[0];
            this.botY = botStarts.get(0)[1];
            this.bot2X = botStarts.get(1)[0];
            this.bot2Y = botStarts.get(1)[1];
        } else if (botStarts.size() == 1) {
            this.botX = botStarts.get(0)[0];
            this.botY = botStarts.get(0)[1];
            this.bot2X = maze.length - 2;
            this.bot2Y = maze[0].length - 2;
        } else {
            this.botX = 1;
            this.botY = 1;
            this.bot2X = maze.length - 2;
            this.bot2Y = maze[0].length - 2;
        }

        
        this.botPath = botA.calculateShortestPath(botX, botY, maze, exit);
        this.bot2Path = botD.calculateShortestPath(bot2X, bot2Y, maze, exit);
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
    
            // Update animation frames when player move
            spriteCounter++;
            if (spriteCounter > 8) {
                spriteNum++;
                if (spriteNum > 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
    
            // Check player reache exit
            if(playerX == exit[0] && playerY == exit[1]){
                // Move to next maze if available
                if(currentMaze.next != null){
                    currentMaze = currentMaze.next;
                    loadCurrentMaze();
                } 
                else {
                    // Player wins the game
                    System.out.println("Player wins");
                    gamePanel.gameState = gamePanel.gameOverState;
                    return;
                }
            }
            System.out.println("Player position before move: (" + playerX + ", " + playerY + ")");
            // Move bot only when player moves
            botMoveCounter++;
            if (botMoveCounter >= botMoveDelay) {
                botMoveCounter = 0;
                moveBot();
            }
        } 
        else {
            // Reset animation if player cannot move
            spriteNum = 1;
        }
    }    

    private void moveBot(){
        if(botPath.isEmpty() || (botX == playerX && botY == playerY)){
            botPath = botD.calculateShortestPath(botX, botY, maze, new int[]{playerX, playerY});
        }

        if(!botPath.isEmpty()){
            int[] nextStep = botPath.remove(0);
            botX = nextStep[0];
            botY = nextStep[1];

            // Check if bot reaches player
            if(botX == playerX && botY == playerY){
                System.out.println("BotA caught the player!");
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }
        }

        // Move botB (Normal movement)
        if(bot2Path.isEmpty() || (bot2X == playerX && bot2Y == playerY)){
            bot2Path = botA.calculateShortestPath(bot2X, bot2Y, maze, exit);
        }

        if(!bot2Path.isEmpty()){
            int teleDistance = Math.min(3, botPath.size());
            int[] nextStep = bot2Path.remove(0);
            bot2X = nextStep[0];
            bot2Y = nextStep[1];

            for (int i = 0; i < teleDistance; i++) {
                botPath.remove(0);
            }

            // Check if bot reaches exit
            if(bot2X == exit[0] && bot2Y == exit[1]){
                System.out.println("BotB wins!");
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }

            // Check if bot meets player
            if(bot2X == playerX && bot2Y == playerY){
                System.out.println("BotB caught the player!");
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }
        }

        // Optional: Check if bots collide with each other
        if(botX == bot2X && botY == bot2Y){
            System.out.println("Bots have collided!");
            // Ínert the function handle this later
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

        // Draw botA's path 
        if (botPath != null && !botPath.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphics2D.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); 
            g2d.setColor(Color.RED);

            for (int[] step : botPath) {
                g2d.fillRect(step[1] * tileSize, step[0] * tileSize, tileSize, tileSize);
            }
            g2d.dispose();
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

       // Draw botA effect
       graphics2D.setColor(Color.MAGENTA);
       graphics2D.fillOval(botY * tileSize + 10, botX * tileSize + 10, tileSize - 20, tileSize - 20);

       // Draw botB
       graphics2D.setColor(Color.ORANGE); 
       graphics2D.fillOval(bot2Y * tileSize + 10, bot2X * tileSize + 10, tileSize - 20, tileSize - 20);
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
