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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.SlotsRequest;
import com.convobee.data.entity.Slots;
import com.convobee.data.repository.SlotsRepo;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.SlotUtil;

@Service
public class SlotsService {

	@Autowired
	SlotsRepo slotsRepo;


	/*It supports for month and year as well
	 * 
	 *  2019-02-28 00:00:00
		2019-02-28 00:30:00
		...
		2019-03-02 23:00:00
		2019-03-02 23:30:00
	 * */

	public void addSlot(SlotsRequest slotsRequest) throws ParseException
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
			slot.setSloturl(SlotUtil.getRandomSlotUrl());//Generating slots
			slot.setCreatedat(Timestamp.valueOf("2020-01-01 19:30:00"));
			slotsList.add(slot);
			dif += 3600000/2;
		}
		slotsRepo.saveAll(slotsList);
	}


	public Map<Integer, String> showSlots(String timezone) {
		final LocalDateTime now = LocalDateTime.of(LocalDate.now(ZoneId.of(timezone)), LocalTime.of(07, 00, 00));
		final LocalDateTime utc = DateTimeUtil.toUtc(now, timezone);
		int originalTimeDiff = now.getMinute()-utc.getMinute();
		int minuteDiff = Math.abs(originalTimeDiff);
		LocalDate utcStartTempDate = utc.toLocalDate();
		String utcStartDate = utcStartTempDate.toString().replace('T', ' ');
		String utcEndDate = utc.toLocalDate().plusDays(14).toString().replace('T', ' '); 
		System.out.println(slotsRepo.findSlotsByDates(utcStartDate));
		//Map<String, List<Slots>> slotMap = new HashMap<String, List<Slots>>();
		Map<Integer, String> map = null;
		for(int i = 0;i<14;i++)
		{
			//System.out.println(slotsRepo.findSlotsByDates(utcStartTempDate.toString().replace('T', ' ')));
			//slotMap.put(utcStartTempDate.toString().replace('T', ' '), slotsRepo.findSlotsByDates(utcStartTempDate.toString().replace('T', ' ')));
			int temp = 0;
			List<Slots> listOfSlots = slotsRepo.findSlotsByDates(utcStartTempDate.toString().replace('T', ' '));
			for(Slots utcSlot : listOfSlots) {
				boolean isIndex;
				temp+=1;
				List<String> timings = new LinkedList<String>();
				if(minuteDiff==0) {
					isIndex = utcSlot.getSlottime().toLocalDateTime().equals(utc);
					timings.add("7 AM");
					timings.add("8 AM");
					timings.add("9 AM");
					timings.add("10 AM");
					timings.add("5 PM");
					timings.add("6 PM");
					timings.add("7 PM");
					timings.add("8 PM");
					timings.add("9 PM");
				}
				else {
					isIndex = utcSlot.getSlottime().toLocalDateTime().equals(utc.plusMinutes(minuteDiff));
					timings.add("7:30 AM");
					timings.add("8:30 AM");
					timings.add("9:30 AM");
					timings.add("10:30 AM");
					timings.add("5:30 PM");
					timings.add("6:30 PM");
					timings.add("7:30 PM");
					timings.add("8:30 PM");
					timings.add("9:30 PM");
				}
				if(isIndex) {
					List<String> finalTime = new LinkedList<String>();
					int count = 0;
					LocalDateTime localDT = listOfSlots.get(temp-1).getSlottime().toLocalDateTime();
					for(int hoursToAdd = 0; count<9 ; ) {
						if(count==4) {
							hoursToAdd+=6;
						}
						System.out.println("localDT+1 = " + localDT.plusHours(hoursToAdd).toString().replace('T', ' '));
						finalTime.add(localDT.plusHours(hoursToAdd).toString().replace('T', ' '));
						count++;
						hoursToAdd++;
					}
					
					System.out.println(slotsRepo.findSlotsIdByDateTime(finalTime));
					List<Integer> finalSlotIds = slotsRepo.findSlotsIdByDateTime(finalTime);
					map = IntStream.range(0, finalSlotIds.size())
                            .collect(
                                 HashMap::new, 
                                 (m, k) -> m.put(finalSlotIds.get(k), timings.get(k)), 
                                 Map::putAll
                            );
					/*List<Slots> finalSlots = new LinkedList<Slots>();
        				for(int j = temp-1; count<9 ;j+=2 ) {
        					if(count == 4) {
        						j=j+12;
        					}
        					count++;
        					finalSlots.add(listOfSlots.get(j));
        					System.out.println("Final Slot = " + listOfSlots.get(j));
        				}*/
				}
			}

			utcStartTempDate = utcStartTempDate.plusDays(1);
		}
		return map;
		
		//return slotsRepo.findSlotsByDates(utcStartDate).toString();
		
		
		
		// return slotMap;
		/* System.out.println(timezone+" Zone");
        System.out.println("Now: " + now);
        System.out.println("UTC: " + utc);
        System.out.println("Start date " + utc.toLocalDate());
        System.out.println("End date " + utc.toLocalDate().plusDays(14));
        System.out.println("");*/
	}
}
