package GameManage;

import Main.GameManager;
import Main.GamePanel;
import Maze.Main_Maze.Maze;
import Main_Tetris.Tetris;

public class GameFactory {
    public static Game createNewGame(String gameName, GamePanel gamePanel) {
        switch (gameName) {
            case "Tetris":
                return new Tetris(gamePanel);
            case "Maze":
                System.out.println("NGANNNNNNNNNN");
                return new Maze(gamePanel);
            default:
                throw new IllegalArgumentException("System cannot load the game " + gameName);
        }
    }
}
