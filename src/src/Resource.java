import javax.swing.*;
import java.util.Objects;

public class Resource {
    static ImageIcon blockIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("block.png")));
    static ImageIcon wallIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("wall.png")));
    static ImageIcon playerIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("player.png")));
    static ImageIcon bombIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("bomb.png")));
    static ImageIcon expIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("explosion.png")));
    static ImageIcon exphIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("explosion-row.png")));
    static ImageIcon expvIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("explosion-col.png")));
    static ImageIcon woodIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("wood.png")));
    static ImageIcon heartIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("heart.png")));
    static ImageIcon ghostIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("ghost.png")));
    static ImageIcon skeletonIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("skeleton.png")));
    static ImageIcon skeleton2Icon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("skeleton2.png")));
    static ImageIcon powerIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("power.png")));
    static ImageIcon radiusIcon = new ImageIcon(Objects.requireNonNull(Resource.class.getClassLoader().getResource("radius.png")));


}
