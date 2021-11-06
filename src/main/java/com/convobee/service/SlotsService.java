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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.SlotsRequest;
import com.convobee.data.entity.Slots;
import com.convobee.data.repository.SlotsRepo;
import com.convobee.utils.CommonUtil;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.SlotUtil;

@Transactional
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
			slot.setSloturl(CommonUtil.getRandomUrl());//Generating slots
			slot.setCreatedat(Timestamp.valueOf("2020-01-01 19:30:00"));
			slotsList.add(slot);
			dif += 3600000/2;
		}
		slotsRepo.saveAll(slotsList);
	}


	public Map<String, Map<Integer, String>> showSlots(String timezone) {
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
			List<Integer> finalSlotIds = slotsRepo.findSlotsIdByDateTime(finalTime);
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
	}
}
