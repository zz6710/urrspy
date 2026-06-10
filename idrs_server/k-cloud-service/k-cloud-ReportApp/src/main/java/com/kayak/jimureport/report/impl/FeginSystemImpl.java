//package com.kayak.jimureport.report.impl;
//
//
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.ws.rs.core.MediaType;
//
///**
// * system 短信接口
// */
//@Component
//@FeignClient(name = "fms-system-server")
//public interface FeginSystemImpl {
//
//    @RequestMapping(value = "/sendSmsMsg?errmsg={errmsg}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
//    String sendSmsMsg(@PathVariable("errmsg") String errmsg);
//}
