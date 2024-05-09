
import java.awt.Image;


public abstract class  Obstacle {
    
    public String type;
    public int size_x;
    public int size_y;
    public boolean can_move;
    public Image image;
    public Location location;

    public Obstacle(String type, int size_x, int size_y, int x,int y) {
        this.type = type;
        this.size_x = size_x;
        this.size_y = size_y;
        this.location = new Location(x,y);
    }

    public String getType() {
        return type;
    }

    public int getSize_x() {
        return size_x;
    }

    public int getSize_y() {
        return size_y;
    }

    public boolean isCan_move() {
        return can_move;
    }

    public Image getImage() {
        return image;
    }

    public Location getLocation() {
        return location;
    }
    
    
    
}
