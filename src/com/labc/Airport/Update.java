package com.labc.Airport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.labc.Airport.Utilities.FlightUpdater;

/**
 * Servlet implementation class Update
 */
@WebServlet("/Update")
@MultipartConfig
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public Update() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> parameters = new HashMap<String, String>();
		for(Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			parameters.put(entry.getKey(), entry.getValue()[0]);
		}
		if(FlightUpdater.updateFlight(parameters)) 
			response.getWriter().write("Flght updated successfully!");
		else
			response.getWriter().write("Error updating flight!");			
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
