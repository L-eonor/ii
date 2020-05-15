package MES;

public class PusherThread1 implements Runnable {
    @Override
    public void run() {

        System.out.println("--------------[Executing] PusherThread1 is Running [Executing]--------------");

        int full1=1;
        int orderID;
        int orderKey;
        String stringOrderKey;

        while(true){
            Pusher p1= (Pusher) SFS.getCell(3, 7);

            if(p1.isPushing() && full1==1){
                //Get orderID receiving
                orderKey = p1.getOrderPushing();
                stringOrderKey = String.valueOf(orderKey);

                updateOrder(orderKey);

                orderID = Integer.parseInt(stringOrderKey.substring(1)); // to use in unload lists

                //Compares HashMap_received_units with units_sent and updates or terminates order if last unit arrived
                for (int i=0; i < Main.orderListUnloadEnded.size(); i++){
                    if((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) == Main.orderListUnloadEnded.get(i).getQuantity())){
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                        Main.orderListUnloadEnded.get(i).setEndTime(StopWatch.getTimeElapsed());
                        Main.orderListUnloadEnded.get(i).setStatus(4);
                        System.out.println("Order Unload fim no Pusher 1 -> " + Main.orderListUnloadEnded.get(i));
                    }
                    else if ((Main.orderListUnloadEnded.get(i).getId() == orderID) && (Main.receivedOrderPieces.get(orderKey) != Main.orderListUnloadEnded.get(i).getQuantity())) {
                        Main.orderListUnloadEnded.get(i).setUnitsReachedEnd(Main.receivedOrderPieces.get(orderKey));
                    }
                }

                full1=2;
            }
            if(!p1.isPushing() && full1==2){
                full1=1;
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

