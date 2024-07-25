package InventoryManagement;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class ShowRecords {
    public void displayRecords() {
        JFrame window = new JFrame("Pharmacy - Transaction Records");
        window.setSize(800, 600);
        window.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("TransactionID");
        tableModel.addColumn("ProductID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("TotalPrice");
        tableModel.addColumn("TransactionDate");

        JTable recordsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(recordsTable);
        window.add(scrollPane, BorderLayout.CENTER);

        try {
            String url = "jdbc:mysql://localhost:3306/pharmacydb";
            String user = "root";
            String pass = "password";
            Connection con = DriverManager.getConnection(url, user, pass);
            if (con != null) System.out.println("Successfully Connected");

            String query = "SELECT * FROM transactions";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int transactionId = rs.getInt("TransactionID");
                int productId = rs.getInt("ProductID");
                int quantity = rs.getInt("Quantity");
                double price = rs.getDouble("TotalPrice");
                String date = rs.getString("TransactionDate");
                tableModel.addRow(new Object[]{transactionId, productId,  quantity, price, date});
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window,
                    "Error: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
    }
}
