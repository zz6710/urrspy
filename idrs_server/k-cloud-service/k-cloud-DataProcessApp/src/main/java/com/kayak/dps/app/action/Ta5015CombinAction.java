package com.kayak.dps.app.action;

import com.alibaba.fastjson.JSON;
import com.kayak.config.model.Ta5015;
import com.kayak.config.service.Ta5015CombinService;
import com.kayak.core.action.BaseController;
import com.kayak.core.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@RestController
public class Ta5015CombinAction extends BaseController {

    @Autowired
    private Ta5015CombinService service;

    @RequestMapping(value = "/Ta5015Combin/commit.json", produces = {"application/json;charset=UTF-8"})
    public String importBaseReportFileMange(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile multipartFile)  throws Exception{
        File file = null;
        StringBuilder dataBuilder = new StringBuilder();
        try {
            if (multipartFile == null) {
                return updateFailure("上传文件为空");
            }
            // 转换File
            File tmpFile = FileUtil.multipartFileToFile(multipartFile);
            file = new File(tmpFile.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String strLine;
            while((strLine = reader.readLine()) != null){
                dataBuilder.append(strLine);
            }
            reader.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        } finally {
            FileUtil.delFile(file);
        }

        String jsonData = dataBuilder.toString();
        String trueJsonData = jsonData.substring(jsonData.indexOf("[{"),jsonData.lastIndexOf("}]")+2);
        return service.submit(trueJsonData);
    }
}
