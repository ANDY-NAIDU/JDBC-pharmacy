package InventoryManagement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PlaceNewOrder {
    public void placeOrder() {
        
        JFrame window = new JFrame("Pharmacy - Order Page");
        window.setSize(400, 250);
        window.setLayout(null);
        
        JLabel idLabel = new JLabel("Enter product ID");
        idLabel.setBounds(30, 30, 150, 30);
        window.add(idLabel);
        
        JTextField idTextField = new JTextField();
        idTextField.setBounds(210, 30, 150, 30);
        window.add(idTextField);
        
        JLabel quantityLabel = new JLabel("Enter product Quantity");
        quantityLabel.setBounds(30, 80, 150, 30);
        window.add(quantityLabel);
        
        JTextField quantityTextField = new JTextField();
        quantityTextField.setBounds(210, 80, 150, 30);
        window.add(quantityTextField);
        
        JButton enter = new JButton("Place Order");
        enter.setBounds(30, 140, 150, 30);
        window.add(enter);
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String quantityStr = quantityTextField.getText();
                String idStr = idTextField.getText();

                if (quantityStr.isEmpty() || idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(window,
                            "Please fill in all details",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityStr);
                    int id = Integer.parseInt(idStr);

                    String url = "jdbc:mysql://localhost:3306/pharmacydb";
                    String user = "root";
                    String pass = "password";
                    Connection con = DriverManager.getConnection(url, user, pass);
                    if (con != null) System.out.println("Successfully Connected");

                    // Retrieve current quantity
                    String selectQuery = "SELECT Quantity, Price FROM products WHERE ProductID = ?";
                    PreparedStatement selectStmt = con.prepareStatement(selectQuery);
                    selectStmt.setInt(1, id);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        int currentQuantity = rs.getInt("Quantity");
                        double price = rs.getDouble("Price");

                        if (currentQuantity < quantity) {
                            JOptionPane.showMessageDialog(window,
                                    "Not enough stock available",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Decrease quantity
                            int newQuantity = currentQuantity - quantity;

                            String updateQuery = "UPDATE products SET Quantity = ? WHERE ProductID = ?";
                            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                            updateStmt.setInt(1, newQuantity);
                            updateStmt.setInt(2, id);
                            updateStmt.executeUpdate();

                            // Insert into transactions
                            String insertTransactionQuery = "INSERT INTO transactions (ProductID, Quantity, TotalPrice) VALUES (?, ?, ?)";
                            PreparedStatement insertStmt = con.prepareStatement(insertTransactionQuery);
                            insertStmt.setInt(1, id);
                            insertStmt.setInt(2, quantity);
                            insertStmt.setDouble(3,  -1*quantity * price);
                            insertStmt.executeUpdate();

                            JOptionPane.showMessageDialog(window,
                                    "Order placed successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(window,
                                "Product not found",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(window,
                            "Please enter valid numeric values for ID and Quantity",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(window,
                            "Error: " + ex.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.setBounds(210, 140, 150, 30);
        window.add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });

        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
    }
}
