package com.assignment.utils;

public class RestaurantUtils {

	
	public static void append(StringBuilder builder, String text, boolean shouldAppend){
		if(shouldAppend){
			builder.append(text);
		}
	}
}
