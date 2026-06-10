package com.kayak.rpt.zz.manage.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.rpt.config.dao.ReportTimeConfigDao;
import com.kayak.rpt.config.model.ReportTimeConfig;
import com.kayak.rpt.zz.manage.model.ReportTimeConfigInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(desc = "报送时点配置导入服务", model = ReportTimeConfigInfo.class)
@Slf4j
public class ReportTimeConfigImportService {

    @Autowired
    ReportTimeConfigDao dao;
    /**
     * 导入数据列表
     * @param list 集合
     * @throws Exception
     */
    public void importReportTimeConfigList(List<ReportTimeConfigInfo> list) throws Exception {
        if(list != null && list.size() >0){
            for(ReportTimeConfigInfo item: list){
                ReportTimeConfig info = new ReportTimeConfig();
                info.setTimeType("1"); //设置为非规则配置
                info.setReportType(item.getReportType());
                info.setReportTable(item.getReportTable());
                StringBuilder sb = new StringBuilder();
                buildItem(item.getMonth01(),sb);
                buildItem(item.getMonth02(),sb);
                buildItem(item.getMonth03(),sb);
                buildItem(item.getMonth04(),sb);
                buildItem(item.getMonth05(),sb);
                buildItem(item.getMonth06(),sb);
                buildItem(item.getMonth07(),sb);
                buildItem(item.getMonth08(),sb);
                buildItem(item.getMonth09(),sb);
                buildItem(item.getMonth10(),sb);
                buildItem(item.getMonth11(),sb);
                buildItem(item.getMonth12(),sb);

                String allData = sb.toString();
                if(allData.length() > 0){
                    info.setEndDateString(allData.substring(0,allData.length()-1));
                    dao.flashEndDataList(info);
                }
            }
        }
    }

    private void buildItem(String data, StringBuilder sb){
        if(data != null && data.length() > 0){
            sb.append(data);
            sb.append(",");
        }
    }

}
