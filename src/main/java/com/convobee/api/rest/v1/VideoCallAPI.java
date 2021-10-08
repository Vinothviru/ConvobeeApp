package com.convobee.api.rest.v1;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.utils.UserUtil;

@RestController
public class VideoCallAPI {

	@Autowired
	UserUtil userUtil;
	
	List<Integer> listOfUserIds = new LinkedList<Integer>();
	@RequestMapping(value = "/initiatemeeting", method = RequestMethod.GET)
	public List<Integer> initiateMeeting(HttpServletRequest request)
	{
		//listOfUserIds.add(userUtil.getLoggedInUserId(request));
		listOfUserIds.add(1);
		listOfUserIds.add(3);
		listOfUserIds.add(2898);
		listOfUserIds.add(2917);
		listOfUserIds.add(2931);
		
		System.out.println("First = " + listOfUserIds);
		return listOfUserIds;
	}
}
