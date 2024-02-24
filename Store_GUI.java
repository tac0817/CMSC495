package Inventory_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

public class Store_GUI extends JFrame {

    private JTextField searchBox;
    private int storeNum = 0;
    JLabel results;
    JPanel resultList;

    public Store_GUI() {
        setTitle("Store Page");
        setSize(350, 400);
        searchBox = new JTextField("Enter your product name here", 30);
        JLabel welcome = new JLabel("Welcome to [Store Name] store page!");
        JLabel stockText = new JLabel("Store: " + storeNum);
        results = new JLabel();
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSearchButtonClick();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 150, 10));
        resultList = new JPanel();
        resultList.setLayout(new BoxLayout(resultList, BoxLayout.Y_AXIS));

        panel.add(welcome);
        panel.add(stockText);
        panel.add(searchBox);
        panel.add(searchButton);
        panel.add(results);
        panel.add(resultList);
        add(panel);

        setVisible(true);

    }

    private void onSearchButtonClick() {
        File file = new File("");
        BufferedReader read;
        int i = 0;
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

                while (fileLine != null) {
                    if (fileLine.contains(searchBox.getText())) {
                        results.setText("Results Found");
                        products.add(new JLabel(fileLine));
                        products.get(i).addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent me) {
                                
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
    
 
    
    public static void main(String[] args) {
    	
    		LoginDisplay Login_GUI = new LoginDisplay();
    }
}