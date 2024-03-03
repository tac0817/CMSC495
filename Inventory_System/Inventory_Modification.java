package Inventory_System;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Inventory_Modification {

	private String selectedStore;
	private List<String[]> userRentals;
	
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
	        	int itemId = getNumberOfLines(".\\Documents\\Buy_Better_" + selectedStore + ".txt") + 1;
	            String itemName = itemNameField.getText();
	            String category = categoryField.getText();
	            String description = descriptionArea.getText();
	            String priceString = priceField.getText();
	            String stock = stockField.getText();
	            String rentOrBuy = rentRadioButton.isSelected() ? "rent" : "buy";

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
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter(".\\Documents\\Buy_Better_" + selectedStore + ".txt", true))) {
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
                updateTable((String) storeDropdown.getSelectedItem(), tableModel, false);
            }
        });

        // Action listener for the delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row and delete the corresponding item
                int[] selectedRows = inventoryTable.getSelectedRows();
                if (selectedRows.length > 0) {
                	
                	// Loop through the selected rows and delete the corresponding items
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int selectedRow = selectedRows[i];
                        deleteItemFromFile((String) storeDropdown.getSelectedItem(), selectedRow);
                    }
                    updateIdsInSource((String) storeDropdown.getSelectedItem());
                    // Update the table after deletion
                    updateTable((String) storeDropdown.getSelectedItem(), tableModel, false);
                } 
                else {
                    JOptionPane.showMessageDialog(deleteItemFrame, "Please select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Display the frame
        deleteItemFrame.setLocationRelativeTo(null);
        deleteItemFrame.setVisible(true);
    }
	
    private void updateTable(String store, DefaultTableModel tableModel, boolean isTransfer) {
        // Load inventory for the selected store
        ArrayList<String[]> inventoryList = loadInventory(store);

        // Clear existing data from the table
        tableModel.setRowCount(0);
        
        // Populate the table with columns 1 to 7
        for (String[] rowData : inventoryList) {
            // Ensure rowData has at least 7 columns
        	
            if (rowData.length >= 7 && !isTransfer) {
                tableModel.addRow(Arrays.copyOf(rowData, 7));
            }
            else {
            	Object[] modifiedRowData = Arrays.copyOf(rowData, 3); // Include the quantity
                modifiedRowData[2] = rowData[5]; // Add the quantity to the 3rd column
                tableModel.addRow(modifiedRowData);
            }
        }
    }
	
    private void deleteItemFromFile(String store, int selectedRow) {
        String fileName = ".\\Documents\\Buy_Better_" + store + ".txt";

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
        } 
        catch (IOException e) {
            System.out.println("The file could not be affected");
        }

        // Replace the original file with the temporary file
        try {
            Files.delete(Paths.get(fileName));
            Files.move(Paths.get("temp.txt"), Paths.get(fileName));
        } 
        catch (IOException e) {
            System.out.println("Failed to delete item.");
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
        String[] columnNamesSource = {"Item ID", "Item Name", "Stock"};
        DefaultTableModel sourceTableModel = new DefaultTableModel(columnNamesSource, 0);
        JTable sourceTable = new JTable(sourceTableModel);
        JScrollPane sourceTableScrollPane = new JScrollPane(sourceTable);

        // Table for displaying items to transfer
        String[] columnNamesTransfer = {"Item ID", "Item Name", "Stock"};
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

                for (String[] rowData : sourceInventory) {
                    if (rowData.length >= 3) {
                        Object[] modifiedRowData = Arrays.copyOf(rowData, 3); // Include the quantity
                        modifiedRowData[2] = rowData[5]; // Add the quantity to the 3rd column
                        sourceTableModel.addRow(modifiedRowData);
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

                // Populate the destination table
                for (String[] rowData : destinationInventory) {
                    if (rowData.length >= 3) {
                        Object[] modifiedRowData = Arrays.copyOf(rowData, 3); // Include the quantity
                        modifiedRowData[2] = rowData[5]; // Add the quantity to the 3rd column
                        transferTableModel.addRow(modifiedRowData);
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
            	ListSelectionModel selectionModel = sourceTable.getSelectionModel();
                if (selectionModel.getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
                    int[] selectedRows = sourceTable.getSelectedRows();
                    if (selectedRows.length > 0) {
                        for (int selectedRow : selectedRows) {
                            
                        	System.out.println("Selected Row: " + selectedRow);
                            String selectedItemId = sourceTableModel.getValueAt(selectedRow, 0).toString();
                            String selectedItemName = sourceTableModel.getValueAt(selectedRow, 1).toString();
                            Integer selectedQuantity = Integer.parseInt(sourceTableModel.getValueAt(selectedRow, 2).toString());
                            
                            transferItem(sourceStoreDropdown, destinationStoreDropdown, selectedItemId, selectedItemName, selectedQuantity);

                        }
                    }
                    SwingUtilities.invokeLater(() -> {

                        // Now, after transferring all items, remove them from the source
                        removeItemsFromSource(selectedRows, sourceStoreDropdown, sourceTableModel);
                        
                        updateTable((String) sourceStoreDropdown.getSelectedItem(), sourceTableModel, true);
                        updateTable((String) destinationStoreDropdown.getSelectedItem(), transferTableModel, true);
                    
                    });
                }
                else {
                    JOptionPane.showMessageDialog(transferItemFrame, "Please select an item to transfer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    // Method to remove transferred items from the source
    private void removeItemsFromSource(int[] selectedRows, JComboBox<String> sourceDropdown, DefaultTableModel sourceTableModel) {
        String sourceStore = (String) sourceDropdown.getSelectedItem();
        ArrayList<String[]> sourceInventory = loadInventory(sourceStore);

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int selectedRow = selectedRows[i];
            String selectedItemId = sourceTableModel.getValueAt(selectedRow, 0).toString();
            String selectedItemName = sourceTableModel.getValueAt(selectedRow, 1).toString();

            sourceInventory.removeIf(row -> row.length >= 2 && row[0].equals(selectedItemId) && row[1].equals(selectedItemName));
        }

        // Write back the updated source inventory
        writeInventory(sourceStore, sourceInventory);

        // Update IDs in the source inventory to start from 1 again
        updateIdsInSource(sourceStore);
    }
    
    // Method to transfer item from source store to destination store
    private void transferItem(JComboBox<String> sourceDropdown, JComboBox<String> destinationDropdown,
                               String selectedItemId, String selectedItemName, int selectedQuantity) {
    	
    	 String sourceStore = (String) sourceDropdown.getSelectedItem();
    	    String destinationStore = (String) destinationDropdown.getSelectedItem();

    	    // Read the source inventory
    	    ArrayList<String[]> sourceInventory = loadInventory(sourceStore);

    	    // Find the item in the source inventory
    	    String[] transferredItem = null;
    	    for (String[] row : sourceInventory) {
    	        if (row.length >= 2 && row[0].equals(selectedItemId) && row[1].equals(selectedItemName)) {
    	            transferredItem = Arrays.copyOf(row, row.length);  // Capture the row before removal
    	            break;
    	        }
    	    }

    	    // Make sure transferredItem is not null before using it
    	    if (transferredItem != null) {
    	        // Read the destination inventory
    	        ArrayList<String[]> destinationInventory = loadInventory(destinationStore);

    	        // Check if the item already exists in the destination store
    	        boolean itemExists = false;

    	        for (String[] destinationItem : destinationInventory) {
    	            if (destinationItem.length >= 2 && destinationItem[1].equals(selectedItemName)) {
    	                // Item already exists, update stock
    	                int currentStock = Integer.parseInt(destinationItem[5]); // Assuming stock is in the 6th column
    	                currentStock += selectedQuantity;
    	                destinationItem[5] = String.valueOf(currentStock);
    	                itemExists = true;
    	                break;
    	            }
    	        }

    	        if (!itemExists) {
    	            // Item does not exist, add a new entry with transferred stock count
    	            // Determine the destination item ID (current line number + 1)
    	            int destinationItemId = getNumberOfLines(".\\Documents\\Buy_Better_" + destinationStore + ".txt") + 1;

    	            // Create the new line for the destination store
    	            String newLine = destinationItemId + ";" + transferredItem[1] + ";" + transferredItem[2] + ";" + transferredItem[3] +
    	                    ";" + transferredItem[4] + ";" + transferredItem[5] + ";" + transferredItem[6];

    	            // Append the new line to the destination inventory
    	            destinationInventory.add(newLine.split(";"));
    	        }


            // Write back the updated destination inventory
            writeInventory(destinationStore, destinationInventory);

            // Update IDs in the source inventory to start from 1 again
            updateIdsInSource(sourceStore);
        }
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
        String fileName = ".\\Documents\\Buy_Better_" + store + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String[] rowData : inventory) {
                // Join the columns with a semicolon and write to the file
                writer.write(String.join(";", rowData));
                writer.newLine(); // Add a new line after each entry
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  
	
	public void returnRental() {
			userRentals = new ArrayList<>();

	        // Specify the file path
	        String filePath = ".\\Documents\\User_Rentals.txt";

	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                // Add each line to the ArrayList
	            	String[] rentalDetails = line.split(";");
	                userRentals.add(rentalDetails);
	                
	            }
	        } 
	        catch (IOException e) {
	            System.out.println("Failed to read from User_Rentals.txt");
	        }
	        
	        // Create a dropdown with user names
	        String[] userNames = getUserNames();
	        JComboBox<String> userDropdown = new JComboBox<>(userNames);

	        // Create "View Items" and "Finished" buttons
	        JButton viewItemsButton = new JButton("View Items");
	        JButton finishedButton = new JButton("Finished");

	        // Add action listener for "View Items" button (customize as needed)
	        viewItemsButton.addActionListener(e -> {
	            String selectedUser = (String) userDropdown.getSelectedItem();
	            List<String[]> userRentalsFiltered = getUserRentalsForUser(selectedUser);

	            JPanel itemsPanel = new JPanel(new GridLayout(userRentalsFiltered.size()+1, 3, 4, 4));

	            for (String[] rentalDetails : userRentalsFiltered) {
	                JCheckBox checkBox = new JCheckBox();
	                checkBox.setText(rentalDetails[2]); // Adjust index
	                
	                String[] quantityOptions = getQuantityOptions(Integer.parseInt(rentalDetails[3])); // Adjust index
	                JComboBox<String> quantityDropdown = new JComboBox<>(quantityOptions);
	                JLabel storeName = new JLabel((String) rentalDetails[0]);
	                
	                itemsPanel.add(checkBox);
	                itemsPanel.add(quantityDropdown);
	                itemsPanel.add(storeName);
	            }
	            
	            // Create "Return" and "Cancel" buttons
	            JButton returnButton = new JButton("Return");
	            JButton cancelButton = new JButton("Cancel");
	            
	            // Add action listener for "Return" button (customize as needed)
	            returnButton.addActionListener(returnEvent -> {
	            	
	            	// Retrieve the selected items and quantities	            	
	                for (Component component : itemsPanel.getComponents()) {
	                    if (component instanceof JCheckBox) {
	                        JCheckBox checkBox = (JCheckBox) component;

	                        // Check if the item is selected
	                        if (checkBox.isSelected()) {
	                            String itemName = checkBox.getText();
	                            // Extract item name if necessary

	                            for (Component innerComponent : itemsPanel.getComponents()) {
	                                if (innerComponent instanceof JComboBox) {
	                                    JComboBox<String> quantityDropdown = (JComboBox<String>) innerComponent;

	                                    // Check if this JComboBox is associated with the current JCheckBox
	                                    if (checkBox.getY() == quantityDropdown.getY()) {
	                                        // Retrieve the selected quantity
	                                        String selectedQuantity = (String) quantityDropdown.getSelectedItem();
	                                        
	                                     // Retrieve the associated store name label
	                                        JLabel storeLabel = (JLabel) itemsPanel.getComponent(itemsPanel.getComponentZOrder(quantityDropdown) + 1);

	                                        returnItemToStore(itemName, Integer.parseInt(selectedQuantity), storeLabel.getText());
	                                        removeItemFromRental(itemName, Integer.parseInt(selectedQuantity));
	                                        
	                                        
	                                    }
	                                }
	                            }
	                        }
	                    }
	                    
	                    Container parent = itemsPanel.getParent();

	                    // Traverse up the hierarchy until a JDialog is found
	                    while (parent != null && !(parent instanceof Window)) {
	                    	parent = parent.getParent();
	                    }

	                    // Dispose of the window
	                    if (parent instanceof Window) {
	                    	((Window) parent).dispose();
	                    }
	                    
	                    //Remove the grandparent JDialog
	                    Container grandParent = parent != null ? parent.getParent() : null;
	                    if (grandParent instanceof Window) {
	                        ((Window) grandParent).dispose();
	                    }
	                }
	            });

	            // Add action listener for "Cancel" button (customize as needed)
	            cancelButton.addActionListener(cancelEvent -> {
	                //canceling the return process
	                Container parent = cancelButton.getParent();
	                while (parent != null && !(parent instanceof JOptionPane)) {
	                    parent = parent.getParent();
	                }
	                if (parent instanceof JOptionPane) {
	                    ((JOptionPane) parent).setValue(JOptionPane.CLOSED_OPTION);
	                }
	            });

	            // Add buttons to the itemsPanel
	            itemsPanel.add(returnButton);
	            itemsPanel.add(cancelButton);
	            JOptionPane.showOptionDialog(null, itemsPanel, "Return Rental - " + selectedUser, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
	        });

	        // Add action listener for "Finished" button (customize as needed)
	        finishedButton.addActionListener(e -> {
	            // Add logic to finish and close the option pane
	            JOptionPane.getRootFrame().dispose();
	        });

	        // Create a panel with components
	        JPanel panel = new JPanel();
	        panel.add(new JLabel("Select User:"));
	        panel.add(userDropdown);
	        panel.add(viewItemsButton);
	        panel.add(finishedButton);

	        // Show the option pane with the panel
	        JOptionPane.showOptionDialog(null, panel, "Return Rental", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);

	    }
	private String[] getQuantityOptions(int stockCount) {
			List<String> options = new ArrayList<>();
			for (int i = 1; i <= stockCount; i++) {
				options.add(String.valueOf(i));
			}
			return options.toArray(new String[0]);
		}
	
	private List<String[]> getUserRentalsForUser(String selectedUser) {
			List<String[]> userRentalsFiltered = new ArrayList<>();
			for (String[] rentalDetails : userRentals) {
				if (rentalDetails.length > 1 && rentalDetails[1].equals(selectedUser)) {
					userRentalsFiltered.add(rentalDetails);
				}
			}
			return userRentalsFiltered;
		}
	
	private void removeItemFromRental(String itemName, int selectedQuantity) {
		
	    String filePath = ".\\Documents\\User_Rentals.txt";
	    List<String> updatedLines = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(";");
	            String currentItemName = parts[2];
	            int currentQuantity = Integer.parseInt(parts[3]);

	            if (currentItemName.equals(itemName)) {
	                currentQuantity -= selectedQuantity;
	                
	                // If the quantity becomes zero or negative, skip this line
	                if (currentQuantity <= 0) {
	                    continue;
	                }

	                // Update the quantity in the line
	                String updatedLine = parts[0] + ";" + parts[1] + ";" + currentItemName + ";" + currentQuantity;
	                updatedLines.add(updatedLine);
	            } else {
	                updatedLines.add(line);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Failed to remove item from rental.");
	        return;
	    }

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        for (String updatedLine : updatedLines) {
	            writer.write(updatedLine);
	            writer.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Failed to update User_Rentals.txt after removing item.");
	    }
	}
	
    private String[] getUserNames() {
        Set<String> userNames = new HashSet<>();
        for (String[] rentalDetails : userRentals) {
            if (rentalDetails.length > 0) {
                userNames.add(rentalDetails[1]);
            }
        }
        return userNames.toArray(new String[0]);
    }
    
    private void returnItemToStore(String itemName, int quantity, String storeName) {
    	//Store name
    	String storeFilePath = ".\\Documents\\Buy_Better_" + storeName + ".txt";
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(storeFilePath));) {

    		String line;
        	List<String> lines = new ArrayList<>();

        	while ((line = reader.readLine()) != null) {
        		
        		String[] parts = line.split(";");
        		String currentItemName = parts[1];
        		int currentQuantity = Integer.parseInt(parts[5]);
        		
        		if (currentItemName.equals(itemName) && parts[6].equals("rent")) {
        			currentQuantity += quantity;
        			
        			String updatedLine = parts[0] + ";" + parts[1] + ";" + parts[2] + ";" + parts[3] +
        					";" + parts[4] + ";" + currentQuantity + ";" + parts[6];
        			
        			lines.add(updatedLine);
        		}
        		
        		else {
        			lines.add(line);
        		}
        	}
        	
        	try(BufferedWriter writer = new BufferedWriter(new FileWriter(storeFilePath))){
        	
        		for (String updatedLine : lines) {
        			writer.append(updatedLine);
        			writer.newLine();
        		}
            }
        }
    	
    	catch(IOException e) {
    		System.out.println("Failed to return item to the store.");
    	}
    }
	
	public static ArrayList<String[]> loadInventory(String store) {
	    String fileName = ".\\Documents\\Buy_Better_" + store + ".txt";
	    ArrayList<String[]> inventoryList = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] rowData = line.split(";");
	            inventoryList.add(rowData);
	        }
	    } 
	    catch (IOException e) {
	        e.printStackTrace(); 
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
	    } 
	    
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    return lines;
	}
	
}
