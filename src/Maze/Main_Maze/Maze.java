package Maze.Main_Maze;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import GameManage.Game;
import Main.GamePanel;
import Main.KeyHandler;
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

    GamePanel gamePanel;
    KeyHandler keyHandler;
    Bot bot;

    public Maze(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.keyHandler = gamePanel.keyHandler;
        this.bot = Bot.getInstance();

        // Initialize maze linked list using MapFactory
        head = MapFactory.createMazeLinkedList();

        // Set the current maze to the head of the list
        currentMaze = head;
        loadCurrentMaze();
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
            playerMoved = true;
        } else if(KeyHandler.downPressed){
            dx = 1;
            dy = 0;
            playerMoved = true;
        } else if(KeyHandler.leftPressed){
            dx = 0;
            dy = -1;
            playerMoved = true;
        } else if(KeyHandler.rightPressed){
            dx = 0;
            dy = 1;
            playerMoved = true;
        }
        KeyHandler.upPressed = false;
        KeyHandler.downPressed = false;
        KeyHandler.leftPressed = false;
        KeyHandler.rightPressed = false;

        int newX = playerX + dx;
        int newY = playerY + dy;
        if(isValidMove(newX, newY)){
            playerX = newX;
            playerY = newY;
            if(playerX == exit[0] && playerY == exit[1]){
                // Move to next maze if available
                if(currentMaze.next != null){
                    currentMaze = currentMaze.next;
                    loadCurrentMaze();
                } else {
                    // player wins the game
                    System.out.println("Player wins the game!");
                    gamePanel.gameState = gamePanel.gameOptionState;
                    return;
                }
            }

            // Check if player meets bot
            if(playerX == botX && playerY == botY){
                gamePanel.gameState = gamePanel.gameOverState;
                return;
            }

            // Move bot only when player moves
            //moveBot();
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

    public void draw(Graphics2D graphics2D){
        // Draw maze
        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze[i].length; j++){
                if(maze[i][j] == 1){
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                } else if(i == exit[0] && j == exit[1]){
                    graphics2D.setColor(Color.GREEN);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                } else {
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }

        // Draw player
        graphics2D.setColor(Color.BLUE);
        graphics2D.fillOval(playerY * tileSize + 10, playerX * tileSize + 10, tileSize - 20, tileSize - 20);

        // Draw bot
        graphics2D.setColor(Color.RED);
        graphics2D.fillOval(botY * tileSize + 10, botX * tileSize + 10, tileSize - 20, tileSize - 20);
    }

    public void end(){
    }

    private boolean isValidMove(int x, int y){
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }
}
