package com.kayak.pms.disclosureControl.service;

import cn.hutool.core.collection.CollectionUtil;
import com.aspose.words.*;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.T85.service.SysDictItemService;
import com.kayak.pms.basePublish.dao.DisclosureChannelRuleDao;
import com.kayak.pms.basePublish.dao.DisclosureRuleDao;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.basePublish.service.DisclosureModVersionService;
import com.kayak.pms.connect.utils.ZipUtil;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeChannelDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDocDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeProcessDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeVersionDao;
import com.kayak.pms.disclosureControl.model.DisclosureNotice;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeChannel;
import com.kayak.pms.global.constants.DisclosureStatus;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import com.kayak.utils.OnlineUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

@Service
@APIDefine(desc = "公告详情渠道信息表服务", model = DisclosureNoticeChannel.class)
public class DisclosureNoticeChannelService {

    private static final Logger logger = LoggerFactory.getLogger(DisclosureNoticeChannelService.class);
    //文件分隔符
    private static final String separate = "/";

    @Autowired
    private DisclosureNoticeChannelDao disclosureNoticeChannelDao;

    @Autowired
    private DisclosureRuleDao disclosureRuleDao;

    @Autowired
    private DisclosureModVersionService disclosureModVersionService;

    @Autowired
    private DisclosureNoticeService disclosureNoticeService;
    
    @Autowired
    private DisclosureNoticeVersionDao disclosureNoticeVersionDao;

    @Autowired
    private DisclosureNoticeDao disclosureNoticeDao;

    private String fileStorePath;

    @Value("${path.word}")
    private String winPath;

    @Autowired
    private DisclosureNoticeProcessDao disclosureNoticeProcessDao;

    @Autowired
    private SysDictItemService sysDictItemService;

    @Autowired
    private WordToPdfUtil wordToPdfUtil;

    @Autowired
    private DisclosureNoticeDocDao disclosureNoticeDocDao;

    @Autowired
    private DisclosureChannelRuleDao disclosureChannelRuleDao;

    @API(desc = "查询公告详情渠道信息表信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureNoticeChannel> findDisclosureNoticeChannels(SqlParam<DisclosureNoticeChannel> params) throws Exception {
        params.setMakeSql(true);
        return disclosureNoticeChannelDao.findDisclosureNoticeChannels(params);
    }

