package com.convobee.api.rest.response;

import java.util.LinkedList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphLineChartResponse {
	LinkedList<Double> confidenceDatalist;
	LinkedList<Double> impressionDatalist;
	LinkedList<Double> proficiencyDatalist;
}
