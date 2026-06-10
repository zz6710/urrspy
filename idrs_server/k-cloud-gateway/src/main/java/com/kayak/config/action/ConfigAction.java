package com.kayak.config.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.service.GraphqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ConfigAction extends BaseController {

    @Autowired
    private GraphqlService graphqlService;

    @PostMapping(value = "/configCenter/**")
    public Object forwardJson(HttpServletRequest request) {
        try {
            return graphqlService.requestPostJson("ConfigServer", request.getRequestURI(),
                    RequestSupport.getParameters());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }
}
