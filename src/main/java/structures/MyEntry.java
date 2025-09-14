package structures;

public class MyEntry<K, V> {
    public K key;
    public V value;

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    // Public getters (and optionally setters) for key and value
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
