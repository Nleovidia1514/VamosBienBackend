package com.labc.Airport.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ParameterStringBuilder {
	public static String getParamsString(HashMap<String, String> parameters) {
		StringBuilder result = new StringBuilder();
		try {
			for ( Map.Entry<String, String> entry : parameters.entrySet()) {
					result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
					result.append("=");
					result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
					result.append("&");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length()-1)
				: resultString;
	}

}
