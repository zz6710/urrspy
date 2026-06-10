package com.kayak.clear.constants;

/**
 * 批量清算任务类型
 *
 * @author mosy [mosy@kayak.com.cn]
 * @Description 清算类型
 **/
public class BatchTaskType {

    /** 系统清算*/
    public static final String SYSTEM = "1";
    /** 产品清算 */
    public static final String PRODUCT = "2";
    /** 销售商文件导入 **/
    public static final String DISTRIBUTOR_FILE_IMP = "3";
    /** 销售商文件导出 **/
    public static final String DISTRIBUTOR_FILE_EXP = "4";
    /** 资管 文件导入 **/
    public static final String ZG_FILE_IMP = "5";
    /** 资管 文件导出 **/
    public static final String ZG_FILE_EXP = "6";
    /** 数据归档 表数据归档 **/
    public static final String DATA_TO_HIS = "7";
    /** 实时清算 **/
    public static final String ACTUAL_TIME = "9";


    /**
     * 判断是系统清算
     *
     * @param taskType
     * @return
     */
    public static boolean isSystem(String taskType) {
        return SYSTEM.equals(taskType);
    }

    /**
     * 判断是产品清算
     *
     * @param taskType
     * @return
     */
    public static boolean isProduct(String taskType) {
        return PRODUCT.equals(taskType);
    }

    /**
     * 判断是文件导入清算
     *
     * @param taskType
     * @return
     */
    public static boolean isFile(String taskType) {
        return DISTRIBUTOR_FILE_IMP.equals(taskType) || DISTRIBUTOR_FILE_EXP.equals(taskType) || ZG_FILE_IMP.equals(taskType) || ZG_FILE_EXP.equals(taskType);
    }
    
    
    /**
     * 判断是否销售商清算
     */
    public static boolean isDistributor(String taskType){
    	 return DISTRIBUTOR_FILE_IMP.equals(taskType) || DISTRIBUTOR_FILE_EXP.equals(taskType);
    }

    public static boolean isDataToHis(String taskType) {
        return DATA_TO_HIS.equals(taskType);
    }
}
