package com.kayak.rpt.zz.manage.action;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import com.kayak.rpt.zz.manage.service.BaseReportExportLogService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class BaseReportExportLogAction  extends BaseController {

    @Autowired
    BaseReportExportLogService baseReportExportLogService;

    @RequestMapping(value = "/baseReportExportLog/download.json")
    public void download(HttpServletResponse response,
                         @RequestParam(value = "id") String id) throws Exception {
        if (id == null || id.length() == 0) {
            log.error("下载文件异常，缺少id参数.");
            return;
        }
        if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
            log.error("检测到sql注入异常");
            return;
        }
        BaseReportExportLog exportLog = new BaseReportExportLog();
        exportLog.setId(id);
        FetcherData<BaseReportExportLog> fetcherData = new FetcherData<>(BeanUtil.beanToMap(exportLog), BaseReportExportLog.class);
        SqlResult<BaseReportExportLog> sqlResult = baseReportExportLogService.findBaseReportExportLogs(fetcherData);
        if (sqlResult.getRows().isEmpty()) {
            return;
        }
        BaseReportExportLog reportExportLog = sqlResult.getRows().get(0);
        String localPath = reportExportLog.getFilePath();
        String remotePath = reportExportLog.getRemotePath();
        File file = new File(localPath);
        // 本地不存在，从远程下载
        if (!file.exists() && Tools.isNotEmpty(remotePath)) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileTransfer transfer = FileTransferHelpler.getTransfer();
            transfer.downloadFileAndDisconnect(remotePath, localPath);
        }
        if (file.exists()) {
            try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
                OutputStream outputStream = response.getOutputStream()) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream;charset=utf-8");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                response.setContentLength((int) file.length());
                IOUtils.copy(input, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("file not exits.");
        }
    }
}
