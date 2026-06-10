package com.kayak.bak.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.dao.BakConfigDao;
import com.kayak.bak.business.dao.BakPhaseDao;
import com.kayak.bak.core.abs.BakTypeContext;
import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.core.config.SubConfig;
import com.kayak.bak.model.dto.BakConfigDTO;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.SourceConfigDTO;
import com.kayak.bak.model.dto.SourceTableDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.bak.model.request.GetFieldInfoRequest;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BakConfigService {

    @Value("${database.schemas}")
    private String databases;

    @Resource
    private BakConfigDao bakConfigDao;

    @Resource
    private BakPhaseDao bakPhaseDao;

    @Resource
    private BakTypeContext bakTypeContext;

    /**
     * 查询归档配置列表
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<SysBakConfigPO> getBakConfigList(SqlParam<SysBakConfigPO> params) throws Exception {
        return bakConfigDao.getBakConfigList(params);
    }

    /**
     * 删除归档配置信息
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBakConfig(SqlParam<SysBakConfigPO> params) throws Exception {
        bakConfigDao.deleteBakConfig(params);
    }

    /**
     * 修改归档配置信息
     * @param params
     * @throws Exception
     */
    public void updateBakConfig(SqlParam<SysBakConfigPO> params) throws Exception {
        if (ObjectUtil.isEmpty(params.getModel().getId())) {
            throw new Exception("空id, 更新异常");
        }
        SysBakConfigPO configPO = params.getModel();
        //读取默认分表阈值
        if (ObjectUtil.isEmpty(params.getModel().getThreshold())) {
            configPO.setThreshold(SubConfig.SUB_NUM);
        }
        bakConfigDao.updateBakConfig(configPO);
    }

    /**
     * 新增归档配置信息
     * @param params
     * @throws Exception
     */
    public void addBakConfig(SqlParam<SysBakConfigPO> params) throws Exception {
        BakConfigDTO configDTO = BeanUtil.copyProperties(params.getModel(), BakConfigDTO.class);
        //计算相关日期
        bakTypeContext.loadContext(configDTO.getType());
        configDTO = bakTypeContext.loadDate(configDTO);
        //数据源是否存在，获取数据量
        configDTO.initAddData();
        bakConfigDao.addBakConfig(configDTO);
    }

    /**
     * 查询归档仓库列表
     * @param params
     * @return
     * @throws Exception
     */
    public List<SysBakCollectionPO> getBakCollectionList(SqlParam<SysBakConfigPO> params) throws Exception {
        return bakConfigDao.getBakCollectionList(params);
    }

    /**
     * 查询单表字段信息
     * @return
     */
    public List<BakFieldDTO> getFieldInfo(GetFieldInfoRequest request) throws Exception {
        return bakPhaseDao.getFieldInfo(request.getDbName(), request.getTableName());
    }

    /**
     * 查询目标库列表
     * @return
     */
    public List<SourceConfigDTO> getDbList() throws Exception {
        List<SourceConfigDTO> list = new ArrayList<>();
        String[] data = databases.split(",");
        for (int i = 0; i < data.length; i++) {
            list.add(new SourceConfigDTO().setDbName(data[i]));
        }
        //return SourceConfig.getDbList();
        return list;
    }

    /**
     * 查询目标表列表
     * @return
     */
    public List<SourceTableDTO> getTableList(String dbName) throws Exception {
        return bakConfigDao.getTableList(dbName);
    }

    /**
     * 检查目标数据库，获取相关信息
     * @param db
     * @param table
     * @return
     */
    private int getTbNum(String db, String table) throws Exception {
        int dataSource = SourceConfig.getDataSource(db);
        return bakConfigDao.getCountForDb(dataSource, table);
    }

}
