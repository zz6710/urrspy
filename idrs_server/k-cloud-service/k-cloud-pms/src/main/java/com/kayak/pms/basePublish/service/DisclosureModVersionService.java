package com.kayak.pms.basePublish.service;

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
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureModColumnDao;
import com.kayak.pms.basePublish.dao.DisclosureModVersionDao;
import com.kayak.pms.basePublish.model.DisclosureModColumn;
import com.kayak.pms.basePublish.model.DisclosureModVersion;
import com.kayak.pms.global.constants.XpStatus;
import com.kayak.utils.ObjectToMapUtils;
import com.kayak.utils.SftpUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@APIDefine(desc = "信披模板版本信息服务", model = DisclosureModVersion.class)
public class DisclosureModVersionService {

    @Autowired
    private DisclosureModVersionDao disclosureModVersionDao;

    @Autowired
    private DisclosureModColumnDao disclosureModColumnDao;

    @Autowired
    private DisclosureModService disclosureModService;


    /**
    * @功能描述:查询所有基本信息
    * @params:[params]
    * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureModVersion>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "查询信披模板版本信息信息", auth = APIAuth.NO)
    public SqlResult<DisclosureModVersion> findDisclosureModVersionsAuth(SqlParam<DisclosureModVersion> params) throws Exception {
//        params.setMakeSql(true);
        return disclosureModVersionDao.findDisclosureModVersions(params);
    }
    /**
    * @功能描述:启用模板版本
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "启用信披模板", auth = APIAuth.NO)
    public String recoverStatus(SqlParam<DisclosureModVersion> params) throws Exception {
        try {
            disclosureModVersionDao.recoverStatus(params);
            return RequestSupport.updateReturnJson(true, "成功启用", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "操作异常", null).toString();
        }
    }
    /**
    * @功能描述:停用信披模板
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "停用信披模板", auth = APIAuth.NO)
    public String stopStatus(SqlParam<DisclosureModVersion> params) throws Exception {
        try {
            Integer count = disclosureModVersionDao.findCountOfBandVersion(params);
            if (count > 0){
                return RequestSupport.updateReturnJson(false, "当前模板的版本已在“信披生成规则”中启用,请先停用对应信披生成规则", null).toString();
            }
            disclosureModVersionDao.stopStatus(params);
            return RequestSupport.updateReturnJson(true, "成功停用", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "操作异常", null).toString();
        }
    }

    @API(desc = "添加信披模板版本信息",  auth = APIAuth.NO)
    public String addDisclosureModVersion(DisclosureModVersion disclosureModVersion) throws Exception {
        return disclosureModVersionDao.addDisclosureModVersion(disclosureModVersion);
    }
    /**
    * @功能描述:模板版本最大版本号及下一版本号
    * @params:[param]
    * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureModVersion>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "获取下一版本最大版本号",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<DisclosureModVersion> getMaxXPVersion(SqlParam<DisclosureModVersion> param) throws Exception {
        DisclosureModVersion disclosureModVersion = disclosureModVersionDao.getMaxXPVersion(param.getModel().getDisclosureModId());
        String newVersion= disclosureModService.getNextVersion(disclosureModVersion.getVersion());
        disclosureModVersion.setVersion(newVersion);
        SqlResult<DisclosureModVersion> sqlResult = new SqlResult<>();
        ArrayList<DisclosureModVersion> list = new ArrayList<>();
        list.add(disclosureModVersion);
        sqlResult.setRows(list);
        sqlResult.setResults(list.size());
        sqlResult.setDesensitized(false);
        return sqlResult;
    }
    @API(desc = "上传文档子模板", auth = APIAuth.NO, operation = APIOperation.INSTER)
    public String updateDocSonVersion(DisclosureModVersion disclosureModVersion) throws Exception {
        return addDisclosureModVersion(disclosureModVersion);
    }
    /**
    * @功能描述:模板版本更新获取最大版本号
    * @params:[disclosureModId]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public String getNewestXPVersion(String disclosureModId) throws Exception {

        List<SqlRow> newestPrintTempVersion = disclosureModVersionDao.getNewestPrintTempVersion(disclosureModId);
        if(newestPrintTempVersion.size()<1)
            return null;
        return newestPrintTempVersion.get(0).getString("version");
    }

    /**
     * 查询是否有被引用的模板版本
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "删除单个版本模板", auth = APIAuth.YES)
    public String checkDisclosureMod(SqlParam<DisclosureModVersion> params) throws Exception {
        Integer count = disclosureModVersionDao.checkDisclosureModInTask(params);
        if (count>0){
            return RequestSupport.updateReturnJson(false, "当前版本已在“公告版本”中使用，无法删除", null).toString();
        }
        count = disclosureModVersionDao.checkDisclosureModInRule(params);
        if (count>0){
            return RequestSupport.updateReturnJson(false, "当前版本已在“产品信披规则”中使用，请先删除对应的产品信披规则", null).toString();
        }
        count = disclosureModVersionDao.checkDisclosureMod(params);
        if (count>0){
            return RequestSupport.updateReturnJson(false, "当前版本已在“信披生成规则”中使用，请先删除对应信披生成规则", null).toString();
        }
        deleteDisclosureModVersion(params);
        disclosureModVersionDao.deleteDisclosureModCol(params.getModel().getId());

        //删除远程模板文件夹及文件
        deleteForModVersion(params.getModel().getId());

        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    @API(desc = "删除信披模板版本", auth = APIAuth.NO)
    public int deleteDisclosureModVersion(SqlParam<DisclosureModVersion> params) throws Exception {
        return disclosureModVersionDao.deleteDisclosureModVersion(params).getEffect();
    }
    /**
    * @功能描述:信披模板版本配置字段信息
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "模板信批字段维护",auth = APIAuth.NO,operation = APIOperation.UPDATE)
    public String updateModColumns(SqlParam<DisclosureModVersion> params) throws Exception {
        JSONArray jsonObj=new JSONArray(params.getModel().getDatas());
        AtomicBoolean bo = new AtomicBoolean(true);
        AtomicBoolean br = new AtomicBoolean(true);
        String date = DateUtil.getSysWordDay();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称
        DaoUtil.doTrans(() -> {
            for (int i = 0; i < jsonObj.length(); i++) {
                Map<String, Object> param=Tools.json2map((JSONObject) jsonObj.get(i));
                DisclosureModColumn disclosureModColumn = ObjectToMapUtils.mapToEntity(param, DisclosureModColumn.class);
                String isSysvalue = disclosureModColumn.getIsSysvalue();
                String isdisplay = disclosureModColumn.getIsdisplay();
                String roleids = disclosureModColumn.getRoleids();
                String userId = disclosureModColumn.getUserId();
                if ("".equals(isSysvalue)||isSysvalue==null||"".equals(isdisplay)||isdisplay==null){
                    bo.set(false);
                    break;
                }
                if ("2".equals(disclosureModColumn.getIsSysvalue())){
                    if ("".equals(roleids)||roleids==null){
                        br.set(false);
                        break;
                    }
                }
                disclosureModColumn.setUpdDate(date);
                disclosureModColumn.setUpdTime(time);
                disclosureModColumn.setUpdUserId(userid);
                disclosureModColumn.setUpdUserName(username);
                disclosureModColumnDao.updateDisclosureModColumn(disclosureModColumn);
            }
        });
        if (!bo.get()){
            return RequestSupport.updateReturnJson(false,"是否显示和取值类型不可为空",null).toString();
        }
        if (!br.get()){
            return RequestSupport.updateReturnJson(false,"手工维护需选择负责角色，不可为空",null).toString();
        }
        return RequestSupport.updateReturnJson(true,"保存成功",null).toString();
    }


    @API(desc = "根据id获取文档模板信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public DisclosureModVersion getPrintXPVersionById(String id) throws Exception {
        return disclosureModVersionDao.getPrintXPVersionById(id);
    }
    /**
    * @功能描述:版本号0.1递增方法
    * @params:[version]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public String preVersion(String version) throws Exception {
        double tempVersion = Double.parseDouble(version.substring(1,version.length()));
        if(StringUtils.isNotBlank(version)){
            BigDecimal num = new BigDecimal("0.1");
            BigDecimal old = new BigDecimal(version.substring(1));
            BigDecimal nw = old.subtract(num);
            version = "V" + nw;
            return version;
        }
        return "V1.0";
    }

    /**
     * @功能描述:模板删除实现
     * @return:void
     * @Athor:crh
     * @date:2023/4/017
     */
    protected void deleteForModVersion(String directory) throws Exception {
        String s = "";
        String remotePath = "";
        String lineIp = "";
        String sftpUserName = "";
        String sftpUserPwd = "";
        /**指定模板上传文件服务器的ip、用户名、用户密码*/
        //远端服务器ip地址
        s="70000010004";
        lineIp = SysUtil.getSystemParamsByParaid(s);
        //远端服务器用户名
        s="70000010005";
        sftpUserName = SysUtil.getSystemParamsByParaid(s);
        //远端服务器密码
        s="70000010006";
        sftpUserPwd = SysUtil.getSystemParamsByParaid(s);
        //远端sftp文件服务器根路径
        remotePath = SysUtil.getSystemParamsByParaid("70000010003");
        String finalLineIp = lineIp;
        String finalSftpUserName = sftpUserName;
        String finalSftpUserPwd = sftpUserPwd;
        String finalRemotePath = remotePath;
        //数据库始终保存远端服务器路径
        String fileSavePath = finalRemotePath;

        //删除文件夹及文件
        //SftpUtils.deleteSFTP(finalLineIp, finalSftpUserName, finalSftpUserPwd, fileSavePath,directory);
    }
}
