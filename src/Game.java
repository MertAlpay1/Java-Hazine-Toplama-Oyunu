import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

public class Game extends JPanel {

    public static final int TILE_SIZE = 16;

    public int MAP_WIDTH = 0;
    public int MAP_HEIGHT = 0;
    public int MAP_SIZE_WIDTH = MAP_WIDTH / TILE_SIZE;
    public int MAP_SIZE_HEIGHT = MAP_HEIGHT / TILE_SIZE;
    private int[][] Map ;
    private int[][] Spawn;
    private int[][] Fog ;
    int winterSummerBorderX;
    static int steps=0;
    static int c=1;
    static boolean End=false;
    static boolean Start=false;
    
    private ArrayList<StaticObstacle> obstacles = new ArrayList<>();
    private ArrayList<DynamicObstacle> obstacles2=new ArrayList<>();
    private ArrayList<Chest> chests=new ArrayList<>();
    //Red Paths
    private ArrayList<Point> redBox=new ArrayList<>();
    //Green Paths
    private ArrayList<Point> greenBox=new ArrayList<>();
    private ArrayList<Point> chestlocations=new ArrayList<>();
    private List<Point> Paths= new ArrayList<>();
    Set<String> collectedChests = new HashSet<>();
    List<String> sortedChests = new ArrayList<>(collectedChests);
    private ArrayList<Message>messages=new ArrayList<>();
    
    private List<TileInfo> TileInfos = new ArrayList<>();

    private Random random = new Random();
    private Image grassImage;
    private Image snowImage;    
   
