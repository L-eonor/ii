import java.util.*;

public class Path_Logic {

    public int nodeIdentification=0;
    public int[] start;
    public int[] goal;
    private Stack<Integer> path;
    private ArrayList<Node> usedNodes;
    private PriorityQueue<Node> unusedNodes;
    public Node solutionFound;
    private int[][] sfs = {
            { 0,  0, 0,  0, 0,  0,  0,  0,  0},
            {-1, -1, 0, -1, 0, -1,  0,  0, -1},
            {-1,  1, 0,  1, 0,  1,  0,  2, -1},
            {-1,  1, 0,  1, 0,  1,  0,  2,  0},
            {-1,  1, 0,  1, 0,  1,  0,  2,  0},
            {-1, -1, 0, -1, 0, -1,  0,  0, -1},
            { 0,  0, 0,  0, 0,  0,  0,  0,  0}};

    /*public int[][] sfs = {
            { 0,  -1,   0,  0},
            { 0,   0,   0,  0},
            { 0,   0,  -1,  0},
            { 0,   -1,  0,  0}};

    * Conveyor - 0;
    * Rotator - 0;
    * Máquina - 1;
    * Pusher - 2;
    */

    /**
     * Constructor for class Path_logic.
     * @param start initial position, where start[0] is in the Y axis and start[1] is in the X axis.
     * @param goal final position, where goal[0] is in the Y axis and goal[1] is in the X axis.
     * */
    public Path_Logic(int[] start, int[] goal){
        this.start= start;
        this.goal= goal;
        this.path= new Stack<Integer>();
        this.usedNodes= new ArrayList<>();
        this.unusedNodes = new PriorityQueue<>(new NodeComparator());


        System.out.println("Start: " + this.start[0] + "," + this.start[1]+";");
        System.out.println("Goal: " + this.goal[0] + "," + this.goal[1]+";");
        System.out.println(" * * * *");
    }

    /**
     * Starts the algorithm in order to find the shortest path to the solution.
     */
    public void findPath() {
        Node parentNode= new Node(null, this.nodeIdentification, start, 0,manhattanDistance(start[0],start[1]), 0); this.nodeIdentification++;
        unusedNodes.add(parentNode);
        if(solve()) System.out.println("Shortest path found.");
        else System.out.println("No path found.");
    }

    /**
     * Solves the first node in the priority queue. If solution is not yet found it
     * creates the node children in order to find one.
     *
     * @return  <code>true</code> if solution found.
     */
    private boolean solve() {

        //Uncomment in case you want to display the priority queue values.
        /*
        Iterator value = unusedNodes.iterator();
        System.out.println("The iterator values are: ");
        while (value.hasNext()) {
            System.out.println(value.next());
        }
        */

        Node node = unusedNodes.poll();
        System.out.println("  [POLLED]  " + node);




        if(node == null) {
            System.out.println("Error: empty queue. No solution found.");
            return false;
        }

        if (!checkGoal(node)) {
            System.out.println("Creating children for Node: " + node.getNodeId() + " Position: [" + node.getPosition()[0] + "," + node.getPosition()[1] + "]     . . .");
            addChildren(node);
            usedNodes.add(node);
            return solve();
        }
        else {
            usedNodes.add(node);
            solutionFound = node;
            addPathToStack();
            return true;
        }

    }

    private void addPathToStack() {
        Node node = solutionFound;
        while(node != null){
            path.push(node.getPosition()[1]);
            path.push(node.getPosition()[0]);
            node = node.getParentNode();
        }
    }

    /**
     * Check if solution was found in the specified node.
     *
     * @param  node  node to check
     * @return  <code>true</code> if solution found, <code>false</code> otherwise.
     */
    private boolean checkGoal(Node node) {
        return ((node.getPosition()[0] == goal[0]) && (node.getPosition()[1]==goal[1]));
    }

    /**
     * Creates children nodes and adds them to the priority queue.
     * @param parentNode parent node in which the functions will create the children from.
     */
    private void addChildren(Node parentNode) {
        int positionY=parentNode.getPosition()[0];
        int positionX=parentNode.getPosition()[1];
        int [] childPosition= new int[2];

        for(int i=0; i < 4; i++) {
            switch (i) {
                case 0:
                    childPosition[0]= positionY;
                    childPosition[1]= positionX+1;
                    break;

                case 1:
                    childPosition[0]= positionY-1;
                    childPosition[1]= positionX;
                    break;

                case 2:
                    childPosition[0]= positionY;
                    childPosition[1]= positionX-1;
                    break;

                case 3:
                    childPosition[0]= positionY+1;
                    childPosition[1]= positionX;
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }


            if(parentNode.getParentNode () != null) {
                if (inBoundsOfArray(childPosition[0], childPosition[1]) && !Arrays.equals(childPosition, parentNode.getParentNode().getPosition()) && (sfs[childPosition[0]][childPosition[1]] == 0)) {
                    int[] childPositionAux=cloneArray(childPosition);
                    Node childNode= new Node(parentNode, this.nodeIdentification, childPositionAux, parentNode.getCost()+1, manhattanDistance(childPosition[0], childPosition[1]), parentNode.getDepth() + 1); this.nodeIdentification++;
                    if (!unusedNodes.contains(childNode) && !usedNodes.contains(childNode)) {
                        unusedNodes.add(childNode);
                        System.out.println("  [ADDED]  " + childNode);
                    }
                }
            }
            else {
                if (inBoundsOfArray(childPosition[0], childPosition[1]) && (sfs[childPosition[0]][childPosition[1]] == 0)) {
                    int[] childPositionAux=cloneArray(childPosition);
                    Node childNode= new Node(parentNode, this.nodeIdentification, childPositionAux, parentNode.getCost()+1, manhattanDistance(childPosition[0], childPosition[1]), parentNode.getDepth() + 1); this.nodeIdentification++;
                    if (!unusedNodes.contains(childNode) && !usedNodes.contains(childNode)) {
                        unusedNodes.add(childNode);
                        System.out.println("  [ADDED]  " + childNode);
                    }
                }
            }

        }
        System.out.println("-  -  -");
    }

    /**
     * Determines if the indices are in the array boundaries.
     * @param childPositionY position in the Y axis.
     * @param childPositionX position in the X axis.
     * @return  <code>true</code> if positions are valid, <code>false</code> otherwise.
     */
    public boolean inBoundsOfArray(int childPositionY, int childPositionX) {
        return ((childPositionX >= 0) && (childPositionX < sfs[0].length) && (childPositionY >= 0) && (childPositionY < sfs.length));
    }

    /**
     * Determines the Manhattan distance between the positions passed and the goal.
     * @param positionY position in the Y axis.
     * @param positionX position in the X axis.
     * @return Manhattan distance value.
     */
    private int manhattanDistance(int positionY, int positionX) {
        int distanceY = Math.abs(goal[0] - positionY);
        int distanceX = Math.abs(goal[1] - positionX);

        return distanceY+distanceX;
    }


    private int[] cloneArray(int[] a) {
        int [] b = new int[a.length];
        for(int i=0; i < a.length; i++){
            b[i]=a[i];
        }
        return b;
    }

    public Stack<Integer> getPath() {
        return this.path;
    }
}