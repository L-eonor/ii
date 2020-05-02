package MES;

public class orderTransform extends order {


    private String Px;
    private String Py;
    private int nTotal;
    private int nDone;
    private int nProcessing;

    public orderTransform(int id, int submitTime, int startTime, int type, int status, String Px, String Py, int nTotal, int nDone,
                          int nProcessing, int maxDelay) {
        super(id, submitTime, startTime, type, status, maxDelay);
        this.Px = Px;
        this.Py = Py;
        this.nTotal = nTotal;
        this.nDone = nDone;
        this.nProcessing = nProcessing;
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

    public void setNTotal(int nTotal) {
        this.nTotal = nTotal;
    }

    public int getNDone() {
        return nDone;
    }

    public void setNDone(int nDone) {
        this.nDone = nDone;
    }

    public int getNProcessing() {
        return nProcessing;
    }

    public void setNProcessing(int nProcessing) {
        this.nProcessing = nProcessing;
    }



}