    private Character player=new Character("1","Vander",2,2,0,0);
    private App app=new App();
    
    
    public Game(int MAP_WIDTH,int MAP_HEIGHT) {
        setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));

        MAP_SIZE_WIDTH = MAP_WIDTH / TILE_SIZE;
        MAP_SIZE_HEIGHT = MAP_HEIGHT / TILE_SIZE;
        Map = new int[MAP_SIZE_WIDTH][MAP_SIZE_HEIGHT];
        Fog = new int[MAP_SIZE_WIDTH][MAP_SIZE_HEIGHT];
        Spawn= new int[MAP_SIZE_WIDTH][MAP_SIZE_HEIGHT];
        winterSummerBorderX = MAP_SIZE_WIDTH / 2;
        try {
            grassImage = ImageIO.read(new File("res/grass.png"));
            snowImage = ImageIO.read(new File("res/wintertile.png"));
            
           
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDynamicObstacles();
                repaint(); 
            }
        });
        
        
        timer.start(); 
    }

    public void generateNewMap(){
        generateObstacles(); 
        generateDynamicObstacles();
        randomspawn();
        generatechest();
        
    }
    public void startGame(){
        
        Start=true;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        
        
        if(MAP_SIZE_WIDTH<=100){
            drawMap(g);
            drawBoxes(g);
            drawObstacles(g);
            drawObstacles2(g);
            drawChest(g);
            drawCharacter(g);
            if(Start){
            generateFog(g);
            drawMessage(g);
            drawStepsToGoal(g);
            }
        }
        else {
            try {
            Image backgroundImage = ImageIO.read(new File("res/background.jpg"));
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
            
        }
            
           
    }

    private void drawCharacter(Graphics g){
         

        int characterX = player.getLocation().getX();
        int characterY = player.getLocation().getY();
        Image image=player.getImage();
        
        g.drawImage(image, characterX*TILE_SIZE, characterY*TILE_SIZE,TILE_SIZE,TILE_SIZE, null);
        Point point=new Point(characterX,characterY);
        
        if(Start){ 
        greenBox.add(point);   
        player.shortestPath(Paths);
        checkObstaclesInSight();
        if(!chests.isEmpty()){
        steps++;
        app.setStepsToGoal(steps);
        }
        }
        
    }
    private void drawStepsToGoal(Graphics g) {
    g.setColor(Color.BLUE);
    g.setFont(new Font("Arial", Font.BOLD, 15));
    g.drawString("Steps to Goal: " + app.getStepsToGoal(), 10, 30);
}
    
    private void drawMap(Graphics g) {
        

        for (int x = 0; x < MAP_SIZE_WIDTH; x++) {
            for (int y = 0; y < MAP_SIZE_HEIGHT; y++) {
                if (x < winterSummerBorderX) {
                    //winter
                    
                    
                    g.drawImage(snowImage, x * TILE_SIZE, y * TILE_SIZE,TILE_SIZE,TILE_SIZE, null);
                } else {
                    //summer
                    g.drawImage(grassImage, x * TILE_SIZE, y * TILE_SIZE,TILE_SIZE,TILE_SIZE, null);
                }
            }

        }   
    }
    private void drawObstacles(Graphics g) {
    for (StaticObstacle obstacle : obstacles) {
        int obstacleX = obstacle.getLocation().getX();
        int obstacleY = obstacle.getLocation().getY();
        Image obstacleImage = obstacle.getImage(); 
        int obstacleWidth = obstacle.getSize_x() * TILE_SIZE; 
        int obstacleHeight = obstacle.getSize_y() * TILE_SIZE; 
        g.drawImage(obstacleImage, obstacleX, obstacleY, obstacleWidth, obstacleHeight, null); 
    }
}
    private void drawObstacles2(Graphics g) {
    for (DynamicObstacle obstacle : obstacles2) {
        int obstacleX = obstacle.getLocation().getX();
        int obstacleY = obstacle.getLocation().getY();
        Image obstacleImage = obstacle.getImage();
        int obstacleWidth = obstacle.getSize_x() * TILE_SIZE;
        int obstacleHeight = obstacle.getSize_y() * TILE_SIZE;
        g.drawImage(obstacleImage, obstacleX, obstacleY, obstacleWidth, obstacleHeight, null);
        Point point = new Point(obstacleX, obstacleY);
        if(!redBox.contains(point)){
            redBox.add(point);
        }
    }
}
    
      private void drawBoxes(Graphics g){
           g.setColor(Color.RED);
           for (Point point : redBox) {
        int x = point.x ;
        int y = point.y ;
       g.fillRect(x, y, 2*TILE_SIZE, 2*TILE_SIZE);
    }
           
          g.setColor(Color.GREEN);
    for (Point point : greenBox) {
        int x = point.x;
        int y = point.y;
        g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
           
          
      }
private void moveDynamicObstacles() {
    for (DynamicObstacle obstacle : obstacles2) {
        obstacle.move();
    }
}
private void drawChest(Graphics g){
    int characterX = player.getLocation().getX()*TILE_SIZE;
    int characterY = player.getLocation().getY()*TILE_SIZE;

    
    

    for (int i = 0; i < chests.size(); i++) {
        Chest chest = chests.get(i);
        int chestX = chest.getLocation().getX();
        int chestY = chest.getLocation().getY();
        Image chestImage = chest.getImage();
        int chestWidth = chest.getSize_x() * TILE_SIZE;
        int chestHeight = chest.getSize_y() * TILE_SIZE;

        if (characterX == chestX && characterY == chestY) {
            int a=chest.getLocation().getX()/TILE_SIZE+2;
            int b=chest.getLocation().getY()/TILE_SIZE+2;
            sortedChests.add(chest. getType()+" collected "+chest.location.getX()/TILE_SIZE+","+chest.location.getY()/TILE_SIZE+"-"+a+","+b); 
            chests.remove(chest);
            app.getCollectedItems().add(c+"-"+chest.getType());
            c++;
            i--;
        } else {
            g.drawImage(chestImage, chestX, chestY, chestWidth, chestHeight, null);
        }
    }
    
    
    Collections.sort(sortedChests);
    
    for(String chestName : sortedChests){
        
        Message m=new Message(chestName);
        messages.add(m);
    }
    sortedChests.clear();
    
    if(chests.isEmpty()&&!End){
    List<String> collectedItems=app.getCollectedItems();
        for(String collectedItem:collectedItems){
            
            Message m=new Message(collectedItem,-1000);
            messages.add(m);
            End=true;
        }
    }
    
    
    
    
}
private void drawMessage(Graphics g) {
    g.setColor(Color.BLUE);
    g.setFont(new Font("Arial", Font.BOLD, 10));
    int offsetY = 30;

    Iterator<Message> iterator = messages.iterator();
    while (iterator.hasNext()) {
        Message message = iterator.next();
        if (message.isMessageOn()) {
            int messageX = getWidth() - g.getFontMetrics().stringWidth(message.getMessage()) - 20;
            g.drawString(message.getMessage(), messageX, offsetY);
            offsetY+=30;
            int messageCounter = message.getMessageCounter();
            messageCounter++;
            message.setMessageCounter(messageCounter);
            if (messageCounter >= 40) { 
                iterator.remove();
            }
        }
    }

}
private void checkObstaclesInSight() {
    int characterX = player.getLocation().getX();
    int characterY = player.getLocation().getY();
    int minX=3, maxX=3;
    int minY=3,maxY=3;
  if (characterX == 0)
    minX = 0;
  if (characterX == 1) 
    minX = 1;
  if (characterX == 2) 
    minX = 2;
  if (characterX == MAP_SIZE_WIDTH - 3) 
    maxX = 3;
  if (characterX == MAP_SIZE_WIDTH - 2) 
    maxX = 2;
  if (characterX == MAP_SIZE_WIDTH - 1) 
    maxX = 1;

   if (characterY == 0)
    minY = 0;
  if (characterY == 1) 
    minY = 1;
  if (characterY == 2) 
    minY = 2;
  if (characterY == MAP_SIZE_WIDTH - 3) 
    maxY = 3;
  if (characterY == MAP_SIZE_WIDTH - 2) 
    maxY = 2;
  if (characterY == MAP_SIZE_WIDTH - 1) 
    maxY = 1;
  
      for (int i = characterX - minX; i <= characterX + maxX; i++) {
            for (int j = characterY - minY; j <= characterY + maxY; j++) {
                
                
                for(TileInfo tileinfo:TileInfos){
                    if(!tileinfo.isExplored()){
                    Set<Point> points=tileinfo.getPoints();
                    for(Point point:points){
                       int x= point.x;
                       int y=point.y;
                        if(x==i&&y==j){
                            
                       String string=tileinfo.getType()+" spotted";
                        Message m=new Message(string);
                        messages.add(m);
                        tileinfo.setExplored(true);
                      }
                    }
                   }
                }
                
       }
    }
    
    
    
}
private void generateFog(Graphics g){
    int characterX = player.getLocation().getX();
    int characterY = player.getLocation().getY();
    int minX=3, maxX=3;
    int minY=3,maxY=3;
  if (characterX == 0)
    minX = 0;
  if (characterX == 1) 
    minX = 1;
  if (characterX == 2) 
    minX = 2;
  if (characterX == MAP_SIZE_WIDTH - 3) 
    maxX = 2;
  if (characterX == MAP_SIZE_WIDTH - 2) 
    maxX = 1;
  if (characterX == MAP_SIZE_WIDTH - 1) 
    maxX = 0;

   if (characterY == 0)
    minY = 0;
  if (characterY == 1) 
    minY = 1;
  if (characterY == 2) 
    minY = 2;
  if (characterY == MAP_SIZE_WIDTH - 3) 
    maxY = 2;
  if (characterY == MAP_SIZE_WIDTH - 2) 
    maxY = 1;
  if (characterY == MAP_SIZE_WIDTH - 1) 
    maxY = 0;
  
    for (int i = characterX - minX; i <= characterX + maxX; i++) {
            for (int j = characterY - minY; j <= characterY + maxY; j++) {
                Fog[i][j]=1;
      }
    }
   
     for (int x = 0; x < MAP_SIZE_WIDTH; x++) {
        for (int y = 0; y < MAP_SIZE_HEIGHT; y++) {
    
            if(Fog[x][y]==0){
                
              g.setColor(new Color(0, 0, 0, 255));
              g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                
            }
        }
      }
     
     
}

    private void randomspawn(){
        
        int x=0;
        int y=0;
        boolean check = false;
        
        while (!check) {
       x = random.nextInt(MAP_SIZE_WIDTH);
       y = random.nextInt(MAP_SIZE_HEIGHT);

        
        check = (isInsideMap(x, y ,7, 7)&&isAreaEmpty(x, y ,7, 7));
    }
    setUpSpawn(x, y ,7, 7);
    x=x+3;
    y=y+3;
    
    player.getLocation().setX(x);
    player.getLocation().setY(y);
        
    }
   
    private void generateObstacles() {
        
        int x;
        int y;
        int random_size;
        int size;
        //Wall 
        for(int i=0;i<3;){
            x = random.nextInt(MAP_SIZE_WIDTH) ; 
             y = random.nextInt(MAP_SIZE_HEIGHT) ; 
             x= winterSummerControl(x,10);
            if (isInsideMap(x, y, 12,3) && isAreaEmpty(x, y, 12,3)) {
                x=x+1; y=y+1;
                setUpMap(x,y,10,1);
                setUpTileInfo(x,y,10,1,"Wall");
                i++;
                if(x>winterSummerBorderX){
                obstacles.add(new SummerWall("Wall", 10, 1, x * TILE_SIZE, y * TILE_SIZE));
                }
                else {
                obstacles.add(new IceWall("Wall",10,1,x*TILE_SIZE,y*TILE_SIZE));
                }
                 
                 
            }
        }
        //Mountain
        for(int i=0;i<2;){
            x = random.nextInt(MAP_SIZE_WIDTH) ; 
            y = random.nextInt(MAP_SIZE_HEIGHT) ; 
            size=15;
           x= winterSummerControl(x,size);
            if (isInsideMap(x, y, size,size) && isAreaEmpty(x, y, size,size)) {
                setUpMap(x,y,size,size);
                setUpTileInfo(x,y,size,size,"Mountain");
                i++;
                if(x>winterSummerBorderX){
                obstacles.add(new SummerMountain("Mountain",size,size,x*TILE_SIZE,y*TILE_SIZE));
                }
                else {
                obstacles.add(new WinterMountain("Mountain",size,size,x*TILE_SIZE,y*TILE_SIZE));
                }
            }
        }
         
        
        //Tree 
        for (int i = 0; i < 15;) {
             x = random.nextInt(MAP_SIZE_WIDTH) ; 
             y = random.nextInt(MAP_SIZE_HEIGHT) ; 
             random_size=random.nextInt(4)+2;
           x=  winterSummerControl(x,random_size);
            if (isInsideMap(x, y, random_size,random_size) && isAreaEmpty(x, y, random_size,random_size)) {
                setUpMap(x,y,random_size,random_size);
                setUpTileInfo(x,y,random_size,random_size,"Tree");
                i++;
            if (x > winterSummerBorderX) {
                obstacles.add(new SummerTree("Tree", random_size, random_size, x * TILE_SIZE, y * TILE_SIZE));
            } else {
                obstacles.add(new WinterTree("Tree", random_size, random_size, x * TILE_SIZE, y * TILE_SIZE));
            }
        }
           
        }
        //Rock 
        for(int i=0;i<5; ){
             x = random.nextInt(MAP_SIZE_WIDTH) ; 
             y = random.nextInt(MAP_SIZE_HEIGHT) ; 
             random_size=random.nextInt(2)+2;
            x= winterSummerControl(x,random_size);
            if (isInsideMap(x, y, random_size,random_size) && isAreaEmpty(x, y, random_size,random_size)) {
                setUpMap(x,y,random_size,random_size);
                setUpTileInfo(x,y,random_size,random_size,"Rock");
                i++;
                if(random_size==2)
                  obstacles.add(new SmallRock("Rock", random_size, random_size, x * TILE_SIZE, y * TILE_SIZE));
                else 
                    if (x > winterSummerBorderX) {
                obstacles.add(new SummerLargeRock("Rock", random_size, random_size, x * TILE_SIZE, y * TILE_SIZE));
            } else {
                obstacles.add(new IceRock("Rock", random_size, random_size, x * TILE_SIZE, y * TILE_SIZE));
            }
                  
            }
        }
       
    }
    
     private int winterSummerControl(int x,int sizeX){
        
        if(x<=MAP_SIZE_WIDTH/2 &&x >(MAP_SIZE_WIDTH/2)-sizeX)
            return x-sizeX;
        
        return x;
    }
    
    private void generateDynamicObstacles(){
        int x;
        int y;
        int size;
        //BIRD
        for(int i=0;i<2;){
            x = random.nextInt(MAP_SIZE_WIDTH) ; 
            y = random.nextInt(MAP_SIZE_HEIGHT) ; 
            size=2;
            if (isInsideMap(x, y,size,12) && isAreaEmpty(x, y, size,12)) {
                setUpMap(x,y,size,12);
                setUpTileInfo(x,y,size,12,"Bird");
 
                obstacles2.add(new Bird("Bird", size, size, x * TILE_SIZE, y * TILE_SIZE));
                i++;
              }
            }
        //BEE
            for(int i=0;i<2;){
            x = random.nextInt(MAP_SIZE_WIDTH) ; 
            y = random.nextInt(MAP_SIZE_HEIGHT) ; 
               size=2; 
                if (isInsideMap(x, y,7,size) && isAreaEmpty(x, y, 7,size)) {
                setUpMap(x,y,7,size);
                setUpTileInfo(x,y,7,size,"Bee");

                obstacles2.add(new Bee("Bee", size, size, x * TILE_SIZE, y * TILE_SIZE));
                i++;
              }
            }
                
            
        }
        private void generatechest(){
        int x;
        int y;
        final int size=2;
        final int startX=player.getLocation().getX();
        final int startY=player.getLocation().getY();
        for(int i=0;i<5;){
          AStar Astar=new AStar(Map);

            x = random.nextInt(MAP_SIZE_WIDTH) ; 
            y = random.nextInt(MAP_SIZE_HEIGHT) ; 
            
            if (isInsideMap(x, y,size,size) && isAreaEmpty(x, y, size,size)&& isSpawnEmpty(x, y, size,size)) {
            if(Astar.isReachable(startX, startY, x, y)){
                
                Point point = new Point(x, y);
                chestlocations.add(point);
                setUpTileInfo(x,y,size,size,"Gold Chest");
                chests.add(new GoldChest("Gold Chest", size, size, x * TILE_SIZE, y * TILE_SIZE));
                i++;
              }
            }
        }
          for(int i=0;i<5;){
           AStar Astar=new AStar(Map);

            x = random.nextInt(MAP_SIZE_WIDTH) ; 
            y = random.nextInt(MAP_SIZE_HEIGHT) ; 
            
            if (isInsideMap(x, y,size,size) && isAreaEmpty(x, y, size,size)&& isSpawnEmpty(x, y, size,size)) {
            if(Astar.isReachable(startX, startY, x, y)){
                Point point = new Point(x, y);
                chestlocations.add(point);
                setUpTileInfo(x,y,size,size,"Silver Chest");

                chests.add(new SilverChest("Silver Chest", size, size, x * TILE_SIZE, y * TILE_SIZE));
                i++;
              }
            }
        }  
          for(int i=0;i<5;){
           AStar Astar=new AStar(Map);

            x = random.nextInt(MAP_SIZE_WIDTH) ; 
            y = random.nextInt(MAP_SIZE_HEIGHT) ; 
            
            if (isInsideMap(x, y,size,size) && isAreaEmpty(x, y, size,size)&& isSpawnEmpty(x, y, size,size)) {
            if(Astar.isReachable(startX, startY, x, y)){
                Point point = new Point(x, y);
                chestlocations.add(point);
                setUpTileInfo(x,y,size,size,"Emerald Chest");

                chests.add(new EmeraldChest("Emerald Chest", size, size, x * TILE_SIZE, y * TILE_SIZE));
                i++;
              }
            }
        }  
         for(int i=0;i<5;){
         AStar Astar=new AStar(Map);

            x = random.nextInt(MAP_SIZE_WIDTH) ; 
            y = random.nextInt(MAP_SIZE_HEIGHT) ; 
            
            if (isInsideMap(x, y,size,size) && isAreaEmpty(x, y, size,size)&& isSpawnEmpty(x, y, size,size)) {
            if(Astar.isReachable(startX, startY, x, y)){
                Point point = new Point(x, y);
                chestlocations.add(point);
                setUpTileInfo(x,y,size,size,"Copper Chest");

                chests.add(new CopperChest("Copper Chest", size, size, x * TILE_SIZE, y * TILE_SIZE));
                i++;
              }
            }
        }   
         

    Set<Point> unvisited = new HashSet<>(chestlocations);
    //Spawnpoint
    Point currentPoint = new Point(startX, startY);

    while (!unvisited.isEmpty()) {
        Point nearestPoint = findNearestPoint(currentPoint, unvisited);
        AStar astar=new AStar(Map);
        List<Point> pathToNearest = astar.calculateShortestPath(currentPoint.x, currentPoint.y, nearestPoint);
       
        Paths.addAll(pathToNearest);
        
        currentPoint = nearestPoint;
        unvisited.remove(nearestPoint);
    }

}
    
 private Point findNearestPoint(Point currentPoint, Set<Point> unvisited) {
    Point nearestPoint = null;
    double minDistance = Double.MAX_VALUE;

    for (Point point : unvisited) {
        double distance = currentPoint.distance(point);
        if (distance < minDistance) {
            minDistance = distance;
            nearestPoint = point;
        }
    }

    return nearestPoint;
}   
    
private void setUpMap(int x,int y,int size_x,int size_y){
        for(int i=x;i<x+size_x;i++){
            for(int j=y;j<y+size_y;j++){
                
                Map[i][j]=1;
            }        
        }
}
private void setUpSpawn(int x,int y,int size_x,int size_y){
        for(int i=x;i<x+size_x;i++){
            for(int j=y;j<y+size_y;j++){
                
                Spawn[i][j]=1;
            }        
        }
}
private void setUpTileInfo(int x, int y, int size_x, int size_y,String type) {
    Set<Point> points = new HashSet<>();
    
    for (int i = x; i < x + size_x; i++) {
        for (int j = y; j < y + size_y; j++) {
           
            points.add(new Point(i, j));
           
        }
    }
    TileInfo tileInfo = new TileInfo(points, type);
            TileInfos.add(tileInfo);
    
}
    private boolean isInsideMap(int x, int y, int size_x,int size_y) {
    return x + size_x <= MAP_SIZE_WIDTH && y + size_y <= MAP_SIZE_HEIGHT;
}
    private boolean isAreaEmpty(int x, int y, int size_x,int size_y) {
    for (int i = x; i < x + size_x; i++) {
        for (int j = y; j < y + size_y; j++) {
            if (Map[i][j] != 0) { 
                return false;
            }
        }
    }
    return true; 
    }
    private boolean isSpawnEmpty(int x, int y, int size_x,int size_y) {
    for (int i = x; i < x + size_x; i++) {
        for (int j = y; j < y + size_y; j++) {
            if (Spawn[i][j] != 0) { 
                return false;
            }
        }
    }
    return true; 
    }


}
