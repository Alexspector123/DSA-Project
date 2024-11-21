package Maze.Map;

import Maze.Main_Maze.MazeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    int[][] maze;
    int[] start;
    int[] botStartD;
    int[] botStartA;
    int[] exit;
    MazeNode node;

    private static final int WIDTH = 13;
    private static final int HEIGHT = 13; 

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

    public static MazeNode createNewNode(int[][] maze, int[] start, int[] botStartD, int[] botStartA, int[] exit) {
        return new MazeNode(maze, start, botStartD, botStartA, exit);
    }

    private void generateMazeRandomly() {
        MazeGenerator[] generators = {
            new PrimsMazeGenerator(),
            new KruskalsMazeGenerator(),
            //new GeneticMazeGenerator()
        };
        Random rand = new Random();
        MazeGenerator selectedGenerator = generators[rand.nextInt(generators.length)];
        this.maze = selectedGenerator.generateMaze(WIDTH, HEIGHT);
        reduceWalls(10); 
    }

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

    private void reduceWalls(int percentage) {
        Random rand = new Random();
        int width = maze[0].length;
        int height = maze.length;
    
        for (int y = 1; y < height - 1; y += 1) {
            for (int x = 1; x < width - 1; x += 1) {
                if (maze[y][x] == 1 && rand.nextInt(100) < percentage) {
                    maze[y][x] = 0;
                }
            }
        }
    }

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
