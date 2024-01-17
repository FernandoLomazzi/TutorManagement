package com.tutormanagement.utils;

public class StringManager {
	public static String capitalize(String s) {
		if (s == null || s.isEmpty())
			return s;
		char firstLetter = Character.toUpperCase(s.charAt(0));
		return firstLetter + s.substring(1).toLowerCase();
	}

	public static void main(String[] argc) {
		String s = "", s2 = "perro", s3 = "PERRa", s4 = "a", s5 = "C", s6 = "pedr1t)w0";

		System.out.println(capitalize(s));
		System.out.println(capitalize(s2));
		System.out.println(capitalize(s3));
		System.out.println(capitalize(s4));
		System.out.println(capitalize(s5));
		System.out.println(capitalize(s6));
	}
}
