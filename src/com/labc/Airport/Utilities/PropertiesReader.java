package com.labc.Airport.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	private static PropertiesReader props = new PropertiesReader();
	private Properties prop = new Properties();
	
	private PropertiesReader() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")){
			prop.load(input);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PropertiesReader getInstance() {
		return props;
	}
	
	public String getValue(String key) {
		return prop.getProperty(key);
	}
}
