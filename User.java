package Inventory_System;

public class User {
	private String name, username, mainStore;
	private boolean isAdmin;
	
	public User(String name, String username, String mainStore, boolean isAdmin) {
		
		this.name = name;
		this.username = username;
		this.mainStore = mainStore;
		this.isAdmin = isAdmin;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getMainStore() {
		return mainStore;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
}
