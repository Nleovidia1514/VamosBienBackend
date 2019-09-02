package com.labc.Airport.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

public class FlightUpdater {
	private static PropertiesReader propRead = PropertiesReader.getInstance();
	
	public static boolean updateFlight(HashMap<String, String> params) {
		try (Connection conn = Pool.getConnection();
				PreparedStatement stm = conn.prepareStatement(propRead.getValue("update_flight"))){
			stm.setString(1, params.get("depart"));
			stm.setString(2, params.get("destiny"));
			stm.setTimestamp(3, Timestamp.valueOf(params.get("dateDepart")));
			stm.setTimestamp(4, Timestamp.valueOf(params.get("timeOfDepart")));
			stm.setTimestamp(5, Timestamp.valueOf(params.get("timeOfArrival")));
			stm.setFloat(6, Float.valueOf(params.get("price")));
			stm.setString(7, params.get("description"));
			stm.setString(8, params.get("estado"));
			stm.setInt(9, Integer.valueOf(params.get("id")));
			stm.execute();
			return true;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
