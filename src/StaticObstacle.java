
import java.awt.Image;


public abstract class StaticObstacle extends Obstacle {

    public StaticObstacle(String type, int size_x, int size_y, int x, int y) {
        super(type, size_x, size_y, x, y);
        this.can_move = false;
    }


    }
    

