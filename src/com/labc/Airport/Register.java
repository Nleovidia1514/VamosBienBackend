package com.labc.Airport;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.labc.Airport.Utilities.Client;
import com.labc.Airport.Utilities.Pool;
import com.labc.Airport.Utilities.PropertiesReader;

@WebServlet("/Register")
@MultipartConfig()
public class Register extends HttpServlet {
	private Client cli;
	private static final long serialVersionUID = 1L;
	private static Object lock = new Object();
       //f
    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-Type", "text/plain");
		Map<String,String[]> params = request.getParameterMap();
		cli = new Client();			
		if(cli.register(params)) {
			PropertiesReader propRead = PropertiesReader.getInstance();
			File file = new File(propRead.getValue("path")+"/Users/"+request.getParameter("username"));
			if(!file.mkdirs()) {
				response.getWriter().println("There was an error");
				return;
			}
			HttpSession session = request.getSession();
			session.setAttribute("username", request.getParameter("username"));
			session.setAttribute("userCategory", request.getParameter("accountType"));
			
			try (Connection conn = Pool.getConnection();
			PreparedStatement stm = conn.prepareStatement(propRead.getValue("get_user_pic"))) {
				stm.setString(1, (String) session.getAttribute("username"));
				ResultSet rs = stm.executeQuery();
				while(rs.next()) {
					session.setAttribute("userPicture", rs.getString(1));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			response.getWriter().print("Registered");
		}
		else
			response.getWriter().print("not successful");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
