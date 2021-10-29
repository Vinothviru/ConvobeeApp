package com.convobee.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.api.rest.request.FeedbacksToUsRequest;
import com.convobee.api.rest.request.GraphLineChartRequest;
import com.convobee.api.rest.request.ViewFeedbackRequest;
import com.convobee.api.rest.response.DashboardPieChatResponse;
import com.convobee.api.rest.response.FeedbackHistoryResponse;
import com.convobee.api.rest.response.GraphLineChartResponse;
import com.convobee.api.rest.response.InvalidPieChartResponse;
import com.convobee.api.rest.response.ViewFeedbackResponse;
import com.convobee.api.rest.response.builder.GraphLineChartResponseBuilder;
import com.convobee.api.rest.response.builder.InvalidPieChartResponseBuilder;
import com.convobee.api.rest.response.builder.PieChartResponseBuilder;
import com.convobee.api.rest.response.builder.ViewFeedbackResponseBuilder;
import com.convobee.data.entity.Feedbacks;
import com.convobee.data.entity.FeedbacksToUs;
import com.convobee.data.entity.Users;
import com.convobee.data.mapper.FeedbacksMapper;
import com.convobee.data.mapper.FeedbacksToUsMapper;
import com.convobee.data.repository.FeedbacksRepo;
import com.convobee.data.repository.FeedbacksToUsRepo;
import com.convobee.data.repository.UsersRepo;
import com.convobee.utils.CommonUtil;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.UserUtil;

@Service
public class FeedbacksService {

	@Autowired
	FeedbacksMapper feedbacksMapper;
	
	@Autowired
	FeedbacksToUsMapper feedbacksToUsMapper;
	
	@Autowired
	FeedbacksRepo feedbacksRepo;
	
	@Autowired
	FeedbacksToUsRepo feedbacksToUsRepo;
	
	@Autowired
	UsersRepo usersRepo;
	
	@Autowired
	UserUtil userUtil;
	
	@Autowired
	ViewFeedbackResponseBuilder viewFeedbackResponseBuilder;
	
	@Autowired
	PieChartResponseBuilder pieChartResponseBuilder;
	
	@Autowired
	GraphLineChartResponseBuilder graphLineChartResponseBuilder;
	
	@Autowired
	InvalidPieChartResponseBuilder invalidPieChartResponseBuilder;
	
	int count = 0;
	
	public void submitFeedback(HttpServletRequest request, FeedbacksRequest feedbacksRequest) {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		Feedbacks feedback = feedbacksMapper.mapFeedbacks(feedbacksRequest, loggedinUserId);
		feedbacksRepo.save(feedback);
		/* After providing feedback, corresponding user's is_feedback_given status column will be changed to true */
		Optional<Users> user = usersRepo.findById(loggedinUserId);
		user.get().setIsfeedbackgiven(true);
		usersRepo.save(user.get());
	}
	
	public void submitFeedbackToUs(HttpServletRequest request, FeedbacksToUsRequest feedbacksToUsRequest) {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		FeedbacksToUs feedbacksToUs = feedbacksToUsMapper.mapFeedbacksToUs(feedbacksToUsRequest, loggedinUserId);
		feedbacksToUsRepo.save(feedbacksToUs);
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	public LinkedList<FeedbackHistoryResponse> getFeedbackHistory(HttpServletRequest request) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		LinkedList<FeedbackHistoryResponse> feedbackHistoryResponse = new LinkedList<FeedbackHistoryResponse>();
		LinkedList<String> listOfSlotTime = feedbacksRepo.findSlotTimeByUserId(loggedinUserId);
		LinkedList<Object[]> nickNameAndfeedbackIdList = feedbacksRepo.findNickNamesAndfeedbackIdByUserId(loggedinUserId);
		int size = nickNameAndfeedbackIdList.size();
		for(int i = 0; i<size ;i++) {
			FeedbackHistoryResponse feedbackHistory = new FeedbackHistoryResponse();
			feedbackHistory.setSlotDateTime(listOfSlotTime.get(i).replace('T', ' '));
			feedbackHistory.setPeerNickName(nickNameAndfeedbackIdList.get(i)[0].toString());
			feedbackHistory.setFeedbackId(Integer.valueOf(nickNameAndfeedbackIdList.get(i)[1].toString()));
			feedbackHistoryResponse.add(feedbackHistory);
		}
		return feedbackHistoryResponse;
	}
	
