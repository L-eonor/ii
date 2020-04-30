package MES;

import java.util.*;

public class Path_Logic {

    public int nodeIdentification = 0;
    public int[] start;
    public int[] goal;
    private Stack<Node> path;
    private ArrayList<Node> usedNodes;
    private PriorityQueue<Node> unusedNodes;
    public Node solutionFound;
    private SFS floor = MES.Main.floor;


    /**
     * Constructor for class Path_logic.
     *
     * @param start initial position, where start[0] is in the Y axis and start[1] is in the X axis.
     * @param goal  final position, where goal[0] is in the Y axis and goal[1] is in the X axis.
     */
    public Path_Logic(int[] start, int[] goal) {
        // Reverse position array into Java matrix style -> y first, x second.
        this.start= reverseArray(start);
        this.goal= reverseArray(goal);

        this.path = new Stack<>();
        this.usedNodes = new ArrayList<>();
        this.unusedNodes = new PriorityQueue<>(new NodeComparator());
    }


    /**
     * Starts the algorithm in order to find the shortest path to the solution.
     *
     * @return <code>true</code> if solution found.
     */
    public boolean findPath() {
        if (floor.sfsCells[start[0]][start[1]] == null) {
            System.out.println("Invalid starting position.");
            return false;
        } else if (floor.sfsCells[goal[0]][goal[1]] == null) {
            System.out.println("Invalid goal position.");
            return false;
        } else {
            System.out.println("Start: " + this.start[1] + "," + this.start[0] + ";");
            System.out.println("Goal: " + this.goal[1] + "," + this.goal[0] + ";");
            System.out.println();
        }

        Node parentNode = new Node(null, this.nodeIdentification, start, 0, manhattanDistance(start[0], start[1]), 0);
        this.nodeIdentification++;
        unusedNodes.add(parentNode);
        if (solve()) {
            System.out.println("Shortest path found.");
            return true;
        } else {
            System.out.println("No path found.");
            return false;
        }
    }


    /**
     * Solves the first node in the priority queue. If solution is not yet found it
     * creates the node children in order to find one.
     *
     * @return <code>true</code> if solution found.
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
        //System.out.println("  [POLLED]  " + node);


        if (node == null) {
            System.out.println("Error: empty queue. No solution found.");
            return false;
        }

        if (!checkGoal(node)) {
            //System.out.println("Creating children for Node: " + node.getNodeId() + " Position: [" + node.getPosition()[0] + "," + node.getPosition()[1] + "]     . . .");
            addChildren(node);
            usedNodes.add(node);
            return solve();
        } else {
            usedNodes.add(node);
            solutionFound = node;
            addPathToStack();
            return true;
        }

    }


    /**
     * Check if solution was found in the specified node.
     *
     * @param node node to check
     * @return <code>true</code> if solution found, <code>false</code> otherwise.
     */
    private boolean checkGoal(Node node) {
        return ((node.getPosition()[0] == goal[0]) && (node.getPosition()[1] == goal[1]));
    }


