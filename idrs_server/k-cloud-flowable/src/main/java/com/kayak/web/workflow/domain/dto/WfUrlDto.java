package com.kayak.web.workflow.domain.dto;

import lombok.Data;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-11-03 11:07
 **/
@Data
public class WfUrlDto {
    /**
     * url键
     */
    private String urlKey;

    /**
     * url值
     */
    private String urlValue;

    /**
     * 请求类型
     */
    private String requestType;
}
