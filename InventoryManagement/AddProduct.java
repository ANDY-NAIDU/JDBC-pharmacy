package InventoryManagement;

import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddProduct {
    public void add(){
        JFrame window = new JFrame("Pharmacy - Add Product Page");
        window.setSize(400, 350);
        window.setLayout(null);
        

        JLabel idLabel = new JLabel("Enter product ID");
        idLabel.setBounds(30,30,150,30);
        window.add(idLabel);
        

        JTextField idTextField = new JTextField();
        idTextField.setBounds(210,30,150,30);
        window.add(idTextField);
       


        JLabel nameLabel = new JLabel("Enter product Name");
        nameLabel.setBounds(30,80,200,30);
        window.add(nameLabel);
        

        JTextField nameTextField = new JTextField();
        nameTextField.setBounds(210,80,150,30);
        window.add(nameTextField);
        


        JLabel quantityLabel = new JLabel("Enter product Quantity");
        quantityLabel.setBounds(30,130,150,30);
        window.add(quantityLabel);
        

        JTextField quantityTextField = new JTextField();
        quantityTextField.setBounds(210,130,150,30);
        window.add(quantityTextField);
        


        JLabel priceLabel = new JLabel("Enter Price");
        priceLabel.setBounds(30,180,150,30);
        window.add(priceLabel);
        

        JTextField priceTextField = new JTextField();
        priceTextField.setBounds(210,180,150,30);
        window.add(priceTextField);
        
        

        JButton enter = new JButton();
        enter.setText("Add");
        enter.setBounds(30,230,150,30);
        window.add(enter);
        enter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
                String name = nameTextField.getText();
                String quantity = quantityTextField.getText();
                String id = idTextField.getText();
                String price = priceTextField.getText();


                if(name.isEmpty() || quantity.isEmpty() || id.isEmpty() || price.isEmpty()){
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
                    if(con != null) System.out.println("Successfully Connected");
                    Statement statement = con.createStatement();

                    String query = "INSERT INTO products VALUES ('" + id + "', '" + name + "','" + price + "', '" + quantity +"')";


                    String query2 = "INSERT INTO transactions(productID,Quantity,TotalPrice)  VALUES ('" + id + "', '" + quantity + "','" + Integer.parseInt(quantity)*Integer.parseInt(price)+"')";
                    statement.executeUpdate(query);
                    statement.executeUpdate(query2);
                    JOptionPane.showMessageDialog(null, "Data inserted successfully!");
                    
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window,
                        "Error: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            window.dispose();
            }
        });

        JButton cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setBounds(210,230,150,30);
        window.add(cancel);
        cancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                window.dispose();
            }
        });

        
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
    
    }
}
