Given the size of a grid, all the connected coordinates in the grid, and the weight of each connection, BackyardDig.java finds the minimum spanning tree of these points. The minimum spanning tree is found by using Kurskal's algorithm. This implementation of Kruskal's algorithm involved taking advantage of minimum priority queues and union finds. Sample file digInput1.txt is included for a sample run.

BackyardDig.java takes in two command line arguments: 
1) name of grid file, which defines the grid (see digInput.txt for format specifications). The first line of the input file specifies the dimensions of the grid. The rest of the lines contain the coordinates of two connected points, followed by the weight between the two points. 

2) name of the output file, to which BackyardDig outputs the minimum spanning tree



Sample run: 
java BackyardDig digInput1.txt digOutput1.txt