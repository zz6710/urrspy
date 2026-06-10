package com.kayak.pms.opFlow.engine.entity;

import com.kayak.graphql.annotation.GraphQLModel;

/**
 * Created by daniel on 25/05/2017.
 */

@GraphQLModel(fetcher = "selectEntityService")
public class SelectEntity {
    private String value;
    private String label;

    public SelectEntity(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public SelectEntity() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
