package MES;

public class orderLoad extends order {

    private final String Px;

    public orderLoad(int id, float submitTime, int type, int status, int maxDelay,String unitType){
        super(id, submitTime,type, status, maxDelay);
        this.Px=unitType;
    }

}
