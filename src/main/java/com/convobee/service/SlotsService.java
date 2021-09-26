package com.convobee.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.SlotsRequest;
import com.convobee.data.entity.Slots;
import com.convobee.data.repository.SlotsRepo;
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
}
