package Main;

import GameManage.Game;
import GameManage.GameFactory;

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
}

