package com.kayak.sqlinfo.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.sqlinfo.service.SqlInfoService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SqlInfoAction extends BaseController {

    @Autowired
    private SqlInfoService sqlInfoService;

    /**
     * 报表查询条件信息查询 
     * @param id
     * @return
     */
    @PostMapping(value = "/base/sqlInfo/sqlInfo.json")
    public Object sqlInfoQuery(@RequestParam String id) {

        Map<String, Object> params = new HashMap<>();
        if (null != id && (id.contains("update") || id.contains("delete") || id.contains("insert"))) {
            return updateFailure("仅限查询类语句");
        }
        params.put("id", id);
//        if (!RequestSupport.getCanCode(params)) {
//            return updateSuccess();
//        }
        try {
            Object obj = sqlInfoService.requestPostForm("BaseServer", "/base/sqlInfo/sqlInfo.json", params);
            String jsonStr = new ObjectMapper().writeValueAsString(obj);
            return updateSuccess(new JSONArray(jsonStr));
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
    }

    @GetMapping (value = "/getResultData.json")
    public String reportDataQuery() {
        try {
            log.info("访问地址：/getResultData.json");
            Object obj = sqlInfoService.reportData("RptApp", "/getResultData.json",RequestSupport.getParameters());
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
    }

    /**
     * 估值表导入
     * @return
     */
    @PostMapping(value = "/valtabImpdata/valTabImport.action")
    public void importDps(MultipartHttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "file", required = false) MultipartFile[] files) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> params = new HashMap<>();
        params.put("assetCode", request.getParameter("asset_code"));
        params.put("isprodorasset", request.getParameter("isprodorasset"));
        try {
            sqlInfoService.importDps("DpsApp", "/valtabImpdata/valTabImport.action", params, files);
            response.getWriter().write(updateSuccess("委外估值文件解析完成"));
        } catch (Exception e) {
            response.getWriter().write(updateFailure("委外估值文件上传失败:" + e.getMessage()));
        }
    }
}
