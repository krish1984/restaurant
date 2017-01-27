package com.assignment.controllers;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.services.MenuService;
import com.assignment.utils.RestaurantUtils;

@RestController
public class SatisfactionController {
	
	@Autowired
	MenuService menuService;
	
	
	@RequestMapping("/")
	public String getMaximumSatisfaction(
			@RequestParam(value="maxTime", required=false, defaultValue="10000") Integer givenTime,
			@RequestParam(value="debug", required=false, defaultValue="true") boolean debug
			) {
		Map<Integer, Integer> satisfactionsToTimesMap;
		StringBuilder output = new StringBuilder();
		Integer totalSatifaction = 0;
		Integer timeElapsed = 0;
		try {
			satisfactionsToTimesMap = menuService.getMenuInformation();
			Iterator<Integer> itemSatisfactions = satisfactionsToTimesMap.keySet().iterator();
			while(itemSatisfactions.hasNext()){
				Integer itemSatisfaction =itemSatisfactions.next();
				Integer timeTakenForItem =satisfactionsToTimesMap.get(itemSatisfaction);
				RestaurantUtils.append(output, "Evaluating item: " +  itemSatisfaction + " : " +  timeTakenForItem + " ", debug);
				if(givenTime >= timeElapsed){
					if(timeTakenForItem <= givenTime){
						timeElapsed = timeElapsed+timeTakenForItem;
						if(timeElapsed <= givenTime){
							totalSatifaction = totalSatifaction+itemSatisfaction;
							RestaurantUtils.append(output, " -- > picked<br/>",debug);
						}else{
							timeElapsed = timeElapsed-timeTakenForItem;
							RestaurantUtils.append(output, " -- > skipped<br/>",debug);
						}
						
					}else{
						RestaurantUtils.append(output, "---> skipped<br/>",debug);
					}
				}else{
					break;
				}
			}
		}catch(Exception e){
			output.append(e.getMessage());
			return output.toString();
		}
		output.append("\n<p style=\"background-color:lightgreen;width:30%;\">Maximum Satisfaction: "+ totalSatifaction + " in " + timeElapsed + " sec. </p>");
		return output.toString();
	}
	
	
	@PostConstruct
    public void init() {
        Assert.notNull(menuService, "helloService is null!");
    }

	
}
