package com.kayak.rpt.nineAttachments.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.nineAttachments.dao.NineAttachmentsLogDao;
import com.kayak.rpt.nineAttachments.model.NineAttachmentsLog;

import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.*;

@Service
@APIDefine(desc = "个人养老金文件日志", model = NineAttachmentsLog.class)
public class NineAttachmentsLogService {

    private static final Logger log = LoggerFactory.getLogger(NineAttachmentsLogService.class);

    @Autowired
    private NineAttachmentsLogDao nineAttachmentsLogDao;
    @Autowired
    public ComnDao comnDao;

    @API(desc = "查询九大附件日志信息", auth = APIAuth.YES)
    public SqlResult<NineAttachmentsLog> findNineAttachmentsLogs(SqlParam<NineAttachmentsLog> params) throws Exception {
        //params.setMakeSql(true);
        if (    StringUtils.isBlank(params.getModel().getReportSendDate())&&
                StringUtils.isNotBlank(params.getModel().getGetFileDate())&&
                StringUtils.isNotBlank(params.getModel().getProdCd())&&
                StringUtils.isNotBlank(params.getModel().getStatus())
           ) {
            SqlResult<NineAttachmentsLog> nineAttachmentsLog=new SqlResult<>();
            nineAttachmentsLog.setRows(new ArrayList<>());
            return nineAttachmentsLog;
        }
        return nineAttachmentsLogDao.findNineAttachmentsLogs(params);
    }


    public  void download(Map<String, Object> params , HttpServletResponse response) throws Exception {
        //避免中文名字文件乱码
        log.info(" >>>>> 九大附件文件下载----------------------------------------------");
        String fileName = Objects.toString(params.get("zipNm"));
        String downLoadPath = params.get("zipDir") +"/";
        Map<String, String> map = queryPs();
        String ip=Objects.toString(map.get("SFTP_IP"));
      /*  String userName = AESUtils.AESDecrypted(Objects.toString(map.get("USERNAMES")));
        String passWord = AESUtils.AESDecrypted(Objects.toString(map.get("PASSWORD")));
        SFtpHelper.getFile(ip, userName, passWord, downLoadPath, fileName, response);*/
        log.info(" >>>>> 九大附件文件下载完成---------------------------------------------");
    }
    public Map<String, String> queryPs() throws Exception {
        String qdeal = "  select t.id,t.config_describe,t.config_name,"+
                " t.config_code,t.config_type,t.status "+
                " from base_port_config_info t "+
                " where t.config_type = 'JDFJZL' and t.status ='1'" +
                " and t.config_name in ('SFTP_IP','USERNAMES','PASSWORD')";
        List<SqlRow> rs= comnDao.findRows(qdeal);
        Map<String, String> map= new HashMap<>();
        for(SqlRow r:rs){
            map.put(r.getString("config_name"),r.getString("config_code"));
        }
        return map;
    }
}
