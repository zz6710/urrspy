package com.kayak.pms.T81.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.T81.dao.T8ProdInfoDao;
import com.kayak.pms.T81.model.T8ProdInfo;
import com.kayak.pms.T8ProdDeal.dao.T8TruteeDao;
import com.kayak.pms.prod.dao.DocumentAttachmentDao;
import com.kayak.pms.prod.dao.ProdScheduleDao;
import com.kayak.pms.prod.dao.T8ProdScheduleDao;
import com.kayak.pms.prod.enums.ScheduleNode;
import com.kayak.pms.prod.model.ProdSchedule;
import com.kayak.pms.prod.model.T8ProdSchedule;
import com.kayak.pms.prod.model.T8ProdSync;
import com.kayak.pms.prod.service.T8ProdSyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

@Slf4j
@Service
@APIDefine(desc = "产品基础信息服务", model = T8ProdInfo.class)
public class T8ProdInfoService {

    @Autowired
    private T8ProdInfoDao t8ProdInfoDao;

    @Autowired
    private T8TruteeDao t8TruteeDao;

    @Autowired
    private ProdScheduleDao prodScheduleDao;

    @Autowired
    private DocumentAttachmentDao attachmentDao;

    @Autowired
    protected DaoService daoService;

    @Autowired
    private T8ProdScheduleDao t8ProdScheduleDao;

    @Autowired
    private T8ProdSyncService t8ProdSyncService;

