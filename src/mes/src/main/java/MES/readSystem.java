package MES;

public class readSystem implements Runnable {

    private String orderStringID1 = "N00";
    private int orderID1;

    public readSystem() {
    }

    public void run(){
        System.out.println("--------------[Executing] ReadSystem is Running [Executing]--------------");

        while(true) {

            /* --------------------------- COLLUMN 0 --------------------------------*/
            //WarehouseOut AT1
            Cell AT1 = SFS.getCell(1, 0);
            AT1.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "AT1._FREE.x"));
            //WarehouseIn AT2
            Cell AT2 = SFS.getCell(7, 0);
            AT2.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "AT2.EMPTY.x"));
            /* --------------------------- COLLUMN 1 --------------------------------*/
            //Conveyor C1T1
            Cell C1T1 = SFS.getCell(1, 1);
            C1T1.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C1T1.FREE.x"));
            //Machine C1T3
            Machine C1T3 = (Machine) SFS.getCell(3, 1);
            C1T3.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C1T3.FREE.x"));
            //C1T3.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C1T3.CURRENT_TOOL"));
            //C1T3.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C1T3.PROCESSING_TIME"));
            //Machine C1T4
            Machine C1T4 = (Machine) SFS.getCell(4, 1);
            C1T4.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C1T4.FREE.x"));
            //C1T4.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C1T4.CURRENT_TOOL"));
            //C1T4.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C1T4.PROCESSING_TIME"));
            //Machine C1T5
            Machine C1T5 = (Machine) SFS.getCell(5, 1);
            C1T5.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C1T5.FREE.x"));
            //C1T5.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C1T5.CURRENT_TOOL"));
            //C1T5.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C1T5.PROCESSING_TIME"));
            ////Conveyor C1T7
            Cell C1T7 = SFS.getCell(7, 1);
            C1T7.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C1T7.FREE.x"));
            /* --------------------------- COLLUMN 2 --------------------------------*/
            //Rotator
            Rotator C2T1 = (Rotator) SFS.getCell(1, 2);
            C2T1.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C2T1.EMPTY.x"));

            //Conveyor
            Cell C2T2 = SFS.getCell(2, 2);
            C2T2.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C2T2.FREE.x"));
            //Rotator
            Rotator C2T3 = (Rotator) SFS.getCell(3, 2);
            C2T3.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C2T3.EMPTY.x"));

            //Rotator
            Rotator C2T4 = (Rotator) SFS.getCell(4, 2);
            C2T4.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C2T4.EMPTY.x"));

            //Rotator
            Rotator C2T5 = (Rotator) SFS.getCell(5, 2);
            C2T5.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C2T5.EMPTY.x"));

            //Conveyor
            Cell C2T6 = SFS.getCell(6, 2);
            C2T6.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C2T6.FREE.x"));
            //Rotator
            Rotator C2T7 = (Rotator) SFS.getCell(7, 2);
            C2T7.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C2T7.EMPTY.x"));

            /* --------------------------- COLLUMN 3 --------------------------------*/
            //Conveyor C3T1
            Cell C3T1 = SFS.getCell(1, 3);
            C3T1.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C3T1.FREE.x"));
            //Machine C3T3
            Machine C3T3 = (Machine) SFS.getCell(3, 3);
            C3T3.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C3T3.FREE.x"));
            //C3T3.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C3T3.CURRENT_TOOL"));
            //C3T3.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C3T3.PROCESSING_TIME"));
            //Machine C3T4
            Machine C3T4 = (Machine) SFS.getCell(4, 3);
            C3T4.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C3T4.FREE.x"));
            //C3T4.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C3T4.CURRENT_TOOL"));
            //C3T4.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C3T4.PROCESSING_TIME"));
            //Machine C3T5
            Machine C3T5 = (Machine) SFS.getCell(5, 3);
            C3T5.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C3T5.FREE.x"));
            //C3T5.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C3T5.CURRENT_TOOL"));
            //C3T5.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C3T5.PROCESSING_TIME"));
            ////Conveyor C1T7
            Cell C3T7 = SFS.getCell(7, 3);
            C3T7.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C3T7.FREE.x"));
            /* --------------------------- COLLUMN 4 --------------------------------*/
            //Rotator
            Rotator C4T1 = (Rotator) SFS.getCell(1, 4);
            C4T1.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C4T1.EMPTY.x"));

            //Conveyor
            Cell C4T2 = SFS.getCell(2, 4);
            C4T2.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C4T2.FREE.x"));
            //Rotator
            Rotator C4T3 = (Rotator) SFS.getCell(3, 4);
            C4T3.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C4T3.EMPTY.x"));

            //Rotator
            Rotator C4T4 = (Rotator) SFS.getCell(4, 4);
            C4T4.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C4T4.EMPTY.x"));

            //Rotator
            Rotator C4T5 = (Rotator) SFS.getCell(5, 4);
            C4T5.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C4T5.EMPTY.x"));

            //Conveyor
            Cell C4T6 = SFS.getCell(6, 4);
            C4T6.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C4T6.FREE.x"));
            //Rotator
            Rotator C4T7 = (Rotator) SFS.getCell(7, 4);
            C4T7.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C4T7.EMPTY.x"));

            /* --------------------------- COLLUMN 5 --------------------------------*/
            //Conveyor C5T1
            Cell C5T1 = SFS.getCell(1, 5);
            C5T1.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C5T1.FREE.x"));
            //Machine C5T3
            Machine C5T3 = (Machine) SFS.getCell(3, 5);
            C5T3.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C5T3.FREE.x"));
            //C5T3.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C5T3.CURRENT_TOOL"));
            //C5T3.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C5T3.PROCESSING_TIME"));
            //Machine C5T4
            Machine C5T4 = (Machine) SFS.getCell(4, 5);
            C5T4.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C5T4.FREE.x"));
            //C5T4.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C5T4.CURRENT_TOOL"));
            //C5T4.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C5T4.PROCESSING_TIME"));
            //Machine C3T5
            Machine C5T5 = (Machine) SFS.getCell(5, 5);
            C5T5.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C5T5.FREE.x"));
            //C5T5.setTool(OPCUA_Connection.getValueInt("MAIN_TASK", "C5T5.CURRENT_TOOL"));
            //C5T5.setProcessTime(OPCUA_Connection.getValueInt("MAIN_TASK", "C5T5.PROCESSING_TIME"));
            ////Conveyor C5T7
            Cell C5T7 = SFS.getCell(7, 5);
            C5T7.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C5T7.FREE.x"));
            /* --------------------------- COLLUMN 6 --------------------------------*/
            //Rotator
            Rotator C6T1 = (Rotator) SFS.getCell(1, 6);
            C6T1.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C6T1.EMPTY.x"));

            //Conveyor
            Cell C6T2 = SFS.getCell(2, 6);
            C6T2.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C6T2.FREE.x"));
            //Rotator
            Rotator C6T3 = (Rotator) SFS.getCell(3, 6);
            C6T3.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C6T3.EMPTY.x"));

            //Rotator
            Rotator C6T4 = (Rotator) SFS.getCell(4, 6);
            C6T4.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C6T4.EMPTY.x"));

            //Rotator
            Rotator C6T5 = (Rotator) SFS.getCell(5, 6);
            C6T5.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C6T5.EMPTY.x"));

            //Conveyor
            Cell C6T6 = SFS.getCell(6, 6);
            C6T6.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C6T6.FREE.x"));
            //Rotator
            Rotator C6T7 = (Rotator) SFS.getCell(7, 6);
            C6T7.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C6T7.EMPTY.x"));

            /* --------------------------- COLLUMN 7 --------------------------------*/
            //Rotator C7T1a
            Rotator C7T1a = (Rotator) SFS.getCell(1, 7);
            C7T1a.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T1a.EMPTY.x"));

            //Conveyor
            Cell C7T2 = SFS.getCell(2, 7);
            C7T2.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T2.FREE.x"));
            //Pusher
            Pusher C7T3 = (Pusher) SFS.getCell(3, 7);
            C7T3.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T3.EMPTY.x"));
            C7T3.setUnitTypePushing(OPCUA_Connection.getValueInt("MAIN_TASK", "C7T3.UNIT_TYPE")); // set type pushing
            C7T3.setOrderPushing(OPCUA_Connection.getValueInt("MAIN_TASK", "C7T3.ORDER_TYPE")); //set order type and id pushing
            C7T3.setPushing(OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T3.PUSH_N"));
            //System.out.println(C7T3.isPushing()+" "+C7T3.getOrderPushing()+" "+C7T3.getUnitTypePushing());

            //Pusher
            Pusher C7T4 = (Pusher) SFS.getCell(4, 7);
            C7T4.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T4.EMPTY.x"));
            C7T4.setUnitTypePushing(OPCUA_Connection.getValueInt("MAIN_TASK", "C7T4.UNIT_TYPE")); // set type pushing
            C7T4.setOrderPushing(OPCUA_Connection.getValueInt("MAIN_TASK", "C7T4.ORDER_TYPE")); //set order type and id pushing
            C7T4.setPushing(OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T4.PUSH_N"));
            //System.out.println(C7T4.isPushing()+" "+C7T4.getOrderPushing()+" "+C7T4.getUnitTypePushing());

            //Pusher
            Pusher C7T5 = (Pusher) SFS.getCell(5, 7);
            C7T5.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T5.EMPTY.x"));
            C7T5.setUnitTypePushing(OPCUA_Connection.getValueInt("MAIN_TASK", "C7T5.UNIT_TYPE")); // set type pushing
            C7T5.setOrderPushing(OPCUA_Connection.getValueInt("MAIN_TASK", "C7T5.ORDER_TYPE")); //set order type and id pushing
            C7T5.setPushing(OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T5.PUSH_N"));
            //System.out.println(C7T5.isPushing()+" "+C7T5.getOrderPushing()+" "+C7T5.getUnitTypePushing());

            //Conveyor
            Cell C7T6 = SFS.getCell(6, 7);
            C7T6.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T6.FREE.x"));
            //Rotator C7T7a
            Rotator C7T7a = (Rotator) SFS.getCell(7, 7);
            C7T7a.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T7a.EMPTY.x"));

            /* --------------------------- COLLUMN 8--------------------------------*/
            //LoadIn C7T1b
            Cell C7T1b = SFS.getCell(1, 8);
            C7T1b.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T1b.Init.x"));
            //LoadIn C7T7b
            Cell C7T7b = SFS.getCell(7, 8);
            C7T7b.setUnitPresence(!OPCUA_Connection.getValueBoolean("MAIN_TASK", "C7T7b.Init.x"));

            /* ---------------------------- SLIDERS ---------------------------------*/
            //Slider PM1 PM11
            Slider PM1 = (Slider) SFS.getCell(3,8);
            PM1.setSensorLevel4(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM11"));
            PM1.setSensorLevel3(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM12"));
            PM1.setSensorLevel2(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM13"));
            //Slider PM2
            Slider PM2 = (Slider) SFS.getCell(4,8);
            PM2.setSensorLevel4(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM21"));
            PM2.setSensorLevel3(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM22"));
            PM2.setSensorLevel2(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM23"));

            //Slider PM3
            Slider PM3 = (Slider) SFS.getCell(5,8);
            PM3.setSensorLevel4(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM31"));
            PM3.setSensorLevel3(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM32"));
            PM3.setSensorLevel2(OPCUA_Connection.getValueBoolean("MAIN_TASK", "PM33"));

            /* ---------------------------- UNITS Warehouse ---------------------------------*/


            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P1"),"P1");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P2"),"P2");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P3"),"P3");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P4"),"P4");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P5"),"P5");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P6"),"P6");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P7"),"P7");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P8"),"P8");
            Warehouse.setPiece(OPCUA_Connection.getValueInt("MAIN_TASK", "ARMAZEM.P9"),"P9");




        }


    }


}