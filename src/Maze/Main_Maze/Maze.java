package Maze.Main_Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import Main.GamePanel;
import Maze.Map.Maze1;
import javax.swing.Timer;

import GameManage.Game;

public class Maze extends Game {
    GamePanel gamePanel;
    private static final int[][] maze = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 2, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 0, 1, 0, 1, 0, 1, 0, 1},
        {1, 1, 0, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 0, 1, 3, 1},
        {1, 0, 1, 1, 1, 1, 0, 1, 1, 1},
        {1, 4, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private static final int[] exit = {5, 8};  // Exit position
    private static int playerX = 1, playerY = 1;  // Player position
    private static int botX = 7, botY = 1;       // Bot position
    private static List<int[]> botPath = new ArrayList<>(); // Bot calculated path
    private Timer timer; // For refreshing the bot's movements

    private int dx = 0, dy = 0;
    public Bot bot;
    
    public Maze(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.bot = Bot.getInstance();

        botPath = calculateShortestPath(botX, botY);
        timer = new Timer(1000, e -> updateBot());
        timer.start();
    }

    public void update() {

        int newX = playerX + dx;
        int newY = playerY + dy;
        if (isValidMove(newX, newY)) {
            playerX = newX;
            playerY = newY;
        } else {
            System.out.println("You hit a wall!");
        }

        gamePanel.repaint();
        checkWinner();
    }

    private void updateBot() {
        if (!botPath.isEmpty()) {
            int[] nextStep = botPath.remove(0);
            botX = nextStep[0];
            botY = nextStep[1];
        }
        gamePanel.repaint();
        checkWinner();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tileSize = 60; // Size of each tile in the maze

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 1) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                } else if (i == exit[0] && j == exit[1]) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }

        // Draw player
        g2d.setColor(Color.BLUE);
        g2d.fillOval(playerY * tileSize + 10, playerX * tileSize + 10, tileSize - 20, tileSize - 20);

        // Draw bot
        g2d.setColor(Color.RED);
        g2d.fillOval(botY * tileSize + 10, botX * tileSize + 10, tileSize - 20, tileSize - 20);
    }

    public void end() {

    }

    private void checkWinner() {
        
    }

    private static boolean isValidMove(int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }

    private static List<int[]> calculateShortestPath(int startX, int startY) {
        int rows = maze.length;
        int cols = maze[0].length;

        int[][] dist = new int[rows][cols];
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        dist[startX][startY] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> dist[a[0]][a[1]]));
        pq.add(new int[]{startX, startY});

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  // Up, Down, Left, Right
        int[][][] parent = new int[rows][cols][2];  // Store parent for path reconstruction

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int x = current[0], y = current[1];

            if (x == exit[0] && y == exit[1]) break;  // Exit found

            for (int[] dir : directions) {
                int newX = x + dir[0], newY = y + dir[1];
                if (isValidMove(newX, newY) && dist[newX][newY] > dist[x][y] + 1) {
                    dist[newX][newY] = dist[x][y] + 1;
                    parent[newX][newY][0] = x;
                    parent[newX][newY][1] = y;
                    pq.add(new int[]{newX, newY});
                }
            }
        }

        // Reconstruct path from the bot to the exit
        List<int[]> path = new ArrayList<>();
        int x = exit[0], y = exit[1];
        while (!(x == startX && y == startY)) {
            path.add(0, new int[]{x, y});
            int tempX = parent[x][y][0];
            int tempY = parent[x][y][1];
            x = tempX;
            y = tempY;
        }

        return path;
    }
}
