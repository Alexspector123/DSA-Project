package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Main_Tetris.Tetris;
import Maze.Main_Maze.Maze;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;
    public static boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed, holdPressed, releasePressed;

    // Game Name
    String gameName;

    // Debug
    boolean showDebugText = false;

    public KeyHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // KEYPRESSED:
        @Override
        public void keyPressed(KeyEvent e) {

            int code = e.getKeyCode();

        // TITLE STATE:
            if(gamePanel.gameState == gamePanel.titleState){
                titleState(code);
            }
        // CHOOSE GAME STATE:
            else if(gamePanel.gameState == gamePanel.gameOptionState){
                chooseGameState(code);
            }
        // GAME STATE:
            else if(gamePanel.gameState == gamePanel.playState){
                playState(code);
            }
        // PAUSE STATE:
            else if(gamePanel.gameState == gamePanel.pauseState){
                pauseState(code);
            }
            else if(gamePanel.gameState == gamePanel.gameOverState){
                gameOverState(code);
            }
    }
    // STATES
        // TITLE STATE
        public void titleState(int code){
            if(code == KeyEvent.VK_W)
            {
                gamePanel.ui.commandNum--;
                if(gamePanel.ui.commandNum < 0)
                    gamePanel.ui.commandNum = 1;
            }
            if(code == KeyEvent.VK_S)
            {
                gamePanel.ui.commandNum++;
                if(gamePanel.ui.commandNum > 1)
                    gamePanel.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH PLAY BUTTON:
                    if(gamePanel.ui.commandNum == 0){
                        gamePanel.tempGameState = gamePanel.titleState;
                        gamePanel.gameState = gamePanel.gameOptionState;
                    }
                    // PRESS ENTER WITH EXIT BUTTON:
                    if (gamePanel.ui.commandNum == 1) {
                        System.exit(0);
                    }
            }
        }
        // CHOOSE GAME STATE
        public void chooseGameState(int code){

            if(code == KeyEvent.VK_ESCAPE){
                gamePanel.gameState = gamePanel.titleState;
            }
            if(code == KeyEvent.VK_W)
            {
                gamePanel.ui.commandNum--;
                if(gamePanel.ui.commandNum < 0)
                    gamePanel.ui.commandNum = 2;
            }
            if(code == KeyEvent.VK_S)
            {
                gamePanel.ui.commandNum++;
                if(gamePanel.ui.commandNum > 2)
                    gamePanel.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH PLAY TETRIS GAME BUTTON:
                    if(gamePanel.ui.commandNum == 0){
                        gamePanel.tempGameState = gamePanel.gameOptionState;
                        gamePanel.gameState = gamePanel.playState;
                        gamePanel.playManager.loadGame("Tetris");
                        gameName = "Tetris";
                    }
                    // PRESS ENTER WITH PLAY MAZE GAME BUTTON:
                    if (gamePanel.ui.commandNum == 1) {
                        gamePanel.tempGameState = gamePanel.gameOptionState;
                        gamePanel.gameState = gamePanel.playState;
                        gamePanel.playManager.loadGame("Maze");
                        gameName = "Maze";
                    }
                    // PRESS ENTER WITH RETURN BUTTON:
                    if (gamePanel.ui.commandNum == 2) {
                        gamePanel.gameState = gamePanel.titleState;
                    }
            }
        }
        // PLAY STATE
        public void playState(int code){
            // Button for Tetris
            if(gamePanel.playManager.getCurrentGame().getClass() == Tetris.class){
                // W-A-S-D MOVEMENT:
                if(code == KeyEvent.VK_W){
                    upPressed = true;
                }
                if(code == KeyEvent.VK_A){
                    leftPressed = true;
                }
                if(code == KeyEvent.VK_S){
                    downPressed = true;
                }
                if(code == KeyEvent.VK_D){
                    rightPressed = true;
                }
                if(code == KeyEvent.VK_SPACE){
                    spacePressed = true;
                }
                if(code == KeyEvent.VK_E){
                    holdPressed = true;
                }
                if(code == KeyEvent.VK_Q){
                    releasePressed = true;
                }
            }
            // Button for Maze
            if(gamePanel.playManager.getCurrentGame().getClass() == Maze.class){
                // W-A-S-D MOVEMENT:
                if(code == KeyEvent.VK_W){
                    upPressed = true;
                }
                if(code == KeyEvent.VK_A){
                    leftPressed = true;
                }
                if(code == KeyEvent.VK_S){
                    downPressed = true;
                }
                if(code == KeyEvent.VK_D){
                    rightPressed = true;
                }
            }

            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            } else if (code != KeyEvent.VK_ENTER){
                enterPressed = false;
            }
        // CHANGE STATE:
            // PRESS ESCAPE TOP PAUSE GAME AND SETTINGS:
            if(code == KeyEvent.VK_ESCAPE)
            {
                gamePanel.gameState = gamePanel.pauseState;
            }
            // Debug
            if(code == KeyEvent.VK_T){
                if(showDebugText == false){
                    showDebugText = true;
                }
                else if(showDebugText == true){
                    showDebugText = false;
                }
            }
}

        // PAUSE STATE
        public void pauseState(int code){
            if(code == KeyEvent.VK_ESCAPE)
            {
                gamePanel.gameState = gamePanel.playState;
            }
            if ( code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }
            // INTERACT WITH BUTTONS IN PAUSE SCREEN:
                if(code == KeyEvent.VK_W)
                {
                    gamePanel.ui.pauseCommandNum--;
                    if(gamePanel.ui.pauseCommandNum < 0)
                        gamePanel.ui.pauseCommandNum = 1;
                }
                if(code == KeyEvent.VK_S)
                {
                    gamePanel.ui.pauseCommandNum++;
                    if(gamePanel.ui.pauseCommandNum > 1)
                        gamePanel.ui.pauseCommandNum = 0;
                }
                if (code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH PLAY BUTTON:
                    if(gamePanel.ui.pauseCommandNum == 0){
                        gamePanel.gameState = gamePanel.playState;
                    }
                    // PRESS ENTER WITH EXIT BUTTON:
                    if(gamePanel.ui.pauseCommandNum == 1){
                        gamePanel.gameState = gamePanel.gameOptionState;
                        gamePanel.reset();
                    }
                }
                enterPressed = false;
        }

        public void gameOverState(int code){
            if(code == KeyEvent.VK_W){
                gamePanel.ui.commandNum--;
            if(gamePanel.ui.commandNum < 0){
                gamePanel.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gamePanel.ui.commandNum++;
            if(gamePanel.ui.commandNum > 1){
                gamePanel.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH PLAY BUTTON:
                    if(gamePanel.ui.pauseCommandNum == 0){
                        gamePanel.gameState = gamePanel.playState;
                        gamePanel.retry();
                    }
                    // PRESS ENTER WITH EXIT BUTTON:
                    if(gamePanel.ui.pauseCommandNum == 1){
                        gamePanel.gameState = gamePanel.gameOptionState;
                        gamePanel.reset();
                    }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}