package MES;

public class Warehouse {

    //Attributes
    private static int P1=0;
    private static int P2=0;
    private static int P3=0;
    private static int P4=0;
    private static int P5=0;
    private static int P6=0;
    private static int P7=0;
    private static int P8=0;
    private static int P9=0;

    //Methods

    public static void removePiece(String type){
        switch (type){
            case "P1":
                P1--;
                break;
            case "P2":
                P2--;
                break;
            case "P3":
                P3--;
                break;
            case "P4":
                P4--;
                break;
            case "P5":
                P5--;
                break;
            case "P6":
                P6--;
                break;
            case "P7":
                P7--;
                break;
            case "P8":
                P8--;
                break;
            case "P9":
                P9--;
                break;
        }
    }

    public static void addPiece(String type){
        switch (type){
            case "P1":
                P1++;
                break;
            case "P2":
                P2++;
                break;
            case "P3":
                P3++;
                break;
            case "P4":
                P4++;
                break;
            case "P5":
                P5++;
                break;
            case "P6":
                P6++;
                break;
            case "P7":
                P7++;
                break;
            case "P8":
                P8++;
                break;
            case "P9":
                P9++;
                break;
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

    public static int getPiece(String type){
        switch (type){
            case "P1":
                return P1;
            case "P2":
                return P2;
            case "P3":
                return P3;
            case "P4":
                return P4;
            case "P5":
                return P5;
            case "P6":
                return P6;
            case "P7":
                return P7;
            case "P8":
                return P8;
            case "P9":
                return P9;
            default: return -1;
        }
    }


}
