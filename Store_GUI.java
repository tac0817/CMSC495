package Inventory_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

public class Store_GUI extends JFrame {

    private JTextField searchBox;
    JLabel results;
    JPanel resultList;
    String[] storeLoc = {"Towson", "Columbia", "Annapolis", "Rockville", "Frederick"};
    List cart;
    JComboBox locales = new JComboBox(storeLoc);

    public Store_GUI() {
        setTitle("Store Page");
        setSize(350, 500);
        searchBox = new JTextField("Enter your product name here", 30);
        JLabel welcome = new JLabel("Welcome to [Store Name] store page!");
        JLabel stockText = new JLabel("Store: ");
        JLabel cartLabel = new JLabel("Cart:");
        cart = new List();
        cart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cart.remove(cart.getSelectedItem());
                }
            }
        });

        results = new JLabel();
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSearchButtonClick();
            }
        });

        JButton cButton = new JButton("To Checkout");
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCheckoutButtonClick();
            }
        });
        
        JButton signoutButton = new JButton("Sign Out");
        signoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginDisplay();
            }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 150, 10));
        resultList = new JPanel();
        resultList.setLayout(new BoxLayout(resultList, BoxLayout.Y_AXIS));
        panel.add(signoutButton);
        panel.add(welcome);
        panel.add(stockText);
        panel.add(locales);
        panel.add(cartLabel);
        panel.add(cart);
        panel.add(searchBox);
        panel.add(searchButton);
        panel.add(results);
        panel.add(resultList);
        add(panel);
        panel.add(cButton);

        setVisible(true);

    }

    private void onSearchButtonClick() {
        File file;
        if (locales.getSelectedItem().equals("Towson")) {
            file = new File("C:\\Users\\jaden\\OneDrive\\Documents\\NetBeansProjects\\Inventory_System\\src\\inventory_search\\Store 1.txt");
        } else if (locales.getSelectedItem().equals("Columbia")) {
            file = new File("");
        } else if (locales.getSelectedItem().equals("Annapolis")) {
            file = new File("");
        } else if (locales.getSelectedItem().equals("Rockville")) {
            file = new File("");
        } else {
            file = new File("");
        }

        BufferedReader read;
        ArrayList<JLabel> products = new ArrayList<>();
        results.setText("");
        resultList.removeAll();

        if (searchBox.getText().contentEquals("Enter your product name here")
                || searchBox.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "No product name was entered.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                read = new BufferedReader(new FileReader(file));
                String fileLine = read.readLine();
                int i = 0;

                while (fileLine != null) {
                    if (fileLine.contains(searchBox.getText())) {
                        String p = fileLine;
                        results.setText("Results Found");
                        products.add(new JLabel(fileLine));
                        products.get(i).addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent me) {
                                cart.add(p);
                            }
                        });
                        resultList.add(products.get(i));
                        i++;
                    }
                    fileLine = read.readLine();
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "The Store's stock file could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "The Store's stock file is empty.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(this, "The Store's stock file is not entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void onCheckoutButtonClick() {
        int[] itemNum = new int[cart.getItems().length];
        String[] items = cart.getItems();
        for (int i = 0; i < cart.getItems().length; i++) {
            int num = 1;
            for (int j = i + 1; j < items.length; j++) {
                if (items[i].equals(items[j])) {
                    num++;
                    itemNum[j] = -1;
                }
            }
            if (itemNum[i] != -1) {
                itemNum[i] = num;
            }
        }

        JDialog checkoutPage = new JDialog(this, "Checkout", true);
        checkoutPage.setSize(550, 250);
        checkoutPage.setLayout(new GridLayout(cart.getItemCount(), 2,20, 20));

        JLabel itemLabel = new JLabel("Item Name:");
        JLabel numLabel = new JLabel("Item Amount:");
        JLabel purchaseLabel = new JLabel("Choose a payment method:");
        JComboBox payment = new JComboBox(new String[]{"Credit Card (Visa)", "Credit Card (MasterCard)", "PayPal"});

        checkoutPage.add(itemLabel, 0,0);
        checkoutPage.add(numLabel, 0,1);

        for (int i = 0; i < itemNum.length; i++) {
            if (itemNum[i] != -1) {
                checkoutPage.add(new JLabel(items[i]));
                checkoutPage.add(new JLabel("" + itemNum[i]));
            }
        }
        
        checkoutPage.add(purchaseLabel);
        checkoutPage.add(payment);
        
        checkoutPage.setLocationRelativeTo(this);
        checkoutPage.setVisible(true);
        checkoutPage.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    }
    
    public static void main(String[] args) {
    	
    		LoginDisplay Login_GUI = new LoginDisplay();
    }
}
