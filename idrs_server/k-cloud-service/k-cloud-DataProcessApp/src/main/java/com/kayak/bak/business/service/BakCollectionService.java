package com.kayak.bak.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.dao.BakCollectionDao;
import com.kayak.bak.model.dto.BakCollectionDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BakCollectionService {

    @Resource
    private BakCollectionDao bakCollectionDao;

    /**
     * 查询归档集合列表
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<SysBakCollectionPO> getBakCollectionList(SqlParam<SysBakCollectionPO> params) throws Exception {
        return bakCollectionDao.getBakCollectionList(params);
    }

    /**
     * 新增归档集合信息
     * @param params
     * @throws Exception
     */
    public void addBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        BakCollectionDTO bakCollectionDTO = BeanUtil.copyProperties(params.getModel(), BakCollectionDTO.class);
        //处理数据
        bakCollectionDTO.initAddData();
        bakCollectionDao.addBakCollection(bakCollectionDTO);
    }

    /**
     * 删除集合表信息
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        bakCollectionDao.deleteBakCollection(params);
    }

    /**
     * 修改集合表信息
     * @param params
     * @throws Exception
     */
    public void updateBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        if (ObjectUtil.isEmpty(params.getModel().getId())) {
            throw new Exception("空id, 更新异常");
        }
        bakCollectionDao.updateBakCollection(params);
    }
}
