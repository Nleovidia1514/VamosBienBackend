package com.labc.Airport.Utilities;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mailer {
	
	private static PropertiesReader pr = PropertiesReader.getInstance();
	
	public static boolean send(String to, String subject, String content, boolean html) {
		String host = "smtp.gmail.com";
		final String user = pr.getValue("email");
		final String password = pr.getValue("emPassword");
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", "465");
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, password);
				}
			}
		);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			if(!html)
				message.setText(content);
			else
				message.setContent(content,"text/html");
			Transport.send(message);
			return true;
		}catch(MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean send(String to, String subject, String content, boolean html, DataSource file) {
		String host = "smtp.gmail.com";
		final String user = pr.getValue("email");
		final String password = pr.getValue("emPassword");
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", "465");
		props.put("mail.mime.encodeeol.strict", "true");
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, password);
				}
			}
		);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			BodyPart messageBodyPart = new MimeBodyPart();
			if(!html)
				messageBodyPart.setText(content);
			else
				messageBodyPart.setContent(content,"text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			
			messageBodyPart.setDataHandler(new DataHandler(file));
			messageBodyPart.setFileName("Ticket");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			return true;
		}catch(MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
