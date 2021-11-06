package com.convobee.data.mapper;

import org.springframework.stereotype.Service;

import com.convobee.constants.Constants;
import com.convobee.data.entity.Meetings;
import com.convobee.data.entity.Slots;
import com.convobee.data.entity.Users;
import com.convobee.utils.DateTimeUtil;

@Service
public class MeetingsMapper {
	public Meetings mapMeetings(int useraid, int userbid, String meetingurl, int slotId) {
		Meetings meetings = new Meetings();
		Users user_a = new Users();
		user_a.setUserid(useraid);
		Users user_b = new Users();
		user_b.setUserid(userbid);
		Slots slot = new Slots();
		slot.setSlotid(slotId);
		meetings.setSlots(slot);
		meetings.setUseraid(user_a);
		meetings.setUserbid(user_b);
		meetings.setMeetingurl(meetingurl);
		meetings.setMeetingstatus(Constants.INITIATED);
		meetings.setStartedat(DateTimeUtil.getCurrentUTCTime());
		meetings.setEndedat(null);
		return meetings;
	}
}
