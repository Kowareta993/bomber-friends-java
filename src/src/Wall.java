import java.awt.*;

public class Wall extends Block {
    protected int hits;

    public Wall(int x, int y, int w, int h) {
        super(x, y, w, h);
        hits = -1;
    }

    @Override
    protected void loadImage() {
        image = Resource.wallIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    @Override
    public void explode(int power) {
        if (hits > 0)
            hits--;
        if (hits == 0)
            visible = false;
    }
}
