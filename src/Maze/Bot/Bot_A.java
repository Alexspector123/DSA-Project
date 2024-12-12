package Maze.Bot;

import java.util.*;

public class Bot_A {
    private static Bot_A instance;
    private Bot_A() {}
    public static Bot_A getInstance() {
        if(instance == null) {
            instance = new Bot_A();
        }
        return instance;
    }
    public List<int[]> calculateShortestPath(int startX, int startY, int[][] maze, int[] target) {
        int r = maze.length;
        int c = maze[0].length;
        int[][] gScore = new int[r][c];
        for(int[] row : gScore){
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        gScore[startX][startY] = 0;
        int[][][] parent = new int[r][c][2];
        boolean[][] visited = new boolean[r][c];
        PriorityQueue<int[]> openSet = new PriorityQueue<>((a, b) -> {
            int fA = gScore[a[0]][a[1]] + heuristic(a[0], a[1], target[0], target[1]);
            int fB = gScore[b[0]][b[1]] + heuristic(b[0], b[1], target[0], target[1]);
            return Integer.compare(fA, fB);
        });
        openSet.add(new int[]{startX, startY});
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        while(!openSet.isEmpty()){
            int[] current = openSet.poll();
            int x = current[0], y = current[1];
            if(visited[x][y]) continue;
            visited[x][y] = true;
            if(x == target[0] && y == target[1]) {
                return reconstructPath(parent, startX, startY, target[0], target[1]);
            }
            for(int[] dir : directions){
                int newX = x + dir[0], newY = y + dir[1];
                if(isValidMove(newX, newY, maze) && !visited[newX][newY]){
                    int tentativeGScore = gScore[x][y] + 1;
                    if(tentativeGScore < gScore[newX][newY]){
                        gScore[newX][newY] = tentativeGScore;
                        parent[newX][newY][0] = x;
                        parent[newX][newY][1] = y;
                        openSet.add(new int[]{newX, newY});
                    }
                }
            }
        }
        return new ArrayList<>();
    }
    private int heuristic(int x1, int y1, int x2, int y2){
        // Manhattan distance
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    private boolean isValidMove(int x, int y, int[][] maze){
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }
    private List<int[]> reconstructPath(int[][][] parent, int startX, int startY, int targetX, int targetY){
        List<int[]> path = new ArrayList<>();
        int x = targetX, y = targetY;
        while(!(x == startX && y == startY)){
            path.add(0, new int[]{x, y});
            int tempX = parent[x][y][0];
            int tempY = parent[x][y][1];
            x = tempX;
            y = tempY;
        }
        return path;
    }
    
}
