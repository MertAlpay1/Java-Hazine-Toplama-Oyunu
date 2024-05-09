
import java.util.ArrayList;
import java.util.List;


public class App {
   private int stepsToGoal; 
   private List<String> collectedItems; 
    
    public App() {
        stepsToGoal = 0;
        collectedItems = new ArrayList<>();
    }

    public int getStepsToGoal() {
        return stepsToGoal;
    }

    public void setStepsToGoal(int stepsToGoal) {
        this.stepsToGoal = stepsToGoal;
    }

    public List<String> getCollectedItems() {
        return collectedItems;
    }

    public void setCollectedItems(List<String> collectedItems) {
        this.collectedItems = collectedItems;
    }
    
}
