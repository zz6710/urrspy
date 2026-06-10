package com.kayak.dps.py.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.kayak.core.system.SysBeans;
import com.kayak.dps.pub.ICallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class ExcelImportListener<T> extends AnalysisEventListener<T> {

    /**
     * 缓存大小
     */
    private Integer BATCH_SIZE;

    /**
     * 缓存数据
     */
    private List<T> cacheList ;

    /**
     * 已经成功入库的数量
     */
    private Integer successNum = 0;

    /**
     * 返回给前端的错误信息提示
     */
    private String stopMsg = "";



    private Map<String, Object> params = new HashMap<>();

    private ICallback handle ;

    private static final Logger log = LoggerFactory.getLogger(ExcelImportListener.class);


    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        cacheList.add(t);
        if (cacheList.size() >= BATCH_SIZE) {
            log.info("开始一批Excel记录的导入，条数为：{}", cacheList.size());
            try {
                importFile(cacheList);
            } catch (Exception e) {
                solveException(e);
                throw new ExcelAnalysisStopException(this.stopMsg);

            }
            cacheList = new ArrayList<>(BATCH_SIZE);
            this.successNum += BATCH_SIZE;
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        try {
           importFile(cacheList);
        } catch (Exception e) {
            solveException(e);
            throw new ExcelAnalysisStopException(this.stopMsg);
        }
        this.successNum += cacheList.size();
        log.info("完成最后一批Excel记录的导入，条数为：{}。共导入【{}】条数据", cacheList.size(),this.successNum);
    }

    protected void importFile(List<T> cacheList) throws Exception {
        handle.call(cacheList);
    }

    private void solveException(Exception e){

        this.stopMsg = "已成功导入:" + this.successNum  + "条 数据！" + "文件行标为:" + (this.successNum + 2) + "到" + (this.successNum + 2 + BATCH_SIZE) + " 的数据存在问题, 请修改后将数据或者余下数据重传。特别注意上传时日期范围选择！！！：" + e.getMessage();
        log.error(stopMsg);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public ExcelImportListener(ICallback handle) {
        this.handle = handle;
        ExcelImportModel model= SysBeans.getBean("excelImportModel");
        this.BATCH_SIZE=model.getBatchSize();
        if(BATCH_SIZE==null ) BATCH_SIZE=1000;
        cacheList=new ArrayList<>(BATCH_SIZE);
    }


    public String getStopMsg() {
        return stopMsg;
    }
}
