package com.convobee.data.mapper;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.UsersRequest;
import com.convobee.data.entity.Users;
import com.convobee.utils.DateTimeUtil;

@Service
public class UsersMapper {
	
	public Users mapUserFromRequest(UsersRequest usersRequest) throws Exception{
		Users user = new Users();
		user.setUsername(usersRequest.getUsername());
		user.setNickname(usersRequest.getNickname());
		user.setPassword(usersRequest.getPassword());
		user.setMailid(usersRequest.getMailid());
		user.setRole(usersRequest.getRole());
		user.setCountry(usersRequest.getCountry());
		user.setCity(usersRequest.getCity());
		user.setEducationlevel(usersRequest.getEducationlevel());
		user.setProficiencylevel(usersRequest.getProficiencylevel());
		user.setSignuptype(usersRequest.getSignuptype());
		user.setReportcount(usersRequest.getReportcount());
		user.setIsfeedbackgiven(true);
		user.setCreatedat(DateTimeUtil.getCurrentUTCTime());
		user.setModifiedat(DateTimeUtil.getCurrentUTCTime());
		return user;
	}

}