    @API(desc = "查询公告详情渠道详情", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureNoticeChannel> findDisclosureNoticeChannelsInfo(SqlParam<DisclosureNoticeChannel> params) throws Exception {
//        params.setMakeSql(true);
        return disclosureNoticeChannelDao.findDisclosureNoticeChannelsInfo(params);
    }

    @API(desc = "查询公告详情渠道详情", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureNoticeChannel> findDisclosureManualNoticeChannelsInfo(SqlParam<DisclosureNoticeChannel> params) throws Exception {
//        params.setMakeSql(true);
        return disclosureNoticeChannelDao.findDisclosureManualNoticeChannelsInfo(params);
    }
    /**
     * @Description: 发行公告到期公告 doc转pdf
     * @Param: [disclosureNotice]
     * @return: java.lang.String
     * @Author: XIEZEDONG🐼
     * @Date: 2021/8/30
     */
    public String issuedDocToPdf(DisclosureNotice disclosureNotice) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日");
        String separator = File.separator;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", disclosureNotice.getId());
            params.put("prodCode", disclosureNotice.getProdCode());
            String id = disclosureNoticeDao.findVersionId(params);
            String newFilePath = OnlineUtils.getOnlinepath(winPath) + "xpTemp" + separator + id + separator;
            //根据类型拼接不同的模版信息
            List<SqlRow> sqlRowList = disclosureNoticeDao.findIssuedInfo(params);
            String fileName = disclosureNotice.getFileName();

            try {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(fileName)) {
                    wordToPdfUtil.getLicense();
                    com.aspose.words.Document document = new com.aspose.words.Document(newFilePath + fileName);
                    // 获得要替换的word模板
                    Range range = document.getRange();// range获取word中的内容
                    if (CollectionUtil.isNotEmpty(sqlRowList)) {
                        for (SqlRow sqlRow : sqlRowList) {
                            Iterator<Map.Entry<String, Object>> it = sqlRow.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, Object> entry = it.next();
                                //System.out.println("key:" + entry.getKey() + " val:" + entry.getValue());

                                //word字段单独处理
                                range.replace("${other}", "", true, false);
                                range.replace("${nowdate}", format.format(new Date()), true, false);
                                String establish_date = "";
                                if (entry.getKey().equals("establish_date")) {
                                    if (entry.getValue() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(entry.getValue().toString())) {
                                        String date = entry.getValue().toString();
                                        //将日期分割20210817
                                        String year = date.substring(0, 4);
                                        String month = date.substring(4, 6);
                                        String day = date.substring(6, 8);
                                        establish_date += year + "年" + month + "月" + day + "日";
                                        range.replace("${establish_date}", establish_date, true, false);
                                    }

                                }
                                if (entry.getKey().equals("raise_type")) {
                                    if (entry.getValue() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(entry.getValue().toString())) {
                                        String value = sysDictItemService.findDictValueByKey(entry.getValue().toString(), "xp_raise_type");
                                        range.replace("${raise_type}", value, true, false);
                                    }

                                }

                                if (entry.getKey().equals("prod_classify")) {
                                    if (entry.getValue() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(entry.getValue().toString())) {
                                        String value = sysDictItemService.findDictValueByKey(entry.getValue().toString(), "t8_prod_classify");
                                        range.replace("${prod_classify}", value, true, false);
                                    }

                                }
                                //风险
                                if (entry.getKey().equals("prod_risk_level")) {
                                    if (entry.getValue() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(entry.getValue().toString())) {
                                        String value = sysDictItemService.findDictValueByKey(entry.getValue().toString(), "cust_risk_g");
                                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                                            range.replace("${prod_risk_level}", value, true, false);
                                        }
                                    }

                                }

                                //募集日期处理
                                String apply_start_date = "";
                                if (entry.getKey().equals("apply_start_date")) {
                                    if (entry.getValue() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(entry.getValue().toString())) {
                                        String date = entry.getValue().toString();
                                        //将日期分割20210817
                                        String year = date.substring(0, 4);
                                        String month = date.substring(4, 6);
                                        String day = date.substring(6, 8);
                                        apply_start_date += year + "年" + month + "月" + day + "日";
                                        range.replace("${apply_start_date}", apply_start_date, true, false);
                                    }

                                }


                                //募集日期处理
                                String apply_end_date = "";
                                if (entry.getKey().equals("apply_end_date")) {
                                    if (entry.getValue() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(entry.getValue().toString())) {
                                        String date = entry.getValue().toString();
                                        //将日期分割20210817
                                        String year = date.substring(0, 4);
                                        String month = date.substring(4, 6);
                                        String day = date.substring(6, 8);
                                        apply_end_date += year + "年" + month + "月" + day + "日";
                                        range.replace("${apply_end_date}", apply_end_date, true, false);
                                    }

                                }
                                //计划终止日期
                                String end_date = "";
                                if (entry.getKey().equals("end_date")) {
                                    if (entry.getValue() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(entry.getValue().toString())) {
                                        String date = entry.getValue().toString();
                                        //将日期分割20210817
                                        String year = date.substring(0, 4);
                                        String month = date.substring(4, 6);
                                        String day = date.substring(6, 8);
                                        end_date += year + "年" + month + "月" + day + "日";
                                        range.replace("${end_date}", end_date, true, false);
                                    }

                                }

                                if (entry.getValue() == null) {
                                    range.replace("${" + entry.getKey() + "}", "", true, false);
                                } else {
                                    String value = entry.getValue().toString();
                                    range.replace("${" + entry.getKey() + "}", value, true, false);
                                }

                            }
                        }
                    }
                    //创建临时存放目录
                    File localPathFile2 = new File(newFilePath + separator + "temp2");
                    if (!localPathFile2.exists() && !localPathFile2.isDirectory()) {
                        localPathFile2.mkdirs();
                    }
                    String docPath = newFilePath + separator + "temp2" + separator + disclosureNotice.getNoticeTitle() + ".docx";
                    document.save(docPath, SaveFormat.DOCX);
                    String outPath = docPath.substring(0, docPath.lastIndexOf(".")) + ".pdf";
                    wordToPdfUtil.doc2pdf(docPath, outPath);
                    return outPath;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @API(desc = "添加公告详情渠道信息表", params = "id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name", auth = APIAuth.NO)
    public String addDisclosureNoticeChannel(SqlParam<DisclosureNoticeChannel> params) throws Exception {
        params.getModel().setNoticeChannelPublicStatus(DisclosureStatus.waitPub.getItemKey());
        if (params.getModel().getDisclosureNoticeChannelId() == null) {
            return RequestSupport.updateReturnJson(false, "请选择渠道!", null).toString();
        }
        //添加的时候 相同的id只能选择一次
        boolean flag = disclosureNoticeChannelDao.cheackHCommonId(params.getModel().getDisclosureNoticeId(), params.getModel().getDisclosureNoticeChannelId());
        if (flag) {
            //如果有相同的就添加失败
            return RequestSupport.updateReturnJson(false, "所选择发布渠道已存在!", null).toString();
        }
        //根据传进来的公告id和渠道id组装新增数据信息,查询公告信息，已经发布的公告不可以增加渠道
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("id",params.getModel().getDisclosureNoticeId());
        SqlParam<DisclosureNotice> disclosureNoticeSqlParam = new FetcherData(queryParam,DisclosureNotice.class);
        SqlResult<DisclosureNotice> result = disclosureNoticeDao.findDisclosureNotices(disclosureNoticeSqlParam);
        String disclosureStatus = result.getRows().get(0).getDisclosureStatus();
        if(DisclosureStatus.overDown.getItemKey().equals(disclosureStatus)){
            return RequestSupport.updateReturnJson(false, "公告已生成不能新增渠道!", null).toString();
        }
        if(DisclosureStatus.overSend.getItemKey().equals(disclosureStatus)){
            return RequestSupport.updateReturnJson(false, "公告已发布成功不能新增渠道!", null).toString();
        }
        //如果渠道配置里没有这个类型的公告配置也不能添加
        Map<String, Object> channelRuleQueryParam = new HashMap<>();
        channelRuleQueryParam.put("disclosureType",result.getRows().get(0).getDisclosureType());
        SqlParam<DisclosureChannelRule> channelRuleSqlParam = new FetcherData(channelRuleQueryParam,DisclosureChannelRule.class);
        SqlResult<DisclosureChannelRule> channelRule = disclosureChannelRuleDao.findChannelRule(channelRuleSqlParam);
        List<DisclosureChannelRule> rows = channelRule.getRows();
        String disclosureNoticeChannelId = params.getModel().getDisclosureNoticeChannelId();
        Boolean hasRule = false;
        for (DisclosureChannelRule d:rows) {//如果查到的渠道规则里包含添加的渠道则可以添加
            if(disclosureNoticeChannelId.equals(d.getChannelIds())){
                hasRule =true;
            }
        }
        if(!hasRule){
            return RequestSupport.updateReturnJson(false, "该类型不能添加此渠道,请配置!", null).toString();
        }
        //获取公告最大版本号
        String notice_version_id = disclosureNoticeDocDao.getNoticeLatestVersionId(params.getModel().getDisclosureNoticeId());
        params.getModel().setNoticeVersionId(notice_version_id);
        params.getModel().setChannelPublicDate( result.getRows().get(0).getProdBaseDate());
        disclosureNoticeChannelDao.addDisclosureNoticeChannel(params).getEffect();
        return RequestSupport.updateReturnJson(true, "操作成功!", null).toString();
    }

    @API(desc = "手工公告添加渠道信息", params = "id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name", auth = APIAuth.NO)
    public String addManualNoticeChannel(SqlParam<DisclosureNoticeChannel> params) throws Exception {
        params.getModel().setNoticeChannelPublicStatus(DisclosureStatus.waitPub.getItemKey());
        if (params.getModel().getDisclosureNoticeChannelId() == null) {
            return RequestSupport.updateReturnJson(false, "请选择渠道!", null).toString();
        }
        //添加的时候 相同的id只能选择一次
        boolean flag = disclosureNoticeChannelDao.cheackHCommonId(params.getModel().getDisclosureNoticeId(), params.getModel().getDisclosureNoticeChannelId());
        if (flag) {
            //如果有相同的就添加失败
            return RequestSupport.updateReturnJson(false, "所选择发布渠道已存在!", null).toString();
        }
        //根据传进来的公告id和渠道id组装新增数据信息,查询公告信息，已经发布的公告不可以增加渠道
        Map<String, Object> queryParam = new HashMap<>();
        if(Tools.isNotEmpty(params.getModel().getDisclosureNoticeId())){
            queryParam.put("id",params.getModel().getDisclosureNoticeId());
            SqlParam<DisclosureNotice> disclosureNoticeSqlParam = new FetcherData(queryParam,DisclosureNotice.class);
            SqlResult<DisclosureNotice> result = disclosureNoticeDao.findDisclosureNotices(disclosureNoticeSqlParam);
            String disclosureStatus = result.getRows().get(0).getDisclosureStatus();
            if(DisclosureStatus.overDown.getItemKey().equals(disclosureStatus)){
                return RequestSupport.updateReturnJson(false, "公告已生成不能新增渠道!", null).toString();
            }
            if(DisclosureStatus.overSend.getItemKey().equals(disclosureStatus)){
                return RequestSupport.updateReturnJson(false, "公告已发布成功不能新增渠道!", null).toString();
            }
            //如果渠道配置里没有这个类型的公告配置也不能添加
            Map<String, Object> channelRuleQueryParam = new HashMap<>();
            channelRuleQueryParam.put("disclosureType",result.getRows().get(0).getDisclosureType());
            SqlParam<DisclosureChannelRule> channelRuleSqlParam = new FetcherData(channelRuleQueryParam,DisclosureChannelRule.class);
            SqlResult<DisclosureChannelRule> channelRule = disclosureChannelRuleDao.findChannelRule(channelRuleSqlParam);
            List<DisclosureChannelRule> rows = channelRule.getRows();
            String disclosureNoticeChannelId = params.getModel().getDisclosureNoticeChannelId();
            Boolean hasRule = false;
            for (DisclosureChannelRule d:rows) {//如果查到的渠道规则里包含添加的渠道则可以添加
                if(disclosureNoticeChannelId.equals(d.getChannelIds())){
                    hasRule =true;
                }
            }
            if(!hasRule){
                return RequestSupport.updateReturnJson(false, "该类型不能添加此渠道,请配置!", null).toString();
            }
            params.getModel().setChannelPublicDate( result.getRows().get(0).getProdBaseDate());
        }

        //获取公告最大版本号
        //String notice_version_id = disclosureNoticeDocDao.getNoticeLatestVersionId(params.getModel().getDisclosureNoticeId());
        params.getModel().setNoticeVersionId("");

        disclosureNoticeChannelDao.addDisclosureNoticeChannel(params).getEffect();
        return RequestSupport.updateReturnJson(true, "操作成功!", null).toString();
    }

    @API(desc = "修改公告详情渠道信息表", params = "id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name", auth = APIAuth.NO)
    public int updateDisclosureNoticeChannel(SqlParam<DisclosureNoticeChannel> params) throws Exception {
        return disclosureNoticeChannelDao.updateDisclosureNoticeChannel(params).getEffect();
    }

    @API(desc = "删除公告详情渠道信息表", params = "id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name", auth = APIAuth.NO)
    public int deleteDisclosureNoticeChannel(SqlParam<DisclosureNoticeChannel> params) throws Exception {
        return disclosureNoticeChannelDao.deleteDisclosureNoticeChannel(params).getEffect();
    }
    /**
     * 查询净值整体信息的产品净值信息
     *
     */
    @API(desc = "查询公告详情产品净值", auth = APIAuth.NO,params = "id")
    public SqlResult queryProdNav(SqlParam<DisclosureNoticeChannel> params) {
        List<SqlRow> objects = new ArrayList<>();
        SqlResult result = new SqlResult();
        try{
            Object disclosureNoticeVersionId = params.getParams().get("noticeVersionId");
            List<Map<String,Object>> configParamsList = disclosureNoticeDocDao.getMaxRowColumn(String.valueOf(disclosureNoticeVersionId));
            for (Map<String, Object> configParams : configParamsList) {
                if(params.getParams().get("modName").toString().contains(String.valueOf(configParams.get("remark")))){
                    configParams.put("noticeVersionId",disclosureNoticeVersionId);
                    List<SqlRow> dataResList = disclosureNoticeDocDao.getFillingValueDataByExeId(String.valueOf(configParams.get("exe_id")), configParams);
                    objects.addAll(dataResList);
                }
            }
            result.setRows(objects);
            result.setResults(objects.size());
            return result;
        }catch (Exception e){
            logger.info("公告详情净值信息查询失败");
        }
        return result;
    }

    public String splitFile(String channelPath, int i,int mb,String savePath) throws IOException {
        File file1 = new File(channelPath);
        long countSize = file1.length();
        long fileSize = 1024 * 1024 * mb;
        int num = 0;
        if (countSize % fileSize == 0) {
            num = (int) (countSize / fileSize);
        } else {
            num = (int) (countSize / fileSize) + 1;
        }
        int k =0;
        for (; k < num; k++) {
            String[] split = file1.getName().split(".zip");
            File file = new File(savePath + File.separator + split[0] + "-" + k);
        }

        //Users/java/test/文档/channel/时间/id
        //Users/java/test/文档/channel/时间

        File[] list = file1.listFiles();
        for (int j = 0; j < list.length; j++) {
            File oldfile = list[0];
            String name = oldfile.getName();

            String max = channelPath + File.separator + i + File.separator + name;
            //文件输入流，用于读取要复制的文件
            FileInputStream fileInputStream = new FileInputStream(oldfile);
            //要生成的新文件（指定路径如果没有则创建）
            File newfile = new File(max);
            //获取父目录
            File fileParent = newfile.getParentFile();
            System.out.println(fileParent);
            //判断是否存在
            if (!fileParent.exists()) {
                // 创建父目录文件夹
                fileParent.mkdirs();
            }
            //判断文件是否存在
            if (!newfile.exists()) {
                //创建文件
                newfile.createNewFile();
            }

            //新文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(newfile);
            byte[] buffer = new byte[1024];
            int len;
            //将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
                fileOutputStream.flush();
            }
            fileInputStream.close();
            fileOutputStream.close();
            oldfile.delete();

            String zipSavePath = channelPath + i + ".zip";
            File zipSave = new File(zipSavePath);
            ZipUtil.zipCompress(zipSavePath, new File(channelPath));
            long fileMax = 1024 * 1024 * 15;
            if (zipSave.length() > fileMax) {
                zipSave.delete();
                //splitFile(channelPath, i);
            }
            String splitSavePath = channelPath + File.separator + i + i + ".zip";
            ZipUtil.zipCompress(splitSavePath, new File(channelPath + File.separator + i));
            if (channelPath.length() > 15) {
                //splitFile(channelPath, i);
            }
            i++;
        }
        return "";
    }

