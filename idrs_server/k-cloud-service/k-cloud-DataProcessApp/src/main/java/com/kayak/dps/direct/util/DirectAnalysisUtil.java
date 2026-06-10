package com.kayak.dps.direct.util;

import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 直连返回文件解析工具类
 */
public class DirectAnalysisUtil {

    /**
     * 获取解析返回验证错误的数据行主键
     * @param root 待解析结点数据列表
     * @param keyName 解析keyId名称
     * @return
     */
    public static Set<String> getErrorPrimaryKeyList(Element root, String keyName) {
        //获取校验正确的数据行列表
        List<Element> errorInfos = root.elements("ErrorInformationTuple");//成功信息
        Set<String> keyIds = new HashSet<>();
        //循环获取正确的行数据id
        for (Element info : errorInfos) {
//            String errorMessage = info.elementText("ErrorMessage");
//            if (errorMessage.contains("重复")) {
//                continue;
//            }
            keyIds.add(info.elementText(keyName));

        }
        return keyIds;
    }
}
