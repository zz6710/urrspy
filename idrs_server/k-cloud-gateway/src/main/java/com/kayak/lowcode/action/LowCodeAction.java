package com.kayak.lowcode.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.service.GraphqlService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LowCodeAction extends BaseController {

    @Value("${serverName:LowCodeServer}")
    private String serverName;

    @Autowired
    private GraphqlService graphqlService;

    @PostMapping(value = "/generator/selectTables.json")
    public Object selectTables() {
        try {
            return graphqlService.requestPostJson(serverName, "/generator/selectTables.json",
                    RequestSupport.getParameters());
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }

    @PostMapping(value = "/generator/selectDetails.json")
    public Object selectDetails() {
        try {
            return graphqlService.requestPostJson(serverName, "/generator/selectDetails.json",
                    RequestSupport.getParameters());
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }
}
