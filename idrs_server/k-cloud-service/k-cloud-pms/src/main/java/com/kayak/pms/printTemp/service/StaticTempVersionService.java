package com.kayak.pms.printTemp.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.printTemp.dao.StaticTempVersionDao;
import com.kayak.pms.printTemp.model.PrintTempVersion;
import com.kayak.pms.printTemp.model.StaticTempVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.kayak.base.dao.util.DaoUtil.doTrans;

/**
 * @program: k-cloud
 * @description: 静态文档版本服务
 * @author: WangZhenXin
 * @create: 2021-01-02 10:13
 * @memo 备注信息
 */
@Service
@APIDefine(desc = "静态文档版本服务", model = StaticTempVersion.class)
public class StaticTempVersionService {
    private static final Logger logger = LoggerFactory.getLogger(StaticTempVersionService.class);

    @Autowired
    private StaticTempVersionDao staticTempVersionDao;




    @API(desc = "更新版本", auth = APIAuth.NO, operation = APIOperation.INSTER)
    public void saveStaticTempVersion(StaticTempVersion staticTempVersion) throws Exception {
        //设置创建时间(保存静态文档版本信息)
        Date now = new Date();
        staticTempVersion.setCreateDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        staticTempVersion.setCreateTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
        staticTempVersion.setCreateUserId((String) SysUtil.getSysUserParamValue("sys_user_userid"));
        staticTempVersion.setCreateUserName((String) SysUtil.getSysUserParamValue("sys_user_username"));
        doTrans(() -> {
            //保存文档模板
            staticTempVersionDao.saveStaticTempVersionByTrans(staticTempVersion);
        });
    }

    @API(desc = "根据文档Id获取静态文档版本列表", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<StaticTempVersion> getStaticTempVersionByTempId(SqlParam<StaticTempVersion> param) throws Exception {
        return staticTempVersionDao.getStaticTempVersionByTempId(param);
    }

    //作废前版本
    public void updateStaticTempVersionStatus(String t8StaticTempId, String version) throws Exception {
        //处理版本号
        String[] split = version.split("\\.");
        int i = Integer.parseInt(split[1]) - 1;
        String oldVersion = split[0] + "." + i;
        staticTempVersionDao.updateStaticTempVersionStatus(t8StaticTempId,oldVersion);
    }

    @API(desc = "获取最新的静态文档版本号", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getNewestStaticTempVersion2(SqlParam<StaticTempVersion> param) throws Exception {
        StaticTempVersion staticTempVersion = param.getModel();
        String t8StaticTempId = staticTempVersion.getT8StaticTempId();
        List<SqlRow> newestStaticTempVersion = staticTempVersionDao.getNewestStaticTempVersion(t8StaticTempId);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(newestStaticTempVersion.size());
        sqlRowSqlResult.setRows(newestStaticTempVersion);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "获取最新的静态文档版本号", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public String getNewestStaticTempVersion(String t8StaticTempId) throws Exception {
        List<SqlRow> newestStaticTempVersion = staticTempVersionDao.getNewestStaticTempVersion(t8StaticTempId);
        if (newestStaticTempVersion != null && newestStaticTempVersion.size() > 0) {
            String version = newestStaticTempVersion.get(0).getString("version");
            String[] split = version.split("\\.");
            //最新版本等于最大版本+1
            int i = Integer.parseInt(split[1]) + 1;
            version = split[0] + "." + i;
            return version;
        } else {
            throw new PromptException("查询文档最新版本号失败");
        }
    }

    @API(desc = "静态文档生效", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateStaticTempVersionStatus(SqlParam<StaticTempVersion> param) throws Exception {
        String id = param.getModel().getId();
        Integer integer = staticTempVersionDao.updateStaticTempVersionStatus(id);
        if (integer < 1) {
            throw new PromptException("修改失败");
        } else {
            return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
        }
    }

    @API(desc = "静态文档作废", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String deleteTempVersion(SqlParam<StaticTempVersion> param) throws Exception {
        String id = param.getModel().getId();
        Integer integer = staticTempVersionDao.deleteTempVersion(id);
        if (integer < 1) {
            throw new PromptException("修改失败");
        } else {
            return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
        }
    }
}
