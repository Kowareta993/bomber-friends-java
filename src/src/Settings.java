import java.util.HashMap;
import java.util.Set;

public class Settings {
    private static Settings singleton = null;
    private final HashMap<String, Integer> settings;

    private Settings() {
        settings = new HashMap<>();
        settings.put("woods", 50);
        settings.put("specials", 20);
        settings.put("bomb-power", 1);
        settings.put("bomb-radius", 1);
        settings.put("ghosts", 3);
        settings.put("skeletons", 2);
        settings.put("bomber-skeletons", 1);
    }

    public static Settings getSettings() {
        if (singleton == null)
            singleton = new Settings();
        return singleton;
    }

    public int get(String key) {
        return settings.get(key);
    }

    public int size() {
        return settings.size();
    }

    public void set(String key, String value) {
        set(key, Integer.parseInt(value));
    }

    public Set<String> getKeys() {
        return settings.keySet();
    }

    public void set(String key, int value) {
        settings.put(key, value);
    }
}
