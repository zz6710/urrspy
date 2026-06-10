package com.kayak.web.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("import_menu_file_manage")
public class ImportMenuFileManage extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String menuId;
    private String fileName;
    private String localFilePath;
    private String status;
}
