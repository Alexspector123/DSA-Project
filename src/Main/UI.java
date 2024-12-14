package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

import Main_Tetris.Tetris;
import Maze.Main_Maze.Maze;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {
    GamePanel gamePanel;

    Graphics2D g2;
    Font maruMonica, purisaB;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false; // #10
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    public int titleScreenState = 0;
    public int subState = 0;

    BufferedImage endScreenImage;

    Font alagard, romulus;

    // TITLE SCREEN IMAGES:
    BufferedImage titleScreen,
            playButton1, playButton2,
            exitButton1, exitButton2, dialouge;
    // PAUSE SCREEN IMAGES:
    BufferedImage pauseScreen,
            resumeButton1, resumeButton2,
            musicButton1, musicButton2,
            sfxButton1, sfxButton2,
            controlButton1, controlButton2,
            backButton1, backButton2,
            bar1_1, bar1_2, bar1_3, bar1_4, bar1_5, bar1_6, bar1_7, bar1_8, bar1_9, bar1_10, bar1_11, bar1_12,
            bar2_1, bar2_2, bar2_3, bar2_4, bar2_5, bar2_6, bar2_7, bar2_8, bar2_9, bar2_10, bar2_11, bar2_12;

    // GAME OVER SCREEN
    BufferedImage gameOverScreen, retryButton1, retryButton2, quitButton1, quitButton2;
    // CHOOSING GAME SCREEN
    BufferedImage gameFrameTetris1, gameFrameTetris2, gameFrameMaze1, gameFrameMaze2;
    // ANIMATION FOR BUTTON:
    // TITLE SCREEN BUTTONS:
    public int commandNum = 0;
    // PAUSE SCREEN BUTTONS:
    public int pauseCommandNum = 0;
    // SETTING SCREEN BUTTONS:
    public int settingCommandNum = 0;
    // ANIMATION FOR DIALOGUES:
    public String currentDialogue = " ";

    public UI(GamePanel gamePanel) {

        this.gamePanel = gamePanel;
        // GET UI IMAGES:
        getUIImage();
        // FONT CHỮ TRONG GAME:
        try {
            InputStream is = getClass().getResourceAsStream("/Font/alagard.ttf");
            if (is != null) {
                alagard = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(28f);
                is.close();
            }
            is = getClass().getResourceAsStream("/Font/romulus.ttf");
            if (is != null) {
                romulus = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(28f);
                is.close();
            }
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        // CHECK CURRENT GAME STATE:
        // TITLE SCREEN STATE:
        if (gamePanel.gameState == gamePanel.titleState) {
            drawTitleScreen();
        } else if (gamePanel.gameState == gamePanel.gameOptionState) {
            gamePanel.playManager.draw();
        }
        // PLAY STATE:
        else if (gamePanel.gameState == gamePanel.playState) {
            // Tetris game play
            if (gamePanel.playManager.getCurrentGame() instanceof Tetris) {
                // drawTutorial();
                // gamePanel.player.checkLevelUp();
            }
            // Maze game play
            else if (gamePanel.playManager.getCurrentGame() instanceof Maze) {
                //drawMazeGame();
            }
        }
        // PAUSE STATE:
        else if (gamePanel.gameState == gamePanel.pauseState) {
            drawPauseScreen();
        }
        // GAMEOVER STATE:
        else if (gamePanel.gameState == gamePanel.gameOverState) {
            drawGameOverScreen();
        }
    }

    public void drawTutorial() {

    }

    public void drawMazeGame() {

    }

    public void drawTitleScreen() {
        int x = 0;
        int y = 0;
        // DRAW MENU TITLE SCREEN:
        g2.drawImage(titleScreen, x, y, null);
        // DRAW BUTTON:
        x += gamePanel.tileSize * 16;
        y += gamePanel.tileSize * 5;
        // PLAY BUTTON:
        if (commandNum == 0) {
            g2.drawImage(playButton1, x, y, null);
        } else {
            g2.drawImage(playButton2, x, y, null);
        }

        // EXIT BUTTON:
        y += 82;
        if (commandNum == 1) {
            g2.drawImage(exitButton1, x, y, null);
        } else {
            g2.drawImage(exitButton2, x, y, null);
        }
    }

    public void getUIImage() {
        // PAUSE SCREEN:
        pauseScreen = setup("PauseScreen", gamePanel.screenWidth, gamePanel.screenHeight);
        resumeButton1 = setup("ResumeButton_1", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        resumeButton2 = setup("ResumeButton_2", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        controlButton1 = setup("ControlButton_1", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        controlButton2 = setup("ControlButton_2", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        backButton1 = setup("BackButton_1", (gamePanel.tileSize * 2) + 16, gamePanel.tileSize - 6);
        backButton2 = setup("BackButton_2", (gamePanel.tileSize * 2) + 16, gamePanel.tileSize - 6);

        // TITLE SCREEN:
        titleScreen = setup("TitleScreen", gamePanel.screenWidth, gamePanel.screenHeight);
        exitButton1 = setup("Exitbutton_1", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        exitButton2 = setup("Exitbutton_2", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        playButton1 = setup("Playbutton_1", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        playButton2 = setup("Playbutton_2", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        // GAME OVER SCREEN:
        gameOverScreen = setup("GameOverScreen", gamePanel.screenWidth, gamePanel.screenHeight);
        retryButton1 = setup("RetryButton_1", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        retryButton2 = setup("RetryButton_2", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        quitButton1 = setup("QuitButton_1", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        quitButton2 = setup("QuitButton_2", (gamePanel.tileSize * 3) + 7, gamePanel.tileSize + 10);
        // CHOOSING SCREEN
        gameFrameTetris1 = setup("Frame_11", gamePanel.tileSize * 6, gamePanel.tileSize * 6);
        gameFrameTetris2 = setup("Frame_21", gamePanel.tileSize * 6, gamePanel.tileSize * 6);
        gameFrameMaze1 = setup("Frame_12", gamePanel.tileSize * 6, gamePanel.tileSize * 6);
        gameFrameMaze2 = setup("Frame_22", gamePanel.tileSize * 6, gamePanel.tileSize * 6);
    }

    public void drawPauseScreen() {
        int x = 0;
        int y = 0;
        // DRAW PAUSE SCREEN:
        g2.drawImage(pauseScreen, x, y, null);
        // DRAW BUTTON:
        x += gamePanel.tileSize * 12;
        y += gamePanel.tileSize * 5;
        // RESUME BUTTON:
        if (pauseCommandNum == 0) {
            g2.drawImage(resumeButton1, x, y, null);
        } else {
            g2.drawImage(resumeButton2, x, y, null);
        }

        // EXIT BUTTON:
        y += 82;
        if (pauseCommandNum == 1) {
            g2.drawImage(exitButton1, x, y, null);
        } else {
            g2.drawImage(exitButton2, x, y, null);
        }
    }

    public BufferedImage setup(String imagePath, int width, int height) {

        BufferedImage image = null;
        UtilityTool uTool = new UtilityTool();
        String filePath = "res/Background/" + imagePath + ".png";
        File imageFile = new File(filePath);

        try (FileInputStream readFile = new FileInputStream(imageFile)) {
            image = ImageIO.read(readFile);
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /*
     * public int getItemIndexOnSlot(int slotCol, int slotRow){
     * 
     * int itemIndex = 0;
     * if(gamePanel.gameState == gamePanel.characterState){
     * itemIndex = slotCol + (slotRow*6);
     * }
     * else if(gamePanel.gameState == gamePanel.tradeState){
     * itemIndex = slotCol + (slotRow*5);
     * }
     * return itemIndex;
     * }
     */
    public void drawGameOverScreen() {
        int x = 0;
        int y = 0;
        // DRAW MENU TITLE SCREEN:
        g2.drawImage(gameOverScreen, x, y, null);
        x = gamePanel.tileSize * 16;
        y = gamePanel.tileSize * 5;

        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        g2.drawString("Score: " + gamePanel.playManager.getCurrentGame().getScore(), x, y);
        // DRAW BUTTON:
        y += 100;
        // RETRY BUTTON:
        if (commandNum == 0) {
            g2.drawImage(retryButton1, x, y, null);
        } else {
            g2.drawImage(retryButton2, x, y, null);
        }

        // QUIT BUTTON:
        y += 82;
        if (commandNum == 1) {
            g2.drawImage(quitButton1, x, y, null);
        } else {
            g2.drawImage(quitButton2, x, y, null);
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = Color.WHITE;
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gamePanel.WIDTH / 2 - length / 2;
        return x;
    }

    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}