package com.convobee.data.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.UsersRequest;
import com.convobee.data.entity.Users;
import com.convobee.utils.DateTimeUtil;

@Service
public class UsersMapper {
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	public Users mapUserFromRequest(UsersRequest usersRequest) throws Exception{
		Users user = new Users();
		user.setUsername(usersRequest.getUsername());
		user.setNickname(usersRequest.getNickname());
		user.setPassword(passwordEncoder.encode(usersRequest.getPassword()));
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
