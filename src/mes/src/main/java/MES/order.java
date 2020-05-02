package MES;

import java.util.Comparator;

public class order {
    private int id;
    private int submitTime; //System time a que a ordem chega
    private int startTime; //Quando começa a ser processada
    private int endTime; //Quando finalizas a ordem
    private int type; //1-transform 2-Unload 3-Request 4-Load
    private int status; //1-por iniciar 2-em processamento 3-concluida
    private int maxDelay;

    public order (int id, int submitTime, int startTime, int type, int status, int maxDelay) {
        this.id = id;
        this.submitTime = submitTime;
        this.startTime = startTime;
        this.type = type;
        this.status = status;
        this.maxDelay=maxDelay;
        this.endTime=0;
    }

    public int getId() {
        return id;
    }

    public int getSubmitTime() {
        return submitTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        this.endTime = this.startTime+maxDelay;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

}

class OrderComparator implements Comparator<orderTransform> {

    @Override
    public int compare(orderTransform o1, orderTransform o2) {
        return (o1.getEndTime() - o2.getEndTime());
    }
}


