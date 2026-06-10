package com.kayak.pms.connect.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @program: k-cloud
 * @description: sftp配置文件对象
 * @author: WangZhenXin
 * @create: 2020-12-31 11:24
 * @memo 备注信息
 */
@Data
@Component
public class SftpConfig {

    private String id;

    private String mailbox;

    private String host;

    private String port;

    private String charset;

    private String username;

    private String password;

    private String privateKey;

    private String method;

}
