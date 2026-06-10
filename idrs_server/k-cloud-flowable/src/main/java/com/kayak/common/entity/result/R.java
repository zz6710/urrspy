package com.kayak.common.entity.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author yuanjinqiao
 */
@Data
@NoArgsConstructor
@ApiModel("请求响应对象")
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败
     */
    public static final int FAIL_CODE = 500;

    /**
     * 成功
     */
    public static final boolean SUCCESS = true;

    /**
     * 失败
     */
    public static final boolean FAIL = false;

    @ApiModelProperty("消息状态码")
    private int code;

    @ApiModelProperty("消息是否成功")
    private boolean success;

    @ApiModelProperty("消息内容")
    private String returnmsg;

    @ApiModelProperty("数据对象")
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS_CODE, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS_CODE, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(String msg) {
        return restResult(null, SUCCESS_CODE, SUCCESS, msg);
    }

    public static R ok(String msg, Object data) {
        return restResult(data, SUCCESS_CODE, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL_CODE, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL_CODE, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL_CODE, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg, T data) {
        return restResult(data, FAIL_CODE, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, FAIL, msg);
    }

    private static <T> R<T> restResult(T data, int code, boolean success, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setSuccess(success);
        r.setData(data);
        r.setReturnmsg(msg);
        return r;
    }

}
