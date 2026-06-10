package com.kayak.bak.business.action;

import com.kayak.bak.core.bak.TableRedo;
import com.kayak.bak.model.dto.RedoDataDTO;
import com.kayak.core.system.RequestSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/bakRedo")
public class BakRedoAction {

    @Resource
    private TableRedo tableRedo;

    /**
     * 还原指定日期区间数据
     * @param param
     */
    @RequestMapping(value = "/redoData.action")
    public String redoData(RedoDataDTO param) {
        try {
            tableRedo.redoData(param);
            return RequestSupport.updateReturnJson(true,"数据还原成功！",null).toString();
        } catch (Exception e) {
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }
}
