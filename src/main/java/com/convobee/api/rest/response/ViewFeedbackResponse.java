package com.convobee.api.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewFeedbackResponse {
	private float proficiencyLevel;
	private float confidenceLevel;
	private float impressionLevel;
	private String appretiateFeedback;
	private String adviseFeedback;
}
