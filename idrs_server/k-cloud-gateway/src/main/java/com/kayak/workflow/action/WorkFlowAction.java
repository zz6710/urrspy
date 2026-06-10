package com.kayak.workflow.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.service.GraphqlService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WorkFlowAction extends BaseController {

	@Autowired
	private GraphqlService graphqlService;

	@PostMapping(value = "/wf/**")
	public Object searchDefault(HttpServletRequest httpRequest) {
		//TODO

		try {
			String currentURL = httpRequest.getRequestURI();

			String url = currentURL.substring(currentURL.indexOf("wf") + 2);
			String bodyParameters = RequestSupport.getBodyParametersFlowable(httpRequest);
			return graphqlService.requestPostJson("WorkflowServer", url, bodyParameters);
		} catch (JSONException e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

}
