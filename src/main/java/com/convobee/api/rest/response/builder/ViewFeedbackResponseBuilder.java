package com.convobee.api.rest.response.builder;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.ViewFeedbackResponse;

@Service
public class ViewFeedbackResponseBuilder {
	public ViewFeedbackResponse buildResponse(float proficiencyLevel, float confidenceLevel, float impressionLevel, String appretiateFeedback, String adviseFeedback){
		ViewFeedbackResponse viewFeedbackResponse = ViewFeedbackResponse.builder() 
										.proficiencyLevel(proficiencyLevel)
										.confidenceLevel(confidenceLevel)
										.impressionLevel(impressionLevel)
										.appretiateFeedback(appretiateFeedback)
										.adviseFeedback(adviseFeedback).build();
		return viewFeedbackResponse;
	}
}
