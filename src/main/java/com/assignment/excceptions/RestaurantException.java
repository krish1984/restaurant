package com.assignment.excceptions;

public class RestaurantException extends Exception{
	private static final long serialVersionUID = 1L;

	public RestaurantException(String message) {
	        super(message);
	}
	
	public RestaurantException(String message, Throwable e) {
        super(message,e);
	}
}
