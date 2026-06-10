package com.kayak.pms.disclosureControl.service;

import com.kayak.core.util.DateUtil;
import com.kayak.pms.basePublish.dao.*;
import com.kayak.utils.DateHelper;
import com.kayak.pms.global.constants.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.T81.dao.T8ProdInfoDao;
import com.kayak.pms.basePublish.enums.RegularAssetEnum;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.disclosureControl.dao.*;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.*;
import com.kayak.pms.disclosureControl.util.SynDataUtil;
import com.kayak.pms.email.dao.T8DisChannelInfoDao;
import com.kayak.pms.email.model.T8DisChannelInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
@Slf4j
@Service
@APIDefine(desc = "产品信息公告服务", model = ScheduleNotice.class)
public class ScheduleNoticeService {

    //文件分隔符
    private static final String separate = "/";

    @Autowired
    private ScheduleNoticeDao scheduleNoticeDao;

    @Autowired
    private DisclosureNoticeDao disclosureNoticeDao;

    @Autowired
    private DisclosureProdTaskService disclosureProdTaskService;

    @Autowired
    private DisclosureNoticeProcessDao disNoticeProcessDao;

    @Autowired
    private DisclosureChannelDao disclosureChannelDao;

    @Autowired
    private DisclosureOperationDao disOperationDao;

    @Autowired
    private ScheduleProdRuleDao scheduleProdRuleDao;

    @Autowired
    private ScheduleWorkdayDao scheduleWorkdayDao;

    @Autowired
    private DisclosureProdTaskDao t8DisclosureProdTaskDao;

    @Autowired
    private DisclosureWorkdayDao disclosureWorkdayDao;

    @Autowired
    private DisclosureNoticeValueDao disclosureNoticeValueDao;

    @Autowired
    private DisclosureDataSetDao disclosureDataSetDao;

    @Autowired
    private DisclosureChannelRuleDao disclosureChannelRuleDao;

    @Autowired
    private T8DisclosureNoticeChannelDao  t8DisclosureNoticeChannelDao;

    @Autowired
    private DisclosureNoticeVersionDao  disclosureNoticeVersionDao;

    @Autowired
    private T8DisChannelInfoDao t8DisChannelInfoDao;


    static String userId = Strings.EMPTY;

    static String userName = Strings.EMPTY;


    public static void innitUserInfo() {
        userId =     (String) SysUtil.getSysUserParamValue("sys_user_userid");
        userName = (String) SysUtil.getSysUserParamValue("sys_user_username");
    }


    public SqlResult<ScheduleNotice> findT8DisclosureNotices(SqlParam<ScheduleNotice> params) throws Exception {
        params.setMakeSql(true);
        return scheduleNoticeDao.findT8DisclosureNotices(params);
    }

    public int addT8DisclosureNotice(SqlParam<ScheduleNotice> params) throws Exception {
        return scheduleNoticeDao.addT8DisclosureNotice(params);
    }

    public int updateT8DisclosureNotice(SqlParam<ScheduleNotice> params) throws Exception {
        return scheduleNoticeDao.updateT8DisclosureNotice(params);
    }

    public int deleteT8DisclosureNotice(SqlParam<ScheduleNotice> params) throws Exception {
        return scheduleNoticeDao.deleteT8DisclosureNotice(params);
    }

