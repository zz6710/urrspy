package com.kayak.factory;

import com.kayak.web.system.service.ISystemService;
import com.kayak.web.system.service.impl.SystemMicroService;
import com.kayak.web.system.service.impl.SystemSingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-09-07 15:44
 **/
@Component
public class SystemServiceFactory {
    @Value("${kayak.service}")
    private boolean service;

    @Autowired
    private SystemMicroService systemMicroService;

    @Autowired
    private SystemSingleService systemSingleService;

    public ISystemService createService() {
        if (service) {
            return systemMicroService;
        } else {
            return systemSingleService;
        }
    }
}
