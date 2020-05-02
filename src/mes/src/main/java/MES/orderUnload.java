package MES;

public class orderUnload extends order {
    private String Px;
    private String Dy;
    private int quantity;
    private int nDone;

    public orderUnload(int id, int submitTime, int startTime, int type, int status, String Px, String Dy, int quantity, int maxDelay) {
        super(id, submitTime, startTime, type, status, maxDelay);
        this.Px=Px;
        this.Dy=Dy;
        this.quantity=quantity;
        this.nDone=0;
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

    public int getNDone() {
        return nDone;
    }

    public void setNDone(int nDone) {
        this.nDone = nDone;
    }

}
