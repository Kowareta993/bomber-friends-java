import java.awt.*;

public class HExplosion  extends Explosion{
    public HExplosion(int x, int y, int w, int h, int p) {
        super(x, y, w, h, p);
    }

    @Override
    protected void loadImage() {
        image = Resource.exphIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}
