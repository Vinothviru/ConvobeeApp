package com.convobee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultPage {

	@RequestMapping("/default")
	public String defaultPage() {
		return "default";
	}
}