	public ViewFeedbackResponse viewFeedback(HttpServletRequest request, ViewFeedbackRequest viewFeedbackRequest) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		Feedbacks feedback = feedbacksRepo.findById(viewFeedbackRequest.getFeedbackId()).get();
		int feedbackReceiverUserId = feedback.getReceiveruser().getUserid();
		/* Checking whether the logged in user accessing his data or not */
		if(loggedinUserId == feedbackReceiverUserId)
		{
			ViewFeedbackResponse viewFeedbackResponse = viewFeedbackResponseBuilder.buildResponse(feedback.getProficiencylevel(), feedback.getConfidencelevel(), 
										feedback.getImpressionlevel(), feedback.getAppreciatefeedback(), feedback.getAdvisefeedback());
			return viewFeedbackResponse;
		}
		else
		{
			System.out.println("Inoruthan data va access pandriya da body soda");
			return null;
		}
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	public DashboardPieChatResponse getPieChart(HttpServletRequest request) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		int rowCount = feedbacksRepo.findTotalRowsByReceiveruserid(loggedinUserId);
		
		DashboardPieChatResponse pieChartResponseList = new DashboardPieChatResponse(); 
		
		LinkedList<Object[]> confidenceLevel = feedbacksRepo.findConfidenceLevelByReceiveruser(loggedinUserId);
		pieChartResponseList = processPieChartValues(confidenceLevel, rowCount, pieChartResponseList);
		
		LinkedList<Object[]> proficiencyLevel = feedbacksRepo.findProficiencyLevelByReceiveruser(loggedinUserId);
		pieChartResponseList = processPieChartValues(proficiencyLevel, rowCount, pieChartResponseList);
		
		LinkedList<Object[]> impressionLevel = feedbacksRepo.findImpressionLevelByReceiveruser(loggedinUserId);
		pieChartResponseList = processPieChartValues(impressionLevel, rowCount, pieChartResponseList);
		
