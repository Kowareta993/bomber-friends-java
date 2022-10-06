import java.awt.*;

public class BomberSkeleton extends Sprite{
    private int cooldown;
    private int bombs;

    private Bomb bomb;

    public BomberSkeleton(int x, int y, int w, int h, Wall[][] map) {
        super(x, y, w, h, map);
        bombs = 1;
        speed = 2 * speed;
        bombPower = 4;
        bombRadius = 5;
        cooldown = 5000;
        start();
    }
    @Override
    protected void loadImage() {
        image = Resource.skeleton2Icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    @Override
    public void render() {
        super.render();
        if (bombs != 1)
            cooldown -= Game.tick;
        if (cooldown <= 0) {
            bombs++;
            cooldown = 5000;
        }
        putBomb();

    }

    private void putBomb() {
        if (bombs == 0)
            return;
        bombs -= 1;
        bomb = new Bomb((x / Game.blockSize) * Game.blockSize, (y / Game.blockSize) * Game.blockSize, width, height, bombRadius, bombPower);
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }
}
