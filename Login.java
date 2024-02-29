package Inventory_System;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Login {
	private static String filePath = "User_DB.txt";
	private static User currentUser;
	
	public static boolean userExists(String username) {
		boolean userExists = false;
	
		try {
		
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			List<String[]> userDataList = new ArrayList<>();
		
			for (String line : lines) {
			
				String[] parts = line.split(";");
				userDataList.add(parts);
			}
		
			for (String[] user : userDataList) {
			
				if(user[1].equals(username)) {
				userExists = true;
				}
			}
		}
	
		catch (IOException e) {
			System.out.println("File Could not be found.");
		}
	
		return userExists;
	}
	
    public static String hashPassword(String password) {
        try {
            // Use a secure random number generator to generate a salt
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);

            // Combine the password and salt, then hash them using SHA-256
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt);
            byte[] hashedPassword = messageDigest.digest(password.getBytes());

            // Encode the hashed password and salt as a Base64 string
            String encodedHashedPassword = Base64.getEncoder().encodeToString(hashedPassword);
            String encodedSalt = Base64.getEncoder().encodeToString(salt);

            // Store the salt along with the hashed password
            return encodedSalt + ":" + encodedHashedPassword;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }
    
    public static void register(String name, String username, String password, String storeSelection) {
    	
    	StringBuilder userData = new StringBuilder();
    	
    	userData.append(name).append(";");
    	userData.append(username).append(";");
    	userData.append(password).append(";");
    	userData.append("0").append(";");
    	userData.append(storeSelection);
    	
    	userData.append(System.lineSeparator());
    	
        // Write the data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(userData.toString());
        } catch (IOException e) {
            System.out.println("System could not register user at this Time.\nFailed to write to file.");
        }

        System.out.println(userData);
    }

    public static boolean verifyLogin(String inputUsername, String inputPassword) {
    	
    	boolean matchingPassword = false;
		
    	try {
    		
    		List<String> lines = Files.readAllLines(Paths.get(filePath));
    		
    		List<String[]> userDataList = new ArrayList<>();
    		
    		for (String line : lines) {
    			
    			String[] parts = line.split(";");
    			userDataList.add(parts);
    		}
    		
    		for (String[] user : userDataList) {
    			
    			if(user[1].equals(inputUsername)) {
    				boolean isAdmin = false;
    				if (user[3].equals("1")) {
    					isAdmin = true;
    				}
    				setCurrentUser(user[0], user[1], user[4], isAdmin);
    				
    				matchingPassword = passwordMatch(inputPassword, user[2]);
    			}
    		}
    	}
    	
    	catch (IOException e) {
    		System.out.println("File Could not be found.");
    	}
		return matchingPassword;
    }
    
    public static void setCurrentUser(String name, String username, String mainStore, Boolean isAdmin) {
    	
    	currentUser = new User(name, username, mainStore, isAdmin);
    }
    
    public static User getCurrentUser() {
    	return currentUser;
    }
    
    public static String checkPasswordComplexity(String password) {
        StringBuilder message = new StringBuilder();

        // Check for at least one digit, lowercase, uppercase, and special character
        
        // Check for at least 8 characters
        if (password.length() < 8) {
            message.append("Password must be at least 8 characters long.\n");
        }
        
        if (!password.matches(".*[0-9].*")) {
            message.append("Password must contain at least one digit.\n");
        }

        if (!password.matches(".*[a-z].*")) {
            message.append("Password must contain at least one lowercase letter.\n");
        }

        if (!password.matches(".*[A-Z].*")) {
            message.append("Password must contain at least one uppercase letter.\n");
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            message.append("Password must contain at least one special character.\n");
        }

        return message.toString();
    }
    
    public static boolean passwordMatch(String inputPassword, String storedPassword) {
    	
        // Extract the salt and hashed password from the stored string
        String[] parts = storedPassword.split(":");
        String encodedSalt = parts[0];
        String encodedStoredHashedPassword = parts[1];

        // Decode the salt and hashed password
        byte[] salt = Base64.getDecoder().decode(encodedSalt);
        byte[] storedHashedPasswordBytes = Base64.getDecoder().decode(encodedStoredHashedPassword);

        try {
            // Combine the entered password and decoded salt, then hash them using SHA-256
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt);
            byte[] hashedEnteredPassword = messageDigest.digest(inputPassword.getBytes());

            // Compare the hashed entered password with the stored hashed password
            return MessageDigest.isEqual(hashedEnteredPassword, storedHashedPasswordBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return false;
        }
    }
}