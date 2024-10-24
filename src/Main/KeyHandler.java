package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;
    public static boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

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
            if(gamePanel.gameState == gamePanel.gameOptionState){
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
        //OPTION STATE
            else if(gamePanel.gameState == gamePanel.optionsState) {
                optionsState(code);
            }
    }
    // STATES
        // TITLE STATE
        public void titleState(int code){
            if(code == KeyEvent.VK_W)
            {
                gamePanel.ui.commandNum--;
                gamePanel.playSE(12);
                if(gamePanel.ui.commandNum < 0)
                gamePanel.ui.commandNum = 2;
            }
            if(code == KeyEvent.VK_S)
            {
                gamePanel.ui.commandNum++;
                gamePanel.playSE(12);
                if(gamePanel.ui.commandNum > 2)
                gamePanel.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH PLAY BUTTON:
                    if(gamePanel.ui.commandNum == 0){
                        gamePanel.tempGameState = gamePanel.titleState;
                        gamePanel.stopMusic();
                        gamePanel.playMusic(0);
                        //gamePanel.gameState = gamePanel.gameOptionState;
                        gamePanel.gameState = gamePanel.playState;
                    }
                    // PRESS ENTER WITH SETTING BUTTON:
                    if (gamePanel.ui.commandNum == 1) {
                        gamePanel.tempGameState = gamePanel.titleState;
                        gamePanel.gameState = gamePanel.optionsState;
                    }
                    // PRESS ENTER WITH EXIT BUTTON:
                    if (gamePanel.ui.commandNum == 2) {
                        System.exit(0);
                    }
            }
        }
        // CHOOSE GAME STATE
        public void chooseGameState(int code){
            if(code == KeyEvent.VK_W)
            {
                gamePanel.ui.commandNum--;
                gamePanel.playSE(12);
                if(gamePanel.ui.commandNum < 0)
                gamePanel.ui.commandNum = 2;
            }
            if(code == KeyEvent.VK_S)
            {
                gamePanel.ui.commandNum++;
                gamePanel.playSE(12);
                if(gamePanel.ui.commandNum > 2)
                gamePanel.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH PLAY TETRIS GAME BUTTON:
                    if(gamePanel.ui.commandNum == 0){
                        gamePanel.tempGameState = gamePanel.titleState;
                        gamePanel.stopMusic();
                        gamePanel.playMusic(0);
                        gamePanel.gameState = gamePanel.playTetrisGameState;
                    }
                    // PRESS ENTER WITH PLAY MAZE GAME BUTTON:
                    if (gamePanel.ui.commandNum == 1) {
                        gamePanel.tempGameState = gamePanel.titleState;
                        gamePanel.gameState = gamePanel.playMazeGameState;
                    }
                    // PRESS ENTER WITH EXIT BUTTON:
                    if (gamePanel.ui.commandNum == 2) {
                        System.exit(0);
                    }
            }
        }
        // PLAY STATE
        public void playState(int code){
            // W-A-S-D MOVEMENT:
            if(code == KeyEvent.VK_W)
            {
                upPressed = true;
            }
            if(code == KeyEvent.VK_A)
            {
                leftPressed = true;
            }
            if(code == KeyEvent.VK_S)
            {
                downPressed = true;
            }
            if(code == KeyEvent.VK_D)
            {
                rightPressed = true;
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
                    gamePanel.playSE(12);
                    if(gamePanel.ui.pauseCommandNum < 0)
                        gamePanel.ui.pauseCommandNum = 2;
                }
                if(code == KeyEvent.VK_S)
                {
                    gamePanel.ui.pauseCommandNum++;
                    gamePanel.playSE(12);
                    if(gamePanel.ui.pauseCommandNum > 2)
                        gamePanel.ui.pauseCommandNum = 0;
                }
                if (code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH PLAY BUTTON:
                    if(gamePanel.ui.pauseCommandNum == 0){
                        gamePanel.gameState = gamePanel.playState;
                    }
                    // PRESS ENTER WITH SETTING BUTTON:
                    if(gamePanel.ui.pauseCommandNum == 1){
                        gamePanel.tempGameState = gamePanel.pauseState;
                        gamePanel.gameState = gamePanel.optionsState;
                    }
                    // PRESS ENTER WITH EXIT BUTTON:
                    if(gamePanel.ui.pauseCommandNum == 2){
                        System.exit(0);
                    }
                }
                enterPressed = false;
        }
        // DIALOGUE STATE
        public void dialogueState(int code) {
            if(code == KeyEvent.VK_ENTER){
                gamePanel.gameState = gamePanel.playState;
                
            }
        }
        
        // OPTION STATE:
        public void optionsState(int code) {
            if(code == KeyEvent.VK_ESCAPE) {
                gamePanel.gameState = gamePanel.playState;
            }
            if(code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if(code == KeyEvent.VK_W){
                gamePanel.ui.settingCommandNum--;
                gamePanel.playSE(12);
                if (gamePanel.ui.settingCommandNum < 0 ){
                    gamePanel.ui.settingCommandNum = 4;
                }
            }

            if(code == KeyEvent.VK_S){
                gamePanel.ui.settingCommandNum++;
                gamePanel.playSE(12);
                if ( gamePanel.ui.settingCommandNum > 4 ) {
                    gamePanel.ui.settingCommandNum = 0;
                }
            }
            // PRESS ENTER:
                if (code == KeyEvent.VK_ENTER){
                    // PRESS ENTER WITH EXIT BUTTON:
                        if(gamePanel.ui.settingCommandNum == 3){
                            System.exit(0);
                        }
                    // PRESS ENTER WITH BACK BUTTON:
                        if(gamePanel.ui.settingCommandNum == 4){
                            gamePanel.gameState = gamePanel.tempGameState;
                        }
                }
                // MUSIC BUTTON & VOLUME BAR:
                    if ( code == KeyEvent.VK_A ) {
                        if (gamePanel.ui.settingCommandNum == 0) {
                            if (gamePanel.music.volumeScale > 0) {
                                gamePanel.music.volumeScale--;
                                gamePanel.playSE(12);
                                gamePanel.music.checkVolume();
                            }
                        }
                        if (gamePanel.ui.settingCommandNum == 1) {
                            if (gamePanel.se.volumeScale > 0) {
                                gamePanel.se.volumeScale--;
                                gamePanel.playSE(12);
                            }
                        }
                    }
                    if ( code == KeyEvent.VK_D ) {
                        if (gamePanel.ui.settingCommandNum == 0) {
                            if (gamePanel.music.volumeScale < 11) {
                                gamePanel.music.volumeScale++;
                                gamePanel.playSE(12);
                                gamePanel.music.checkVolume();
                            }
                        }
                        if (gamePanel.ui.settingCommandNum == 1) {
                            if (gamePanel.se.volumeScale < 11) {
                                gamePanel.se.volumeScale++;
                                gamePanel.playSE(12);
                            }
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
            gamePanel.playSE(12);
            // gamePanel.playSE(9);
        }
        if(code == KeyEvent.VK_S){
            gamePanel.ui.commandNum++;
            if(gamePanel.ui.commandNum > 1){
                gamePanel.ui.commandNum = 0;
            }
            gamePanel.playSE(12);
        }
        if(code == KeyEvent.VK_ENTER){
            if(gamePanel.ui.commandNum == 0){
                gamePanel.gameState = gamePanel.playState;
                gamePanel.retry();
            }
            else if(gamePanel.ui.commandNum == 1){
                gamePanel.gameState = gamePanel.titleState;
                gamePanel.restart();
                System.out.println(gamePanel.gameState);
            }
        }
    }
        @Override
        public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W)
        {
            upPressed = false;
        }
        if(code == KeyEvent.VK_A)
        {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S)
        {
            downPressed = false;
        }
        if(code == KeyEvent.VK_D)
        {
            rightPressed = false;
        }
        
    }
}