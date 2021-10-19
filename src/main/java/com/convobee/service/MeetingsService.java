package com.convobee.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.data.entity.Meetings;
import com.convobee.data.mapper.MeetingsMapper;
import com.convobee.data.repository.InterestsRepo;
import com.convobee.data.repository.MeetingsRepo;
import com.convobee.utils.CommonUtil;
import com.convobee.utils.UserUtil;

@Service
public class MeetingsService {

	@Autowired
	MeetingsRepo meetingsRepo;

	@Autowired
	MeetingsMapper meetingsMapper;
	
	@Autowired
	UserUtil userUtil;
	
	@Autowired
	InterestsRepo interestsRepo;
	
	List<Integer> listOfUserIds = new LinkedList<Integer>();
	 
	public Map<LinkedList<Integer>, String> addActiveUsers(HttpServletRequest request)
	{
		//Need to make this dynamic intead of hardcoding
		//listOfUserIds.add(userUtil.getLoggedInUserId(request));
		listOfUserIds.add(3047);
		listOfUserIds.add(3051);
		return initiateMeeting();
	}
	/*
	 * TO DO
	 * Check with more than 2 entries
	 * Handle if no others interest is matched for a user
	 * Need to return what interest that 2 users are matched with
	 * */
	
	public Map<LinkedList<Integer>, String> initiateMeeting() {
		Map<LinkedList<Integer>, String> userVsInterests = new LinkedHashMap<LinkedList<Integer>, String>();
		List<Integer> listOfUsers = new LinkedList<Integer>();
		List<LinkedList<String>> listOfUserInterests = new LinkedList<LinkedList<String>>();
		for(Integer i=0; i<listOfUserIds.size(); i++)
		{
			listOfUsers.add(listOfUserIds.get(i));
			listOfUserInterests.add(interestsRepo.findInterestByUser(listOfUserIds.get(i)));
		}
		int sizeOfInterestsList = listOfUserInterests.size();
		for(int i=0; i<sizeOfInterestsList; i++) {
			LinkedList<Integer> userVsOppUser = new LinkedList<Integer>();
			List<String> list1 = listOfUserInterests.get(i);
			for(int j=i+1; j<sizeOfInterestsList;j++) {
				List<String> list2 = listOfUserInterests.get(j);
				
				/* Comparing 2 lists and finding out the similar interests between them */
				List<String> afterCompare = list1.stream().filter(list2::contains).collect(Collectors.toList());
				
				/* Checking whether any common interests found */
				if(!afterCompare.isEmpty()) {
					//System.out.println("Output = " + afterCompare);
					//System.out.println("Output Result = " + afterCompare.get(0));
					userVsOppUser.add(listOfUsers.get(i));
					userVsOppUser.add(listOfUsers.get(j));
					String meetingUrl = CommonUtil.getRandomUrl();
					userVsInterests.put(userVsOppUser, meetingUrl);
					LinkedList<Integer> reverseInteger = new LinkedList<Integer>(userVsOppUser);
					Collections.reverse(reverseInteger);
					userVsInterests.put(reverseInteger, meetingUrl);//Need to return this to nodejs
					int user_a_id = listOfUsers.get(i);
					int user_b_id = listOfUsers.get(j);
					Meetings meeting = meetingsMapper.mapMeetings(user_a_id, user_b_id, meetingUrl);
					meetingsRepo.save(meeting);
					listOfUserInterests.remove(list1);
					listOfUserInterests.remove(list2);
					listOfUsers.remove(Integer.valueOf(user_a_id));
					listOfUsers.remove(Integer.valueOf(user_b_id));
					sizeOfInterestsList = listOfUserInterests.size();
				}
			}

		}
		listOfUserIds.removeAll(listOfUserIds);
		return userVsInterests;
	}


}
