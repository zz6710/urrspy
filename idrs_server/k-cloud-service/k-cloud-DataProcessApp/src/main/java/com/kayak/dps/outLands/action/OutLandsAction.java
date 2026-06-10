package com.kayak.dps.outLands.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.outLands.dao.OutLandsDao;
import com.kayak.dps.outLands.util.impOutLandsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class OutLandsAction extends BaseController {


    @Autowired
    private OutLandsDao outLandsDao;
    private RequestSupport RequestSuppor;

    @RequestMapping(value = "/outLandsRaiseUpload.json",produces = { "application/json;charset=UTF-8"})
    public String outLandsRaiseUpload( @RequestParam(value = "file", required = false) MultipartFile file,  HttpServletResponse response) {
        try {
            Map<String, Object> params = RequestSupport.getParameters();
            int rowStart = Integer.valueOf(params.get("rowStart").toString());
            int sheetNumber = Integer.valueOf(params.get("sheetNumber").toString());
           String dataDate = params.get("dataDt").toString();
            String[] fieldArrs = {"id","prodCd","prodNm","clcBal"};
            if (Objects.requireNonNull(file.getOriginalFilename()).startsWith("资管产品境内募集余额分地区统计表") || Objects.requireNonNull(file.getOriginalFilename()).startsWith("境外募集余额") ) {
            }else{ return updateFailure("导入模版格式错误！");}
            List<Map<String, Object>> outLandsRaiseList = impOutLandsUtil.readExcelData(file.getInputStream(), sheetNumber, 0, rowStart,  true, null,fieldArrs);
            //String dataDate = outLandsRaiseList.get(1).get(fieldArrs[1]).toString();//日期读取文件第二个字段
            if (outLandsRaiseList.size() < 0) {
                return updateFailure("导入文件为空文件");
            }
            outLandsDao.addOutLandsRaise(dataDate,outLandsRaiseList);
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

    @RequestMapping(value = "/outLandsCaseUpload.json",produces = { "application/json;charset=UTF-8"})
    @Transient
    public String outLandsCaseUpload( @RequestParam(value = "file", required = false) MultipartFile file,  HttpServletResponse response) {
        try {
            Map<String, Object> params = RequestSuppor.getParameters();
            int rowStart = Integer.valueOf(params.get("rowStart").toString());
            int sheetNumber = Integer.valueOf(params.get("sheetNumber").toString());
            String dataDate = params.get("dataDt").toString();
            String[] fieldArrs = {"id","prodCd","prodNm","clcAmt","callAmt"};
            if (Objects.requireNonNull(file.getOriginalFilename()).startsWith("资管产品境内募集余额分地区统计表") || Objects.requireNonNull(file.getOriginalFilename()).startsWith("境外募集及兑付发生额") ) {
            }else{ return updateFailure("导入模版格式错误！");}
            List<Map<String, Object>> outLandsCashList = impOutLandsUtil.readExcelData(file.getInputStream(), sheetNumber, 0, rowStart,  true, null,fieldArrs);
           // String dataDate = outLandsCashList.get(1).get(fieldArrs[1]).toString().split("-")[1].trim();//日期读取文件第二个字段
            if (outLandsCashList.size() < 0) {
                return updateFailure("导入文件为空文件");
            }
            outLandsDao.addOutLandsCash(dataDate,outLandsCashList);
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



    /**
     * 下载模板
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    //@RequestMapping(value = "/downloadScheduleTemp.json", produces = {"application/json;charset=UTF-8"})

}