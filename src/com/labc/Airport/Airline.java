package com.labc.Airport;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labc.Airport.Utilities.Pool;
import com.labc.Airport.Utilities.PropertiesReader;

/**
 * Servlet implementation class Airline
 */
@WebServlet("/Airline")
public class Airline extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static PropertiesReader propRead = PropertiesReader.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Airline() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String jsonToSend = "{";
		try (Connection conn = Pool.getConnection();Statement stm = conn.createStatement()){ 
			ResultSet rs = stm.executeQuery(propRead.getValue("fetch_airlines"));
			while(rs.next()) {
				if(!jsonToSend.equals("{"))
					jsonToSend += ",";
				jsonToSend += "\"" + rs.getString("id_airline") + "\":{\"name\":\""  
					+ rs.getString("name_airline") + "\",\"country\":\""
					+ rs.getString("country_airline") + "\"}";
			}
			jsonToSend += "}";
			out.write(jsonToSend);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
