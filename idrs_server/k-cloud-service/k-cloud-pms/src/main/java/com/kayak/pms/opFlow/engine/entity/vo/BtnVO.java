package com.kayak.pms.opFlow.engine.entity.vo;

import lombok.Data;

/**
 * Created by daniel on 03/06/2017.
 */
@Data
public class BtnVO {
    private String btns;
    private String taskName;

    public BtnVO(String btns, String taskName) {
        this.btns = btns;
        this.taskName = taskName;
    }

}
