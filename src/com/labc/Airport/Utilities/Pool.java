package com.labc.Airport.Utilities;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class Pool {
	private static BasicDataSource ds = new BasicDataSource();
	private static PropertiesReader propRead = PropertiesReader.getInstance();
	
	static {
		ds.setUrl("jdbc:postgresql:"+propRead.getValue("database"));
		ds.setUsername(propRead.getValue("dbUser"));
		ds.setPassword( propRead.getValue("dbPassword"));
		ds.setMinIdle(Integer.valueOf(propRead.getValue("minimum")));
		ds.setMaxIdle(Integer.valueOf(propRead.getValue("minimum")+15));
		ds.setMaxOpenPreparedStatements(Integer.valueOf(propRead.getValue("maximum")));
		ds.setDriverClassName("org.postgresql.Driver");
		
	}
	
	
	
	public static Connection getConnection()  {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
