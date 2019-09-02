package com.labc.Airport.Utilities;

import com.labc.Airport.Utilities.User.Admin;
import com.labc.Airport.Utilities.User.User;

public class UserFactory {
	private static UserFactory instance = new UserFactory();
	
	public User getUser(String type) {
		User user = null;
		if(type.equals("USER"))
			user = new User();
		else if(type.equals("ADMIN")) 
			user = new Admin();
		
		return user;
	}
	
	public static UserFactory getInstance() {
		return instance;
	}

}
