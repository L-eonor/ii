package MES;

public class orderUnload extends order {
    private String Px;
    private String Dy;
    private int quantity;
    private int maxDelay;
    private String path;

    public orderUnload(int id,
                       int priority,
                       int submitTime,
                       int startTime,
                       int endTime,
                       int type,
                       int status,
                       String Px,
                       String Dy,
                       int quantity,
                       int maxDelay,
                       String path) {

        super(id, priority, submitTime, startTime, endTime, type, status);
        this.Px=Px;
        this.Dy=Dy;
        this.quantity=quantity;
        this.maxDelay=maxDelay;
    }

    public String getPx() {
        return Px;
    }

    public void setPx(String px) {
        Px = px;
    }

    public String getDy() {
        return Dy;
    }

    public void setDy(String dy) {
        Dy = dy;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
