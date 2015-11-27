import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 *  The <tt>MinPQ</tt> class represents a priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 *  operations, along with methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a binary heap.
 *  The <em>insert</em> and <em>delete-the-minimum</em> operations take
 *  logarithmic amortized time.
 *  The <em>min</em>, <em>size</em>, and 
 *  <em>is-empty</em> operations take constant time.
 *  Construction takes time proportional to the 
 *  specified capacity or the number of
 *  items used to initialize the data structure.
 *  <p>
 *  For additional documentation, see 
 *  <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  
 *  @param <Key> key for the priority queue
 */
public class MinPQ<Key> {
    
    /**
     * Holder variable for Checkstyle compliance.
     */
    public static final int HOLDER = 4;

    /**
     * Array to store items in indices 1 to N.
     */
    private Key[] pq;                    
    
    /**
     * Number of items in priority queue.
     */
    private int n;                       
    
    /**
     * Optional comparator.
     */
    private Comparator<Key> comparator;  

    /**
     * Initializes an empty priority queue with the given initial capacity.
     * @param initCapacity the initial capacity of the priority queue
     */
    public MinPQ(int initCapacity) {
        this.pq = (Key[]) new Object[initCapacity + 1];
        this.n = 0;
    }

    /**
     * Initializes an empty priority queue.
     */
    public MinPQ() {
        this(1);
    }

    /**
     * Initializes an empty priority queue with the given initial capacity,
     * using the given comparator.
     * @param initCapacity the initial capacity of the priority queue
     * @param comp the order to use when comparing keys
     */
    public MinPQ(int initCapacity, Comparator<Key> comp) {
        this.comparator = comp;
        this.pq = (Key[]) new Object[initCapacity + 1];
        this.n = 0;
    }

    /**
     * Initializes an empty priority queue using the given comparator.
     * @param comp the order to use when comparing keys
     */
    public MinPQ(Comparator<Key> comp) { 
        this(1, comp); 
    }

    /**
     * Initializes a priority queue from the array of keys.
     * Takes time proportional to the number of keys, 
     * using sink-based heap construction.
     * @param keys the array of keys
     */
    public MinPQ(ArrayList<Key> keys) {
        this.n = keys.size();
        this.pq = (Key[]) new Object[keys.size() + 1];
        for (int i = 0; i < this.n; i++) {
            this.pq[i + 1] = keys.get(i);
        }
        for (int k = this.n / 2; k >= 1; k--) {
            this.sink(k);
        }
        assert this.isMinHeap();
    }

    /**
     * Is the priority queue empty?
     * @return true if the priority queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return this.n == 0;
    }

    /**
     * Returns the number of keys on the priority queue.
     * @return the number of keys on the priority queue
     */
    public int size() {
        return this.n;
    }

    /**
     * Returns a smallest key on the priority queue.
     * @return a smallest key on the priority queue
     * @throws java.util.NoSuchElementException if priority queue is empty
     */
    public Key min() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        return this.pq[1];
    }

    /**
     * Helper function to double the size of the heap array.
     * @param capacity 
     */
    private void resize(int capacity) {
        assert capacity > this.n;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= this.n; i++) {
            temp[i] = this.pq[i];
        }
        this.pq = temp;
    }

    /**
     * Adds a new key to the priority queue.
     * @param x the key to add to the priority queue
     */
    public void insert(Key x) {
        // double size of array if necessary
        if (this.n == this.pq.length - 1) {
            this.resize(2 * this.pq.length);
        }

        // add x, and percolate it up to maintain heap invariant
        this.pq[++this.n] = x;
        this.swim(this.n);
        assert this.isMinHeap();
    }

    /**
     * Removes and returns a smallest key on the priority queue.
     * @return a smallest key on the priority queue
     * @throws java.util.NoSuchElementException if the priority queue is empty
     */
    public Key delMin() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        this.exch(1, this.n);
        Key min = this.pq[this.n--];
        this.sink(1);
        this.pq[this.n + 1] = null;         
        if ((this.n > 0) && (this.n == (this.pq.length - 1) / HOLDER)) {
            this.resize(this.pq.length / 2);
        }
        assert this.isMinHeap();
        return min;
    }


   /***********************************************************************
    * Helper functions to restore the heap invariant.
    **********************************************************************/

    /**
     * Method for swimming element up pq.
     * @param k index of element to bring up.
     */
    private void swim(int k) {
        while (k > 1 && this.greater(k / 2, k)) {
            this.exch(k, k / 2);
            k = k / 2;
        }
    }
    
    /**
     * Method for sinking element down pq.
     * @param k index of element to go down.
     */
    private void sink(int k) {
        while (2 * k <= this.n) {
            int j = 2 * k;
            if (j < this.n && this.greater(j, j + 1)) {
                j++;
            }
            if (!this.greater(k, j)) {
                break;
            }
            this.exch(k, j);
            k = j;
        }
    }

   /***********************************************************************
    * Helper functions for compares and swaps.
    **********************************************************************/
    
    /**
     * Check which element in the pq is greater.
     * @param i first index for comparison
     * @param j second index for comparison
     * @return boolean indicating which element is greater.
     */
    private boolean greater(int i, int j) {
        if (this.comparator == null) {
            return ((Comparable<Key>) this.pq[i]).compareTo(this.pq[j]) > 0;
        } else {
            return this.comparator.compare(this.pq[i], this.pq[j]) > 0;
        }
    }
    /**
     * Method to swap elements in pq.
     * @param i index of first element.
     * @param j index of second element.
     */
    private void exch(int i, int j) {
        Key swap = this.pq[i];
        this.pq[i] = this.pq[j];
        this.pq[j] = swap;
    }

    /**
     * Checks if pq[1..N] a min heap?
     * @return true or false if this is a minHeap.
     */
    private boolean isMinHeap() {
        return this.isMinHeap(1);
    }

    /**
     * Helper method if this subtree of pq[1..N] rooted at k is a min heap.
     * @param k index to check for.
     * @return true or false subtree is rooted at k.
     */
    private boolean isMinHeap(int k) {
        if (k > this.n) {
            return true;
        }
        int left = 2 * k, right = 2 * k + 1;
        if (left  <= this.n && this.greater(k, left)) {
            return false;
        }
        if (right <= this.n && this.greater(k, right)) {
            return false;
        }
        return this.isMinHeap(left) && this.isMinHeap(right);
    }




}