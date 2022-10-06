import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Game extends JPanel implements GUI, ActionListener {
    static int width = 23;
    static int height = 15;
    private Block[][] map;
    private Wall[][] walls;
    public static int blockSize = 40;
    public static int tick = 16;
    private Timer timer;
    private Player player;
    private ArrayList<Explosion> explosions;
    private ArrayList<Prize> prizes;
    private Bomb bomb;
    private Heart heart;
    private JButton start;
    private JButton settings;
    private JButton save;
    private HashMap<String, JTextField> settingsFields;
    private ArrayList<Sprite> sprites;
    private JButton back;

    enum State {
        Menu,
        Game,
        Settings,
        Win,
        Lose
    }

    private State state;

    public Game() {
        setPreferredSize(new Dimension(width * blockSize, (height + 2) * blockSize));
        setFocusable(true);
        state = State.Menu;
        add(menuPanel());
    }

    private void startGame() {
        init();
        player.setBombPower(Settings.getSettings().get("bomb-power"));
        player.setBombRadius(Settings.getSettings().get("bomb-radius"));
        addKeyListener(new Adapter());
        timer = new Timer(tick, this);
        timer.start();
    }

    private void endGame() {
        timer.stop();
        back = new JButton("back to menu");
        back.addActionListener(this);
        back.setLocation(width * blockSize / 2, (int) ((height - 0.5) * blockSize));
        add(back);
        revalidate();
        repaint();
    }

    void init() {
        map = new Block[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new Block(j * blockSize, i * blockSize, blockSize, blockSize);
            }
        }
        initWalls();
        initSprites();

        explosions = new ArrayList<>();
        prizes = new ArrayList<>();
        bomb = new Bomb(0, (int) ((height + 0.5) * blockSize), blockSize, blockSize, 0, 0);
        heart = new Heart(blockSize * 4, (int) ((height + 0.5) * blockSize), blockSize, blockSize);

    }

    private void initSprites() {
        player = new Player((width - 2) * blockSize, blockSize, blockSize, blockSize);
        sprites = new ArrayList<>();
        ArrayList<ArrayList<Integer>> freeBlocks = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width / 2; j++) {
                if (walls[i][j] != null)
                    continue;
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(i);
                pair.add(j);
                freeBlocks.add(pair);
            }
        }
        ArrayList<ArrayList<Integer>> locs = randomChoices(freeBlocks, Settings.getSettings().get("ghosts"));
        for (ArrayList<Integer> loc : locs) {
            int i = loc.get(0);
            int j = loc.get(1);
            sprites.add(new Ghost(j * blockSize, i * blockSize, blockSize, blockSize, walls));
        }
        locs = randomChoices(freeBlocks, Settings.getSettings().get("skeletons"));
        for (ArrayList<Integer> loc : locs) {
            int i = loc.get(0);
            int j = loc.get(1);
            sprites.add(new Skeleton(j * blockSize, i * blockSize, blockSize, blockSize, walls));
        }
        locs = randomChoices(freeBlocks, Settings.getSettings().get("bomber-skeletons"));
        for (ArrayList<Integer> loc : locs) {
            int i = loc.get(0);
            int j = loc.get(1);
            sprites.add(new BomberSkeleton(j * blockSize, i * blockSize, blockSize, blockSize, walls));
        }
    }

    private void initWalls() {
        walls = new Wall[height][width];
        for (int i = 0; i < width; i++) {
            walls[0][i] = new Wall(i * blockSize, 0, blockSize, blockSize);
            walls[height - 1][i] = new Wall(i * blockSize, (height - 1) * blockSize, blockSize, blockSize);
        }
        for (int i = 0; i < height; i++) {
            walls[i][0] = new Wall(0, i * blockSize, blockSize, blockSize);
            walls[i][width - 1] = new Wall((width - 1) * blockSize, i * blockSize, blockSize, blockSize);
        }
        for (int i = 2; i < height; i += 2) {
            for (int j = 0; j < width; j += 2) {
                walls[i][j] = new Wall(j * blockSize, i * blockSize, blockSize, blockSize);
            }
        }
        ArrayList<ArrayList<Integer>> freeBlocks = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (walls[i][j] != null)
                    continue;
                if (i == 1 && j >= width - 3)
                    continue;
                if (i == 2 && j >= width - 3)
                    continue;
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(i);
                pair.add(j);
                freeBlocks.add(pair);
            }
        }
        ArrayList<ArrayList<Integer>> locs = randomChoices(freeBlocks, Settings.getSettings().get("specials"));
        for (ArrayList<Integer> loc : locs) {
            int i = loc.get(0);
            int j = loc.get(1);
            walls[i][j] = new SpecialWall(j * blockSize, i * blockSize, blockSize, blockSize);
        }
        locs = randomChoices(freeBlocks, Settings.getSettings().get("woods"));
        for (ArrayList<Integer> loc : locs) {
            int i = loc.get(0);
            int j = loc.get(1);
            walls[i][j] = new WoodenWall(j * blockSize, i * blockSize, blockSize, blockSize);
        }
    }

    ArrayList<ArrayList<Integer>> randomChoices(ArrayList<ArrayList<Integer>> list, int k) {
        Random random = new Random();
        ArrayList<ArrayList<Integer>> randoms = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int idx = random.nextInt(list.size());
            randoms.add(list.get(idx));
            list.remove(idx);
        }
        return randoms;
    }

    @Override
    public void render() {
        if (checkFinish()) {
            endGame();
            return;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j].render();
            }
        }
        renderSprites();
        renderExplosions();
        renderWalls();
        renderPrizes();
        checkCollisions();
        repaint();
    }

    private boolean checkFinish() {
        if (player.lives <= 0) {
            state = State.Lose;
            return true;
        }
        if (sprites.size() == 0) {
            state = State.Win;
            return true;
        }
        return false;
    }

    private void renderPrizes() {
        Iterator<Prize> it = prizes.iterator();
        while (it.hasNext()) {
            Prize prize = it.next();
            if (prize.visible)
                prize.render();
            else
                it.remove();
        }
    }

    private void renderWalls() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (walls[i][j] == null)
                    continue;
                if (walls[i][j].visible)
                    walls[i][j].render();
                else {
                    if (walls[i][j] instanceof SpecialWall) {
                        Random random = new Random();
                        int rng = random.nextInt(3);
                        if (rng == 0)
                            prizes.add(new Heart(j * blockSize, i * blockSize, blockSize, blockSize));
                        if (rng == 1)
                            prizes.add(new BombRadiusPrize(j * blockSize, i * blockSize, blockSize, blockSize));
                        if (rng == 2)
                            prizes.add(new BombPowerPrize(j * blockSize, i * blockSize, blockSize, blockSize));
                    }
                    walls[i][j] = null;
                }
            }
        }
    }


    private void renderExplosions() {
        Iterator<Explosion> it = explosions.iterator();
        while (it.hasNext()) {
            Explosion explosion = it.next();
            if (explosion.visible)
                explosion.render();
            else
                it.remove();
        }
    }

    private void addExplosions(Bomb bomb) {
        explosions.add(new Explosion(bomb.x, bomb.y, blockSize, blockSize, bomb.getPower()));
        int x = bomb.y / blockSize;
        int y = bomb.x / blockSize;
        for (int i = 1; i <= bomb.getRadius(); i++) {
            if (y - i >= 0) {
                if (walls[x][y - i] != null) {
                    if (walls[x][y - i].hits != -1)
                        explosions.add(new HExplosion(bomb.x - i * blockSize, bomb.y, blockSize, blockSize, bomb.getPower()));
                    break;
                }
                explosions.add(new HExplosion(bomb.x - i * blockSize, bomb.y, blockSize, blockSize, bomb.getPower()));
            }
        }
        for (int i = 1; i <= bomb.getRadius(); i++) {
            if (y + i < width) {
                if (walls[x][y + i] != null) {
                    if (walls[x][y + i].hits != -1)
                        explosions.add(new HExplosion(bomb.x + i * blockSize, bomb.y, blockSize, blockSize, bomb.getPower()));
                    break;
                }
                explosions.add(new HExplosion(bomb.x + i * blockSize, bomb.y, blockSize, blockSize, bomb.getPower()));
            }
        }
        for (int i = 1; i <= bomb.getRadius(); i++) {
            if (x - i >= 0) {
                if (walls[x - i][y] != null) {
                    if (walls[x - i][y].hits != -1)
                        explosions.add(new VExplosion(bomb.x, bomb.y - i * blockSize, blockSize, blockSize, bomb.getPower()));
                    break;
                }
                explosions.add(new VExplosion(bomb.x, bomb.y - i * blockSize, blockSize, blockSize, bomb.getPower()));
            }
        }
        for (int i = 1; i <= bomb.getRadius(); i++) {
            if (x + i >= 0) {
                if (walls[x + i][y] != null) {
                    if (walls[x + i][y].hits != -1)
                        explosions.add(new VExplosion(bomb.x, bomb.y + i * blockSize, blockSize, blockSize, bomb.getPower()));
                    break;
                }
                explosions.add(new VExplosion(bomb.x, bomb.y + i * blockSize, blockSize, blockSize, bomb.getPower()));
            }
        }
    }

    private void renderSprites() {
        if (player.visible)
            player.render();
        else {
            player.visible = true;
            player.setX((width - 2) * blockSize);
            player.setY(blockSize);
        }
        Iterator<Sprite> it1 = sprites.iterator();
        while (it1.hasNext()) {
            Sprite sprite = it1.next();
            if (sprite.visible) {
                sprite.render();
                if (sprite instanceof BomberSkeleton bomberSkeleton) {
                    Bomb bomb = bomberSkeleton.getBomb();
                    if (bomb == null)
                        continue;
                    if (bomb.visible)
                        bomb.render();
                    else {
                        addExplosions(bomb);
                        bomberSkeleton.setBomb(null);
                    }
                }
            } else
                it1.remove();
        }
        Iterator<Bomb> it = player.getBombs().iterator();
        while (it.hasNext()) {
            Bomb bomb = it.next();
            if (bomb.visible)
                bomb.render();
            else {
                addExplosions(bomb);
                it.remove();
            }
        }
    }

    private void fixPosition(Wall wall, Sprite sprite) {
        Rectangle intersection = sprite.getBounds().intersection(wall.getBounds());
        if (intersection.width * intersection.height < 16) {
            if (sprite.x < intersection.x)
                sprite.setX(intersection.x - blockSize);
            else
                sprite.setX(intersection.x + intersection.width);
            if (sprite.y < intersection.y)
                sprite.setY(intersection.y - blockSize);
            else
                sprite.setY(intersection.y + intersection.height);
        } else
            sprite.moveBack();
    }

    private void checkCollisions() {
        int j = player.getX() / blockSize;
        int i = player.getY() / blockSize;
        if (walls[i][j] != null && player.getBounds().intersects(walls[i][j].getBounds())) {
            fixPosition(walls[i][j], player);
        } else if (walls[i + 1][j] != null && player.getBounds().intersects(walls[i + 1][j].getBounds())) {
            fixPosition(walls[i + 1][j], player);

        } else if (walls[i][j + 1] != null && player.getBounds().intersects(walls[i][j + 1].getBounds())) {
            fixPosition(walls[i][j + 1], player);

        } else if (walls[i + 1][j + 1] != null && player.getBounds().intersects(walls[i + 1][j + 1].getBounds())) {
            fixPosition(walls[i + 1][j + 1], player);
        }
        for (Sprite sprite : sprites) {
            j = sprite.getX() / blockSize;
            i = sprite.getY() / blockSize;
            if (walls[i][j] != null && sprite.getBounds().intersects(walls[i][j].getBounds())) {
                fixPosition(walls[i][j], sprite);
                continue;
            }
            if (walls[i + 1][j] != null && sprite.getBounds().intersects(walls[i + 1][j].getBounds())) {
                fixPosition(walls[i + 1][j], sprite);
                continue;
            }
            if (walls[i][j + 1] != null && sprite.getBounds().intersects(walls[i][j + 1].getBounds())) {
                fixPosition(walls[i][j + 1], sprite);
                continue;
            }
            if (walls[i + 1][j + 1] != null && sprite.getBounds().intersects(walls[i + 1][j + 1].getBounds())) {
                fixPosition(walls[i + 1][j + 1], sprite);
            }
        }
        for (Explosion explosion : explosions) {
            if (!explosion.isFirstImpact())
                continue;
            if (explosion.getBounds().intersects(player.getBounds())) {
                player.explode(explosion.getPower());
                break;
            }
        }
        for (Sprite sprite : sprites) {
            if (player.getBounds().intersects(sprite.getBounds())) {
                player.explode(1);
                sprite.explode(1);
            }
        }
        for (Explosion explosion : explosions) {
            if (!explosion.isFirstImpact())
                continue;
            explosion.setFirstImpact(false);
            Rectangle bounds = explosion.getBounds();
            int x = bounds.y / blockSize;
            int y = bounds.x / blockSize;
            for (Sprite sprite : sprites)
                if (sprite.getBounds().intersects(explosion.getBounds()))
                    sprite.explode(explosion.getPower());
            if (walls[x][y] == null)
                continue;
            walls[x][y].explode(explosion.getPower());

        }
        for (Prize prize : prizes) {
            if (!prize.visible)
                continue;
            if (player.getBounds().intersects(prize.getBounds())) {
                player.getPrize(prize);
                prize.visible = false;
            }
            for (Sprite sprite : sprites)
                if (sprite.getBounds().intersects(prize.getBounds())) {
                    sprite.getPrize(prize);
                    prize.visible = false;
                }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (state) {
            case Game, Lose, Win -> paintGame(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private JPanel menuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        start = new JButton("start game");
        start.addActionListener(this);
        settings = new JButton("settings");
        settings.addActionListener(this);
        panel.add(start);
        panel.add(settings);
        return panel;
    }


    private void paintGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                g2d.drawImage(map[i][j].getImage(), map[i][j].getX(), map[i][j].getY(), this);
                if (walls[i][j] == null)
                    continue;
                g2d.drawImage(walls[i][j].getImage(), walls[i][j].getX(), walls[i][j].getY(), this);
            }
        }
        for (Explosion explosion : explosions)
            g2d.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);
        for (Prize prize : prizes)
            g2d.drawImage(prize.getImage(), prize.getX(), prize.getY(), this);
        for (Bomb bomb : player.getBombs()) {
            g2d.drawImage(bomb.getImage(), bomb.getX(), bomb.getY(), this);
            g.setColor(Color.red);
            drawText(g, String.valueOf(bomb.getTimer() / 1000.0), bomb.x, bomb.y, 14);
        }
        g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
        for (Sprite sprite : sprites) {
            g2d.drawImage(sprite.getImage(), sprite.getX(), sprite.getY(), this);
            if (sprite instanceof BomberSkeleton bomberSkeleton) {
                if (bomberSkeleton.getBomb() != null)
                    g2d.drawImage(bomberSkeleton.getBomb().getImage(), bomberSkeleton.getBomb().getX(), bomberSkeleton.getBomb().getY(), this);
            }
        }
        paintPanel(g2d);
    }

    private void paintPanel(Graphics2D g2d) {
        if (state == State.Lose) {
            g2d.setColor(Color.red);
            drawText(g2d, "You Lost", width / 2 * blockSize, (height + 1) * blockSize, 25);
            return;
        }
        if (state == State.Win) {
            g2d.setColor(Color.blue);
            drawText(g2d, "You Won", width / 2 * blockSize, (height + 1) * blockSize, 25);
            return;
        }
        g2d.drawImage(bomb.getImage(), bomb.x, bomb.y, this);
        g2d.setColor(Color.black);
        drawText(g2d, String.valueOf(player.getNumBombs()), bomb.x + blockSize, bomb.y, 20);
        drawText(g2d, "P:" + player.bombPower, bomb.x + 2 * blockSize, bomb.y, 20);
        drawText(g2d, "R:" + player.bombRadius, bomb.x + 3 * blockSize, bomb.y, 20);

        g2d.setColor(Color.blue);
        drawText(g2d, String.valueOf(player.getCooldown() / 1000.0), bomb.x, bomb.y, 14);
        for (int i = 0; i < player.lives; i++) {
            g2d.drawImage(heart.getImage(), heart.x + blockSize * 4 + i * blockSize, heart.y, this);
        }
    }


    private void drawText(Graphics g, String text, int x, int y, int size) {
        Font small = new Font("Arial", Font.BOLD, size);
        FontMetrics fm = getFontMetrics(small);
        g.setFont(small);
        g.drawString(text, x + (blockSize - fm.stringWidth(text)) / 2, y + blockSize / 2);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (state == State.Game)
            render();
        if (actionEvent.getSource() == start) {
            removeAll();
            startGame();
            revalidate();
            repaint();
            state = State.Game;
        }
        if (actionEvent.getSource() == settings) {
            removeAll();
            add(settingsPanel());
            revalidate();
            repaint();
            state = State.Settings;
        }
        if (actionEvent.getSource() == save) {
            for (String key : settingsFields.keySet()) {
                Settings.getSettings().set(key, settingsFields.get(key).getText());
            }
            removeAll();
            add(menuPanel());
            revalidate();
            repaint();
            state = State.Menu;
        }
        if (actionEvent.getSource() == back) {
            removeAll();
            add(menuPanel());
            revalidate();
            repaint();
            state = State.Menu;
        }
    }

    private JPanel settingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(Settings.getSettings().size() + 1, 2));
        JTextField field;
        settingsFields = new HashMap<>();
        for (String key : Settings.getSettings().getKeys()) {
            panel.add(new JLabel(key + ": "));
            field = new JTextField(String.valueOf(Settings.getSettings().get(key)));
            panel.add(field);
            settingsFields.put(key, field);
        }
        save = new JButton("save");
        save.addActionListener(this);
        panel.add(save);
        return panel;
    }

    private class Adapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}

