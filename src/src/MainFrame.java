import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super();
        this.setTitle("BomberMan");
        add(new Game());
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public static void main(String[] argv) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
