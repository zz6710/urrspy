package com.kayak.dps.check.util;

import com.kayak.dps.check.model.ExpressDictDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressDictUtil {

    //整体全量字典值，执行指标校验时加载List中为
    public static Map<String, List<ExpressDictDTO>> dictMap = new HashMap<>(); // 存放字典值字符串，用于校验合法性
}
