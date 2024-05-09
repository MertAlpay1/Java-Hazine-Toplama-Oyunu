
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Bird extends DynamicObstacle{
    int tileSize = Game.TILE_SIZE;
    int origin_y;
    boolean moveup=false;
    public Bird(String type, int size_x, int size_y, int x, int y) {
        super(type, size_x, size_y, x, y);
        try {
             this.image=ImageIO.read(new File("res/bird.png"));
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        origin_y=y;
    }

    @Override
    public void move() {
        int y=getLocation().getY();
        if(moveup){
            if ( y> origin_y ) {
            getLocation().setY(y - tileSize);
        }else {
            moveup = false;
        }
        }
        else {
            if(y < origin_y + 9 * tileSize){
                getLocation().setY(y + tileSize);
            }
            else {
                moveup = true;
            }
            
        }
    
    }
    
}
