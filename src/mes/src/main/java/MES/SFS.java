package MES;

import java.util.ArrayList;

public class SFS {

    //Attributes

    static final public Cell[][] sfsCells = new Cell[][]{
            {null, null, null, null, null, null, null, null, null},
            {new Cell(1, 0, "WarehouseOut"), new Cell(1, 1), new Rotator(1, 2), new Cell(1, 3), new Rotator(1, 4), new Cell(1, 5), new Rotator(1, 6), new Rotator(1, 7), new Cell(1, 8)},
            {null, null, new Cell(2, 2), null, new Cell(2, 4), null, new Cell(2, 6), new Cell(2, 7), null},
            {null, new Machine(3, 1), new Rotator(3, 2), new Machine(3, 3), new Rotator(3, 4), new Machine(3, 5), new Rotator(3, 6), new Pusher(3, 7), new Slider(3, 8)},
            {null, new Machine(4, 1), new Rotator(4, 2), new Machine(4, 3), new Rotator(4, 4), new Machine(4, 5), new Rotator(4, 6), new Pusher(4, 7), new Slider(4, 8)},
            {null, new Machine(5, 1), new Rotator(5, 2), new Machine(5, 3), new Rotator(5, 4), new Machine(5, 5), new Rotator(5, 6), new Pusher(5, 7), new Slider(5, 8)},
            {null, null, new Cell(6, 2), null, new Cell(6, 4), null, new Cell(6, 6), new Cell(6, 7), null},
            {new Cell(7, 0, "WarehouseIn"), new Cell(7, 1), new Rotator(7, 2), new Cell(7, 3), new Rotator(7, 4), new Cell(7, 5), new Rotator(7, 6), new Rotator(7, 7), new Cell(7, 8)},
    };

