package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;

/**
 * Created by daniel on 31/03/2017.
 */
@Data
public class User {
    private String departmentId;
    private String roleId;

    public User() {
    }

    public User(String departmentId, String roleId) {
        this.departmentId = departmentId;
        this.roleId = roleId;
    }

}
