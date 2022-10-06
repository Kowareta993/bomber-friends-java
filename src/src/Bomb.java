import java.awt.*;

public class Bomb extends Block {
    private int timer;
    private final int radius;
    private final int power;

    public Bomb(int x, int y, int w, int h, int r, int p) {
        super(x, y, w, h);
        this.timer = 3000;
        radius = r;
        power = p;
    }

    @Override
    protected void loadImage() {
        image = Resource.bombIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    @Override
    public void render() {
        timer -= Game.tick;
        if (timer <= 0) {
            visible = false;
        }
    }

    public int getRadius() {
        return radius;
    }

    public int getTimer() {
        return timer;
    }

    public int getPower() {
        return power;
    }
}
