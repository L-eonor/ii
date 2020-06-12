package MES;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class machineStats extends JFrame{
    private JPanel panel_m;
    private JTable table_us;
    private JTable table_m;

    public Vector<Vector<Object>> objetos = new Vector<Vector<Object>>();

    public machineStats() {


        //machines stats table
        Object[] columns_m = {"ID MACHINE", "TOTAL TIME (s)", "TOTAL OPERATIONS", "P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9"};
        DefaultTableModel defaultTableModel_m = new DefaultTableModel();
        defaultTableModel_m.setColumnIdentifiers(columns_m);
        table_m.setModel(defaultTableModel_m);
        table_m.setBackground(Color.WHITE);
        table_m.setForeground(Color.BLACK);
        Font font = new Font("", 1, 22);
        table_m.setRowHeight(30);
        table_m.setFont(font);


        //unload stats table
        Object[] columns_us = {"PUSHER", "TOTAL", "P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9"};
        DefaultTableModel defaultTableModel_us = new DefaultTableModel();
        defaultTableModel_us.setColumnIdentifiers(columns_us);
        table_us.setModel(defaultTableModel_us);
        table_us.setBackground(Color.WHITE);
        table_us.setForeground(Color.BLACK);
        table_us.setRowHeight(30);
        table_us.setFont(font);

        add(panel_m);
        setTitle("Machine and Unload Statistics");
        setSize(400, 500);

        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fill table_m
                if (objetos.size() > 0) objetos.clear();

                dbConnection.updateMachineStatsDB();

                objetos = dbConnection.readMachineStatsDB();

                if (defaultTableModel_m.getRowCount() > 0) {
                    for (int i = defaultTableModel_m.getRowCount() - 1; i > -1; i--) {
                        defaultTableModel_m.removeRow(i);
                    }
                }

                for (int i = 0; i < objetos.size(); i++) {
                    Vector<Object> row = objetos.get(i);
                    defaultTableModel_m.addRow(row);
                }

                //fill table_us
                if (objetos.size() > 0) objetos.clear();

                dbConnection.updateUnloadStatsDB();

                objetos = dbConnection.readUnloadStatsDB();

                if (defaultTableModel_us.getRowCount() > 0) {
                    for (int i = defaultTableModel_us.getRowCount() - 1; i > -1; i--) {
                        defaultTableModel_us.removeRow(i);
                    }
                }

                for (int i = 0; i < objetos.size(); i++) {
                    Vector<Object> row = objetos.get(i);
                    defaultTableModel_us.addRow(row);
                }
            }
        });

        timer.setRepeats(true);
        timer.start();
    }
}
