package MES;

public class WarehouseIn implements Runnable{
    @Override
    public void run() {
        System.out.println("--------------[Executing] WarehouseIn is Running [Executing]--------------");

        int full=1;
        int orderType;
        int orderID;
        int orderKey;
        String stringOrderKey;

        while(true){

            Cell AT2= SFS.getCell(7, 0);

            if(AT2.getUnitPresence() && full==1){
                //Get orderID receiving
                orderKey = OPCUA_Connection.getValueInt("MAIN_TASK", "AT2.ORDER_TYPE");
                stringOrderKey = String.valueOf(orderKey);

                updateOrder(orderKey);

                orderType = Integer.parseInt(stringOrderKey.substring(0, 1)); // to identify if its TransformationOrder or a LoadOrder
                orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in find ID in lists

                //Compares HashMap_received_units with units_sent and updates or terminates order if last unit arrived
                if(orderType==1) {
                    for (int i = 0; i < Main.orderListTransformationEnded.size(); i++) {
                        if ((Main.orderListTransformationEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) == Main.orderListTransformationEnded.get(i).getNTotal())) {
                            Main.orderListTransformationEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                            Main.orderListTransformationEnded.get(i).setEndTime(StopWatch.getTimeElapsed());
                            Main.orderListTransformationEnded.get(i).setStatus(4);
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
            }

        }


    }

    private void updateOrder(int orderKey) {
        // If present in Hash Table updates otherwise creates new key with value 1
        //System.out.println(Main.receivedOrderPieces.containsKey(orderID));
        if(Main.receivedOrderPieces.containsKey(orderKey)){
            Main.receivedOrderPieces.computeIfPresent(orderKey, (k, v) -> v + 1);
        }
        else {
            Main.receivedOrderPieces.put(orderKey,1);
        }
    }
}
