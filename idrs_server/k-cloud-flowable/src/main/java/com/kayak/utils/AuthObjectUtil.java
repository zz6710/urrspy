package com.kayak.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.web.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户角色信息转换
 *
 * @author yuanjinqiao
 * @date 2021/3/4
 */
@Component
public class AuthObjectUtil {

    @Autowired
    private SystemServiceFactory systemServiceFactory;

    private static AuthObjectUtil authObjectUtil;

    @PostConstruct
    public void init() {
        authObjectUtil = this;
    }

    /**
     * 用户信息补充
     * 建议使用FieldUtil.noPrefix()获取字段,避免硬编码
     *
     * @param obj           转换对象
     * @param convertRule   转换规则(key=userid字段，value=被填充字段)
     * @param fillFieldName 填充字段
     */
    public static void complementUserInfo(Object obj, Map<String, String> convertRule, String fillFieldName) {
        if (obj == null || convertRule == null || CollectionUtil.isEmpty(convertRule)) {
            return;
        }
        Set<String> keys = convertRule.keySet();

        // 获取userids
        Set<String> userids = new HashSet<>();
        for (String key : keys) {
            if (obj instanceof List) {
                ((List) obj).stream().forEach(v -> userids.add(ReflectUtil.invoke(v, "get" + StrUtil.upperFirst(key))));
            } else {
                userids.add(ReflectUtil.invoke(obj, "get" + StrUtil.upperFirst(key)));
            }
        }
        if(CollectionUtil.isEmpty(userids)){
            return;
        }
        // 获取用户信息
        List<SysUser> userList = authObjectUtil.systemServiceFactory.createService().findUserByIds(userids);
        Map<String, SysUser> userMap = userList.stream().collect(Collectors.toMap(s -> s.getUserid(), s -> s));

        // 补充用户信息
        if (obj instanceof List) {
            ((List) obj).stream().forEach(v -> {
                keys.stream().forEach(key -> {
                    SysUser user = userMap.get(ReflectUtil.invoke(v, "get" + StrUtil.upperFirst(key)));
                    if (user != null) {
                        Object fillFieldValue = ReflectUtil.invoke(user, "get" + StrUtil.upperFirst(fillFieldName));
                        ReflectUtil.invoke(v, "set" + StrUtil.upperFirst(convertRule.get(key)), fillFieldValue);
                    }
                });
            });
        } else {
            keys.stream().forEach(key -> {
                SysUser user = userMap.get(ReflectUtil.invoke(obj, "get" + StrUtil.upperFirst(key)));
                if (user != null) {
                    Object fillFieldValue = ReflectUtil.invoke(user, "get" + StrUtil.upperFirst(fillFieldName));
                    ReflectUtil.invoke(obj, "set" + StrUtil.upperFirst(convertRule.get(key)), fillFieldValue);
                }
            });
        }
    }

}
