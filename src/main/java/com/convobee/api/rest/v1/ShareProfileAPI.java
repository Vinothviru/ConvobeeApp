package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.ShareProfileRequest;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.service.ShareProfileService;

@RestController
@CrossOrigin
public class ShareProfileAPI {
	@Autowired
	ShareProfileService shareProfileService;

	@RequestMapping(value = "/getshareprofilelink", method = RequestMethod.GET)
	public ResponseEntity getShareProfileLink(HttpServletRequest request) throws Exception{
		return  new BaseResponse().getResponse( ()-> shareProfileService.getShareProfileLink(request));
	}
	
	@RequestMapping(value = "/shareprofile", method = RequestMethod.GET)
	public ResponseEntity shareProfile(@ModelAttribute ShareProfileRequest shareProfileRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> shareProfileService.shareProfile(shareProfileRequest));
	}
}
