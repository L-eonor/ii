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
                row.add(rsSelect.getString(11));

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
                    "UPDATE `orderListUnload` SET `quantity_done` = '%d' " +
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




}
