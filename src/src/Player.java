import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Sprite {
    private int numBombs;

    private int cooldown;

    private ArrayList<Bomb> bombs;

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height, null);
        numBombs = 4;
        cooldown = 2000;
        bombRadius = 1;
        bombPower = 1;
        bombs = new ArrayList<>();
        lives = 3;
        dx = 0;
        dy = 0;
        speed = 2 * speed;
    }


    public int getCooldown() {
        return cooldown;
    }

    public int getNumBombs() {
        return numBombs;
    }

    @Override
    protected void loadImage() {
        image = Resource.playerIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void keyPressed(KeyEvent event) {

        int key = event.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            putBomb();

        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            dx = -speed;
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            dx = speed;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            dy = -speed;
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            dy = speed;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            dy = 0;
        }
    }

    @Override
    public void render() {
        x += dx;
        y += dy;
        if (cooldown <= 0) {
            numBombs++;
            cooldown = 2000;
        } else if (numBombs != 4) {
            cooldown -= Game.tick;
        }
    }

    public void putBomb() {
        if (numBombs == 0)
            return;
        numBombs -= 1;
        bombs.add(new Bomb((x / Game.blockSize) * Game.blockSize, (y / Game.blockSize) * Game.blockSize, width, height, bombRadius, bombPower));
    }

    @Override
    public void moveBack() {
        x -= dx;
        y -= dy;
    }

    @Override
    public void explode(int power) {
        lives -= power;
        visible = false;
    }
}
