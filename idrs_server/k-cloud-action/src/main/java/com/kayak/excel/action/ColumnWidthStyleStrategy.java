package com.kayak.excel.action;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @Author liwenkai
 */
public class ColumnWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

    private static final int MAX_COLUMN_WIDTH = 256;
    private final Map<Integer, Map<Integer, Integer>> CACHE = new HashMap(8);

    public ColumnWidthStyleStrategy() {
    }

    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, CellData cellData, Cell cell, Head head, int relativeRowIndex, boolean isHead) {
        if (isHead || cellData != null) {
            Map<Integer, Integer> maxColumnWidthMap = (Map)CACHE.get(writeSheetHolder.getSheetNo());
            if (maxColumnWidthMap == null) {
                maxColumnWidthMap = new HashMap(16);
                CACHE.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
            }

            Integer columnWidth = this.dataLength(cellData, cell, isHead);
            if (columnWidth >= 0) {
                if (columnWidth > 255) {
                    columnWidth = 255;
                }

                Integer maxColumnWidth = (Integer)((Map)maxColumnWidthMap).get(head.getColumnIndex());
                if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                    ((Map)maxColumnWidthMap).put(head.getColumnIndex(), columnWidth);
                    writeSheetHolder.getSheet().setColumnWidth(head.getColumnIndex(), columnWidth * 256);
                }

            }

            if(!isHead && cellData.getType() == CellDataTypeEnum.STRING && cellData.getStringValue().indexOf("\n") != -1){// 设置自动换行
                CellStyle cellStyle = writeSheetHolder.getParentWriteWorkbookHolder().getWorkbook().createCellStyle();
                cellStyle.setWrapText(true);
                cell.setCellStyle(cellStyle);
            }
        }

    }

    private Integer dataLength(CellData cellData, Cell cell, boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        } else {
            switch(cellData.getType()) {
                case STRING:// 字符串根据最长行定义长度
                    int len = 0;
                    String[] cellstrs = cellData.getStringValue().split("\n");
                    for(String cellstr : cellstrs){
                        if(cellstr.getBytes().length > len){
                            len = cellstr.getBytes().length;
                        }
                    }
                    return len;
                case BOOLEAN:
                    return cellData.getBooleanValue().toString().getBytes().length;
                case NUMBER:
                    return cellData.getNumberValue().toString().getBytes().length;
                default:
                    return -1;
            }
        }
    }

}
