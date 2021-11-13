package com.convobee.api.rest.response.builder;

import com.convobee.api.rest.response.OAuthResponse;

public class OauthResponseBuilder {

	public OAuthResponse buildResponse(String username, String maild, String encryptedText){
		OAuthResponse oauthResponse = OAuthResponse.builder() 
										.username(username)
										.mailid(maild).
										bvfhdjsk(encryptedText).build();
		return oauthResponse;
	}
}
