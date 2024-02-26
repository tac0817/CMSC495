package Inventory_System;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginDisplay extends JFrame{
	
	private JTextField usernameText;
	private JPasswordField passwordText;
	private Boolean LoggedIn = false;
	
	public LoginDisplay() {
		setTitle("Inventory Login");
		setSize(400, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
		
		JLabel usernameLabel = new JLabel("Username: ");
		usernameText = new JTextField();
		JLabel passwordLabel = new JLabel("Password: ");
		passwordText = new JPasswordField();
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		JButton loginButton = new JButton("Login");
		JButton registerButton = new JButton("Register");
		
		loginButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
			User user = Login.getCurrentUser();
				onLoginButtonClick();
				System.out.println(user.getName() + "\n" + user.getUsername() + "\n" + user.getMainStore() + "\n" + user.isAdmin());
			}
		});
		
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRegisterButtonClick();
            }
        });
		
		fieldsPanel.add(usernameLabel);
		fieldsPanel.add(usernameText);
		fieldsPanel.add(passwordLabel);
		fieldsPanel.add(passwordText);
		
		buttonPanel.add(loginButton);
		buttonPanel.add(registerButton);
		
		mainPanel.add(fieldsPanel);
		mainPanel.add(buttonPanel);
		
		add(mainPanel);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public Boolean getLoggedInStatus() {
		
		return LoggedIn;
	}
	
	private void onLoginButtonClick() {
        String username = usernameText.getText();
        char[] passwordChars = passwordText.getPassword();
        String password = new String(passwordChars);

        // Call verifyLogin method from PasswordHashing class
        boolean loginSuccessful = false;
        
		try {
			loginSuccessful = Login.verifyLogin(username, password);
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (loginSuccessful) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            LoggedIn = true; 
            dispose();
            new Store_GUI();
        } 
        
        else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            LoggedIn = false;
        }

        // Clear the password field for security
        passwordText.setText("");
	}
	
	private void onRegisterButtonClick() {
	    // Create a new JDialog for the registration form
	    JDialog registrationDialog = new JDialog(this, "Registration Form", true);
	    registrationDialog.setSize(400, 250);
	    registrationDialog.setLayout(new GridLayout(7, 2, 10, 10)); // GridLayout with 5 rows, 2 columns, and gaps

	    // Add components to the registration dialog
	    JLabel nameLabel = new JLabel("Name:");
	    JTextField nameField = new JTextField();
	    JLabel usernameLabel = new JLabel("Username:");
	    JTextField usernameField = new JTextField();
	    JLabel passwordLabel = new JLabel("Password:");
	    JPasswordField passwordField = new JPasswordField();
	    JLabel rePasswordLabel = new JLabel("Re-enter Password:");
	    JPasswordField rePasswordField = new JPasswordField();
	    
        JLabel mainStoreLabel = new JLabel("Main Store:");
        String[] mainStores = {"Towson", "Columbia", "Annapolis", "Rockville", "Frederick"};
        JComboBox<String> mainStoreComboBox = new JComboBox<>(mainStores);
        
	    JButton registerButton = new JButton("Register");

	    registerButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Retrieve data from the fields
	            String name = nameField.getText();
	            String username = usernameField.getText();
	            
	            char[] passwordChars = passwordField.getPassword();
	            char[] rePasswordChars = rePasswordField.getPassword();
	            String password = new String(passwordChars);
	            String rePassword = new String(rePasswordChars);

	            String selectedMainStore = (String) mainStoreComboBox.getSelectedItem();
	            
	            
	            String passwordComplexity = Login.checkPasswordComplexity(password);
	            
	            if(!passwordComplexity.isBlank() && !passwordComplexity.isEmpty()) {
	            	JOptionPane.showMessageDialog(registrationDialog, passwordComplexity, "Weak Password", JOptionPane.WARNING_MESSAGE);
	            	return;
	            }
	            
	            // Check if any field is empty
	            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
	                JOptionPane.showMessageDialog(registrationDialog, "All fields must be filled.", "Incomplete Fields", JOptionPane.WARNING_MESSAGE);
	                return; // Exit the method without proceeding further
	            }
	            
	            // Check for existing username
	            if(Login.userExists(username)){
	            	JOptionPane.showMessageDialog(registrationDialog,  "Username is already in use. Please try another.",
	            			"Username in Use", JOptionPane.WARNING_MESSAGE);
	            	
	            	usernameField.setText("");
	            	return;
	            }
	            
	            // Check for password consistency
	            if (!password.equals(rePassword)) {
	                JOptionPane.showMessageDialog(registrationDialog, "Passwords do not match. Please re-enter.", 
	                		"Password Mismatch", JOptionPane.WARNING_MESSAGE);
	                // Clear password fields
	                passwordField.setText("");
	                rePasswordField.setText("");
	                // Exit the method without proceeding further
	                return; 
	            }
	            
	            Login.register(name, username, Login.hashPassword(password), selectedMainStore);
	            
	            JOptionPane.showMessageDialog(registrationDialog, "Thank you for registering with Better Buy's Inventory System",
	            		"Complete Registration", JOptionPane.INFORMATION_MESSAGE);
	            // Close the registration dialog
	            registrationDialog.dispose();
	        }
	    });

	    // Add components to the registration dialog
	    registrationDialog.add(nameLabel);
	    registrationDialog.add(nameField);
	    
	    registrationDialog.add(usernameLabel);
	    registrationDialog.add(usernameField);
	    
	    registrationDialog.add(passwordLabel);
	    registrationDialog.add(passwordField);
	    
	    registrationDialog.add(rePasswordLabel);
	    registrationDialog.add(rePasswordField);
	    
	    registrationDialog.add(mainStoreLabel);
	    registrationDialog.add(mainStoreComboBox);
	    //Create spacing with new label
	    registrationDialog.add(new JLabel());
	    registrationDialog.add(registerButton);

	    // Center the registration dialog on the parent frame
	    registrationDialog.setLocationRelativeTo(this);

	    // Set the dialog to be visible
	    registrationDialog.setVisible(true);
	}
}
