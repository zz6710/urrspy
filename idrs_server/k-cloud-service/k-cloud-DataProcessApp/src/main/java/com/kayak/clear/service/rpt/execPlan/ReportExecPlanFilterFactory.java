package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.chain.FilterChain;
import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.clear.req.ReportTimeExecPlanOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class ReportExecPlanFilterFactory {
    @Autowired
    private ReportExecPlanFilter_BaseType01 baseType01;
    @Autowired
    private ReportExecPlanFilter_BaseType02 baseType02;
    @Autowired
    private ReportExecPlanFilter_BaseType03 baseType03;
    @Autowired
    private ReportExecPlanFilter_BaseType04 baseType04;
    @Autowired
    private ReportExecPlanFilter_BaseType05 baseType05;
    @Autowired
    private ReportExecPlanFilter_BaseType06 baseType06;
    @Autowired
    private ReportExecPlanFilter_BaseType07 baseType07;
    @Autowired
    private ReportExecPlanFilter_BaseType08 baseType08;
    @Autowired
    private ReportExecPlanFilter_BaseType09 baseType09;
    @Autowired
    private ReportExecPlanFilter_BaseType10 baseType10;
    @Autowired
    private ReportExecPlanFilter_BaseType11 baseType11;
    @Autowired
    private ReportExecPlanFilter_BaseType12 baseType12;
    @Autowired
    private ReportExecPlanFilter_BaseType13 baseType13;
    @Autowired
    private ReportExecPlanFilter_BaseType99 baseType99;

    /**
     * 获取过滤链列表工厂
     * @return
     */
    public FilterChain<ReportTimeExecPlanInput, List<ReportTimeExecPlanOutput>> getFilterChainListFactory(){
        FilterChain<ReportTimeExecPlanInput, List<ReportTimeExecPlanOutput>> chains = new FilterChain();
        chains.addFilter(baseType01);
        chains.addFilter(baseType02);
        chains.addFilter(baseType03);
        chains.addFilter(baseType04);
        chains.addFilter(baseType05);
        chains.addFilter(baseType06);
        chains.addFilter(baseType07);
        chains.addFilter(baseType08);
        chains.addFilter(baseType09);
        chains.addFilter(baseType10);
        chains.addFilter(baseType11);
        chains.addFilter(baseType12);
        chains.addFilter(baseType13);
        chains.addFilter(baseType99);
        return chains;
    }
}
