package MES;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class HistoricWindow extends JFrame{
    private JPanel panel1;
    private JTable table_u;
    private JTable table_t;
    private JTable table_l;
    public Vector<Vector<Object>> objetos = new Vector<Vector<Object>>();


    public HistoricWindow(){

        //transformation table
        Object[] columns_t = {"ID", "SUBMIT TIME", "START TIME", "END TIME", "PX", "PY", "QUANTITY", "MAX DELAY", "EXPECTED DURATION", "DURATION"};
        DefaultTableModel defaultTableModel_t = new DefaultTableModel();
        defaultTableModel_t.setColumnIdentifiers(columns_t);
        table_t.setModel(defaultTableModel_t);
        table_t.setBackground(Color.WHITE);
        table_t.setForeground(Color.BLACK);
        Font font = new Font("", 1, 22);
        table_t.setRowHeight(30);
        table_t.setFont(font);


        //unload table
        Object[] columns1 = {"ID", "SUBMIT TIME", "START TIME", "END_TIME", "PX", "DY", "QUANTITY", "DURATION"};
        DefaultTableModel defaultTableModel_u = new DefaultTableModel();
        defaultTableModel_u.setColumnIdentifiers(columns1);
        table_u.setModel(defaultTableModel_u);
        table_u.setBackground(Color.WHITE);
        table_u.setForeground(Color.BLACK);
        font = new Font("", 1, 22);
        table_u.setRowHeight(30);
        table_u.setFont(font);

        //load table
        Object[] columns_l = {"ID", "SUBMIT TIME", "START TIME", "END_TIME", "TYPE", "DURATION"};
        DefaultTableModel defaultTableModel_l = new DefaultTableModel();
        defaultTableModel_l.setColumnIdentifiers(columns_l);
        table_l.setModel(defaultTableModel_l);
        table_l.setBackground(Color.WHITE);
        table_l.setForeground(Color.BLACK);
        font = new Font("", 1, 22);
        table_l.setRowHeight(30);
        table_l.setFont(font);


        add(panel1);
        setTitle("Historic of Orders");
        setSize(400, 500);

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fill table_t
                if (objetos.size() > 0) objetos.clear();

                objetos = dbConnection.readHistoricOrderListTransformationDB();

                if(defaultTableModel_t.getRowCount() > 0){
                    for (int i = defaultTableModel_t.getRowCount() - 1; i > -1; i--){
                        defaultTableModel_t.removeRow(i);
                    }
                }

                for (int i = 0; i < objetos.size(); i++){
                    Vector<Object> row = objetos.get(i);
                    defaultTableModel_t.addRow(row);
                }

                //fill table_u
                if (objetos.size() > 0) objetos.clear();
                objetos = dbConnection.readHistoricOrderListUnloadDB();
                if(defaultTableModel_u.getRowCount() > 0){
                    for (int i = defaultTableModel_u.getRowCount() - 1; i > -1; i--){
                        defaultTableModel_u.removeRow(i);
                    }
                }

                for (int i = 0; i < objetos.size(); i++){
                    Vector<Object> row = objetos.get(i);
                    defaultTableModel_u.addRow(row);
                }

                //fill table_l
                if (objetos.size() > 0) objetos.clear();
                objetos = dbConnection.readHistoricOrderListLoadDB();
                if(defaultTableModel_l.getRowCount() > 0){
                    for (int i = defaultTableModel_l.getRowCount() - 1; i > -1; i--){
                        defaultTableModel_l.removeRow(i);
                    }
                }

                for (int i = 0; i < objetos.size(); i++){
                    Vector<Object> row = objetos.get(i);
                    defaultTableModel_l.addRow(row);
                }

            }


        });

        timer.setRepeats(true);
        timer.start();


    }





}
