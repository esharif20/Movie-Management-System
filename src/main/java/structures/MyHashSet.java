package structures;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Represents a hash set that stores elements using a hash table.
 * <p>
 * This implementation of a hash set uses an array of linked lists to handle collisions,
 * dynamically resizing the underlying array when the load factor exceeds a certain threshold.
 * It also optimizes hash distribution by resizing the array to the next prime number size.
 */
public class MyHashSet<E> implements Iterable<E> {
    private LinkedList<E>[] buckets;
    private int capacity;
    private int size;
    private static final int DEFAULT_CAPACITY = 29;
    private static final float loadFactor = 0.75f;

    /**
     * Constructs a new hash set with the default initial capacity and load factor.
     */
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    /**
     * Determines if the hash set needs resizing.
     * 
     * @return {@code true} if the current size exceeds the capacity multiplied by the load factor, {@code false} otherwise.
     */
    private boolean needsResizing() {
        return size >= capacity * loadFactor;
    }

    /**
     * Resizes the hash set by increasing the capacity to the next prime number greater than double the current capacity
     * and rehashes all existing elements to the new array of buckets.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        // Double the current capacity and find the next prime number for the new capacity.
        int newCapacity = findNextPrimeEfficiently(capacity * 2);
        // Initialize a new array of LinkedLists with the new capacity.
        LinkedList<E>[] newBuckets = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }

        // Rehash all existing elements into the new buckets array.
        for (LinkedList<E> bucket : buckets) {
            if (bucket != null) {
                for (E element : bucket) {
                    // Calculate the new index for each element.
                    int newIndex = Math.abs(element.hashCode()) % newCapacity;
                    // Add the element to its new bucket.
                    newBuckets[newIndex].add(element);
                }
            }
        }

        // Update the buckets array and capacity to the new values.
        buckets = newBuckets;
        capacity = newCapacity;
    }


    /**
     * Finds the next prime number efficiently.
     * 
     * @param number The starting number to find the next prime number.
     * @return The next prime number greater than the given number.
     */
    private int findNextPrimeEfficiently(int number) {
        if (number % 2 == 0) number++;
        while (!isPrime(number)) {
            number += 2;
        }
        return number;
    }

    /**
     * Checks if a number is prime.
     * 
     * @param number The number to check.
     * @return {@code true} if the number is prime, {@code false} otherwise.
     */
    private boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number == 2 || number == 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) return false;
        }
        return true;
    }

    /**
     * Returns an iterator over the elements in this set.
     * 
     * @return An {@code Iterator<E>} that can be used to iterate over the elements of the set.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentBucket = 0;
            private Iterator<E> bucketIterator = buckets[currentBucket].iterator();

            private void moveToNextNonEmptyBucket() {
                while (currentBucket < buckets.length - 1 && !bucketIterator.hasNext()) {
                    currentBucket++;
                    bucketIterator = buckets[currentBucket].iterator();
                }
            }

            @Override
            public boolean hasNext() {
                moveToNextNonEmptyBucket();
                return bucketIterator.hasNext();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return bucketIterator.next();
            }
        };
    }

    /**
     * Adds the specified element to the set if it is not already present.
     * 
     * @param element The element to be added to the set.
     * @return {@code true} if the set did not already contain the specified element, {@code false} otherwise.
     */
    public boolean add(E element) {
        if (needsResizing()) {
            resize();
        }
        int bucketIndex = getBucketIndex(element);
        LinkedList<E> bucket = buckets[bucketIndex];
        if (!bucket.contains(element)) {
            bucket.add(element);
            size++;
            return true;
        }
        return false;
    }

    /**
     * Determines if the set contains the specified element.
     * 
     * @param element The element whose presence in the set is to be tested.
     * @return {@code true} if the set contains the specified element, {@code false} otherwise.
     */
    public boolean contains(E element) {
        int bucketIndex = getBucketIndex(element);
        return buckets[bucketIndex].contains(element);
    }

    /**
     * Removes the specified element from the set if it is present.
     * 
     * @param element The element to be removed from the set.
     * @return {@code true} if the set contained the specified element, {@code false} otherwise.
     */
    public boolean remove(E element) {
        int bucketIndex = getBucketIndex(element);
        boolean wasRemoved = buckets[bucketIndex].remove(element);
        if (wasRemoved) {
            size--;
        }
        return wasRemoved;
    }

    /**
     * Returns the number of elements in the set.
     * 
     * @return The number of elements in the set.
     */
    public int size() {
        return size;
    }

    public boolean isEmpty(){
        return size==0;
    }

    /**
     * Removes all of the elements from the set.
     * The set will be empty after this call returns.
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i].clear();
        }
        size = 0;
    }
    
    /**
     * Returns an array containing all of the elements in this set.
     * 
     * @param a The array into which the elements of the set are to be stored, if it is big enough;
     *          otherwise, a new array of the same runtime type is allocated for this purpose.
     * @return An array containing all the elements in this set.
     */
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int index = 0;
        for (LinkedList<E> bucket : buckets) {
            if (bucket != null) {
                for (E element : bucket) {
                    a[index++] = (T)element;
                }
            }
        }

        if (index < a.length) {
            a[index] = null;
        }

        return a;
    }

    /**
     * Returns the element itself if it is present in the set.
     * 
     * @param element The element to retrieve from the set.
     * @return The element if present, {@code null} otherwise.
     */
    public E get(E element) {
        // Calculate the index for the element's bucket.
        int bucketIndex = getBucketIndex(element);
        // Retrieve the bucket at the calculated index.
        LinkedList<E> bucket = buckets[bucketIndex];
        
        // Check if the bucket is not empty.
        if (bucket != null) {
            // Iterate through elements in the bucket.
            for (E currentElement : bucket) {
                // Return the element if it matches the search query.
                if (currentElement.equals(element)) {
                    return currentElement;
                }
            }
        }
        // Return null if the element is not found.
        return null;
    }

    
    /**
     * Calculates the bucket index for a given element based on its hash code.
     * 
     * @param element The element for which to calculate the bucket index.
     * @return The calculated bucket index.
     */
    private int getBucketIndex(E element) {
        return Math.abs(element.hashCode()) % capacity;
    }
}
