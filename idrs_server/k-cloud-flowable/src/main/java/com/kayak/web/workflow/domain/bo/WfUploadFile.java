package com.kayak.web.workflow.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文件上传对象
 *
 * @author yuanjinqiao
 * @date 2022-05-19
 */

@Data
@ApiModel("流程上传文件对象")
public class WfUploadFile {

    @ApiModelProperty(value = "文件代码", required = true)
    @NotNull(message = "文件代码")
    private String uploadCode;

    @ApiModelProperty(value = "文件名", required = true)
    @NotNull(message = "文件名")
    private String uploadName;

    @ApiModelProperty(value = "文件路径", required = true)
    @NotBlank(message = "文件路径")
    private String uploadPath;

}
