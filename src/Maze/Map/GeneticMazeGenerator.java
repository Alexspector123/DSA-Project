package Maze.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticMazeGenerator implements MazeGenerator {
    private static final int WALL = 1;
    private static final int PATH = 0;
    private static final int POPULATION_SIZE = 50;
    private static final int GENERATIONS = 1000;
    private static final double MUTATION_RATE = 0.01;

    private static final int[] START = {1, 1};
    private static final int[] EXIT = {19, 19}; // Adjust based on maze size

    @Override
    public int[][] generateMaze(int width, int height) {
        // Initialize population
        List<int[][]> population = initializePopulation(width, height);
        Random rand = new Random();

        for (int gen = 0; gen < GENERATIONS; gen++) {
            // Evaluate fitness
            List<FitnessMaze> fitnessList = new ArrayList<>();
            for (int[][] maze : population) {
                double fitness = calculateFitness(maze, width, height);
                fitnessList.add(new FitnessMaze(maze, fitness));
            }

            // Sort based on fitness (higher is better)
            fitnessList.sort((a, b) -> Double.compare(b.fitness, a.fitness));

            // Check if a perfect maze is found
            if (fitnessList.get(0).fitness >= 100.0) {
                return fitnessList.get(0).maze;
            }

            // Selection: Select top 20% as parents
            int parentsCount = POPULATION_SIZE / 5;
            List<int[][]> parents = new ArrayList<>();
            for (int i = 0; i < parentsCount; i++) {
                parents.add(fitnessList.get(i).maze);
            }

            // Crossover and mutation to create new population
            List<int[][]> newPopulation = new ArrayList<>();
            while (newPopulation.size() < POPULATION_SIZE) {
                // Select two random parents
                int[][] parent1 = parents.get(rand.nextInt(parents.size()));
                int[][] parent2 = parents.get(rand.nextInt(parents.size()));

                // Crossover
                int[][] child = crossover(parent1, parent2, width, height);

                // Mutation
                mutate(child, width, height);

                newPopulation.add(child);
            }

            population = newPopulation;
        }

        // Return the best maze after all generations
        population.sort((a, b) -> Double.compare(calculateFitness(b, width, height), calculateFitness(a, width, height)));
        return population.get(0);
    }

    private List<int[][]> initializePopulation(int width, int height) {
        List<int[][]> population = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[][] maze = new int[height][width];
            // Initialize maze with random walls
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x == START[0] && y == START[1] ||
                        x == EXIT[0] && y == EXIT[1]) {
                        maze[y][x] = PATH;
                    } else {
                        maze[y][x] = rand.nextDouble() < 0.3 ? PATH : WALL;
                    }
                }
            }
            population.add(maze);
        }

        return population;
    }

    private double calculateFitness(int[][] maze, int width, int height) {
        // Fitness components:
        // 1. Path connectivity from start to exit
        // 2. Number of dead-ends (optional)

        double fitness = 0.0;

        // Check connectivity using BFS
        boolean connected = isConnected(maze, width, height, START, EXIT);
        fitness += connected ? 50.0 : 0.0;

        // Count dead-ends
        int deadEnds = countDeadEnds(maze, width, height);
        fitness += deadEnds;

        // Optional: Add more fitness criteria as needed

        return fitness;
    }

    private boolean isConnected(int[][] maze, int width, int height, int[] start, int[] exit) {
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
            int[][] neighbors = {
                {x + 1, y},
                {x - 1, y},
                {x, y + 1},
                {x, y - 1}
            };

            for (int[] neighbor : neighbors) {
                int nx = neighbor[0];
                int ny = neighbor[1];
                if (nx > 0 && nx < width && ny > 0 && ny < height &&
                    maze[ny][nx] == PATH && !visited[ny][nx]) {
                    queue.add(neighbor);
                    visited[ny][nx] = true;
                }
            }
        }

        return false;
    }

    private int countDeadEnds(int[][] maze, int width, int height) {
        int deadEnds = 0;

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (maze[y][x] == PATH) {
                    int paths = 0;
                    if (maze[y + 1][x] == PATH) paths++;
                    if (maze[y - 1][x] == PATH) paths++;
                    if (maze[y][x + 1] == PATH) paths++;
                    if (maze[y][x - 1] == PATH) paths++;

                    if (paths == 1) {
                        deadEnds++;
                    }
                }
            }
        }

        return deadEnds;
    }

    private int[][] crossover(int[][] parent1, int[][] parent2, int width, int height) {
        int[][] child = new int[height][width];
        Random rand = new Random();

        // Single-point crossover
        int crossoverPoint = rand.nextInt(height * width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                if (index < crossoverPoint) {
                    child[y][x] = parent1[y][x];
                } else {
                    child[y][x] = parent2[y][x];
                }
            }
        }

        // Ensure start and exit are paths
        child[START[1]][START[0]] = PATH;
        child[EXIT[1]][EXIT[0]] = PATH;

        return child;
    }

    private void mutate(int[][] maze, int width, int height) {
        Random rand = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((x == START[0] && y == START[1]) ||
                    (x == EXIT[0] && y == EXIT[1])) {
                    continue; // Do not mutate start and exit
                }

                if (rand.nextDouble() < MUTATION_RATE) {
                    maze[y][x] = maze[y][x] == WALL ? PATH : WALL;
                }
            }
        }
    }

    // Helper class to store maze and its fitness
    private class FitnessMaze {
        int[][] maze;
        double fitness;

        public FitnessMaze(int[][] maze, double fitness) {
            this.maze = maze;
            this.fitness = fitness;
        }
    }
}
