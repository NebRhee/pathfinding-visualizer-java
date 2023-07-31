import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class VisualPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
    int maxRow;
    int maxCol;
    int nodeSize;
    int screenWidth;
    int screenHeight;

    private int COST_MULTIPLIER = 10;

    private boolean addingWalls;
    private boolean deletingWalls;
    private boolean addingStartNode;
    private boolean addingGoalNode;
    private boolean animationRunning;

//    private int iteration = 0;

    private boolean isSmall;
    private boolean isMedium;
    private boolean isLarge;

    private boolean isAStar;
    private boolean isBFS;

    // Timer
    private int animationDelay = 30;
    Timer timer;
    private boolean pathFound;

    // 2D Array of Nodes
    private Node[][] grid;

    private Node startNode;
    private Node goalNode;

    Set<Node> visited;
    PriorityQueue<Node> openPriorityQueue;
    Queue<Node> openQueue;
    Stack<Node> openStack;


    // Colors
    Color lineColor = Color.black;
    Color startColor = Color.blue;
    Color goalColor = Color.red;
    Color wallColor = new Color(0, 0, 0);
    Color visitedColor = new Color(33, 85, 228);
    Color pathColor = new Color(244, 255, 87);
    Color neighborColor = new Color(3, 244, 255);

    public VisualPanel() {
        isMedium = true;
        isSmall = false;
        if (isSmall) {
            maxRow = 15;
            maxCol = 21;
            nodeSize = 64;
        } else if (isMedium) {
            maxRow = 30;
            maxCol = 42;
            nodeSize = 32;
        } else {
            maxRow = 60;
            maxCol = 84;
            nodeSize = 16;
        }

        screenWidth = nodeSize * maxCol;
        screenHeight = nodeSize * maxRow;

        // Set dimensions and layout
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocus();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        timer = new Timer(animationDelay, this);
        openPriorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getFCost).thenComparingInt(Node::getHCost).thenComparingInt(Node::getGCost));
        openQueue = new LinkedList<>();
        visited = new HashSet<>();
        openStack = new Stack<>();

        isAStar = true;
        isBFS = false;

        initializeGrid();
        initializeValues();
    }

    private void initializeGrid() {

        grid = new Node[maxRow][maxCol];
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                grid[row][col] = new Node(row, col);
            }
        }
    }

    private void initializeValues() {
        animationRunning = false;
        pathFound = false;
        openPriorityQueue.clear();
        openQueue.clear();
        openStack.clear();
        visited.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Clear panel
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw each node in the grid
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Node node = grid[row][col];
                int x = col * nodeSize;
                int y = row * nodeSize;

                // Draw the node based on its state
                if (node.isStartNode()) {
                    g.setColor(startColor);
                    g.fillRect(x, y, nodeSize, nodeSize);
                    node.setWall(false);
                } else if (node.isGoalNode()) {
                    g.setColor(goalColor);
                    g.fillRect(x, y, nodeSize, nodeSize);
                    node.setWall(false);
                } else if (node.isWall()) {
                    g.setColor(wallColor);
                    g.fillRect(x, y, nodeSize, nodeSize);
                } else if (node.isNeighbor()) {
                    g.setColor(neighborColor);
                    g.fillRect(x, y, nodeSize, nodeSize);
                } else if (node.isVisited() && !node.isPath()) {
                    g.setColor(visitedColor);
                    g.fillRect(x, y, nodeSize, nodeSize);
                } else if (node.isPath()) {
                    g.setColor(pathColor);
                    g.fillRect(x, y, nodeSize, nodeSize);
                }
                // Draw Grid Lines
                g.setColor(lineColor);
                g.drawRect(x, y, nodeSize, nodeSize);

            }
        }
    }

    private void editWall(int x, int y) {
        int row = y / nodeSize;
        int col = x / nodeSize;

        if (row >= 0 && row < maxRow && col >= 0 && col < maxCol) {
            Node node = grid[row][col];

            if (addingWalls) {
                node.setWall(true);
            } else if (deletingWalls) {
                node.setWall(false);
            }

            repaint();
        }
    }

    // Resets the entire grid to its initial state
    public void resetGrid() {
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                grid[i][j].reset();
            }
        }
        startNode = null;
        goalNode = null;
        initializeValues();
        repaint();
    }

    // Visualize Algorithms
    public void runAnimation() {
        animationRunning = true;
        timer.start();
        if (startNode == null || goalNode == null) {
            animationRunning = false;
            return;
        }
        findPath();
        stopAnimation();
    }

    public void stopAnimation() {
        animationRunning = false;
    }

    /*
        ALGORITHMS
    */

    private void findPath() {
        startNode.setGCost(0);
        startNode.setHCost(calculateHCost(startNode));
        startNode.calculateFCost();

        openPriorityQueue.add(startNode);
        openQueue.add(startNode);
        openStack.push(startNode);

        timer.start();
    }

    // A Star Algorithm
    private void findPathIterationAStar() {
        if (!openPriorityQueue.isEmpty()) {
            Node current = openPriorityQueue.poll();

            if (current == goalNode) {
                System.out.println("path found");
                pathFound = true;
                drawPath();
                return;
            }

            visited.add(current);
            current.setVisited(true);

            List<Node> neighbors = getNeighbors(current);

            for (Node neighbor : neighbors) {
                if (visited.contains(neighbor) || neighbor.isWall()) {
                    continue;
                }

                int newGCost = current.getGCost() + 1;
                if (newGCost < neighbor.getGCost() || !openPriorityQueue.contains(neighbor)) {
                    neighbor.setGCost(newGCost);
                    neighbor.setHCost(calculateHCost(neighbor));
                    neighbor.calculateFCost();
                    neighbor.setParent(current);
                    if (!openPriorityQueue.contains(neighbor)) {
                        openPriorityQueue.add(neighbor);
                        neighbor.setNeighbor(true);
                    }
                }
            }
        }
    }


    // Breadth First Search Algorithm
    private void findPathIterationBFS() {
        if (!openQueue.isEmpty()) {
            Node current = openQueue.poll();

            if (current == goalNode) {
                System.out.println("path found");
                pathFound = true;
                drawPath();
                return;
            }

            visited.add(current);
            current.setVisited(true);

            List<Node> neighbors = getNeighbors(current);

            for (Node neighbor : neighbors) {
                if (visited.contains(neighbor) || neighbor.isWall()) {
                    continue;
                }
                openQueue.add(neighbor);
                neighbor.setParent(current);
                visited.add(neighbor);
                neighbor.setNeighbor(true);
            }
        }
    }

    // Draws the path from the goal node
    private void drawPath() {
        Node current = goalNode;
        while (current != startNode) {
            current.setPath(true);
            current = current.getParent();
        }
        repaint();
    }

    // Get Neighbors for algorithm
    private List<Node> getNeighbors(Node current) {
        List<Node> neighbors = new ArrayList<>();

        int row = current.getRow();
        int col = current.getCol();


        if (row > 0) {
            neighbors.add(grid[row - 1][col]);
        }
        if (row < maxRow - 1) {
            neighbors.add(grid[row + 1][col]);
        }
        if (col > 0) {
            neighbors.add(grid[row][col - 1]);
        }
        if (col < maxCol - 1) {
            neighbors.add(grid[row][col + 1]);
        }
        return neighbors;
    }

    // Calculate HCost Euclidean Distance
    private int calculateHCost(Node currentNode) {
        int dy = Math.abs(currentNode.getRow() - goalNode.getRow());
        int dx = Math.abs(currentNode.getCol() - goalNode.getCol());
        int hCost = (int) (Math.sqrt(dx * dx + dy * dy) * COST_MULTIPLIER);
        return hCost;
    }


    // Animating Iteration using Timer
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isAStar) {
            findPathIterationAStar();
        } else if (isBFS) {
            findPathIterationBFS();
        }
        repaint();


        if (pathFound) {
            timer.stop();
        }
    }


    /*
            Key Listeners
    */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (animationRunning) {
            System.out.println("Animation Running");
        } else if (keyCode == KeyEvent.VK_S) {
            System.out.println("S Pressed");
            addingStartNode = true;
        } else if (keyCode == KeyEvent.VK_G) {
            System.out.println("G Pressed");
            addingGoalNode = true;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            System.out.println("Enter Pressed");
//            setCostsIfAllowed();
            runAnimation();
        } else if (keyCode == KeyEvent.VK_R) {
            System.out.println("R Pressed");
            resetGrid();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_S) {
            System.out.println("S Released");
            addingStartNode = false;
        } else if (keyCode == KeyEvent.VK_G) {
            System.out.println("G Released");
            addingGoalNode = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    /*
        Mouse Listeners
    */
    @Override
    public void mousePressed(MouseEvent e) {
        if (animationRunning) {
            System.out.println("Animation Running");
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (addingStartNode) {
                // "s" key is pressed
                // Create a start node at the clicked location
                int row = e.getY() / nodeSize;
                int col = e.getX() / nodeSize;
                if (startNode != null) {
                    startNode.setStartNode(false);
                }
                startNode = grid[row][col];
                startNode.setStartNode(true);
                repaint();
            } else if (addingGoalNode) {
                // 'g' key is pressed
                // Create an end node at the clicked location
                int row = e.getY() / nodeSize;
                int col = e.getX() / nodeSize;
                if (goalNode != null) {
                    goalNode.setGoalNode(false);
                }
                goalNode = grid[row][col];
                goalNode.setGoalNode(true);
                repaint();
            } else {
                addingWalls = true;
                editWall(e.getX(), e.getY());
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            deletingWalls = true;
            editWall(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        addingWalls = false;
        deletingWalls = false;
//        setCostsIfAllowed();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (animationRunning) {
            System.out.println("Animation Running");
        } else {
            editWall(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    /*
        Set grid side:
        1 - Small
        2 - Medium
        3 - Large
     */
    public void setGridSize(int size) {
        resetGrid();
        if (size == 1) { // Small
            isSmall = true;
            isMedium = false;
            isLarge = false;

            maxRow = 15;
            maxCol = 21;
            nodeSize = 64;

        } else if (size == 2) { // Medium Default
            isSmall = false;
            isMedium = true;
            isLarge = false;

            maxRow = 30;
            maxCol = 42;
            nodeSize = 32;
        } else { // Large
            isSmall = false;
            isMedium = false;
            isLarge = true;

            maxRow = 60;
            maxCol = 84;
            nodeSize = 16;
        }
        resizeGrid();

    }

    private void resizeGrid() {
        initializeGrid();
        repaint();
    }

    public void setGridSpeed(int speed) {
        int complement = 100 - speed;
        double percentage = (double) complement / 100;
        animationDelay = (int) (1 - (1 - 180) * percentage);
        timer.setDelay(animationDelay);
        System.out.println(animationDelay + " ms");
    }

    public void changeAlgorithm(String algorithm) {
        if (algorithm.equals("A Star")) {
            isAStar = true;
            isBFS = false;
        } else if (algorithm.equals("BFS")) {
            isAStar = false;
            isBFS = true;
        }
        resetGrid();
    }
}


