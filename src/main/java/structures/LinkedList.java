package structures;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.interfaces.IList;

public class LinkedList<E> implements IList<E>, Iterable<E> {
    
    ListElement<E> head;
    private int size = 0;
    ListElement<E> tail;
    
    public LinkedList() {
        this.head = null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private ListElement<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E value = current.getValue();
                current = current.getNext();
                return value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported.");
            }
        };
    }

    public E find(E element) {
        ListElement<E> current = head;
        while (current != null) {
            if (current.getValue().equals(element)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }
    


    public E get(int index) {
        if (isEmpty() || index >= size()) {
            return null;
        }
        // Gets the element at index in the list
        ListElement<E> ptr = head;
        for (int i = 0; i < index; i++) {
            ptr = ptr.getNext();
        }
        return ptr.getValue();
    }

    public int indexOf(E element) {
        // Gets the index of element in the list
        ListElement<E> ptr = head;
        int i = 0;
        while (ptr != null) {
            if (element.equals(ptr.getValue())) {
                return i;
            }
            i++;
            ptr = ptr.getNext();
        }
        return -1;
    }

    public E set(int index, E element) {
        if (isEmpty()) {
            return null;
        }

        // Sets element at index in the list
        ListElement<E> ptr = head;
        ListElement<E> prev = null;

        for (int i = 0; i < index; i++) {
            prev = ptr;
            ptr = ptr.getNext();
        }

        E ret = ptr.getValue();

        ListElement<E> newLink = new ListElement<>(element);
        newLink.setNext(ptr.getNext());
        if (prev != null) {
            prev.setNext(newLink);
        } else {
            head = newLink;
        }

        return ret;
    }

    public boolean add(E element) {
        ListElement<E> newElement = new ListElement<>(element);
        if (head == null) {
            head = tail = newElement;
        } else {
            tail.setNext(newElement);
            tail = newElement;
        }
        size++;
        return true;
    }


    public void clear() {
        head = null;
        size = 0; // Ensure the size reflects the cleared state.
    }
    

    public boolean contains(E element) {
        return indexOf(element) != -1;
    }

    public boolean isEmpty() {
        return head == null;
    }


    public boolean remove(E element) {
        if (head == null) return false;
    
        if (head.getValue().equals(element)) {
            head = head.getNext();
            if (head == null) tail = null; // List became empty
            size--;
            return true;
        }
    
        ListElement<E> current = head;
        while (current.getNext() != null) {
            if (current.getNext().getValue().equals(element)) {
                current.setNext(current.getNext().getNext());
                if (current.getNext() == null) tail = current; // Removed last element
                size--;
                return true;
            }
            current = current.getNext();
        }
    
        return false;
    }
    


    public int size() {
        return size;
    }
    
    
    public String toString() {
        if (head == null) return "[]";
    
        StringBuilder sb = new StringBuilder("[");
        for (ListElement<E> ptr = head; ptr != null; ptr = ptr.getNext()) {
            sb.append(ptr == head ? "" : ", ").append(ptr.getValue());
        }
        sb.append("]");
        return sb.toString();
    }
    
}