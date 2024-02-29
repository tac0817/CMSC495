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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        // Create a new frame for deleting items
        JFrame deleteItemFrame = new JFrame("Delete Item");
        deleteItemFrame.setSize(800, 400);
        deleteItemFrame.setLayout(new BorderLayout());

        // Drop-down list for selecting the store
        JComboBox<String> storeDropdown = new JComboBox<>(new String[]{"Towson", "Columbia", "Annapolis", "Rockville", "Frederick"});

        // Table for displaying inventory
        String[] columnNames = {"Item ID", "Item Name", "Category", "Description", "Price", "Stock", "Rent/Buy"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable inventoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);

        // Button for deleting selected item
        JButton deleteButton = new JButton("Delete Item");

        // Add components to the frame
        deleteItemFrame.add(storeDropdown, BorderLayout.NORTH);
        deleteItemFrame.add(tableScrollPane, BorderLayout.CENTER);
        deleteItemFrame.add(deleteButton, BorderLayout.SOUTH);

        // Populate the table when the store is selected
        storeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the table based on the selected store
                updateTable((String) storeDropdown.getSelectedItem(), tableModel);
            }
        });

        // Action listener for the delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row and delete the corresponding item
                int selectedRow = inventoryTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Delete the item from the file
                    deleteItemFromFile((String) storeDropdown.getSelectedItem(), selectedRow);

                    // Update the table after deletion
                    updateTable((String) storeDropdown.getSelectedItem(), tableModel);
                } else {
                    JOptionPane.showMessageDialog(deleteItemFrame, "Please select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Display the frame
        deleteItemFrame.setLocationRelativeTo(null);
        deleteItemFrame.setVisible(true);
    }
	
    private void updateTable(String store, DefaultTableModel tableModel) {
        // Load inventory for the selected store
        ArrayList<String[]> inventoryList = loadInventory(store);

        // Clear existing data from the table
        tableModel.setRowCount(0);

        // Populate the table with columns 1 to 7
        for (String[] rowData : inventoryList) {
            // Ensure rowData has at least 7 columns
            if (rowData.length >= 7) {
                tableModel.addRow(Arrays.copyOf(rowData, 7));
            }
        }
    }
	
    private void deleteItemFromFile(String store, int selectedRow) {
        String fileName = "Buy_Better_" + store + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {
            String line;
            int currentRow = 0;

            while ((line = reader.readLine()) != null) {
                // Skip the line to delete
                if (currentRow++ != selectedRow) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        // Replace the original file with the temporary file
        try {
            Files.delete(Paths.get(fileName));
            Files.move(Paths.get("temp.txt"), Paths.get(fileName));
            System.out.println("Item deleted successfully.");
        } catch (IOException e) {
            System.out.println("Failed to delete item.");
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }
    
    public void transferItem() {
        // Create a new frame for transferring items
        JFrame transferItemFrame = new JFrame("Transfer Item");
        transferItemFrame.setSize(1000, 600);
        transferItemFrame.setLayout(new BorderLayout());

        // Drop-down lists for selecting the source and destination stores
        JComboBox<String> sourceStoreDropdown = new JComboBox<>(new String[]{"Towson", "Columbia", "Annapolis", "Rockville", "Frederick"});
        JComboBox<String> destinationStoreDropdown = new JComboBox<>(new String[]{"Towson", "Columbia", "Annapolis", "Rockville", "Frederick"});

        // Table for displaying items from the source store
        String[] columnNamesSource = {"Item ID", "Item Name"};
        DefaultTableModel sourceTableModel = new DefaultTableModel(columnNamesSource, 0);
        JTable sourceTable = new JTable(sourceTableModel);
        JScrollPane sourceTableScrollPane = new JScrollPane(sourceTable);

        // Table for displaying items to transfer
        String[] columnNamesTransfer = {"Item ID", "Item Name"};
        DefaultTableModel transferTableModel = new DefaultTableModel(columnNamesTransfer, 0);
        JTable transferTable = new JTable(transferTableModel);
        JScrollPane transferTableScrollPane = new JScrollPane(transferTable);

        // Button for transferring selected items
        JButton transferButton = new JButton("Transfer Selected Items");

        // Add components to the frame
        JPanel storeDropdownPanel = new JPanel(new FlowLayout());
        storeDropdownPanel.add(new JLabel("Source Store:"));
        storeDropdownPanel.add(sourceStoreDropdown);
        storeDropdownPanel.add(new JLabel("Destination Store:"));
        storeDropdownPanel.add(destinationStoreDropdown);

        transferItemFrame.add(storeDropdownPanel, BorderLayout.NORTH);
        transferItemFrame.add(sourceTableScrollPane, BorderLayout.WEST);
        transferItemFrame.add(transferTableScrollPane, BorderLayout.EAST);
        transferItemFrame.add(transferButton, BorderLayout.SOUTH);

        // Populate the source store table when the source store is selected
        sourceStoreDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load inventory for the source store
                String sourceStore = (String) sourceStoreDropdown.getSelectedItem();
                ArrayList<String[]> sourceInventory = loadInventory(sourceStore);

                // Clear existing data from the source table
                sourceTableModel.setRowCount(0);

                // Populate the source table with columns 1 and 2
                for (String[] rowData : sourceInventory) {
                    // Ensure rowData has at least 2 columns
                    if (rowData.length >= 2) {
                        sourceTableModel.addRow(Arrays.copyOf(rowData, 2));
                    }
                }
            }
        });
        
        // Populate the destination store table when the destination store is selected
        destinationStoreDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load inventory for the destination store
                String destinationStore = (String) destinationStoreDropdown.getSelectedItem();
                ArrayList<String[]> destinationInventory = loadInventory(destinationStore);

                // Clear existing data from the destination table
                transferTableModel.setRowCount(0);

                // Populate the destination table with columns 1 and 2
                for (String[] rowData : destinationInventory) {
                    // Ensure rowData has at least 2 columns
                    if (rowData.length >= 2) {
                        transferTableModel.addRow(Arrays.copyOf(rowData, 2));
                    }
                }
            }
        });

        // Display the frame
        transferItemFrame.setLocationRelativeTo(null);
        transferItemFrame.setVisible(true);
        
        // Event handling for the transferButton
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected rows from the source table
                int[] selectedRows = sourceTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    for (int selectedRow : selectedRows) {
                        String selectedItemId = sourceTableModel.getValueAt(selectedRow, 0).toString();
                        String selectedItemName = sourceTableModel.getValueAt(selectedRow, 1).toString();
                        transferItem(sourceStoreDropdown, destinationStoreDropdown, selectedItemId, selectedItemName);
                    }
                    // Update the source and destination tables after transfer
                    updateTable((String) sourceStoreDropdown.getSelectedItem(), sourceTableModel);
                    updateTable((String) destinationStoreDropdown.getSelectedItem(), transferTableModel);
                } else {
                    JOptionPane.showMessageDialog(transferItemFrame, "Please select an item to transfer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
 // Method to transfer item from source store to destination store
    private void transferItem(JComboBox<String> sourceDropdown, JComboBox<String> destinationDropdown,
                               String selectedItemId, String selectedItemName) {
        String sourceStore = (String) sourceDropdown.getSelectedItem();
        String destinationStore = (String) destinationDropdown.getSelectedItem();

        // Read the source inventory
        ArrayList<String[]> sourceInventory = loadInventory(sourceStore);

        // Remove the transferred item from the source inventory
        sourceInventory.removeIf(row -> row.length >= 2 && row[0].equals(selectedItemId) && row[1].equals(selectedItemName));

        // Write back the updated source inventory
        writeInventory(sourceStore, sourceInventory);

        // Read the destination inventory
        ArrayList<String[]> destinationInventory = loadInventory(destinationStore);

        // Determine the destination item ID (current line number + 1)
        int destinationItemId = getNumberOfLines("Buy_Better_" + destinationStore + ".txt") + 1;

        // Create the new line for the destination store
        String newLine = destinationItemId + ";" + selectedItemName;

        // Append the new line to the destination inventory
        destinationInventory.add(newLine.split(";"));

        // Write back the updated destination inventory
        writeInventory(destinationStore, destinationInventory);
        
        // Update IDs in the source inventory to start from 1 again
        updateIdsInSource(sourceStore);
    }
    
    // Helper method to update IDs in the source inventory
    private void updateIdsInSource(String sourceStore) {
        // Read the source inventory
        ArrayList<String[]> sourceInventory = loadInventory(sourceStore);

        // Update the IDs starting from 1 again
        for (int i = 0; i < sourceInventory.size(); i++) {
            String[] rowData = sourceInventory.get(i);
            if (rowData.length >= 1) {
                rowData[0] = String.valueOf(i + 1);
            }
        }

        // Write back the updated source inventory
        writeInventory(sourceStore, sourceInventory);
    }
    
    // Helper method to write inventory data back to the file
    private void writeInventory(String store, ArrayList<String[]> inventory) {
        String fileName = "Buy_Better_" + store + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String[] rowData : inventory) {
                // Join the columns with a semicolon and write to the file
                writer.write(String.join(";", rowData));
                writer.newLine(); // Add a new line after each entry
            }
        } 
        catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
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
