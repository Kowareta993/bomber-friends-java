import java.awt.*;

public class Skeleton extends Sprite {
    public Skeleton(int x, int y, int w, int h, Wall[][] map) {
        super(x, y, w, h, map);
        lives = 3;
        start();
    }

    @Override
    protected void loadImage() {
        image = Resource.skeletonIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }


}
