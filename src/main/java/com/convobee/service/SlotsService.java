package com.convobee.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.SlotsRequest;
import com.convobee.api.rest.response.ShowSlotsFinalResponse;
import com.convobee.api.rest.response.ShowSlotsResponse;
import com.convobee.api.rest.response.SlotTimeResponse;
import com.convobee.data.entity.Slots;
import com.convobee.data.repository.BookedSlotsRepo;
import com.convobee.data.repository.SlotsRepo;
import com.convobee.utils.CommonUtil;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.SlotUtil;
import com.convobee.utils.UserUtil;

@Transactional
@Service
public class SlotsService {

	@Autowired
	SlotsRepo slotsRepo;

	@Autowired
	BookedSlotsRepo bookedSlotsRepo;
	
	@Autowired
	UserUtil userUtil;

	/*It supports for month and year as well
	 * 
	 *  2019-02-28 00:00:00
		2019-02-28 00:30:00
		...
		2019-03-02 23:00:00
		2019-03-02 23:30:00
	 * */

	public boolean addSlot(SlotsRequest slotsRequest) throws ParseException
	{
		List<Slots> slotsList = new ArrayList<Slots>();
		String firstDate = slotsRequest.getFromdate();//"28/02/2019";  
		String firstTime = slotsRequest.getFromtime();//"05:30";
		String secondDate = slotsRequest.getTodate();//"03/03/2019";
		String secondTime = slotsRequest.getTotime();//"05:30";
		String format = "dd/MM/yyyy hh:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(firstDate + " " + firstTime);
		Date dateObj2 = sdf.parse(secondDate + " " + secondTime);
		long dif = dateObj1.getTime(); 
		while (dif < dateObj2.getTime()) {
			Slots slot = new Slots();
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdfs.setTimeZone(TimeZone.getTimeZone("UTC"));
			//System.out.println(sdfs.format(new Date(dif)));
			slot.setSlottime(Timestamp.valueOf(sdfs.format(new Date(dif))));
			slot.setSloturl(CommonUtil.getRandomUrl());//Generating slots
			slot.setCreatedat(Timestamp.valueOf("2020-01-01 19:30:00"));
			slotsList.add(slot);
			dif += 3600000/2;
		}
		slotsRepo.saveAll(slotsList);
		return true;
	}


	/*public Map<String, Map<Integer, String>> showSlots(String timezone) {
		final LocalDateTime now = LocalDateTime.of(LocalDate.now(ZoneId.of(timezone)), LocalTime.of(07, 00, 00));
		LocalDateTime utc = DateTimeUtil.toUtc(now, timezone);
		int originalTimeDiff = now.getMinute()-utc.getMinute();
		int minuteDiff = Math.abs(originalTimeDiff);
		int flag = 0;
		Map<Integer, String> map = null;
		Map<String, Map<Integer, String>> finalMap = new LinkedHashMap<String, Map<Integer, String>>();
		if(minuteDiff==30) {
			utc = utc.plusMinutes(minuteDiff);
		}
		for(int totalDays = 0;totalDays<14;totalDays++)
		{
			if(flag!=0)
			{
				utc = utc.plusDays(1);
			}
			flag = 1;

			List<String> finalTime = new LinkedList<String>();
			int count = 0;
			for(int hoursToAdd = 0; count<9 ; ) {
				if(count==4) {
					hoursToAdd+=6;
				}
				//System.out.println("localDT+1 = " + utc.plusHours(hoursToAdd).toString().replace('T', ' '));
				finalTime.add(utc.plusHours(hoursToAdd).toString().replace('T', ' '));
				count++;
				hoursToAdd++;
			}
			//System.out.println(slotsRepo.findSlotsIdByDateTime(finalTime));
			LinkedList<Integer> finalSlotIds = slotsRepo.findSlotsIdByDateTime(finalTime);
			List<String> timings = SlotUtil.getSlotTimings(minuteDiff);
			map = IntStream.range(0, finalSlotIds.size())
					.collect(
							LinkedHashMap::new, 
							(m, k) -> m.put(finalSlotIds.get(k), timings.get(k)), 
							Map::putAll
							);
			finalMap.put(utc.toLocalDate().toString(), map);
		}
		return finalMap;
	}*/
	
