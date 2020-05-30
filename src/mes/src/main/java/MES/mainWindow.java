package MES;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class mainWindow extends JFrame {
    private JButton showUnloadOrdersButton;
    private JButton showTransformationOrdersButton;
    private JPanel panelParent;
    private JButton showHistoric_t;
    private JTable table_u;
    private JTable table_t;
    private JTable table_l;
    private JButton closeButton;
    private JButton machineStatsButton;
    //public unloadOrdersWindow unloadOrdersWindow = new unloadOrdersWindow();
    public MES.HistoricWindow HistoricWindow = new HistoricWindow();

    public machineStats MachineStats = new machineStats();

    public Vector<Vector<Object>> objetos = new Vector<Vector<Object>>();

    public mainWindow() {


        //transformation table
        Object[] columns_t = {"ID", "SUBMIT TIME", "START TIME", "STATUS", "PX", "PY", "TOTAL", "SENT", "IN PROD", "DONE", "WAITING", "DEADLINE"};
        DefaultTableModel defaultTableModel_t = new DefaultTableModel();
        defaultTableModel_t.setColumnIdentifiers(columns_t);
        table_t.setModel(defaultTableModel_t);
        table_t.setBackground(Color.WHITE);
        table_t.setForeground(Color.BLACK);
        Font font = new Font("", 1, 22);
        table_t.setRowHeight(30);
        table_t.setFont(font);

        //unload table
        Object[] columns1 = {"ID", "SUBMIT TIME", "START TIME", "STATUS", "PX", "DY", "TOTAL", "SENT", "QUEUED"};
        DefaultTableModel defaultTableModel_u = new DefaultTableModel();
        defaultTableModel_u.setColumnIdentifiers(columns1);
        table_u.setModel(defaultTableModel_u);
        table_u.setBackground(Color.WHITE);
        table_u.setForeground(Color.BLACK);
        font = new Font("", 1, 22);
        table_u.setRowHeight(30);
        table_u.setFont(font);

        //load table
        Object[] columns_l = {"ID", "SUBMIT TIME", "START TIME", "STATUS", "TYPE"};
        DefaultTableModel defaultTableModel_l = new DefaultTableModel();
        defaultTableModel_l.setColumnIdentifiers(columns_l);
        table_l.setModel(defaultTableModel_l);
        table_l.setBackground(Color.WHITE);
        table_l.setForeground(Color.BLACK);
        font = new Font("", 1, 22);
        table_u.setRowHeight(30);
        table_u.setFont(font);

        add(panelParent);
        setTitle("Order Visualizer");
        setSize(400, 500);

        //transformationOrderWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //unloadOrdersWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

        showHistoric_t.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HistoricWindow.setVisible(true);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        machineStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MachineStats.setVisible(true);
            }
        });



        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fill table_t
                if (objetos.size() > 0) objetos.clear();

                objetos = dbConnection.readOrderListTransformationDB();

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
                objetos = dbConnection.readOrderListUnloadDB();
                if(defaultTableModel_u.getRowCount() > 0){
                    for (int i = defaultTableModel_u.getRowCount() - 1; i > -1; i--){
                        defaultTableModel_u.removeRow(i);
                    }
                }

                for (int i = 0; i < objetos.size(); i++){
                    Vector<Object> row = objetos.get(i);
                    defaultTableModel_u.addRow(row);
                }

                //fill table_u
                if (objetos.size() > 0) objetos.clear();

                objetos = dbConnection.readOrderListLoadDB();

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
        /*
        showUnloadOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unloadOrdersWindow.setVisible(true);
            }
        });

        showTransformationOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transformationOrderWindow.setVisible(true);
            }
        });
        */

    }

}
