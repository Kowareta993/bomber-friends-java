import java.awt.*;

public class Heart extends Prize {
    public Heart(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    protected void loadImage() {
        image = Resource.heartIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}