    // 拆分文件
    public   static   void  splitFile(File file, int  size)  throws  Exception {
        if(size<=0){
            size = 1024;
        }
        if(!file.isFile()){
            throw new Exception("file not exists"+file.getAbsolutePath());
        }
        String filename = file.getAbsolutePath();
        File filetmp = new File(filename+"_"+0+".vk");
        if(filetmp.isFile()){
            throw new Exception("file exists"+filetmp.getAbsolutePath());
        }

        byte[] buf = new byte[1024*10];
        FileInputStream fis = new FileInputStream(file);
        int readsize = 0;
        int pos = 0;
        int k = 0;
        int m = -1;
        File fileout = null;
        FileOutputStream fos = null;
        while((readsize = fis.read(buf, 0, buf.length))>0){

            if(k!=m)
            {
                if(fos!=null){
                    fos.close();
                    fos = null;
                }
                m = k;
                fileout = new File(filename+"_"+k+".vk");
                fos = new FileOutputStream(fileout);
            }
            fos.write(buf,0,readsize);
            fos.flush();
            pos += readsize;
            if(pos>size*(k+1)){
                k++;
            }
        }
        if(fos!=null){
            fos.close();
            fos = null;
        }
        fis.close();
    }

    // 合并文件
    public   static   void  combination(File file)  throws  Exception {
        String filename = file.getAbsolutePath();
        File fileout = new File(filename);

        if(fileout.isFile()){
            throw new Exception("file exists"+fileout.getAbsolutePath());
        }
        FileOutputStream fos = new FileOutputStream(fileout);
        int k = 0;
        File filein = null;
        FileInputStream fis = null;
        byte[] buf = new byte[1024*10];
        while(true){
            if(fis!=null){
                fis.close();
                fis = null;
            }
            filein = new File(filename+"_"+k+".vk");
            if(!filein.isFile()){
                break;
            }
            fis = new FileInputStream(filein);
            int readsize = 0;
            while((readsize = fis.read(buf, 0, buf.length))>0){
                fos.write(buf,0,readsize);
                fos.flush();
            }
            k++;
        }
        if(fis!=null){
            fis.close();
            fis = null;
        }
        fos.close();
    }

