package Maze.Bot;

import java.util.*;

public class Bot_D {
    private static Bot_D instance;

    private Bot_D() {}

    public static Bot_D getInstance() {
        if(instance == null) {
            instance = new Bot_D();
        }
        return instance;
    }

    public List<int[]> calculateShortestPath(int startX, int startY, int[][] maze, int[] target) {
        int r = maze.length;
        int c = maze[0].length;
        int[][] dist = new int[r][c];
        for(int[] row : dist){
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        dist[startX][startY] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> dist[a[0]][a[1]]));
        pq.add(new int[]{startX, startY});
        int[][][] parent = new int[r][c][2];
        boolean[][] flg = new boolean[r][c];
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        while(!pq.isEmpty()){
            int[] current = pq.poll();
            int x = current[0], y = current[1];
            if(flg[x][y]) continue;
            flg[x][y] = true;
            if(x == target[0] && y == target[1]) break;
            for(int[] d : directions){
                int newX = x + d[0], newY = y + d[1];
                if(isValidMove(newX, newY, maze) && dist[newX][newY] > dist[x][y] + 1){
                    dist[newX][newY] = dist[x][y] + 1;
                    parent[newX][newY][0] = x;
                    parent[newX][newY][1] = y;
                    pq.add(new int[]{newX, newY});
                }
            }
        }
        List<int[]> path = new ArrayList<>();
        int x = target[0], y = target[1];
        if(dist[x][y] == Integer.MAX_VALUE){
            return path;
        }
        while(!(x == startX && y == startY)){
            path.add(0, new int[]{x, y});
            int tempX = parent[x][y][0];
            int tempY = parent[x][y][1];
            x = tempX;
            y = tempY;
        }
        return path;
    }

    private boolean isValidMove(int x, int y, int[][] maze){
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }
}
