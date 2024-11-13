package Maze.Map;

import java.util.Map;

import Maze.Main_Maze.MazeNode;

public class MapFactory {

    public static MazeNode createMazeLinkedList() {
        // Maze 1
        int[][] maze1 = {
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
        int[] start1 = {1, 1};
        int[] botStart1 = {7, 1};
        int[] exit1 = {5, 8};
        MazeNode node1 = new MazeNode(maze1, start1, botStart1, exit1);

        // Maze 2
        int[][] maze2 = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,2,0,0,0,0,0,0,3,1},
            {1,0,1,1,1,1,1,0,1,1},
            {1,0,1,0,0,0,1,0,1,1},
            {1,0,1,0,1,0,1,0,1,1},
            {1,0,1,0,1,0,1,0,1,1},
            {1,0,1,0,1,0,1,0,1,1},
            {1,0,0,0,1,0,0,0,4,1},
            {1,1,1,1,1,1,1,1,1,1}
        };
        int[] start2 = {1, 1};
        int[] botStart2 = {7, 8};
        int[] exit2 = {1, 8};
        MazeNode node2 = new MazeNode(maze2, start2, botStart2, exit2);
        node1.next = node2;
        return node1;
    }
}
