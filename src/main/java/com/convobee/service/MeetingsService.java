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

import com.convobee.api.rest.response.MeetingResponse;
import com.convobee.api.rest.response.VideoCallResponse;
import com.convobee.api.rest.response.builder.MeetingResponseBuilder;
import com.convobee.api.rest.response.builder.VideoCallResponseBuilder;
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
	
	@Autowired
	MeetingResponseBuilder meetingResponseBuilder;
	
	@Autowired
	VideoCallResponseBuilder videoCallResponseBuilder;
	
	List<Integer> listOfUserIds = new LinkedList<Integer>();
	 
	public VideoCallResponse addActiveUsers(HttpServletRequest request)
	{ 
		//Need to make this dynamic intead of hardcoding
		//listOfUserIds.add(userUtil.getLoggedInUserId(request));
		listOfUserIds.add(4512);
		listOfUserIds.add(3047);
		listOfUserIds.add(4499);
		listOfUserIds.add(3051);
		listOfUserIds.add(4505);
		listOfUserIds.add(4540);
		listOfUserIds.add(4510);
		listOfUserIds.add(4548);
		listOfUserIds.add(4581);
		
		return initiateMeeting();
	}
	/*
	 * TO DO
	 * Check with more than 2 entries - Done
	 * Handle if no others interest is matched for a user - Done
	 * Need to return what interest that 2 users are matched with - Done
	 * Follow the points in the google docs
	 * */
	
	//public Map<LinkedList<Integer>, String> initiateMeeting() {
	public VideoCallResponse initiateMeeting() {
		List<MeetingResponse> meetingResponseList = new LinkedList<MeetingResponse>();
		List<Integer> listOfUsers = new LinkedList<Integer>();
		List<LinkedList<String>> listOfUserInterests = new LinkedList<LinkedList<String>>();
		for(Integer i=0; i<listOfUserIds.size(); i++)
		{
			listOfUsers.add(listOfUserIds.get(i));
			listOfUserInterests.add(interestsRepo.findInterestByUser(listOfUserIds.get(i)));
		}
		int sizeOfInterestsList = listOfUserInterests.size();
		LinkedList<Integer> mismatchUser = new LinkedList<Integer>();
		for(int i=0; i<sizeOfInterestsList; i++) {
			int flag = 0;
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
					int user_a_id = listOfUsers.get(i);
					int user_b_id = listOfUsers.get(j);
					Meetings meeting = meetingsMapper.mapMeetings(user_a_id, user_b_id, meetingUrl);
					meetingsRepo.save(meeting);//Persisting into Meetings table
					meetingResponseList.add(meetingResponseBuilder.buildResponse(meeting.getMeetingid(), user_a_id, user_b_id, meetingUrl, afterCompare, list2));
					meetingResponseList.add(meetingResponseBuilder.buildResponse(meeting.getMeetingid(), user_b_id, user_a_id, meetingUrl, afterCompare, list1));
					listOfUserInterests.remove(list1);
					listOfUserInterests.remove(list2);
					listOfUsers.remove(Integer.valueOf(user_a_id));
					listOfUsers.remove(Integer.valueOf(user_b_id));
					sizeOfInterestsList = listOfUserInterests.size();
					i--;
					flag = 1;
					break;
				}
			}
			
			/* Handling unmatched users */
			if(mismatchUser.size()<2 && flag == 0)
			{
				int user_id = listOfUsers.get(i);
				mismatchUser.add(user_id);
				
//				listOfUserInterests.remove(listOfUserInterests.get(i));
//				listOfUsers.remove(Integer.valueOf(user_id));
//				sizeOfInterestsList = listOfUserInterests.size();
				//i--;
				
				if(mismatchUser.size()==2)
				{
					String meetingUrl = CommonUtil.getRandomUrl();
					Meetings meeting = meetingsMapper.mapMeetings(mismatchUser.get(0), mismatchUser.get(1), meetingUrl);
					meetingsRepo.save(meeting);//Persisting into Meetings table
					
					meetingResponseList.add(meetingResponseBuilder.buildResponse(meeting.getMeetingid(), mismatchUser.get(0), mismatchUser.get(1), meetingUrl, null, listOfUserInterests.get(listOfUsers.indexOf(mismatchUser.get(1)))));
					meetingResponseList.add(meetingResponseBuilder.buildResponse(meeting.getMeetingid(), mismatchUser.get(1), mismatchUser.get(0), meetingUrl, null, listOfUserInterests.get(listOfUsers.indexOf(mismatchUser.get(0)))));
					
					//listOfUserInterests.remove(listOfUserInterests.get(listOfUsers.indexOf(mismatchUser.get(0))));
					//listOfUserInterests.remove(listOfUserInterests.get(listOfUsers.indexOf(mismatchUser.get(1))));
					//listOfUsers.remove(Integer.valueOf(mismatchUser.get(0)));
					//listOfUsers.remove(Integer.valueOf(mismatchUser.get(1)));
					//sizeOfInterestsList = listOfUserInterests.size();
					//i--;
					
					mismatchUser.removeAll(mismatchUser);//Cleaning up users after mapping
				}
			}
			
		}
		listOfUserIds.removeAll(listOfUserIds);
		MeetingResponse.UnmatchedMeetingResponse unmatchedUser = new MeetingResponse.UnmatchedMeetingResponse();
		if(mismatchUser.size()!=0)
		{
			unmatchedUser.setUnMatchedUserId(mismatchUser.get(0));
		}
		return videoCallResponseBuilder.buildResponse(meetingResponseList, unmatchedUser);
	}


}
