package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.system.dao.UserPwdDao;
import com.kayak.system.model.User;
import com.kayak.system.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
	@APIDefine(desc = "用户服务", model = UserInfo.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserPwdService extends ComnDao {

	private final UserPwdDao userPwdDao;

	
	
	

	
	@API(desc = "修改用户", auth = APIAuth.NO)
	public String updateUserInfo(SqlParam<UserInfo> params) throws Exception {
		boolean result = userPwdDao.updateUserInfo(params) > 0;
		return RequestSupport.updateReturnJson(result, result ? "修改成功" : "修改失败", null).toString();
	}


}
