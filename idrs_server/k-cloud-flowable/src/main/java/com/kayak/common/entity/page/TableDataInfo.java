package com.kayak.common.entity.page;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kayak.common.entity.result.R;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author yuanjinqiao
 */

@Data
@NoArgsConstructor
@ApiModel("分页响应对象")
public class TableDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    private long results;

    /**
     * 列表数据
     */
    @ApiModelProperty("列表数据")
    private List<T> rows;

    /**
     * 消息状态码
     */
    @ApiModelProperty("消息状态码")
    private int code;

    /**
     * 消息是否成功
     */
    @ApiModelProperty("消息是否成功")
    private boolean success;

    /**
     * 消息内容
     */
    @ApiModelProperty("消息内容")
    private String returnmsg;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, long total) {
        this.rows = list;
        this.results = total;
    }

    public static <T> TableDataInfo<T> build(IPage<T> page) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setSuccess(R.SUCCESS);
        rspData.setReturnmsg("查询成功");
        rspData.setRows(page.getRecords());
        rspData.setResults(page.getTotal());
        return rspData;
    }

    public static <T> TableDataInfo<T> build(List<T> list) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setSuccess(R.SUCCESS);
        rspData.setReturnmsg("查询成功");
        rspData.setRows(list);
        rspData.setResults(list.size());
        return rspData;
    }

    public static <T> TableDataInfo<T> build() {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setSuccess(R.SUCCESS);
        rspData.setReturnmsg("查询成功");
        return rspData;
    }

}
