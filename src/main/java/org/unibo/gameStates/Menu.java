package org.unibo.gameStates;

import static org.unibo.Game.*;
import static org.unibo.GameState.*;
import static org.unibo.utils.LoadSave.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.unibo.Game;
import org.unibo.GameState;
import org.unibo.ui.MenuButton;
import org.unibo.utils.LoadSave;

public class Menu extends State implements StateMethods {
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImage;
    private BufferedImage imageBackgroundPink;
    private int menuX;
    private int menuY;
    private int menuWidth;
    private int menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        laodBackground();
        imageBackgroundPink = LoadSave.GetSpriteAtlas(BACKGROUND_MENU);
    }

    private void laodBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(MENU_BACKGROUND);
        menuWidth = (int) (backgroundImage.getWidth() * SCALE);
        menuHeight = (int) (backgroundImage.getHeight() * SCALE);
        menuX = (GAME_WIDTH / 2) - (menuWidth / 2);
        menuY = (GAME_HEIGHT / 2) - (menuHeight / 2);
    }

    // TODO: Implement a way to automatically laod buttons at the center of the
    // screen
    private void loadButtons() {
        buttons[0] = new MenuButton(GAME_WIDTH / 2, (int) (160 * SCALE), 0, PLAYING);
        buttons[1] = new MenuButton(GAME_WIDTH / 2, (int) (230 * SCALE), 1, OPTIONS);
        buttons[2] = new MenuButton(GAME_WIDTH / 2, (int) (300 * SCALE), 2, QUIT);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isInside(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isInside(e, mb)) {
                if (mb.isMousePressed()) {
                    mb.setMousePressed(false);
                    mb.setGameState();
                    System.out.println("State: " + GameState.state);
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBooleans();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }

        for (MenuButton mb : buttons) {
            if (isInside(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(imageBackgroundPink, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) {
            mb.render(g);
        }
    }
}