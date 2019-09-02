package com.labc.Airport;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.labc.Airport.Utilities.Mailer;
import com.labc.Airport.Utilities.ParameterStringBuilder;
import com.labc.Airport.Utilities.Pool;
import com.labc.Airport.Utilities.PropertiesReader;

@WebServlet("/Payment")
@MultipartConfig()
public class Payment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static PropertiesReader propRead = PropertiesReader.getInstance();

      
    public Payment() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("KeyId", propRead.getValue("instapago_key"));
		parameters.put("PublicKeyId",propRead.getValue("instapago_publicKey"));
		for(Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			parameters.put(entry.getKey(), entry.getValue()[0]);
		}
		URL instapagoAPI = new URL("https://api.instapago.com/payment");
		HttpURLConnection conn = (HttpURLConnection) instapagoAPI.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		out.flush();
		out.close();
		int status = conn.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		conn.disconnect();
		response.setContentType("application/json");
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(content.toString());
			File ticketsDir = new File(
					propRead.getValue("path")+"/Users/"+
					request.getSession(false).getAttribute("username")
					+"/tickets");
			ticketsDir.mkdirs();
			File ticketPdf = new File(ticketsDir.getAbsolutePath()+"/Ticket-"+json.get("reference")+".pdf");
			ticketPdf.createNewFile();
			URL pdfConverterAPI = new URL("https://selectpdf.com/api2/convert/");
			HttpURLConnection pdfConn = (HttpURLConnection) pdfConverterAPI.openConnection();
			pdfConn.setRequestMethod("POST");
			pdfConn.setDoOutput(true);
			pdfConn.setRequestProperty("Content-Type", "application/json");
			DataOutputStream pdfOut = new DataOutputStream(pdfConn.getOutputStream());
			pdfOut.writeBytes("{"
					+ "key:\""+propRead.getValue("html_to_pdf_key")+"\","
					+ "html:\"<p>ESTO 	VALE POR UN TICKET PARA:</p><bt><h1>"+parameters.get("Description")+"</h1>\"}");
			pdfOut.flush();
			pdfOut.close();
			BufferedInputStream inputStream = new BufferedInputStream(pdfConn.getInputStream());
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(ticketPdf));
			byte[] b = new byte[8 * 1024];
			int read = 0;
			while ((read = inputStream.read(b)) > -1) {
				outputStream.write(b, 0, read);
			}
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			System.out.println("pdf ticket succesfully generated at: " + ticketPdf.getAbsolutePath());
			try(Connection sqlConn = Pool.getConnection();
			PreparedStatement stm = sqlConn.prepareStatement(propRead.getValue("create_new_payment"));
					PreparedStatement stm1 = sqlConn.prepareStatement("SELECT email_user FROM Usuario WHERE username_user = ?")) {
				stm.setString(1, (String) json.get("id"));	
				stm.setString(2, (String) json.get("message"));
				stm.setString(3, json.get("success")+"");
				stm.setString(4, (String) json.get("code"));
				stm.setString(5, (String) json.get("reference"));
				stm.setString(6, "culo");
				stm.setTimestamp(7, Timestamp.valueOf("2019-10-30 00:00:00"));
				stm.execute();
				stm1.setString(1, (String) request.getSession(false).getAttribute("username"));
				ResultSet rs = stm1.executeQuery();
				String to = null;
				while(rs.next())
					to = rs.getString("email_user");
				Mailer.send(to, "THANK YOU FOR YOUR PURCHASE", (String) json.get("voucher"), true, new FileDataSource(ticketPdf.getAbsolutePath()));
				response.getWriter().write("operation successful!");
				
			}
		} catch (ParseException | SQLException e) {
			e.printStackTrace();
		}
		response.getWriter().append(content.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
