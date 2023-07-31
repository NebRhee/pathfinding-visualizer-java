public class Node {
    private int row;
    private int col;
    private boolean isWall;
    private boolean visited;
    private boolean path;
    private boolean startNode;
    private boolean goalNode;
    private boolean neighbor;
    private int gCost;
    private int hCost;
    private int fCost;
    private Node parent;
//    private double distance;


    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        this.isWall = false;
        this.visited = false;
        this.path = false;
        this.startNode = false;
        this.goalNode = false;
        this.gCost = 0;
        this.hCost = 0;
        this.fCost = 0;
//        this.distance = Double.POSITIVE_INFINITY;
    }

    // Getters and Setters
//    public double getDistance() {
//        return distance;
//    }
//
//    public void setDistance(double distance) {
//        this.distance = distance;
//    }

    public boolean isNeighbor() {
        return neighbor;
    }

    public void setNeighbor(boolean neighbor) {
        this.neighbor = neighbor;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        if (visited) {
            neighbor = false;
        }
        this.visited = visited;
    }

    public boolean isPath() {
        return path;
    }

    public void setPath(boolean path) {
        this.path = path;
    }

    public boolean isStartNode() {
        return startNode;
    }

    public void setStartNode(boolean startNode) {
        this.startNode = startNode;
    }

    public boolean isGoalNode() {
        return goalNode;
    }

    public void setGoalNode(boolean goalNode) {
        this.goalNode = goalNode;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void reset() {
        this.isWall = false;
        this.visited = false;
        this.path = false;
        this.startNode = false;
        this.goalNode = false;
        this.neighbor = false;
        this.gCost = 0;
        this.hCost = 0;
        this.fCost = 0;

    }

    public void calculateFCost() {
        int fCost = hCost + gCost;
        setFCost(fCost);
    }
}
