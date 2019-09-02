package com.labc.Airport.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

public class FlightCreator {
	private static PropertiesReader propRead = PropertiesReader.getInstance();
	private static String insertFlightQuery = propRead.getValue("insert_flight");
	
	public static boolean insertFlight(HashMap<String, String> parameters) {
			
		try (Connection conn = Pool.getConnection();
				PreparedStatement stm = conn.prepareStatement(insertFlightQuery)) {
			stm.setString(1, parameters.get("airlineId"));
			stm.setInt(2, Integer.valueOf(parameters.get("capacity")));
			stm.setTimestamp(3, Timestamp.valueOf((String)(parameters.get("dateOfDepart") + " 00:00:00")));
			stm.setTimestamp(4, Timestamp.valueOf((String)(parameters.get("dateOfDepart") + " " + parameters.get("timeOfDepart")+":00")));
			stm.setTimestamp(5, Timestamp.valueOf((String)(parameters.get("dateOfDepart") + " " + parameters.get("timeOfArrival")+":00")));
			stm.setString(6, parameters.get("placeOfDepart"));
			stm.setString(7, parameters.get("placeOfArrival"));
			stm.setDouble(8, Double.valueOf(parameters.get("price")));
			stm.setString(9, parameters.get("description"));
			return stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}