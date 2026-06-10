package com.kayak.pms.email.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureChannelDao;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeValueDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeVersionDao;
import com.kayak.pms.disclosureControl.dao.ScheduleNoticeDao;
import com.kayak.pms.disclosureControl.dao.T8DisclosureNoticeChannelDao;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeChannel;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import com.kayak.pms.disclosureControl.model.ScheduleNotice;
import com.kayak.pms.disclosureControl.model.T8DisclosureNoticeChannel;
import com.kayak.pms.email.dao.M8DisclosureManualDao;
import com.kayak.pms.email.dao.T8DisChannelInfoDao;
import com.kayak.pms.email.model.M8DisclosureManual;
import com.kayak.pms.email.model.T8DisChannelInfo;
import com.kayak.utils.DateHelper;
import com.kayak.utils.FileUtil;
import com.kayak.pms.global.constants.DisclosureStatus;
import com.kayak.pms.global.constants.IsDocking;
import com.kayak.pms.global.constants.XpStatus;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@APIDefine(desc = "手工公告配置", model = M8DisclosureManual.class)
public class M8DisclosureManualService {

    private static Logger logger = LoggerFactory.getLogger(M8DisclosureManualService.class);
    private static final String separate = "/";

    @Autowired
    private M8DisclosureManualDao m8DisclosureManualDao;

    @Autowired
    private T8DisclosureNoticeChannelDao t8DisclosureNoticeChannelDao;

    @Autowired
    private ScheduleNoticeDao scheduleNoticeDao;

    @Autowired
    private DisclosureChannelDao disclosureChannelDao;

    @Autowired
    private DisclosureNoticeVersionDao disclosureNoticeVersionDao;

    @Autowired
    private DisclosureNoticeValueDao disclosureNoticeValueDao;

    @Autowired
    private WordToPdfUtil wordToPdfUtil;

    @API(desc = "手工公告配置页面新增公告",auth = APIAuth.YES,operation = APIOperation.INSTER)
    public String insertManualInfo(SqlParam<M8DisclosureManual> param) throws Exception {
        ScheduleNotice scheduleNotice = new ScheduleNotice();
        scheduleNotice.setProdSerCd(param.getModel().getProdSerCd());
        scheduleNotice.setReviewStatus("4");
        scheduleNotice.setDisclosureStatus("2");
        scheduleNotice.setEffectStatus("1");
        scheduleNotice.setIsManualNotice("1");
        scheduleNotice.setDisclosureType("11");
        scheduleNotice.setDisclosureSonType("0101");
        scheduleNotice.setNoticeTitle(param.getModel().getTitle());
        scheduleNotice.setProdName(param.getModel().getProdName1());
        scheduleNotice.setPlanFbDate(param.getModel().getPlanFbDate());
        scheduleNotice.setProdCode(param.getModel().getProdCode());
        scheduleNotice.setCrtUserName(param.getModel().getCrtUser());
        scheduleNotice.setCrtUserId(param.getModel().getCrtUserId());
        String crtDate = param.getModel().getCrtDate().replaceAll("-","").replaceAll(" +","").replaceAll(":","");
        scheduleNotice.setCrtDate(crtDate.substring(0,8));
        scheduleNotice.setCrtTime(crtDate.substring(8));
        scheduleNotice.setChannelList(param.getModel().getChannelList());
        scheduleNotice.setFileList(param.getModel().getFileList());
        /**
         * 校验任务是否存在已生成的公告
         */
        //手工公告相同数据可以插入多次，产生多条记录
        //SqlRow notice = t8DisclosureNoticeChannelDao.checkExistNoticeId(scheduleNotice);
        //String noticeId = notice!=null? notice.getString("notice_id"):"";
        String noticeId = "";
        //更新公告的前提是存在原有公告，若不存在又想更新则不做操作；若的确存在基准日期、信披类型符合任务的公告，请检查产品代码、产品参数是否符合
        if (Strings.isNotBlank(noticeId)){
            scheduleNotice.setId(noticeId);
            log.info(" 该公告执行更新{}", JSONObject.toJSONString(scheduleNotice));
            scheduleNoticeDao.updT8DisclosureNotice(scheduleNotice);
        }else {
            log.info(" 生成新公告{}",JSONObject.toJSONString(scheduleNotice));
            scheduleNotice.setId(scheduleNoticeDao.addT8DisclosureNotice(scheduleNotice));

        }
        if (Strings.isNotBlank(scheduleNotice.getId())){
            this.disclosureNoticeVCV(scheduleNotice);
        }
       //return m8DisclosureManualDao.insertManualInfo(m8DisclosureManual);
        return RequestSupport.updateReturnJson(true, "操作成功!!", null).toString();
    }

