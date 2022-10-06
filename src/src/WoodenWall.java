import java.awt.*;

public class WoodenWall extends Wall {
    public WoodenWall(int x, int y, int w, int h) {
        super(x, y, w, h);
        hits = 1;
    }

    @Override
    protected void loadImage() {
        image = Resource.woodIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }


}
