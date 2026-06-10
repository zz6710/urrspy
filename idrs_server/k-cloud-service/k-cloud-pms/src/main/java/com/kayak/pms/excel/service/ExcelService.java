package com.kayak.pms.excel.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.excel.dao.ExcelDao;
import com.kayak.pms.excel.model.Excel;
import com.kayak.pms.excel.model.TradeField;
import com.kayak.utils.ExcelUtils;
import com.kayak.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * exl文件导入服务
 * axin
 * 说明：
 * setSharding() 设置保存数据的数据库，默认为0-公共数据库 ,需要调用其他数据库可重写此方法
 * setId() 设置sys_exlimp表配置ID。必须重写此方法
 * saveData() 保存数据方法，必须重写此方法。
 * saveParseData() 如果sys_exlimp配置了表名，在重写saveData()时可直接调用saveParseData()保存方法
 *
 * 实现方式一：静态调用
 *List<Map<String,Object>> l = ExcelUtils.parseExcel(file, new ExcelService() {
 *
 *   @Override
 *   public Integer setId() {
 *     return 2;
 *   }
 *
 *    @Override
 *    public void saveData() throws Exception {
 *    //如果有其他业务逻辑，可自己写保存方法
 *       DaoUtil.doTrans(super::saveParseData);
 *    }
 *
 * });
 *
 * 实现方式二：继承  extends ExcelService
 *  同样重写方式一的方法即可
 *
 */

@Service
@Slf4j
@APIDefine(desc = "Excel服務", model = Excel.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class ExcelService {

    private final ExcelDao excelDao = SysBeans.getBean("excelDao");
    //数据库默认为公共数据库
    private int sharding;

    //文件
    private MultipartFile file;
    //主配置ID
    private Integer id;

    //exl主配置
    private Excel excel ;
    //exl字段
    private List<TradeField> tradeFields ;
    private String fileName;
    //文件存储路径
    private String saveFile;
    //解析结果
    private List<Map<String,Object>> l;

    //代码块执行部分
    static {

    }


    public List<Map<String,Object>> readExcel (MultipartFile file) throws Exception {

        try {
            init(file);
            //文件验证
            checkFile();
            //初始化数据
            findConfig();
            //转存文件
            fileSaveToLocal();
            //文件解析
            readExcel();
            //存储数据
            saveData();
        }catch (Exception e){

            throw new Exception(e.getMessage());

        }finally {

            if(StringUtils.isNotBlank(saveFile))
                saveFileData();

        }

        return l ;

    }

    private void init(MultipartFile file){
        //file
        this.file = file;
        //ID
        this.id = setId();

        this.sharding = setSharding();

        this.fileName = file.getOriginalFilename();
    }

    /**
     * 文件验证
     * @throws Exception
     */
    private void checkFile () throws Exception {
        //空文件验证
        if (file == null) {
            throw new Exception("上传文件为空");
        }

        if (id == null) {
            throw new Exception("未设置Excel[id]");
        }

        //文件类型验证
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            throw new Exception("上传文件不为Excel");
        }
    }

    private void fileSaveToLocal () throws Exception {

        String filePath = FileUtil.getFileStorePath() + FileUtil.IMPORT +
                File.separator + excel.getModName() + File.separator + DateUtil.getTimestamp14();

        saveFile = FileUtil.fileSaveToLocal(file, fileName, filePath);

    }


    private void readExcel() throws Exception {
        l = ExcelUtils.readExcel(excel, tradeFields, new File(saveFile)) ;
    }

    private void findConfig() throws Exception {
        excel = excelDao.findExcelId(id).get(0);
        tradeFields = excelDao.findTradeFieldId(id);
    }

    //保存数据
    public void saveParseData() throws Exception {

        if (StringUtils.isBlank(excel.getTableName()))
            return;

        String id = "";
        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();

        for (TradeField t : tradeFields) {
            if (t.getFieldLabel().equalsIgnoreCase("id")) {
                id = t.getFieldLabel();
            }else{
                key.append(t.getFieldLabel()).append(",");
                val.append("$S{").append(t.getFieldLabel()).append("},");
            }
        }

        key = new StringBuilder(key.substring(0, key.length() - 1));
        val = new StringBuilder(val.substring(0, val.length() - 1));

        if (StringUtils.isNotBlank(id)){
            key.append(",id");
            val.append(",$AUTOIDS{id} ");
        }

        String sql = "insert into " + excel.getTableName() + " (" + key + ") values ( " + val + ")";

        excelDao.saveParseData(sql ,sharding, l);

    }


    //存储文件
    public void saveFileData() {

        Map<String ,Object> map = new HashMap<>();
        map.put("sysExlimpId",id);
        map.put("filePath",saveFile);
        map.put("fileName",fileName);
        map.put("crtDate", DateUtil.getNowDate());

        try {
            excelDao.saveFileData(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //设置数据库
    public int setSharding (){ return DataSourceProperty.PUB ;}
    /**
     * 设置excel主配置ID
     * @return
     */
    public abstract Integer setId ();

    /**
     * 保存数据 ：解析数据
     * @throws Exception
     */
    public abstract void saveData() throws Exception ;


}
