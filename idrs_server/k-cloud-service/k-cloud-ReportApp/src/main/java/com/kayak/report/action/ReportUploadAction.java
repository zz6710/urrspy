package com.kayak.report.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.util.FileUtil;
import com.kayak.report.dao.ReportUploadDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Slf4j
public class ReportUploadAction extends BaseController {

    @Autowired
    private ReportUploadDao reportUploadDao;

    @RequestMapping(value = "/upload.json", method = RequestMethod.POST)
    public String upload(@RequestParam(value = "file") MultipartFile[] files) throws Exception {
        if (ArrayUtils.isEmpty(files)) {
            return updateFailure("文件列表为空！");
        }
        for (MultipartFile multipartFile : files) {
            File temporaryFile = FileUtil.multipartFileToFile(multipartFile);
            String javaEncode = FileUtil.getJavaEncode(temporaryFile);
            FileUtil.deleteFile(temporaryFile);
            try (InputStream in = multipartFile.getInputStream()) {
                reportUploadDao.insertXMLInfo(in, javaEncode);
            } catch (Exception e) {
               log.error(e.getMessage(),e);
                return updateFailure("文件上传失败:"+e.getMessage());
            }
        }
        return updateSuccess("文件上传成功！");
    }
}
