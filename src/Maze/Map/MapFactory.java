package Maze.Map;

import Maze.Main_Maze.MazeNode;

public class MapFactory {

    public static MazeNode createMazeLinkedList() {
        Map m1 = new Map();
        Map m2 = new Map();
        Map m3 = new Map();
        Map m4 = new Map();
        MazeNode.mappingNode(m1.node, m2.node);
        MazeNode.mappingNode(m2.node, m3.node);
        MazeNode.mappingNode(m3.node, m4.node);
        return m1.node;
    }
}
