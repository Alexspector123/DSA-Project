package Main;

import GameManage.Game;
import GameManage.GameFactory;
import Maze.Main_Maze.Maze;
import Main_Tetris.Tetris;

public class GameManager extends Game{
    private static GameManager instance;
    private Game currentGame;
    GamePanel gamePanel;
    UI ui;
    private GameManager() {}

    public static GameManager getInstance() {
        if(instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setUp(GamePanel gamePanel, UI ui) {
        this.gamePanel = gamePanel;
        this.ui = ui;
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
        ui.g2.drawImage( ui.titleScreen , x , y , null );
        // DRAW BUTTON:
        x += gamePanel.tileSize * 16;
        y += gamePanel.tileSize * 5;
        // TETRIS BUTTON:
        if ( ui.commandNum == 0 ) {
            ui.g2.drawImage( ui.playButton1 , x , y , null );
        } else {
            ui.g2.drawImage( ui.playButton2 , x , y , null );
        }

        // MAZE BUTTON:
        y += 82;
        if ( ui.commandNum == 1 ) {
            ui.g2.drawImage( ui.playButton1 , x , y , null );
        } else {
            ui.g2.drawImage( ui.playButton2 , x , y , null );
        }
        // RETURM BUTTON:
        y += 82;
        if ( ui.commandNum == 2 ) {
            ui.g2.drawImage( ui.exitButton1 , x , y , null );
        } else {
            ui.g2.drawImage( ui.exitButton2 , x , y , null );
        }
    }
}