    //份额分类模板处理
    private com.aspose.words.Document isShareSort(com.aspose.words.Document document, DisclosureNotice disclosureNotice,String fileName,Map<String,Object> infoMap) throws Exception {
        //所有模板中产品概况循环添加下属份额分类
        List<SqlRow> sqlRows = disclosureNoticeDao.findDisclosureRegularShareSort(disclosureNotice.getProdCode(),disclosureNotice.getProdBaseDate(),disclosureNotice.getId());
        Table table1 = (Table) document.getChild(NodeType.TABLE, 0, true);
        Table table2 = null;
        Table table3 = null;
        Table table7 = null;

        //产品净值表现
        if (!fileName.contains("单一资产") && !fileName.contains("现金") && !fileName.contains("私募封闭")) {
            table2 = (Table) document.getChild(NodeType.TABLE, 1, true);
        }
        //财务指标
        if (fileName.contains("现金")) {
            table3 = (Table) document.getChild(NodeType.TABLE, 1, true);
        } else if (fileName.contains("公募") || fileName.contains("私募开放")) {
            table3 = (Table) document.getChild(NodeType.TABLE, 2, true);
        }
        //开放式份额信息
        if (fileName.contains("公募开放非固收") || fileName.contains("私募开放非固收")) {
            table7 = (Table) document.getChild(NodeType.TABLE, 5, true);
        } else if (fileName.contains("公募开放固收") || fileName.contains("私募开放固收")) {
            table7 = (Table) document.getChild(NodeType.TABLE, 6, true);
        } else if (fileName.contains("现金")) {
            table7 = (Table) document.getChild(NodeType.TABLE, 4, true);
        }

        if (sqlRows != null && sqlRows.size() > 0) {
            for (Map map1 : sqlRows) {
                Node deepClone1 = table1.getLastRow().deepClone(true);
                Range range1 = table1.getLastRow().getRange();

                if (map1.get("sales_name") != "" && map1.get("sales_name") != null) {
                    range1.replace("${list_sale_name}", map1.get("sales_name").toString(), true, false);
                } else {
                    range1.replace("${list_sale_name}", "", true, false);
                }

                if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
                    range1.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
                } else {
                    range1.replace("${list_sale_code}", "", true, false);
                }

                if (map1.get("share_value") != "" && map1.get("share_value") != null) {
                    range1.replace("${netval}", map1.get("share_value").toString(), true, false);
                } else {
                    range1.replace("${netval}", "", true, false);
                }

                if (map1.get("share_total_value") != "" && map1.get("share_total_value") != null) {
                    range1.replace("${netval_total}", map1.get("share_total_value").toString(), true, false);
                } else {
                    range1.replace("${netval_total}", "", true, false);
                }

                if (map1.get("end_asset_value") != "" && map1.get("end_asset_value") != null) {
                    range1.replace("${netval_vol}", map1.get("end_asset_value").toString(), true, false);
                } else {
                    range1.replace("${netval_vol}", "", true, false);
                }

                table1.getRows().add(deepClone1);

                //产品净值表现
                Node deepClone2;
                Range range2;
                if (table2 != null) {
                    deepClone2 = table2.getLastRow().deepClone(true);
                    range2 = table2.getLastRow().getRange();

                    if (map1.get("sales_name") != "" && map1.get("sales_name") != null) {
                        range2.replace("${list_sale_name}", map1.get("sales_name").toString(), true, false);
                    } else {
                        range2.replace("${list_sale_name}", "", true, false);
                    }
                    if (map1.get("dur_net_growth") != "" && map1.get("dur_net_growth") != null) {
                        range2.replace("${list_report}", map1.get("dur_net_growth").toString()+"%", true, false);
                    } else {
                        range2.replace("${list_report}", "", true, false);
                    }
                    if (map1.get("sur_net_growth") != "" && map1.get("sur_net_growth") != null) {
                        range2.replace("${list_subsist}", map1.get("sur_net_growth").toString()+"%", true, false);
                    } else {
                        range2.replace("${list_subsist}", "", true, false);
                    }
                    table2.getRows().add(deepClone2);
                }

                //产品财务指标
                Node deepClone3;
                Range range3;
                if(table3 != null) {
                    deepClone3 = table3.getLastRow().deepClone(true);
                    range3 = table3.getLastRow().getRange();
                    if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
                        range3.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
                    } else {
                        range3.replace("${list_sale_code}", "", true, false);
                    }
                    if (map1.get("end_share_value") != "" && map1.get("end_share_value") != null) {
                        range3.replace("${list_netval_end}", map1.get("end_share_value").toString(), true, false);
                    } else {
                        range3.replace("${list_netval_end}", "", true, false);
                    }
                    if (map1.get("end_total_value") != "" && map1.get("end_total_value") != null) {
                        range3.replace("${list_netval_total}", map1.get("end_total_value").toString(), true, false);
                    } else {
                        range3.replace("${list_netval_total}", "", true, false);
                    }
                    if (map1.get("end_prod_share") != "" && map1.get("end_prod_share") != null) {
                        range3.replace("${list_end_vol}", map1.get("end_prod_share").toString(), true, false);
                    } else {
                        range3.replace("${list_end_vol}", "", true, false);
                    }
                    if (map1.get("end_asset_value") != "" && map1.get("end_asset_value") != null) {
                        range3.replace("${list_netval_vol}", map1.get("end_asset_value").toString(), true, false);
                    } else {
                        range3.replace("${list_netval_vol}", "", true, false);
                    }
                    if (infoMap.get("list_income") != "" && infoMap.get("list_income") != null) {
                        range3.replace("${list_income}", infoMap.get("list_income").toString(), true, false);
                    } else {
                        range3.replace("${list_income}", "", true, false);
                    }
                    if (infoMap.get("list_profit") != "" && infoMap.get("list_profit") != null) {
                        range3.replace("${list_profit}", infoMap.get("list_profit").toString(), true, false);
                    } else {
                        range3.replace("${list_profit}", "", true, false);
                    }
                    table3.getRows().add(deepClone3);
                }

                //开放式份额
                Node deepClone7;
                Range range7;
                if (table7 != null) {
                    deepClone7 = table7.getLastRow().deepClone(true);
                    range7 = table7.getLastRow().getRange();

                    if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
                        range7.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
                    } else {
                        range7.replace("${list_sale_code}", "", true, false);
                    }
                    if (map1.get("begin_total_share") != "" && map1.get("begin_total_share") != null) {
                        range7.replace("${list_begin_vol}", map1.get("begin_total_share").toString(), true, false);
                    } else {
                        range7.replace("${list_begin_vol}", "", true, false);
                    }
                    if (map1.get("dur_purch_share") != "" && map1.get("dur_purch_share") != null) {
                        range7.replace("${list_sub_vol}", map1.get("dur_purch_share").toString(), true, false);
                    } else {
                        range7.replace("${list_sub_vol}", "", true, false);
                    }
                    if (map1.get("dur_redem_share") != "" && map1.get("dur_redem_share") != null) {
                        range7.replace("${list_sub_redeem}", map1.get("dur_redem_share").toString(), true, false);
                    } else {
                        range7.replace("${list_sub_redeem}", "", true, false);
                    }
                    if (map1.get("end_total_share") != "" && map1.get("end_total_share") != null) {
                        range7.replace("${list_end_vol}", map1.get("end_total_share").toString(), true, false);
                    } else {
                        range7.replace("${list_end_vol}", "", true, false);
                    }
                    table7.getRows().add(deepClone7);
                }

            }

            if (table1 != null) {
                table1.getLastRow().remove();
                //合并资产净值单元格
                //20220406 去除单元格合并
                /*if(fileName.contains("单一资产")) {
                    Cell cellStartRange = table1.getRows().get(15).getCells().get(5); //第15行第6列
                    Cell cellEndRange = table1.getRows().get(15+sqlRows.size() - 1).getCells().get(5); //第n行第6列
                    mergeCells(cellStartRange, cellEndRange);
                }
                if (fileName.contains("私募封闭")) {
                    Cell cellStartRange = table1.getRows().get(14).getCells().get(5); //第14行第6列
                    Cell cellEndRange = table1.getRows().get(14+sqlRows.size() - 1).getCells().get(5); //第n行第6列
                    mergeCells(cellStartRange, cellEndRange);
                }*/
            }
            if (table2 !=null)
                table2.getLastRow().remove();
            if (table3 != null) {
                table3.getLastRow().remove();
                //合并最后两列单元格
                Cell cellStartRange1 = table3.getRows().get(3).getCells().get(5); //第3行第6列
                Cell cellEndRange1 = table3.getRows().get(3+sqlRows.size() - 1).getCells().get(5); //第n行第6列
                mergeCells(cellStartRange1, cellEndRange1);

                Cell cellStartRange2 = table3.getRows().get(3).getCells().get(6); //第3行第7列
                Cell cellEndRange2 = table3.getRows().get(3+sqlRows.size() - 1).getCells().get(6); //第n行第7列
                mergeCells(cellStartRange2, cellEndRange2);

            }
            if (table7 != null) {
                table7.getLastRow().remove();
            }


        } else {
            if(table1 != null) {
                Node deepClone1 = table1.getLastRow().deepClone(true);
                Range range1 = table1.getLastRow().getRange();
                range1.replace("${list_sale_name}", "", true, false);
                range1.replace("${list_sale_code}", "", true, false);
                range1.replace("${netval}", "", true, false);
                range1.replace("${netval_total}", "", true, false);
                range1.replace("${netval_vol}", "", true, false);
                table1.getRows().add(deepClone1);
                table1.getLastRow().remove();
            }
            if (table2 != null) {
                Node deepClone2 = table2.getLastRow().deepClone(true);
                Range range2 = table2.getLastRow().getRange();
                range2.replace("${list_sale_name}", "", true, false);
                range2.replace("${list_report}", "", true, false);
                range2.replace("${list_subsist}", "", true, false);
                table2.getRows().add(deepClone2);
                table2.getLastRow().remove();
            }

            if (table3 != null) {
                Node deepClone3 = table3.getLastRow().deepClone(true);
                Range range3 = table3.getLastRow().getRange();
                range3.replace("${list_sale_code}", "", true, false);
                range3.replace("${list_netval_end}", "", true, false);
                range3.replace("${list_netval_total}", "", true, false);
                range3.replace("${list_end_vol}", "", true, false);
                range3.replace("${list_netval_vol}", "", true, false);
                range3.replace("${list_income}", "", true, false);
                range3.replace("${list_profit}", "", true, false);
                table3.getRows().add(deepClone3);
                table3.getLastRow().remove();
            }
            if (table7 != null) {
                Node deepClone7 = table7.getLastRow().deepClone(true);
                Range range7 = table7.getLastRow().getRange();
                range7.replace("${list_sale_code}", "", true, false);
                range7.replace("${list_begin_vol}", "", true, false);
                range7.replace("${list_sub_vol}", "", true, false);
                range7.replace("${list_sub_redeem}", "", true, false);
                range7.replace("${list_end_vol}", "", true, false);
                table7.getRows().add(deepClone7);
                table7.getLastRow().remove();
            }

        }
        return document;
    }

    /**
     * @Description 合并单元格
     * @Date 2021/11/30 16:08
     * @Param [startCell, endCell] 开始cell  结束cell
     * @Return void
     */
    private static void mergeCells(Cell startCell, Cell endCell) {
        Table parentTable = startCell.getParentRow().getParentTable();

        Point startCellPos = new Point(startCell.getParentRow().indexOf(startCell), parentTable.indexOf(startCell.getParentRow()));
        Point endCellPos = new Point(endCell.getParentRow().indexOf(endCell), parentTable.indexOf(endCell.getParentRow()));
        Rectangle mergeRange = new Rectangle(Math.min(startCellPos.x, endCellPos.x), Math.min(startCellPos.y, endCellPos.y), Math.abs(endCellPos.x - startCellPos.x) + 1,
                Math.abs(endCellPos.y - startCellPos.y) + 1);

        for (Row row : parentTable.getRows()) {
            for (Cell cell : row.getCells()) {
                Point currentPos = new Point(row.indexOf(cell), parentTable.indexOf(row));

                if (mergeRange.contains(currentPos)) {
                    if (currentPos.x == mergeRange.x)
                        cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);

                    if (currentPos.y == mergeRange.y)
                        cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
                }
            }
        }
    }


    public String formatStr(String str,String type) {
    	String arrays[] = null;
        String mStr=Strings.EMPTY;
        arrays = str.split(type);
    	for(int i=0;i<arrays.length;i++) {
    		if(i==0) {
    			mStr=arrays[i];
    		}else {
    			if("".equals(arrays[i])||arrays[i].length()==0)
    				continue;
    			mStr = mStr+ControlChar.LINE_BREAK+
    					ControlChar.NON_BREAKING_SPACE+
    					ControlChar.NON_BREAKING_SPACE+
    					ControlChar.NON_BREAKING_SPACE+
    					ControlChar.NON_BREAKING_SPACE+
    					ControlChar.NON_BREAKING_SPACE+
    					ControlChar.NON_BREAKING_SPACE+
    					ControlChar.NON_BREAKING_SPACE+arrays[i];	
    		}

    	}
      
        if(arrays!=null)
        	str = mStr;
        return str;
    }

    public String getReportType(String str) throws Exception {
        String reportType = str;
        List<SqlRow> accRow2 = disclosureNoticeChannelDao.findReportType(str);
        if(accRow2!=null && accRow2.size()>0){
            for(int i=0;i<accRow2.size(); i++){
                if(str.equals(accRow2.get(i).getString("itemkey"))){
                    reportType= accRow2.get(i).getString("itemval");
                }
            }
        }
        return reportType;
    }

}
