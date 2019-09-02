package com.labc.Airport;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.labc.Airport.Utilities.Encrypter;
import com.labc.Airport.Utilities.Mailer;
import com.labc.Airport.Utilities.Pool;
import com.labc.Airport.Utilities.PropertiesReader;

@WebServlet("/PasswordRecovery")
@MultipartConfig
public class PasswordRecovery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static PropertiesReader pr = PropertiesReader.getInstance();
       
    public PasswordRecovery() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		PrintWriter out = response.getWriter();
		String op = request.getParameter("op");
		String recoveryCode = null;
		String userEmail = null;
		try (Connection conn = Pool.getConnection()){
			switch (op) {
			case "sendCode":
				userEmail = request.getParameter("email");
				recoveryCode = generateCode(conn);	
				try(PreparedStatement stm = conn.prepareStatement(pr.getValue("update_verifCode"))) {
					stm.setString(1, recoveryCode);
					stm.setString(2, userEmail);
					stm.execute();
					Cookie cookie = new Cookie("email", userEmail);
					cookie.setMaxAge(300);
					response.addCookie(cookie);
					if(Mailer.send(userEmail, "Vamos bien Password Recovery", "<p>There was an attempt to recover"
							+ " your password in www.VamosBien.com, if this wasn't you please ignore this message"
							+ " otherwise, use this code to reset your password:</p><br><h1>" + recoveryCode+"</h1>", true))
						out.write("success");	
				}
				break;
			case "verifyCode":
				userEmail = request.getCookies()[1].getValue();
				try(PreparedStatement stm = conn.prepareStatement(pr.getValue("check_if_user_exists"))) {
					stm.setString(1, userEmail);
					ResultSet rs = stm.executeQuery();
					String username = null;
					String userCategory = null;
					String userPicture = null;
					while(rs.next()) {
						recoveryCode = rs.getString("recoveryCode_user");
						username = rs.getString("username_user");
						userCategory = rs.getString("cat_user");
						userPicture = rs.getString("picture_user");
					}
					if(request.getParameter("code").equals(recoveryCode)) {
						HttpSession session = request.getSession();
						session.setAttribute("username", username);
						session.setAttribute("userCategory", userCategory);
						session.setAttribute("userPicture", userPicture);
						session.setAttribute("userEmail", userEmail);
						out.write("success");
					}
					else
						out.write("failure");
				}
				break;
			case "resetPass":
				try(PreparedStatement stm = conn.prepareStatement("UPDATE Usuario SET pass_user = ? WHERE email_user = ?")) {
					HttpSession session = request.getSession(false);
					if(session != null) {
						stm.setString(1, Encrypter.getInstance().encrypt(request.getParameter("newPass")));
						stm.setString(2, (String) session.getAttribute("userEmail"));
						stm.execute();
						out.write("success");
					}
					else
						out.write("failure");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.write("failure");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static String generateCode(Connection conn) {
		String code = "";
		try {
			for(int i = 0; i < 4; i++)
				code += (int) (Math.random() * 9);
			try(PreparedStatement stm = conn.prepareStatement("SELECT recoveryCode_user FROM Usuario WHERE recoveryCode_user = ?")) {
				stm.setString(1, code);
				ResultSet rs = stm.executeQuery();
				if(rs.next())
					code = generateCode(conn);
				return code;
			}
		} catch (SQLException e) {	
			e.printStackTrace();
			return null;
		}
	}
}
