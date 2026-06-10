package com.kayak.pms.excel.Action;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.excel.service.ExcelService;
import com.kayak.pms.excel.service.ImportFundService;
import com.kayak.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@RestController
@RequestMapping(value = "/excelUploadAction")
public class ExcelUploadAction extends BaseController {

    @Autowired
    ImportFundService importFundService;

    @RequestMapping(value = "/fundUploadAction.json")
    @ResponseBody
    public String fundUploadAction(@RequestParam(value = "file") MultipartFile file) {

        try {

            //解析方法一
//          List<Map<String,Object>> l = importFundService.readExcel(file);
            //解析方法二
            List<Map<String,Object>> l2 = ExcelUtils.parseExcel(file, new ExcelService() {

                @Override
                public int setSharding(){return DataSourceProperty.PUB; }

                @Override
                public Integer setId() {
                    return 2;
                }

                @Override
                public void saveData() throws Exception {
                    saveParseData();
                }

            });

            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
        }


    }

}
