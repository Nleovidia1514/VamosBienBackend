package com.labc.Airport.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class Client {
	private Connection conn;
	public HttpSession session;
	private static PropertiesReader pr = PropertiesReader.getInstance();
	private static Encrypter encr = Encrypter.getInstance();
	private static Object lock = new Object();
	
	public Client() {
		this.conn = this.getConnection();
	}
	
	public String[] login(Map<String,String[]> params) {
		String result[] = new String[3];
		String useremail = params.get("email")[0].toLowerCase().trim();
		String password = params.get("password")[0];
		System.out.println(password);
		try (PreparedStatement stm = conn.prepareStatement(pr.getValue("check_if_user_exists"))){
			stm.setString(1, useremail);
			ResultSet rs = stm.executeQuery();
			if(rs.next()) {					
				if(rs.getString(2).equals(encr.encrypt(password.trim()))) {
					result[0] = rs.getString(3);
					result[1] = rs.getString(4);
					result[2] = rs.getString(5);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean register(Map<String,String[]> params) {
		boolean result;
		String type = params.get("accountType")[0], email = params.get("email")[0], password = params.get("password")[0],
				firstName = params.get("firstName")[0], username = params.get("username")[0],
				lastName = params.get("lastName")[0], phoneNumber = params.get("phone")[0];
//				city = params.get("city")[0], state = params.get("state")[0], country = params.get("country")[0],
//				
		try (PreparedStatement stm = conn.prepareStatement(pr.getValue("create_new_user"))){
			stm.setString(1, type);
			stm.setString(2, email.toLowerCase().trim());
			stm.setString(3, encr.encrypt(password.trim()));
			stm.setString(4, username);
			stm.setString(5, firstName);
			stm.setString(6, lastName);
			stm.setString(7, "mcbo");
			stm.setString(8, "zul");
			stm.setString(9, null);
			stm.setString(10, "4001");
			stm.setString(11, phoneNumber);
			stm.execute();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	private Connection getConnection() {
		return Pool.getConnection();
	}
}
