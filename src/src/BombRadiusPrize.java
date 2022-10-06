import java.awt.*;

public class BombRadiusPrize extends  Prize{
    public BombRadiusPrize(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void loadImage() {
        image = Resource.radiusIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}
