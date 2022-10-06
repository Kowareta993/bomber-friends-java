import java.awt.*;

public class Ghost extends Sprite {
    public Ghost(int x, int y, int w, int h, Wall[][] map) {
        super(x, y, w, h, map);
        lives = 1;
        speed = 3 * speed;
        start();
    }


    @Override
    protected void loadImage() {
        image = Resource.ghostIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }




}
