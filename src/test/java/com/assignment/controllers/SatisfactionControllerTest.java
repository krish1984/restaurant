package com.assignment.controllers;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.assignment.RestaurantApplication;
import com.assignment.controllers.SatisfactionController;
import com.assignment.excceptions.RestaurantException;
import com.assignment.services.MenuService;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
public class SatisfactionControllerTest {
	@Autowired
	ResourceLoader resourceLoader;

	SatisfactionController sController;
	MenuService menuServiceMock;
	Map<Integer, Integer> menuData = null;
	
	@Before
	public void setUp() throws Exception {
		menuData = prepareMenuData();
	}
	
	
	@Test
	public void testSuccessfulMaximumSatisfaction() throws RestaurantException {
		sController = new SatisfactionController();
		menuServiceMock = EasyMock.createMock(MenuService.class);
		setPrivateValue(sController, menuServiceMock);
		EasyMock.expect(menuServiceMock.getMenuInformation()).andReturn(menuData).anyTimes();
		EasyMock.replay(menuServiceMock);
		
		String result = null;
		result=sController.getMaximumSatisfaction(5, false);
		assertTrue(result!=null);
		assertTrue(result.contains("Maximum Satisfaction: 10 in 2 sec."));
		
		result=sController.getMaximumSatisfaction(7, false);
		assertTrue(result!=null);
		assertTrue(result.contains("Maximum Satisfaction: 15 in 7 sec."));
		
		result=sController.getMaximumSatisfaction(0, false);
		assertTrue(result!=null);
		assertTrue(result.contains("Maximum Satisfaction: 0 in 0 sec."));
	}
	
	
	@Test
	public void testFailMaximumSatisfaction() throws RestaurantException {
		sController = new SatisfactionController();
		menuServiceMock = EasyMock.createMock(MenuService.class);
		setPrivateValue(sController, menuServiceMock);
		EasyMock.expect(menuServiceMock.getMenuInformation()).andThrow(new RestaurantException("No data file found"));
		EasyMock.replay(menuServiceMock);
		
		String result = null;
		result=sController.getMaximumSatisfaction(5, false);
		assertTrue(result!=null);
		assertTrue(result.contains("No data file found"));
		
	}
	

	private void setPrivateValue(Object target, Object value) {
		Field f;
		try {
			f = target.getClass().getDeclaredField("menuService");
			f.setAccessible(true);
			f.set(target, value);
		} catch (Exception e) {
			Assert.fail("Error setting field value");
		}
	}

	private Map<Integer, Integer> prepareMenuData() {
		Resource dataFile = resourceLoader.getResource("test_data.txt");
		FileReader fr = null;
		BufferedReader br = null;
		Map<Integer, Integer> satisfactionsToTimesMap = new TreeMap<Integer, Integer>(Collections.reverseOrder());
		try {
			fr = new FileReader(dataFile.getFile());
			br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				String[] parts = line.split("\\s+");
				Integer itemSatisfaction = null;
				Integer timeToEatItem = null;
				try {
					itemSatisfaction = Integer.parseInt(parts[0]);
					timeToEatItem = Integer.parseInt(parts[1]);
					satisfactionsToTimesMap.put(itemSatisfaction, timeToEatItem);
				} catch (NumberFormatException e) {
				}
				line = br.readLine();
			}

			return satisfactionsToTimesMap;
		} catch (Exception e) {
			Assert.fail("Error processing test data.");
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

}
