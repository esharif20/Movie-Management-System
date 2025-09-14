package structures.interfaces;
import structures.interfaces.IMap;

/**
* An interface for a generic map, from keys of type K to values of type V.
*/
public interface IMap<K,V> {
    
    // Adds a mapping from key to value to the map
    public void put(K key, V value);
    
    // Finds the respective value that is mapped to from key
    public V get(K key);
    
}
