package MES;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SFS {

    //Attributes
    ArrayList<Machine> MachinesA = new ArrayList<>();
    ArrayList<Machine> MachinesB = new ArrayList<>();
    ArrayList<Machine> MachinesC = new ArrayList<>();

    static public Cell[][] sfsCells = {
            {null, null, null, null, null, null, null, null, null},
            {new Cell(1, 0, "WarehouseOut"), new Cell(1, 1), new Rotator(1, 2), new Cell(1, 3), new Rotator(1, 4), new Cell(1, 5), new Rotator(1, 6), new Rotator(1, 7), new Cell(1, 8)},
            {null, null, new Cell(2, 2), null, new Cell(2, 4), null, new Cell(2, 6), new Cell(2, 7), null},
            {null, new Machine(3, 1), new Rotator(3, 2), new Machine(3, 3), new Rotator(3, 4), new Machine(3, 5), new Rotator(3, 6), new Pusher(3, 7), new Slider(3, 8)},
            {null, new Machine(4, 1), new Rotator(4, 2), new Machine(4, 3), new Rotator(4, 4), new Machine(4, 5), new Rotator(4, 6), new Pusher(4, 7), new Slider(4, 8)},
            {null, new Machine(5, 1), new Rotator(5, 2), new Machine(5, 3), new Rotator(5, 4), new Machine(5, 5), new Rotator(5, 6), new Pusher(5, 7), new Slider(5, 8)},
            {null, null, new Cell(6, 2), null, new Cell(6, 4), null, new Cell(6, 6), new Cell(6, 7), null},
            {new Cell(7, 0, "WarehouseIn"), new Cell(7, 1), new Rotator(7, 2), new Cell(7, 3), new Rotator(7, 4), new Cell(7, 5), new Rotator(7, 6), new Rotator(7, 7), new Cell(7, 8)},
    };

    //Constructor
    public SFS() {
        //Add machines to respective list
        MachinesA.add((Machine) getCell(3,1));
        MachinesA.add((Machine) getCell(3,3));
        MachinesA.add((Machine) getCell(3,5));
        MachinesB.add((Machine) getCell(4,1));
        MachinesB.add((Machine) getCell(4,3));
        MachinesB.add((Machine) getCell(4,5));
        MachinesC.add((Machine) getCell(5,1));
        MachinesC.add((Machine) getCell(5,3));
        MachinesC.add((Machine) getCell(5,5));
    }

    public Cell getCell(int y, int x){
        if(sfsCells[y][x] != null) return sfsCells[y][x];
        else return null;
    }

    public int[] getUnloadPosition(String Dy) {
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

    public Machine getMachineToSendPiece(String M) {
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

    public Machine getLowerWeight(ArrayList<Machine> Machine) {
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