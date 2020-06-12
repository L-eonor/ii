package MES;

public class order {
    private int id;
    private int priority;
    private int submitTime;
    private int startTime;
    private int endTime;
    private int type;
    private int status;

    public order (int id, int priority,
                  int submitTime, int startTime,
                  int endTime, int type,
                  int status) {
        this.id = id;
        this.priority = priority;
        this.submitTime = submitTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
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

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

}
