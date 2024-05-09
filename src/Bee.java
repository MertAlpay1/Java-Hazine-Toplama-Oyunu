
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Bee extends DynamicObstacle{
    int tileSize = Game.TILE_SIZE;
    int origin_x;
    boolean moveright=false;
    public Bee(String type, int size_x, int size_y, int x, int y) {
        super(type, size_x, size_y, x, y);
         try {
             this.image=ImageIO.read(new File("res/Bee.png"));
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    
        
        origin_x=x;
    }

    @Override
    public void move() {
       int x=getLocation().getX();
    if(moveright){
            if ( x> origin_x ) {
            getLocation().setX(x - tileSize);
        }else {
            moveright = false;
        }
        }
        else {
            if(x < origin_x + 5 * tileSize){
                getLocation().setX(x + tileSize);
            }
            else {
                moveright = true;
            }
            
        }
    
    }

    
    
    
}