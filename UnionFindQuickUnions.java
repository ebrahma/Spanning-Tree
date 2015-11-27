import java.util.Stack;


//import java.lang.IllegalArgumentException;
/**
 * A class that uses an array to do efficient unions.
 * @author mariya_kazachkova
 *
 */
public class UnionFindQuickUnions implements UnionFind {

      //constructor
    /**Array to hold the sets.*/
    private int[] myArray;
    /**A stack that will help in path compression.*/
    private Stack<Integer> myStack = new Stack<Integer>(); 
      
    /**
     * The constructor that initializes all of the spots in the array to -1. 
     * @param x The size of the array.
     */
    public UnionFindQuickUnions(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Must be a positive number");
        }
        this.myArray = new int[x];
        //initializing to -1
        for (int i = 0; i < x; i++) {
            this.myArray[i] = -1;
        }   
    }

     /**
     * Determine the name of the set containing the specified element.
     * @param x the element whose set we wish to find
     * @return the name of the set containing x
     */
    public int find(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Must be non-negative");
        }
        int maybeRoot = this.myArray[x];
        if (maybeRoot < 0) {
            return x; //already root
        }
        return this.findHelper(x);
      
    }
    /**
     * Helper method for the above find method.
     * @param x The elemnt we wish to find.
     * @return The name of the set containing x.
     */
    private int findHelper(int x) {
      //we found root
        if (this.myArray[x] < 0) {
            for (int i = 0; i <= this.myStack.size(); i++) {
                int position = this.myStack.pop();
                this.myArray[position] = x; //do path compression
            }
            return x;
        }  
      //keep looking
        this.myStack.push(x);
        return this.findHelper(this.myArray[x]);
          
    }

    /**
     * Merge two sets if they are not already the same set.
     * @param a an item in the first set to be merged (need not be set name)
     * @param b an item in the second set to be merged (need not be set name)
     */
    public void union(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("Must be non-negative");
        }
        int aRoot = this.find(a);
        int bRoot = this.find(b);
        if (aRoot == bRoot) {
            return; //same set
        }    
        int bigger;
        int smaller;
        //which one is more negative
        if (this.myArray[aRoot] < this.myArray[bRoot] 
                || this.myArray[aRoot] == this.myArray[bRoot]) {
            bigger = aRoot;
            smaller = bRoot;
        } else {
            bigger = bRoot;
            smaller = aRoot;
        }
      
      
      
     
        int newSize = this.myArray[bigger] + this.myArray[smaller];
        this.myArray[smaller] = bigger;
        this.myArray[bigger] = newSize;
        
        
    }

    /**
     * Return the number of subsets in the structure.
     * @return the number of subsets
     */
    public int getNumSubsets() {
        int counter = 0; //will count negative numbers (roots)
     
        for (int i = 0; i < this.myArray.length; i++) {
        
            if (this.myArray[i] < 0) {
                counter++;
            }
        }
        int returned = counter;  
        return returned; 
    }    

    /**
     * Returns a String representation of the implementation.  Normally
     * this would never be part of an interface like this, but will help us
     * test your implementation in a consistent way.  See assignment handout.
     * @return a String representing the current state of the structure
     */
    public String getCurrentState() {
        String returned = "";
        for (int i = 0; i < this.myArray.length; i++) {
            returned = returned + i + ": " + this.myArray[i] + "\n";
        }
        return returned;
    }

}
