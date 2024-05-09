
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class SummerMountain extends Mountain {
    
    public SummerMountain(String type, int size_x, int size_y, int x, int y) {
        super(type, size_x, size_y, x, y);
        try {
            this.image=ImageIO.read(new File("res/summountain.png"));
        }
        catch(IOException e){
            
            e.printStackTrace();
        }
    }
    
}
