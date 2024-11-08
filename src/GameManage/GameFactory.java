package GameManage;

import Main.GameManager;
import Main.GamePanel;
import Main_Tetris.Tetris;
import Maze.Main_Maze.Maze;

public class GameFactory {
    public static Game createNewGame(String gameName, GamePanel gamePanel) {
        switch (gameName) {
            case "Tetris":
                return new Tetris(gamePanel);
            case "Maze":
                return new Maze(gamePanel);
            default:
                throw new IllegalArgumentException("System cannot load the game " + gameName);
        }
    }
}
