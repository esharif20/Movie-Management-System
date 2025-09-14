package structures;
import structures.interfaces.IMap;

public class KeyValuePairLinkedList<K extends Comparable<K>,V> {

    protected ListElement<KeyValuePair<K,V>> head;
    protected int size;
    
    public KeyValuePairLinkedList() {
        head = null;
        size = 0;
    }
    
    public void add(K key, V value) {
        this.add(new KeyValuePair<K,V>(key,value));
    }

    public void add(KeyValuePair<K,V> kvp) {
        ListElement<KeyValuePair<K,V>> new_element = 
                new ListElement<>(kvp);
        new_element.setNext(head);
        head = new_element;
        size++;
    }
    
    public int size() {
        return size;
    }
    
    public ListElement<KeyValuePair<K,V>> getHead() {
        return head;
    }
    
    public KeyValuePair<K,V> get(K key) {
        ListElement<KeyValuePair<K,V>> temp = head;
        
        while(temp != null) {
            if(temp.getValue().getKey().equals(key)) {
                return temp.getValue();
            }
            
            temp = temp.getNext();
        }
        
        return null;
    }

    // Method to check if the list contains a given key
    public boolean containsKey(K key) {
        ListElement<KeyValuePair<K, V>> current = head;
        while (current != null) {
            if (current.getValue().getKey().equals(key)) {
                return true; // Key found
            }
            current = current.getNext(); // Move to the next element
        }
        return false; // Key not found
    }

    public boolean remove(K key) {
        if (head == null) {
            return false; // List is empty, nothing to remove
        }

        // Special case for head
        if (head.getValue().getKey().equals(key)) {
            head = head.getNext();
            size--;
            return true;
        }

        ListElement<KeyValuePair<K, V>> current = head;
        while (current.getNext() != null) {
            if (current.getNext().getValue().getKey().equals(key)) {
                current.setNext(current.getNext().getNext()); // Remove the element
                size--;
                return true; // Key was found and removed
            }
            current = current.getNext();
        }

        return false; // Key was not found
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void update(K key, V value) {
    ListElement<KeyValuePair<K, V>> current = head;
    // Check if the current element's key matches the key to update
    while (current != null) {
        if (current.getValue().getKey().equals(key)) {
            // Update the value of the KeyValuePair
            current.getValue().setValue(value);
            return; // Exit the method as we've done the update
        }
        current = current.getNext(); // Move to the next element
    }
    // If we reach here, the key was not found; optionally, you can handle this case
}

public boolean addOrUpdate(K key, V value) {
    ListElement<KeyValuePair<K,V>> current = head;
    while (current != null) {
        if (current.getValue().getKey().equals(key)) {
            current.getValue().setValue(value); // Assuming KeyValuePair has a setValue method.
            return true;
        }
        current = current.getNext();
    }
    return false;
}

    

}
