package InventoryManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Home {
    public static void main(String args[]) {
        
        JFrame window = new JFrame("Pharmacy - Home Page");
        window.setSize(800, 600);
        window.setLayout(null);

        JLabel enterProductID = new JLabel("Enter Name / ID");
        enterProductID.setBounds(170, 30, 110, 30);
        window.add(enterProductID);

        JTextField enterProductIDField = new JTextField();
        enterProductIDField.setBounds(295, 30, 150, 30);
        window.add(enterProductIDField);

        JButton viewProduct = new JButton();
        viewProduct.setText("View Product");
        viewProduct.setBounds(470, 30, 150, 30);
        window.add(viewProduct);

        JButton displayProduct = new JButton("List All Products");
        displayProduct.setBounds(30, 90, 150, 30);
        window.add(displayProduct);

        JButton addProduct = new JButton("Add Product");
        addProduct.setBounds(30, 150, 150, 30);
        window.add(addProduct);

        JButton newOrder = new JButton("Place Order");
        newOrder.setBounds(30, 210, 150, 30);
        window.add(newOrder);

        JButton transactiButton = new JButton("Show Records");
        transactiButton.setBounds(30, 270, 150, 30);
        window.add(transactiButton);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ProductID");
        tableModel.addColumn("ProductName");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");

        JTable area = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBounds(200, 90, 550, 400);
        window.add(scrollPane);

        viewProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idOrName = enterProductIDField.getText();

                if (idOrName.isEmpty()) {
                    JOptionPane.showMessageDialog(window,
                            "Please fill the details",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String url = "jdbc:mysql://localhost:3306/pharmacydb";
                    String user = "root";
                    String pass = "password";
                    Connection con = DriverManager.getConnection(url, user, pass);
                    if (con != null) System.out.println("Successfully Connected");

                    String query = "SELECT * FROM products WHERE ProductID = ? OR ProductName = ?";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, idOrName);
                    stmt.setString(2, idOrName);
                    ResultSet rs = stmt.executeQuery();

                    tableModel.setRowCount(0); // Clear previous results

                    while (rs.next()) {
                        int productId = rs.getInt("ProductID");
                        String productName = rs.getString("ProductName");
                        int productQuantity = rs.getInt("Quantity");
                        double productPrice = rs.getDouble("Price");
                        tableModel.addRow(new Object[]{productId, productName, productQuantity, productPrice});
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
            }
        });

        displayProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String url = "jdbc:mysql://localhost:3306/pharmacydb";
                    String user = "root";
                    String pass = "password";
                    Connection con = DriverManager.getConnection(url, user, pass);
                    if (con != null) System.out.println("Successfully Connected");

                    String query = "SELECT * FROM products";
                    PreparedStatement stmt = con.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    tableModel.setRowCount(0); // Clear previous results

                    while (rs.next()) {
                        int productId = rs.getInt("ProductID");
                        String productName = rs.getString("ProductName");
                        int productQuantity = rs.getInt("Quantity");
                        double productPrice = rs.getDouble("Price");
                        tableModel.addRow(new Object[]{productId, productName, productQuantity, productPrice});
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
            }
        });

        addProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddProduct temp = new AddProduct();
                temp.add();
            }
        });

        newOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                PlaceNewOrder order = new PlaceNewOrder();
                order.placeOrder();
            }
        });

        transactiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShowRecords records = new ShowRecords();
                records.displayRecords();
            }
        });

        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
    }
}
