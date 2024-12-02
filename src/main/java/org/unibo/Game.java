package org.unibo;

import static org.unibo.utils.LoadSave.loadSavesFile;

import java.awt.Graphics;

import org.unibo.gameStates.GameState;
import org.unibo.gameStates.Menu;
import org.unibo.gameStates.Playing;

public class Game implements Runnable {
    private GamePanel gamePanel;
    private Thread gameThread;

    public static final int FPS = 120;
    private static final int UPS = 200;

    private Playing playing;
    private Menu menu;
    public boolean gameOver;

    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALE = 1.0f;
    public static final int TILES_WIDTH = 26;
    public static final int TILES_HEIGHT = 14;
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_WIDTH * TILES_SIZE;
    public static final int GAME_HEIGHT = TILES_HEIGHT * TILES_SIZE;

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        initGame();
    }
    
    private void initGame() {
        playing = new Playing(this); // TODO: Move it at the end of the func
        loadSavesFile();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch(GameState.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
                System.exit(0);
                break;
            default:
                break;
        }   
    }

    public void render(Graphics g) {
        switch(GameState.state) {
            case MENU:
                menu.render(g);
                break;
            case PLAYING:
                playing.render(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        // FPS variables
        double timePerFrame = 1_000_000_000.0 / FPS;
        int frames = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaFPS = 0;

        // UPS variables
        double timeperUpdate = 1_000_000_000 / UPS;
        int updates = 0;
        long previousTime = System.nanoTime();
        double deltaUPS = 0;

        while (true) {
            long currentTime = System.nanoTime();

            // UPS loop
            deltaUPS += (currentTime - previousTime) / timeperUpdate;
            deltaFPS += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaUPS >= 1) {
                update();
                updates++;
                deltaUPS--;
            }

            // FPS loop
            if (deltaFPS >= 1) {
                gamePanel.repaint();
                frames++;
                deltaFPS--;
            }

            // FPS Println
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                //System.out.println("Frames: " + frames + " | Updates: " + updates);
                updates = 0;
                frames = 0;
            }
        }
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetPlayerBoolean();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}
