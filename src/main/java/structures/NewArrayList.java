package structures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import structures.interfaces.IList;

/**
 * A custom implementation of an ArrayList.
 * <p>
 * This class provides a basic implementation of the IList interface, allowing for dynamic
 * array resizing, element addition, and other fundamental list operations.
 *
 * @param <E> the type of elements stored in this list
 */
public class NewArrayList<E> implements IList<E> {

    private Object[] array; // The array buffer into which the elements of the ArrayList are stored
    private int size; // The current number of elements in the ArrayList
    private int capacity; // The current capacity of the ArrayList

    /**
     * Constructs an empty list with an initial capacity of 100.
     */
    public NewArrayList() {
        this.capacity = 100;
        this.array = new Object[capacity];
        this.size = 0;
    }

    /**
     * Adds an element to the end of the list.
     * 
     * @param element the element to be added
     * @return true (as specified by Collection.add(E))
     */
    public boolean add(E element) {
        ensureCapacity(); // Ensures that the array can accommodate at least one more element
        array[size++] = element;
        return true;
    }

    /**
     * Returns true if this list contains the specified element.
     * 
     * @param element element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    public boolean contains(E element) {
        return indexOf(element) != -1;
    }

    /**
     * Removes all of the elements from this list.
     * The list will be empty after this call returns.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null; // Explicitly clear elements for garbage collection
        }
        size = 0;
    }

    /**
     * Returns true if this list contains no elements.
     * 
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this list.
     * 
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Returns the element at the specified position in this list.
     * 
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @SuppressWarnings("unchecked")
    public E get(int index) {
        rangeCheck(index); // Check if index is within the bounds of the list
        return (E) array[index];
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * 
     * @param element element to search for
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */
    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.
     * 
     * @param element element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public boolean remove(E element) {
        int index = indexOf(element);
        if (index != -1) {
            fastRemove(index); // Remove the element by shifting subsequent elements left
            return true;
        }
        return false;
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * 
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public E set(int index, E element) {
        rangeCheck(index); // Check if index is within the bounds of the list
        E oldValue = get(index);
        array[index] = element;
        return oldValue;
    }

    /**
     * Returns a string representation of this list, containing the String representation
     * of each element.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence
     * (from first to last element). The runtime type of the returned array is that of
     * the specified array. If the list fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the specified array
     * and the size of this list.
     *
     * @param <T> the component type of the array to contain the collection
     * @param a the array into which the elements of this list are to be stored, if
     *          it is big enough; otherwise, a new array of the same runtime type is
     *          allocated for this purpose.
     * @return an array containing the elements of this list
     * @throws ArrayStoreException if the runtime type of the specified array is not a supertype
     *         of the runtime type of every element in this list
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(this.array, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }


    public void sort() {
        sort(null);
    }


    /**
     * Sorts this list according to the order induced by the specified Comparator.
     * All elements in this list must be mutually comparable using the specified
     * comparator (that is, c.compare(e1, e2) must not throw a ClassCastException
     * for any elements e1 and e2 in the list).
     *
     * @param comparator the Comparator used to compare list elements. A null value
     *                   indicates that the elements' natural ordering should be used.
     */
    public void sort(Comparator<E> comparator) {
        // Sort using merge sort or any other stable sort algorithm, enhanced with the given Comparator
        mergeSort(0, size - 1, comparator);
    }

    /**
     * Performs the merge sort algorithm on a portion of the array, sorting the elements
     * between the specified left and right indices, inclusive. This method sorts the elements
     * according to the order induced by the specified Comparator, or according to their
     * natural ordering if the Comparator is null.
     * 
     * The method recursively divides the array segment into two halves, sorts each half, and
     * then merges them back together in sorted order. This is a divide-and-conquer algorithm
     * that guarantees O(n log n) time complexity.
     * 
     * @param left The starting index of the segment of the array to be sorted.
     * @param right The ending index of the segment of the array to be sorted.
     * @param comparator The Comparator to determine the order of the array. A null value
     *        indicates that the elements' natural ordering should be used.
     */
    private void mergeSort(int left, int right, Comparator<E> comparator) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(left, middle, comparator); // Sort the left half
            mergeSort(middle + 1, right, comparator); // Sort the right half
            merge(left, middle, right, comparator); // Merge the sorted halves
        }
    }

    /**
     * Merges two sorted subarrays into a single sorted subarray. The first subarray is
     * defined from {@code left} to {@code middle}, and the second subarray is defined
     * from {@code middle + 1} to {@code right}.
     * 
     * This method is called by the mergeSort method and is responsible for the actual
     * merging process, ensuring that the elements from the two subarrays are combined
     * in sorted order, according to the specified Comparator or their natural ordering
     * if the Comparator is null.
     * 
     * @param left The starting index of the first subarray.
     * @param middle The ending index of the first subarray, which also divides the two subarrays.
     * @param right The ending index of the second subarray.
     * @param comparator The Comparator to determine the order of the elements. A null value
     *        indicates that the elements' natural ordering should be used.
     */
    @SuppressWarnings("unchecked")
    private void merge(int left, int middle, int right, Comparator<E> comparator) {
        int n1 = middle - left + 1; // Number of elements in the first subarray
        int n2 = right - middle; // Number of elements in the second subarray

        // Temporary arrays to hold the elements of the subarrays
        E[] leftArray = (E[]) new Object[n1];
        E[] rightArray = (E[]) new Object[n2];

        // Copy data to temp arrays
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0; // Initial indexes of the first and second subarrays
        int k = left; // Initial index of the merged subarray

        // Merge the temp arrays back into the original array
        while (i < n1 && j < n2) {
            if (comparator != null ? comparator.compare(leftArray[i], rightArray[j]) <= 0
                    : ((Comparable<E>) leftArray[i]).compareTo(rightArray[j]) <= 0) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }

        // Copy remaining elements of leftArray if any
        while (i < n1) {
            array[k++] = leftArray[i++];
        }

        // Copy remaining elements of rightArray if any
        while (j < n2) {
            array[k++] = rightArray[j++];
        }
    }



    /**
     * Removes all of the elements of this collection that satisfy the given predicate.
     * Errors or runtime exceptions thrown during iteration or by the predicate are relayed
     * to the caller.
     *
     * @param filter a predicate which returns {@code true} for elements to be removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     */
    public void removeIf(Predicate<? super E> filter) {
        int dest = 0;
        for (int src = 0; src < size; src++) {
            @SuppressWarnings("unchecked")
            E element = (E) array[src];
            if (!filter.test(element)) {
                array[dest++] = element;
            }
        }
        // Null out the elements beyond the current logical size of the array to let GC do its work
        for (int i = dest; i < size; i++) {
            array[i] = null;
        }
        size = dest;
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an Iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return get(currentIndex++);
            }
        };
    }

    // Helper Methods:

    /**
     * Ensures that the backing array has enough capacity to accommodate at least one more element.
     * If not, the capacity of the array is doubled.
     */
    private void ensureCapacity() {
        if (size == capacity) {
            capacity *= 2;
            array = java.util.Arrays.copyOf(array, capacity);
        }
    }

    /**
     * Removes the element at the specified position in this list. Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     */
    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }
        array[--size] = null; // clear to let GC do its work
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private E elementData(int index) {
        return get(index);
    }
}