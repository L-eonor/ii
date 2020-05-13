package MES;

public class PusherThread3 implements Runnable {
    @Override
    public void run() {

        System.out.println("--------------[Executing] PusherThread3 is Running [Executing]--------------");

        int full3=1;
        int orderID;
        int orderKey;
        String stringOrderKey;

        while(true){

            Pusher p3= (Pusher) SFS.getCell(5, 7);

            if(p3.isPushing() && full3==1){
                //Get orderID receiving
                orderKey = p3.getOrderPushing();
                stringOrderKey = String.valueOf(orderKey);

                updateOrder(orderKey);

                orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in unload lists

                //Compares HashMap_received_units with units_sent and updates or terminates order if last unit arrived
                for (int i=0; i < Main.orderListUnloadEnded.size(); i++){
                    if((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) == Main.orderListUnloadEnded.get(i).getQuantity())){
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                        Main.orderListUnloadEnded.get(i).setEndTime(StopWatch.getTimeElapsed());
                        Main.orderListUnloadEnded.get(i).setStatus(4);
                        System.out.println("Order Unload fim no Pusher 3.");
                        System.out.println(Main.orderListUnloadEnded.get(i));
                    }
                    else if ((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) != Main.orderListUnloadEnded.get(i).getQuantity())) {
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                    }
                }

                full3=2;
            }
            if(!p3.isPushing() && full3==2){
                full3=1;
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
