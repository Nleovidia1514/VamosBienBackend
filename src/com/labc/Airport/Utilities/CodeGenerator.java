package com.labc.Airport.Utilities;

public class CodeGenerator {
	
	public static String generateCode() {
		String code = "";
		for(int i = 0; i < 4; i++)
			code += (int) (Math.random() * 9);
		return code;
	}
	
}