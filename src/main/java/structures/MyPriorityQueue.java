package structures;

import java.util.Collection;
import java.util.Comparator;

/**
 * A custom implementation of a priority queue.
 * <p>
 * This priority queue organizes elements according to their natural ordering or according to a {@link Comparator}
 * provided at queue construction time. The queue supports insertion ({@code offer}), extraction ({@code poll}),
 * and inspection ({@code peek}) operations.
 *
 * @param <E> the type of elements held in this priority queue
 */
public class MyPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] array; // The array buffer into which the elements of the priority queue are stored
    private int size; // The number of elements in the priority queue
    private Comparator<E> comparator; // Optional comparator to determine the order of elements

    /**
     * Constructs a priority queue with the default initial capacity (10) that orders its elements
     * according to their natural ordering.
     */
    public MyPriorityQueue() {
        this(DEFAULT_CAPACITY, null);
    }

    /**
     * Constructs a priority queue with the default initial capacity (10) that orders its elements
     * according to the specified comparator.
     *
     * @param comparator the comparator that will be used to order this priority queue. If {@code null},
     *                   the natural ordering of the elements will be used.
     */
    public MyPriorityQueue(Comparator<E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    /**
     * Constructs a priority queue with the specified initial capacity that orders its elements
     * according to the specified comparator.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @param comparator the comparator that will be used to order this priority queue. If {@code null},
     *                   the natural ordering of the elements will be used.
     * @throws IllegalArgumentException if {@code initialCapacity} is less than 1
     */
    public MyPriorityQueue(int initialCapacity, Comparator<E> comparator) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.array = new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Returns the number of elements in this priority queue.
     *
     * @return the number of elements in this priority queue
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this priority queue contains no elements.
     *
     * @return {@code true} if this priority queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds the specified element to this priority queue.
     *
     * @param element the element to add
     */
    public void offer(E element) {
        ensureCapacity(size + 1);
        array[size++] = element;
        heapifyUp(size - 1);
    }

    /**
     * Retrieves and removes the head of this queue, or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @SuppressWarnings("unchecked")
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E removedElement = (E) array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;
        heapifyDown(0);
        return removedElement;
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return (E) array[0];
    }

        /**
     * Ensures that the internal array has the capacity to accommodate additional elements.
     * If not, the capacity of the array is increased.
     *
     * @param minCapacity the minimum capacity needed to accommodate additional elements
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > array.length) {
            int newCapacity = array.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    /**
     * Performs the "heapify-up" operation to restore the heap property after adding a new element.
     * Starting from the given index, the method ensures that the heap property is maintained by
     * comparing the added element with its parent and swapping if necessary.
     *
     * @param index the index of the newly added element
     */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E current = (E) array[index];
            E parent = (E) array[parentIndex];
            if (compare(current, parent) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Performs the "heapify-down" operation to restore the heap property after removing the root element.
     * Starting from the given index, the method ensures that the heap property is maintained by
     * comparing the element with its children and swapping with the smaller child if necessary.
     *
     * @param index the index of the element to heapify down
     */
    private void heapifyDown(int index) {
        int childIndex;
        while ((childIndex = 2 * index + 1) < size) {
            int rightChild = childIndex + 1;
            if (rightChild < size && compare((E) array[rightChild], (E) array[childIndex]) < 0) {
                childIndex = rightChild;
            }
            if (compare((E) array[index], (E) array[childIndex]) <= 0) {
                break;
            }
            swap(index, childIndex);
            index = childIndex;
        }
    }

    /**
     * Compares two elements using the provided comparator or their natural ordering if no comparator is provided.
     *
     * @param o1 the first object to be compared
     * @param o2 the second object to be compared
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to,
     *         or greater than the second
     */
    private int compare(E o1, E o2) {
        if (comparator != null) {
            return comparator.compare(o1, o2);
        } else {
            return ((Comparable<E>) o1).compareTo(o2);
        }
    }

    /**
     * Swaps the elements at the specified positions in the internal array.
     *
     * @param i the index of the first element to be swapped
     * @param j the index of the second element to be swapped
     */
    private void swap(int i, int j) {
        Object temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Adds all of the elements in the specified collection to this priority queue.
     *
     * @param collection the collection containing elements to be added to this priority queue
     */
    public void addAll(NewArrayList<? extends E> collection) {
        ensureCapacity(size + collection.size());
        for (E element : collection) {
            array[size++] = element;
        }
        heapify();
    }

    /**
     * Restores the heap property for the entire priority queue.
     */
    private void heapify() {
        // Start from the last non-leaf node and proceed upwards
        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }
}
