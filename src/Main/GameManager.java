package Main;

import GameManage.Game;
import GameManage.GameFactory;
import Maze.Main_Maze.Maze;
import Tetris.Main_Tetris.Tetris;

public class GameManager {
    private static GameManager instance;
    private Game currentGame;
    GamePanel gamePanel;
    private GameManager() {}

    public static GameManager getInstance() {
        if(instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void loadGame(String gameName) {
        if(currentGame != null) {
            currentGame.end();
        }

        currentGame = GameFactory.createNewGame(gameName, gamePanel);
    }

    public void stopGame() {
        if(currentGame != null) {
            currentGame.end();
        }
    }

    public void choose(String gameName) {
         if (currentGame != null) {
            currentGame.end();
        }

        switch(gameName) {
            case "Tetris":
                currentGame = new Tetris(gamePanel);
                break;
            case "Maze":
                currentGame = new Maze(gamePanel);
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Tetris.");
                currentGame = new Tetris(gamePanel);
        }

        // Start the selected game
        //currentGame.start();
    }
}

