package com.kayak.pms.home.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.home.service.HomeWorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * com.kayak.pms.home
 * user:rennannan
 * date:2021/3/9 20:18
 * function:首页工作流查询
 */
@RestController
@RequestMapping("/home")
public class HomeWorkFlowAction extends BaseController {
    @Autowired
    private HomeWorkFlowService homeWorkFlowService;

    @RequestMapping(value = "/workflow/findUserWorkFlow.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public SqlResult<HashMap> findHomeWorkFlowInfos() {
        try {
            return homeWorkFlowService.findHomeWorkFlowInfos();
        } catch (Exception e) {
            log.error("首页工作流查询异常【{}】", e);
            return null;
        }
    }
}
