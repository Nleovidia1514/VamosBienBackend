package com.labc.Airport;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labc.Airport.Utilities.Pool;
import com.labc.Airport.Utilities.PropertiesReader;

@WebServlet("/Flights")
public class Flights extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static PropertiesReader propRead = PropertiesReader.getInstance();
   
    public Flights() {
        super();     

    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();	
		String jsonToSend = "{";
		try (Connection conn = Pool.getConnection()){
			if(request.getQueryString() != null) {
				try(PreparedStatement stm = conn.prepareStatement(propRead.getValue("fetch_flight"))){
					stm.setInt(1, Integer.valueOf(request.getParameter("id")));
					ResultSet rs = stm.executeQuery();
					while(rs.next()) {
						jsonToSend += ""	
								+ "\"dateDepart\":\""+rs.getTimestamp("dateDepart_vuelo")+"\","
								+ "\"timeOfDepart\":\""+rs.getString("timeDepart_vuelo")+"\","
								+ "\"timeOfArrival\":\""+rs.getString("timeArriv_vuelo")+"\","
								+ "\"depart\":\""+rs.getString("departure_vuelo")+"\","
								+ "\"destiny\":\""+rs.getString("destiny_vuelo")+"\","
								+ "\"picture\":\""+rs.getString("picture_vuelo")+"\","
								+ "\"price\":"+rs.getString("price_vuelo")+","
								+ "\"estado\":\""+rs.getString("estado_vuelo")+"\","
								+ "\"description\":\""+rs.getString("description_vuelo")+"\""
								+ "}";
					}
					out.write(jsonToSend);
				}
						
			}	
			else {
				try(PreparedStatement stm = conn.prepareStatement(propRead.getValue("fetch_flights"));) {
					ResultSet rs = stm.executeQuery();
					while(rs.next()) {
						String flight = "";
						if(!jsonToSend.equals("{")) {
							flight += ",";
						}
							flight += "\""+rs.getString("id_vuelo") + "\":{"
									+ "\"dateDepart\":\""+rs.getTimestamp("dateDepart_vuelo")+"\","
									+ "\"depart\":\""+rs.getString("departure_vuelo")+"\","
									+ "\"destiny\":\""+rs.getString("destiny_vuelo")+"\","
									+ "\"picture\":\""+rs.getString("picture_vuelo")+"\","
									+ "\"description\":\""+rs.getString("description_vuelo")+"\","
									+ "\"estado\":\""+rs.getString("estado_vuelo")+"\""
									+ "}";
							jsonToSend += flight;
								
					}
					jsonToSend += "}";
					out.write(jsonToSend);
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();	
		
		try (Connection conn = Pool.getConnection();) {
			try(PreparedStatement stm = conn.prepareStatement(propRead.getValue("delete_flight"))) {
				stm.setInt(1, Integer.valueOf(request.getParameter("id")));
				stm.execute();
				response.setStatus(200);
				out.write("Flight deleted succesfully!");
			}
		} catch (SQLException e) {
			out.write("There was an error deleting the flight!");
			e.printStackTrace();
		}
	}
	

}
