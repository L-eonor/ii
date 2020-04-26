package MES;



import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class TransformationsGraph {
    private DirectedGraph graph = new DirectedGraph();

    public PriorityQueue<GraphSolution> solutions;

    private String Px;
    private String Py;

    public TransformationsGraph(){
        initialize();
    }

    public void initialize() {
        graph.addVertex("P1");
        graph.addVertex("P2");
        graph.addVertex("P3");
        graph.addVertex("P4");
        graph.addVertex("P5");
        graph.addVertex("P6");
        graph.addVertex("P7");
        graph.addVertex("P8");
        graph.addVertex("P9");

        graph.addEdge("P1","P2", "Ma", "1","15");
        graph.addEdge("P1","P3", "Mb", "1","20");
        graph.addEdge("P1","P4", "Mc", "1","10");
        graph.addEdge("P2","P3", "Ma", "1","15");
        graph.addEdge("P2","P6", "Ma", "2","15");
        graph.addEdge("P3","P4", "Mb", "1","15");
        graph.addEdge("P3","P7", "Mb", "2","20");
        graph.addEdge("P4","P5", "Mc", "1","30");
        graph.addEdge("P4","P8", "Mc", "2","10");
        graph.addEdge("P6","P9", "Ma", "3","15");
        graph.addEdge("P7","P9", "Mb", "3","20");
        graph.addEdge("P8","P9", "Mc", "3","10");


    }

    public boolean searchTransformations(String Px, String Py) {

        this.Px = Px;
        this.Py = Py;

        solutions = new PriorityQueue<>((s1, s2) -> s1.getTotalTimeCost() - s2.getTotalTimeCost());
        GraphSolution localPath= new GraphSolution();
        Vertex v = new Vertex(Px);

        if(!v.getLabel().equals(Py)){
            expand(v, localPath);
            return true;
        }
        else return false;

    }

    public void expand(Vertex v, GraphSolution localPath ) {
        if(v.label.equals(Py)) {
            solutions.add(localPath);
            return;
        }

        List<Edge> edges = this.graph.getVertexMap().get(v);

        if(!edges.isEmpty()){
            for (int i = 0; i < edges.size(); i++) {
                GraphSolution copyLocalPath= new GraphSolution(localPath);
                Edge adjacency = edges.get(i);
                copyLocalPath.transformations.add(adjacency.pieceTypeResult.getLabel());
                copyLocalPath.timeCost.add(adjacency.time);
                copyLocalPath.machines.add(adjacency.machine);
                copyLocalPath.tool.add(adjacency.tool);

                Vertex adjacentVertex = adjacency.pieceTypeResult;
                expand(adjacentVertex, copyLocalPath);
            }
        }
        else return;

        return;

    }


}

class GraphSolution {
        public ArrayList<String> transformations;
        public ArrayList<String> machines;
        public ArrayList<Integer> timeCost;
        public ArrayList<String> tool;

    public GraphSolution() {
        transformations = new ArrayList<>();
        machines = new ArrayList<>();
        timeCost = new ArrayList<>();
        tool = new ArrayList<>();
    }

    public GraphSolution(GraphSolution s) {
        transformations = new ArrayList<>();
        transformations = cloneArrayListOfString(s.transformations);
        machines = new ArrayList<>();
        machines = cloneArrayListOfString(s.machines);
        timeCost = new ArrayList<>();
        timeCost = cloneArrayListOfInt(s.timeCost);
        tool = new ArrayList<>();
        tool = cloneArrayListOfString(s.tool);
    }

    @Override
    public String toString() {
        return "GraphSolution{" +
                "transformations=" + transformations +
                ", machines=" + machines +
                ", timeCost=" + timeCost +
                ", tool=" + tool +
                '}';
    }


    private ArrayList<String> cloneArrayListOfString (ArrayList<String> toClone) {

        ArrayList<String> copy = new ArrayList<>();

        for(int i=0; i < toClone.size(); i++) {
            String s = toClone.get(i);
            copy.add(s);
        }

        return copy;
    }

    private ArrayList<Integer> cloneArrayListOfInt (ArrayList<Integer> toClone) {

        ArrayList<Integer> copy = new ArrayList<>();

        for(int i=0; i < toClone.size(); i++) {
            int n = toClone.get(i);
            copy.add(n);
        }

        return copy;
    }


    public int getTotalTimeCost(){
        int totalTimeCost=0;
        for(int i=0; i < transformations.size(); i++) {
            totalTimeCost=totalTimeCost+ timeCost.get(i);
        }
        return totalTimeCost;
    }

}
