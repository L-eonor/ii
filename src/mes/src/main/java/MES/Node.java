package MES;

import java.util.Comparator;

public class Node{

    private Node parentNode;
    private int nodeId;
    private int[] position;
    private float cost;
    private int heuristicValue;
    private int depth;


    public Node(Node parentNode, int nodeId, int[] position, float cost, int heuristicValue, int depth) {
        this.parentNode = parentNode;
        this.nodeId= nodeId;
        this.position = position;
        this.cost = cost;
        this.heuristicValue=heuristicValue;
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o){
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Node)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Node c = (Node) o;

        // Compare the data members and return accordingly
        return (this.position[0] == c.position[0]) && (this.position[1] == c.position[1]);
    }


    public String toString() {
        String nodeString = "NodeId: "+ nodeId + " w/ position ["+getPosition()[0]+","+getPosition()[1]+"] and AStarValue "+getAStarValue()+" ("+getHeuristicValue()+" + "+getCost()+").";
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

    public float getCost(){
        return this.cost;
    }

    public int getHeuristicValue(){
        return this.heuristicValue;
    }

    public int getDepth(){
        return this.depth;
    }

    public float getAStarValue(){
        return this.heuristicValue + this.cost;
    }

}

class NodeComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2) {
        if(n1.getAStarValue() < n2.getAStarValue()) return -1;
        else if (n1.getAStarValue() > n2.getAStarValue()) return 1;
        else return 0;
    }
}


