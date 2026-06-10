package com.kayak.rpt.rhzg.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExcelImportListener<T> extends AnalysisEventListener<T> {

    /**
     * 缓存大小
     */
    private static final int BATCH_SIZE = 1000;

    /**
     * 缓存数据
     */
    private List<T> cacheList = new ArrayList<>(BATCH_SIZE);

    /**
     * 已经成功入库的数量
     */
    private Integer successNum = 0;

    /**
     * 返回给前端的错误信息提示
     */
    private String stopMsg = "";

    private Map<String, Object> params = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(ExcelImportListener.class);


    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        cacheList.add(t);
        if (cacheList.size() >= BATCH_SIZE) {
            log.info("开始一批Excel记录的导入，条数为：{}", cacheList.size());
            try {
                params.put("excelType", analysisContext.readWorkbookHolder().getExcelType());
                getImportService().importFile(cacheList,params);
            } catch (Exception e) {
                solveException(e);
                this.stopMsg = e.getMessage();
                throw new ExcelAnalysisStopException(this.stopMsg);

            }
            cacheList = new ArrayList<>(BATCH_SIZE);
            this.successNum += BATCH_SIZE;
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        try {
            params.put("excelType", analysisContext.readWorkbookHolder().getExcelType());
            getImportService().importFile(cacheList,params);
        } catch (Exception e) {
            solveException(e);
            throw new ExcelAnalysisStopException(this.stopMsg);
        }
        this.successNum += cacheList.size();
        log.info("完成最后一批Excel记录的导入，条数为：{}。共导入【{}】条数据", cacheList.size(),this.successNum);
    }

    private void solveException(Exception e){

        this.stopMsg = "导入数据存在问题, 请修改后重传：" + e.getMessage();
        log.error(stopMsg);
    }





    protected abstract ExcelImportService<T> getImportService();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public ExcelImportListener(Map<String, Object> params) {
        this.params = params;
    }


    public String getStopMsg() {
        return stopMsg;
    }
}
