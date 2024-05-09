
public class Message {
    private String message;
    private int messageCounter;
    private boolean messageOn;
public Message(String message,int timer) {
        this.message = message;
        this.messageCounter = timer;
        this.messageOn = true;
    }
    public Message(String message) {
        this.message = message;
        this.messageCounter = 0;
        this.messageOn = true;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageCounter() {
        return messageCounter;
    }

    public boolean isMessageOn() {
        return messageOn;
    }

    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }

    public void setMessageOn(boolean messageOn) {
        this.messageOn = messageOn;
    }
    

    
    
    
}
