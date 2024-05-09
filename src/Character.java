
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;


public class Character {
    
   private String Id;
   private String Name;
   private Location location;
   private Image image;
   private int size_x;
   private int size_y;
   private static int a=0;
    public Character(String Id, String Name,int size_x, int size_y, int x ,int y) {
        this.Id = Id;
        this.Name = Name;
        this.size_x=size_x;
        this.size_y=size_y;
       this.location = new Location(x,y);
        try {
             this.image=ImageIO.read(new File("res/character.png"));
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void shortestPath(List<Point> Paths){
        
        if (Paths != null && !Paths.isEmpty() && a < Paths.size()) {
        Point nextPos = Paths.get(a++);
        int x = nextPos.x;
        int y = nextPos.y;
        getLocation().setX(x);
        getLocation().setY(y);
    }
        
    }
    

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    

    public Image getImage() {
        return image;
    }

   
    
    
    
}
