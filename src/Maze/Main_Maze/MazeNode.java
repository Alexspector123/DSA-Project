package Maze.Main_Maze;

public class MazeNode {
    public int[][] maze;
    public int[] start;    
    public int[] botStart; 
    public int[] exit;    
    public MazeNode next;

    public MazeNode(int[][] maze, int[] start, int[] botStart, int[] exit) {
        this.maze = maze;
        this.start = start;
        this.botStart = botStart;
        this.exit = exit;
        this.next = null;
    }
}
