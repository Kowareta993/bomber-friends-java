import java.awt.*;

public class BombPowerPrize extends Prize{
    public BombPowerPrize(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void loadImage() {
        image = Resource.powerIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}
