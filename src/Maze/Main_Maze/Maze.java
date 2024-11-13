package Maze.Main_Maze;

import java.awt.*;
import java.util.*;
import GameManage.Game;
import Main.GamePanel;
import Main.KeyHandler;
import java.util.List;
import java.util.ArrayList;

public class Maze extends Game {

    private static final int[][] maze = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 2, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 0, 1, 0, 1, 0, 1, 0, 1},
        {1, 1, 0, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 0, 1, 3, 1},
        {1, 0, 1, 1, 1, 1, 0, 1, 1, 1},
        {1, 4, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private static final int[] exit = {5, 8};  // Exit position
    private int playerX = 1, playerY = 1;  // Player position
    private int botX = 7, botY = 1;       // Bot position
    private List<int[]> botPath = new ArrayList<>(); // Bot calculated path
    private int tileSize = 60; // Size of each tile in the maze

    GamePanel gamePanel;
    KeyHandler keyHandler;
    Bot bot;

    public Maze(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.keyHandler = gamePanel.keyHandler;
        this.bot = Bot.getInstance();
        //botPath = bot.calculateShortestPath(botX, botY, maze, exit);
    }

    public void update(){
        // Handle player input
        int dx = 0, dy = 0;
        boolean playerMoved = false;
        if(keyHandler.upPressed){
            dx = -1;
            dy = 0;
            playerMoved = true;
        } else if(keyHandler.downPressed){
            dx = 1;
            dy = 0;
            playerMoved = true;
        } else if(keyHandler.leftPressed){
            dx = 0;
            dy = -1;
            playerMoved = true;
        } else if(keyHandler.rightPressed){
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

            // Check if player reaches exit
            if(playerX == exit[0] && playerY == exit[1]){
                System.out.println("Player wins!");
                gamePanel.gameState = gamePanel.gameOptionState;
                return;
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
        // Recalculate bot's path if necessary
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
        // Any cleanup code if necessary
    }

    private boolean isValidMove(int x, int y){
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }
}
