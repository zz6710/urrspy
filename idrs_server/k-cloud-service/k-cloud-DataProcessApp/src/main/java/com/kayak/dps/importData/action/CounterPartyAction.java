package com.kayak.dps.importData.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.CounterPartyModelDao;
import com.kayak.dps.app.model.CounterPartyModel;
import com.kayak.dps.app.service.CounterPartyService;
import com.kayak.dps.export.util.ExcelParse;
import com.kayak.dps.outLands.dao.OutLandsDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class CounterPartyAction extends BaseController {


    @Autowired
    private OutLandsDao outLandsDao;
    private RequestSupport RequestSuppor;
    @Autowired
    private CounterPartyService counterPartyService;
    @Autowired
    private CounterPartyModelDao counterPartyModelDao;

    @RequestMapping(value = "/counterPartyUpload.json",produces = { "application/json;charset=UTF-8"})
    public String outLandsRaiseUpload( @RequestParam(value = "file", required = false) MultipartFile file,  HttpServletResponse response) throws IOException, InvalidFormatException {
        List<CounterPartyModel> reportPCDS = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, CounterPartyModel.class, true, null);
        // 移除第一标题行
        reportPCDS.remove(0);
        try {
            counterPartyModelDao.updateImportEnterpScale(reportPCDS);
        } catch (Exception e) {
            e.printStackTrace();
            if(StringUtils.equals(e.getMessage(),"Sheet index (1) is out of range (0..0)")){
                return updateFailure("导入失败，导入的sheet参数不正确！ ");
            }else{
                return updateFailure("导入失败！ "+e.getMessage());
            }
        }
        return updateSuccess("导入成功！");
    }


    //从业人员登记信息管理
    @RequestMapping(value = "/counterPartyInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileTrPractyRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "CounterParty.xlsx";
        counterPartyService.exportFile(response, fileName);
    }



}