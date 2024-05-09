
import java.awt.Point;
import java.util.Set;


public class TileInfo {
    private Set<Point> points;
    private String type;
    private boolean explored;
    public TileInfo(Set<Point> points, String type) {
        this.points = points;
        this.type = type;
        this.explored = false;
    }

    public Set<Point> getPoints() {
        return points;
    }

    

    public String getType() {
        return type;
    }

    

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }
}
