package Maze.Map;

import Maze.Main_Maze.MazeNode;

public class Map {
    int[][] maze;
    int[] start;
    int[] botStart;
    int[] exit;
    MazeNode node;

    public Map(int[][] maze, int[] start, int[] botStart, int[] exit) {
        this.maze = maze;
        this.start = start;
        this.botStart = botStart;
        this.exit = exit;
        this.node = Map.createNewNode(maze, start, botStart, exit);
    }

    public static MazeNode createNewNode(int[][] maze, int[] start, int[] botStart, int[] exit) {
        return new MazeNode(maze, start, botStart, exit);
    }
}
