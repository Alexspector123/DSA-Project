package Maze.Map;

import Maze.Main_Maze.MazeNode;

public class MapFactory {

    public static MazeNode createMazeLinkedList() {
        Map m1 = new Map();
        MazeNode currentNode = m1.node;
        for (int i = 0; i < 7; i++) {
            Map newMap = new Map();
            MazeNode.mappingNode(currentNode, newMap.node); 
            currentNode = newMap.node; 
        }
        return m1.node;
    }
}
