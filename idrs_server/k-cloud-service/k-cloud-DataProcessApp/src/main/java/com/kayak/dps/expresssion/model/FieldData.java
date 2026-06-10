package com.kayak.dps.expresssion.model;

public class FieldData {
    //行号
    private String rowId = "";
    //列号
    private String columnId = "";
    //值
    private String dataValue = "";

    public FieldData(String row_id, String column_id, String data_value) {
        this.rowId = row_id;
        this.columnId = column_id;
        this.dataValue = data_value;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "rowId='" + rowId + '\'' +
                ", columnId='" + columnId + '\'' +
                ", dataValue='" + dataValue + '\'' +
                '}';
    }
}
