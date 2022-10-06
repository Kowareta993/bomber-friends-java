import java.awt.*;

public class VExplosion extends Explosion{
    public VExplosion(int x, int y, int w, int h, int p) {
        super(x, y, w, h, p);
    }

    @Override
    protected void loadImage() {
        image = Resource.expvIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}
