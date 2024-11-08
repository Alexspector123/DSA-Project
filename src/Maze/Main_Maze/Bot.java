package Maze.Main_Maze;

import GameManage.Game;
import Main.GameManager;
import Main.GamePanel;

public class Bot {
    private static Bot instance;
    private Game currentGame;
    GamePanel gamePanel;
    private Bot() {}

    public static Bot getInstance() {
        if(instance == null) {
            instance = new Bot();
        }
        return instance;
    }
}
