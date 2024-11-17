package Maze.Map;

import Maze.Main_Maze.MazeNode;

public class Map {
    int[][] maze;
    int[] start;
    int[] botStartD;
    int[] botStartA;
    int[] exit;
    MazeNode node;

    public Map(int[][] maze, int[] start, int[] botStartD, int[] botStartA, int[] exit) {
        this.maze = maze;
        this.start = start;
        this.botStartD = botStartD;
        this.botStartA = botStartA;
        this.exit = exit;
        this.node = Map.createNewNode(maze, start, botStartD, botStartA, exit);
    }

    public static MazeNode createNewNode(int[][] maze, int[] start, int[] botStartD, int[] botStartA, int[] exit) {
        return new MazeNode(maze, start, botStartD, botStartA, exit);
    }
}
