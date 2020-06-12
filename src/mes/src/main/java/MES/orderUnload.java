package MES;

public class orderUnload extends order {
    private final String Px;
    private final String Dy;
    private final int quantity;
    private int nDone;

    public orderUnload(int id, float submitTime, int type, int status, String Px, String Dy, int quantity, int maxDelay) {
        super(id, submitTime, type, status, maxDelay);
        this.Px=Px;
        this.Dy=Dy;
        this.quantity=quantity;
        this.nDone=0;
    }

    public String getPx() {
        return Px;
    }

    public String getDy() {
        return Dy;
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
