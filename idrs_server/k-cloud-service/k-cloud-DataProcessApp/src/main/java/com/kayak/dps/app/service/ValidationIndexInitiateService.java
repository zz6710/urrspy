package com.kayak.dps.app.service;

import com.kayak.dps.app.dao.ValidationIndexInitiateDao;
import com.kayak.dps.app.model.CheckIndexModel;
import com.kayak.dps.app.model.ValidationIndexTempModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ValidationIndexInitiateService {

    @Resource
    private ValidationIndexInitiateDao validationIndexInitiateDao;

    /**
     * 报送数据校验指标初始数据转换方法(临时)
     * @return
     * @throws Exception
     */
    public void validationIndexTransferHandler() throws Exception {
        List<ValidationIndexTempModel> indexModelList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String hd_table = "";
        log.info("————————————————————————————————————————————报表校验指标转换任务批处理开始————————————————————————————————————");
        try{
            /** 查询所有需要进行指标校验的报表 */
            List<String> tableList = validationIndexInitiateDao.getInvalidateTableList();
            for (String tbName : tableList) {
                log.info("报表" + tbName + "校验指标转换任务处理开始");
                hd_table = tbName;params.put("report_table", tbName);
                String report_table = validationIndexInitiateDao.getReportTableByName(tbName);//根据校验报表名称获取其对应表名

                if (null == report_table || "".equals(report_table)) {//报表不存在时跳过
                    continue;
                }

                /** 根据校验表名获取其所有的校验指标信息 */
                indexModelList = validationIndexInitiateDao.getValidationIndexByIndexType(params);
                String project_type = indexModelList.get(0).getProjectType();//校验项目类型:1-一维/2-二维

                for (ValidationIndexTempModel tempIndex : indexModelList) {
                    if (!"041040".equals(tempIndex.getIndexCode())) {
                        continue;
                    }

                    String expression = "";

                    CheckIndexModel index = new CheckIndexModel();
                    index.setIndexCode(tempIndex.getIndexCode());//校验指标代码
                    index.setReportTable(report_table);//报送表名

                    if(tempIndex.getIndexType().trim().length() <=2) {//当校验指标没有标注时，插一条空数据
                        index.setAllowDeviation("0.00");//允许差值默认为0
                        index.setIndexDetail(tempIndex.getIndexDetail());//校验指标详述
                        index.setCorrectPrompt(tempIndex.getCorrectPrompt());//校验正确模板
                        index.setErrorPrompt(tempIndex.getErrorPrompt());//校验异常模板
                        index.setRemark(tempIndex.getRemark());//备注
                        validationIndexInitiateDao.putIndexIn(index);//指标数据入库
                        continue;
                    }
                    String index_type = tempIndex.getIndexType().trim().substring(0,2);//校验指标类型

                    if ("1".equals(project_type)) {
                        index.setRowNum(null);//校验字段行
                        index.setRowName(null);//校验行名称
                        String [] strC = validationIndexInitiateDao.getRowOrColumnNumByName("C", project_type, tempIndex.getProjectColumn(), report_table);
                        if (null == strC) {
                            index.setColumnNum(null);//校验字段列
                            index.setListName(null);//校验列名称
                        } else {
                            index.setColumnNum(strC[0]);//校验字段列
                            index.setListName(strC[1]);//校验列名称
                        }

                        if (null != strC) {
                            index.setExpress(this.analysisIndexExpression(project_type, tempIndex.getIndexDetail(), index_type, report_table, null, strC[0]));/** 校验表达式 */
                        } else {
                            index.setExpress(null);
                        }

                        //expression = this.analysisIndexExpression(project_type, tempIndex.getIndexDetail(), index_type, report_table, null, strC[0]);
                    } else {
                        String [] strR = validationIndexInitiateDao.getRowOrColumnNumByName("R", project_type, tempIndex.getRowCode(), report_table);
                        if (null == strR) {
                            index.setRowNum(null);//校验字段行
                            index.setRowName(null);//校验行名称
                        } else {
                            index.setRowNum(strR[0]);//校验字段行
                            index.setRowName(strR[1]);//校验行名称
                        }

                        String [] strC = validationIndexInitiateDao.getRowOrColumnNumByName("C", project_type, tempIndex.getColumnCode(), report_table);
                        if (null == strC) {
                            index.setColumnNum(null);//校验字段列
                            index.setListName(null);//校验列名称
                            index.setExpress(null);/** 校验表达式 */
                        } else {
                            index.setColumnNum(strC[0]);//校验字段列
                            index.setListName(strC[1]);//校验列名称
                        }

                        if (null != strC && null != strR) {
                            index.setExpress(this.analysisIndexExpression(project_type, tempIndex.getIndexDetail(), index_type, report_table, strR[0], strC[0]));/** 校验表达式 */
                        } else {
                            index.setExpress(null);
                        }

                        //index.setExpress(null);/** 校验表达式 */
                        //expression = this.analysisIndexExpression(project_type, tempIndex.getIndexDetail(), index_type, report_table, strR[0], strC[0]);
                    }
                    index.setIndexType(index_type);//校验指标类型

                    index.setAllowDeviation("0.00");//允许差值默认为0
                    index.setIndexDetail(tempIndex.getIndexDetail());//校验指标详述
                    index.setCorrectPrompt(tempIndex.getCorrectPrompt());//校验正确模板
                    index.setErrorPrompt(tempIndex.getErrorPrompt());//校验异常模板
                    index.setRemark(tempIndex.getRemark());//备注
                    validationIndexInitiateDao.putIndexIn(index);//指标数据入库
                    //validationIndexInitiateDao.updateIndexExpress(tempIndex.getIndexCode() ,expression);//指标数据入库
                }

                indexModelList.clear();
                log.info("报表" + tbName + "校验指标转换任务处理结束");
            }
            log.info("————————————————————————————————————————————报表校验指标转换任务批处理完成————————————————————————————————————");
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("报表:" + hd_table + "报送指标初始数据转换异常:" + e.getMessage());
        }
    }

    /**
     * 校验指标校验表达式解析方法
     * @param project_type 校验项目类型:1-一维/2-二维
     * @param index_detail 校验指标详述
     * @param index_type 校验指标类型:rpt_validate_type 字典
     * @throws Exception
     */
    private String analysisIndexExpression (String project_type, String index_detail, String index_type, String report_table, String rowNum, String columnNum) throws Exception {
        String expression = "";/**校验表达式*/
        switch (index_type) {
            case "01" ://非空校验
                expression = "";
                break;
            case "02" ://值域校验
                if (index_detail.trim().indexOf("值域范围校验") > -1) {
                    expression = "D()";//需要根据具体字段去判断手动填充数据字典
                }
                break;
            case "03" ://字段格式及长度校验(一般为一维报表数据校验)
                try{
                    index_detail = index_detail.substring(index_detail.indexOf("]"));
                    int begin = (index_detail.lastIndexOf("(")>=1)?(index_detail.lastIndexOf("(")-1):(index_detail.lastIndexOf("（")-1);//长度结尾起始位置
                    int end = (index_detail.lastIndexOf(")")>=1)?(index_detail.lastIndexOf(")")+1):(index_detail.lastIndexOf("）")+1);//长度结尾终止位置

                    if ((index_detail.indexOf("字段格式") > -1 || index_detail.indexOf("字段长度") > -1 || index_detail.indexOf("]格式") > -1) && index_detail.indexOf("定长") == -1) {
                        expression = index_detail.substring(begin,end);
                        expression = expression.replace("（", "(").replace("）", ")");
                    } else if (index_detail.indexOf("日期格式") > -1) {
                        expression = "DATE(" + index_detail.substring(index_detail.indexOf("格式")+1+1) + ")";
                    } else if (index_detail.indexOf("定长") > -1) {
                        begin = (index_detail.lastIndexOf("(")>=1)?(index_detail.lastIndexOf("(")+1):(index_detail.lastIndexOf("（")+1);//长度结尾起始位置
                        end = (index_detail.lastIndexOf(")")>=1)?(index_detail.lastIndexOf(")")):(index_detail.lastIndexOf("）"));//长度结尾终止位置
                        expression = "CD(" + index_detail.substring(begin,end) + ")";
                    } else if (index_detail.indexOf("[") > 0 && index_detail.indexOf("当") == 0) {//带条件的格式要求
                        String condition_column = index_detail.substring(index_detail.indexOf("当"),index_detail.indexOf("为"));
                        String index = validationIndexInitiateDao.getColumnIndexByName(report_table,condition_column);
                        String value = index_detail.substring(index_detail.indexOf("为"),index_detail.indexOf("时"));
                        expression = "value("+report_table+",0,"+index+")=="+value+"?"+index_detail.substring(begin,end).replace("（", "(").replace("）", ")");
                    }
                } catch (Exception e){
                    log.info("校验指标表" + report_table + "校验指标: " + index_detail + " 解析异常,跳过");
                    expression = "";
                }
                break;
            case "04" ://文件格式及大小校验(暂无此类校验)

                break;
            case "05" ://字段联动校验

                break;
            case "06" ://数字校验
                try{
                    String leftNum = "";String rightNum = "";
                    if (index_detail.indexOf("大于等于") > -1 || index_detail.indexOf("大于") > -1) {//存在大于等于或大于
                        String left = index_detail.substring((index_detail.indexOf("大于等于")==-1)?index_detail.indexOf("大于"):index_detail.indexOf("大于等于"),
                                ((index_detail.indexOf("，")==-1)?index_detail.lastIndexOf("的"):index_detail.indexOf("，"))+1).trim();
                        int k = 1;
                        if(index_detail.indexOf("大于等于") > -1){
                            k = 3;
                        }
                        leftNum = left.substring((left.indexOf("大于等于")==-1?left.indexOf("大于"):left.indexOf("大于等于"))+1+k, (left.indexOf("，")>-1)?left.indexOf("，"):left.indexOf("的")).trim();//不确定是否存在小于的情况
                        if (index_detail.indexOf("小于等于") > -1) {//且存在小于等于
                            String right = index_detail.substring(index_detail.indexOf("小于等于")+1, index_detail.indexOf("的")+1);
                            rightNum = right.substring(right.indexOf("小于等于")+4, right.lastIndexOf("的"));;
                            expression = "R["+leftNum+","+rightNum+"]";
                        } else if (index_detail.indexOf("小于") > -1) {
                            String right = index_detail.substring(index_detail.indexOf("小于")+1, index_detail.indexOf("的")+1);
                            rightNum = right.substring(right.indexOf("小于")+2, right.lastIndexOf("的"));;
                            expression = "R["+leftNum+","+rightNum+")";
                        } else {
                            expression = "R["+leftNum+",+N)";
                        }

                        if (index_detail.indexOf("大于") > -1 && index_detail.indexOf("大于等于") == -1) {
                            expression = expression.replace("R[", "R(");
                        }
                    } else {
                        if (index_detail.indexOf("小于等于") > -1) {//且存在小于等于
                            String right = index_detail.substring(index_detail.indexOf("小于等于")+1, index_detail.indexOf("的")+1);
                            rightNum = right.substring(right.indexOf("小于等于")+4, right.lastIndexOf("的"));;
                            expression = "R(-N,"+rightNum+"]";
                        } else if (index_detail.indexOf("小于") > -1) {
                            String right = index_detail.substring(index_detail.indexOf("小于")+1, index_detail.indexOf("的")+1);
                            rightNum = right.substring(right.indexOf("小于")+2, right.lastIndexOf("的"));;
                            expression = "R（-N,"+rightNum+")";
                        } else {
                            expression = "";
                        }
                    }
                } catch (Exception e){
                    log.info("校验指标表" + report_table + "校验指标: " + index_detail + " 解析异常,跳过");
                    expression = "";
                }
                break;
            case "07" ://重复性校验

                break;
            case "08" ://身份证校验

                break;
            case "09" ://计算校验
                try{
                    if ("2".equals(project_type)) {//二维报表
                        List<String> str_expList = new ArrayList<>();//计算表达式
                        List<String> str_symbolList = new ArrayList<>();//计算符号

                        Pattern pattern = Pattern.compile("\\[[\\u4e00-\\u9fa5|0-9|a-z|A-Z|\\（|\\）|\\(|\\)]*\\]");
                        Pattern pattern_symbol = Pattern.compile("/[%]|[#]|\\=|[/*]|-|[/]|[+]|==|>=|<=|[≤]|[≥]|>|</");

                        Matcher match = pattern.matcher(index_detail);
                        Matcher match_symbol = pattern_symbol.matcher(index_detail);

                        while (match.find()) {
                            String contents = match.group().substring(match.group().indexOf("["));//获取value方法入参参数
                            str_expList.add(contents);
                        }
                        while (match_symbol.find()) {
                            String contents = match_symbol.group();//计算符号
                            str_symbolList.add(contents);
                        }
                        log.info("计算表达式数组为:"+ str_expList);
                        log.info("计算符号数组为:"+ str_symbolList);

                        if (index_detail.trim().contains("行[") || !index_detail.trim().contains("列[")) {//该情况为列确定校验行之间的数据关系
                            String column_index = columnNum;
                            //对表计算表达式进行逐项解析替换
                            for (int i=0; i<str_expList.size(); i++) {//格式为[（xxxx）name]
                                String field_name = str_expList.get(i);
                                field_name = field_name.substring((field_name.indexOf("）")==-1)?0:field_name.indexOf("）")+1, field_name.indexOf("]"));
                                //获取行index
                                String index_row = validationIndexInitiateDao.getRowIndexByName(report_table, field_name, "R");
                                String exp = "value(" + report_table + "," + index_row + "," + column_index + ")";
                                str_expList.set(i, exp);
                            }

                            for (int j=0; j<str_expList.size(); j++) {
                                if (j==str_expList.size()-1){
                                    expression = expression + str_expList.get(j);
                                } else {
                                    expression = expression + str_expList.get(j) + str_symbolList.get(j);
                                }
                            }

                        } else if (index_detail.trim().contains("列[") && !index_detail.trim().contains("行[")) {//该情况为行确定校验列之间的数据关系
                            String row_index = rowNum;
                            //对表计算表达式进行逐项解析替换
                            for (int i=0; i<str_expList.size(); i++) {//格式为[name]
                                String field_name = str_expList.get(i);
                                String column_code = index_detail.substring(index_detail.indexOf(field_name)-2, index_detail.indexOf(field_name));//格式为X列或列X
                                column_code = column_code.replace("列","");

                                //获取行index
                                String index_column = validationIndexInitiateDao.getColumnIndexByCode(report_table, column_code, "C");
                                String exp = "value(" + report_table + "," + row_index + "," + index_column + ")";
                                str_expList.set(i, exp);
                            }

                            for (int j=0; j<str_expList.size(); j++) {
                                if (j==str_expList.size()-1){
                                    expression = expression + str_expList.get(j);
                                } else {
                                    expression = expression + str_expList.get(j) + str_symbolList.get(j);
                                }
                            }
                        } else {

                        }
                    } else {//一维报表

                    }
                } catch ( Exception e) {
                    log.info("校验指标表" + report_table + "校验指标: " + index_detail + " 解析异常,跳过");
                    expression = "";
                }
                break;
            case "10" ://表间校验

                break;
            default:

                break;
        }

        return expression;
    }


    public static void main(String[] args) {
        List<String> str_exp = new ArrayList<>();
        List<String> str_symbol = new ArrayList<>();
        String index_detail = "行[(406000)辽宁]≥[(406001)大连]";

        Pattern pattern = Pattern.compile("\\[[\\u4e00-\\u9fa5|0-9|a-z|A-Z|\\（|\\）|\\(|\\)]*\\]");
        Matcher match = pattern.matcher(index_detail);
        while (match.find()) {
            String contents = "";
            if (match.group().indexOf("[") > -1) {
                contents = match.group().substring(match.group().indexOf("["));//计算表达式
            }else {
                contents =  match.group();//计算符号
            }
            str_exp.add(contents);
        }
        System.out.println(str_exp);

        Pattern pattern_symbol = Pattern.compile("/[%]|[#]|\\=|[/*]|-|[/]|[+]|==|>=|<=|[≤]|[≥]|>|</");
        Matcher match_symbol = pattern_symbol.matcher(index_detail);
        while (match_symbol.find()) {
            String contents = match_symbol.group();//计算符号
            str_symbol.add(contents);
        }
        System.out.println(str_symbol);
    }

}
