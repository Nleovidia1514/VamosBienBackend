package com.labc.Airport.Utilities.User;

import java.util.ArrayList;

public class User {
	public ArrayList<UserOption> options = new ArrayList<UserOption>();
	
	public User() {
		this.options.add(new UserOption("Profile", "/profile"));
		this.options.add(new UserOption("Logout", "/logout"));
	}
	
	public ArrayList<UserOption> getOptions() {
		return this.options;
	}
}
