package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8PortInfoService",table = "base_port_manage")
public class T8PortInfoModel {
    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;
    @GraphQLField(label = "接口代码" ,field = "port_code")
    private String portCode;
    @GraphQLField(label = "接口名称" ,field = "port_name")
    private String portName;
    @GraphQLField(label = "接口exeid" ,field = "port_exeid")
    private String portExeid;
    @GraphQLField(label = "地址" ,field = "port_address")
    private String portAddress;
    @GraphQLField(label = "状态" ,field = "port_state")
    private String portState;
    @GraphQLField(label = "录入柜员" ,field = "inputuser")
    private String inputuser;
    @GraphQLField(label = "创建日期" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(label = "创建时间" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(label = "更新日期" ,field = "upd_date")
    private String updDate;
    @GraphQLField(label = "更新时间" ,field = "upd_time")
    private String updTime;
    @GraphQLField(label = "文件类型" ,field = "file_type")
    private String fileType;
    @GraphQLField(label = "分隔符" ,field = "separator")
    private String separator;
    @GraphQLField(label = "接口类型" ,field = "port_type")
    private String portType;
    @GraphQLField(label = "接口方向" ,field = "port_dir")
    private String portDir;
    @GraphQLField(label = "接口表名" ,field = "port_table")
    private String portTable;
    @GraphQLField(label = "处理日期" ,field = "deal_date")
    private String dealDate;
    @GraphQLField(label = "ip地址" ,field = "sftp_ip")
    private String sftpIp;
    @GraphQLField(label = "端口号" ,field = "sftp_port")
    private String sftpPort;
    @GraphQLField(label = "用户名" ,field = "username")
    private String username;
    @GraphQLField(label = "密码" ,field = "password")
    private String password;
    @GraphQLField(label = "本地路径" ,field = "local_path")
    private String localPath;
    @GraphQLField(label = "服务器路径" ,field = "remote_path")
    private String remotePath;
    @GraphQLField(label = "同步文件跳过行数" ,field = "skip_rows")
    private String skipRows;
    @GraphQLField(label = "是否带分隔符结束" ,field = "has_end_separator")
    private String hasEndSeparator;
    @GraphQLField(label = "关联任务id" ,field = "pid")
    private String pid;
    @GraphQLField(label = "是否分片处理" ,field = "slice_flag")
    private String sliceFlag;
    @GraphQLField(label = "分片源表数据库" ,field = "slice_data_source")
    private String sliceDataSource;
    @GraphQLField(label = "分片关键字" ,field = "split_key")
    private String splitKey;
    @GraphQLField(label = "源数据查询条件" ,field = "split_where")
    private String splitWhere;
    @GraphQLField(label = "源数据查询sql" ,field = "select_sql")
    private String selectSql;
    @GraphQLField(label = "源数据查询sql" ,field = "slice_select_sql")
    private String sliceSelectSql;
    @GraphQLField(label = "每片最大条数" ,field = "slice_count")
    private String sliceCount;
    @GraphQLField(label = "rdf模板内容" ,field = "temp_content")
    private String tempContent;
    @GraphQLField(label = "分片文件存放路径" ,field = "slice_file_path")
    private String sliceFilePath;
    @GraphQLField(label = "分片文件合并后存放路径" ,field = "slice_merge_path")
    private String sliceMergePath;
    @GraphQLField(label = "数据来源" ,field = "source_from")
    private String sourceFrom;
    @GraphQLField(kkhtml = "KFieldText", label = "字段代码 ", sql = "field_code = $S{fieldCode}" ,field = "field_code")
    private String fieldCode;
    @GraphQLField(kkhtml = "KFieldText", label = "字段名称 ", sql = "field_name like '%$U{fieldName}%'" ,field = "field_name")
    private String fieldName;
    @GraphQLField(kkhtml = "KFieldText", label = "字段类型 ", sql = "field_type = $S{fieldType}" ,field = "field_type")
    private String fieldType;
    @GraphQLField(kkhtml = "KFieldText", label = "字段序号 ", sql = "field_seq = $S{fieldSeq}" ,field = "field_seq")
    private String fieldSeq;
    @GraphQLField(label = "读文件插入sql" ,field = "insert_sql")
    private String insertSql;
    @GraphQLField(label = "源表名" ,field = "slice_table_name")
    private String sliceTableName;
    @GraphQLField(label = "字符集" ,sql = "charset = $S{charset}" ,field = "charset")
    private String charset;
    @GraphQLField(label = "xml节点信息" ,sql = "xml_node_info = $S{xmlNodeInfo}" ,field = "xml_node_info")
    private String xmlNodeInfo;
    @GraphQLField(label = "同步类型" ,sql = "synch_type = $S{synchType}" ,field = "synch_type")
    private String synchType;
    @GraphQLField(label = "文件不存在是否跳过" ,field = "skip_no_file")
    private String skipNoFile;
}