    public void disclosureNoticeVCV(ScheduleNotice scheduleNotice) throws Exception {
        /**
         * 顺序执行：插入公告->插入版本->插入渠道->插入取值字段信息
         */
        log.info(">>>>>>>>>>>>>>>>公告版本（公告id："+scheduleNotice.getId()+"）数据生成start>>>>>>>>>>>>>>>>");
        //插入公告版本信息
        addManualNoticeVersion(scheduleNotice);
        //插入公告渠道信息
        addManualNoticeChannels(scheduleNotice);
        //插入公告取值信息
        //initNoticeValue(setMapProperties(scheduleNotice),scheduleNotice);
        log.info(">>>>>>>>>>>>>>>>公告版本（公告id："+scheduleNotice.getId()+"）数据生成end>>>>>>>>>>>>>>>>");
    }

    private void addManualNoticeChannels(ScheduleNotice scheduleNotice) throws Exception{
        String channelStr = scheduleNotice.getChannelList();
        List<T8DisclosureNoticeChannel> channelList = JSONObject.parseArray(channelStr, T8DisclosureNoticeChannel.class);
        for (T8DisclosureNoticeChannel channel : channelList) {
            channel.setDisclosureNoticeId(scheduleNotice.getId());
            channel.setDisclosureType(scheduleNotice.getDisclosureType());
            channel.setDisclosureNoticeChannelId(channel.getId());
            channel.setCreateTime(scheduleNotice.getCrtTime());
            channel.setCreateDate(scheduleNotice.getCrtDate());
            channel.setCreateUserId(scheduleNotice.getUserId());
            channel.setCreateUserName(scheduleNotice.getCrtUserName());
            channel.setNoticeChannelPublicStatus(DisclosureStatus.waitPub.getItemKey());

            String noticeChannelId = t8DisclosureNoticeChannelDao.addT8DisclosureNoticeChannel(channel);

        }
    }
    private void addManualNoticeVersion(ScheduleNotice scheduleNotice ) throws Exception {
        String path=System.getProperty("os.name").toLowerCase().startsWith("win")?
                SysUtil.getSystemParamsByParaid("80000080002"):
                SysUtil.getSystemParamsByParaid("80000080003");
        String fileStr = scheduleNotice.getFileList();
        List<DisclosureNoticeVersion> fileList = JSONObject.parseArray(fileStr, DisclosureNoticeVersion.class);
        DisclosureNoticeVersion version = fileList.get(0);
        /**组装公告数据及基本数信息*/
        DisclosureNoticeVersion noticeVersion = new  DisclosureNoticeVersion();
        noticeVersion.setT8DisclosureNoticeId(scheduleNotice.getId());
        //手工公告需要保留文件后缀，不一定是docx，可能是pdf
        noticeVersion.setFileName(version.getFileName());
        noticeVersion.setNoticeVersion("V1.0");
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

        String versionId=disclosureNoticeVersionDao.addT8DisclosureNoticeVersion(noticeVersion);
        String fileStorePath= path+separate+versionId+separate;
        File file2 = new File(fileStorePath);
        boolean newJavaFile = file2.mkdir();
        File localFile = new File(file2+separate+version.getFileName());
        File originFile = new File(version.getFilePath());
        FileUtil.fileCopyOne(originFile,localFile);
        //转存文件
        //originFile.transferTo(localFile);
        noticeVersion.setFilePath(fileStorePath);
        noticeVersion.setCrtPath(fileStorePath);
        noticeVersion.setId(versionId);
        //保存公告版本路径
        disclosureNoticeVersionDao.updT8DisclosureNoticeVersion(noticeVersion);
        scheduleNotice.setNoticeVersionId(versionId);
    }

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

    @API(desc = "文件预览", auth = APIAuth.NO, operation = APIOperation.INSTER)
    public String  previewXP(SqlParam<M8DisclosureManual> sqlParam) throws Exception {
        Map<String, Object> returndata = new HashMap<String,Object>();
        String fileName = sqlParam.getModel().getFileName();
        String filePath = sqlParam.getModel().getFilePath();
        if(!"pdf".equals(fileName.split("\\.")[1])) {
            fileName = fileName.split("\\.")[0] + ".pdf";
            String targetPath = new File(filePath).getParent() + File.separator + fileName;
            wordToPdfUtil.doc2pdf(filePath, targetPath);
            returndata.put("viewUrl", targetPath);
        }else {
            returndata.put("viewUrl", filePath);
        }


        return RequestSupport.updateReturnJson(true,"操作成功",returndata).toString();
    }
}
