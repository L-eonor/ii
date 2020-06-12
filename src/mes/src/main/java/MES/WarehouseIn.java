package MES;

public class WarehouseIn implements Runnable{
    @Override
    public void run() {
        System.out.println("--------------[Executing] WarehouseIn is Running [Executing]--------------");

        String unitTypeString="";
        int full=1;
        int orderType=0;
        int orderID=0;
        int orderKey;
        String stringOrderKey;

        while(true){

            Cell AT2= SFS.getCell(7, 0);

            if(AT2.getUnitPresence() && full==1){
                //Get unit type
                int unitType = OPCUA_Connection.getValueInt("MAIN_TASK", "AT2.UNIT_TYPE");
                unitTypeString = "P"+unitType;

                //Get orderID receiving
                orderKey = OPCUA_Connection.getValueInt("MAIN_TASK", "AT2.ORDER_TYPE");
                stringOrderKey = String.valueOf(orderKey);

                updateOrder(orderKey);

                // Incrementar Delete History depois de enviar para DB - Verificar posição
                int deleteHistory=0;
                try{
                    deleteHistory = OPCUA_Connection.getValueInt("MAIN_TASK", "DELETE_HISTORY");
                }
                catch (Exception e){
                }
                try {
                    OPCUA_Connection.setValueInt("MAIN_TASK", "DELETE_HISTORY", ++deleteHistory);
                }
                catch (Exception e){
                }

                try {
                    orderType = Integer.parseInt(stringOrderKey.substring(0, 1)); // to identify if its TransformationOrder or a LoadOrder
                    orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in find ID in lists
                }
                catch (Exception e){
                }

                //Compares HashMap_received_units with units_sent and updates or terminates order if last unit arrived
                if(orderType==1) {

                    for (int i = 0; i < Main.orderListTransformationEnded.size(); i++) {
                        if ((Main.orderListTransformationEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) == Main.orderListTransformationEnded.get(i).getNTotal())) {
                            Main.orderListTransformationEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                            Main.orderListTransformationEnded.get(i).setEndTime(StopWatch.getTimeElapsed());
                            Main.orderListTransformationEnded.get(i).setStatus(4);

                            dbConnection.updateStatus_OrderTransformDB(Main.orderListTransformationEnded.get(i).getId(), 3);

                            System.out.println("Order Transformation ended in AT2 -> "+ Main.orderListTransformationEnded.get(i));
                        } else if ((Main.orderListTransformationEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) != Main.orderListTransformationEnded.get(i).getNTotal())) {
                            Main.orderListTransformationEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));


                        }
                    }
                }
                else if(orderType==3) {
                    for (int i = 0; i < Main.orderListLoad.size(); i++){
                        if(Main.orderListLoad.get(i).getId() == orderID && Main.orderListLoad.get(i).getUnitsReachedEnd() == 0){
                            Main.orderListLoad.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                            Main.orderListLoad.get(i).setEndTime(StopWatch.getTimeElapsed());
                            Main.orderListLoad.get(i).setStatus(4);
                            System.out.println("Order Load ended in AT2 -> " + Main.orderListLoad.get(i));
                        }
                    }
                }

                full=2;
            }
            if(!AT2.getUnitPresence() && full==2){
                full=1;
                //Atualizar contagem no Armazém
                Warehouse.addPiece(unitTypeString);
            }

        }


    }

    private static void updateOrder(int orderKey) {
        // If present in Hash Table updates otherwise creates new key with value 1
        //System.out.println(Main.receivedOrderPieces.containsKey(orderID));

        if(Main.receivedOrderPieces.containsKey(orderKey)){
            Main.receivedOrderPieces.computeIfPresent(orderKey, (k, v) -> v + 1);
        }
        else {
            Main.receivedOrderPieces.put(orderKey,1);
        }

        System.out.println(Main.receivedOrderPieces.get(orderKey));

        String stringOrderKey = String.valueOf(orderKey);
        int orderType=0;
        int orderID=0;

        try {
            orderType = Integer.parseInt(stringOrderKey.substring(0, 1)); // to identify if its TransformationOrder or a LoadOrder
            orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in find ID in lists
        }
        catch (Exception e){
            System.out.println(e);
        }

        //Update order in db
        if (orderType == 1) {
            dbConnection.updateNDone_OrderTransformationDB(orderID, Main.receivedOrderPieces.get(orderKey));
            System.out.println(String.format("Order not done -> ID: %d | N: %d", orderID,  Main.receivedOrderPieces.get(orderKey)));
        }

        if(orderType == 3) {
            dbConnection.updateStatus_OrderLoadDB(orderID, 3);
        }
    }

    public static void missedPieces(){
        //Ler peças que possam ter chegado
        int deleteHistory=0;

        try {
            deleteHistory = OPCUA_Connection.getValueInt("MAIN_TASK", "DELETE_HISTORY");
        }
        catch (Exception e){
        }

        int orderKey=0;
        int i;
        for(i = 0; i < 20; i++){

            //String completeUnitType = "AT2.COMPLETE["+i+"].unit_type";
            String completeOrderType = "AT2.COMPLETE["+i+"].order_type";
            try {
                orderKey = OPCUA_Connection.getValueInt("MAIN_TASK", completeOrderType);
            }
            catch (Exception e){
            }

            if(orderKey == 0) break;
            else updateOrder(orderKey);

        }

        try {
            OPCUA_Connection.setValueInt("MAIN_TASK", "DELETE_HISTORY", (deleteHistory + i));
        }
        catch (Exception e){
        }
    }

}
