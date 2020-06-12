import java.util.Arrays;
import java.util.Comparator;

public class Node{

    private Node parentNode;
    private int nodeId;
    private int[] position;
    private int cost;
    private int heuristicValue;
    private int depth;


    public Node(Node parentNode, int nodeId, int[] position, int cost, int heuristicValue, int depth) {
        this.parentNode = parentNode;
        this.nodeId= nodeId;
        this.position = position;
        this.cost = cost;
        this.heuristicValue=heuristicValue;
        this.depth = depth;
    }

    public String toString() {
        String nodeString = "NodeId: "+ nodeId + " w/ position ["+getPosition()[0]+","+getPosition()[1]+"] and HeuristicValue "+getHeuristicValue()+".";
        return nodeString;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public Node getParentNode(){
        return this.parentNode;
    }

    public int[] getPosition() {
        return this.position;
    }

    public int getCost(){
        return this.cost;
    }

    public int getHeuristicValue(){
        return this.heuristicValue;
    }

    public int getDepth(){
        return this.depth;
    }

    public int getAStarValue(){
        return this.heuristicValue + this.cost;
    }

}

class NodeComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2) {
        return (n1.getAStarValue() - n2.getAStarValue());
    }
}


