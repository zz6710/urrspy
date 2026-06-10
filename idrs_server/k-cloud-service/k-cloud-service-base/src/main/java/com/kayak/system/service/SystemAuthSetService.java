package com.kayak.system.service;

import com.google.common.collect.Lists;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.constants.RoleAuthorityTypeEnum;
import com.kayak.core.util.Tools;
import com.kayak.system.dao.*;
import com.kayak.system.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@APIDefine(desc = "授权设置服务", model = SystemAuthSet.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SystemAuthSetService {

    private final SystemAuthSetDao systemAuthSetDao;

    private final SystemAuthRoleCheckDao systemAuthRoleCheckDao;

    private final RoleServerDao roleServerDao;

    private final RoleDao roleDao;

    private final MenuDao menuDao;

    private final ServerMethodDao serverMethodDao;

    private final RoleAuthorityDao roleAuthorityDao;



    @API(desc = "新增授权设置",auth = APIAuth.YES)
    public String add(SqlParam<SystemAuthSet> params) throws Exception {
         try {
             systemAuthSetDao.add(params);
         }catch (SQLIntegrityConstraintViolationException e){
             return RequestSupport.updateReturnJson(false, "该授权角色已存在", null).toString();
         } catch (Exception e){
           return RequestSupport.updateReturnJson(false, "新增失败", null).toString();
         }
         return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "删除授权设置",auth = APIAuth.YES)
    public String delete(SqlParam<SystemAuthSet> params) throws Exception {
        try {
            systemAuthSetDao.deleteByKey(params);
        }catch (Exception e){
            return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
        }
        CacheUtil.freshenGateway();
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "查询授权设置列表")
    public SqlResult<SystemAuthSet> find(SqlParam<SystemAuthSet> params) throws Exception {
        params.setMakeSql(true);
        SqlResult<SystemAuthSet> result = systemAuthSetDao.find(params);
        return result;
    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "查询授权设置树状图",auth = APIAuth.YES)
    public SqlResult<RoleAuthority> findTree(SqlParam<SystemAuthSet> params) throws Exception {

        Map<String, List<ServerMethod>> serverMethodMap = serverMethodDao.findByRoleId("0").stream()
                .collect(Collectors.toMap(ServerMethod::getModelName,
                        s -> Lists.newArrayList(s),
                        (List<ServerMethod> newValueList, List<ServerMethod> oldValueList) ->
                        {
                            oldValueList.addAll(newValueList);
                            return oldValueList;
                        }
                        )
                );

        // 避免多个菜单配置相同的model，导致界面的tree的key相同
        Set<String> alreadyExistsModelSet = new HashSet<>();

        List<RoleAuthority> roleAuthorities = new ArrayList<>();
        List<Menu> menus = menuDao.findByRoleId("0");

        for (Menu menu : menus) {
            roleAuthorities.add(
                    new RoleAuthority(menu.getMenuid(),
                            menu.getUpperid(),
                            RoleAuthorityTypeEnum.MENU.getValue(),
                            menu.getMenuname())
            );

            String model = menu.getModel();
            if (Tools.isBlank(model)) {
                continue;
            }
            String[] modelArr = model.split(",");
            for (String m : modelArr) {
                if (Tools.isBlank(m)) {
                    continue;
                }
                if (alreadyExistsModelSet.contains(m)) {
                    continue;
                } else {
                    alreadyExistsModelSet.add(m);
                }
                List<ServerMethod> serverMethods = serverMethodMap.get(m);
                if (CollectionUtils.isEmpty(serverMethods)) {
                    continue;
                }
                for (ServerMethod serverMethod : serverMethods) {
                    roleAuthorities.add(
                            new RoleAuthority(serverMethod.getServer(),
                                    menu.getMenuid(),
                                    RoleAuthorityTypeEnum.SERVER.getValue(),
                                    serverMethod.getServerDesc()));
                }

            }
        }
        return SqlResult.build(roleAuthorities, roleAuthorities.size());
    }

    @API(desc = "查询子角色",auth = APIAuth.NO)
    public SqlResult<Role> findRoles(SqlParam<SystemAuthSet> params) throws Exception {
        List<Role> roles=roleDao.findAll();
        return SqlResult.build(roles,roles.size());
    }
}
