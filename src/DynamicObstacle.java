
public abstract class DynamicObstacle extends Obstacle {
    
    public DynamicObstacle(String type, int size_x, int size_y, int x, int y) {
        super(type, size_x, size_y, x, y);
        this.can_move=true;
        
    }
    
    public abstract void move();
    
    }
    
    

