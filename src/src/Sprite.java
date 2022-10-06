import java.util.Random;

public abstract class Sprite extends Block {
    protected int lives;
    protected int bombPower;
    protected int bombRadius;
    protected int dx;
    protected int dy;
    protected int speed;


    protected Wall[][] map;

    public Sprite(int x, int y, int w, int h, Wall[][] map) {
        super(x, y, w, h);
        this.map = map;
        this.speed = 1;
    }

    protected void start() {
        randomDirection(true);
    }

    private void randomDirection(boolean hit) {
        Random random = new Random();
        int i = y / Game.blockSize;
        int j = x / Game.blockSize;
        if (!hit) {
            if (map[i - 1][j] != null && map[i + 1][j] != null)
                return;
            if (map[i][j - 1] != null && map[i][j + 1] != null)
                return;

        }
        int dir = random.nextInt(4);
        switch (dir) {
            case 0 -> {
                dx = -speed;
                dy = 0;
            }
            case 1 -> {
                dx = speed;
                dy = 0;
            }
            case 2 -> {
                dx = 0;
                dy = -speed;
            }
            case 3 -> {
                dx = 0;
                dy = speed;
            }
        }
    }

    @Override
    public void render() {
        if (!visible)
            return;
        if (x % Game.blockSize < 4 && y % Game.blockSize < 4) {
            randomDirection(false);
        }
        x += dx;
        y += dy;
    }


    public void moveBack() {
        x -= dx;
        y -= dy;
        randomDirection(true);
    }

    public void getPrize(Prize prize) {
        if (prize instanceof Heart)
            this.lives++;
        if (prize instanceof BombPowerPrize)
            this.bombPower++;
        if (prize instanceof BombRadiusPrize)
            this.bombRadius++;
    }

    public void setBombRadius(int bombRadius) {
        this.bombRadius = bombRadius;
    }

    public void setBombPower(int bombPower) {
        this.bombPower = bombPower;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void explode(int power) {
        lives -= power;
        if (lives < 0)
            visible = false;
    }
}
