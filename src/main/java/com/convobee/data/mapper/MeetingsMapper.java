package com.convobee.data.mapper;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.convobee.constants.Constants;
import com.convobee.data.entity.Meetings;
import com.convobee.data.entity.Slots;
import com.convobee.data.entity.Users;

@Service
public class MeetingsMapper {
	public Meetings mapMeetings(int useraid, int userbid, String meetingurl) {
		Meetings meetings = new Meetings();
		Users user_a = new Users();
		user_a.setUserid(useraid);
		Users user_b = new Users();
		user_b.setUserid(userbid);
		Slots slot = new Slots();
		slot.setSlotid(5);
		meetings.setSlots(slot);
		meetings.setUseraid(user_a);
		meetings.setUserbid(user_b);
		meetings.setMeetingurl(meetingurl);
		meetings.setMeetingstatus(Constants.INITIATED);
		meetings.setStartedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		meetings.setEndedat(null);
		return meetings;
	}
}
