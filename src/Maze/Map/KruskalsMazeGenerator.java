package Maze.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KruskalsMazeGenerator implements MazeGenerator {
    private static final int WALL = 1;
    private static final int PATH = 0;
    private class Edge {
        int x1, y1, x2, y2;
        Edge(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
    private class DisjointSet {
        private int[] parent;
        public DisjointSet(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }
        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }
        public void union(int x, int y) {
            parent[find(x)] = find(y);
        }
    }
    @Override
    public int[][] generateMaze(int width, int height) {
        int[][] maze = new int[height][width];

        // Initialize maze walls
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maze[y][x] = WALL;
            }
        }
        int cellsX = (width - 1) / 2;
        int cellsY = (height - 1) / 2;
        int totalCells = cellsX * cellsY;
        DisjointSet ds = new DisjointSet(totalCells);
        List<Edge> edges = new ArrayList<>();
        // Initialize cells and edges
        for (int y = 0; y < cellsY; y++) {
            for (int x = 0; x < cellsX; x++) {
                int cellX = x * 2 + 1;
                int cellY = y * 2 + 1;
                maze[cellY][cellX] = PATH;
                int cell = y * cellsX + x;
                // Add edges to the right and down
                if (x < cellsX - 1) {
                    edges.add(new Edge(cellX, cellY, cellX + 2, cellY));
                }
                if (y < cellsY - 1) {
                    edges.add(new Edge(cellX, cellY, cellX, cellY + 2));
                }
            }
        }
        Collections.shuffle(edges, new Random());
        // Kruskal's algorithm
        for (Edge edge : edges) {
            int cell1X = edge.x1;
            int cell1Y = edge.y1;
            int cell2X = edge.x2;
            int cell2Y = edge.y2;
            int cell1 = ((cell1Y - 1) / 2) * cellsX + (cell1X - 1) / 2;
            int cell2 = ((cell2Y - 1) / 2) * cellsX + (cell2X - 1) / 2;
            if (ds.find(cell1) != ds.find(cell2)) {
                ds.union(cell1, cell2);
                // Remove wall between cells
                int wallX = (cell1X + cell2X) / 2;
                int wallY = (cell1Y + cell2Y) / 2;
                maze[wallY][wallX] = PATH;
            }
        }
        return maze;
    }
}