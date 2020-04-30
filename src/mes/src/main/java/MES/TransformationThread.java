package MES;

import java.lang.reflect.Array;
import java.util.List;

public class TransformationThread implements Runnable {

    //Attributes
    static int[] warehouseOut = {1, 1};
    static int[] warehouseIn = {0, 7};
    int unitCount=0;
    TransformationsGraph transformTable = new TransformationsGraph();
    List<orderTransform> orderListTransformation=Main.orderListTransformation;
    SFS floor = Main.floor;

    public TransformationThread(){

    }

    public void run(){
        System.out.println("--------------[Executing] TransformationThread is Running [Executing]--------------");

        while(true) {
            if(!orderListTransformation.isEmpty()){

                orderTransform  order= orderListTransformation.remove(0);
                //Order attributes
                int orderMaxDelay = order.getMaxDelay();
                int orderUnits = order.getNTotal();
                String orderPx = order.getPx();
                String orderPy = order.getPy();

                for (int a=0; a < orderUnits ; a++) {
                    if (transformTable.searchTransformations(orderPx, orderPy)) {
                        System.out.println("Searched transformations. Found " + transformTable.solutions.size() + " solutions.");
                        /* prints all transformations
                        Iterator value = transformTable.solutions.iterator();
                        while(value.hasNext()) {
                            System.out.println(transformTable.solutions.poll());
                        }*/

                    } else System.out.println(" No need for transformations. ");


                    //String with the whole path of the Transformation order
                    StringBuilder pathString = new StringBuilder();

                    GraphSolution transformationResult = transformTable.solutions.poll();
                    if (transformationResult == null)
                        throw new AssertionError("Error: transformationResult null pointer. ");

                    int[] startTransformation = warehouseOut;
                    for (int j = 0; j < transformationResult.transformations.size(); j++) {
                        //Identify machine
                        String machine = transformationResult.machines.get(j);
                        //Relate machine type with respective position
                        Machine machineToGo = floor.getMachineToSendPiece(machine);
                        int[] goal = machineToGo.getPosition();
                        if (goal == null) System.out.println("Error: order machine input not valid. ");
                        else {
                            machineToGo.addWeight();
                        }
                        //Calculate path to Machine
                        Path_Logic path = new Path_Logic(startTransformation, goal);
                        pathString.append(path.getStringPath());
                        //Add Tool and Time to string respectively
                        pathString.append(transformationResult.tool.get(j)).append(transformationResult.timeCost.get(j));
                        startTransformation = goal;
                    }
                    //Path to Warehouse In cell

                    Path_Logic pathEnd = new Path_Logic(startTransformation, warehouseIn);
                    pathString.append(pathEnd.getStringPath());

                    System.out.println("[Transformation] Esta é a string: " + pathString);
                    OPCUA_Connection.setValueString("MAIN_TASK", "AT1_order_path_mes", pathString.toString());
                    OPCUA_Connection.setValueInt("MAIN_TASK", "unit_type", unitTypeIdentifier(orderPx));
                    OPCUA_Connection.setValueInt("MAIN_TASK", "UNIT_COUNT_MES", ++unitCount);


                }
            }
        }

    }

    public int unitTypeIdentifier(String Px){
        switch (Px){
            case "P1": return 1;
            case "P2": return 2;
            case "P3": return 3;
            case "P4": return 4;
            case "P5": return 5;
            case "P6": return 6;
            case "P7": return 7;
            case "P8": return 8;
            case "P9": return 9;
            default: return 0;
        }
    }
}