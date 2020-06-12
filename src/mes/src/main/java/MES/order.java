package MES;

import java.util.Comparator;

public class order {
    private final int id;
    private final float submitTime; //System time a que a ordem chega
    private  float startTime; //Quando começa a ser processada
    private float endTime; //Quando finalizas a ordem
    private final int type; //1-transform 2-Unload 3-Load 4-Request
    private final int maxDelay;
    private int status; //  1-Waiting to start | 2-Processing | 3- All pieces sent | 4- All pieces received
    private int unitsReachedEnd;

    public order (int id, float submitTime, int type, int status, int maxDelay) {
        this.id = id;
        this.submitTime = submitTime;
        this.type = type;
        this.status = status;
        this.maxDelay=maxDelay;
        this.unitsReachedEnd=0;
    }

    @Override
    public String toString() {
        return "order{" +
                "id=" + id +
                ", submitTime=" + submitTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type=" + type +
                ", status=" + status +
                ", maxDelay=" + maxDelay +
                '}';
    }

    public int getId() {
        return id;
    }

    public float getSubmitTime() {
        return submitTime;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float time) {
        this.endTime = time;
    }

    public float getIdealEndTime() {
        return this.submitTime+this.maxDelay;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int s) {
        this.status=s;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public int getUnitsReachedEnd(){
        return this.unitsReachedEnd;
    }

    public synchronized void setUnitsReachedEnd(int u){
        this.unitsReachedEnd = u;
    }


}

class OrderComparator implements Comparator<orderTransform> {

    @Override
    public int compare(orderTransform o1, orderTransform o2) {
        if (o1.getIdealEndTime() < o2.getIdealEndTime()) return -1;
        else if (o1.getIdealEndTime() > o2.getIdealEndTime()) return 1;
        else return 0;
    }
}


