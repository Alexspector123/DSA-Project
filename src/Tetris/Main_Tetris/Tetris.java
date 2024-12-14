package Main_Tetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.*;

import GameManage.Game;
import Main.GamePanel;
import Mino.*;

public class Tetris extends Game {

    // Main Play Area
    private final int WIDTH = 360;
    private final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    private Mino currentMino;
    private final int MINO_START_X;
    private final int MINO_START_Y;
    // The Next Mino
    private Mino nextMino;
    private final int NEXTMINO_X;
    private final int NEXTMINO_Y;

    // The Ghost Mino
    private Mino ghostMino;

    // The Hold Mino
    private Mino holdMino;
    private final int HOLDMINO_X;
    private final int HOLDMINO_Y;

    public static ArrayList<Block> staticBlocks;
    private static Stack<Mino> HoldBlocks;

    // Mino Bag
    private ArrayList<Mino> minoBag;
    private Random random = new Random();

    // Drop attribute
    public static int dropInterval = 60; // Minos drop every 60 frames

    // Effect
    private boolean effectCounterOn;
    private int effectCounter;
    private ArrayList<Integer> effectY = new ArrayList<>();

    // Score
    private int level = 1;
    private int lines;

    GamePanel gamePanel;

    public Tetris(GamePanel gamePanel) {

        this.gamePanel = gamePanel;
        // Main Play Area Frame
        left_x = (GamePanel.Width / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 185;
        NEXTMINO_Y = top_y + 500;

        HOLDMINO_X = left_x - 205;
        HOLDMINO_Y = top_y + 100;

        staticBlocks = new ArrayList<>();
        HoldBlocks = new Stack();
        minoBag = new ArrayList<>();

        setScore(0);

        // Initialize the bag with shuffled Minos
        refillAndShuffleBag();

        // Set the starting Mino
        currentMino = pickMino();
        nextMino = pickMino();
        ghostMino = new Mino();
        ghostMino.create(new Color(192, 192, 192));

        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private void refillAndShuffleBag() {

        minoBag.clear();

        minoBag.add(new Mino_L_Right());
        minoBag.add(new Mino_L_Left());
        minoBag.add(new Mino_Square());
        minoBag.add(new Mino_Bar());
        minoBag.add(new Mino_T());
        minoBag.add(new Mino_Z_Right());
        minoBag.add(new Mino_Z_Left());

        // Fisher-Yates Shuffle
        for (int i = minoBag.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Collections.swap(minoBag, i, j);
        }
    }

    private Mino pickMino() {

        if (minoBag.isEmpty()) {
            refillAndShuffleBag();
        }
        return minoBag.remove(0);
    }

    public void update() {

        if (currentMino.active == false) {

            staticBlocks.add(currentMino.block[0]);
            staticBlocks.add(currentMino.block[1]);
            staticBlocks.add(currentMino.block[2]);
            staticBlocks.add(currentMino.block[3]);

            // check if the game is over
            if (currentMino.block[0].x == MINO_START_X && currentMino.block[0].y == MINO_START_Y) {
                // this means the currentMino immediately collided a block and couldn't move at
                // all
                // so it's the position is the same with the nextMino
                gamePanel.gameState = gamePanel.gameOverState;
            }

            currentMino.deactivating = false;

            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // when a mino becomes inactive, check if line(s) can be deleted
            checkDelete();
        } else {
            ghostMino.duplicate(currentMino);
            currentMino.update();
            holdMechanic();
        }
    }

    private void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {

            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    // increase the count if there is a static blocks
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if (x == right_x) {

                // if the blockCount hits 12, that means the current y line is all filled with
                // blocks
                // so we can delete them
                if (blockCount == 12) {

                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        // remove all the blocks in the current y line
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }
                    lineCount++;
                    lines++;
                    // Drop Speed
                    // if the line score hits a certain number, increase the drop speed
                    // 1 is the fastest
                    if (lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval -= 1;
                        }
                    }
                    // a line has been deleted so need to slide down blocks that are above it
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        // Add Score
        if (lineCount > 0) {
            int singleLineScore = 10 * level;
            setScore(getScore() + singleLineScore * lineCount);
        }
    }

    public void holdMechanic() {
        if (gamePanel.keyHandler.holdPressed) {
            HoldBlocks.push(currentMino);
            holdMino = HoldBlocks.peek();
            holdMino.setXY(HOLDMINO_X, HOLDMINO_Y);
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
            gamePanel.keyHandler.holdPressed = false;
        }
        if (gamePanel.keyHandler.releasePressed) {
            if (!HoldBlocks.empty()) {
                nextMino = currentMino;
                nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
                currentMino = HoldBlocks.pop();
                if (!HoldBlocks.empty()) {
                    holdMino = HoldBlocks.peek();
                }
                holdMino.setXY(HOLDMINO_X, HOLDMINO_Y);
                currentMino.setXY(MINO_START_X, MINO_START_Y);
            }
            gamePanel.keyHandler.releasePressed = false;
        }
    }

    public void draw(Graphics2D graphics2D) {

        // Draw the Mino inside
        for (int i = 0; i < HEIGHT; i += Block.SIZE) {
            for (int j = 0; j < WIDTH; j += Block.SIZE) {
                graphics2D.drawRect(left_x + j, top_y + i, Block.SIZE, Block.SIZE);
            }
        }

        // Draw Play Area Frame
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(4f));
        graphics2D.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw Next Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        graphics2D.drawRect(x, y, 200, 200);
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 30));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString("NEXT", x + 60, y + 60);

        // Draw Score Frame
        graphics2D.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        graphics2D.drawString("LEVEL: " + level, x, y);
        y += 70;
        graphics2D.drawString("LINES: " + lines, x, y);
        y += 70;
        graphics2D.drawString("SCORE: " + getScore(), x, y);

        // Draw the currentMino and ghostMino
        if (currentMino != null) {
            ghostMino.draw(graphics2D);
            currentMino.draw(graphics2D);
        }
        // Draw the nextMino
        nextMino.draw(graphics2D);

        // Draw the holdMino
        if (!HoldBlocks.empty()) {
            holdMino.draw(graphics2D);
        }

        // Draw Static Blocks
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(graphics2D);
        }

        // Draw effect
        if (effectCounterOn) {
            effectCounter++;

            graphics2D.setColor(Color.white);
            for (int i = 0; i < effectY.size(); i++) {
                graphics2D.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }

            if (effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // Draw Hold Block Frame
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(4f));
        graphics2D.drawRect(left_x - 300, top_y, 200, 200);
        graphics2D.drawString("NEXT", left_x - 300 + 60, top_y + 60);
    }

    public void end() {
        
    }
}
