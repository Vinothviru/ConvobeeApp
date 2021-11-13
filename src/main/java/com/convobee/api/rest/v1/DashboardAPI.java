package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.GraphLineChartRequest;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.service.DashboardService;

@RestController
public class DashboardAPI {
	
	@Autowired
	DashboardService dashboardService;
	
	@RequestMapping(value = "/getdashboarddetails", method = RequestMethod.GET)
	public ResponseEntity addSlot(HttpServletRequest request, @ModelAttribute GraphLineChartRequest graphLineChartRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> dashboardService.getDashboardDetails(request, graphLineChartRequest));
	}
}
