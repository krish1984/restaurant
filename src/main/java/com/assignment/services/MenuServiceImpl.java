package com.assignment.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.assignment.excceptions.RestaurantException;

@Service
public class MenuServiceImpl implements MenuService{
	@Autowired
	ResourceLoader resourceLoader;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Reads the data.txt to build a Map of satisfaction amt of each dish to the amount of time taken to eat that dish.
	 * 
	 * throws  RestaurantException if no data.txt could be found or in any other exceptional case.
	 */
	@Override
	public Map<Integer, Integer> getMenuInformation() throws RestaurantException{
		Resource dataFile = resourceLoader.getResource("data.txt");
		if(dataFile==null)
			throw new RestaurantException("No data file found");
		
		FileReader fr = null;
		BufferedReader br = null;
		Map<Integer,Integer> satisfactionsToTimesMap  = new TreeMap<Integer,Integer>(Collections.reverseOrder());
		try{
			fr = new FileReader(dataFile.getFile());
			br = new BufferedReader(fr);
			String line = br.readLine();
			int count=0;
			while(line!=null){
				//ignore the 1st record as it represents the given time but not an actual item.
				if(count!=0){
					String[] parts = line.split("\\s+");
					Integer itemSatisfaction = null;
					Integer timeToEatItem = null;
					try{
						itemSatisfaction = Integer.parseInt(parts[0]);
						timeToEatItem = Integer.parseInt(parts[1]);
						satisfactionsToTimesMap.put(itemSatisfaction, timeToEatItem);
					}catch(NumberFormatException e){
						logger.error(e.getMessage(),e);
					}
				}
				++count;
				line=br.readLine();
			}
			
			return satisfactionsToTimesMap;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new RestaurantException(e.getMessage(),e);
		}
		finally{
			try {
				if(br!=null)
					br.close();
				if(fr!=null)
					fr.close();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

}
