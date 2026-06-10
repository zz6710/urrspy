package com.kayak.pms.basePublish.service;

import com.alibaba.fastjson.JSON;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureModVersionDao;
import com.kayak.pms.basePublish.dao.DisclosureWordDateDao;
import com.kayak.pms.basePublish.model.DisclosureWordDate;
import com.kayak.pms.connect.utils.FileUtil;
import com.kayak.pms.disclosureControl.dao.*;
import com.kayak.pms.disclosureControl.model.*;
import com.kayak.pms.disclosureControl.service.DisclosureNoticeChannelService;
import com.kayak.pms.disclosureControl.service.DisclosureNoticeVersionService;
import com.kayak.pms.disclosureControl.service.ScheduleNoticeService;
import com.kayak.utils.DateHelper;
import com.kayak.pms.global.constants.DisclosureStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/5/14 15:07
 */
@APIDefine(desc = "通用补录界面服务", model = DisclosureWordDate.class)
@Service
@Slf4j
public class DisclosureWordDateService {

    private static final String separator = "/";
    private static final String DisclosureDir = "DisclosureDir" + separator;
    private static final String DisclosureImageDir = "DisclosureImageDir" + separator;


    @Autowired
    private T8DisclosureNoticeChannelDao  t8DisclosureNoticeChannelDao;

    @Autowired
    private ScheduleNoticeService scheduleNoticeService;

    @Autowired
    private DisclosureNoticeVersionDao  disclosureNoticeVersionDao;

    @Autowired
    private DisclosureNoticeChannelService  disclosureNoticeChannelService;

    @Autowired
    private DisclosureNoticeVersionService disclosureNoticeVersionService;

    @Autowired
    private DisclosureWordDateDao wordDateDao;


    @Autowired
    private DisclosureModVersionDao disclosureModVersionDao;

    @Autowired
    private DisclosureNoticeProcessDao disNoticeProcessDao;

    @Autowired
    private DisclosureOperationDao disclosureOperationDao;

    @Autowired
    private DisclosureNoticeDao disclosureNoticeDao;

    @Autowired
    private DisclosureNoticeValueDao disclosureNoticeValueDao;

    @Value("${path.word}")
    private String basePath;


