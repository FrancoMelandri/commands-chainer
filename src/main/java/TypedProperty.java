import java.util.HashMap;
import java.util.Map;

public class TypedProperty {
    private Map<String, Object> map;

    public TypedProperty() {
        this.map = new HashMap<>();
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }
}
