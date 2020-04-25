package MES;

import java.util.*;

public class DirectedGraph {

    private Map<Vertex, ArrayList<Edge>> adjVertices;

    public DirectedGraph() {
        adjVertices= new HashMap<>();
    }

    void addVertex(String label) {
        Vertex v = new Vertex(label);
        ArrayList <Edge> edges = new ArrayList<>();
        adjVertices.putIfAbsent(v, edges);
    }

    void removeVertex(String label) {
        Vertex v = new Vertex(label);
        adjVertices.values().stream().forEach(e -> e.remove(v));
        adjVertices.remove(new Vertex(label));
    }

    void addEdge(String label1, String label2, String machine, String tool, String time) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        Edge edge = new Edge(v1,v2, machine, tool, time);
        adjVertices.get(v1).add(edge);
    }

    void removeEdge(String label1, String label2, String machine, String tool, String time) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        Edge edge = new Edge(v1,v2, machine, tool, time);
        List<Edge> eV1 = adjVertices.get(v1);
        if (eV1 != null)
            eV1.remove(edge);
    }


    public Map<Vertex, ArrayList<Edge>> getVertexMap() {
        return this.adjVertices;
    }
}


    class Vertex {
        String label;

        Vertex(String label) {
            this.label = label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return Objects.equals(label, vertex.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }

        public String getLabel() {
            return label;
        }

        // equals and hashCode
    }

    class Edge {

        String machine;
        Vertex key;
        Vertex pieceTypeResult;
        Integer time;
        String tool;

        Edge(Vertex key,Vertex pieceTypeResult, String machine, String tool, String time) {
            this.machine=machine;
            this.pieceTypeResult=pieceTypeResult;
            this.time=Integer.parseInt(time);
            this.tool=tool;
        }





    }
