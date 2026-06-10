package com.kayak.healthy.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kayak.core.action.BaseController;

@Controller
public class HealthyAction extends BaseController {

	@RequestMapping(value = "/heathy.json")
	public @ResponseBody String getLoginUser() {
		return updateSuccess();
	}

}
