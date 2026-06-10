package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.system.dao.LiableDao;
import com.kayak.system.model.Liable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@APIDefine(desc = "责任人服务", model = Liable.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LiableService {

    private final LiableDao liableDao;

    @API(desc = "查询责任人", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<Liable> find1(SqlParam<Liable> params) throws Exception {
        return find(params);
    }

    @API(desc = "查询责任人", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Liable> find(SqlParam<Liable> params) throws Exception {
        params.setMakeSql(false);
        return liableDao.find(params);
    }

    @API(desc = "新增责任人" ,auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String add(SqlParam<Liable> params) throws Exception {
        List<Liable> liables = liableDao.find_l(params);
        if (!CollectionUtils.isEmpty(liables)) {
            //throw new PromptException("该部门已有责任人，请核对后在新增");
            return RequestSupport.updateReturnJson(false, "该部门已有责任人，请核对后在新增", null).toString();
        }
        boolean result = liableDao.add(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "添加成功" : "添加失败", null).toString();
    }

    @API(desc = "修改责任人", operation = APIOperation.UPDATE)
    public String update(SqlParam<Liable> params) throws Exception {
        boolean result = liableDao.update(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "修改成功" : "修改失败", null).toString();
    }

    @API(desc = "删除责任人", operation = APIOperation.DELETE)
    public String delete(SqlParam<Liable> params) throws Exception {
        boolean result = liableDao.delete(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "删除成功" : "删除失败", null).toString();
    }

}
