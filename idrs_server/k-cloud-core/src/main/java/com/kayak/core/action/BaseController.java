package com.kayak.core.action;

import com.kayak.core.system.RequestSupport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BaseController {

	protected static final Logger log = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 返回更新失败的结果
	 * 
	 * @param returnmsg 返回到客户端的文本信息（错误信息）
	 */
	protected String updateFailure(String returnmsg) {
		return updateFailure(returnmsg, null);
	}

	/**
	 * 返回更新失败的结果
	 * 
	 * @param returnmsg  返回到客户端的文本信息（错误信息）
	 * @param returndata 返回到客户端的数据JSON对象
	 */
	protected String updateFailure(String returnmsg, Map<String, Object> returndata) {
		return returnUpdate(false, returnmsg, returndata);
	}

	/**
	 * 返回更新成功的结果
	 */
	protected String updateSuccess() {
		return updateSuccess(null, null);
	}

	/**
	 * 返回更新成功的结果
	 * 
	 * @param returndata 返回到客户端的数据JSON对象
	 */
	protected String updateSuccess(Map<String, Object> returndata) {
		return updateSuccess(null, returndata);
	}

	/**
	 * 返回更新成功的结果
	 * 
	 * @param returnmsg 返回到客户端的文本信息
	 */
	protected String updateSuccess(String returnmsg) {
		return updateSuccess(returnmsg, null);
	}

	/**
	 * 返回更新成功的结果
	 * 
	 * @param returnmsg  返回到客户端的文本信息
	 * @param returndata 返回到客户端的数据JSON对象
	 */
	protected String updateSuccess(String returnmsg, Map<String, Object> returndata) {
		return returnUpdate(true, returnmsg, returndata);
	}

	protected String returnUpdate(boolean success, String returnmsg, Map<String, Object> returndata) {
		return RequestSupport.updateReturnJson(success, returnmsg, returndata).toString();
	}

	protected <T> String  updateSuccess(List<T> rows) {
		JSONArray jsonArray = new JSONArray();

		if (rows != null && rows.size() > 0) {
			for (T row : rows) {
				jsonArray.put(row);
			}
		}

		return updateSuccess(jsonArray);
	}

	protected String updateSuccess(JSONArray rows) {
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("rows", rows);
		return json.toString();
	}

}
