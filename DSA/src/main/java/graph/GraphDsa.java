package graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphDsa {
    private Map<String, ArrayList<String>> adjList = new HashMap<>();

    public void printGraph() {
        System.out.println("Graph Data");
        System.out.println(adjList);
    }

    /**
     *  Big O(1).
     *  This is because HashMap operations such as get and put have an average-case time complexity of O(1).
     */
    public boolean createVertex(String vertex) {

        if (adjList.get(vertex) == null) {
            adjList.put(vertex, new ArrayList<String>());
            return true;
        }
        return false;
    }

    /**
     * Big O(1)
     * adding an element to an ArrayList is O(1) on average but can be worse in the case of resizing the array, which is amortized O(1).
     */
    public boolean addEdges(String vertex1, String vertex2) {
        if (adjList.get(vertex2) != null && adjList.get(vertex1) != null) {
            adjList.get(vertex1).add(vertex2);
            adjList.get(vertex2).add(vertex1);
            return true;
        }
        return false;
    }

    /**
     * Big O(N)
     * This is because removing an element from an ArrayList requires shifting the remaining elements, where N is the number of edges for a vertex. The HashMap operations are O(1), but the list operation dominates.
     */
    public boolean removeEdges(String vertex1, String vertex2) {
        if (adjList.get(vertex2) != null && adjList.get(vertex1) != null) {
            adjList.get(vertex1).remove(vertex2);
            adjList.get(vertex2).remove(vertex1);
            return true;
        }
        return false;
    }

    /**
     * Bog O(V+E)
     * Removing the vertex from the adjacency list of all its connected vertices: O(V + E) in the worst case, where V is the number of vertices and E is the number of edges in the graph. This is because you need to iterate over all edges of the vertex being removed (O(E)) and update each connected vertex's list (O(V))
     */

    public boolean removeVertex(String vertex) {
        if (adjList.get(vertex) == null) return false;
        for (String other : adjList.get(vertex)) {
            adjList.get(other).remove(vertex);
        }
        /*
        // If the graph was unidirectional
        adjList.forEach((k,v)->v.remove(vertex));
         */
        adjList.remove(vertex);
        return true;
    }
}
