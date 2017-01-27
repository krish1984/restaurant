package com.assignment.services;

import java.util.Map;

import com.assignment.excceptions.RestaurantException;

public interface MenuService {
	
	/**
	 * Reads the data.txt to build a Map of satisfaction amt of each dish to the amount of time taken to eat that dish.
	 * 
	 * throws  RestaurantException if no data.txt could be found or in any other exceptional case.
	 */
	public Map<Integer,Integer> getMenuInformation() throws RestaurantException;
}
