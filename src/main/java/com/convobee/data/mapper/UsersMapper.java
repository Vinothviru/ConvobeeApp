package com.convobee.data.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.UsersRequest;
import com.convobee.constants.Constants;
import com.convobee.data.entity.Users;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.EncryptionUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsersMapper {
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${aes.secret.key}")
    private String aesSecretKey;
    
	public Users mapUserFromRequest(UsersRequest usersRequest) throws Exception{
		String isFromOauthSignup = usersRequest.getBvfhdjsk();
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
		/* Handled for OAuth flow */
		if(isFromOauthSignup!=null&&!isFromOauthSignup.isEmpty()&&!isFromOauthSignup.isBlank()) {
			if(usersRequest.getMailid().equals(EncryptionUtil.decrypt(isFromOauthSignup, aesSecretKey))) {
				user.setStatus(true);
			}
			else {
				throw new Exception(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
			}
		}
		user.setCreatedat(DateTimeUtil.getCurrentUTCTime());
		user.setModifiedat(DateTimeUtil.getCurrentUTCTime());
		return user;
	}
	
	public Users mapUserFromRequestForUpdate(UsersRequest usersRequest) throws Exception{
		Users user = new Users();
		user.setUserid(usersRequest.getUserid());
		user.setUsername(usersRequest.getUsername());
		user.setNickname(usersRequest.getNickname());
		user.setPassword(passwordEncoder.encode(usersRequest.getPassword()));
		user.setMailid(usersRequest.getMailid());
		user.setCountry(usersRequest.getCountry());
		user.setCity(usersRequest.getCity());
		user.setEducationlevel(usersRequest.getEducationlevel());
		user.setProficiencylevel(usersRequest.getProficiencylevel());
		user.setModifiedat(DateTimeUtil.getCurrentUTCTime());
		user.setCreatedat(usersRequest.getCreatedat());
		user.setSignuptype(usersRequest.getSignuptype());
		user.setRole(usersRequest.getRole());
		user.setStatus(true);
		return user;
	}

}
