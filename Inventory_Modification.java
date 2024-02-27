package Inventory_System;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Inventory_Modification {

	private String selectedStore;
	
	public void addItem() {
		
	    // Create a new frame for adding items
	    JFrame addItemFrame = new JFrame("Add Item");
	    addItemFrame.setSize(1000, 600);
	    addItemFrame.setLayout(new BorderLayout());

	    // Drop-down list and radio buttons
	    JPanel topPanel = new JPanel(new FlowLayout());
	    JComboBox<String> storeDropdown = new JComboBox<>(new String[]{"Towson", "Columbia", "Annapolis", "Rockville", "Frederick"});
	    JRadioButton rentRadioButton = new JRadioButton("Rent");
	    JRadioButton buyRadioButton = new JRadioButton("Buy");
	    ButtonGroup radioGroup = new ButtonGroup();
	    radioGroup.add(rentRadioButton);
	    radioGroup.add(buyRadioButton);
	    topPanel.add(storeDropdown);
	    	    
        // Left Panel: Table
        JPanel leftPanel = new JPanel();
        String[] columnNames = {"Item Name", "Category", "Description", "Price", "Stock"};
        ArrayList<String[]> inventoryList = new ArrayList<>();

        // Call the loadInventory method to get inventory data for the selected store
        selectedStore = (String) storeDropdown.getSelectedItem();
        inventoryList = loadInventory(selectedStore);

        // Populate the table with column names and inventory data
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        JTable inventoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        inventoryTable.setCellSelectionEnabled(true); // Allow cell selection
        inventoryTable.setRowSelectionAllowed(true); // Allow row selection
        inventoryTable.setColumnSelectionAllowed(false); // Disable column selection

        // Populate the table with columns 1 to 5
        for (String[] rowData : inventoryList) {
            // Ensure rowData has at least 5 columns
            if (rowData.length >= 5) {
                String[] selectedColumns = Arrays.copyOfRange(rowData, 1, 6);
                tableModel.addRow(selectedColumns);
            }
        }
        
        inventoryTable.setEnabled(false);
        leftPanel.add(tableScrollPane);
        
	    //Text Fields and Buttons
	    JPanel rightPanel = new JPanel();
	    JTextField itemNameField = new JTextField(15);
	    JTextField categoryField = new JTextField(15);
	    JTextArea descriptionArea = new JTextArea(5, 15);
	    JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
	    JTextField priceField = new JTextField(15);
	    JTextField stockField = new JTextField(15);
	    JButton submitButton = new JButton("Submit");
	    JButton finishButton = new JButton("Finish");

	    // Set line wrap and word wrap for descriptionArea
	    descriptionArea.setLineWrap(true);
	    descriptionArea.setWrapStyleWord(true);
	    
	    rightPanel.setLayout(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.insets = new Insets(5, 5, 5, 5);

	    gbc.gridx = 0;
	    gbc.gridy = 0;

	    rightPanel.add(buyRadioButton, gbc);
	    gbc.gridy++;
	    rightPanel.add(new JLabel("Item Name:"), gbc);
	    gbc.gridy++;
	    rightPanel.add(new JLabel("Category:"), gbc);
	    gbc.gridy++;
	    rightPanel.add(new JLabel("Description:"), gbc);
	    gbc.gridy++;
	    rightPanel.add(new JLabel("Price:"), gbc);
	    gbc.gridy++;
	    rightPanel.add(new JLabel("Stock:"), gbc);

	    gbc.gridx = 1;
	    gbc.gridy = 0;

	    rightPanel.add(rentRadioButton, gbc);
	    gbc.gridy++;
	    rightPanel.add(itemNameField, gbc);
	    gbc.gridy++;
	    rightPanel.add(categoryField, gbc);
	    gbc.gridy++;
	    gbc.fill = GridBagConstraints.BOTH;
	    rightPanel.add(descriptionScrollPane, gbc);
	    gbc.gridy++;
	    rightPanel.add(priceField, gbc);
	    gbc.gridy++;
	    rightPanel.add(stockField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy++;
	    gbc.gridwidth = 2;
	    rightPanel.add(submitButton, gbc);
	    gbc.gridy++;
	    rightPanel.add(finishButton, gbc);
	    
	    storeDropdown.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Reload inventory when the locale (store) changes
	            selectedStore = (String) storeDropdown.getSelectedItem();
	            ArrayList<String[]> inventoryList = Inventory_Modification.loadInventory(selectedStore);

	            // Clear existing data from the table
	            DefaultTableModel tableModel = (DefaultTableModel) inventoryTable.getModel();
	            tableModel.setRowCount(0);

	            // Populate the table with columns 1 to 5
	            for (String[] rowData : inventoryList) {
	                // Ensure rowData has at least 5 columns
	                if (rowData.length >= 5) {
	                    String[] selectedColumns = Arrays.copyOfRange(rowData, 1, 6);
	                    tableModel.addRow(selectedColumns);
	                }
	            }
	        }
	    });

	    // Event handling for the submitButton
	    submitButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Get data from text fields and radio buttons
	        	int itemId = getNumberOfLines("Buy_Better_" + selectedStore + ".txt") + 1;
	            String itemName = itemNameField.getText();
	            String category = categoryField.getText();
	            String description = descriptionArea.getText();
	            String priceString = priceField.getText();
	            String stock = stockField.getText();
	            String rentOrBuy = rentRadioButton.isSelected() ? "Rent" : "Buy";

	            // Validate that all fields are filled
	            if (itemName.isEmpty() || category.isEmpty() || description.isEmpty() ||
	                priceString.isEmpty() || stock.isEmpty()) {
	                JOptionPane.showMessageDialog(addItemFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            
	            // Check if any field contains a semicolon
	            if (itemName.contains(";") || category.contains(";") || description.contains(";") ||
	                priceString.contains(";") || stock.contains(";")) {
	                JOptionPane.showMessageDialog(addItemFrame, "Fields cannot contain semicolons (;).", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            
	            double price;
	            // Check if price is a valid float
	            try {
	                price = Float.parseFloat(priceString);
	            } 
	            
	            catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(addItemFrame, "Price must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }

	            // Check if stock is a valid integer
	            try {
	                Integer.parseInt(stock);
	            } 
	            
	            catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(addItemFrame, "Stock must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            
	            // Format price as a dollar amount with two decimal places
	            DecimalFormat priceFormatter = new DecimalFormat("$###,##0.00");
	            String formattedPrice = priceFormatter.format(price);
	            
	            // Construct the new line
	            String newLine = itemId + ";" + itemName + ";" + category + ";" + description + ";" + formattedPrice + ";" + stock + ";" + rentOrBuy;

	            // Append the new line to the file
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Buy_Better_" + selectedStore + ".txt", true))) {
	                writer.write(newLine);
	                writer.newLine();  // Add a new line after each entry
	                JOptionPane.showMessageDialog(addItemFrame, "Item added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	            } 
	            
	            catch (IOException ex) {
	                JOptionPane.showMessageDialog(addItemFrame, "Failed to add item. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });
	    
	    // Add components to the frame
	    addItemFrame.add(topPanel, BorderLayout.NORTH);
	    addItemFrame.add(leftPanel, BorderLayout.CENTER);
	    addItemFrame.add(rightPanel, BorderLayout.EAST);

	    // Display the frame
	    addItemFrame.setLocationRelativeTo(null);
	    addItemFrame.setVisible(true);

	    // Event handling for the finishButton
	    finishButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Close the frame when Finish button is clicked
	            addItemFrame.dispose();
	        }
	    });
	}
	
	
	public void deleteItem() {
		System.out.println("Test delete");

	}
	
	public void transferItem() {
		System.out.println("Test transfer");

	}
	
	public void returnRental() {
		System.out.println("Test return");

	}
	
	public static ArrayList<String[]> loadInventory(String store) {
	    String fileName = "Buy_Better_" + store + ".txt";
	    ArrayList<String[]> inventoryList = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] rowData = line.split(";");
	            inventoryList.add(rowData);
	        }
	    } catch (IOException e) {
	        e.printStackTrace(); // Handle the exception according to your needs
	    }

	    return inventoryList;
	}
	
	// Helper method to get the number of lines in a file
	private int getNumberOfLines(String fileName) {
	    int lines = 0;
	    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	        while (reader.readLine() != null) {
	            lines++;
	        }
	    } catch (IOException e) {
	        e.printStackTrace(); // Handle the exception according to your needs
	    }
	    return lines;
	}
	
}
