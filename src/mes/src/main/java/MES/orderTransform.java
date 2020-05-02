package MES;

public class orderTransform extends order {


    private String Px;
    private String Py;
    private int nTotal;
    private int nDone;

    public orderTransform(int id, int submitTime, int startTime, int type, int status, String Px, String Py, int nTotal, int nDone, int maxDelay) {
        super(id, submitTime, startTime, type, status, maxDelay);
        this.Px = Px;
        this.Py = Py;
        this.nTotal = nTotal;
        this.nDone = nDone;
        setEndTime();
    }



    public String getPx() {
        return Px;
    }

    public void setPx(String px) {
        Px = px;
    }

    public String getPy() {
        return Py;
    }

    public void setPy(String py) {
        Py = py;
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
