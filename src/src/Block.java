import java.awt.*;

public class Block implements GUI {
    protected Image image;
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected boolean visible;

    public Block(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        visible = true;
        loadImage();
    }

    protected void loadImage() {
        image = Resource.blockIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public void render() {

    }

    public void explode(int power) {
    }
}