    @API(desc = "导出产品数据定时任务", auth = APIAuth.NO)
    public String exportProdDataSchedule(SqlParam<T8ProdInfo> params) throws Exception {
        /*SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        //获取前一天
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,-1);
        String lastDay = format.format(c.getTime());*/
        //系统当前日期
        String date = DateUtil.getNowDate();

        //存放路径
        String basePath = SysUtil.getSystemParamsByParaid("80000080108") + date;

        File baseFile = new File(basePath);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        List<T8ProdSync> tabaleNameList = t8ProdSyncService.findTableName();
        //List<String> tabaleNameList = Stream.of("t8_prod_info", "t8_fee_prod", "t8_prod_series").collect(Collectors.toList());
        //tar包名字
        String tarName = "ewdsp_data_ewpms_" + date + "_000.tar";
        //tar包路径
        File tarFile = new File(basePath + File.separator + tarName);
        //临时目录 压缩完tar就删除
        File temp = new File(basePath + File.separator + "temp");
        if (!temp.exists()) {
            temp.mkdirs();
        }
        if (CollectionUtil.isNotEmpty(tabaleNameList)) {
            BufferedWriter bf = null;
            GZIPOutputStream gz = null;
            FileInputStream in = null;
            TarArchiveOutputStream tar = null;
            FileOutputStream fileOutputStream = null;
            BufferedOutputStream bufferedWriter = null;
            try {
                for (T8ProdSync t8ProdSync : tabaleNameList) {
                    //获取数据
                    //List<SqlRow> dataList = t8ProdInfoDao.findDataByTableName(t8ProdSync,lastDay);
                    List<SqlRow> dataList = t8ProdInfoDao.findDataByTableName(t8ProdSync);
                    if (CollectionUtil.isNotEmpty(dataList)) {
                        File file = new File(basePath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        //创建文件
                        String fileName = "a_ewpms_" + t8ProdSync.getTableName() + "_" + date + ".dat";
                        //文件路径
                        String filePath = basePath + File.separator + fileName;
                        bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "gb18030"));
                        File files = new File(filePath);
                        if (!files.exists()) {
                            files.createNewFile();
                        }
                        for (SqlRow sqlRow : dataList) {
                            String outdata = "";
                            for (String key : sqlRow.keySet()) {
                                //outdata += "|+|";
                                if (sqlRow.get(key) == null) {
                                    outdata += "";
                                } else {
                                    outdata += sqlRow.get(key);
                                }
                                outdata += "|+|";
                            }
                            bf.write(outdata);
                            bf.newLine();
                            bf.flush();
                        }
                        String gzName = basePath + File.separator + fileName + ".gz";
                        //gz压缩
                        in = gzip(in, files, gzName);

                        //文件复制
                        // FileUtils.copyFile(files, new File(temp + File.separator + fileName));
                        //gz复制
                        FileUtils.copyFile(new File(gzName), new File(temp + File.separator + fileName + ".gz"));
                        //清理文件
                        FileUtil.del(files);
                        FileUtil.del(gzName);


                    }

                }
                if (temp.listFiles().length > 0) {
                    //文件里面复制进了数据 进行压缩 tar包
                    fileOutputStream = new FileOutputStream(tarFile);
                    bufferedWriter = new BufferedOutputStream(fileOutputStream);
                    tar = new TarArchiveOutputStream(bufferedWriter);
                    tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
                    for (File file : temp.listFiles()) {
                        addTarArchiveEntryToTarArchiveOutputStream(file, tar, "");
                    }

                    String okFile = "ewdsp_data_ewpms_" + date + "_000.ok";
                    File file = new File(basePath + File.separator + okFile);
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                }

                tar.finish();
                tar.close();
                bufferedWriter.close();
                fileOutputStream.close();
                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (temp.exists()) {
                    FileUtils.deleteDirectory(temp);
                }
                fileOutputStream.close();
                bufferedWriter.close();
                bf.close();
                tar.close();
            }
        }
        return RequestSupport.updateReturnJson(true, "导出成功", null).toString();
    }

    private FileInputStream gzip(FileInputStream in, File files, String gzName) throws IOException {
        GZIPOutputStream gz;
        gz = new GZIPOutputStream(new FileOutputStream(gzName));
        FileInputStream ins = null;
        try {
            in = new FileInputStream(files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            gz.write(buf, 0, len);
        }
        if (ins != null) {
            ins.close();
        }
        gz.finish();
        if (gz != null) {
            gz.close();
        }
        return in;
    }


    private static void addTarArchiveEntryToTarArchiveOutputStream(File file, TarArchiveOutputStream tar, String prefix) throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(file, prefix + File.separator + file.getName());

        if (file.isFile()) {
            entry.setSize(file.length());
            tar.putArchiveEntry(entry);
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 BufferedInputStream input = new BufferedInputStream(fileInputStream);) {
                IOUtils.copy(input, tar);
            }
            tar.closeArchiveEntry();
        } else {
            tar.putArchiveEntry(entry);
            tar.closeArchiveEntry();
            prefix += File.separator + file.getName();
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    addTarArchiveEntryToTarArchiveOutputStream(f, tar, prefix);
                }
            }
        }
    }


    @API(desc = "查询产品基础信息信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> findT8ProdInfos(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(true);
        return t8ProdInfoDao.findT8ProdInfos(params);
    }

    @API(desc = "查询其它材料报备确认信息,可行性文件", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public List<T8ProdInfo> findOhterFilingT8ProdInfos2(String prodCoodoe) throws Exception {
        return t8ProdInfoDao.findOtherFilingT8ProdInfos2(prodCoodoe);
    }

    @API(desc = "查询产品是否存在", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> isExistsProdCount(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.isExistsProdCount(params);
    }


    @API(desc = "查询产品子系列是否上会通过", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> isExistsSeries(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.isExistsSeries(params);
    }

    @API(desc = "查询产品参数是否录入完", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> isProdParamsCount(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.isProdParamsCount(params);
    }


    @API(desc = "查询产品是否存在", operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<T8ProdInfo> isExistsProdCountUpt(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.isExistsProdCountUpt(params);
    }

    /**
     * 仅做产品克隆权限使用
     *
     * @param params
     * @return
     */
    @API(desc = "产品克隆", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String prodCopy(SqlParam<T8ProdInfo> params) {
        return null;
    }

    /**
     * 仅做产品创设进度权限使用
     *
     * @param params
     * @return
     */
    @API(desc = "创设进度", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String prodCreationProgress(SqlParam<T8ProdInfo> params) {
        return null;
    }

    @API(desc = "删除附件", auth = APIAuth.NO, operation = APIOperation.DELETE)
    public String deleteFile(SqlParam<T8ProdInfo> param) throws Exception {
        //此方法只用作于权限控制
        deleteFileIsExist(param.getModel().getPath());
        DaoUtil.doTrans(() -> {
            attachmentDao.delete(param.getModel().getId());
        });
        return RequestSupport.updateReturnJson(true, "附件删除成功", null).toString();
    }

    private Boolean deleteFileIsExist(String filePath) {
        boolean delete = false;
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            delete = file.delete();
        }
        return delete;
    }

//    @API(desc = "修改产品基础信息",  auth = APIAuth.YES,operation = APIOperation.UPDATE)
//    public int updateT8ProdInfo(SqlParam<T8ProdInfo> params) throws Exception {
//        params.getModel().setUpdUser(SysUtil.getSysUserParamValue("sys_user_userid").toString());
//        params.getModel().setUpdTime(DateHelper.getCurrentTime());
//        params.getModel().setUpdDate(DateHelper.getCurrentDate());
//        return t8ProdInfoDao.updateT8ProdInfo(params.getModel());
//    }
//
//    @API(desc = "删除产品基础信息", auth = APIAuth.YES,operation = APIOperation.DELETE)
//    public int deleteT8ProdInfo(SqlParam<T8ProdInfo> params) throws Exception {
//        return t8ProdInfoDao.deleteT8ProdInfo(params).getEffect();
//    }

    @API(desc = "创意下拉菜单", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getOriginality(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        return t8ProdInfoDao.getOriginality(params);
    }

    @API(desc = "系列下拉菜单", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdSeries(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        return t8ProdInfoDao.getProdSeries(params);
    }
    @API(desc = "系列下拉菜单", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getNewProdSeries(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        return t8ProdInfoDao.getNewProdSeries(params);
    }

    @API(desc = "子系列下拉菜单", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdSonSeries(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        return t8ProdInfoDao.getProdSonSeries(params);
    }

    //申报登记
    @API(desc = "通过产品代码与产品名称模糊查询产品信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> findProdInfoByLike(Map<String, Object> params) throws Exception {
        SqlResult<Map<String, Object>> prodInfoByLike = t8ProdInfoDao.findProdInfoByLike(params);
        return prodInfoByLike;
    }

    @API(desc = "根据产品Id获取产品信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdInfoByProdCode(SqlParam<T8ProdInfo> params) throws Exception {
        T8ProdInfo prodInfoById = t8ProdInfoDao.getProdInfoByProdCode(params.getModel().getProdCode());
        List<T8ProdInfo> t8ProdInfos = Collections.singletonList(prodInfoById);
        SqlResult<T8ProdInfo> sqlResult = new SqlResult<>();
        sqlResult.setResults(t8ProdInfos.size());
        sqlResult.setRows(t8ProdInfos);
        sqlResult.setDesensitized(false);
        return sqlResult;
    }

    @API(desc = "根据产品代码获取产品信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdInfoByCode(SqlParam<T8ProdInfo> params) throws Exception {
        T8ProdInfo prodInfoById = t8ProdInfoDao.getProdInfoByCode(params.getModel().getProdCode());
        List<T8ProdInfo> t8ProdInfos = Collections.singletonList(prodInfoById);
        SqlResult<T8ProdInfo> sqlResult = new SqlResult<>();
        sqlResult.setResults(t8ProdInfos.size());
        sqlResult.setRows(t8ProdInfos);
        sqlResult.setDesensitized(false);
        return sqlResult;
    }

    /**
     * 功能：根据产品id与节点新增或者修改产品进度表中信息
     * 作者：rennannan
     * 日期：20210315
     *
     * @param schedule
     * @return
     */
    public int addOrUpdateSchedule(ProdSchedule schedule) throws Exception {
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        schedule.setCrtDate(date);//创建日期
        schedule.setCrtTime(time);//创建时间
        schedule.setCrtUser(userid);//创建人
        schedule.setUpdDate(date);//修改日期
        schedule.setUpdTime(time);//修改时间
        schedule.setUpdUser(userid);//修改人
        int count = 0;
        int countSche = this.prodScheduleDao.findProdScheduleCount(schedule);
        if (countSche > 0) { //存在则更新
            count = this.prodScheduleDao.updateProdScheduleByInfoId(schedule);
        } else {//不存在插入
            count = this.prodScheduleDao.insertProdSchedule(schedule);
        }
        return count;
    }


    @API(desc = "根据产品代码查询产品名称", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getProdNameByProdCode(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.getProdNameByProdCode(params);
    }

    @API(desc = "通过产品代码或产品名称获取产品列表", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> findProdListByProdCodeOrProdName(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.findProdListByProdCodeOrProdName(params);
    }

    @API(desc = "通过产品代码或产品名称获取产品托管协议和代销协议列表", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> findProdListByProdCodeOrProdName2(SqlParam<T8ProdInfo> params) throws Exception {

        return t8ProdInfoDao.findProdListByProdCodeOrProdName2(params);
    }

    @API(desc = "查询托管协议文档", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> findEscrowAgreementByProdName(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.findEscrowAgreementByProdName(params);
    }

    @API(desc = "查询产品信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> findProdInfoByByCodeAndDate(SqlParam<Map<String, Object>> params) throws Exception {
        return t8ProdInfoDao.findProdInfoByByCodeAndDate(params);
    }

    @API(desc = "额度决策会下拉菜单", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdInfos(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        return t8ProdInfoDao.getProdInfos(params);
    }
    @API(desc = "资管同步产品", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdInfosZG(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        return t8ProdInfoDao.getProdInfosZG(params);
    }
    @API(desc = "产品id-产品代码", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdInfosAndCode(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        return t8ProdInfoDao.getProdInfosAndCode(params);
    }

    @API(desc = "查询产品信息排除已经生成产品规则的产品", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getIdExcludeOtherProdRuleId(SqlParam<T8ProdInfo> params) throws Exception {
        Map<String, Object> parameters = RequestSupport.getParameters();
        return t8ProdInfoDao.getIdExcludeOtherProdRuleId(parameters);
    }


    @API(desc = "查询产品信息进度状态", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getProdProgressRecord(SqlParam<T8ProdInfo> params) throws Exception {
        //查询产品组件关联关系
        List<SqlRow> assemblyList = t8ProdInfoDao.getProd_assembly_info(params);
        List<SqlRow> resultList = new ArrayList<>();
        //循环关联关系
        for (SqlRow sr : assemblyList) {
//            if (StringUtils.isNotEmpty(params.getModel().getIsShareSort())) {
//                if (params.getModel().getIsShareSort().equals("1")) {
//                    if (sr.getString("assembly_id").equals("limitInfo")) {
//                        continue;
//                    }
//                }
//            }
            String crtDate = sr.getString("crt_date");
            String crtTime = sr.getString("crt_time");
            if (crtDate != null && !"".equals(crtDate)) {
                LocalDate localCrtDate = LocalDate.parse(crtDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
                sr.put("crt_date", localCrtDate.toString());
            }
            if (crtTime != null && !"".equals(crtTime)) {
                LocalTime localCrtTime = LocalTime.parse(crtTime, DateTimeFormatter.ofPattern("HHmmss"));
                sr.put("crt_time", localCrtTime.toString());
            }
            //state(1:产品没有关联该组件（前台需要置灰），2：没有数据（还没有填写），3：有数据（已经填了）)
            //如果产品代码为空则表示没有关联该组件
            if (sr.get("prod_code") == null || sr.getString("prod_code").equals("")) {
                sr.put("state", "1");
            } else {
                //有关联组件但是没有数据
                if (sr.get("crt_user") == null || sr.getString("crt_user").equals("")) {
                    sr.put("state", "2");
                } else {
                    //有数据
                    sr.put("state", "3");
                }
            }
            resultList.add(sr);
        }
        return SqlResult.build(resultList);
    }

    @API(desc = "查询产品分红方式", auth = APIAuth.NO)
    public SqlResult<Map<String, Object>> findProdDividendInfo(SqlParam<T8ProdInfo> params) throws Exception {
        return t8ProdInfoDao.findProdDividendInfo(params);
    }

    public String getDictVal(String dict, String itemKey) throws Exception {
        List<SqlRow> sqlRows = t8ProdInfoDao.findDictItemOnly(dict, itemKey);
        StringBuffer sbf = new StringBuffer();
        sqlRows.forEach(param -> {
            sbf.append(param.getString("itemval") + " ");
        });
        return sbf.toString();
    }

    //记录表

//    @API(desc = "查询产品记录信息", auth = APIAuth.YES)
//    public SqlResult<T8ProdInfo> findT8ProdInfoHiss(SqlParam<T8ProdInfo> params) throws Exception {
//        params.setMakeSql(true);
//        return t8ProdInfoDao.findT8ProdInfoHiss(params);
//    }
//
//    @API(desc = "添加产品记录信息", params = "id,prod_mode,prod_mode_id,prod_series,prod_code,prod_name,prod_brand,regist_code,is_originality,originality_id,prod_risk_level,prod_cur,netprice,raise_type,income_type,prod_classify,manager_code,publish_explain,prod_status,filing_status,filing_materials_status,risk_score,risk_score_status,prod_son_status,prod_doc_mods,distributor_code,approval_status,t8_prod_account_info_id,bnote_remit_flag,prod_trait,invest_direction,other_risk,crt_date,crt_time,crt_user", auth = APIAuth.NO)
//    public int addT8ProdInfoHis(SqlParam<T8ProdInfo> params) throws Exception {
//        return t8ProdInfoDao.addT8ProdInfoHis(params.getModel()).getEffect();
//    }

    public String getDateType(String term, char termType) {

        int days = Integer.parseInt(term);
        String type = "按日";

        switch (termType) {
            case '1':

            case '2':
                type = "按日";
                break;
            case '3':
                if (1 <= days && days < 3)
                    type = "按月";
                if (3 <= days && days < 12)
                    type = "按季";
                if (3 < days)
                    type = "按年";
                break;
            default:
                break;

        }

        return type;
    }


    ///权限需要迁移的代码


    @API(desc = "修改产品平均", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public String updateRiskScore(SqlParam<T8ProdInfo> param) throws Exception {
        int count = t8ProdInfoDao.updateRiskScore(param);
        String msg = count > 0 ? "修改成功" : "修改失败";
        return RequestSupport.updateReturnJson(true, msg, null).toString();
    }

    @API(desc = "修改产品报备状态", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String updateFilingStatus(SqlParam<T8ProdInfo> params) throws Exception {
        //将确认时间与操作人记入产品进度表中 rennannan 20210315
        ProdSchedule schedule = new ProdSchedule();
        schedule.setProdCode(params.getModel().getProdCode()); //产品代码
        schedule.setT8ProdInfoId(params.getModel().getId());//产品id
        schedule.setNodeId(ScheduleNode.register.getKey());//节点  报备确认
        schedule.setBusinessDate(params.getModel().getApplyRegistDate());//业务日期
        AtomicInteger flag = new AtomicInteger();

        DaoUtil.doTrans(() -> {
            T8ProdInfo t8ProdInfo = params.getModel();
            t8ProdInfo.setProdSonStatus("7");
            t8ProdInfo.setProdStatus("3");
            int count = t8ProdInfoDao.updateStatusAndDate(t8ProdInfo);
            /*add by 张昌思 20210426 改产品排期状态*/
            T8ProdSchedule t8ProdSchedule = new T8ProdSchedule();
            t8ProdSchedule.setProdCode(params.getModel().getProdCode());
            t8ProdSchedule.setCurrentProgress(params.getModel().getProdSonStatus());
            t8ProdScheduleDao.updateScheduleSonStatus(t8ProdSchedule);
            count += this.addOrUpdateSchedule(schedule);
            flag.set(count);
        });
        Boolean result = flag.get() == 2;
        return RequestSupport.updateReturnJson(result, result ? "操作成功" : "操作失败", null).toString();
    }

    //rennannan 20210129
    @API(desc = "产品状态调整", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String updateT8ProdInfoStatus(SqlParam<T8ProdInfo> params) throws Exception {
        params.setMakeSql(false);
        AtomicInteger count = new AtomicInteger();
        DaoUtil.doTrans(() -> {
            int num = t8ProdInfoDao.updateT8ProdInfoStatus(params.getModel());
            count.set(num);
            /*add by 张昌思 20210423 改产品排期状态*/
            T8ProdSchedule t8ProdSchedule = new T8ProdSchedule();
            t8ProdSchedule.setProdCode(params.getModel().getProdCode());
            t8ProdSchedule.setCurrentProgress(params.getModel().getProdSonStatus());
            t8ProdScheduleDao.updateScheduleSonStatus(t8ProdSchedule);
        });
        if (count.get() < 0) {
            return RequestSupport.updateReturnJson(false, "操作失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    //zls 20210303
    @API(desc = "其它报备材料确认状态修改", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String updateT8ProdInfoStatusForOther(SqlParam<T8ProdInfo> params) throws Exception {
        int count = t8ProdInfoDao.updateT8ProdInfoStatusForOther(params.getModel());
        if (count < 0) {
            return RequestSupport.updateReturnJson(false, "操作失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();

    }

    /**
     * 功能：查询流程是否开启
     * 作者：rennannan
     * 日期：20210331
     *
     * @return
     */
    @API(desc = "查询流程是否开启", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public String getProdProcessOpen(SqlParam<T8ProdInfo> params) throws Exception {
        int count = this.t8ProdInfoDao.getProdProcessOpen(params);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", count);
        return RequestSupport.updateReturnJson(true, "操作成功", map).toString();
    }

    /**
     * 功能：渠道规则查询未被选中过的产品
     * 作者：rennannan
     * 日期：20210611
     *
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "渠道规则查询产品", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdInfo> getProdByExistsProdIds(SqlParam<T8ProdInfo> params) throws Exception {
        Map<String, Object> parameters = RequestSupport.getParameters();
        return t8ProdInfoDao.getProdByExistsProdIds(parameters);
    }

}