    /**
     * 功能：先删除生成日期为当天的公告数据，再插入
     * 作者：rennannan
     *
     * @param list
     * @throws Exception
     */
    public void delAndDelDisclosureNotice(List<ScheduleNotice> list, String date) throws Exception {
        //查询已经开始补录的notice数据
        DisclosureNoticeProcess process = new DisclosureNoticeProcess();
        process.setCrtDate(date);
        process.setDisclosureType("5");
        List<DisclosureNoticeProcess> inputProcessList = disNoticeProcessDao.findInputProcess(process);
        String inputNoticeIds = "";
        for (DisclosureNoticeProcess inputProcess : inputProcessList) {
            inputNoticeIds += inputProcess.getT8DisclosureNoticeId() + ",";
        }
        DisclosureOperation ope = new DisclosureOperation();
        ope.setCrtDate(date);
        ope.setOperationType(OperationTypeEnum.TWO.getVal());//操作类型  数据补录
        ope.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());//信披类型
        ScheduleNotice notice = new ScheduleNotice();
        notice.setCrtDate(date);
        notice.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());

        if (StringUtils.isNotEmpty(inputNoticeIds)) {
            inputNoticeIds = inputNoticeIds.substring(0, inputNoticeIds.length() - 1);
            inputNoticeIds = "(" + inputNoticeIds + ")";
            ope.setNotInNoticeIds(inputNoticeIds);
            process.setNotInNoticeIds(inputNoticeIds);
            notice.setNotInNoticeIds(inputNoticeIds);
        }
        DaoUtil.doTrans(() -> {

            //删除待办operation表中的数据
            disOperationDao.deleteOpeByTypeAndCrtDate(ope);

            //删除数据补录表process表中数据
            disNoticeProcessDao.deleteProcessByCrtDate(process);

            //根据当前日期删除公告notice表数据
            scheduleNoticeDao.deleteByCrtDate(notice);
            List<ScheduleNotice> needRemoveNoticeList = new ArrayList<>();
            for (ScheduleNotice schedule : list) {
                for (DisclosureNoticeProcess pro : inputProcessList) {
                    if (schedule.getTaskId().equals(pro.getTaskId())) {
                        needRemoveNoticeList.add(schedule);
                    }
                }
            }
            list.removeAll(needRemoveNoticeList);
            for (ScheduleNotice schedule : list) {
//                comnGeneNotice(schedule);
            }
        });

    }
    /**
     * @功能描述:生成公告数据（公告基本数据、版本、渠道、字段替换值）
     * @params:[scheduleNotice, forUpdate]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/4
     */
    public void comnGeneNotice(ScheduleNotice scheduleNotice,Boolean forUpdate,String userid,String username) throws Exception {

        String disclosureType = scheduleNotice.getDisclosureType();
        String disclosureSonType = scheduleNotice.getDisclosureSonType();
        String baseDate = scheduleNotice.getProdBaseDate();//基准日期
        String beforeChangeTitle = scheduleNotice.getNoticeTitle();//未替换的公告标题
        String sysCrtDate = scheduleNotice.getSysCrtDate();//公告的系统生成时间
        String prodCode = scheduleNotice.getProdCode();
        /**
         * 获取公告字段取值范围，定期报告、整体公告、净值整体公告等需要定义取值范围的公告，通过该方法获取公告取值范围；该范围将可作为取值sql中的条件执行
         */
        //字段取值的日期范围->getReportDate->不同公告取值范围不同
        Map<String, String> reportDateMap = getReportDate(baseDate, disclosureType,disclosureSonType);
        String startDate = reportDateMap.get("startDate");
        String endDate = reportDateMap.get("endDate");
        scheduleNotice.setReportStartDate(startDate);//公告取值范围起始日期
        scheduleNotice.setReportEndDate(endDate);//公告取值范围结束日期


        //组装公告对象基本信息
//        String date = DateUtil.getSysWordDay();
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        scheduleNotice.setUpdDate(date);
        scheduleNotice.setUpdTime(time);
        scheduleNotice.setCrtDate(date);
        scheduleNotice.setCrtTime(time);
        scheduleNotice.setCrtUserId(userid);
        scheduleNotice.setCrtUserName(username);
        scheduleNotice.setUpdUserId(userid);
        scheduleNotice.setUpdUserName(username);
        scheduleNotice.setEffectStatus(EffectStatus.yes.getItemKey());//公告是否生效，初始状态为生效

        /**
         * 公告标题替换公共方法，替换原标题占位符 TODO 待改造字段配置化，统一配置
         */
        String title=disclosureProdTaskService.queryTitle(beforeChangeTitle,baseDate,prodCode,disclosureType,disclosureSonType);
        scheduleNotice.setNoticeTitle(title);


        /**
         * 校验任务是否存在已生成的公告
         */
        SqlRow notice = t8DisclosureNoticeChannelDao.checkExistNoticeId(scheduleNotice);
        String noticeId = notice!=null? notice.getString("noticeId"):"";
        //更新公告的前提是存在原有公告，若不存在又想更新则不做操作；若的确存在基准日期、信披类型符合任务的公告，请检查产品代码、产品参数是否符合
        if (Strings.isNotBlank(noticeId)){
            if (forUpdate){
                scheduleNotice.setId(noticeId);
                log.info(" 该公告执行更新{}",JSONObject.toJSONString(scheduleNotice));
                scheduleNoticeDao.updT8DisclosureNotice(scheduleNotice);
            }
        }else {
            if (!forUpdate){
                log.info(" 生成新公告{}",JSONObject.toJSONString(scheduleNotice));
                scheduleNotice.setId(scheduleNoticeDao.addT8DisclosureNotice(scheduleNotice));
            }
        }


        /**
         * 顺序执行：插入公告->插入版本->插入渠道->插入取值字段信息
         */
        if (Strings.isNotBlank(scheduleNotice.getId())){
            this.disclosureNoticeVCV(scheduleNotice);
        }
    }

    public void disclosureNoticeVCV(ScheduleNotice scheduleNotice ) throws Exception {
        /**
         * 顺序执行：插入公告->插入版本->插入渠道->插入取值字段信息
         */
        log.info(">>>>>>>>>>>>>>>>公告版本（公告id："+scheduleNotice.getId()+"）数据生成start>>>>>>>>>>>>>>>>");
        //插入公告版本信息
        addDisclosureNoticeVersion(scheduleNotice);
        //插入公告渠道信息
        addDisclosureNoticeChannel(scheduleNotice);
        //插入公告取值信息
        initNoticeValue(setMapProperties(scheduleNotice),scheduleNotice);
        log.info(">>>>>>>>>>>>>>>>公告版本（公告id："+scheduleNotice.getId()+"）数据生成end>>>>>>>>>>>>>>>>");
    }


    /**
     * @功能描述:新增待发布渠道信息
     * @params:[scheduleNotice]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/4
     */
    private void addDisclosureNoticeChannel(ScheduleNotice scheduleNotice) throws Exception {
        try {
            String channelIds = scheduleNotice.getChannelIds();
            DisclosureChannelRule ChannelRule = new  DisclosureChannelRule();
            //判断是否有不对接渠道的渠道id，有则剔除不生成该渠道的公告（为防止渠道配置设置了不对接的渠道）
            if (Strings.isNotBlank(channelIds)) {
                List<DisclosureChannel> channels = disclosureChannelDao.findChannelsByIds(channelIds);
                if (channels.size() != 0) {
                    for (DisclosureChannel channel : channels) {
                        //该渠道不对接则跳出或渠道没启用则跳出
//                        if (channel.getIsDocking().equals(IsDocking.no.getItemKey())||channel.getStatus().equals(XpStatus.stop.getItemKey())) {
//                            continue;
//                        }
                        //不对接的渠道也要展示
                        if (channel.getStatus().equals(XpStatus.stop.getItemKey())) {
                            continue;
                        }

                        //配置没启用则跳出，查询配置时需要确定生成该渠道是通过哪条渠道配置获取的；产品参数、信披类型作为参数做校验
//                        ChannelRule.setChannelIds(channel.getId());
//                        ChannelRule.setDisclosureType(scheduleNotice.getDisclosureType());
//                        ChannelRule.setDisclosureSonType(scheduleNotice.getDisclosureSonType());
//                        if (disclosureChannelRuleDao.findIsStoping(ChannelRule)<=0){
//                            continue;
//                        }
                        //组装渠道基本信息
                        T8DisclosureNoticeChannel noticeChannel = new T8DisclosureNoticeChannel();
                        noticeChannel.setDisclosureNoticeId(scheduleNotice.getId());
                        noticeChannel.setDisclosureNoticeVersionId(scheduleNotice.getNoticeVersionId());
                        noticeChannel.setDisclosureNoticeChannelId(channel.getId());
                        noticeChannel.setUploadFileName(scheduleNotice.getNoticeTitle());
                        noticeChannel.setNoticeChannelPublicStatus(DisclosureStatus.waitPub.getItemKey());
                        //不对接的渠道直接设置为已发布
                        if (channel.getIsDocking().equals(IsDocking.no.getItemKey())) {
                            noticeChannel.setNoticeChannelPublicStatus(DisclosureStatus.overDown.getItemKey());
                        }
                        noticeChannel.setChannelPublicDate(scheduleNotice.getPlanFbDate());

                        String date = DateUtil.getSysWordDay();
                        String time = DateUtil.getNowTime();
                        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
                        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称

                        noticeChannel.setCreateDate(date);
                        noticeChannel.setCreateTime(time);
                        noticeChannel.setCreateUserId(userid);
                        noticeChannel.setCreateUserName(username);
                        String noticeChannelId = t8DisclosureNoticeChannelDao.addT8DisclosureNoticeChannel(noticeChannel);
                        //新增的渠道拼接
                        scheduleNotice.setNoticeChannelId((Strings.isNotBlank(scheduleNotice.getNoticeChannelId())?scheduleNotice.getNoticeChannelId()+",":"") + noticeChannelId);
                    }
                }
            }
        } catch (Exception e) {
            //事务不支持时主动删除新增数据
            //校验任务状态，若未生成过公告则可以删除
            if (t8DisclosureNoticeChannelDao.checkTaskStatus(scheduleNotice.getTaskId())>0){
                scheduleNoticeDao.delT8DisclosureNotice(scheduleNotice);
            }
            scheduleNoticeDao.delT8DisclosureNoticeVersion(scheduleNotice);
            throw new RuntimeException();
        }
    }

    /**
     * @功能描述:新增公告
     * @params:[scheduleNotice]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/4
     */
    private void addDisclosureNoticeVersion(ScheduleNotice scheduleNotice ) throws Exception {
        try{
            /**数据库配置表参数获取配置路径*/
            String path=System.getProperty("os.name").toLowerCase().startsWith("win")?
                    SysUtil.getSystemParamsByParaid("70000010000"):
                    SysUtil.getSystemParamsByParaid("70000010001");

            /**组装公告数据及基本数信息*/
            DisclosureNoticeVersion noticeVersion = new  DisclosureNoticeVersion();
            noticeVersion.setT8DisclosureNoticeId(scheduleNotice.getId());
            noticeVersion.setFileName(scheduleNotice.getNoticeTitle());
            //查询公告版本max+1，存在原公告版本则沿用公告版本号，没有则V1.0
            SqlRow notVersionMax =  disclosureNoticeValueDao.findDisNoticeNowVersion(scheduleNotice.getId());
            String notVersionMaxNum =notVersionMax !=null? notVersionMax.getString("notice_version"):"";
            String notVersionMaxId =notVersionMax !=null? notVersionMax.getString("id"):"";
            if (Strings.isNotBlank(notVersionMaxNum)){
                int number =Integer.parseInt(notVersionMaxNum.substring(notVersionMaxNum.lastIndexOf(".")+1));
                number+=1;
                noticeVersion.setNoticeVersion(notVersionMaxNum.substring(0,notVersionMaxNum.lastIndexOf(".")+1)+ number);
            }else {
                noticeVersion.setNoticeVersion("V1.0");
            }
            noticeVersion.setModVersion(scheduleNotice.getVersion());
            noticeVersion.setDisclosureModVersionId(scheduleNotice.getDisclosureModVersionId());
            noticeVersion.setIsNoticePub(scheduleNotice.getDisclosureStatus());//是否发布,版本状态与公告状态一致
            noticeVersion.setProdCode(scheduleNotice.getProdCode());

            String date = DateHelper.getCurrentDate();
            String time = DateHelper.getCurrentTime();
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
            String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称
            noticeVersion.setCrtDate(date);
            noticeVersion.setCrtTime(time);
            noticeVersion.setCrtUserId(userid);
            noticeVersion.setCrtUserName(username);
            //没模板的都不执行公告版本新增
            if (Strings.isBlank(noticeVersion.getDisclosureModVersionId()))
                return;
            String versionId=disclosureNoticeVersionDao.addT8DisclosureNoticeVersion(noticeVersion);
            String fileStorePath= path+versionId+separate;
            noticeVersion.setFilePath(fileStorePath);
            noticeVersion.setCrtPath(fileStorePath);
            noticeVersion.setId(versionId);
            //保存公告版本路径
            disclosureNoticeVersionDao.updT8DisclosureNoticeVersion(noticeVersion);
            scheduleNotice.setNoticeVersionId(versionId);
//            /**更改上一版本发布状态*/
//            if (Strings.isNotBlank(notVersionMaxId)){
//                noticeVersion.setIsNoticePub(NoticeVersionPub.closeSend.getItemKey());//取消发布
//                disclosureNoticeVersionDao.updNoticeVersionStatus(noticeVersion);
//            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            //事务不支持时主动删除新增数据
            //校验任务状态，若未生成过公告则可以删除
            if (t8DisclosureNoticeChannelDao.checkTaskStatus(scheduleNotice.getTaskId())>0)
                scheduleNoticeDao.delT8DisclosureNotice(scheduleNotice);
            throw new RuntimeException();
        }
    }

    /**
     * 功能：插入数据补录与待办信息
     * 作者：rennannan
     * 日期：20210604
     */
    public void addProcessAndOpe(List<ScheduleNotice> userList, String role, String noticeId, String disclosureType, String prodCode) throws Exception {
        String nowDate = DateUtil.getNowDate();
        String nowTime = DateUtil.getNowTime();
        for (ScheduleNotice scheNotice : userList) {
            //插入数据补录信息
            String userid = scheNotice.getUserId();
            DisclosureNoticeProcess process = new DisclosureNoticeProcess();
            process.setRoleId(role);
            process.setUserId(userid);
            process.setCrtDate(nowDate);
            process.setCrtTime(nowTime);
            process.setT8DisclosureNoticeId(noticeId);
            process.setInputStatus("0");
            String processId = disNoticeProcessDao.insertNoticeProcess(process);
            //生成待办
            DisclosureOperation ope = new DisclosureOperation();
            ope.setStatus("0");//状态 0 待办结
            ope.setProdCode(prodCode); //产品id
            ope.setDisclosureType(disclosureType);//信披类型
            ope.setRoleid(role);//角色编号
            ope.setOperationType(OperationTypeEnum.TWO.getVal());//操作类型 2 代表数据补录
            ope.setUserid(userid);//用户编号
            //ope.setDealId(processId);//业务流水号  process的id
            ope.setDealId(noticeId);
            //ope.setDealTable("idb_disclosure_notice_process");//业务表
            ope.setDealTable("idb_disclosure_notice");
            ope.setCrtDate(nowDate);
            ope.setCrtTime(nowTime);
            disOperationDao.insertDisOperation(ope);
        }
    }

    /**
     * 功能：根据信披规则id查找模板字段对应的角色
     * 作者：rennannan
     * 日期：20210604
     *
     * @param ruleId
     * @return
     * @throws Exception
     */
    public List<String> findRoleList(String ruleId) throws Exception {
        List<ScheduleNotice> scheduleList = scheduleNoticeDao.findRoleIdsByRuleId(ruleId);
        List<String> rolesList = new ArrayList();
        if (scheduleList.size() > 0) {
            String rolesStr = "";
            for (ScheduleNotice notice : scheduleList) {
                rolesStr += notice.getRoleIds() + ",";
            }
            String[] strs = rolesStr.split(",");

            for (int i = 0; i < strs.length; i++) {
                if (!rolesList.contains(strs[i])) {
                    rolesList.add(strs[i]);
                }
            }
        }
        return rolesList;
    }

    /**
    * @功能描述:提供字段配置可作为sql条件的参数
    * @params:[notice]
    * @return:java.util.Map<java.lang.String,java.lang.Object>
    * @Athor:ouyifan
    * @date:2022/8/24
    */
    public static Map<String, Object> setMapProperties(ScheduleNotice notice) throws ParseException {
        Map<String, Object> param = new HashMap<>();

        param.put("t8_disclosure_rule_id", notice.getDisclosureProdRuleId());//产品信披规则表ID
        param.put("t8DisclosureRuleId", notice.getDisclosureProdRuleId());//产品信披规则表ID

        param.put("t8_disclosure_notice_id", notice.getId());//notice的ID
        param.put("t8DisclosureNoticeId", notice.getId());//notice的ID

        param.put("prod_code", notice.getProdCode());//产品代码
        param.put("prodCode", notice.getProdCode());//产品代码
        param.put("prod_cd", notice.getProdCode());//产品代码
        param.put("prodCd", notice.getProdCode());//产品代码

        param.put("disclosure_type", notice.getDisclosureType());//信披类型
        param.put("disclosureType", notice.getDisclosureType());//信披类型

        param.put("disclosureSonType", notice.getDisclosureSonType());//信披子类型
        param.put("disclosure_son_type", notice.getDisclosureSonType());//信披子类型

        param.put("report_date", notice.getProdBaseDate());//报告日期
        param.put("reportDate", notice.getProdBaseDate());//报告日期

        param.put("regular_start_date", notice.getReportStartDate());//系统预计生成报告取值区间开始日期
        param.put("regularStartDate", notice.getReportStartDate());//系统预计生成报告取值区间开始日期

        param.put("regular_end_date", notice.getReportEndDate());//系统预计生成报告取值区间结束日期
        param.put("regularEndDate", notice.getReportEndDate());//系统预计生成报告取值区间结束日期

        param.put("prod_base_date", notice.getProdBaseDate());//基准日期
        param.put("prodBaseDate", notice.getProdBaseDate());//基准日期

        param.put("iss_date", notice.getPlanFbDate());//计划发布日期

        param.put("crt_date", notice.getCrtDate());
        param.put("crtDate", notice.getCrtDate());

        param.put("crt_time", notice.getCrtTime());
        param.put("crtTime", notice.getCrtTime());

        param.put("crt_user_id", notice.getCrtUserId());
        param.put("crtUserId", notice.getCrtUserId());

        param.put("crt_user_name", notice.getCrtUserName());
        param.put("crtUserName", notice.getCrtUserName());

        param.put("noticeChannelId", notice.getNoticeChannelId());
        param.put("notice_channel_id", notice.getNoticeChannelId());

        param.put("noticeVersionId", notice.getNoticeVersionId());
        param.put("notice_version_id", notice.getNoticeVersionId());
        param.put("disclosureModVersionId", notice.getDisclosureModVersionId());//模板ID
        param.put("prodForm", notice.getProdForm());//产品形态
        param.put("deal_date", DateUtil.getNowDate());//手动或系统生成公告当天日期（即数据处理日）
        param.put("dealDate", DateUtil.getNowDate());//手动或系统生成公告当天日期（即数据处理日）
        //获取申购赎回公告的标题开始日期和结束日期 先注释掉
//        String noticeTitle = notice.getNoticeTitle();
//        Map<String, String> titleDate = getTitleDate(noticeTitle);
//        param.put("titleStartDate",titleDate.get("start") );
//        param.put("titleEndDate",titleDate.get("end") );
        return param;
    }

    /**
     * 获取申购赎回公告标题开始和结束时间
     * @return
     */
    public static Map<String, String> getTitleDate(String noticeTitle){
        String substring = noticeTitle.substring(noticeTitle.length()-29,noticeTitle.length()-6);
        String start = substring.substring(0,11);
        String end = substring.substring(12);
        String ys = start.substring(0,4);
        String Ms = start.substring(5,7);
        String ds = start.substring(8,10);

        String ye = end.substring(0,4);
        String Me = end.substring(5,7);
        String de = end.substring(8,10);
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("start",ys+Ms+ds);
        dateMap.put("end",ye+Me+de);
        return dateMap;
    }


    /**
     * 功能：根据基准日期和信披类型得到报告开始日期与报告结束日期
     * 作者：rennannan
     * 日期：20210705
     *
     * @return
     */
    public  Map<String, String> getReportDate(String baseDate, String disclousureType,String disclousureSonType) throws Exception {
        String startDate = "";
        String endDate = "";
        Date cumDate = DateUtil.parseDate(baseDate,"yyyyMMdd");
        Map<String, String> param = new HashMap<>();
        if (StringUtils.isNotEmpty(disclousureType)){
            if (StringUtils.isNotEmpty(disclousureSonType)) {
                if (disclousureSonType.equals(DisclosureSonType.quarter.getItemKey())) {//季报
                    startDate = DateUtil.getMaxOrMinDateOfQuarter(baseDate, "min");
                    endDate = DateUtil.getMaxOrMinDateOfQuarter(baseDate, "max");
                } else if (disclousureSonType.equals(DisclosureSonType.seAnnual.getItemKey())||disclousureSonType.equals(DisclosureSonType.comSeAnnual.getItemKey())) {//半年报
                    startDate = baseDate.substring(0, 4) + "0101";
                    endDate = baseDate.substring(0, 4) + "0630";
                } else if (disclousureSonType.equals(DisclosureSonType.annual.getItemKey())||disclousureSonType.equals(DisclosureSonType.comAnnual.getItemKey())) {//年报
                    startDate = baseDate.substring(0, 4) + "0101";
                    endDate = baseDate.substring(0, 4) + "1231";
                } else if (disclousureSonType.equals(DisclosureSonType.month.getItemKey())) {//月报
                    startDate = DateUtil.getFirstDayDateOfMonth(baseDate);
                    endDate = DateUtil.getLastDayOfMonth(baseDate);
                }else if (disclousureSonType.equals(DisclosureSonType.netValueEntity.getItemKey())) {
                    //TODO 业务确认，是取截止到前一天还是截止到净值发布日当天的数据
                    endDate = baseDate;
                }
            }

            if (disclousureType.equals(DisclosureType.purchase.getItemKey())) {
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                Calendar cal = Calendar.getInstance();
                cal.setTime(cumDate);
                //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周了
                int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
                //获得当前日期是一个星期的第几天
                if (1 == dayWeek) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                }
                //设置一个星期的第一天
                cal.setFirstDayOfWeek(Calendar.MONDAY);
                //获得当前日期是一个星期的第几天
                int day = cal.get(Calendar.DAY_OF_WEEK);
                //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
                cal.add(Calendar.DATE, (cal.getFirstDayOfWeek() - day -7));
                String dateMonday = DateUtil.dateFormate(cal.getTime(),"yyyyMMdd");
                cal.add(Calendar.DATE, 6);
                String dateSunday = DateUtil.dateFormate(cal.getTime(),"yyyyMMdd");
                startDate = dateMonday;//获取周一日期
                endDate =  dateSunday;//获取周日日期
            }else {
                endDate = baseDate;
            }
        }
        param.put("startDate", startDate);//报告开始日期
        param.put("endDate", endDate);//报告结束日期
        return param;
    }


    public List<DisclosureWorkday> WorkdayCheck(String dayFirst, int afterDays) throws Exception {
       Map<String, String> sysDateParam = new HashMap<String, String>();
       sysDateParam.put("pgmno", "001");
       sysDateParam.put("startDate", dayFirst);
       int i = afterDays;
       sysDateParam.put("days", Integer.toString(i));
       return  disclosureWorkdayDao.findWorkDayByfiveDays(sysDateParam);
    }

    public ScheduleNotice setProperties(ScheduleNotice scheduleNotice, DisclosureProdTask prodTask) throws Exception {
        BeanUtil.copyProperties(prodTask, scheduleNotice, true);
        scheduleNotice.setTaskId(prodTask.getId());//存取任务的id
        scheduleNotice.setId("");//公告id滞空
        scheduleNotice.setDisclosureProdRuleId(prodTask.getT8DisclosureProdRuleId());//存取产品规则Id
        scheduleNotice.setDisclosureRuleId(prodTask.getT8DisclosureRuleId());//存取规则Id
        SqlRow row ;
        //查询信披规则信息 包括预计补录规则、天数 预计审批规则、天数 预计发布规则、天数
        if (DisclosureType.ensemble.getItemKey().equals(prodTask.getDisclosureType())
                ||(DisclosureType.net.getItemKey().equals(prodTask.getDisclosureType())&&DisclosureSonType.netValueEntity.getItemKey().equals(prodTask.getDisclosureSonType()))){
            //TODO 与产品信披规则不同，仅匹配配置了完全相同的产品参数的生成规则，需不需要考虑“没有匹配的生成规则时还需要轮询其他更符合条件的规则”还需参照业务需要做判断
            row=t8DisclosureNoticeChannelDao.findDisInfoZT(prodTask);
            scheduleNotice.setDisclosureRuleId(row.getString("ruleId"));
        }else {
            row=t8DisclosureNoticeChannelDao.findDisInfo(prodTask);
            scheduleNotice.setDisclosureProdRuleId(row.getString("ruleId"));
        }
        scheduleNotice.setVersion(row.getString("version"));
        scheduleNotice.setDisclosureModVersionId(row.getString("disclosure_mod_version_id"));
        //公告数据系统生成日期
        scheduleNotice.setSysCrtDate(row.getString("sys_crt_date"));
        //为替换的公告title
        scheduleNotice.setNoticeTitle(row.getString("notice_title"));
        //基准日期
        String baseDate = prodTask.getProdBaseDate();
        //预计补录
        String expSupplementRule = row.getString("exp_supplement_rule");
        String expSupplementDays = row.getString("exp_supplement_days");
        String planBlDate = "";
        Map<String, String> param = new HashMap<String, String>();
        param.put("workday", baseDate);
        if (StringUtils.isNotEmpty(expSupplementDays)&&!scheduleNotice.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())) {
            //计划补录完成日期天数，如果大于0，去对应工作日方案表中查询，否则，计划补录完成日期=基准日期
            int expDays = Integer.parseInt(expSupplementDays);
            if (expDays > 0) {
                param.put("days", expSupplementDays);
                param.put("rule", expSupplementRule);
                param.put("pgmno", "001");
                if (CollectionUtils.isNotEmpty(disclosureWorkdayDao.findPlanCrtDate(param))) {
                    planBlDate = disclosureWorkdayDao.findPlanCrtDate(param).get(0).getWorkday();
                    if (Strings.isBlank(planBlDate))
                        planBlDate = DateUtil.add(baseDate, "yyyyMMdd", expDays);
                } else {
                    planBlDate = DateUtil.add(baseDate, "yyyyMMdd", expDays);
                }

            } else {
                planBlDate = baseDate;
            }
        }else {
            planBlDate = baseDate;
        }
        scheduleNotice.setPlanBlDate(planBlDate);

        //预计审批
        String expApprovalRule = row.getString("exp_approval_rule");
        String expApprovalDays = row.getString("exp_approval_days");
        String planSpDate = "";
        if (StringUtils.isNotEmpty(expApprovalDays)&&!scheduleNotice.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())) {
            int approvalDays = Integer.parseInt(expApprovalDays);
            if (approvalDays > 0) {
                param.put("pgmno", "001");
                param.put("days", expApprovalDays);
                param.put("rule", expApprovalRule);
                if (CollectionUtils.isNotEmpty(disclosureWorkdayDao.findPlanCrtDate(param))) {
                    planSpDate = disclosureWorkdayDao.findPlanCrtDate(param).get(0).getWorkday();
                    if (Strings.isBlank(planSpDate))
                        planSpDate = DateUtil.add(baseDate, "yyyyMMdd", approvalDays);
                } else {
                    planSpDate = DateUtil.add(baseDate, "yyyyMMdd", approvalDays);
                }

            } else {
                planSpDate = baseDate;
            }
        }else {
            planSpDate = baseDate;
        }
        scheduleNotice.setPlanSpDate(planSpDate);

        //预计发布
        String expPublishRule = row.getString("exp_publish_rule");
        String expPublishDays = row.getString("exp_publish_days");
        String planFbDate = "";
        if (StringUtils.isNotEmpty(expPublishDays)&&!scheduleNotice.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())) {
            int publishDays = Integer.parseInt(expPublishDays);
            if (publishDays > 0) {
                param.put("pgmno", "001");
                param.put("days", expPublishDays);
                param.put("rule", expPublishRule);
                if (CollectionUtils.isNotEmpty(disclosureWorkdayDao.findPlanCrtDate(param))) {
                    planFbDate = disclosureWorkdayDao.findPlanCrtDate(param).get(0).getWorkday();
                    if (Strings.isBlank(planFbDate))
                        planFbDate = DateUtil.add(baseDate, "yyyyMMdd", publishDays);
                } else {
                    planFbDate = DateUtil.add(baseDate, "yyyyMMdd", publishDays);
                }
            } else {
                planFbDate = baseDate;
            }
        }else {
            planFbDate = baseDate;
        }
        scheduleNotice.setPlanFbDate(planFbDate);
        //生成日期
        scheduleNotice.setCrtDate(DateUtil.getNowDate());
        //生成时间
        scheduleNotice.setCrtTime(DateUtil.getNowTime());

        if (row.getString("if_condition").equals(Condition.yes.getItemKey())){
            scheduleNotice.setReviewStatus(ApprovalStatus.lunch.getItemKey());
        }else if (row.getString("if_condition").equals(Condition.no.getItemKey())){
            scheduleNotice.setReviewStatus(ApprovalStatus.down.getItemKey());
        }else{
            scheduleNotice.setReviewStatus(row.getString("if_condition"));
        }
        String channel_ids="";
        //整体公告ids
        if (DisclosureType.ensemble.getItemKey().equals(prodTask.getDisclosureType())||(DisclosureType.net.getItemKey().equals(prodTask.getDisclosureType())&&DisclosureSonType.netValueEntity.getItemKey().equals(prodTask.getDisclosureSonType()))){
            //TODO 与产品信披规则不同，仅匹配配置了完全相同的产品参数的渠道配置，需不需要考虑“没有匹配的渠道配置时还需要轮询其他更符合条件的配置”还需参照业务需要做判断
            channel_ids = t8DisclosureNoticeChannelDao.findChannelInfoZT(prodTask);
        }else {
            channel_ids=row.getString("channel_ids");
        }
        scheduleNotice.setChannelIds(channel_ids);
        DisclosureChannelRule ChannelRule = new  DisclosureChannelRule();
        ChannelRule.setChannelIds(channel_ids);
        ChannelRule.setDisclosureType(scheduleNotice.getDisclosureType());
        ChannelRule.setDisclosureSonType(scheduleNotice.getDisclosureSonType());
        if (StringUtils.isNotBlank(channel_ids)){
            //如果有渠道ids，判断是否有对接渠道的渠道id，有则代表需要对接（为防止渠道配置设置了不对接的渠道）
            Integer count =  disclosureChannelDao.findIsDocking(channel_ids);
            if (count>0) {
                if (row.getString("if_clearing").equals(Clearing.yes.getItemKey())) {
                    scheduleNotice.setDisclosureStatus(DisclosureStatus.clearing.getItemKey());
                } else if (row.getString("if_clearing").equals(Clearing.no.getItemKey())) {
                    scheduleNotice.setDisclosureStatus(DisclosureStatus.waitPub.getItemKey());
                }else{
                    scheduleNotice.setDisclosureStatus(DisclosureStatus.waitPub.getItemKey());
                }
            }else{//若ids都是不对接，则按照不对接生成
                scheduleNotice.setDisclosureStatus(DisclosureStatus.overDown.getItemKey());
            }
        }else if (StringUtils.isBlank(channel_ids)){
            scheduleNotice.setDisclosureStatus(DisclosureStatus.overDown.getItemKey());
        }else {
            scheduleNotice.setDisclosureStatus(DisclosureStatus.overDown.getItemKey());
        }
        return scheduleNotice;
    }

    /**
     * 功能：自动跑批生成定期公告
     * 作者：rennannan
     * 日期：20210607
     *
     * @throws Exception
     */
    @API(desc = "自动生成定期公告", operation = APIOperation.INSTER, auth = APIAuth.NO)
    public void autoGenerateNotice(SqlParam<ScheduleNotice> params) throws Exception {
        DisclosureProdTask prodTask = new DisclosureProdTask();
        String date = DateUtil.getNowDate();
        prodTask.setSysCrtDate(date); // 查询当前日期的任务
        prodTask.setDisclosureType("5");//定期报告
        List<ScheduleNotice> prodNotices = getNeedGenerateNotice(prodTask);
        if (prodNotices.size() > 0) {
            delAndDelDisclosureNotice(prodNotices, DateUtil.getNowDate());
        }
    }

    /**
     * 功能：自动生成发行公告
     * 作者：rennannan
     * 日期：20211025
     *
     * @throws Exception
     */
    @API(desc = "自动生成发行公告", operation = APIOperation.INSTER, auth = APIAuth.NO)
    public void autoGenerateIssueNotice(SqlParam<ScheduleNotice> params) throws Exception {
        DisclosureProdTask prodTask = new DisclosureProdTask();
        String date = DateUtil.getNowDate();
        prodTask.setSysCrtDate(date); // 查询当前日期的任务
        prodTask.setDisclosureType(DisclosureTypeEnum.TWO.getVal());//发行公告
        comnIssueOpe(date, prodTask);
    }

    /**
     * 功能：自动生成到期公告
     * 作者：rennannan
     * 日期：20211102
     *
     * @throws Exception
     */
    @API(desc = "自动生成到期公告", operation = APIOperation.INSTER, auth = APIAuth.NO)
    public void autoGenerateEndNotice(SqlParam<ScheduleNotice> params) throws Exception {
        DisclosureProdTask prodTask = new DisclosureProdTask();
        String date = DateUtil.getNowDate();
        prodTask.setSysCrtDate(date); // 查询当前日期的任务
        prodTask.setDisclosureType(DisclosureTypeEnum.THREE.getVal());//到期公告
        comnIssueOpe(date, prodTask);
    }

    /**
     * 功能：跑批调用公共方法（发行公告、到期公告）
     *
     * @param date
     * @param prodTask
     * @throws Exception
     */
    public void comnIssueOpe(String date, DisclosureProdTask prodTask) throws Exception {
        List<ScheduleNotice> prodNotices = getNeedGenerateNotice(prodTask);
        if (prodNotices.size() > 0) {
            //插入公告 notice表、operation表、chanel表
            addIssueNotice(prodNotices, date, prodTask.getDisclosureType());
        }
    }

    /**
     * 功能：手动生成公告调用公共方法（发行公告、到期公告）
     *
     * @param date
     * @param prodTask
     * @throws Exception
     */
    public void manualNoticeOpe(String date, DisclosureProdTask prodTask) throws Exception {
        List<ScheduleNotice> prodNotices = getNeedGenerateNotice(prodTask);
        if (prodNotices.size() > 0) {
            //插入公告 notice表、operation表、chanel表
            addNoticeManual(prodNotices, prodTask.getId(), prodTask.getDisclosureType());
        }
    }

    /**
     * 功能：删除待办表中发起审批数据（发行公告、到期公告）
     * 作者：rennannan
     * 日期：20211108
     *
     * @param noticeDate     基准日期或公告生成日期
     * @param disclosureType 信披类型
     * @param insType        删除类型  0代表根据生成日期删除  1代表根据基准日期删除
     */
    public void delOpeByNotice(String noticeDate, String disclosureType, String insType) throws Exception {
        DisclosureOperation delOpe = new DisclosureOperation();
        delOpe.setOperationType(OperationTypeEnum.SEVEN.getVal());//待办类型  发起审批
        delOpe.setDisclosureType(disclosureType);//信披类型
        delOpe.setCrtDate(noticeDate);
        if (insType.equals("0")) {//根据公告生成日期删
            disOperationDao.deleteOpeByNoticeType(delOpe);
        }
        if (insType.equals("1")) {//根据公告基准日期删
            disOperationDao.deleteOpeByNoticeInfo(delOpe);
        }
    }

    /**
     * 功能：删除渠道表中数据（发行公告、到期公告）
     * 作者：rennannan
     * 日期：20211108
     *
     * @param noticeDate     基准日期或公告生成日期
     * @param disclosureType 信披类型
     * @param insType        删除类型  0代表根据生成日期删除  1代表根据基准日期删除
     */
    public void delChannelByNotice(String noticeDate, String disclosureType, String insType) throws Exception {
        ScheduleNotice delNotice = new ScheduleNotice();
        delNotice.setDisclosureType(disclosureType);
        delNotice.setCrtDate(noticeDate);
        if (insType.equals("0")) {//根据公告生成日期删
            scheduleNoticeDao.deleteChannelByTypeAndDate(delNotice);
        }
        if (insType.equals("1")) {//根据公告基准日期删
            scheduleNoticeDao.deleteChannelByNoticeInfo(delNotice);
        }

    }

    /**
     * 功能：删除公告表中数据（发行公告、到期公告）
     * 作者：rennannan
     * 日期：20211108
     *
     * @param noticeDate     基准日期或公告生成日期
     * @param disclosureType 信披类型
     * @param insType        删除类型  0代表根据生成日期删除  1代表根据基准日期删除
     */
    public void delNotice(String noticeDate, String disclosureType, String insType) throws Exception {
        ScheduleNotice delNotice = new ScheduleNotice();
        delNotice.setDisclosureType(disclosureType);
        if (insType.equals("0")) {//根据公告生成日期删
            delNotice.setCrtDate(noticeDate);
            scheduleNoticeDao.deleteByCrtDate(delNotice);
        }
        if (insType.equals("1")) {//根据公告基准日期删
            delNotice.setProdBaseDate(noticeDate);
            scheduleNoticeDao.deleteByBaseDate(delNotice);
        }
    }

    /**
     * 功能：手动生成公告调用方法（发行公告、到期公告）
     * 作者：rennannan
     * 日期：20211108
     * noticeList 需要生成的公告   taskId 信披任务id
     */
    public void addNoticeManual(List<ScheduleNotice> noticeList, String taskId, String disclosureType) throws Exception {
        DaoUtil.doTrans(() -> {
            //删除operation待办表中数据  根据任务id删除
            DisclosureOperation operation = new DisclosureOperation();
            operation.setTaskId(taskId);
            operation.setOperationType(OperationTypeEnum.SEVEN.getVal());//操作类型7 发起审批
            disOperationDao.deleteOpeByTaskId(operation);

            //删除公告渠道表中数据 根据任务id删除
            ScheduleNotice notice = new ScheduleNotice();
            notice.setTaskId(taskId);
            scheduleNoticeDao.deleteChannelByTaskId(notice);

            //删除公告表notice中数据  根据任务id删除
            scheduleNoticeDao.deleteNoticeByTaskId(notice);

            //插入公告
            batchInsertNotice(noticeList, disclosureType);
        });
    }

    /**
     * 功能：跑批插入发行公告或到期公告、渠道表信息、待办表信息
     * 作者：rennannan
     * 日期：20211025
     *
     * @param noticeList 需要生成的公告   noticeDate生成公告日期
     * @throws Exception
     */
    public void addIssueNotice(List<ScheduleNotice> noticeList, String noticeDate, String disclosureType) throws Exception {
        DaoUtil.doTrans(() -> {
            /*----------------------------------删除数据------------------------------*/
            //删除operation待办表中数据
            delOpeByNotice(noticeDate, disclosureType, "0");

            //删除公告渠道表中数据
            delChannelByNotice(noticeDate, disclosureType, "0");

            //删除公告表notice中数据
            delNotice(noticeDate, disclosureType, "0");

            //插入公告
            batchInsertNotice(noticeList, disclosureType);
        });
    }

    /**
     * 功能：批量插入公告数据（发行公告、到期公告）
     * 作者：rennannan
     * 日期：20211108
     *
     * @param noticeList
     * @param disclosureType
     * @throws Exception
     */
    public void batchInsertNotice(List<ScheduleNotice> noticeList, String disclosureType) throws Exception {
        for (ScheduleNotice notice : noticeList) {
            /*----------------------------------插入数据------------------------------*/
            //插入公告
            String noticeId = scheduleNoticeDao.addT8DisclosureNotice(notice);

            //将任务状态更新为2已执行
            DisclosureProdTask task = new DisclosureProdTask();
            task.setId(notice.getTaskId());
            task.setStatus("2");
            t8DisclosureProdTaskDao.updateTaskStatus(task);

            //插入渠道表(配置在dataset中)
            //插入公告数据
            notice.setId(noticeId);
            initNoticeValue(setMapProperties(notice),notice);

            //插入待办表
            //查询规则中配置的公告负责角色，生成发起审批待办
            insertOperation(notice.getDisclosureProdRuleId(),notice.getProdCode(), noticeId, disclosureType, "0", OperationTypeEnum.SEVEN.getVal(), "1");
        }
    }

    /**
     * 功能：插入或者修改待办（发行公告、到期公告）
     * 作者：rennannan
     * 日期：20211108
     *
     * @param ruleId 信披规则id
     * @param noticeId 公告id
     * @param disclosureType 信披类型
     *@param status 办结状态 0-未办结 1-已办结
     *@param sqlType sql操作类型 1-新增 2-修改
     * @throws Exception
     */
    public void insertOperation(String ruleId, String prodCode, String noticeId, String disclosureType,String status, String operationType, String sqlType) throws Exception {
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        ScheduleProdRule rule = scheduleProdRuleDao.findRuleById(ruleId);
        if (rule != null) {
            String roleId = rule.getNoticeRoleid();
            //根据角色id去产品用户组查询对应用户
            ScheduleNotice noticeQuery = new ScheduleNotice();
            noticeQuery.setProdCode(prodCode);
            noticeQuery.setRoleId(roleId);
            List<ScheduleNotice> userList = scheduleNoticeDao.findUserIds(noticeQuery);
            for (ScheduleNotice user : userList) {
                DisclosureOperation ope = new DisclosureOperation();
                ope.setDealId(noticeId);//业务id存noticeid
                ope.setRoleid(roleId);//角色id
                ope.setDealTable("idb_disclosure_notice");//业务表
                ope.setDisclosureType(disclosureType);//信披类型 发行公告
                ope.setOperationType(operationType);//待办类型
                ope.setStatus(status);//办结状态
                ope.setProdCode(prodCode);
                ope.setUserid(user.getUserId());
                ope.setCrtDate(date);
                ope.setCrtTime(time);
                if ("1".equals(sqlType)) {
                    disOperationDao.insertDisOperation(ope);
                } else {
                    disOperationDao.updateOperation(ope);
                }
            }
        }
    }
    /**
     * 功能：通过信披类型、计划生成日期查询需要生成的任务并返回公告信息
     * 作者：rennannan
     * 日期：20211025
     *
     * @param prodTask
     * @return
     * @throws Exception
     */
    public List<ScheduleNotice> getNeedGenerateNotice(DisclosureProdTask prodTask) throws Exception {
        List<DisclosureProdTask> prodTasks = t8DisclosureProdTaskDao.findProdTasks(prodTask);
        List<ScheduleNotice> prodNotices = new ArrayList<>();
        prodTasks.forEach((task) -> {
            ScheduleNotice notice = new ScheduleNotice();
            //处理公告信息
            try {
                setProperties(notice, task);
            } catch (Exception e) {
                e.printStackTrace();
            }
            prodNotices.add(notice);
        });
        return prodNotices;
    }
    /**
     * @功能描述:新增字段及字段值信息，数据支持同一版本公告的补录数据沿用
     * @params:[param, scheduleNotice]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/4
     */
    public void initNoticeValue(Map<String, Object> param,ScheduleNotice scheduleNotice) throws Exception {
        try{
            List<DisclosureNoticeValue> listHistory =new ArrayList<DisclosureNoticeValue>();
            DisclosureNoticeValue noticeValue = new DisclosureNoticeValue();
            String notVersionNewId  =scheduleNotice.getNoticeVersionId();
            String noticeId  =scheduleNotice.getId();
            noticeValue.setId(noticeId);
            noticeValue.setT8DisclosureVersionId(notVersionNewId);
            boolean needBL =false;//是否需要补录
            List<Map<String,Object>> listVlog = new ArrayList<>();


            log.info(">>>>>>>>>>>>>>>>公告（公告版本id"+notVersionNewId+"）字段替换start>>>>>>>>>>>>>>>>");
            List<DisclosureNoticeValue> listNew = disclosureNoticeValueDao.findDisNoticeValuesNew(noticeValue);
            if(listNew.size()<=0){
                return;
            }
            /**查询上一版本字段补录数据，并组装公告上一版本信息*/
            SqlRow notVersionHis= disclosureNoticeValueDao.findDisNoticeHisVersion(scheduleNotice.getId());
            String notVersionHisId = notVersionHis!=null?notVersionHis.getString("notVersionId") :"";
            //set notice的版本Id为上一版本
            if (Strings.isNotBlank(notVersionHisId)){
                noticeValue.setT8DisclosureVersionId(notVersionHisId);
                listHistory = disclosureNoticeValueDao.findDisNoticeValuesHis(noticeValue);//仅手工补录数据
            }
            /**循环当前最新版本要生成的字段，并在存在上一版本字段并存在有值的情况时，沿用上一版本字段值*/
            for (DisclosureNoticeValue noticeValueNew : listNew) {
                Map<String,Object> mapVlog = new HashMap<>();
                //上一版本的字段作为更新的基本数据，有值的取原来的值，没值的新增(留存补录数据)
                for (DisclosureNoticeValue noticeValueHistory : listHistory) {
                    //上一版本该字段为补录数据且当前版本也为需要补录字段才执行替换
                    if (Strings.isNotBlank(noticeValueNew.getColumnKey())&&Strings.isNotBlank(noticeValueHistory.getColumnKey())){
                        if (noticeValueNew.getColumnKey().equalsIgnoreCase(noticeValueHistory.getColumnKey())
                                && Strings.isNotBlank(noticeValueHistory.getColumnValue())
                                && noticeValueNew.getIsSysvalue().equals(IsSysvalue.hand.getItemKey())
                                && noticeValueNew.getIsSysvalue().equals(IsSysvalue.hand.getItemKey())){
                            noticeValueNew.setColumnValue(noticeValueHistory.getColumnValue());
                            mapVlog.put("key",noticeValueNew.getColumnKey());
                            mapVlog.put("val",noticeValueHistory.getColumnValue());
                            mapVlog.put("tag","补录数据延用");
                        }
                    }
                }
                //更新最新的公告版本id
                noticeValueNew.setT8DisclosureVersionId(notVersionNewId);

                /**字段若不存在上一版本的字段值可以沿用，则执行当前字段sql配置，组装需要执行sql值条件参数*/
                if (Strings.isBlank(noticeValueNew.getColumnValue())&& IsSysvalue.sys.getItemKey().equals(noticeValueNew.getIsSysvalue())){
                    param.put("value_sql",noticeValueNew.getValueSql());//取值sql
                    param.put("column_key",noticeValueNew.getColumnKey());//字段key
                    param.put("dict",noticeValueNew.getDict());//字段字典值
                    param.put("functype",noticeValueNew.getFunctype());
                    param.put("data_type",noticeValueNew.getDataType());
                    param.put("money_format",noticeValueNew.getMoneyFormat());//金额格式
                    param.put("computed_expression",noticeValueNew.getComputedExpression());//计算方式
                    param.put("data_length",noticeValueNew.getDataLength());//数据长度
                    param.put("sql_parameter",noticeValueNew.getSqlParameter());//sql参数名
                    param.put("data_source",noticeValueNew.getDataSource());//数据库数据源


                    /**执行字段取值，字段大小写均匹配；传入字段配置信息，返回字段获取值*/
                    String value= disclosureDataSetDao.dataIntegrate(param);
                    if (Strings.isNotBlank(value)){
                        noticeValueNew.setColumnValue(value);
                    }
                    mapVlog.put("key",noticeValueNew.getColumnKey());
                    mapVlog.put("val",noticeValueNew.getColumnValue());
                    mapVlog.put("tag","系统自动取值");
                }else {
                    if (Strings.isBlank(noticeValueNew.getColumnValue())&& IsSysvalue.hand.getItemKey().equals(noticeValueNew.getIsSysvalue())) {
                        needBL = true;//标记当前公告及公告版本为需要补录的状态
                        mapVlog.put("key", noticeValueNew.getColumnKey());
                        mapVlog.put("val", noticeValueNew.getColumnValue());
                        mapVlog.put("tag", "待补录");
                    }
                }

                //插入前校验->没有key跳出
                if (Strings.isBlank(noticeValueNew.getColumnKey())){
                    continue;
                }
                /**插入字段基本信息*/
                String date = DateUtil.getSysWordDay();
                String time = DateUtil.getNowTime();
                String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
                String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称

                noticeValueNew.setCrtDate(date);
                noticeValueNew.setCrtTime(time);
                noticeValueNew.setCrtUserId(userid);
                noticeValueNew.setCrtUserName(username);
                disclosureNoticeValueDao.addDisclosureNoticeValue(noticeValueNew);
                listVlog.add(mapVlog);
            }
            for (Map map: listVlog) {
                log.info(">>>>"+map.get("tag")+"字段key："+ map.get("key"));
                log.info(">>>>"+map.get("tag")+"字段值："+ map.get("val"));
            }
            log.info(">>>>>>>>>>>>>>>>公告（公告版本id"+notVersionNewId+"）字段替换end>>>>>>>>>>>>>>>>");

            /**检索补录数据全部已补录，则更新公告及版本状态*/

            /**数据补录结束后更新*/

            if (!needBL){//不需补录则更新状态为待发布或已生成
                Map<String, Object> needBLParams = new HashMap<>();
                String disclosureStatus = Strings.isNotBlank(scheduleNotice.getChannelIds())
                        ? DisclosureStatus.waitPub.getItemKey()
                        : DisclosureStatus.overDown.getItemKey();
                //如果只有一个不需要对接的渠道也更新状态为已生成(湖南银保监局)
                String channelIds = scheduleNotice.getChannelIds() == null?"":scheduleNotice.getChannelIds();
                String[] split = channelIds.split(",");
                if(split.length== 1 && split[0].equals("37")){
                    disclosureStatus = DisclosureStatus.overDown.getItemKey();
                }
                needBLParams.put("disclosureStatusAfter", disclosureStatus);
                needBLParams.put("t8DisclosureNoticeId", noticeId);
                needBLParams.put("noticeVersionId", notVersionNewId);
                disclosureNoticeDao.updateDisclosureNoticeStatus(needBLParams);//数据补录更新状态
                disclosureNoticeDao.updateDisclosureVersionStatus(needBLParams);//数据补录更新版本状态
            }



            /**
             * 执行需要特殊替换公告表格的表格配置信息，对应公告版本转换版本配置数据；根据信披类型判断执行表格数据入库的方法；
             *  配置表中仅配置需要执行表格写入的信披类型，可通过公告信披类型直接匹配配置数据，若存在配置数据则需要执行表格写入，
             *  否则无法执行。
             */
            log.info(">>>>>>>>>>>>>>>>公告表格替换start>>>>>>>>>>>>>>>>");
            this.initAllTypeGridValue(scheduleNotice);
            log.info(">>>>>>>>>>>>>>>>公告表格替换end>>>>>>>>>>>>>>>>");

        } catch (Exception e) {
            //事务不支持时主动删除新增的数据
            //校验任务状态，若未生成过公告则可以删除
            if (t8DisclosureNoticeChannelDao.checkTaskStatus(scheduleNotice.getTaskId())>0) {
                scheduleNoticeDao.delT8DisclosureNotice(scheduleNotice);
            }
            scheduleNoticeDao.delT8DisclosureNoticeVersion(scheduleNotice);
            scheduleNoticeDao.delT8DisclosureNoticeChannel(scheduleNotice);
            e.printStackTrace();
        }
    }

    /**
     * @功能描述:执行表格配置信息对应公告版本转换版本配置数据，根据信披类型判断执行表格数据入库的方法
     * @params:[scheduleNotice]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/11
     */
    private void initAllTypeGridValue(ScheduleNotice scheduleNotice) throws Exception{

        /**
         *执行表格配置信息对应公告版本转换版本配置数据,暂时无用，注释了
         */
        //this.initGridConfigValue(scheduleNotice);

        /**
         * 根据信披类型判断执行表格数据入库的方法
         */
        //净值整体（直接走统一配置的方法，不在这插入数据——liwei）
//        if (DisclosureSonType.netValueEntity.getItemKey().equals(scheduleNotice.getDisclosureSonType())) {
//            this.initGridNetPriceValue(scheduleNotice);
//        }
        //定期报告
        if (DisclosureType.regular.getItemKey().equals(scheduleNotice.getDisclosureSonType()))
            this.initGridRegularData(scheduleNotice);
    }
    /**
     * @功能描述:处理公告表格数据公共方法，读取配置表列信息转换对应公告版本的配置数据
     * @params:[scheduleNotice]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/9
     */
    private void initGridConfigValue(ScheduleNotice scheduleNotice) throws Exception{
        //获取配置表信息
        DisclosureGridConfig gridConfigValue = new DisclosureGridConfig();
        //组装查询条件
        gridConfigValue.setDisclosureType(scheduleNotice.getDisclosureType());
        gridConfigValue.setDisclosureSonType(scheduleNotice.getDisclosureSonType());
        List<IdbNoticeGridConfigSource> configList = disclosureNoticeValueDao.findDisNoticeGridValuesNew(gridConfigValue);
        for (IdbNoticeGridConfigSource configValue: configList) {
            /**写的什么逻辑没看懂,暂时先注释了*/
            //组装对应公告版本信息
            //configValue.setDisclosureModVersionId(scheduleNotice.getDisclosureModVersionId());
            //插入公告版本表格配置信息
            //disclosureNoticeValueDao.addNoticeGridConfigValue(configValue);
        }
    }

    /**
     * @功能描述:净值整体公告表格数据处理公共方法
     * @params:[scheduleNotice]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/9
     */
    public void initGridNetPriceValue(ScheduleNotice scheduleNotice) throws Exception{
        //组装数据
        Map<String ,Object> params = new HashMap<>();

        //获取数据范围->日期范围、多产品净值数据
        params.put("prodBaseDate",scheduleNotice.getProdBaseDate());//净值整体公告基准日即为披露日
        params.put("taskId",scheduleNotice.getTaskId());//净值整体任务id
        //查询当天需要发布的产品净值数据
        List<DisclosureGridValue> prodList = this.findNetProd(params);
        for (DisclosureGridValue gridValue: prodList ) {
            //日期范围内数据处理、每条数据对应一个产品，存在多条数据、该日期内的所有产品的数据对应一条公告版本
            gridValue.setNoticeVersionId(scheduleNotice.getNoticeVersionId());//组装当前版本信息
            gridValue.setDealDate(DateUtil.getNowDate());//处理日期
            gridValue.setCrtDate(DateUtil.getNowDate());
            gridValue.setCrtTime(DateUtil.getNowTime());
            //数据入库
            disclosureNoticeValueDao.addNoticeGridNetValue(gridValue);
        }
    }
    /**
    * @功能描述:查询以某天为基准日、某任务id为组别的当日需发布的多个产品
    * @params:[params]
    * @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureGridValue>
    * @Athor:ouyifan
    * @date:2022/9/13
    */
    public List<DisclosureGridValue> findNetProd(Map<String ,Object> params) throws Exception {
        //组装数据
        DisclosureGridValue disclosuregridValue = new DisclosureGridValue();

        //获取数据范围->日期范围、多产品净值数据
        disclosuregridValue.setPosDt((String) params.get("prodBaseDate"));//净值整体公告基准日即为披露日
        disclosuregridValue.setTaskId((String) params.get("taskId"));//净值整体任务id
        //查询当天需要发布的产品净值数据
        return disclosureNoticeValueDao.findNetProdValue(disclosuregridValue);
    }
    /**
     * @功能描述:定期报告季度、半年度、年度报告资产配置情况表格数据/非标持仓表格数据/投资关联数据处理公共方法
     * @params:[scheduleNotice]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/8/11
     */
    public void initGridRegularData(ScheduleNotice scheduleNotice) throws Exception{
        //组装数据
        DisclosureGridValue disclosuregridValue = new DisclosureGridValue();

        //获取表格数据范围->日期范围、单产品资产配置情况
        disclosuregridValue.setProdCd(scheduleNotice.getProdCode());

        //公告取值日期范围
        disclosuregridValue.setPosStartDt(scheduleNotice.getReportStartDate());
        disclosuregridValue.setPosEndDt(scheduleNotice.getReportEndDate());

        //读取净值公告表格配置数据
        List<DisclosureGridValue> gridValueListAssetConfig = disclosureNoticeValueDao.findGridAssetConfigVal(disclosuregridValue);
        for (DisclosureGridValue gridValue: gridValueListAssetConfig ) {
            //日期范围内数据处理、多条数据对应一个产品、该日期内的所有产品的数据对应一条公告版本
            gridValue.setNoticeVersionId(scheduleNotice.getNoticeVersionId());//组装当前版本信息
            //数据入库
            disclosureNoticeValueDao.addNoticeGridAssetConfigVal(gridValue);
        }

        //读取非标持仓表格配置数据
        List<DisclosureGridValue> gridValueListFBasset = disclosureNoticeValueDao.findGridFBassetHoldingVal(disclosuregridValue);
        for (DisclosureGridValue gridValue: gridValueListFBasset ) {
            gridValue.setNoticeVersionId(scheduleNotice.getNoticeVersionId());//组装当前版本信息
            //数据入库
            disclosureNoticeValueDao.addNoticeGridFBassetHoldingVal(gridValue);
        }

        //读取投资关联配置数据
        List<DisclosureGridValue> gridValueListAffiliate = disclosureNoticeValueDao.findGridAffiliateVal(disclosuregridValue);
        for (DisclosureGridValue gridValue: gridValueListAffiliate ) {
            gridValue.setNoticeVersionId(scheduleNotice.getNoticeVersionId());//组装当前版本信息
            //数据入库
            disclosureNoticeValueDao.addNoticeGridAffiliateVal(gridValue);
        }
    }

    @API(desc = "计算产品净值增长率(非份额)", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public String saveNetvalRate(ScheduleNotice scheduleNotice) throws Exception {
        Map<String,String> map = disclosureNoticeValueDao.getRate(scheduleNotice);
        return RequestSupport.updateReturnJson(true, "操作成功!!", null).toString();
    }

    public static void main(String[] args) {

    }
}
