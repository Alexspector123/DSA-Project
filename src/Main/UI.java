package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

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
    public boolean gameFinished = false;  //#10
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
                    settingButton1, settingButton2,
                    exitButton1, exitButton2, dialouge;
    // PAUSE SCREEN IMAGES:
        BufferedImage pauseScreen,
                      resumeButton1, resumeButton2,
                      musicButton1, musicButton2,
                      sfxButton1, sfxButton2,
                      controlButton1, controlButton2,
                      backButton1, backButton2,
                      bar1_1,bar1_2,bar1_3,bar1_4,bar1_5,bar1_6,bar1_7,bar1_8,bar1_9,bar1_10,bar1_11,bar1_12,
                      bar2_1,bar2_2,bar2_3,bar2_4,bar2_5,bar2_6,bar2_7,bar2_8,bar2_9,bar2_10,bar2_11,bar2_12;

    // GAME OVER SCREEN
        BufferedImage gameOverScreen, retryButton1, retryButton2, quitButton1, quitButton2;
    // ANIMATION FOR BUTTON:
            // TITLE SCREEN BUTTONS:
            public int commandNum = 0;
            // PAUSE SCREEN BUTTONS:
                public int pauseCommandNum = 0;
            // SETTING SCREEN BUTTONS:
                public int settingCommandNum = 0;
            // ANIMATION FOR DIALOGUES:
                public String currentDialogue = " ";

    public UI(GamePanel gamePanel){

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
    public void addMessage(String text){
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
        if(gamePanel.gameState == gamePanel.titleState){
            drawTitleScreen();
        }
        // PLAY STATE:
        else if(gamePanel.gameState == gamePanel.playState){
            drawTutorial();
        //    gamePanel.player.checkLevelUp();
        }
        // PAUSE STATE:
        else if(gamePanel.gameState == gamePanel.pauseState){
            drawPauseScreen();
        }
        // OPTIONS STATE:
        else if (gamePanel.gameState == gamePanel.optionsState) {
            drawOptionsScreen();
        }
        // DIALOGUE STATE:
//        else if(gamePanel.gameState == gamePanel.dialogueState){
//            drawDialogueScreen();
//        }
        // GAMEOVER STATE:
        else if(gamePanel.gameState == gamePanel.gameOverState){
            drawGameOverScreen();
        }
    }
    public void drawTutorial(){
        
    }
    public void drawTitleScreen(){

    }
    public void getUIImage(){
        
    }
    public void drawPauseScreen(){
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString("Paused", PlayManager.left_x+130, PlayManager.top_y+320);
    }
    public void drawOptionsScreen(){
        

    }
    
    public BufferedImage setup(String imagePath,int width, int height) {

        BufferedImage image = null;
        UtilityTool uTool = new UtilityTool();
        String filePath = "res/Background/" + imagePath + ".png";
        File imageFile = new File(filePath);

        try (FileInputStream readFile = new FileInputStream(imageFile)) {
            image = ImageIO.read(readFile);
            image = uTool.scaleImage(image, width , height);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

/*         public int getItemIndexOnSlot(int slotCol, int slotRow){

            int itemIndex = 0;
            if(gamePanel.gameState == gamePanel.characterState){
                itemIndex = slotCol + (slotRow*6);
            }
            else if(gamePanel.gameState == gamePanel.tradeState){
                itemIndex = slotCol + (slotRow*5);
            }
            return itemIndex;
        }*/
        public void drawGameOverScreen(){
            g2.setColor(Color.yellow);
            g2.setFont(g2.getFont().deriveFont(50f));
            g2.drawString("GameOver", PlayManager.left_x+130, PlayManager.top_y+320);
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
    public int getXforCenteredText(String text){
        int length =(int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gamePanel.WIDTH/2 - length/2;
        return x;
    }
    public int getXforAlignToRightText(String text, int tailX){
        int length =(int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}