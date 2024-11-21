package Maze.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimsMazeGenerator implements MazeGenerator {
    private static final int WALL = 1;
    private static final int PATH = 0;

    @Override
    public int[][] generateMaze(int width, int height) {
        int[][] maze = new int[height][width];

        // Initialize maze with walls
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maze[y][x] = WALL;
            }
        }

        Random rand = new Random();
        List<int[]> frontier = new ArrayList<>();

        // Start at a random cell
        int startX = 1;
        int startY = 1;
        maze[startY][startX] = PATH;

        // Add walls of the starting cell to the wall list
        addFrontier(startX, startY, maze, frontier);

        while (!frontier.isEmpty()) {
            // Randomly select a frontier cell
            int[] current = frontier.remove(rand.nextInt(frontier.size()));
            int x = current[0];
            int y = current[1];

            // Get neighbors that are in the maze (visited cells)
            List<int[]> neighbors = getVisitedNeighbors(x, y, maze);

            if (!neighbors.isEmpty()) {
                // Randomly select one neighbor
                int[] neighbor = neighbors.get(rand.nextInt(neighbors.size()));

                // Remove the wall between the current cell and the neighbor
                int wallX = x + (neighbor[0] - x) / 2;
                int wallY = y + (neighbor[1] - y) / 2;
                maze[y][x] = PATH;
                maze[wallY][wallX] = PATH;

                // Add unvisited neighbors of current cell to frontier
                addFrontier(x, y, maze, frontier);
            }
        }

        return maze;
    }

    private void addFrontier(int x, int y, int[][] maze, List<int[]> frontier) {
        int width = maze[0].length;
        int height = maze.length;

        int[][] directions = {
            {x - 2, y},
            {x + 2, y},
            {x, y - 2},
            {x, y + 2}
        };

        for (int[] dir : directions) {
            int nx = dir[0];
            int ny = dir[1];
            if (nx > 0 && nx < width && ny > 0 && ny < height) {
                if (maze[ny][nx] == WALL) {
                    maze[ny][nx] = WALL; // Mark as frontier
                    frontier.add(new int[]{nx, ny});
                }
            }
        }
    }

    private List<int[]> getVisitedNeighbors(int x, int y, int[][] maze) {
        List<int[]> neighbors = new ArrayList<>();
        int[][] directions = {
            {x - 2, y},
            {x + 2, y},
            {x, y - 2},
            {x, y + 2}
        };

        for (int[] dir : directions) {
            int nx = dir[0];
            int ny = dir[1];
            if (nx > 0 && nx < maze[0].length && ny > 0 && ny < maze.length) {
                if (maze[ny][nx] == PATH) {
                    neighbors.add(new int[]{nx, ny});
                }
            }
        }

        return neighbors;
    }
}
