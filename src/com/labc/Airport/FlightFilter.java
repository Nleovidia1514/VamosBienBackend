package com.labc.Airport;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labc.Airport.Utilities.Pool;
import com.labc.Airport.Utilities.PropertiesReader;

@WebServlet("/FlightFilter")
public class FlightFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static PropertiesReader pr = PropertiesReader.getInstance();
   
    public FlightFilter() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int date = Integer.valueOf(request.getParameter("date"));
		int month = Integer.valueOf(request.getParameter("month"));
		int year =  Integer.valueOf(request.getParameter("year"));
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String destination = request.getParameter("destination");
		String jsonToSend = "{";
		PrintWriter out = response.getWriter();
		try(Connection conn = Pool.getConnection()) {
			PreparedStatement stm = conn.prepareStatement(pr.getValue("filter_flights"));
			stm.setInt(1, date);
			stm.setInt(2, month);
			stm.setInt(3, year);
			stm.setString(4, destination);
			ResultSet rs = stm.executeQuery();
			while(rs.next()) {
				String flight = "";
				if(!jsonToSend.equals("{")) {
					flight += ",";
				}
					flight += "\""+rs.getString("id_vuelo") + "\":{"
							+ "\"dateDepart\":\""+df.format(rs.getTimestamp("dateDepart_vuelo"))+"\","
							+ "\"depart\":\""+rs.getString("departure_vuelo")+"\","
							+ "\"destiny\":\""+rs.getString("destiny_vuelo")+"\","
							+ "\"picture\":\""+rs.getString("picture_vuelo")+"\","
							+ "\"description\":\""+rs.getString("description_vuelo")+"\","
							+ "\"estado\":\""+rs.getString("estado_vuelo")+"\""
							+ "}";
					jsonToSend += flight;
						
			}
			jsonToSend += "}";
			response.setStatus(200);
			out.write(jsonToSend);
		} catch (SQLException e) {
			response.setStatus(400);
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
