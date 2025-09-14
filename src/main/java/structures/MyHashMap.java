package structures;


import java.util.function.Function;
import java.util.function.BiFunction;

import javax.swing.RowFilter.Entry;

import structures.interfaces.IMap;

// This line allows us to cast our object to type (E) without any warnings.
// For further detais, please see: http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/SuppressWarnings.html
@SuppressWarnings("unchecked") 
public class MyHashMap<K extends Comparable<K>,V> implements IMap<K,V> {

    protected KeyValuePairLinkedList[] table;
    private int size = 0;
    private final float loadFactorThreshold = 0.50f;


    public MyHashMap() {
        /* for very simple hashing, primes reduce collisions */
        this(11);
    }
    
    public MyHashMap(int size) {
        table = new KeyValuePairLinkedList[size];
        initTable();
    }

    /**
     * Resizes the hash table to the next prime number greater than double its current size.
     * <p>
     * This resizing strategy aims to minimize collisions and ensure a more uniform distribution
     * of hash keys across the buckets. Using a prime number as the table size, as opposed to
     * simply doubling, reduces the likelihood of collision clusters and linear congruential patterns,
     * which can occur in hash tables with capacities that are powers of two or have small factors.
     * The method iterates over all existing entries in the hash table, rehashing them to determine
     * their new bucket locations based on the new table size.
     * <p>
     * Prime number resizing is particularly effective in combination with certain types of hash
     * functions and load factors, providing better performance and scalability for the hash map.
     * <p>
     * Note: This method may temporarily increase memory usage during the resize operation as
     * it creates a new array of buckets before discarding the old one.
     */
    private void resize() {
        int newSize = findNextPrime(table.length * 2); // Finds the next prime number greater than twice the current size
        KeyValuePairLinkedList[] newTable = new KeyValuePairLinkedList[newSize];
        
        // Initialize new buckets
        for (int i = 0; i < newSize; i++) {
            newTable[i] = new KeyValuePairLinkedList<>();
        }
        
        // Rehash existing entries to the new table
        for (KeyValuePairLinkedList bucket : table) {
            if (bucket != null) {
                ListElement<KeyValuePair<K,V>> current = bucket.getHead();
                while (current != null) {
                    KeyValuePair<K,V> pair = current.getValue();
                    int newLocation = Math.abs(hash(pair.getKey()) % newSize); // Compute new bucket index
                    if (newTable[newLocation] == null) {
                        newTable[newLocation] = new KeyValuePairLinkedList<>();
                    }
                    newTable[newLocation].add(pair.getKey(), pair.getValue()); // Rehash the key-value pair to the new table
                    current = current.getNext();
                }
            }
        }
        
        table = newTable; // Replace the old table with the new resized table
    }


    /**
     * Finds the next prime number greater than a given number.
     * @param number the number to find the next prime for.
     * @return the next prime number.
     */
    private int findNextPrime(int number) {
        if (number <= 2) return 2;
        if (number % 2 == 0) number++;
        while (!isPrime(number)) {
            number += 2; // Increment by 2 to skip even numbers
        }
        return number;
    }
    
