package com.kayak.pms.opFlow.engine.entity.result;

import lombok.Data;

/**
 * Created by daniel on 31/03/2017.
 */
@Data
public class Error {
    private Integer code;
    private String message;

    public Error(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
