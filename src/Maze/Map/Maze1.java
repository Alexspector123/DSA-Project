package Maze.Map;

public class Maze1 {
     // Maze layout: 0 = open path, 1 = wall, 2 = player, 3 = exit, 4 = bot
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

    // Getters for the maze and various positions
    public static int[][] getMaze() {
        return maze;
    }

    public static int[] getExitPosition() {
        return exit;
    }

    public static int getPlayerX() {
        return playerX;
    }

    public static int getPlayerY() {
        return playerY;
    }

    public static int getBotX() {
        return botX;
    }

    public static int getBotY() {
        return botY;
    }

    // Optionally, you can add setters if you want to change the player/bot positions externally
    public static void setPlayerPosition(int x, int y) {
        playerX = x;
        playerY = y;
    }

    public static void setBotPosition(int x, int y) {
        botX = x;
        botY = y;
    }
}
