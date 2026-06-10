package com.kayak.dict.action;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.core.action.BaseController;
import com.kayak.graphql.service.GraphqlService;

@RestController
public class DictAction extends BaseController {

	@Value("${basename:BaseServer}")
	private String basename;

	@Autowired
	private GraphqlService graphqlService;

	@PostMapping(value = "/base/dict/{dict}.json")
	public Object dict(@PathVariable String dict) {
		try {
			return graphqlService.requestPostForm(basename, "/base/dict/" + dict + ".json", null);
		} catch (JSONException e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}
	}

}
