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
        currentGame = null;
        this.gamePanel = gamePanel;
    }
    public Game getCurrentGame() {
        return currentGame;
    }
    public void loadGame(String gameName) {
        if(currentGame != null) {
            currentGame.end();
        }
        this.currentGame = GameFactory.createNewGame(gameName, gamePanel);
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
        gamePanel.ui.g2.drawImage(gamePanel.ui.pauseScreen , x , y , null );
        // DRAW BUTTON:
        x += gamePanel.tileSize * 7;
        y += gamePanel.tileSize * 5;
        // TETRIS BUTTON:
        if (gamePanel.ui.commandNum == 0) {
            gamePanel.ui.g2.drawImage(gamePanel.ui.gameFrameTetris2 , x , y - 65, null );
        } 
        else {
            gamePanel.ui.g2.drawImage(gamePanel.ui.gameFrameTetris1 , x , y - 65, null );
        }
        // MAZE BUTTON:
        if (gamePanel.ui.commandNum == 1) {
            gamePanel.ui.g2.drawImage(gamePanel.ui.gameFrameMaze2 , 2*x, y - 65, null );
        } 
        else {
            gamePanel.ui.g2.drawImage(gamePanel.ui.gameFrameMaze1 , 2*x, y - 65, null );
        }
        // RETURN BUTTON:
        if (gamePanel.ui.commandNum == 2) {
            gamePanel.ui.g2.drawImage(gamePanel.ui.exitButton1 , (int) 2.5 * x , y * 2, null );
        } 
        else {
            gamePanel.ui.g2.drawImage(gamePanel.ui.exitButton2 , (int) 2.5 * x , y * 2, null );
        }
    }
}

