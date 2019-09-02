package com.labc.Airport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.labc.Airport.Utilities.Client;

@WebServlet("/Login")
@MultipartConfig()
public class Login extends HttpServlet {
	private Client cli;
	private static final long serialVersionUID = 1L;
       
    public Login() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Content-Type", "text/plain");
		Map<String,String[]> params = request.getParameterMap();
		cli = new Client();
		String[] userInfo = cli.login(params);
		PrintWriter out = response.getWriter();
		if(userInfo[0] != null) {	
			HttpSession session = request.getSession();
			session.setAttribute("username", userInfo[0]);
			session.setAttribute("userCategory", userInfo[1]);
			session.setAttribute("userPicture", userInfo[2]);
			out.write("Welcome " + userInfo[0]);
		}
		else {
			out.write("Incorrect e-mail/password");
		}	
		System.out.println("");
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
