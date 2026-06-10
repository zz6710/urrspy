package com.kayak.web.workflow.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kayak.common.entity.BaseEntity;
import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 任务代理业务对象 flow_surrogate
 *
 * @author yuanjinqiao
 * @date 2022-10-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务代理业务对象")
public class WfSurrogateBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 流程key
     */
    @ApiModelProperty(value = "流程key")
    @NotBlank(message = "流程key不能为空", groups = {AddGroup.class, EditGroup.class})
    private String processKey;

    /**
     * 流程名
     */
    @ApiModelProperty(value = "流程名")
    @NotBlank(message = "流程名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String processName;

    /**
     * 代理开始日期
     */
    @ApiModelProperty(value = "代理开始日期")
    @NotNull(message = "代理开始日期不能为空", groups = {AddGroup.class, EditGroup.class})
    @DateTimeFormat(pattern = "yyyyMMdd")
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 代理结束日期
     */
    @ApiModelProperty(value = "代理结束日期")
    @NotNull(message = "代理结束日期不能为空", groups = {AddGroup.class, EditGroup.class})
    @DateTimeFormat(pattern = "yyyyMMdd")
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 授权人
     */
    @ApiModelProperty(value = "授权人")
    private String creator;

    /**
     * 授权人姓名
     */
    @ApiModelProperty(value = "授权人姓名")
    private String createName;

    /**
     * 代理人
     */
    @ApiModelProperty(value = "代理人")
    @NotBlank(message = "代理人不能为空", groups = {AddGroup.class, EditGroup.class})
    private String surrogate;

    /**
     * 代理人名字
     */
    @ApiModelProperty(value = "代理人名字")
    @NotBlank(message = "代理人名字不能为空", groups = {AddGroup.class, EditGroup.class})
    private String surrogateName;

    /**
     * 状态 1-启用  0-禁用
     */
    @ApiModelProperty(value = "状态 1-启用  0-禁用")
    @NotBlank(message = "状态 1-启用  0-禁用不能为空", groups = {AddGroup.class, EditGroup.class})
    private String status;

}
