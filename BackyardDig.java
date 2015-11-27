import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * Program to determine network of tunnels to 
 * locate all buried cars with minimal work. 
 * @author Emily, Hansin, Mariya
 */
public final class BackyardDig {
    
    /**
     * Constructor for Checkstyle compliance.
     */
    private BackyardDig() {
        
    }
    
    /**
     * Main method to accept file of Backyard locations and 
     * then run Kruskal's.
     * @param args user's input.
     * @throws IOException if insufficient arguments are input.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            throw new IllegalArgumentException("One argument are required.");
        }

        String fileName = args[0];
        String outFile = args[1];
        Scanner fromFile = new Scanner(new FileReader(fileName));
        PrintWriter writer = new PrintWriter(outFile);
        
        int rows = fromFile.nextInt();
        int cols = fromFile.nextInt();
        ArrayList<Edge> list = new ArrayList<>();
        int numVertices = 0;
        int endWeight = 0;

        fromFile.nextLine();
        fromFile.nextLine();
        boolean[] vertex = new boolean[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            vertex[i] = false;
        }

        while (fromFile.hasNextLine()) {
            String input = fromFile.nextLine();
            input = input.replace('(', ' ');
            input = input.replace(')', ' ');
            input = input.replace(',', ' ');
            Scanner stringScan = new Scanner(input);
            int x1 = stringScan.nextInt();
            int y1 = stringScan.nextInt();
            int x2 = stringScan.nextInt();
            int y2 = stringScan.nextInt();
            int weight = stringScan.nextInt();
            int index1 = ((x1 * cols) + y1);
            int index2 =  ((x2 * cols) + y2);
            if (!vertex[index1]) {
                numVertices++;
                vertex[index1] = true;
            }

            if (!vertex[index2]) {
                numVertices++;
                vertex[index2] = true;
            }

            Edge e = new Edge(index1, index2, weight);
            list.add(e);
            stringScan.close();
        }
        
        //starting kruskal's
        ArrayList<Edge> kruskal = kruskal(list, numVertices);
        for (int i = 0; i < kruskal.size(); i++) {
            Edge x = kruskal.get(i);
            endWeight = endWeight + x.weight;
        }

        writer.println(endWeight + "\n");
        for (int i = 0; i < kruskal.size(); i++) {
            Edge x = kruskal.get(i);
            writer.println("(" + (x.u / cols) + "," 
                    + (x.u % cols) + ")" + " " + "(" 
                    + (x.v / cols) + "," + (x.v % cols) + ")");
        }

        writer.close();
        fromFile.close();
    }
    
    /**
     * Method for running Kruskal's algorithm.
     * @param edges ArrayList of edges
     * @param numVertices the number of total vertices
     * @return ArrayList of Edges
     */
    public static ArrayList<Edge> kruskal(ArrayList<Edge> edges, 
            int numVertices) {
        UnionFindQuickUnions ds = new UnionFindQuickUnions(numVertices);
        MinPQ<Edge> pq = new MinPQ<Edge>(edges);
        ArrayList<Edge> mst = new ArrayList<>();
        while (mst.size() != numVertices - 1) {
            Edge e = pq.delMin();       // Edge e = (u, v)
            int uset = ds.find(e.u);
            int vset = ds.find(e.v);
            if (uset != vset) {
                // Accept the edge
                mst.add(e);
                ds.union(uset, vset);
            } 
        }

        return mst; 
    }

    /**
     * Inner class for Edge object.
     */
    private static class Edge implements Comparable<Edge> {
        
        /**
         * The start coordinate.
         */
        protected int u;
        
        /**
         * The end coordinate.
         */
        protected int v;
        
        /**
         * The weight of the Edge.
         */
        protected int weight;

            /**
             * Constructor for Edge.
             * @param x first coordinate.
             * @param y second coordinate.
             * @param w weight value.
             */
        public Edge(int x, int y, int w) {
            this.u = x;
            this.v = y;
            this.weight = w;
        }
        
        /**
         * Override compareTo method.
         * @param b the Edge to compare with.
         * @return int representing which Edge is of greater weight.
         */
        public int compareTo(Edge b) {
            if (this.weight < b.weight) {
                return -1;
            } else if (this.weight > b.weight) {
                return 1;
            }

            return 0;
        }
    }
}