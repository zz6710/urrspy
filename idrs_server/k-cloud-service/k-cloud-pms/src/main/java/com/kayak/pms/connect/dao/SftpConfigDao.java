package com.kayak.pms.connect.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.connect.model.SftpConfig;
import org.bouncycastle.asn1.x9.OtherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: k-cloud
 * @description: 读取sftp配置文件Dao
 * @author: WangZhenXin
 * @create: 2020-12-31 11:22
 * @memo 备注信息
 */
@Repository
public class SftpConfigDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(SftpConfigDao.class);

    public List<SftpConfig> getAllSftpConfig() throws Exception {
        ArrayList<SftpConfig> sftpConfigArrayList = new ArrayList<>();
        /*List<SqlRow> rowList = super.findRows("select id, mailbox, host, port, charset, username, password, private_key, method " +
                "from t8_sftp_config t", DataSourceProperty.PUB, new SftpConfig());
        for (SqlRow sqlRow : rowList) {
            SftpConfig sftpConfig = new SftpConfig();
            sftpConfig.setId(sqlRow.getString("id"));
            sftpConfig.setMailbox(sqlRow.getString("mailbox"));
            sftpConfig.setHost(sqlRow.getString("host"));
            sftpConfig.setPort(sqlRow.getString("port"));
            sftpConfig.setCharset(sqlRow.getString("charset"));
            sftpConfig.setUsername(sqlRow.getString("username"));
            sftpConfig.setPassword(sqlRow.getString("password"));
            sftpConfig.setPrivateKey(sqlRow.getString("private_key"));
            sftpConfig.setMethod(sqlRow.getString("method"));
            sftpConfigArrayList.add(sftpConfig);
        }*/
        return sftpConfigArrayList;
    }
}
