package com.labc.Airport.Utilities.User;

public class Admin extends User {
	
	public Admin(){
		super();
		this.options.add(new UserOption("Manage Users","/userManager"));
		this.options.add(new UserOption("Manage Flights","/flightManager"));
	}
}
