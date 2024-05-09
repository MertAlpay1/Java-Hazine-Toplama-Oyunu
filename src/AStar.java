
import java.awt.Point;
import java.util.*;


public class AStar {
    
    private int[][] map;
    private Node[][] nodes;
    private PriorityQueue<Node> openList;
    private Set<Node> closedList;
    private Node startNode;
    private Node targetNode;
    
    
    public AStar(int [][]map){
        this.map=map;
        this.nodes=new Node[map.length][map[0].length];
        this.openList= new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        this.closedList = new HashSet<>();
        initializeNodes();
    }
     
    private void initializeNodes(){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                nodes[i][j]=new Node(i,j);
            }
        }
    }
    
    public boolean isReachable(int startX,int startY,int targetX,int targetY){
        
        startNode = nodes[startX][startY];
        targetNode=nodes[targetX][targetY];
        
        openList.clear();
        closedList.clear();
        
        openList.add(startNode);
        
        while(!openList.isEmpty()){
            Node currentNode=openList.poll();
            
        
         if (currentNode == targetNode) {
                return true;
            }
         
         closedList.add(currentNode);
         List<Node> neighbors=getNeighbors(currentNode);
         
         for(Node neighbor:neighbors){
             if (closedList.contains(neighbor)) {
                    continue;
                }
             int tentativeGScore = currentNode.getG() + 1;
             if (tentativeGScore < neighbor.getG()) {
                    neighbor.setParent(currentNode);
                    neighbor.setG(tentativeGScore);
                    neighbor.setH(heuristic(neighbor, targetNode));
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
             
         }
        }
        return false;
    }
   
    public List<Point> calculateShortestPath(int x,int y, Point target) {
        List<Point> path = new ArrayList<>();
        
        

                
        openList.clear();
        closedList.clear();

        startNode = nodes[x][y];
        targetNode = nodes[target.x][target.y];

        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode == targetNode) {
                return constructPath(currentNode);
            }

            closedList.add(currentNode);
            List<Node> neighbors = getNeighbors(currentNode);

            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor)) {
                    continue;
                }
                int tentativeGScore = currentNode.getG() + 1;
                if (tentativeGScore < neighbor.getG()) {
                    neighbor.setParent(currentNode);
                    neighbor.setG(tentativeGScore);
                    neighbor.setH(heuristic(neighbor, targetNode));
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

      
        return path;
    }

    private List<Point> constructPath(Node targetNode) {
        List<Point> path = new ArrayList<>();
        Node currentNode = targetNode;
        while (currentNode != null) {
            path.add(new Point(currentNode.getX(), currentNode.getY()));
            currentNode = currentNode.getParent();
        }
        Collections.reverse(path); 
        return path;
    }

    private List<Node>getNeighbors(Node node){
        List<Node>neighbors=new ArrayList<>();
        int x = node.getX();
        int y = node.getY();
        
        if (isValidCell(x - 1, y)) neighbors.add(nodes[x - 1][y]);
        if (isValidCell(x + 1, y)) neighbors.add(nodes[x + 1][y]);
        if (isValidCell(x, y - 1)) neighbors.add(nodes[x][y - 1]);
        if (isValidCell(x, y + 1)) neighbors.add(nodes[x][y + 1]);
        
        return neighbors;
    }
    
    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y] == 0;
    }
    private int heuristic(Node a, Node b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }
    
     private static class Node {
    
         private int x;
         private int y;
         private int g;
         private int h;
         private Node parent;
         
    public Node(int x,int y){
        this.x=x;
        this.y=y;
        this.g=Integer.MAX_VALUE;
        this.h=0;
        this.parent=null;
    }

        public int getX() {
            return x;
        }


        public int getY() {
            return y;
        }

        public int getF() {
            return g + h;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

  }
}
