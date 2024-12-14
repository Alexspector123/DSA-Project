package Maze.Main_Maze;

public class MazeNode {
    public int[][] maze;
    public int[] start;    
    int[] botStartD;
    int[] botStartA;
    public int[] exit;    
    public MazeNode next;

    public MazeNode(int[][] maze, int[] start, int[] botStartD, int[] botStartA, int[] exit) {
        this.maze = maze;
        this.start = start;
        this.botStartD = botStartD;
        this.botStartA = botStartA;
        this.exit = exit;
        this.next = null;
    }

    public static void mappingNode(MazeNode n1, MazeNode n2) {
        n1.next = n2;
    }
}
