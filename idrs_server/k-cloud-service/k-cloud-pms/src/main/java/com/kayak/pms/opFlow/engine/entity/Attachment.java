package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 12/06/2017.
 */
@Data
@Alias("attachment")
public class Attachment {

    private String id;
    private String originalFilename;
    private String newId;
    private boolean disableDelete = true;//只允许当前用户上次的文件才可以删除
    private String fileExtension;
    private String contentType;
    private Long fileSize;
    private String createDate;
    private String createTime;
    private String url;
    private String approvalId;
    private String username;

    private String operator;

}
