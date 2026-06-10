package com.kayak.system.service;

import com.google.common.collect.Lists;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.kayak.core.system.constants.UserConstants.SUPER_ROLE_ID;

@Service
@Slf4j
@APIDefine(desc = "角色权限服务", model = RoleAuthority.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleAuthorityService {

    private final RoleServerDao roleServerDao;

    private final RoleDao roleDao;

    private final MenuDao menuDao;

    private final ServerMethodDao serverMethodDao;

    private final RoleAuthorityDao roleAuthorityDao;

    @API(desc = "查询角色可选权限", auth = APIAuth.NO)
    public SqlResult<RoleAuthority> find(SqlParam<RoleAuthority> roleSqlParam) throws Exception {
        String roleId;
        String requestRoleId = roleSqlParam.getModel().getId();
        if (Tools.isBlank(requestRoleId)) {
            return SqlResult.build(Collections.emptyList());
        }
        //如果是角色是超级管理员，直接去超级管理员id,否则取父角色id
        if (SUPER_ROLE_ID.equals(requestRoleId)) {
            roleId = SUPER_ROLE_ID;
        } else {
            roleId = roleDao.findRoleById(requestRoleId).getParentroleid();
        }
        //查询出对应角色id的权限
        Map<String, List<ServerMethod>> serverMethodMap = serverMethodDao.findByRoleId2(roleId).stream()
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
        List<Menu> menus = menuDao.findByRoleId(roleId);
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

    @API(desc = "查询角色已有权限", auth = APIAuth.NO)
    public SqlResult<RoleServer> findServer(SqlParam<RoleServer> roleServerSqlParam) throws Exception {
        roleServerSqlParam.setMakeSql(true);
        return roleServerDao.find(roleServerSqlParam);
    }

    @API(desc = "查询角色已有权限", auth = APIAuth.NO)
    public SqlResult<RoleAuthority> findAlreadyOwned(SqlParam<RoleAuthority> roleAuthoritySqlParam) throws Exception {
        RoleAuthority model = roleAuthoritySqlParam.getModel();
        String roleId = model.getId();

        List<ServerMethod> serverMethods = serverMethodDao.findByRoleId(roleId);
        Set<String> modelsSet = serverMethods.stream().map(serverMethod -> serverMethod.getModelName()).collect(Collectors.toSet());
        List<Menu> menus = menuDao.findByRoleId(roleId);

        List<Menu> removeMenus = new ArrayList<>();

        for (Menu menu : menus) {
            for (Menu _menu : menus) {
                if (menu.getMenuid().equals(_menu.getUpperid())) {
                    removeMenus.add(menu);
                    break;
                }
            }
        }

        //删除上级菜单
        menus.removeAll(removeMenus);

        ListIterator<Menu> menuListIterator = menus.listIterator();
        while (menuListIterator.hasNext()) {
            Menu menu = menuListIterator.next();
            String modelNames = menu.getModel();
            if (Tools.isBlank(modelNames)) {
                continue;
            }

            String[] modelArr = modelNames.split(",");
            for (String modelName : modelArr) {
                if (modelsSet.contains(modelName)) {
                    menuListIterator.remove();
                    break;
                }
            }
        }

        List<RoleAuthority> result = new ArrayList<>();
        for (ServerMethod serverMethod : serverMethods) {
            RoleAuthority ra = new RoleAuthority();
            ra.setId(serverMethod.getServer());
            result.add(ra);
        }
        for (Menu menu : menus) {
            RoleAuthority ra = new RoleAuthority();
            ra.setId(menu.getMenuid());
            result.add(ra);
        }

        return SqlResult.build(result);
    }

    @API(desc = "权限设置", auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String save(SqlParam<RoleAuthoritySave> roleAuthoritySqlParam) throws Exception {
        RoleAuthoritySave model = roleAuthoritySqlParam.getModel();
        String ids = model.getId();
        String types = model.getType();//权限类型 1-菜单，2-服务

        if (Tools.isBlank(ids) || Tools.isBlank(types)) {
            throw new PromptException("权限不能为空");
        }

        String[] idArr = ids.split(",");
        String[] typeArr = types.split(",");

        Set<RoleMenu> roleMenus = new HashSet<>();
        Set<RoleServer> roleServers = new HashSet<>();

        for (int i = 0; i < idArr.length; i++) {
            if (RoleAuthorityTypeEnum.MENU.getValue().equals(typeArr[i])) {//菜单
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuid(idArr[i]);
                roleMenu.setModuleid("0");
                roleMenu.setRoleid(model.getRoleId());
                roleMenus.add(roleMenu);
            } else if (RoleAuthorityTypeEnum.SERVER.getValue().equals(typeArr[i])) {//服务
                RoleServer roleServer = new RoleServer();
                roleServer.setRoleid(model.getRoleId());
                roleServer.setServer(idArr[i]);
                roleServers.add(roleServer);
            } else {
                log.error("保存角色权限异常,类型不匹配:[{}]", model.getId());
                throw new PromptException("参数异常");
            }
        }
            HashMap<String, Object> map = new HashMap<>();
            map.put("roleid", model.getRoleId());
            //通过角色id查询原来的服务权限权限
            SqlResult<RoleServer> aRoleAuthority = roleAuthorityDao.findARoleServerAuthority(map);
            final List<RoleServer> rows = aRoleAuthority.getRows();
            //liat转map
            final Map<String, String> serveCcollect = rows.stream().collect(Collectors.toMap(RoleServer::getServer, RoleServer::getRoleid));

            //通过角色id查询原来的菜单权限
            SqlResult<RoleMenu> roleMenuResult = roleAuthorityDao.findARoleMenuAuthority(map);
            List<RoleMenu> roleMenuRow = roleMenuResult.getRows();
            //list转map
            Map<String, String> menuCollect = roleMenuRow.stream().collect(Collectors.toMap(RoleMenu::getMenuid, RoleMenu::getRoleid));
             //修改权限
            roleAuthorityDao.save(model.getRoleId(), roleMenus, roleServers, serveCcollect, menuCollect);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

}
