package Inventory_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

public class Store_GUI extends JFrame {
	
    private JTextField searchBox;
    private JPanel adminPanel;
    JLabel results;
    JPanel resultList;
    JComboBox storeLoc = new JComboBox(new String[] {"Towson", "Columbia", "Annapolis", "Rockville", "Frederick"});
    List cart;

    public Store_GUI(User currentUser) {
    	
    	setTitle("Store Page");
        setSize(700, 630);
        searchBox = new JTextField("Enter your product name here", 30);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        if(currentUser.isAdmin()) {
        	adminPanel = new JPanel();
        	adminPanel = createAdminPanel();
        }
        
        JLabel welcome = new JLabel("Welcome " + currentUser.getName() + " to " + currentUser.getMainStore() + "'s store page!");
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

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 150, 10));
        resultList = new JPanel();
        resultList.setLayout(new BoxLayout(resultList, BoxLayout.Y_AXIS));
        
        userPanel.add(signoutButton);
        userPanel.add(welcome);
        userPanel.add(stockText);
        userPanel.add(storeLoc);
        userPanel.add(cartLabel);
        userPanel.add(cart);
        userPanel.add(searchBox);
        userPanel.add(searchButton);
        userPanel.add(results);
        userPanel.add(resultList);
        userPanel.add(cButton);
        
        tabbedPane.addTab("Inventory System", userPanel);
        
        if(currentUser.isAdmin()) {
        	tabbedPane.addTab("Admin/Modification", adminPanel);
        }
        
        add(tabbedPane);

        setVisible(true);

    }

    private JPanel createAdminPanel() {
    	JPanel adminPanel = new JPanel(new GridBagLayout());
    	
    	JButton addButton = new JButton("Add Item");
    	JButton removeButton = new JButton("Remove Item");
    	JButton moveButton = new JButton("Transfer Item");
    	JButton rentalButton = new JButton("Return Rental");
    	
    	Inventory_Modification adminActions = new Inventory_Modification();
    	
    	addButton.addActionListener(e -> {
    		adminActions.addItem();
    	});
    	
    	removeButton.addActionListener(e -> {
    		adminActions.deleteItem();
    	});
    	
    	moveButton.addActionListener(e -> {
    		adminActions.transferItem();
    	});
    	
    	rentalButton.addActionListener(e -> {
    		adminActions.returnRental();
    	});
    	
    	GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        adminPanel.add(addButton, gbc);
        
        gbc.gridy++;
        adminPanel.add(removeButton, gbc);
        
        gbc.gridy++;
        adminPanel.add(moveButton, gbc);
        
        gbc.gridy++;
        adminPanel.add(rentalButton, gbc);

    	return adminPanel;
    }
    
    private void onSearchButtonClick() {
    	String store = (String) storeLoc.getSelectedItem();
        File file = new File("Buy_Better_" + store + ".txt");

        BufferedReader read;
        ArrayList<JLabel> products = new ArrayList<>();
        results.setText("");
        resultList.removeAll();

        if (searchBox.getText().contentEquals("Enter your product name here")
                || searchBox.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "No product name was entered.", "Error", JOptionPane.ERROR_MESSAGE);
        } 
        
        else {
            try {
                read = new BufferedReader(new FileReader(file));
                String fileLine = read.readLine();
                int i = 0;

                while (fileLine != null) {
                	String[] item = fileLine.split(";");
                    if (item[1].contains(searchBox.getText()) || item[2].contains(searchBox.getText())) {
                        String p = fileLine;
                        results.setText("Results Found");
                        products.add(new JLabel(item[1] + ": " + item[3]));
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
        checkoutPage.setLayout(new GridLayout(cart.getItemCount(), 2, 20, 20));

        JLabel itemLabel = new JLabel("Item Name:");
        JLabel numLabel = new JLabel("Item Amount:");
        JLabel purchaseLabel = new JLabel("Choose a payment method:");
        JComboBox payment = new JComboBox(new String[]{"Credit Card (Visa)", "Credit Card (MasterCard)", "PayPal"});

        checkoutPage.add(itemLabel, 0, 0);
        checkoutPage.add(numLabel, 0, 1);

        for (int i = 0; i < itemNum.length; i++) {
            if (itemNum[i] != -1) {
                checkoutPage.add(new JLabel(items[i]));
                checkoutPage.add(new JLabel("" + itemNum[i]));
            }
        }
        
        checkoutPage.add(purchaseLabel);
        checkoutPage.add(payment);
        
        checkoutPage.setLocationRelativeTo(null);
        checkoutPage.setVisible(true);
        checkoutPage.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    }
    
    public static void main(String[] args) {
    	
    		LoginDisplay Login_GUI = new LoginDisplay();
    }
}