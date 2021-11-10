package com.convobee.service;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.MeetingsRequest;
import com.convobee.api.rest.request.UsersRequest;
import com.convobee.api.rest.response.MeetingResponse;
import com.convobee.api.rest.response.VideoCallResponse;
import com.convobee.api.rest.response.builder.MeetingResponseBuilder;
import com.convobee.api.rest.response.builder.VideoCallResponseBuilder;
import com.convobee.constants.Constants;
import com.convobee.data.entity.Meetings;
import com.convobee.data.entity.Users;
import com.convobee.data.mapper.MeetingsMapper;
import com.convobee.data.repository.BookedSlotsRepo;
import com.convobee.data.repository.InterestsRepo;
import com.convobee.data.repository.MeetingsRepo;
import com.convobee.data.repository.SlotsRepo;
import com.convobee.data.repository.UsersRepo;
import com.convobee.exception.UserValidationException;
import com.convobee.utils.CommonUtil;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.UserUtil;

@Transactional
@Service
public class MeetingsService {

	@Autowired
	MeetingsRepo meetingsRepo;

	@Autowired
	MeetingsMapper meetingsMapper;
	
	@Autowired
	UserUtil userUtil;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	InterestsRepo interestsRepo;
	
	@Autowired
	MeetingResponseBuilder meetingResponseBuilder;
	
	@Autowired
	VideoCallResponseBuilder videoCallResponseBuilder;
	
	@Autowired
	BookedSlotsRepo bookedSlotsRepo;
	
	@Autowired
	UsersRepo usersRepo;
	
	@Autowired
	SlotsRepo slotsRepo;
	
	List<Integer> listOfUserIds = new LinkedList<Integer>();
	 
	public VideoCallResponse addActiveUsers(HttpServletRequest request, MeetingsRequest meetingsRequest)
	{ 
		//Need to make this dynamic intead of hardcoding
		//listOfUserIds.add(userUtil.getLoggedInUserId(request));
		listOfUserIds.add(3047);
		listOfUserIds.add(4512);
		listOfUserIds.add(4499);
		listOfUserIds.add(4505);
		listOfUserIds.add(4540);
		listOfUserIds.add(4510);
		listOfUserIds.add(4548);
		listOfUserIds.add(4581);
		listOfUserIds.add(3051);
		listOfUserIds.add(4690);
		
		return initiateMeeting(meetingsRequest);
	}
	/*
	 * Covered cases are below
	 * Check with more than 2 entries - Done
	 * Handle if no others interest is matched for a user - Done
	 * Need to return what interest that 2 users are matched with - Done
	 * Follow the points in the google docs - Done
	 * 
	 * TO DO
	 * Low priority, not going to make big deal -  Need to do optimisation by removing the elements by un commenting the below lines else more iterations will happen(search this whole string to find the place) 
	 * Need to check whether the requesting user and the data after retrieval's user id is same - I don't think it is valid
	 * */
	
