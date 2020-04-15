import java.util.*;

public class Path_Logic {

    public int nodeIdentification=0;
    public int[] start;
    public int[] goal;
    private Stack<Node> path;
    private ArrayList<Node> usedNodes;
    private PriorityQueue<Node> unusedNodes;
    public Node solutionFound;
    private SFS floor;


    /**
     * Constructor for class Path_logic.
     * @param start initial position, where start[0] is in the Y axis and start[1] is in the X axis.
     * @param goal final position, where goal[0] is in the Y axis and goal[1] is in the X axis.
     * */
    public Path_Logic(int[] start, int[] goal){
        this.start= start;
        this.goal= goal;
        this.path= new Stack<>();
        this.usedNodes= new ArrayList<>();
        this.unusedNodes = new PriorityQueue<>(new NodeComparator());
        this.floor = new SFS();
    }


    /**
     * Starts the algorithm in order to find the shortest path to the solution.
     *
     * @return  <code>true</code> if solution found.
     */
    public boolean findPath() {
        if(floor.sfsCells[start[0]][start[1]] == null) {
            System.out.println("Invalid starting position.");
            return false;
        }
        else if(floor.sfsCells[goal[0]][goal[1]] == null){
            System.out.println("Invalid goal position.");
            return false;
        }
        else{
            System.out.println("Start: " + this.start[0] + "," + this.start[1]+";");
            System.out.println("Goal: " + this.goal[0] + "," + this.goal[1]+";");
            System.out.println(" * * * *");
        }

        Node parentNode= new Node(null, this.nodeIdentification, start, 0,manhattanDistance(start[0],start[1]), 0); this.nodeIdentification++;
        unusedNodes.add(parentNode);
        if(solve()) {
            System.out.println("Shortest path found.");
            return true;
        }
        else {
            System.out.println("No path found.");
            return false;
        }
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

        for(int i=0; i < 4; i++) {
            int [] childPosition= new int[2];
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
                if (inBoundsOfArray(childPosition[0], childPosition[1]) && !Arrays.equals(childPosition, parentNode.getParentNode().getPosition()) && (floor.sfsCells[childPosition[0]][childPosition[1]] != null)) {
                    Node childNode= new Node(parentNode, this.nodeIdentification, childPosition, parentNode.getCost()+1, manhattanDistance(childPosition[0], childPosition[1]), parentNode.getDepth() + 1); this.nodeIdentification++;
                    if (!unusedNodes.contains(childNode) && !usedNodes.contains(childNode)) {
                        unusedNodes.add(childNode);
                        System.out.println("  [ADDED]  " + childNode);
                    }
                }
            }
            else {
                if (inBoundsOfArray(childPosition[0], childPosition[1]) && (floor.sfsCells[childPosition[0]][childPosition[1]] != null)) {
                    Node childNode= new Node(parentNode, this.nodeIdentification, childPosition, parentNode.getCost()+1, manhattanDistance(childPosition[0], childPosition[1]), parentNode.getDepth() + 1); this.nodeIdentification++;
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
     * Adds the positions of the found path to a LIFO stack.
     */
    private void addPathToStack() {
        Node node = solutionFound;
        while(node != null){
            path.push(node);
            node = node.getParentNode();
        }
    }


    /**
     * Determines if the indices are in the array boundaries.
     * @param childPositionY position in the Y axis.
     * @param childPositionX position in the X axis.
     * @return  <code>true</code> if positions are valid, <code>false</code> otherwise.
     */
    public boolean inBoundsOfArray(int childPositionY, int childPositionX) {
        return ((childPositionX >= 0) && (childPositionX < floor.sfsCells[0].length) && (childPositionY >= 0) && (childPositionY < floor.sfsCells.length));
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


    /**
     * Clones a 1D Array.
     * @param a 1D Array to clone.
     * @return b , the cloned array.
     */
    private int[] cloneArray(int[] a) {
        int [] b = new int[a.length];
        for(int i=0; i < a.length; i++){
            b[i]=a[i];
        }
        return b;
    }


    /**
     * Gets the LIFO stack.
     * @return Stack, with ordered positions of the path found.
     */
    public Stack<Node> getPath() {
        return this.path;
    }

    private boolean checkRulesToCreateNode(int [] childPosition, Node parentNode){
        //Check if position is in bounds of Array;
        boolean ArrayInBounds = inBoundsOfArray(childPosition[0], childPosition[1]);
        //Check if it's not going back from where it came
        boolean goingBack=Arrays.equals(childPosition, parentNode.getParentNode().getPosition());
        //Check if it's not a null path
        boolean validCell=(floor.sfsCells[childPosition[0]][childPosition[1]] != null);

        if(parentNode.getParentNode () != null) {
            if (inBoundsOfArray(childPosition[0], childPosition[1]) && !Arrays.equals(childPosition, parentNode.getParentNode().getPosition()) && (floor.sfsCells[childPosition[0]][childPosition[1]] != null)) {
                Node childNode= new Node(parentNode, this.nodeIdentification, childPosition, parentNode.getCost()+1, manhattanDistance(childPosition[0], childPosition[1]), parentNode.getDepth() + 1); this.nodeIdentification++;
                if (!unusedNodes.contains(childNode) && !usedNodes.contains(childNode)) {
                    unusedNodes.add(childNode);
                    System.out.println("  [ADDED]  " + childNode);
                }
            }
        }
        else {
            if (inBoundsOfArray(childPosition[0], childPosition[1]) && (floor.sfsCells[childPosition[0]][childPosition[1]] != null)) {
                Node childNode= new Node(parentNode, this.nodeIdentification, childPosition, parentNode.getCost()+1, manhattanDistance(childPosition[0], childPosition[1]), parentNode.getDepth() + 1); this.nodeIdentification++;
                if (!unusedNodes.contains(childNode) && !usedNodes.contains(childNode)) {
                    unusedNodes.add(childNode);
                    System.out.println("  [ADDED]  " + childNode);
                }
            }
        }
    return true;
    }
}