	public ShowSlotsFinalResponse showSlots(HttpServletRequest request, String timezone) {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		final LocalDateTime now = LocalDateTime.of(LocalDate.now(ZoneId.of(timezone)), LocalTime.of(07, 00, 00));
		LocalDateTime utc = DateTimeUtil.toUtc(now, timezone);
		int originalTimeDiff = now.getMinute()-utc.getMinute();
		int minuteDiff = Math.abs(originalTimeDiff);
		int flag = 0;
		if(minuteDiff==30) {
			utc = utc.plusMinutes(minuteDiff);
		}
		LocalDateTime costantUtc = utc;
		List<String> finalTime = new LinkedList<String>();
		List<String> finalDate = new LinkedList<String>();
		for(int totalDays = 0;totalDays<14;totalDays++)
		{
			if(flag!=0)
			{
				utc = utc.plusDays(1);
			}
			flag = 1;


			int count = 0;
			for(int hoursToAdd = 0; count<9 ; ) {
				if(count==4) {
					hoursToAdd+=6;
				}
				//System.out.println("localDT+1 = " + utc.plusHours(hoursToAdd).toString().replace('T', ' '));
				finalTime.add(utc.plusHours(hoursToAdd).toString().replace('T', ' '));
				count++;
				hoursToAdd++;
			}
			finalDate.add(utc.toLocalDate().toString());
		}
		LinkedList<Integer> finalSlotIds = slotsRepo.findSlotsIdByDateTime(finalTime);
		LinkedList<Object[]> countofBookedSlots = bookedSlotsRepo.findCountOfSlotsBySlotids(finalSlotIds);
		List<List<Integer>> finalSlotIdsParts = chopped(finalSlotIds, 9);
		int temp = 0;
		String pace;
		List<String> timings = SlotUtil.getSlotTimings(minuteDiff);
		LinkedList<ShowSlotsResponse> showResponseFinal = new LinkedList<ShowSlotsResponse>();
		//LinkedList<LinkedList<SlotTimeResponse>> slotTimeResponseListFinal = new LinkedList<LinkedList<SlotTimeResponse>>();
		for(int i = 0; i< 14; i++) {
			ShowSlotsResponse showResponse = new ShowSlotsResponse();
			showResponse.setDate(finalDate.get(i));
			LinkedList<SlotTimeResponse> slotTimeResponseList = new LinkedList<SlotTimeResponse>();
			List<Integer> listOfIds = finalSlotIdsParts.get(i);
			for(Integer id = 0; id<listOfIds.size() ; id++) {
				pace = "Low";
				SlotTimeResponse slotButton = new SlotTimeResponse();
				slotButton.setSlotId(listOfIds.get(id));
				try {
					if(!countofBookedSlots.isEmpty() && listOfIds.get(id).equals(countofBookedSlots.get(temp)[0])) {
						int valueOfCount = Integer.valueOf(countofBookedSlots.get(temp)[1].toString());
						if(valueOfCount>=1&&valueOfCount<2) {
							pace = "medium";
						}
						else if(valueOfCount>=2&&valueOfCount<3) {
							pace = "high";
						}
						temp++;
					}
				}
				catch(Exception e) {
					countofBookedSlots.removeAll(countofBookedSlots);
				}
				
				slotButton.setTime(timings.get(id));
				slotButton.setPace(pace);
				slotTimeResponseList.add(slotButton);
			}
			//slotTimeResponseListFinal.add(slotTimeResponseList);
			showResponse.setSlotTimeResponseList(slotTimeResponseList);
			showResponseFinal.add(showResponse);
		}
		ShowSlotsFinalResponse showSlotsFinalResponse = new ShowSlotsFinalResponse();
		showSlotsFinalResponse.setShowResponseFinal(showResponseFinal);
		showSlotsFinalResponse.setUserBookedIds(slotsRepo.findSlotsIdByUTCTime(loggedinUserId,Timestamp.valueOf(costantUtc)));
		//showResponse.setUserBookedIds(slotsRepo.findSlotsIdByUTCTime(loggedinUserId,Timestamp.valueOf(costantUtc)));
		return showSlotsFinalResponse;
	}
	
	// chops a list into non-view sublists of length L
	static <T> List<List<T>> chopped(List<T> list, final int L) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}
}
