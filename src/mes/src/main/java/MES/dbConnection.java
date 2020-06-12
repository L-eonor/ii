package MES;

import javax.swing.*;
import java.sql.*;
import java.util.Vector;

public class dbConnection {

    /**Exemplos de Querys
     * UPDATE `orderListUnload`  SET `end_time` = `start_time` + interval 30 second WHERE id=3; -> Alterar o tempo que lá está
     */

    /** SUCCESS */
    public static final int SUCCESS = 0;

    /** ERROR */
    public static final int ERROR = -1;

    /** ALREADY_EXISTS */
    public static final int ALREADY_EXISTS = 1;


    public static Connection connect() {
        Connection conn = null;
        try {
            conn =  DriverManager.getConnection("jdbc:mysql://db.fe.up.pt/up201504515", "up201504515" , "nu8maaM6r");
            //System.out.println("Connected to the MYSQL server successfully.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return conn;
    }

    public static Vector<Vector<Object>> readOrderListUnloadDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from orderListUnload WHERE status NOT LIKE 'concluida' ORDER BY id ");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {

                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(6));
                row.add(rsSelect.getString(7));
                row.add(rsSelect.getString(8));
                row.add(rsSelect.getString(9));
                row.add(rsSelect.getString(10));
                row.add(rsSelect.getString(11));


                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Vector<Vector<Object>> readHistoricOrderListUnloadDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from orderListUnload WHERE status LIKE 'concluida' ORDER BY id");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {
                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(4));
                row.add(rsSelect.getString(7));
                row.add(rsSelect.getString(8));
                row.add(rsSelect.getString(9));
                row.add(rsSelect.getString(12));



                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Vector<Vector<Object>> readOrderListTransformationDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from orderListTransformation WHERE status NOT LIKE 'concluida' ORDER BY id");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {

                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(6));
                row.add(rsSelect.getString(7));
                row.add(rsSelect.getString(8));
                row.add(rsSelect.getString(9));
                row.add(rsSelect.getString(10));
                row.add(rsSelect.getString(11));
                row.add(rsSelect.getString(12));
                row.add(rsSelect.getString(13));
                row.add(rsSelect.getString(15));


                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Vector<Vector<Object>> readHistoricOrderListTransformationDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from orderListTransformation WHERE status LIKE 'concluida' ORDER BY id");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {

                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(4));
                row.add(rsSelect.getString(7));
                row.add(rsSelect.getString(8));
                row.add(rsSelect.getString(9));
                row.add(rsSelect.getString(14));
                row.add(rsSelect.getString(15));
                row.add(rsSelect.getString(16));

                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static int insertNew_OrderUnloadDB(int id, String Px, String Dy, int qTotal) {
        try {
            Connection con = dbConnection.connect();

            //Mensagens de erro, checks etc por implementar
            /*
            String statementSelect = String.format("SELECT count(*) from orderUnload where orderNumber = '%d'",
                    orderNumber);

            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            rsSelect.next();

            if (rsSelect.getInt(1) != 0) {
                return ALREADY_EXISTS;
            }
            */
            /*
            String statusText;
            if (1 == status) statusText = "por_iniciar";
            else if (2 == status) statusText = "em_processamento";
            else if (3 == status) statusText = "concluida";
            else statusText = "ERRO";
            */

            String statement = String.format(
                    "INSERT INTO `orderListUnload` (`id`, `Px`, `Dy`, `quantity_total`) " +
                            "VALUES ('%d', '%s', '%s', '%d')",
                    id, Px, Dy, qTotal);


            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int insertNew_OrderTransformationDB(int id, String Px, String Py, int qTotal, int maxDelay) {
        try {
            Connection con = dbConnection.connect();

            //Mensagens de erro, checks etc por implementar
            /*
            String statementSelect = String.format("SELECT count(*) from orderUnload where orderNumber = '%d'",
                    orderNumber);

            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            rsSelect.next();

            if (rsSelect.getInt(1) != 0) {
                return ALREADY_EXISTS;
            }
            */
            /*
            String statusText;
            if (1 == status) statusText = "por_iniciar";
            else if (2 == status) statusText = "em_processamento";
            else if (3 == status) statusText = "concluida";
            else statusText = "ERRO";
            */

            String statement = String.format(
                    "INSERT INTO `orderListTransformation` (`id`, `Px`, `Py`, `quantity_total`, `max_delay` ) " +
                            "VALUES ('%d', '%s', '%s', '%d', '%d')",
                    id, Px, Py, qTotal, maxDelay);


            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int updateNDone_OrderUnloadDB(int id, int qDone) {
        try {
            Connection con = dbConnection.connect();
            String statement = String.format(
                    "UPDATE `orderListUnload` SET `quantity_sent` = '%d' " +
                            "WHERE `orderListUnload`.`id` = '%d'",
                    qDone, id);

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int updateStatus_OrderUnloadDB(int id, int status) {
        try {
            Connection con = dbConnection.connect();

            String statusText;
            String statement;

            if (2 == status || 3 == status) {
                if (2 == status) {
                    statusText = "em_processamento";

                    statement = String.format(
                            "UPDATE `orderListUnload` SET `status` = '%s', `start_time` = CURRENT_TIMESTAMP " +
                                    "WHERE `orderListUnload`.`id` = '%d'",
                            statusText, id);
                }
                else {
                    statusText = "concluida";

                    statement = String.format(
                            "UPDATE `orderListUnload` SET `status` = '%s', `end_time` = CURRENT_TIMESTAMP " +
                                    "WHERE `orderListUnload`.`id` = '%d'",
                            statusText, id);
                }
            }
            else {
                if (1 == status) statusText = "por_iniciar";
                else if (4 == status) statusText = "em_pausa";
                else statusText = "ERRO";

                statement = String.format(
                        "UPDATE `orderListUnload` SET `status` = '%s' " +
                                "WHERE `orderListUnload`.`id` = '%d'",
                        statusText, id);

            }

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int updateStatus_OrderTransformDB(int id, int status) {
        try {
            Connection con = dbConnection.connect();

            String statusText;
            String statement;

            if (2 == status || 3 == status) {
                if (2 == status) {
                    statusText = "em_processamento";

                    statement = String.format(
                            "UPDATE `orderListTransformation` SET `status` = '%s', `start_time` = CURRENT_TIMESTAMP " +
                                    "WHERE `orderListTransformation`.`id` = '%d'",
                            statusText, id);
                }
                else {
                    statusText = "concluida";

                    statement = String.format(
                            "UPDATE `orderListTransformation` SET `status` = '%s', `end_time` = CURRENT_TIMESTAMP " +
                                    "WHERE `orderListTransformation`.`id` = '%d'",
                            statusText, id);
                }
            }
            else {
                if (1 == status) statusText = "por_iniciar";
                else if (4 == status) statusText = "em_pausa";
                else statusText = "ERRO";

                statement = String.format(
                        "UPDATE `orderListTransformation` SET `status` = '%s' " +
                                "WHERE `orderListTransformation`.`id` = '%d'",
                        statusText, id);

            }

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int updateNDone_OrderTransformationDB(int id, int qDone) {
        try {
            Connection con = dbConnection.connect();
            String statement = String.format(
                    "UPDATE `orderListTransformation` SET `quantity_done` = '%d' " +
                            "WHERE `orderListTransformation`.`id` = '%d'",
                    qDone, id);

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int updateNSent_OrderTransformationDB(int id, int qSent) {
        try {
            Connection con = dbConnection.connect();
            String statement = String.format(
                    "UPDATE `orderListTransformation` SET `quantity_sent` = '%d' " +
                            "WHERE `orderListTransformation`.`id` = '%d'",
                    qSent, id);

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    /**
     public static int readWarehouse(Warehouse warehouse) {
     try {

     Connection con = dbConnection.connect();
     String statementSelect = String.format(
     "SELECT * FROM WareHouse WHERE id = 1");

     PreparedStatement pstSelect = con.prepareStatement(statementSelect);
     ResultSet rsSelect = pstSelect.executeQuery();
     if (rsSelect == null) {
     return ERROR;
     } else {
     while ((rsSelect.next()))
     warehouse.setP1(Integer.parseInt(rsSelect.getString(2)));
     warehouse.setP2(Integer.parseInt(rsSelect.getString(3)));
     warehouse.setP3(Integer.parseInt(rsSelect.getString(4)));
     warehouse.setP4(Integer.parseInt(rsSelect.getString(5)));
     warehouse.setP5(Integer.parseInt(rsSelect.getString(6)));
     warehouse.setP6(Integer.parseInt(rsSelect.getString(7)));
     warehouse.setP7(Integer.parseInt(rsSelect.getString(8)));
     warehouse.setP8(Integer.parseInt(rsSelect.getString(9)));
     warehouse.setP9(Integer.parseInt(rsSelect.getString(10)));
     return SUCCESS;
     }
     } catch (Exception ex) {
     System.out.println(ex.getMessage());
     }
     return ERROR;
     }

     **/
    public static int updateAllWarehouse(Warehouse warehouse, String P1, String P2, String P3, String P4, String P5, String P6,
                                         String P7, String P8, String P9) {
        try {
            Connection con = dbConnection.connect();
            String statement = String.format(
                    "UPDATE WareHouse SET P1 = '%s', P2 = '%s', P3 = '%s', P4 = '%s', P5 = '%s'," +
                            " P6 = '%s', P7 = '%s', P8 = '%s', P9 = '%s' WHERE id = 1",
                    P1, P2, P3, P4, P5, P6, P7, P8, P9);

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();

            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }
    public static int updatePxWarehouse(Warehouse warehouse, String Px, int quantity) {
        try {
            Connection con = dbConnection.connect();
            String statement = String.format(
                    "UPDATE WareHouse SET `'%s'` = '%d' WHERE id = 1",
                    Px, quantity);

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();

            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int insertNew_orderLoadDB(int id, String type){
        try {
            Connection con = dbConnection.connect();

            String statement = String.format(
                    "INSERT INTO `orderListLoad` (`id`, `type`) " +
                            "VALUES ('%d', '%s')",
                    id, type);


            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;

    }

    public static int updateStatus_OrderLoadDB(int id, int status) {
        try {
            Connection con = dbConnection.connect();

            String statusText;
            String statement = "";

            if(status == 2){
                statement = String.format(
                        "UPDATE `orderListLoad` SET `start_time` = CURRENT_TIMESTAMP, `status` = 'em_processamento' " +
                                "WHERE `orderListLoad`.`id` = '%d'",
                        id);

            }

            if(status == 3){
                statement = String.format(
                        "UPDATE `orderListLoad` SET `end_time` = CURRENT_TIMESTAMP, `status` = 'concluida' " +
                                "WHERE `orderListLoad`.`id` = '%d'",
                        id);
            }

            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static Vector<Vector<Object>> readOrderListLoadDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from orderListLoad WHERE status NOT LIKE 'concluida' ORDER BY id ");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {

                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(5));
                row.add(rsSelect.getString(6));

                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Vector<Vector<Object>> readHistoricOrderListLoadDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from orderListLoad WHERE status LIKE 'concluida' ORDER BY id");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {
                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(4));
                row.add(rsSelect.getString(6));
                row.add(rsSelect.getString(7));
                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static int deleteLoadTableDB() {
        try {
            Connection con = dbConnection.connect();

            String statement = String.format(
                    "DELETE FROM `orderListLoad`");


            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int deleteUnloadTableDB() {
        try {
            Connection con = dbConnection.connect();

            String statement = String.format(
                    "DELETE FROM `orderListUnload`");


            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static int deleteTransformationTableDB() {
        try {
            Connection con = dbConnection.connect();

            String statement = String.format(
                    "DELETE FROM `orderListTransformation`");


            PreparedStatement pst = con.prepareStatement(statement);
            int rs = pst.executeUpdate();
            if (rs == 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return ERROR;
    }

    public static Vector<Vector<Object>> readUnloadStatsDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from unloadStats");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {

                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(4));
                row.add(rsSelect.getString(5));
                row.add(rsSelect.getString(6));
                row.add(rsSelect.getString(7));
                row.add(rsSelect.getString(8));
                row.add(rsSelect.getString(9));
                row.add(rsSelect.getString(10));
                row.add(rsSelect.getString(11));

                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static void updateUnloadStatsDB() {
        try {
            Connection con = dbConnection.connect();

            boolean aux = con.getAutoCommit();

            con.setAutoCommit(false);

            String string1;
            String string2;
            String string3;

            Statement statement = con.createStatement();

            Pusher C7T3 = (Pusher) SFS.getCell(3, 7);
            Pusher C7T4 = (Pusher) SFS.getCell(4, 7);
            Pusher C7T5 = (Pusher) SFS.getCell(5, 7);

            string1 = String.format(
                    "UPDATE `unloadStats` SET `total` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `unloadStats`.`id_pusher` = 'PM1'",
                    C7T3.getUnloadUnitCount(), C7T3.getTypeUnloadStats(1),  C7T3.getTypeUnloadStats(2), C7T3.getTypeUnloadStats(3), C7T3.getTypeUnloadStats(4), C7T3.getTypeUnloadStats(5),
                    C7T3.getTypeUnloadStats(6), C7T3.getTypeUnloadStats(7), C7T3.getTypeUnloadStats(8), C7T3.getTypeUnloadStats(9));

            string2 = String.format(
                    "UPDATE `unloadStats` SET `total` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `unloadStats`.`id_pusher` = 'PM2'",
                    C7T4.getUnloadUnitCount(), C7T4.getTypeUnloadStats(1),  C7T4.getTypeUnloadStats(2), C7T4.getTypeUnloadStats(3), C7T4.getTypeUnloadStats(4), C7T4.getTypeUnloadStats(5),
                    C7T4.getTypeUnloadStats(6), C7T4.getTypeUnloadStats(7), C7T4.getTypeUnloadStats(8), C7T4.getTypeUnloadStats(9));

            string3 = String.format(
                    "UPDATE `unloadStats` SET `total` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `unloadStats`.`id_pusher` = 'PM3'",
                    C7T5.getUnloadUnitCount(), C7T5.getTypeUnloadStats(1),  C7T5.getTypeUnloadStats(2), C7T5.getTypeUnloadStats(3), C7T5.getTypeUnloadStats(4), C7T5.getTypeUnloadStats(5),
                    C7T5.getTypeUnloadStats(6), C7T5.getTypeUnloadStats(7), C7T5.getTypeUnloadStats(8), C7T5.getTypeUnloadStats(9));




            statement.addBatch(string1);
            statement.addBatch(string2);
            statement.addBatch(string3);


            statement.executeBatch();
            con.commit();
            con.setAutoCommit(aux);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return;
    }


    public static Vector<Vector<Object>> readMachineStatsDB() {
        try {

            Connection con = dbConnection.connect();
            String statementSelect = String.format("SELECT * from machineStats");
            PreparedStatement pstSelect = con.prepareStatement(statementSelect);
            ResultSet rsSelect = pstSelect.executeQuery();
            Vector<Vector<Object>> data = new  Vector<Vector<Object>>();

            while (rsSelect.next()) {

                Vector<Object> row = new Vector<Object>();

                row.add(rsSelect.getString(1));
                row.add(rsSelect.getString(2));
                row.add(rsSelect.getString(3));
                row.add(rsSelect.getString(4));
                row.add(rsSelect.getString(5));
                row.add(rsSelect.getString(6));
                row.add(rsSelect.getString(7));
                row.add(rsSelect.getString(8));
                row.add(rsSelect.getString(9));
                row.add(rsSelect.getString(10));
                row.add(rsSelect.getString(11));
                row.add(rsSelect.getString(12));

                data.add(row);
            }
            return data;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static void updateMachineStatsDB() {
        try {
            Connection con = dbConnection.connect();

            boolean aux = con.getAutoCommit();

            con.setAutoCommit(false);

            String string1;
            String string2;
            String string3;
            String string4;
            String string5;
            String string6;
            String string7;
            String string8;
            String string9;

            Statement statement = con.createStatement();

            Machine C1T3 = (Machine) SFS.getCell(3,1);
            Machine C3T3 = (Machine) SFS.getCell(3,3);
            Machine C5T3 = (Machine) SFS.getCell(3,5);
            Machine C1T4 = (Machine) SFS.getCell(4,1);
            Machine C3T4 = (Machine) SFS.getCell(4,3);
            Machine C5T4 = (Machine) SFS.getCell(4,5);
            Machine C1T5 = (Machine) SFS.getCell(5,1);
            Machine C3T5 = (Machine) SFS.getCell(5,3);
            Machine C5T5 = (Machine) SFS.getCell(5,5);

            string1 = String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C1T3'",
                    C1T3.getTotalTime(), C1T3.getTotalUnits(),  C1T3.getUnitsDoneByType()[0], C1T3.getUnitsDoneByType()[1], C1T3.getUnitsDoneByType()[2], C1T3.getUnitsDoneByType()[3],
                    C1T3.getUnitsDoneByType()[4], C1T3.getUnitsDoneByType()[5], C1T3.getUnitsDoneByType()[6], C1T3.getUnitsDoneByType()[7], C1T3.getUnitsDoneByType()[8]);

            string2 = String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C3T3';",
                    C3T3.getTotalTime(), C3T3.getTotalUnits(),  C3T3.getUnitsDoneByType()[0], C3T3.getUnitsDoneByType()[1], C3T3.getUnitsDoneByType()[2], C3T3.getUnitsDoneByType()[3],
                    C3T3.getUnitsDoneByType()[4], C3T3.getUnitsDoneByType()[5], C3T3.getUnitsDoneByType()[6], C3T3.getUnitsDoneByType()[7], C3T3.getUnitsDoneByType()[8]);

            string3 = String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C5T3';",
                    C5T3.getTotalTime(), C5T3.getTotalUnits(),  C5T3.getUnitsDoneByType()[0], C5T3.getUnitsDoneByType()[1], C5T3.getUnitsDoneByType()[2], C5T3.getUnitsDoneByType()[3],
                    C5T3.getUnitsDoneByType()[4], C5T3.getUnitsDoneByType()[5], C5T3.getUnitsDoneByType()[6], C5T3.getUnitsDoneByType()[7], C5T3.getUnitsDoneByType()[8]);

            string4 =String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C1T4';",
                    C1T4.getTotalTime(), C1T4.getTotalUnits(),  C1T4.getUnitsDoneByType()[0], C1T4.getUnitsDoneByType()[1], C1T4.getUnitsDoneByType()[2], C1T4.getUnitsDoneByType()[3],
                    C1T4.getUnitsDoneByType()[4], C1T4.getUnitsDoneByType()[5], C1T4.getUnitsDoneByType()[6], C1T4.getUnitsDoneByType()[7], C1T4.getUnitsDoneByType()[8]);

            string5 =String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C3T4';",
                    C3T4.getTotalTime(), C3T4.getTotalUnits(),  C3T4.getUnitsDoneByType()[0], C3T4.getUnitsDoneByType()[1], C3T4.getUnitsDoneByType()[2], C3T4.getUnitsDoneByType()[3],
                    C3T4.getUnitsDoneByType()[4], C3T4.getUnitsDoneByType()[5], C3T4.getUnitsDoneByType()[6], C3T4.getUnitsDoneByType()[7], C3T4.getUnitsDoneByType()[8]);

            string6 =String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C5T4';",
                    C5T4.getTotalTime(), C5T4.getTotalUnits(),  C5T4.getUnitsDoneByType()[0], C5T4.getUnitsDoneByType()[1], C5T4.getUnitsDoneByType()[2], C5T4.getUnitsDoneByType()[3],
                    C5T4.getUnitsDoneByType()[4], C5T4.getUnitsDoneByType()[5], C5T4.getUnitsDoneByType()[6], C5T4.getUnitsDoneByType()[7], C5T4.getUnitsDoneByType()[8]);

            string7 =String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C1T5';",
                    C1T5.getTotalTime(), C1T5.getTotalUnits(),  C1T5.getUnitsDoneByType()[0], C1T5.getUnitsDoneByType()[1], C1T5.getUnitsDoneByType()[2], C1T5.getUnitsDoneByType()[3],
                    C1T5.getUnitsDoneByType()[4], C1T5.getUnitsDoneByType()[5], C1T5.getUnitsDoneByType()[6], C1T5.getUnitsDoneByType()[7], C1T5.getUnitsDoneByType()[8]);

            string8 =String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C3T5';",
                    C3T5.getTotalTime(), C3T5.getTotalUnits(),  C3T5.getUnitsDoneByType()[0], C3T5.getUnitsDoneByType()[1], C3T5.getUnitsDoneByType()[2], C3T5.getUnitsDoneByType()[3],
                    C3T5.getUnitsDoneByType()[4], C3T5.getUnitsDoneByType()[5], C3T5.getUnitsDoneByType()[6], C3T5.getUnitsDoneByType()[7], C3T5.getUnitsDoneByType()[8]);

            string9 =String.format(
                    "UPDATE `machineStats` SET `total_op_time` = '%d', `total_op` = '%d', `P1` = '%d', `P2` = '%d',  `P3` = '%d'," +
                            " `P4` = '%d', `P5` = '%d', `P6` = '%d', `P7` = '%d', `P8` = '%d', `P9` = '%d' " +
                            "WHERE `machineStats`.`id_machine` = 'C5T5';",
                    C5T5.getTotalTime(), C5T5.getTotalUnits(),  C5T5.getUnitsDoneByType()[0], C5T5.getUnitsDoneByType()[1], C5T5.getUnitsDoneByType()[2], C5T5.getUnitsDoneByType()[3],
                    C5T5.getUnitsDoneByType()[4], C5T5.getUnitsDoneByType()[5], C5T5.getUnitsDoneByType()[6], C5T5.getUnitsDoneByType()[7], C5T5.getUnitsDoneByType()[8]);




            statement.addBatch(string1);
            statement.addBatch(string2);
            statement.addBatch(string3);
            statement.addBatch(string4);
            statement.addBatch(string5);
            statement.addBatch(string6);
            statement.addBatch(string7);
            statement.addBatch(string8);
            statement.addBatch(string9);

            statement.executeBatch();
            con.commit();
            con.setAutoCommit(aux);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return;
    }






}
