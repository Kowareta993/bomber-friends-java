import java.awt.*;

public class Explosion extends Block {
    private int timer;
    private boolean firstImpact;
    private int power;

    public Explosion(int x, int y, int w, int h, int p) {
        super(x, y, w, h);
        timer = 200;
        firstImpact = true;
        power = p;
    }

    @Override
    protected void loadImage() {
        image = Resource.expIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    @Override
    public void render() {
        timer -= Game.tick;
        if (timer <= 0)
            visible = false;
    }

    public void setFirstImpact(boolean firstImpact) {
        this.firstImpact = firstImpact;
    }

    public boolean isFirstImpact() {
        return firstImpact;
    }

    public int getPower() {
        return power;
    }
}
