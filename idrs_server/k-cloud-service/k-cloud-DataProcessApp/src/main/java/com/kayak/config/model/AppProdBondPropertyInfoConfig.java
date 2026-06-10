package com.kayak.config.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 房地产打标数据表 涉及 数据汇总表，发邮件的，业务类型定义(在Rpt中有用到),Dps中有用到，用配置最好。
 */
@Data
@Slf4j
@Component
public class AppProdBondPropertyInfoConfig {
    //“打标房地产明细数据锁定”
    @Value("${app.prodbond.property.bizname}")
    private String bizName;
    //业务主表 bizTable，暂时充当了邮件任务表中的业务类型bizType， 配置要大写  APP_PROD_BOND_PROPERTY_INFO
    @Value("${app.prodbond.property.tableuppername}")
    private String  tableUpperName;
    //远程S3文件存储路径  /uurs/email/
    @Value("${app.prodbond.property.remotepath}")
    private String remotePath;
    //执行业务表取数的SQL对应的KEY值
    @Value("${app.prodbond.property.exeid}")
    private String exeId;

    public AppProdBondPropertyInfoConfig(){
        bizName = this.getBizName();
        tableUpperName = this.getTableUpperName();
        remotePath = this.getRemotePath();
        exeId = this.getExeId();

    }

}