    static final public Cell[][] sfsCellsLoad = new Cell[][]{
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, (Rotator) sfsCells[1][6], (Rotator) sfsCells[1][7], sfsCells[1][8]},
            {null, null, null, null, null, null, sfsCells[2][6], sfsCells[2][7], null},
            {null, null, null, null, null, null, (Rotator) sfsCells[3][6], (Pusher) sfsCells[3][7], (Slider) sfsCells[3][8]},
            {null, null, null, null, null, null, (Rotator) sfsCells[4][6], (Pusher) sfsCells[4][7], (Slider) sfsCells[4][8]},
            {null, null, null, null, null, null, (Rotator) sfsCells[5][6], (Pusher) sfsCells[5][7], (Slider) sfsCells[5][8]},
            {null, null, null, null, null, null, sfsCells[6][6], sfsCells[6][7], null},
            {sfsCells[7][0], sfsCells[7][1], (Rotator) sfsCells[7][2], sfsCells[7][3], (Rotator) sfsCells[7][4], sfsCells[7][5], (Rotator) sfsCells[7][6], (Rotator) sfsCells[7][7], sfsCells[7][8]},
    };

    static final public Cell[][] sfsCellsTransformation = new Cell[][]{
            {null, null, null, null, null, null, null, null, null},
            {sfsCells[1][0], sfsCells[1][1], (Rotator) sfsCells[1][2], sfsCells[1][3], (Rotator) sfsCells[1][4], sfsCells[1][5], (Rotator) sfsCells[1][6], null, null},
            {null, null, sfsCells[2][2], null, sfsCells[2][4], null, sfsCells[2][6], null, null},
            {null, (Machine) sfsCells[3][1], (Rotator) sfsCells[3][2], (Machine) sfsCells[3][3], (Rotator) sfsCells[3][4], (Machine) sfsCells[3][5], (Rotator) sfsCells[3][6],null, null},
            {null, (Machine) sfsCells[4][1], (Rotator) sfsCells[4][2], (Machine) sfsCells[4][3], (Rotator) sfsCells[4][4], (Machine) sfsCells[4][5], (Rotator) sfsCells[4][6], null, null},
            {null, (Machine) sfsCells[5][1], (Rotator) sfsCells[5][2], (Machine) sfsCells[5][3], (Rotator) sfsCells[5][4], (Machine) sfsCells[5][5], (Rotator) sfsCells[5][6], null, null},
            {null, null, sfsCells[6][2], null, sfsCells[6][4], null, sfsCells[6][6], null, null},
            {sfsCells[7][0], sfsCells[7][1], (Rotator) sfsCells[7][2], sfsCells[7][3], (Rotator) sfsCells[7][4], sfsCells[7][5], (Rotator) sfsCells[7][6], null, null},
    };

    static final public Cell[][] sfsCellsUnload = new Cell[][]{
            {null, null, null, null, null, null, null, null, null},
            {sfsCells[1][0], sfsCells[1][1], (Rotator) sfsCells[1][2], sfsCells[1][3], (Rotator) sfsCells[1][4], sfsCells[1][5], (Rotator) sfsCells[1][6], (Rotator) sfsCells[1][7], sfsCells[1][8]},
            {null, null, null, null, null, null, sfsCells[2][6], sfsCells[2][7], null},
            {null, null, null, null, null, null, (Rotator) sfsCells[3][6], (Pusher) sfsCells[3][7], (Slider) sfsCells[3][8]},
            {null, null, null, null, null, null, (Rotator) sfsCells[4][6], (Pusher) sfsCells[4][7], (Slider) sfsCells[4][8]},
            {null, null, null, null, null, null, (Rotator) sfsCells[5][6], (Pusher) sfsCells[5][7], (Slider) sfsCells[5][8]},
            {null, null, null, null, null, null, sfsCells[6][6], sfsCells[6][7], null},
            {null, null, null, null, null, null, (Rotator) sfsCells[7][6], (Rotator) sfsCells[7][7], null},
    };

    static final ArrayList<Machine> MachinesA = new ArrayList<Machine>() {
        {
            add((Machine) sfsCells[3][1]);
            add((Machine) sfsCells[3][3]);
            add((Machine) sfsCells[3][5]);
        }
    };
    static final ArrayList<Machine> MachinesB = new ArrayList<Machine>() {
        {
            add((Machine) sfsCells[4][1]);
            add((Machine) sfsCells[4][3]);
            add((Machine) sfsCells[4][5]);
        }
    };
    static final ArrayList<Machine> MachinesC = new ArrayList<Machine>() {
        {
            add((Machine) sfsCells[5][1]);
            add((Machine) sfsCells[5][3]);
            add((Machine) sfsCells[5][5]);
        }
    };


    public static Cell getCell(int y, int x){
        return sfsCells[y][x];

    }

    public static Cell[][] getSfsCells() {
        return sfsCells;
    }


    public static Cell[][] getSfsCellsLoad() {
        return sfsCellsLoad;
    }

    public static Cell[][] getSfsCellsTransformation() {
        return sfsCellsTransformation;
    }

    public static Cell[][] getSfsCellsUnload() {
        return sfsCellsUnload;
    }


    public static int[] getUnloadPosition(String Dy) {
        switch (Dy) {
            case "D1":
                return new int[]{8, 3};
            case "D2":
                return new int[]{8, 4};
            case "D3":
                return new int[]{8, 5};
            default:
                return null;
        }
    }


    public static Machine getMachineToSendPiece(String M) {
        Machine toSend;
        switch (M){
            case "Ma":
                toSend= getLowerWeight(MachinesA);
                return toSend;
            case "Mb":
                toSend= getLowerWeight(MachinesB);
                return toSend;

            case "Mc":
                toSend= getLowerWeight(MachinesC);
                return toSend;
        }
        return null;
    }

    public static Machine getLowerWeight(ArrayList<Machine> Machine) {
        Machine low = Machine.get(0);
        for(int i=0; i < Machine.size(); i++) {
            if(Machine.get(i).getWeight() < low.getWeight()) {
                low = Machine.get(i);
            }
        }

        return low;
    }

    public void readSystemTestFunction() {

        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("|"+getCell(1,0).getUnitPresence()+"  |"+getCell(1,1).getUnitPresence() +"  |"+getCell(1,2).getUnitPresence() +"  |"+getCell(1,3).getUnitPresence() +"  |"+getCell(1,4).getUnitPresence() +"  |"+getCell(1,5).getUnitPresence() +"  |"+getCell(1,6).getUnitPresence() +"  |"+ getCell(1,7).getUnitPresence()+"  |"+ getCell(1,8).getUnitPresence()+" |");
        System.out.println("| null"+"  |null  " +" |"+getCell(2,2).getUnitPresence() +" |null  " +"  |"+getCell(2,4).getUnitPresence() +" |null " +"  |"+getCell(2,6).getUnitPresence() +"  |"+ getCell(2,7).getUnitPresence()+"  |null"+" |");
        System.out.println("| null" +"  |"+getCell(3,2).getUnitPresence() +"  |"+getCell(3,3).getUnitPresence() +"  |"+getCell(3,4).getUnitPresence() +"  |"+getCell(3,5).getUnitPresence() +"  |"+getCell(3,6).getUnitPresence() +"  |"+ getCell(3,7).getUnitPresence()+"  |"+ getCell(3,8).getUnitPresence()+" |");
        System.out.println("| null"+"  |"+getCell(4,1).getUnitPresence() +"  |"+getCell(4,2).getUnitPresence() +"  |"+getCell(4,3).getUnitPresence() +"  |"+getCell(4,4).getUnitPresence() +"  |"+getCell(4,5).getUnitPresence() +"  |"+getCell(4,6).getUnitPresence() +"  |"+ getCell(4,7).getUnitPresence()+"  |"+ getCell(4,8).getUnitPresence()+" |");
        System.out.println("| null"+"  |"+getCell(5,1).getUnitPresence() +"  |"+getCell(5,2).getUnitPresence() +"  |"+getCell(5,3).getUnitPresence() +"  |"+getCell(5,4).getUnitPresence() +"  |"+getCell(5,5).getUnitPresence() +"  |"+getCell(5,6).getUnitPresence() +"  |"+ getCell(5,7).getUnitPresence()+"  |"+ getCell(5,8).getUnitPresence()+" |");
        System.out.println("| null"+"  |null  "+" |"+getCell(6,2).getUnitPresence() +" |null  " +"  |"+getCell(6,4).getUnitPresence() +" |null " +"  |"+getCell(6,6).getUnitPresence() +"  |"+ getCell(6,7).getUnitPresence()+"  |"+ "null"+" |");
        System.out.println("|"+getCell(7,0).getUnitPresence()+"  |"+getCell(7,1).getUnitPresence() +"  |"+getCell(7,2).getUnitPresence() +"  |"+getCell(7,3).getUnitPresence() +"  |"+getCell(7,4).getUnitPresence() +"  |"+getCell(7,5).getUnitPresence() +"  |"+getCell(7,6).getUnitPresence() +"  |"+ getCell(7,7).getUnitPresence()+"  |"+ getCell(7,8).getUnitPresence()+" |");
        System.out.println("---------------------------------------------------------------------------------");
    }


}