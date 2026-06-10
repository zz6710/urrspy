package com.kayak.pms.opFlow.engine.entity.vo;

import java.util.List;

/**
 * Created by daniel on 11/05/2017.
 */
public class BootstrapTableVO<T> {
    private List<T> rows;
    private long results;

    public BootstrapTableVO(List<T> rows, long results) {
        this.rows = rows;
        this.results = results;
    }

    public BootstrapTableVO() {
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getResults() {
        return results;
    }

    public void setResults(long results) {
        this.results = results;
    }
}