    /**
     * Creates children nodes and adds them to the priority queue.
     *
     * @param parentNode parent node in which the functions will create the children from.
     */
    private void addChildren(Node parentNode) {
        int positionY = parentNode.getPosition()[0];
        int positionX = parentNode.getPosition()[1];

        for (int i = 0; i < 4; i++) {
            int[] childPosition = new int[2];
            switch (i) {
                case 0: //cima
                    childPosition[0] = positionY + 1;
                    childPosition[1] = positionX;
                    break;
                case 1: //baixo
                    childPosition[0] = positionY - 1;
                    childPosition[1] = positionX;
                    break;
                case 2: //direita
                    childPosition[0] = positionY;
                    childPosition[1] = positionX + 1;
                    break;
                case 3: //esquerda
                    childPosition[0] = positionY;
                    childPosition[1] = positionX - 1;
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }

            if (checkGeneralRules(parentNode, childPosition)) {
                //Regras de movimento de tapete para tapete
                switch (floor.sfsCells[parentNode.getPosition()[0]][parentNode.getPosition()[1]].getName()){
                    case "Machine":
                        if(floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Rotator")) {
                            createChildNode(parentNode, childPosition);
                        }
                        break;

                    case "Conveyor":
                        if(floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Rotator") || floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Pusher") ||
                                floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("WarehouseIn") || floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("WarehouseOut")) {
                            createChildNode(parentNode, childPosition);
                        }
                        break;

                    case "Rotator":
                        if(floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Conveyor") || floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Machine") || floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Rotator")) {
                            createChildNode(parentNode, childPosition);
                        }
                        break;
                    case "Pusher":
                        if(floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Conveyor") || floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Slider")) {
                            createChildNode(parentNode, childPosition);
                        }
                        break;
                    case "Slider":
                        if(floor.sfsCells[childPosition[0]][childPosition[1]].getName().equals("Pusher")) {
                            createChildNode(parentNode, childPosition);
                        }
                        break;
                    default:
                        break;

                }
            }
        }
        //System.out.println("-  -  -");
    }

    /**
     * Returns cost of turn + weight of Child node = total cost
     *
     * @param parentNode parent node in which the functions will create the children from.
     * @param childPosition position vector of the new child.
     * @return total cost.
     */
    private int getCost(Node parentNode, int[] childPosition) {
        // -> 1, caminho permanece na mesma direção;
        //-> 2, caminho muda direção;
        //-> adiciona peso dos nós criados

        if(parentNode.getParentNode() == null) return 1;

        boolean yChange1 = (parentNode.getParentNode().getPosition()[0] != parentNode.getPosition()[0]);
        boolean xChange1 = (parentNode.getParentNode().getPosition()[1] != parentNode.getPosition()[1]);
        boolean yChange2 = (parentNode.getPosition()[0] != childPosition[0]);
        boolean xChange2 = (parentNode.getPosition()[1] != childPosition[1]);

        boolean yChange = (yChange1 || yChange2);
        boolean xChange = (xChange1 || xChange2);


        if(xChange && yChange) return 2 + floor.getCell(childPosition[0],childPosition[1]).getWeight();
        else return 1 + floor.getCell(childPosition[0],childPosition[1]).getWeight();
    }

    /**
     * Determines if the indices are in the array boundaries.
     *
     * @param parentNode parent MES.Node.
     * @param childPosition position vector of the new child.
     * @return <code>true</code> if rules are checked, <code>false</code> otherwise.
     */
    private boolean checkGeneralRules(Node parentNode, int [] childPosition) {
        boolean inArray = inBoundsOfArray(childPosition[0], childPosition[1]);

        boolean goingBack;
        if (parentNode.getParentNode() == null) goingBack=false;
        else goingBack= Arrays.equals(childPosition, parentNode.getParentNode().getPosition());

        boolean existsCell;
        if(inArray) existsCell= floor.sfsCells[childPosition[0]][childPosition[1]] != null;
        else existsCell=false;

        return inArray && !goingBack && existsCell;
    }

    /**
     * Determines if the indices are in the array boundaries.
     *
     * @param parentNode MES.Node to create child from.
     * @param childPosition position vector of the new child.
     */
    private void createChildNode(Node parentNode, int [] childPosition) {
        int cost=getCost(parentNode, childPosition);
        Node childNode = new Node(parentNode, this.nodeIdentification, childPosition, parentNode.getCost() + cost, manhattanDistance(childPosition[0], childPosition[1]), parentNode.getDepth() + 1);
        this.nodeIdentification++;
        if (!unusedNodes.contains(childNode) && !usedNodes.contains(childNode)) {
            unusedNodes.add(childNode);
            //System.out.println("  [ADDED]  " + childNode);
        }
    }

    /**
     * Adds the positions of the found path to a LIFO stack.
     */
    private void addPathToStack() {
        Node node = solutionFound;
        while (node != null) {
            floor.getCell(node.getPosition()[0], node.getPosition()[1]).addWeight();
            path.push(node);
            node = node.getParentNode();
        }
    }


    /**
     * Determines if the indices are in the array boundaries.
     *
     * @param childPositionY position in the Y axis.
     * @param childPositionX position in the X axis.
     * @return <code>true</code> if positions are valid, <code>false</code> otherwise.
     */
    public boolean inBoundsOfArray(int childPositionY, int childPositionX) {
        return ((childPositionX >= 0) && (childPositionX < floor.sfsCells[0].length) && (childPositionY >= 0) && (childPositionY < floor.sfsCells.length));
    }


    /**
     * Determines the Manhattan distance between the positions passed and the goal.
     *
     * @param positionY position in the Y axis.
     * @param positionX position in the X axis.
     * @return Manhattan distance value.
     */
    private int manhattanDistance(int positionY, int positionX) {
        int distanceY = Math.abs(goal[0] - positionY);
        int distanceX = Math.abs(goal[1] - positionX);

        return distanceY + distanceX;
    }


    /**
     * Clones a 1D Array.
     *
     * @param a 1D Array to clone.
     * @return b , the cloned array.
     */
    private int[] cloneArray(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i];
        }
        return b;
    }

    /**
     * Reverses a 1D Array.
     *
     * @param a 1D Array to reverse.
     * @return b , the reversed array.
     */
    private int[] reverseArray(int[] a){
        int n = a.length;
        int[] b = new int[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }


    /**
     * Gets the LIFO stack.
     *
     * @return Stack, with ordered positions of the path found.
     */
    public Stack<Node> getPath() {
        return this.path;
    }

    public String getStringPath() {

        String pathString = "";
        if(findPath()){
            getPath().pop(); //remove first position of the path because we don't want to send it
            int sizePath = getPath().size();
            for (int i = 0; i < sizePath; i++) {
                Node nodePopped = getPath().pop();
                pathString = pathString + Integer.toString(nodePopped.getPosition()[1]) + Integer.toString(nodePopped.getPosition()[0]);
                if(this.floor.getCell(nodePopped.getPosition()[0], nodePopped.getPosition()[1]).name.equals("Machine") && ( i>1 && i<(sizePath-1))){
                    pathString=pathString+"011";
                }
            }
            return pathString;
        }
        else return "-1";
    }

}