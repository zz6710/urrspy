package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.system.dao.MenuDao;
import com.kayak.system.model.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@APIDefine(desc = "菜单服务", model = Menu.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MenuService {

    private final MenuDao menuDao;

    @API(desc = "查询")
    public SqlResult<Menu> find(SqlParam<Menu> params) throws Exception {
        params.setMakeSql(true);
        return menuDao.find(params);
    }

    @API(desc = "添加", operation = APIOperation.INSTER)
    public String add(SqlParam<Menu> params) throws Exception {
        if (StringUtils.isEmpty(params.getModel().getModuleid())) {
            params.getModel().setModuleid("0");
        }
        if (menuDao.add(params) < 1) {
            throw new PromptException("操作失败");
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    @API(desc = "修改", operation = APIOperation.UPDATE)
    public String update(SqlParam<Menu> params) throws Exception {
        if (menuDao.update(params) < 1) {
            throw new PromptException("操作失败");
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    @API(desc = "删除", operation = APIOperation.DELETE)
    public String delete(SqlParam<Menu> params) throws Exception {
        if (StringUtils.isEmpty(params.getModel().getMenuid()) || StringUtils.isEmpty(params.getModel().getMenuid())) {
            throw new PromptException("操作失败：menuid、moduleid 不能是空");
        }
        menuDao.delete(params);
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }
}
