package com.labc.Airport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.labc.Airport.Utilities.PropertiesReader;
import com.labc.Airport.Utilities.UserFactory;
import com.labc.Airport.Utilities.User.UserOption;

/**
 * Servlet implementation class EndPoint
 */
@WebServlet("/EndPoint")
public class EndPoint extends HttpServlet {
	PropertiesReader pr = PropertiesReader.getInstance();
	UserFactory uf = UserFactory.getInstance();
	private static final long serialVersionUID = 1L;
       
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EndPoint() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub			
		PrintWriter out = response.getWriter();
		if(request.getQueryString() != null) {
			if(request.getParameter("q").equals("checkAuth")) {
				response.setContentType("application/json");
				if(request.getSession(false) != null) 
					out.write("{\"userIsAuth\":true}");
				else
					out.write("{\"userIsAuth\":false}");
			}
			else if(request.getParameter("q").equals("logout")) {
				if(request.getSession(false) != null)
					request.getSession().invalidate();
			}
		}
		else {
			if(request.getSession(false) != null) {
				response.setHeader("Content-Type", "application/json");
				HttpSession session = request.getSession(false);
				ArrayList<UserOption> options = uf.getUser((String)session.getAttribute("userCategory")).getOptions();
				String jsonToSend = "{\"options\": {";
				for (int i = 0; i < options.size(); i++) {
					UserOption userOption = options.get(i);
					String jsonOption = "";
					if(!jsonToSend.equals("{\"options\": {")) {
						jsonOption += ",";
					}
					jsonOption += "\""+userOption.getName()+"\":{\"link\":\""+userOption.getLink()+"\",\"exact\":true}";
					jsonToSend += jsonOption;
				}
				jsonToSend += "},\"username\":\""+(String)session.getAttribute("username")+"\",\"userPicture\":\""+(String)session.getAttribute("userPicture")+"\"}";
				out.write(jsonToSend);
			}
			else
				out.write("{\"options\":null,\"username\":null,\"userPicture\":null}");
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