		return pieChartResponseList;
	}
	
	/* Double values cannot be done using switch case so implemented with else if */
	public DashboardPieChatResponse  processPieChartValues(LinkedList<Object[]> result, int rowCount, DashboardPieChatResponse pieChartResponseList) throws Exception {
		double oneStar = 0, twoStar = 0, threeStar = 0, fourStar = 0, fiveStar = 0;
		for(int i = 0; i<result.size(); i++) {
			if(result.get(i)[0] != null) {
				if(Double.valueOf(result.get(i)[0].toString())==1.0) {
					oneStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==2.0) {
					twoStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==3.0) {
					threeStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==4.0) {
					fourStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
				else if(Double.valueOf(result.get(i)[0].toString())==5.0) {
					fiveStar = CommonUtil.calculatePercentage(Double.valueOf(result.get(i)[1].toString()),rowCount);
				}
			}
		}
		count++;
		if(count==1) {
			pieChartResponseList.setConfidenceLevel(pieChartResponseBuilder.buildConfidenceResponse(oneStar, twoStar, threeStar, fourStar, fiveStar));
		}
			
		else if(count==2)
			pieChartResponseList.setProficiencyLevel(pieChartResponseBuilder.buildProficiencyResponse(oneStar, twoStar, threeStar, fourStar, fiveStar));
		else if(count==3) {
			count=0;
			pieChartResponseList.setImpressionLevel(pieChartResponseBuilder.buildImpressionResponse(oneStar, twoStar, threeStar, fourStar, fiveStar));
		}
		return pieChartResponseList;
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	
	/* 
	 * TO DO
	 * Need to convert dates based on timezone
	 * Need to convert IST dates to user timezone before main logic
	 * Need to remove the deprecated values
	 * */
	public GraphLineChartResponse getGraphLineChart(HttpServletRequest request, GraphLineChartRequest graphLineChartRequest) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		
		/* Process of converting user time zone to UTC and querying and again converting to user time zone starts here*/

		String timeZone = graphLineChartRequest.getTimeZone();
		Month month = graphLineChartRequest.getMonth();
		int year = graphLineChartRequest.getYear();
		LocalDateTime endLocalDateTime;
		LocalDateTime startLocalDateTime;
		if(month==null) {
			endLocalDateTime = LocalDateTime.parse((YearMonth.now(ZoneId.of(timeZone)).atEndOfMonth().toString()+"T23:30:00"));
		}
		else {
			endLocalDateTime = LocalDateTime.parse(YearMonth.of(year, month).atEndOfMonth().toString()+"T23:30:00");
		}
		/* Checking whether the month is between 1 and 10, if yes, then ned to append 0 before month value because LocalDateTime can't handle with single number month value like 2 for feb. */
		if(endLocalDateTime.getMonthValue()>0&&endLocalDateTime.getMonthValue()<10)
		{
			startLocalDateTime =  LocalDateTime.parse(String.valueOf(endLocalDateTime.getYear()) + "-0" + String.valueOf(endLocalDateTime.getMonthValue()) + "-" + "01T00:00:00");
		}
		else {
			startLocalDateTime =  LocalDateTime.parse(String.valueOf(endLocalDateTime.getYear()) + "-" + String.valueOf(endLocalDateTime.getMonthValue()) + "-" + "01T00:00:00");
		}
		
		LocalDateTime utcStartDateTime = DateTimeUtil.toUtc(startLocalDateTime, timeZone);
		LocalDateTime utcEndDateTime = DateTimeUtil.toUtc(endLocalDateTime, timeZone);
		String startDate = utcStartDateTime.toString().replace('T', ' ')+":00";
		String endDate = utcEndDateTime.toString().replace('T', ' ')+":00";
		
		LinkedList<Timestamp> slotTime = feedbacksRepo.findSlotTimeByUserIdAndDateTime(loggedinUserId, startDate, endDate);
		LinkedList<Object[]> skillFactors = feedbacksRepo.findSkillFactorsByUserIdAndDateTime(loggedinUserId, startDate, endDate);
		LinkedList<LocalDate> slotDate = new LinkedList<LocalDate>();
		for(Timestamp dt : slotTime) {
			slotDate.add(DateTimeUtil.toZone(dt.toLocalDateTime(), ZoneId.of("UTC"), ZoneId.of(timeZone)).toLocalDate());
		}
		
		/* Process of converting user time zone to UTC and querying and again converting to user time zone ends here*/
		
		LinkedList<Double> confidence = new LinkedList<Double>();
		LinkedList<Double> impression = new LinkedList<Double>();
		LinkedList<Double> proficiency = new LinkedList<Double>();
		LinkedList<Integer> dates = new LinkedList<Integer>();
		double proficiencyLevel = 0, confidenceLevel = 0, impressionLevel = 0;
		int tempDate = 0;
		int count = 0;
		int skillFactorsSize = skillFactors.size();
		for(int i=0; i<skillFactorsSize; i++)
		{
			int date = slotDate.get(i).getDayOfMonth();
			if(skillFactors.get(i)[0] != null) {
				if(tempDate == 0) {
					tempDate = date;
				}
				
				if(tempDate == date) {
					count++;
					confidenceLevel += Double.valueOf(skillFactors.get(i)[0].toString());
					impressionLevel += Double.valueOf(skillFactors.get(i)[1].toString());
					proficiencyLevel += Double.valueOf(skillFactors.get(i)[2].toString());
					
					if(i == skillFactorsSize-1 && count > 1)
					{
						confidence.add(confidenceLevel/count);
						impression.add(impressionLevel/count);
						proficiency.add(proficiencyLevel/count);
						dates.add(tempDate);
					}
				}
				
				
				/* Checking whether count has been increased more than 2 but new date comes, so storing the old values  
				 * first and re-initiating the logic for new date 
				 * */
				
				else if(count > 1) {
					confidence.add(confidenceLevel/count);
					impression.add(impressionLevel/count);
					proficiency.add(proficiencyLevel/count);
					dates.add(tempDate);
					confidenceLevel = 0; impressionLevel = 0; proficiencyLevel = 0;
					
					/* Assigning new data again */
					tempDate = date;
					count = 0;
					count++;
					confidenceLevel += Double.valueOf(skillFactors.get(i)[0].toString());
					impressionLevel += Double.valueOf(skillFactors.get(i)[1].toString());
					proficiencyLevel += Double.valueOf(skillFactors.get(i)[2].toString());
					
					if(i == skillFactorsSize-1)
					{
						confidence.add(confidenceLevel);
						impression.add(impressionLevel);
						proficiency.add(proficiencyLevel);
						dates.add(tempDate);
					}
				}
				
				/* If the old date and new date is different without any repitation on old date */
				else {
					confidence.add(confidenceLevel);
					impression.add(impressionLevel);
					proficiency.add(proficiencyLevel);
					dates.add(tempDate);
					
					confidenceLevel = 0; impressionLevel = 0; proficiencyLevel = 0;
					
					/* Assigning new data again */
					tempDate = date;
					count = 0;
					count++;
					confidenceLevel += Double.valueOf(skillFactors.get(i)[0].toString());
					impressionLevel += Double.valueOf(skillFactors.get(i)[1].toString());
					proficiencyLevel += Double.valueOf(skillFactors.get(i)[2].toString());
					
					if(i == skillFactorsSize-1)
					{
						confidence.add(confidenceLevel);
						impression.add(impressionLevel);
						proficiency.add(proficiencyLevel);
						dates.add(tempDate);
					}
				}
				
				/* If the skill factor has only one row then value will be added in list here */
				if(i == skillFactorsSize-1 && confidence.isEmpty())
				{
					confidence.add(confidenceLevel/count);
					impression.add(impressionLevel/count);
					proficiency.add(proficiencyLevel/count);
					dates.add(tempDate);
				}
			}
		}
		LinkedList<Double> confidenceDatalist = new LinkedList<Double>();
		LinkedList<Double> impressionDatalist = new LinkedList<Double>();
		LinkedList<Double> proficiencyDatalist = new LinkedList<Double>();
		int monthEndDate = endLocalDateTime.toLocalDate().getDayOfMonth();
		int traverseDates = 0;
		for(int j=1; j<=monthEndDate; j++) {
			if(!dates.isEmpty()  && dates.get(traverseDates)==j) {
				confidenceDatalist.add(confidence.get(traverseDates));
				impressionDatalist.add(impression.get(traverseDates));
				proficiencyDatalist.add(proficiency.get(traverseDates));
				traverseDates++;
				if(traverseDates>=dates.size()) {
					dates.removeAll(dates);
				}
			}
			else {
				confidenceDatalist.add(0.0);
				impressionDatalist.add(0.0);
				proficiencyDatalist.add(0.0);
			}
			
		}
		GraphLineChartResponse graphLineChartResponse = graphLineChartResponseBuilder.buildResponse(confidenceDatalist, impressionDatalist, proficiencyDatalist);
		return graphLineChartResponse;
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	public GraphLineChartResponse getGraphLineChartForYear(HttpServletRequest request) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		String startDate = "2021-01-01 00:00:00";
		String endDate = "2021-12-31 23:30:00";
		//String endDate = "2021-10-31 23:30:00";
		LinkedList<Timestamp> slotTime = feedbacksRepo.findSlotTimeByUserIdAndDateTime(loggedinUserId, startDate, endDate);
		LinkedList<Object[]> skillFactors = feedbacksRepo.findSkillFactorsByUserIdAndDateTime(loggedinUserId, startDate, endDate);
		LinkedList<Double> confidence = new LinkedList<Double>();
		LinkedList<Double> impression = new LinkedList<Double>();
		LinkedList<Double> proficiency = new LinkedList<Double>();
		LinkedList<Integer> months = new LinkedList<Integer>();
		double proficiencyLevel = 0, confidenceLevel = 0, impressionLevel = 0;
		int tempMonth = 0;
		int count = 0;
		int skillFactorsSize = skillFactors.size();
		for(int i=0; i<skillFactorsSize; i++)
		{
			int month = slotTime.get(i).getMonth()+1;//Added 1 because getMonth returns 10 for November
			if(skillFactors.get(i)[0] != null) {
				if(tempMonth == 0) {
					tempMonth = month;
				}
				
				if(tempMonth == month) {
					count++;
					confidenceLevel += Double.valueOf(skillFactors.get(i)[0].toString());
					impressionLevel += Double.valueOf(skillFactors.get(i)[1].toString());
					proficiencyLevel += Double.valueOf(skillFactors.get(i)[2].toString());
					
					if(i == skillFactorsSize-1 && count > 1)
					{
						confidence.add(confidenceLevel/count);
						impression.add(impressionLevel/count);
						proficiency.add(proficiencyLevel/count);
						months.add(tempMonth);
					}
				}
				
				
				/* Checking whether count has been increased more than 2 but new date comes, so storing the old values  
				 * first and re-initiating the logic for new date 
				 * */
				
				else if(count > 1) {
					confidence.add(confidenceLevel/count);
					impression.add(impressionLevel/count);
					proficiency.add(proficiencyLevel/count);
					months.add(tempMonth);
					confidenceLevel = 0; impressionLevel = 0; proficiencyLevel = 0;
					
					/* Assigning new data again */
					tempMonth = month;
					count = 0;
					count++;
					confidenceLevel += Double.valueOf(skillFactors.get(i)[0].toString());
					impressionLevel += Double.valueOf(skillFactors.get(i)[1].toString());
					proficiencyLevel += Double.valueOf(skillFactors.get(i)[2].toString());
					
					if(i == skillFactorsSize-1)
					{
						confidence.add(confidenceLevel);
						impression.add(impressionLevel);
						proficiency.add(proficiencyLevel);
						months.add(tempMonth);
					}
				}
				
				/* If the old date and new date is different without any repitation on old date */
				else {
					confidence.add(confidenceLevel);
					impression.add(impressionLevel);
					proficiency.add(proficiencyLevel);
					months.add(tempMonth);
					
					confidenceLevel = 0; impressionLevel = 0; proficiencyLevel = 0;
					
					/* Assigning new data again */
					tempMonth = month;
					count = 0;
					count++;
					confidenceLevel += Double.valueOf(skillFactors.get(i)[0].toString());
					impressionLevel += Double.valueOf(skillFactors.get(i)[1].toString());
					proficiencyLevel += Double.valueOf(skillFactors.get(i)[2].toString());
					
					if(i == skillFactorsSize-1)
					{
						confidence.add(confidenceLevel);
						impression.add(impressionLevel);
						proficiency.add(proficiencyLevel);
						months.add(tempMonth);
					}
				}
				
				/* If the skill factor has only one row then value will be added in list here */
				if(i == skillFactorsSize-1 && confidence.isEmpty())
				{
					confidence.add(confidenceLevel/count);
					impression.add(impressionLevel/count);
					proficiency.add(proficiencyLevel/count);
					months.add(tempMonth);
				}
			}
		}
		LinkedList<Double> confidenceDatalist = new LinkedList<Double>();
		LinkedList<Double> impressionDatalist = new LinkedList<Double>();
		LinkedList<Double> proficiencyDatalist = new LinkedList<Double>();
		int monthEndValue = Timestamp.valueOf(endDate).getMonth()+1;//Added 1 because getMonth returns 10 for November
		int traverseMonths = 0;
		for(int j=1; j<=monthEndValue; j++) {
			if(!months.isEmpty()  && months.get(traverseMonths)==j) {
				confidenceDatalist.add(confidence.get(traverseMonths));
				impressionDatalist.add(impression.get(traverseMonths));
				proficiencyDatalist.add(proficiency.get(traverseMonths));
				traverseMonths++;
				if(traverseMonths>=months.size()) {
					months.removeAll(months);
				}
			}
			else {
				confidenceDatalist.add(0.0);
				impressionDatalist.add(0.0);
				proficiencyDatalist.add(0.0);
			}
			
		}
		GraphLineChartResponse graphLineChartResponse = graphLineChartResponseBuilder.buildResponse(confidenceDatalist, impressionDatalist, proficiencyDatalist);
		return graphLineChartResponse;
	}
	
	/* No need of user validation here because no param is passed from request exclusively. It is handled using JWT itself */
	/* Unfortunately this is not our required API, We can use this if needed */
	public InvalidPieChartResponse getPieChartInvalid(HttpServletRequest request) throws Exception {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		
		LinkedList<Object[]> result = feedbacksRepo.findAllByReceiveruser(loggedinUserId);
		double proficiencyLevel = 0, confidenceLevel = 0, impressionLevel = 0; 
		for(int i = 0; i<result.size() ;i++) {
			if(result.get(i)[0] != null) {
				proficiencyLevel = Double.valueOf(result.get(i)[0].toString());
				confidenceLevel = Double.valueOf(result.get(i)[1].toString());
				impressionLevel = Double.valueOf(result.get(i)[2].toString());
			}
		}
		double percentageProficiencyLevel = (proficiencyLevel/5)*100;
		double percentageconfidenceLevel = (confidenceLevel/5)*100;
		double percentageimpressionLevel = (impressionLevel/5)*100;
		InvalidPieChartResponse pieChartResponse = invalidPieChartResponseBuilder.buildResponse(percentageProficiencyLevel, percentageconfidenceLevel, percentageimpressionLevel);
		return pieChartResponse;
	}
}