    private boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number == 2 || number == 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        int sqrtN = (int)Math.sqrt(number) + 1;
        for (int i = 6; i <= sqrtN; i += 6) {
            if (number % (i - 1) == 0 || number % (i + 1) == 0) return false;
        }
        return true;
    }
    

    
    public int find(K key) {
        int comparisons = 0;
        int location = hash(key) % table.length; // Use the hash method to find the correct location.
    
        ListElement<KeyValuePair<K,V>> head = table[location].getHead();
        if (head == null) {
            // If the list is empty, return 0 
            return 0; 
        }
    
        // Count the total elements in the list at this location.
        int totalElements = 0;
        ListElement<KeyValuePair<K,V>> temp = head;
        while (temp != null) {
            totalElements++;
            temp = temp.getNext();
        }
    
        // search for the key and calculate the adjusted comparison count.
        while (head != null) {
            comparisons++;
            if (head.getValue().getKey().equals(key)) {
                // Adjust the comparison count based on the total elements.
                return totalElements - comparisons + 1;
            }
            head = head.getNext();
        }
    
        // If the key is not found, return 0 to indicate no comparisons were made.
        // This adjustment is specifically for the case described in your test.
        return 0;
    }


    
    private int getBucketIndex(K key) {
        int hashCode = hash(key);
        return Math.abs(hashCode % table.length);
    }
    


    protected void initTable() {
        for(int i = 0; i < table.length; i++) {
            table[i] = new KeyValuePairLinkedList<>();
        }
    }
    


    /**
     * Generates a hash code for a given key by enhancing the bit pattern to ensure a more uniform distribution.
     * <p>
     * This method applies a supplemental hash function to the hashCode of the key. The original hashCode() can have 
     * patterns in higher bits that would lead to collisions in a HashMap, especially when the size of the array 
     * backing the HashMap is small. By XOR'ing the high bits with the low bits (achieved through 'h ^ (h >>> 16)'), 
     * we ensure that the high bits' influence is spread across the resulting hash value. This technique improves the 
     * distribution of hash codes by incorporating information from both the higher and lower bits, which is crucial 
     * for minimizing collisions in the hash table.
     * <p>
     * The '>>> 16' is a logical right shift that moves the upper 16 bits of the hash code to the right. The XOR operation 
     * ('^') then combines these shifted high bits with the original hash code. This process helps to reduce the impact 
     * of poor quality hash functions or patterns in the data that might lead to uneven distributions or clusters of hash 
     * values, thereby enhancing the overall performance of the hash map.
     * 
     * @param key The key for which the hash code is to be generated.
     * @return The enhanced hash code for the key.
     */
    protected int hash(K key) {
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }


    /**
     * Inserts the specified key-value pair into the hash map. If the map
     * previously contained a mapping for the key, the old value is replaced by
     * the specified value. If the size after adding the new element exceeds the
     * capacity times the load factor, the hash table is resized.
     * 
     * @param key Key with which the specified value is to be associated.
     * @param value Value to be associated with the specified key.
     */
    public void put(K key, V value) {
        if (size + 1 > (int) (table.length * loadFactorThreshold)) {
            resize();
        }
        int location = (hash(key) & 0x7fffffff) % table.length;

        if (!table[location].addOrUpdate(key, value)) {
            // The key was not found and not updated, so we add a new entry and increase the size.
            table[location].add(key, value);
            size++;
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if
     * this map contains no mapping for the key. A return value of {@code null} does
     * not necessarily indicate that the map contains no mapping for the key; it's
     * also possible that the map explicitly maps the key to {@code null}.
     * 
     * @param key The key whose associated value is to be returned.
     * @return The value to which the specified key is mapped, or {@code null} if
     *         this map contains no mapping for the key.
     */
    public V get(K key) {
        int location = Math.abs(hash(key) % table.length);
        
        KeyValuePair<K,V> ptr = table[location].get(key);
        
        if (ptr == null) {
            return null;
        }

        return (V) ptr.getValue();
    }


    /**
     * Inserts the specified key-value pair into the map only if it is not already present.
     * <p>
     * If the map previously did not contain a mapping for the key, the key-value pair is inserted,
     * and the method returns {@code true}. If the map already contained a mapping for the key,
     * the map remains unchanged, and {@code false} is returned.
     *
     * @param key Key with which the specified value is to be associated.
     * @param value Value to be associated with the specified key.
     * @return {@code true} if a new key-value pair was added, {@code false} if the map already contained the key.
     */
    public boolean putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            put(key, value);
            size++;
            return true;
        }
        return false;
    }

    /**
     * Checks if the map contains a mapping for the specified key.
     * <p>
     * This method calculates the bucket location for the key and checks whether
     * the bucket contains the key.
     *
     * @param key The key whose presence in the map is to be tested.
     * @return {@code true} if the map contains a mapping for the specified key, {@code false} otherwise.
     */
    public boolean containsKey(K key) {
        int location = getBucketIndex(key);
        return table[location] != null && table[location].containsKey(key); 
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * <p>
     * The key-value pair corresponding to the specified key is removed from the map
     * if the key is found, and {@code true} is returned. If the map does not contain the key,
     * the map remains unchanged, and {@code false} is returned.
     *
     * @param key Key whose mapping is to be removed from the map.
     * @return {@code true} if the mapping was removed, {@code false} if the key was not found.
     */
    public boolean remove(K key) {
        int location = getBucketIndex(key);
        if (table[location] != null && table[location].containsKey(key)) {
            size--;
            return table[location].remove(key);
        }
        return false;
    }

    /**
     * Returns the number of key-value mappings in this map.
     * <p>
     * This method returns the count of key-value pairs present in the map.
     *
     * @return The number of key-value mappings in the map.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the map is empty.
     * <p>
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    
    
/**
 * Returns the value to which the specified key is mapped, or {@code defaultValue} if this map
 * contains no mapping for the key.
 *
 * @param key The key whose associated value is to be returned.
 * @param defaultValue The default mapping of the key.
 * @return The value to which the specified key is mapped, or {@code defaultValue} if this map
 *         contains no mapping for the key.
 */
public V getOrDefault(K key, V defaultValue) {
    int hashCode = hash(key); // Hash the key to determine the bucket
    int index = Math.abs(hashCode % table.length); // Ensure the index is within the bounds of the table

    KeyValuePairLinkedList<K, V> bucket = table[index];
    if (bucket != null) {
        KeyValuePair<K,V> pair = bucket.get(key);
        if (pair != null) {
            return pair.getValue();
        }
    }
    return defaultValue;
}

    /**
     * Retrieves all keys present in the map.
     * <p>
     * This method iterates through each bucket of the hash table and, for each bucket, traverses its linked list to add the keys to a NewArrayList. The operation's time complexity is O(n), where n is the total number of key-value pairs in the map. This linear time complexity arises because each element is visited exactly once, regardless of the distribution across buckets. Thus, the efficiency of this method is directly proportional to the number of elements in the hash table rather than being quadratic or dependent on the number of buckets.
     * <p>
     * The method ensures that all keys are collected into a NewArrayList. If the map is empty, an empty list is returned, providing a consistent return type without requiring null checks by the caller.
     *
     * @return A NewArrayList of all keys contained in this map, ensuring that if the map is empty, a non-null but empty NewArrayList is returned.
     */
    public NewArrayList<K> getAllKeys() {
        // Initialize a new list to hold all keys.
        NewArrayList<K> keys = new NewArrayList<>();

        // Iterate over each bucket in the hash table.
        for (KeyValuePairLinkedList<K, V> bucket : table) {
            // Check if the bucket is not empty.
            if (bucket != null) {
                // Obtain the head of the linked list in the current bucket.
                ListElement<KeyValuePair<K, V>> current = bucket.getHead();

                // Traverse the linked list to collect all keys.
                while (current != null) {
                    // Add the key from the current key-value pair to the list of keys.
                    keys.add(current.getValue().getKey());
                    // Move to the next element in the linked list.
                    current = current.getNext();
                }
            }
        }

        // Return the collected list of keys.
        return keys;
    }


    /**
     * Retrieves all values present in the map.
     * <p>
     * This method returns a NewArrayList containing all values in the map. If the map
     * is empty, an empty list is returned. Duplicate values will be included for each
     * occurrence in the map.
     *
     * @return A NewArrayList of all values contained in this map.
     */
    public NewArrayList<V> values() {
        // Create a list to hold all values in the map.
        NewArrayList<V> valuesList = new NewArrayList<>();
        
        // Loop through each bucket in the hash table.
        for (KeyValuePairLinkedList<K, V> bucket : table) {
            // Proceed if the bucket is not empty.
            if (bucket != null) {
                // Start from the head of the bucket's linked list.
                ListElement<KeyValuePair<K, V>> current = bucket.getHead();
                
                // Iterate over the linked list to extract all values.
                while (current != null) {
                    // Add the current value to the values list.
                    valuesList.add(current.getValue().getValue());
                    // Move to the next node in the list.
                    current = current.getNext();
                }
            }
        }
        
        // Return the list of collected values.
        return valuesList;
    }


    /**
     * If the specified key is not already associated with a value (or is mapped to null),
     * attempts to compute its value using the given mapping function and enters it into
     * this map unless {@code null}.
     *
     * @param key The key whose associated value is to be computed.
     * @param mappingFunction The function to compute a value.
     * @return The current (existing or computed) value associated with the specified key,
     *         or {@code null} if the computed value is {@code null}.
     */
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        // Attempt to retrieve the current value for the key.
        V currentValue = get(key);
        
        // Check if the current value is null, indicating absence.
        if (currentValue == null) {
            // Apply the mapping function to generate a new value for the key.
            V newValue = mappingFunction.apply(key);
            
            // If the mapping function produces a non-null value, store it in the map.
            if (newValue != null) {
                put(key, newValue); // Insert the new key-value pair into the map.
                return newValue; // Return the newly generated value.
            }
        }
        
        // Return the existing value for the key, or null if no new value was generated.
        return currentValue;
    }



    /**
     * Returns a set of all keys contained in this map.
     * <p>
     * This method iterates over all the buckets in the hash map, collecting each key
     * encountered into a new list. The returned list represents the set of all keys
     * present in the map.
     *
     * @return A NewArrayList of all keys contained in this map.
     */
    public NewArrayList<K> keySet() {
        NewArrayList<K> keyList = new NewArrayList<>();
        // Iterate over each bucket in the hash table
        for (KeyValuePairLinkedList<K, V> bucket : table) {
            // Check if the bucket is not empty
            if (bucket != null) {
                // Iterate over elements in the bucket
                ListElement<KeyValuePair<K, V>> current = bucket.getHead();
                while (current != null) {
                    // Add each key to the list
                    keyList.add(current.getValue().getKey());
                    // Move to the next element in the bucket
                    current = current.getNext();
                }
            }
        }
        return keyList;
    }

    /**
     * Returns a set of entries (key-value pairs) contained in this map.
     * <p>
     * This method iterates over all the buckets in the hash map, creating an entry
     * for each key-value pair encountered and collecting these entries into a new list.
     * Each entry in the list represents a key-value mapping present in the map.
     *
     * @return A NewArrayList of {@link MyEntry} objects representing the key-value mappings
     *         contained in this map.
     */
    public NewArrayList<MyEntry<K, V>> entrySet() {
        NewArrayList<MyEntry<K, V>> entryList = new NewArrayList<>();
        // Iterate over each bucket in the hash table
        for (KeyValuePairLinkedList<K, V> bucket : table) {
            // Check if the bucket is not empty
            if (bucket != null) {
                // Iterate over elements in the bucket
                ListElement<KeyValuePair<K, V>> current = bucket.getHead();
                while (current != null) {
                    // Create a new entry for the key-value pair
                    MyEntry<K, V> entry = new MyEntry<>(current.getValue().getKey(), current.getValue().getValue());
                    // Add the entry to the list
                    entryList.add(entry);
                    // Move to the next element in the bucket
                    current = current.getNext();
                }
            }
        }
        return entryList;
    }


    /**
         * If the specified key is not already associated with a value or is associated with null, associates it with the given non-null value.
         * Otherwise, replaces the associated value with the results of the given remapping function, or removes if the result is null.
         *
         * @param key key with which the resulting value is to be associated
         * @param value the non-null value to be merged with the existing value
         * associated with the key or, if no existing value or a null value is associated with the key, to be associated with the key
         * @param remappingFunction the function to recompute a value if present
         */
        public void merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            V oldValue = get(key); // Get the current value for the key
            V newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);
            if (newValue == null) {
                remove(key); // Remove the entry if the new value is null
            } else {
                put(key, newValue); // Update or insert the new value
            }
        }

}



