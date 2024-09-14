package Main;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS:
    final int fps = 60;
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    // TILE SETTINGS:
    public final int tileSize = originalTileSize * scale; // 48x48 tile

    // SCREEN SETTINGS:
        public final int maxScreenColumn = 18;
        public final int maxScreenRow = 14;
        public final int screenWidth = tileSize * maxScreenColumn;
        public final int screenHeight = tileSize * maxScreenRow;
    // WORLD SETTINGS:
        public final int maxWorldColumn = 64; // can be adjusted
        public final int maxWorldRow = 64; // can be adjusted
    // FOR FULL SCREEN
        public int screenHeight2 = screenHeight;
        public int screenWidth2 = screenWidth;
        BufferedImage tempScreen;
        Graphics2D graphics2D;
        public boolean fullScreenOn = true;
   // INSTANTIATE OBJECTS: 
        public KeyHandler keyHandler = new KeyHandler(this); 
        Thread gameThread; 
    // Sound CLASS 
        Sound music = new Sound(); 
        Sound se = new Sound();
    // UNIT INTERFACE 
        public UI ui = new UI(this);
    // Config
        Config config = new Config(this);
    // GAME STATE VARIABLES :
    public int gameState;
    public int tempGameState; // USED WHEN PLAYER HITS BUTTON "BACK"
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int optionsState = 3;
    public final int gameOverState = 4;

//    // Config
//        Config config = new Config(this);

   // CONSTRUCTOR:
        public GamePanel() {
                this.setPreferredSize(new Dimension(screenWidth,screenHeight));
                this.setBackground(Color.WHITE);
                this.setDoubleBuffered(true);
                this.addKeyListener(keyHandler);
                this.setFocusable(true);
        }

   // METHODS:
        public void setupGame(){
            // DEFAULT GAME STATE:
                gameState = titleState;

            //    playMusic(1);


        }
        public void retry(){
        //    player.restoreLife();
        }
        public void restart(){
        //    player.setDefaultValues();
            // aSetter.setInteractiveTile();            Wtf is this???
        }
        public void setFullScreen() {

            //GET LOCAL SCREEN DEVICE
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gd.setFullScreenWindow(Main.window);

        }
        public void startGameThread(){
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public void run() {

            double drawInterval = 1000000000/fps; // redraw the screen each 0.016 seconds
            double nextDrawTime = System.nanoTime() + drawInterval;

            while ( gameThread != null ) {

                // 1. UPDATE: Update information such as character positions,...
                    update();
                // 2. DRAW: redraw the screen with the updated information
                    repaint();

                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                try {
                    if (remainingTime < 0 ){
                        remainingTime = 0;
                    }

                    Thread.sleep((long) remainingTime);

                    nextDrawTime = nextDrawTime + drawInterval;

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        public void update() {
            if (gameState == playState) {
                
            }
        }
    public void paintComponent(Graphics graphics){

            super.paintComponent(graphics);

            Graphics2D graphics2D = (Graphics2D) graphics;
            //DEBUG
            long drawStart = 0;

        // DEBUG TEXT:
                if(keyHandler.showDebugText == true) {
                    drawStart = System.nanoTime();
                }
                // TITLE SCREEN
                if(gameState == titleState) {
                    ui.draw(graphics2D);
                }
            // PLAY STATE:
                else {
                    // UI
                        ui.draw(graphics2D);
            }

            // Debug
                if(keyHandler.showDebugText == true){

                    long drawEnd = System.nanoTime();
                    long passed = drawEnd - drawStart;

                    graphics2D.setFont(new Font("Arial", Font.PLAIN, 20));
                    graphics2D.setColor(Color.white);
                    int x = 10;
                    int y = 400;
                    int lineHeight = 20;

                    graphics2D.drawString("Draw Time: "+passed, x, y);
                }

                graphics2D.dispose();
                }


    // GAME THEME SONG:

        public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){

        music.stop();
    }
// SOUND EFFECTS:
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
