package MES;

public class orderTransform extends order {


    private final String Px;
    private final String Py;
    private final int nTotal;
    private int nDone;

    public orderTransform(int id, float submitTime, int type, int status, String Px, String Py, int nTotal, int nDone, int maxDelay) {
        super(id, submitTime, type, status, maxDelay);
        this.Px = Px;
        this.Py = Py;
        this.nTotal = nTotal;
        this.nDone = nDone;
    }



    public String getPx() {
        return Px;
    }

    public String getPy() {
        return Py;
    }


    public int getNTotal() {
        return nTotal;
    }

    public int getNDone() {
        return nDone;
    }

    public void setNDone(int nDone) {
        this.nDone = nDone;
    }

    public int getNToProcess() {
        return (this.nTotal - this.nDone);
    }



}
