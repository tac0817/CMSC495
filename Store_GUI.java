/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventory_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;

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
        resultList.removeAll();
        
        if (searchBox.getText().contentEquals("Enter your product name here")
                || searchBox.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "No product name was entered.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                read = new BufferedReader(new FileReader(file));
                String fileLine = read.readLine();  
                results.setText("results found");

                if (fileLine != null) {
                    results = new JLabel("No results found");
                } else {
                    results = new JLabel("Results found");
                }

                while (fileLine != null) {
                    if (fileLine.contains(searchBox.getText())) {
                        resultList.add(new JLabel(fileLine));
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
        new Store_GUI();
    }
}
