package com.ioilala.utils;

public class StringHelper {
	private StringHelper(){}

	// 如果是空格或者null，就返回false
	public static boolean isNullOrTrimEmpty(String str) {
		return str==null?false:str.trim().isEmpty();
	}
}
