package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author yuanjinqiao
 * @description
 * @create 2023-03-08 11:49
 **/
@Data
@ApiModel("工作流预测视图对象")
public class CalculatTaskVo {
    @ApiModelProperty("任务id")
    private String id;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务类型")
    private String taskType;

    @ApiModelProperty("用户id")
    private List<String> userIds;

    @ApiModelProperty("角色id")
    private List<String> roleIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculatTaskVo that = (CalculatTaskVo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
