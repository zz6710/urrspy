package com.kayak.utils;

import com.kayak.context.ApplicationContextHolder;
import com.kayak.rpt.zz.manage.util.CheckDataUtils;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.factorys.FileTransferFactory;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTransferHelpler {

    static FileTransferConfig fileTransferConfig;

    public static FileTransfer getTransfer() throws Exception{
        fileTransferConfig =  ApplicationContextHolder.getBean(FileTransferConfig.class);
        return  FileTransferFactory.createFileTransfer(fileTransferConfig);
    }

    public static FileTransfer getTransfer(FileTransferConfig fileTransferConfig) throws Exception {
        return  FileTransferFactory.createFileTransfer(fileTransferConfig);
    }

    public static void main(String[] args) {
//        String am = "86451166.00";
//        Pattern p=Pattern.compile("^(\\d{1,13}(\\.\\d{1,2})?)");
//        Matcher m=p.matcher(am);
//        System.out.println(m.matches());
//        boolean a = CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd");
//        System.out.println(a);
//
//        String err = CheckDataUtils.checkMoney("86451166.00","本机构管理费（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1");
//        System.out.println("err："+err);
//        String getPpInvestAssets = "-80%-100%:固定收益类资产占比";
//        if(StringUtils.isNotBlank(getPpInvestAssets)){
//            //Pattern p1 = Pattern.compile("^(100(\\.00)?%:[^%:;\\-\\d]+(;100(\\.00)?%:[^%:;\\-\\d]+)*|(\\d{1,2}(\\.\\d{1,2})?)%:[^%:;\\-\\d]+(;(\\d{1,2}(\\.\\d{1,2})?)%-((\\d{1,2}(\\.\\d{1,2})?)|100(\\.00)?)%:[^%:;\\-\\d]+)*;?)$");
//            String regex = "^(100(\\.00)?%:[^%:;\\-\\d]+(;100(\\.00)?%:[^%:;\\-\\d]+)*|" +
//                    "(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
//                    "[^%:;\\-\\d]+(;(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
//                    "[^%:;\\-\\d]+)*;?)$";
//            Pattern p1 = Pattern.compile(regex);
//            Matcher m1=p1.matcher(getPpInvestAssets);
//            if(m1.matches()){
//                String re = "基金投资资产要素格式不对正确的格式：数字%：文字；数字%-数字%：文字（其中，百分号、冒号、分号、连字号均应为英文标点，文字和数字部分均不得含有英文百分号、冒号、分号、连字号）其中，数字格式为n..（5，2），且需大于等于0、小于等于100。\n";
//                System.out.println("err1："+re);
//            }
//        }
//        String actualSubscribedAmt = "864521166.00";
//        String amtOtherDbAgents = "8645166";
//        System.out.println(CheckDataUtils.compareTo(amtOtherDbAgents,actualSubscribedAmt));
//
//        System.out.println(new BigDecimal(amtOtherDbAgents).compareTo(new BigDecimal(actualSubscribedAmt)));
//        String targetpath = "D:\\idrs\\urrs_new\\2301249511.zip";
//        targetpath = targetpath.substring(0,targetpath.lastIndexOf("."));
//        System.out.println(targetpath);
//        System.out.println(isValid("01- 大叔大婶大所大所大撒大所多.Docx"));


//        double value = 1.0;
//        BigDecimal bd = new BigDecimal(Double.toString(value));
//        if (bd.stripTrailingZeros().scale() <= 0) {
//            System.out.println("值为整数: " + bd.intValue());
//        } else {
//            System.out.println("值为小数: " + value);
//        }
        String zonClcAmt = "CNY,100.01,100.01;";
        String[] ss = zonClcAmt.split(",");
        List<String> list = Arrays.asList(ss);
        for(String s:list){
            System.out.println(s);
        }

    }
    public static boolean isValid(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex <= 0 || lastDotIndex == fileName.length() - 1) {
            return false; // 无扩展名或扩展名为空
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
        boolean isValid = extension.matches("(doc|docx|xls|xlsx|zip|pdf|jpg|jpeg|bmp|png|gif|gd|ppt|pptx)");

        // 调试输出
        System.out.println("文件名: " + fileName);
        System.out.println("扩展名: " + extension);
        System.out.println("是否合法: " + isValid);

        return isValid;
    }
}

