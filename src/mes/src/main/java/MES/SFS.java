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
        return sfsCells[y][x];
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

}