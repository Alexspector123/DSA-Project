package Main;

import GameManage.Game;
import GameManage.GameFactory;
import Main.GamePanel;

public class GameManager extends Game{
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

    public void setUp(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public Game getCurrentGame() {
        return currentGame;
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

    public void draw() {

        int x = 0;
        int y = 0;
        // DRAW MENU TITLE SCREEN:
        gamePanel.ui.g2.drawImage(gamePanel.ui.titleScreen , x , y , null );
        // DRAW BUTTON:
        x += gamePanel.tileSize * 16;
        y += gamePanel.tileSize * 5;
        // TETRIS BUTTON:
        if (gamePanel.ui.commandNum == 0 ) {
            gamePanel.ui.g2.drawImage(gamePanel.ui.playButton1 , x , y , null );
        } else {
            gamePanel.ui.g2.drawImage(gamePanel.ui.playButton2 , x , y , null );
        }
        // MAZE BUTTON:
        y += 82;
        if (gamePanel.ui.commandNum == 1 ) {
            gamePanel.ui.g2.drawImage(gamePanel.ui.playButton1 , x , y , null );
        } else {
            gamePanel.ui.g2.drawImage(gamePanel.ui.playButton2 , x , y , null );
        }
        // RETURM BUTTON:
        y += 82;
        if (gamePanel.ui.commandNum == 2 ) {
            gamePanel.ui.g2.drawImage(gamePanel.ui.exitButton1 , x , y , null );
        } else {
            gamePanel.ui.g2.drawImage(gamePanel.ui.exitButton2 , x , y , null );
        }
    }
}

