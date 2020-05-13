package MES;

public class Warehouse {

    //Attributes
    private static int P1;
    private static int P2;
    private static int P3;
    private static int P4;
    private static int P5;
    private static int P6;
    private static int P7;
    private static int P8;
    private static int P9;

    //Methods

    public static void removePiece(String type){
        switch (type){
            case "P1":
                P1--;
            case "P2":
                P2--;
            case "P3":
                P3--;
            case "P4":
                P4--;
            case "P5":
                P5--;
            case "P6":
                P6--;
            case "P7":
                P7--;
            case "P8":
                P8--;
            case "P9":
                P9--;
            default: return;
        }
    }

    public static void addPiece(String type){
        switch (type){
            case "P1":
                P1++;
            case "P2":
                P2++;
            case "P3":
                P3++;
            case "P4":
                P4++;
            case "P5":
                P5++;
            case "P6":
                P6++;
            case "P7":
                P7++;
            case "P8":
                P8++;
            case "P9":
                P9++;
            default: return;
        }
    }

    public static void setPiece(int n,String type){
        switch (type){
            case "P1":
                P1=n;
            case "P2":
                P2=n;
            case "P3":
                P3=n;
            case "P4":
                P4=n;
            case "P5":
                P5=n;
            case "P6":
                P6=n;
            case "P7":
                P7=n;
            case "P8":
                P8=n;
            case "P9":
                P9=n;
            default: return;
        }
    }



}
