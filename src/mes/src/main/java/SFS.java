public class SFS {

    //Attributes
    //public Cell[][] sfsCells = new Cell[8][9];


    static public Cell[][] sfsCells = {
            {null, null, null, null, null, null, null, null},
            {new Cell(1,0), new Cell(1,1), new Rotator(1,2), new Cell(1,3), new Rotator(1,4), new Cell(1,5), new Rotator(1,6), new Rotator(1,7), new Cell(1,8)},
            {null, null, new Cell(2,2), null, new Cell(2,4), null, new Cell(2,6), new Cell(2,7), null},
            {null, new Machine(3, 1), new Rotator(3, 2), new Machine(3, 3), new Rotator(3, 4), new Machine(3, 5), new Rotator(3, 6), new Pusher(3, 7), new Slider(3, 8)},
            {null, new Machine(4, 1), new Rotator(4, 2), new Machine(4, 3), new Rotator(4, 4), new Machine(4, 5), new Rotator(4, 6), new Pusher(4, 7), new Slider(4, 8)},
            {null, new Machine(5, 1), new Rotator(5, 2), new Machine(5, 3), new Rotator(5, 4), new Machine(5, 5), new Rotator(5, 6), new Pusher(5, 7), new Slider(5, 8)},
            {null, null, new Cell(6,2), null, new Cell(6,4), null, new Cell(6,6), new Cell(6,7), null},
            {new Cell(7,0), new Cell(7,1), new Rotator(7,2), new Cell(7,3), new Rotator(7,4), new Cell(7,5), new Rotator(7,6), new Rotator(7,7), new Cell(7,8)},
    };

    //Constructor
    public SFS() {
        //initializeSfs();
    }
    //Methods


    /**
     * Initializes Shop Floor Simulator Cells.
     */
    public void initializeSfs() {

        for(int y=0; y < 7; y++) {
            for(int x=0; x < 9; x++) {
                // Case of Conveyor
                if( ((x==0) && (y==1)) || ((x==0) && (y==6)) || ((x==1) && (y==0)) || ((x==1) && (y==6)) || ((x==2) && (y==1)) || ((x==2) && (y==5)) || ((x==3) && (y==0)) ||
                        ((x==3) && (y==6)) || ((x==4) && (y==1)) || ((x==4) && (y==5)) || ((x==5) && (y==0)) || ((x==5) && (y==6)) || ((x==6) && (y==1)) || ((x==6) && (y==5)) ||
                        ((x==7) && (y==1)) || ((x==7) && (y==5)) || ((x==8) && (y==0)) || ((x==8) && (y==6))) {

                    sfsCells[y][x] = new Cell(y,x);
                }
                // Case of Rotator
                else if(((x==2) && (y==0)) || ((x==2) && (y==2)) || ((x==2) && (y==3)) || ((x==2) && (y==4)) || ((x==2) && (y==6)) ||
                        ((x==4) && (y==0)) || ((x==4) && (y==2)) || ((x==4) && (y==3)) || ((x==4) && (y==4)) || ((x==4) && (y==6))  ||
                        ((x==6) && (y==0)) || ((x==6) && (y==2)) || ((x==6) && (y==3)) || ((x==6) && (y==4)) || ((x==6) && (y==6)) ||
                        ((x==7) && (y==0)) || ((x==7) && (y==6)) ) {

                    sfsCells[y][x] = new Rotator(y,x);
                }
                // Case of Machine
                else if(((x==1) && (y==2)) || ((x==1) && (y==3)) || ((x==1) && (y==4)) ||
                        ((x==3) && (y==2)) || ((x==3) && (y==3)) || ((x==3) && (y==4)) ||
                        ((x==5) && (y==2)) || ((x==5) && (y==3)) || ((x==5) && (y==4))) {

                    sfsCells[y][x] = new Machine(y,x);
                }
                else if (((x==7) && (y==2)) || ((x==7) && (y==3)) || ((x==7) && (y==4))) {

                    sfsCells[y][x] = new Pusher(y,x);
                }
                else {
                    sfsCells[y][x] = null;
                }
            }
        }
    }
}
