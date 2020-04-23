package MES;

public class orderTransform extends order {


    private String Px;
    private String Py;
    private int nTotal;
    private int nDone;
    private int nProcessing;
    private int maxDelay;
    private String path;

    public orderTransform(int id, int priority,
                          int submitTime, int startTime,
                          int endTime, int type, int status, String Px, String Py, int nTotal, int nDone, int nProcessing, int maxDelay, String path) {
        super(id, priority, submitTime, startTime, endTime,
                type, status);

        this.Px = Px;
        this.Py = Py;
        this.nTotal = nTotal;
        this.nDone = nDone;
        this.nProcessing = nProcessing;
        this.maxDelay = maxDelay;
        this.path = path;
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
