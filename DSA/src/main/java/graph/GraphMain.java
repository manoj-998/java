package graph;

import common.Common;

public class GraphMain {
    public static void main(String[] args) {

        System.out.println(Common.lineSperator);
        System.out.println("Create new vertex in a graph");
        GraphDsa graphDsa = new GraphDsa();
        System.out.println("Created A : " + graphDsa.createVertex("A"));
        System.out.println("Created B : " + graphDsa.createVertex("B"));
        System.out.println("Created C : " + graphDsa.createVertex("C"));
        graphDsa.printGraph();

        System.out.println(Common.lineSperator);
        System.out.println("Create new Edge b/w vertex");
        System.out.println("Edge b/w A-B : " + graphDsa.addEdges("A", "B"));
        System.out.println("Edge b/w B-C : " + graphDsa.addEdges("B", "C"));
        System.out.println("Edge b/w C-A : " + graphDsa.addEdges("C", "A"));
        graphDsa.printGraph();

        System.out.println(Common.lineSperator);
        System.out.println("Remove Edge b/w vertex");
        System.out.println("Edge b/w A-B : " + graphDsa.removeEdges("A", "B"));
        graphDsa.printGraph();

        System.out.println(Common.lineSperator);
        System.out.println("Remove vertex");
        System.out.println("Vertex A : " + graphDsa.removeVertex("A"));
        graphDsa.printGraph();
    }
}
