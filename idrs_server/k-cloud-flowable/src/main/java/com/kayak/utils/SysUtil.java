package com.kayak.utils;

import com.kayak.factory.SystemServiceFactory;
import com.kayak.web.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yuanjinqiao
 */
@Component
public class SysUtil {
    @Autowired
    private SystemServiceFactory systemServiceFactory;

    private static SysUtil sysUtil;

    @PostConstruct
    public void init() {
        sysUtil = this;
    }

    public static String getCurrentUserId() {
        HttpServletRequest request = RequestSupport.getLocalRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader("Authorization");
    }

    public static String getCurrentUserUsername() {
        HttpServletRequest request = RequestSupport.getLocalRequest();
        if (request == null) {
            return null;
        }
        return getCurrentUserInfo().getUsername();
    }

    public static SysUser getCurrentUserInfo() {
        HttpServletRequest request = RequestSupport.getLocalRequest();
        final String userid = request.getHeader("Authorization");
        return sysUtil.systemServiceFactory.createService().getUserInfo(userid);
    }
}
