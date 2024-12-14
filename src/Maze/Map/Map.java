package Maze.Map;

import Maze.Main_Maze.MazeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a single maze map.
 */
public class Map {
    int[][] maze;
    int[] start;
    int[] botStartD;
    int[] botStartA;
    int[] exit;
    MazeNode node;

    private static final int WIDTH = 13;
    private static final int HEIGHT = 13; 

    /**
     * Constructs a Map instance by generating a maze and ensuring path connectivity.
     */
    public Map() {
        do {
            generateMazeRandomly();
            this.start = new int[]{1, 1};
            this.exit = new int[]{WIDTH - 2, HEIGHT - 2};
            maze[start[1]][start[0]] = 0;
            maze[exit[1]][exit[0]] = 0;
            reduceWalls(10); 
        } while (!isPathExists());
        this.botStartD = findRandomPathPosition();
        this.botStartA = findRandomPathPosition();
        this.node = createNewNode(maze, start, botStartD, botStartA, exit);
    }

    /**
     * Creates a new MazeNode.
     *
     * @param maze The maze grid.
     * @param start The start coordinates.
     * @param botStartD Bot D's start coordinates.
     * @param botStartA Bot A's start coordinates.
     * @param exit The exit coordinates.
     * @return A new MazeNode instance.
     */
    public static MazeNode createNewNode(int[][] maze, int[] start, int[] botStartD, int[] botStartA, int[] exit) {
        return new MazeNode(maze, start, botStartD, botStartA, exit);
    }

    /**
     * Generates a maze using a randomly selected MazeGenerator from the factory.
     */
    private void generateMazeRandomly() {
        // Use the MazeGeneratorFactory to obtain a MazeGenerator
        MazeGenerator selectedGenerator = MazeGeneratorFactory.getRandomMazeGenerator();
        this.maze = selectedGenerator.generateMaze(WIDTH, HEIGHT);
        reduceWalls(10); 
    }

    /**
     * Finds a random position within the maze paths, excluding start and exit.
     *
     * @return An array containing x and y coordinates.
     */
    public int[] findRandomPathPosition() {
        List<int[]> pathCells = new ArrayList<>();
        for (int y = 1; y < maze.length - 1; y++) {
            for (int x = 1; x < maze[0].length - 1; x++) {
                if (maze[y][x] == 0 && (x != start[0] || y != start[1]) && (x != exit[0] || y != exit[1])) {
                    pathCells.add(new int[]{x, y});
                }
            }
        }
        Random rand = new Random();
        return pathCells.get(rand.nextInt(pathCells.size()));
    }

    /**
     * Reduces the number of walls in the maze by a given percentage.
     *
     * @param percentage The percentage of walls to remove.
     */
    private void reduceWalls(int percentage) {
        Random rand = new Random();
        int width = maze[0].length;
        int height = maze.length;
    
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (maze[y][x] == 1 && rand.nextInt(100) < percentage) {
                    maze[y][x] = 0;
                }
            }
        }
    }

    /**
     * Checks if a valid path exists from the start to the exit using BFS.
     *
     * @return True if a path exists, false otherwise.
     */
    private boolean isPathExists() {
        int width = maze[0].length;
        int height = maze.length;
        boolean[][] visited = new boolean[height][width];
        List<int[]> queue = new ArrayList<>();
    
        queue.add(start);
        visited[start[1]][start[0]] = true;
    
        while (!queue.isEmpty()) {
            int[] current = queue.remove(0);
            int x = current[0];
            int y = current[1];
    
            if (x == exit[0] && y == exit[1]) {
                return true;
            }
    
            // Explore neighbors
            int[][] directions = {
                {x - 1, y},
                {x + 1, y},
                {x, y - 1},
                {x, y + 1}
            };
    
            for (int[] dir : directions) {
                int nx = dir[0];
                int ny = dir[1];
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (maze[ny][nx] == 0 && !visited[ny][nx]) {
                        visited[ny][nx] = true;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }
    
        return false;
    }
}
