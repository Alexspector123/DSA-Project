package Main;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS:
    final int fps = 60;
    final int scale = 3;

    // SCREEN SETTINGS:
        public static final int Width = 1280;
        public static final int Height = 720;

    // FOR FULL SCREEN
        public int screenHeight2 = Height;
        public int screenWidth2 = Width;
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

    // Play Manager
    PlayManager playManager = new PlayManager(this);
//    // Config
//        Config config = new Config(this);

   // CONSTRUCTOR:
        public GamePanel() {
                this.setPreferredSize(new Dimension(Width,Height));
                this.setBackground(Color.BLACK);
                this.setLayout(null);
                this.setDoubleBuffered(true);   // ??????
                this.addKeyListener(keyHandler);
                this.setFocusable(true);
                //setBackground(new Color(0x7F00FF));
        }

   // METHODS:
        public void setupGame(){
            // DEFAULT GAME STATE:
                gameState = playState;
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
        private void update() {
        
            if (gameState == playState) {
                playManager.update();
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
                    playManager.draw(graphics2D);
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
