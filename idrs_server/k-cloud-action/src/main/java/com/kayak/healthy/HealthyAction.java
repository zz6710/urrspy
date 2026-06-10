package com.kayak.healthy;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.action.BaseController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthyAction extends BaseController {
    @PostMapping("/heathy.json")
    public String getAppHealthy(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("success",true);
        return jsonObject.toJSONString();
    }
    
    
    @GetMapping("/check.json")
    public String check(){
      
        return "200";
    }
}
