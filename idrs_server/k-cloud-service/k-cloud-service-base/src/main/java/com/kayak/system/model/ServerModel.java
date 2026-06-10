package com.kayak.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerModel {
    private String modelName;
    private String appName;
    private String modelFullName;
    private String modelField;
    private String serverName;
    private String isEncrypt;
    private String encryptField;
    private String modelKeys;
    private String modelLabel;
    private String modelTable;
}