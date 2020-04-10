public class SFS {

    //Attributes
    public Cell[][] sfsCells = new Cell[7][9];

    //Constructor
    public SFS() {
        initializeSfs();
    }
    //Methods


    /**
     * Initializes Shop Floor Simulator Cells.
     */
    public void initializeSfs() {

        for(int y=0; y < 7; y++) {
            for(int x=0; x < 9; x++) {
                // Case of Conveyor
                if( ((x==0) && (y==0)) || ((x==0) && (y==6)) || ((x==1) && (y==0)) || ((x==1) && (y==6)) || ((x==2) && (y==1)) || ((x==2) && (y==5)) || ((x==3) && (y==0)) ||
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
