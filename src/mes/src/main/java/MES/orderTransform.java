package MES;

public class orderTransform extends order {


    private final String Px;
    private final String Py;
    private final int nTotal;
    private int nDone;
    private boolean outOfUnits;

    public orderTransform(int id, float submitTime, int type, int status, String Px, String Py, int nTotal, int nDone, int maxDelay) {
        super(id, submitTime, type, status, maxDelay);
        this.Px = Px;
        this.Py = Py;
        this.nTotal = nTotal;
        this.nDone = nDone;
        this.outOfUnits = false;

    }

    @Override
    public String toString() {
        return "order{" +
                "id=" + this.getId() +
                ", submitTime=" + this.getSubmitTime() +
                ", startTime=" + this.getStartTime() +
                ", endTime=" + this.getEndTime() +
                ", type=" + this.getType() +
                ", status=" + this.getStatus() +
                ", maxDelay=" + this.getMaxDelay() +
                ", unitsDone=" + this.getNDone() +
                ", unitsTotal=" + this.getNTotal() +
                '}';
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

    public void setOutOfUnits(boolean outOfUnits) {
        this.outOfUnits = outOfUnits;
    }

    public boolean getOutOfUnits(){
        return this.outOfUnits;
    }



}