	//public Map<LinkedList<Integer>, String> initiateMeeting() {
	public VideoCallResponse initiateMeeting(MeetingsRequest meetingsRequest) {
		Instant currentUTCTime = Instant.now();  
		Duration durationInMinutes = Duration.ofMinutes(5);
		String startTime = currentUTCTime.minus(durationInMinutes).toString().replace('T', ' ').replace('Z', ' ');
		String endTime = currentUTCTime.plus(durationInMinutes).toString().replace('T', ' ').replace('Z', ' ');
		int slotId = slotsRepo.findSlotsIdByDateTimeRange(startTime, endTime);
		List<Integer> listOfUsers = meetingsRequest.getListOfUserIds();
		List<MeetingResponse> meetingResponseList = new LinkedList<MeetingResponse>();
		List<LinkedList<String>> listOfUserInterests = new LinkedList<LinkedList<String>>();
		for(Integer i=0; i<listOfUsers.size(); i++)
		{
			listOfUserInterests.add(interestsRepo.findInterestByUser(listOfUsers.get(i)));
		}
		int sizeOfInterestsList = listOfUserInterests.size();
		LinkedList<Integer> mismatchUser = new LinkedList<Integer>();
		for(int i=0; i<sizeOfInterestsList; i++) {
			int flag = 0;
			LinkedList<Integer> userVsOppUser = new LinkedList<Integer>();
			List<String> list1 = listOfUserInterests.get(i);
			for(int j=i+1; j<sizeOfInterestsList;j++) {
				List<String> list2 = listOfUserInterests.get(j);
				System.out.println("list1 = " + list1);
				System.out.println("list2 = " + list2);
				System.out.println();
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
					Meetings meeting = meetingsMapper.mapMeetings(user_a_id, user_b_id, meetingUrl, slotId);
					meetingsRepo.save(meeting);//Persisting into Meetings table
					Users user_a = usersRepo.getById(user_a_id);
					Users user_b = usersRepo.getById(user_b_id);
					user_a.setIsfeedbackgiven(false);
					user_b.setIsfeedbackgiven(false);
					usersRepo.save(user_a);
					usersRepo.save(user_b);
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
					Meetings meeting = meetingsMapper.mapMeetings(mismatchUser.get(0), mismatchUser.get(1), meetingUrl, slotId);
					meetingsRepo.save(meeting);//Persisting into Meetings table
					
					Users user_a = usersRepo.getById(mismatchUser.get(0));
					Users user_b = usersRepo.getById(mismatchUser.get(1));
					user_a.setIsfeedbackgiven(false);
					user_b.setIsfeedbackgiven(false);
					usersRepo.save(user_a);
					usersRepo.save(user_b);
					
					meetingResponseList.add(meetingResponseBuilder.buildResponse(meeting.getMeetingid(), mismatchUser.get(0), mismatchUser.get(1), meetingUrl, null, listOfUserInterests.get(listOfUsers.indexOf(mismatchUser.get(1)))));
					meetingResponseList.add(meetingResponseBuilder.buildResponse(meeting.getMeetingid(), mismatchUser.get(1), mismatchUser.get(0), meetingUrl, null, listOfUserInterests.get(listOfUsers.indexOf(mismatchUser.get(0)))));
					
					/* Very low priority - Need to do optimisation by removing the elements by un commenting the below lines else more iterations will happen */
					
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
		MeetingResponse.UnmatchedMeetingResponse unmatchedUser = new MeetingResponse.UnmatchedMeetingResponse();
		if(mismatchUser.size()!=0)
		{
			unmatchedUser.setUnMatchedUserId(mismatchUser.get(0));
		}
		return videoCallResponseBuilder.buildResponse(meetingResponseList, unmatchedUser);
	}
	
	public VideoCallResponse initiateMeetingForSecondCall(MeetingsRequest meetingsRequest) {
		meetingsRepo.deleteAllById(meetingsRequest.getAffectedMeetingIds());
		return initiateMeeting(meetingsRequest);
	}
	
	public boolean changeStatusOfMeetingtoStarted(HttpServletRequest request, MeetingsRequest meetingsRequest) throws UserValidationException {
		Meetings meeting = meetingsRepo.getById(meetingsRequest.getMeetingId());
		if(!usersService.isValidUser(request, meeting.getUseraid())) {
			if(!usersService.isValidUser(request, meeting.getUserbid())) {
				throw new UserValidationException(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
			}
		}
		meeting.setMeetingstatus(Constants.STARTED);
		meetingsRepo.save(meeting);
		return true;
	}
	
	public boolean changeStatusOfMeetingtoCompleted(HttpServletRequest request, MeetingsRequest meetingsRequest) throws UserValidationException {
		Meetings meeting = meetingsRepo.getById(meetingsRequest.getMeetingId());
		if(!usersService.isValidUser(request, meeting.getUseraid())) {
			if(!usersService.isValidUser(request, meeting.getUserbid())) {
				throw new UserValidationException(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
			}
		}
		meeting.setMeetingstatus(Constants.COMPLETED);
		meeting.setEndedat(DateTimeUtil.getCurrentUTCTime());
		meetingsRepo.save(meeting);
		return true;
	}

	/* Verifying whether the user is not accessing irrelevant data as well as not a banned user */
	public boolean prevalidationOfJoinSession(HttpServletRequest request, UsersRequest usersRequest) throws Exception{
		int userIdFromDashboard = usersRequest.getUserid();
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		if(userIdFromDashboard!=loggedinUserId) {
			throw new Exception(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
		}
		if(usersService.isBannedUser(loggedinUserId)) {
			throw new Exception(Constants.BANNED_USER);
		}
		return true;
	}
}
