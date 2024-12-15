package Maze.Main_Maze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

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
    private int tileSize = 60;      

    // Bot D variables
    private int botDX, botDY;       
    private List<int[]> botDPath;    
    private String botDDirection = "down";
    private int botDSpriteNum = 1;
    private int botDSprite = 0;
    private boolean botDStopped = false;
    private int botDStopCounter = 0;
    private int botDStopDuration = 180;

    // Bot A variables
    private int botAX, botAY;       
    private List<int[]> botAPath;
    private String botADirection = "down";
    private int botASpriteNum = 1;
    private int botASprite = 0;
    private boolean botAStopped = false;
    private int botAStopCounter = 0;
    private int botAStopDuration = 180;

    private BufferedImage up1, up2, up3;
    private BufferedImage down1, down2, down3;
    private BufferedImage left1, left2, left3;
    private BufferedImage right1, right2, right3;

    private BufferedImage up1_A, up2_A, up3_A;
    private BufferedImage down1_A, down2_A, down3_A;
    private BufferedImage left1_A, left2_A, left3_A;
    private BufferedImage right1_A, right2_A, right3_A;

    private BufferedImage up1_D, up2_D, up3_D;
    private BufferedImage down1_D, down2_D, down3_D;
    private BufferedImage left1_D, left2_D, left3_D;
    private BufferedImage right1_D, right2_D, right3_D;

    private BufferedImage t1, t2, t3;
    private BufferedImage holeImage;

    // Animation variables
    private String direction = "down";
    private int spriteNum = 1;
    private int spriteCounter = 0;

    private int botDMoveCounter = 0;
    private int botAMoveCounter = 0;
    private int botMoveDelayD = 60;
    private int botMoveDelayA = 50; 

    private List<int[]> holePositions = new ArrayList<>();
    private int holeGenerationCounter = 0;
    private int holeGenerationDelay = 300;

    GamePanel gamePanel;
    KeyHandler keyHandler;
    Bot_D botD;
    Bot_A botA;

    public Maze(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.keyHandler = gamePanel.keyHandler;
        this.botD = Bot_D.getInstance();
        this.botA = Bot_A.getInstance();
        setScore(0);

        // Initialize maze linked list using MapFactory
        head = MapFactory.createMazeLinkedList();

        // Set the current maze to the head of the list
        currentMaze = head;
        loadCurrentMaze();
        getBasePlayerImage();
        getBaseBotAImage();
        getBaseBotDImage();
        getBaseTileImage();
        getHoleImage();
    }

    private void loadCurrentMaze() {
        this.maze = currentMaze.maze;
        this.exit = currentMaze.exit;
        this.playerX = currentMaze.start[0];
        this.playerY = currentMaze.start[1];
    
        // Bot D's starting position
        if (currentMaze.botStartD != null) {
            this.botDX = currentMaze.botStartD[0];
            this.botDY = currentMaze.botStartD[1];
        }
        else {
            // Default 
            this.botDX = 1;
            this.botDY = 1;
        }
    
        // Bot A's starting position
        if (currentMaze.botStartA != null) {
            this.botAX = currentMaze.botStartA[0];
            this.botAY = currentMaze.botStartA[1];
        } 
        else {
            // Default 
            this.botAX = maze.length - 2;
            this.botAY = maze[0].length - 2;
        }
    
        this.botDPath = botD.calculateShortestPath(botDX, botDY, maze, exit);
        this.botAPath = botA.calculateShortestPath(playerX, botAY, maze, new int[]{playerX, playerY});
        holePositions.clear();
    }

    public void update(){
        int dx = 0, dy = 0;
        boolean playerMoved = false;
    
        if(KeyHandler.upPressed){
            dx = -1;
            dy = 0;
            direction = "up"; // Update direction
            playerMoved = true;
        } 
        else if(KeyHandler.downPressed){
            dx = 1;
            dy = 0;
            direction = "down"; // Update direction
            playerMoved = true;
        } 
        else if(KeyHandler.leftPressed){
            dx = 0;
            dy = -1;
            direction = "left"; // Update direction
            playerMoved = true;
        } 
        else if(KeyHandler.rightPressed){
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

            // Check if player steps into a hole
            for (int[] hole : holePositions) {
                if (playerX == hole[0] && playerY == hole[1]) {
                    if (botMoveDelayD > 20) botMoveDelayD -= 10;
                    if (botMoveDelayA > 20) botMoveDelayA -= 10;
                    holePositions.remove(hole);
                    break;
                }
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
                    gamePanel.gameState = gamePanel.gameOverState;
                    return;
                }
                setScore(getScore() + 100);
            }
            else {
                // Reset animation if player cannot move
                spriteNum = 1;
            }

            holeGenerationCounter++;
            if (holeGenerationCounter >= holeGenerationDelay) {
                holeGenerationCounter = 0;
                generateRandomHole();
            }

            // Move bot 
            if (!botDStopped) {
                botDMoveCounter++;
                if (botDMoveCounter >= botMoveDelayD) {
                    botDMoveCounter = 0;
                    moveBotD();
                }
            } 
            else {
                botDStopCounter++;
                if (botDStopCounter >= botDStopDuration) {
                    botDStopCounter = 0;
                    botDStopped = false;
                }
            }
    
            if (!botAStopped) {
                botAMoveCounter++;
                if (botAMoveCounter >= botMoveDelayA) {
                    botAMoveCounter = 0;
                    moveBotA();
                }
            } 
            else {
                botAStopCounter++;
                if (botAStopCounter >= botAStopDuration) {
                    botAStopCounter = 0;
                    botAStopped = false;
                }
            }
        }     
    }   

    public void generateRandomHole() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(maze.length);
            y = rand.nextInt(maze[0].length);
        } 
        while (maze[x][y] != 0 || (x == playerX && y == playerY) || (x == exit[0] && y == exit[1]));
        holePositions.add(new int[]{x, y});
    }

    private void moveBotA() {
        for (int[] hole : holePositions) {
            if (botAX == hole[0] && botAY == hole[1]) {
                botAStopped = true;
                holePositions.remove(hole);
                return;
            }
        }

        // Move botB (Normal movement)
        if(botAPath.isEmpty() || (botAX == playerX && botAY == playerY)){
            botAPath = botA.calculateShortestPath(botAX, botAY, maze, new int[]{playerX, playerY});
        }

        if(!botAPath.isEmpty()){
            int teleDistance = Math.min(3, botAPath.size());
            int[] nextStep = botAPath.remove(0);

            int dx = nextStep[0] - botAX;
            int dy = nextStep[1] - botAY;

            if(dx == -1) {
                botADirection = "up";
            } 
            else if(dx == 1) {
                botADirection = "down";
            } 
            else if(dy == -1) {
                botADirection = "left";
            } 
            else if(dy == 1) {
                botADirection = "right";
            } 
            else {
                botADirection = "down"; 
            }

            botAX = nextStep[0];
            botAY = nextStep[1];

            botASprite++;
            if (botASprite > 8) {
                botASpriteNum++;
                if (botASpriteNum > 3) {
                    botASpriteNum = 1;
                }
                botASprite = 0;
            }

            // Teleport forward
            for (int i = 0; i < teleDistance - 1; i++) { 
                if(!botAPath.isEmpty()) {
                    int[] step = botAPath.remove(0);
                    botAX = step[0];
                    botAY = step[1];
                }
            }

            // Check if bot reaches player
            if(botAX == playerX && botAY == playerY){
                System.out.println("Bot A caught the player");
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }
        }
    }

    private void moveBotD(){
        for (int[] hole : holePositions) {
            if (botDX == hole[0] && botDY == hole[1]) {
                botDStopped = true;
                holePositions.remove(hole);
                return;
            }
        }

        if(botDPath.isEmpty() || (botDX == exit[0] && botDY == exit[1])){
            botDPath = botD.calculateShortestPath(botDX, botDY, maze, exit);
        }

        if(!botDPath.isEmpty()){
            int[] nextStep = botDPath.remove(0);
            int dx = nextStep[0] - botDX;
            int dy = nextStep[1] - botDY;

            if(dx == -1) {
                botDDirection = "up";
            }  
            else if(dx == 1) {
                botDDirection = "down";
            } 
            else if(dy == -1) {
                botDDirection = "left";
            } 
            else if(dy == 1) {
                botDDirection = "right";
            } 
            else {
                botDDirection = "down"; 
            }

            botDX = nextStep[0];
            botDY = nextStep[1];

            botDSprite++;
            if (botDSprite > 8) {
                botDSpriteNum++;
                if (botDSpriteNum > 3) {
                    botDSpriteNum = 1;
                }
                botDSprite = 0;
            }

            // Check if bot reaches exit
            if(botDX == exit[0] && botDY == exit[1]){
                System.out.println("Bot D wins");
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }

            // Check if bot meets player
            if(botDX == playerX && botDY == playerY){
                System.out.println("Bot D caught the player");
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }
        }

    }

    public void draw(Graphics2D graphics2D) {
        // Draw maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                int x = j * tileSize;
                int y = i * tileSize;
                if (maze[i][j] == 1) {
                    graphics2D.drawImage(t2, x, y, tileSize, tileSize, null);

                } 
                else if (i == currentMaze.exit[0] && j == currentMaze.exit[1]) {
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                    //graphics2D.drawImage(t3, x, y, tileSize, tileSize, null);
                } 
                else {
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                    //graphics2D.drawImage(t3, x, y, tileSize, tileSize, null);
                }
            }
        }

        // Draw holes
        for (int[] hole : holePositions) {
            graphics2D.drawImage(holeImage, hole[1] * tileSize, hole[0] * tileSize, tileSize, tileSize, null);
        }
    
        // Draw player with animation
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } 
                else if (spriteNum == 2) {
                    image = up2;
                } 
                else if (spriteNum == 3) {
                    image = up3;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } 
                else if (spriteNum == 2) {
                    image = down2;
                } 
                else if (spriteNum == 3) {
                    image = down3;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } 
                else if (spriteNum == 2) {
                    image = left2;
                } 
                else if (spriteNum == 3) {
                    image = left3;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } 
                else if (spriteNum == 2) {
                    image = right2;
                } 
                else if (spriteNum == 3) {
                    image = right3;
                }
                break;
            default:
                image = down1;
                break;
        }

        if (image != null) {
            graphics2D.drawImage(image, playerY * tileSize, playerX * tileSize, tileSize, tileSize, null);
        } 

        // Draw Bot A 
        if (botAPath != null && !botAPath.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphics2D.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); 
            g2d.setColor(Color.RED);

            for (int[] step : botAPath) {
                g2d.fillRect(step[1] * tileSize, step[0] * tileSize, tileSize, tileSize);
            }
            g2d.dispose();
        }

        BufferedImage botAImage = null;
        switch (botADirection) {
            case "up":
                if (botASpriteNum == 1) botAImage = up1_A;
                else if (botASpriteNum == 2) botAImage = up2_A;
                else if (botASpriteNum == 3) botAImage = up3_A;
                break;
            case "down":
                if (botASpriteNum == 1) botAImage = down1_A;
                else if (botASpriteNum == 2) botAImage = down2_A;
                else if (botASpriteNum == 3) botAImage = down3_A;
                break;
            case "left":
                if (botASpriteNum == 1) botAImage = left1_A;
                else if (botASpriteNum == 2) botAImage = left2_A;
                else if (botASpriteNum == 3) botAImage = left3_A;
                break;
            case "right":
                if (botASpriteNum == 1) botAImage = right1_A;
                else if (botASpriteNum == 2) botAImage = right2_A;
                else if (botASpriteNum == 3) botAImage = right3_A;
                break;
            default:
                botAImage = down1_A;
                break;
        }

        if (botAImage != null) {
            graphics2D.drawImage(botAImage, botAY * tileSize, botAX * tileSize, tileSize, tileSize, null);
        } 

        // Draw Bot D
        BufferedImage botDImage = null;
        switch (botDDirection) {
            case "up":
                if (botDSpriteNum == 1) botDImage = up1_D;
                else if (botDSpriteNum == 2) botDImage = up2_D;
                else if (botDSpriteNum == 3) botDImage = up3_D;
                break;
            case "down":
                if (botDSpriteNum == 1) botDImage = down1_D;
                else if (botDSpriteNum == 2) botDImage = down2_D;
                else if (botDSpriteNum == 3) botDImage = down3_D;
                break;
            case "left":
                if (botDSpriteNum == 1) botDImage = left1_D;
                else if (botDSpriteNum == 2) botDImage = left2_D;
                else if (botDSpriteNum == 3) botDImage = left3_D;
                break;
            case "right":
                if (botDSpriteNum == 1) botDImage = right1_D;
                else if (botDSpriteNum == 2) botDImage = right2_D;
                else if (botDSpriteNum == 3) botDImage = right3_D;
                break;
            default:
                botDImage = down1_D;
                break;
        }

        if (botDImage != null) {
            graphics2D.drawImage(botDImage, botDY * tileSize, botDX * tileSize, tileSize, tileSize, null);
        } 
        
    }
    

    public void end(){
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }

    public BufferedImage getAnimationImages() {
        return null;
    }

    public void getBasePlayerImage() {
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

    public void getBaseTileImage() {
        t1 = setupTile("ground");
        t2 = setupTile("stone_1_1");
        t3 = setupTile("stone_ 1_2");
    }

    public void getBaseBotAImage() {
        // BOT A IMAGES:
        down1_A = setupBotA("down_1");
        down2_A = setupBotA("down_2");
        down3_A = setupBotA("down_3");
        left1_A = setupBotA("left_1");
        left2_A = setupBotA("left_2");
        left3_A = setupBotA("left_3");
        right1_A = setupBotA("right_1");
        right2_A = setupBotA("right_2");
        right3_A = setupBotA("right_3");
        up1_A = setupBotA("up_1");
        up2_A = setupBotA("up_2");
        up3_A = setupBotA("up_3");

    }

    public void getBaseBotDImage(){
        // BOT D IMAGES:
        down1_D = setupBotD("down_1");
        down2_D = setupBotD("down_2");
        down3_D = setupBotD("down_3");
        left1_D = setupBotD("left_1");
        left2_D = setupBotD("left_2");
        left3_D = setupBotD("left_3");
        right1_D = setupBotD("right_1");
        right2_D = setupBotD("right_2");
        right3_D = setupBotD("right_3");
        up1_D = setupBotD("up_1");
        up2_D = setupBotD("up_2");
        up3_D = setupBotD("up_3");
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

    public BufferedImage setupBotA(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        String filePath = "res/Entities/Bot_A/" + imagePath + ".png";
        File imageFile = new File(filePath);

        try (FileInputStream readImage = new FileInputStream(imageFile)) {
            image = ImageIO.read(readImage);
            image = uTool.scaleImage(image,gamePanel.tileSize + 16, gamePanel.tileSize + 16);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public BufferedImage setupBotD(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        String filePath = "res/Entities/Merchant/" + imagePath + ".png";
        File imageFile = new File(filePath);

        try (FileInputStream readImage = new FileInputStream(imageFile)) {
            image = ImageIO.read(readImage);
            image = uTool.scaleImage(image,gamePanel.tileSize + 16, gamePanel.tileSize + 16);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public BufferedImage setupTile(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        String filePath = "res/Tile/" + imagePath + ".png";
        File imageFile = new File(filePath);

        try (FileInputStream readImage = new FileInputStream(imageFile)) {
            image = ImageIO.read(readImage);
            image = uTool.scaleImage(image,gamePanel.tileSize + 16, gamePanel.tileSize + 16);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void getHoleImage() {
        holeImage = setupTile("hole"); 
    }
}