    @API(desc = "通用补录界面", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addSupplementaryRecord(SqlParam<DisclosureWordDate> params) throws Exception {
        String jsonData = params.getModel().getJsonData();
        JSONObject _json = new JSONObject(jsonData);
        Map<String, Object> map = _json.toMap();
        if (map != null) {
            //保存补录数据
            DaoUtil.doTrans(() -> {
                String date = DateHelper.getCurrentDate();
                String time = DateHelper.getCurrentTime();
                String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
                map.forEach((key, value) -> {
                    DisclosureWordDate disclosureWordDate = new DisclosureWordDate();
                    disclosureWordDate.setColumnKey(key);
                    disclosureWordDate.setColumnValue((String) value);
                    disclosureWordDate.setCrtDate(date);
                    disclosureWordDate.setCrtTime(time);
                    disclosureWordDate.setCrtUser(userId);
                    try {
                        wordDateDao.insert(disclosureWordDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }
        return RequestSupport.updateReturnJson(true, "操作成功!!", null).toString();
    }

    @API(desc = "修改信披模板源字段", params = "id,disclosure_mod_version_id,t8_disclosure_source_id,column_label,column_key,column_value,isdisplay,roleids,is_sysvalue,sqls,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark", auth = APIAuth.NO)
    public String updateDisclosureModColumn(SqlParam<DisclosureWordDate> params) throws Exception {
        try {
            //字段数据
            String jsonData = params.getModel().getFilFormData();
            //补录前版本id
            String versionIdHis = params.getModel().getT8DisclosureVersionId();
            //补录前版本号
            String disclosureVersion = params.getModel().getDisclosureVersion();
            String t8DisclosureNoticeId = params.getModel().getT8DisclosureNoticeId();
            AtomicBoolean needFB = new AtomicBoolean(false);//是否需要发布
            AtomicReference<String> versionIdNew = new AtomicReference<>("");
            JSONObject _json = new JSONObject(jsonData);
            Map<String, Object> map = _json.toMap();
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>数据补录开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            if (map != null) {
                //保存补录数据
                DaoUtil.doTrans(() -> {
                    String date = DateHelper.getCurrentDate();
                    String time = DateHelper.getCurrentTime();
                    String userId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
                    String username = (String) SysUtil.getSysUserParamValue("sys_user_username");
                    /**数据库配置表参数获取配置路径*/
                    String path=System.getProperty("os.name").toLowerCase().startsWith("win")?
                            SysUtil.getSystemParamsByParaid("70000010000"):
                            SysUtil.getSystemParamsByParaid("70000010001");

                    /**组装公告版本数据*/
                    //最大版本号处理
                    List<DisclosureNoticeVersion> notVersionAll =  disclosureNoticeValueDao.findDisNoticeNowVersionAll(params);
                    DisclosureNoticeVersion  notVersionMax = notVersionAll.get(0);
                    if (Strings.isNotBlank(disclosureVersion)){
                        int number =Integer.parseInt(disclosureVersion.substring(disclosureVersion.lastIndexOf(".")+1));
                        number+=1;
                        notVersionMax.setNoticeVersion(disclosureVersion.substring(0,disclosureVersion.lastIndexOf(".")+1)+ number);
                    }
                    notVersionMax.setCrtDate(date);
                    notVersionMax.setCrtTime(time);
                    notVersionMax.setCrtUserId(userId);
                    notVersionMax.setCrtUserName(username);
                    //版本文件存储路径处理
                    versionIdNew.set(disclosureNoticeVersionDao.addT8DisclosureNoticeVersion(notVersionMax));
                    String fileStorePath= path+separator+versionIdNew.get()+separator;
                    notVersionMax.setFilePath(fileStorePath);
                    notVersionMax.setCrtPath(fileStorePath);
                    notVersionMax.setId(versionIdNew.get());

                    //执行插入
                    disclosureNoticeVersionDao.updT8DisclosureNoticeVersion(notVersionMax);

                    /**更替上一版本渠道信息为当前版本信息*/
                    List<T8DisclosureNoticeChannel>  channelsAll = t8DisclosureNoticeChannelDao.findAllNoticeChannel(versionIdHis);
                    for (T8DisclosureNoticeChannel noticeChannel : channelsAll) {
                        //组装渠道基本信息
                        noticeChannel.setDisclosureNoticeVersionId(versionIdNew.get());
                        noticeChannel.setCreateDate(DateUtil.getNowDate());
                        noticeChannel.setCreateTime(DateUtil.getNowTime());
                        noticeChannel.setCreateUserId(userId);
                        noticeChannel.setCreateUserName(username);
                        String noticeChannelId = t8DisclosureNoticeChannelDao.addT8DisclosureNoticeChannel(noticeChannel);
                        String noticeChannelIds = "";
                        //新增的渠道拼接
                        noticeChannelIds = (Strings.isNotBlank(noticeChannelIds)?noticeChannelIds + ",":"") + noticeChannelId;
                        needFB.set(true);
                    }

                    /**更替上一版本字段值信息为当前版本信息*/
                    List<DisclosureNoticeValue> listHis = disclosureNoticeValueDao.findAllDisNoticeValues(versionIdHis);
                    map.forEach((key, value) -> {
                        for (DisclosureNoticeValue  noticeValueHis : listHis) {
                            if (noticeValueHis.getColumnKey().equals(key)){
                                noticeValueHis.setColumnValue((String) value);
                                noticeValueHis.setT8DisclosureVersionId(versionIdNew.get());
                                noticeValueHis.setUpdDate(date);
                                noticeValueHis.setUpdTime(time);
                                /**插入字段基本信息*/
                                try {
                                    disclosureNoticeValueDao.addDisclosureNoticeValue(noticeValueHis);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    log.info(">>>>>补录前公告版本ID："+versionIdHis);
                    log.info(">>>>>补录后公告版本ID："+versionIdNew.get());
                    log.info(">>>>>补录后公告版本号："+disclosureVersion);
                    log.info(">>>>>补录后公告版本号："+notVersionMax.getNoticeVersion());
                });

                //TODO 表格数据更新,查询表格配置落地信息及数据落地信息，替换公告版本id为最新id即可

            }
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>数据补录结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


            /**数据补录结束后更新*/
            Map<String, Object> upd_params = new HashMap<>();
            String disclosureStatus = needFB.get() ? DisclosureStatus.waitPub.getItemKey() : DisclosureStatus.overDown.getItemKey();
            upd_params.put("disclosureStatusAfter", disclosureStatus);
            upd_params.put("t8DisclosureNoticeId", t8DisclosureNoticeId);
            upd_params.put("noticeVersionId", versionIdNew);

            disclosureNoticeDao.updateDisclosureNoticeStatus(upd_params);//数据补录更新状态
            disclosureNoticeDao.updateDisclosureVersionStatus(upd_params);//数据补录更新版本状态
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "补录数据失败!", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "补录数据成功!", null).toString();
    }

    @API(desc = "暂存入库",  auth = APIAuth.NO)
    public String addInformation(SqlParam<DisclosureWordDate> params) throws Exception {
        String jsonData = params.getModel().getFilFormData();
        String disclosureNoticeId = params.getModel().getT8DisclosureNoticeId();
        JSONObject _json = new JSONObject(jsonData);
        String filInvestFormData = params.getModel().getFilInvestFormData();
        JSONObject _json2 = new JSONObject(filInvestFormData);
        HashMap<String, Object> map = JSON.parseObject(jsonData, HashMap.class);
        //Map<String, Object> map = _json.toMap();
        Map<String, Object> map2 = _json2.toMap();
        //20220117 处理前端传回来number类型变科学记数法
        doScientificNotation(map2);
        String date = DateHelper.getCurrentDate();
        String time = DateHelper.getCurrentTime();
        String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
        try {
            DaoUtil.doTrans(() -> {
                //更新产品基本信息表产品简介数据
                if(map.get("prod_code") != null && map.get("prod_code") != ""){
                    wordDateDao.updtProdInfo(Tools.toString(map2.get("prod_desc")),Tools.toString(map.get("prod_code")));
                }
                //查询公告进程表
                if (map != null) {
                    //保存补录数据
                    map.forEach((key, value) -> {
                        DisclosureWordDate disclosureWordDate = new DisclosureWordDate();
                        disclosureWordDate.setColumnKey(key);
                        String valueVal = "";
                        if (value != null) {
                            valueVal = value.toString();
                        }
                        disclosureWordDate.setColumnValue(valueVal);
                        disclosureWordDate.setT8DisclosureNoticeId(params.getModel().getT8DisclosureNoticeId());
                        disclosureWordDate.setUpdDate(date);
                        disclosureWordDate.setUpdTime(time);
                        disclosureWordDate.setUpdUser(userId);
                        try {
                            wordDateDao.update(disclosureWordDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                if (map2 != null) {
                    //保存补录数据
                    String root = FileUtil.getFileStorePath();
                    map2.forEach((key, value) -> {
                        DisclosureWordDate disclosureWordDate = new DisclosureWordDate();
                        disclosureWordDate.setColumnKey(key);
                        String valueVal = "";
                        if (value != null) {
                            if("image_path".equals(key)){
//                                    String filePath = root+separator+DisclosureImageDir + disclosureNoticeId + separator + "disclosure";
//                                    valueVal = filePath+separator+value.toString();
                                valueVal = (String)map2.get("realImagePath");
                            }else if("image_path2".equals(key)){
//                                    String filePath = root+separator+DisclosureImageDir + disclosureNoticeId + separator + "disclosure";
//                                    valueVal = filePath+separator+value.toString();
                                valueVal = (String)map2.get("realImagePath2");
                            }else{
                                valueVal = value.toString();
                            }

                        }
                        disclosureWordDate.setColumnValue(valueVal);
                        disclosureWordDate.setT8DisclosureNoticeId(params.getModel().getT8DisclosureNoticeId());
                        disclosureWordDate.setUpdDate(date);
                        disclosureWordDate.setUpdTime(time);
                        disclosureWordDate.setUpdUser(userId);

                        try {
                            wordDateDao.update(disclosureWordDate);
                            SqlRow sqlRow = wordDateDao.find(disclosureWordDate);

                            if("is_formal,is_single,non_standard_term_desc,non_standard_desc".contains(key)&&(sqlRow==null ||StringUtils.isEmpty(sqlRow.getString("t8_disclosure_notice_id")))) {
                                wordDateDao.insertValue(disclosureWordDate);
                            }

                        } catch (Exception e) {
                            log.error("数据库操作异常{}",e);
                        }
                    });
                }
            });
            return RequestSupport.updateReturnJson(true,"暂存成功！", null).toString();
        } catch ( Exception e) {
            log.error(e.getMessage(), e);
            return RequestSupport.updateReturnJson(false,"暂存失败！", null).toString();
        }
    }
    //将科学计数格式化
    private void doScientificNotation(Map<String, Object> map2) {
        Object securities_underwritten_number = map2.get("securities_underwritten_number");
        if(securities_underwritten_number!=null){
            map2.put("securities_underwritten_number",new BigDecimal(String.valueOf(securities_underwritten_number)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }

        Object securities_issued_number = map2.get("securities_issued_number");
        if(securities_issued_number!=null){
            map2.put("securities_issued_number",new BigDecimal(String.valueOf(securities_issued_number)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }

        Object business_number = map2.get("business_number");
        if(business_number!=null){
            map2.put("business_number",new BigDecimal(String.valueOf(business_number)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }

        Object securities_issued_amount = map2.get("securities_issued_amount");
        if(securities_issued_amount!=null){
            map2.put("securities_issued_amount",new BigDecimal(String.valueOf(securities_issued_amount)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }

        Object gd_sale_fee = map2.get("gd_sale_fee");
        if(gd_sale_fee!=null){
            map2.put("gd_sale_fee",new BigDecimal(String.valueOf(gd_sale_fee)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }

        Object business_amount = map2.get("business_amount");
        if(business_amount!=null){
            map2.put("business_amount",new BigDecimal(String.valueOf(business_amount)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }

        Object securities_underwritten_amount = map2.get("securities_underwritten_amount");
        if(securities_underwritten_amount!=null){
            map2.put("securities_underwritten_amount",new BigDecimal(String.valueOf(securities_underwritten_amount)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }


    }

}
