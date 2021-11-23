package com.convobee.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.convobee.api.rest.request.ShareProfileRequest;
import com.convobee.api.rest.response.DashboardPieChatResponse;
import com.convobee.api.rest.response.GraphLineChartResponse;
import com.convobee.api.rest.response.ShareProfileResponse;
import com.convobee.api.rest.response.builder.ShareProfileResponseBuilder;
import com.convobee.constants.Constants;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.UsersRepo;
import com.convobee.utils.EncryptionUtil;
import com.convobee.utils.UserUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class ShareProfileService {
	
	@Autowired
	UserUtil userUtil;
	
	@Autowired
	UsersRepo usersRepo;
	
	@Autowired
	ShareProfileResponseBuilder shareProfileResponseBuilder;
	
	@Autowired
	FeedbacksService feedbacksService;
	
    @Value("${aes.secret.key}")
    private String aesSecretKey;
    
    @Value("${site.base.url.https}")
    private String baseURL;
	
	public ShareProfileResponse shareProfile(ShareProfileRequest shareProfileRequest) throws Exception {
		String mailid = EncryptionUtil.decrypt(shareProfileRequest.getProfileLink(), aesSecretKey);
		if(mailid==null) {
			throw new Exception(Constants.NO_SUCH_USER);
		}
		Users user = usersRepo.findByMailid(mailid).get();
		boolean isMailIdExists = user!=null ? true : false;
		if(!isMailIdExists) {
			throw new Exception(Constants.NO_SUCH_USER);
		}
		int userId = user.getUserid();
		String userName = user.getUsername();
		String city = user.getCity();
		String country = user.getCountry();
		DashboardPieChatResponse dashboardPieChatResponse = feedbacksService.getPieChart(userId);
		GraphLineChartResponse graphLineChartResponse = feedbacksService.getOverallGraphLineChart(userId);
		ShareProfileResponse shareProfileResponse = shareProfileResponseBuilder.buildResponseForSharedProfile(userName, city, country, graphLineChartResponse, dashboardPieChatResponse);
		return shareProfileResponse;
	}

	public ShareProfileResponse getShareProfileLink(HttpServletRequest request) throws Exception{
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		String mailid = usersRepo.findByUserid(loggedinUserId).getMailid();
		String sharableRandomString = EncryptionUtil.encrypt(mailid,aesSecretKey);
		String sharableURL = UriComponentsBuilder.fromHttpUrl(baseURL).path("/shareprofile").queryParam("profileLink", sharableRandomString).toUriString(); 
		ShareProfileResponse shareProfileResponse = shareProfileResponseBuilder.buildResponseForProfileSharing(sharableURL);
		return shareProfileResponse;
	}
}
