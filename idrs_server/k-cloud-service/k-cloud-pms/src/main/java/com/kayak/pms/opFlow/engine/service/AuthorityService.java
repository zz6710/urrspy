package com.kayak.pms.opFlow.engine.service;

import com.kayak.core.system.SysUtil;
import com.kayak.helper.StringHelper;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.Global;
import com.kayak.pms.opFlow.engine.ProcessInstanceAuth;
import com.kayak.pms.opFlow.engine.constant.ActorTypeConstant;
import com.kayak.pms.opFlow.engine.constant.ParamConstant;
import com.kayak.pms.opFlow.engine.dao.AuthorityDao;
import com.kayak.pms.opFlow.engine.dao.SurrogateDao;
import com.kayak.pms.opFlow.engine.entity.Surrogate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by daniel on 14/06/2017.
 */
@Service
@Transactional
public class AuthorityService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityService.class);

    @Autowired
    AuthorityDao authorityDao;

    @Autowired
    SurrogateDao surrogateDao;

    public List<String> listTaskIdsByCurrentUser() throws Exception {
        Map<String, String> queryCriteria = new HashMap<>();
        queryCriteria.put("ordinaryId", SysUtil.getLoginUserid());
        queryCriteria.put("ordinaryType", ActorTypeConstant.ORDINARY);

        queryCriteria.put("specialId", queryCriteria.get("ordinaryId"));
        queryCriteria.put("specialType", ActorTypeConstant.USER_IDS_BY_ASSIGNMENT_HANDLER);

        //获取用户角色
        String userid = SysUtil.getLoginUserid();
        List<String> roleIdsByUser = authorityDao.getRoleIdsByUser(userid);
        if (roleIdsByUser.size() > 0) {
            String roleIds = String.join(",", roleIdsByUser);
            queryCriteria.put("roleIds", roleIds);
        } else {
            queryCriteria.put("roleIds", SysUtil.getLoginUserRoleIds());
        }
        queryCriteria.put("roleType", ActorTypeConstant.ROLE);

        queryCriteria.put("specialRoles", queryCriteria.get("roleIds"));
        queryCriteria.put("specialRoleType", ActorTypeConstant.ROLE_IDS_BY_ASSIGNMENT_HANDLER);
        return authorityDao.listTaskIdsByCurrentUser(queryCriteria);
    }

    /**
     * 思路:
     * 1、有效的代理人去推导出原始用户
     * 2、查询原始用户具有查看的id(这里还需要加上具体流程ID)
     *
     * @return
     */
    public List<String> listTaskIdsBySurrogateUser() {
        Map<String, Object> surrogateQueryCriteria = new HashMap<String, Object>();
        surrogateQueryCriteria.put(ParamConstant.CURRENT_DATE, DateHelper.getCurrentDate());
        surrogateQueryCriteria.put(ParamConstant.CURRENT_USER_ID, SysUtil.getLoginUserid());
        surrogateQueryCriteria.put("status", SurrogateService.ENABLE);
        List<Surrogate> surrogates = surrogateDao.listProcessIdAndOrignalUserId(surrogateQueryCriteria);

        Set<String> processNames = new HashSet<String>();
        for (Surrogate surrogate : surrogates) {
            if(StringHelper.isNotEmpty(surrogate.getProcessName())){
                String[] splitProcessNames = surrogate.getProcessName().split(",");
                processNames.addAll(Arrays.asList(splitProcessNames));
            }
        }

        //要有代理流程，查询才有意义
        if (processNames.size() > 0) {
            for (Surrogate surrogate : surrogates) {
                Map<String, Object> queryCriteria = new HashMap<String, Object>();
                // 当前当里的流程
                queryCriteria.put("processNames", processNames);

                queryCriteria.put("ordinaryId", surrogate.getCreator());
                queryCriteria.put("ordinaryType", ActorTypeConstant.ORDINARY);

                queryCriteria.put("specialId", surrogate.getCreator());
                queryCriteria.put("specialType", ActorTypeConstant.USER_IDS_BY_ASSIGNMENT_HANDLER);

                //流程创建者具有的权利
                List<String> roleByUserid = SysUtil.getRoleIdsByUser(surrogate.getCreator());

                queryCriteria.put("roleIds", roleByUserid);
                queryCriteria.put("roleType", ActorTypeConstant.ROLE);

                queryCriteria.put("specialRoles", roleByUserid);
                queryCriteria.put("specialRoleType", ActorTypeConstant.USER_IDS_BY_ASSIGNMENT_HANDLER);
                return authorityDao.listTaskIdsBySurrogateUser(queryCriteria);
            }
        }
        return new ArrayList<String>();
    }

    public List<String> listProcessInstanceIdsByCurrentUser() {
        String clzStr = Global.getGlobalConf("PROCESS_INSTANCE_IDS_CLZ");
        if (StringHelper.isNotEmpty(clzStr)) {
            try {
                Class<?> clz = Class.forName(clzStr);
                ProcessInstanceAuth processInstanceAuth = (ProcessInstanceAuth) clz.newInstance();
                return processInstanceAuth.listProcessInstanceIds();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("{} 必须实现接口 {}", clzStr, "com.kayak.opFlow.engine.ProcessInstanceAuth");
            }
        } else {
            Map<String, Object> queryCriteria = new HashMap<String, Object>();
            queryCriteria.put(ParamConstant.CURRENT_USER_ID, SysUtil.getLoginUserid());
            return authorityDao.listProcessInstanceIdsByCurrentUser(queryCriteria);
        }
        return new ArrayList<String>();
    }
}
