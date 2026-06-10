package com.kayak.menu.action;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlQueryTree;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.menu.dao.MenuDao;

@RestController
public class MenuAction extends BaseController {

	@Autowired
	private MenuDao menuDao;

	@Value("${sys.maxComMenu: 15}")
	private int maxComMenu;

	@PostMapping(value = "/sys/findMenus.json")
	public String findMenus() {
		try {

			List<SqlRow> rows = menuDao.findMenuRoot();
			SqlQueryTree sqlQueryTree = new SqlQueryTree("menuid,upperid", rows);
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("rows", sqlQueryTree.getTreeJson());
			return json.toString();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

	// 查询条件配置时检测状态
	@PostMapping(value = "/sys/findMenuForDev.json")
	public String findModelField(){
		try {
			List<SqlRow> rows = menuDao.findMenuForDev();
			SqlQueryTree sqlQueryTree = new SqlQueryTree("menuid,upperid", rows);
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("rows", sqlQueryTree.getTreeJson());
			return json.toString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

	@PostMapping(value = "/sys/findComMenus.json")
	public String findComMenus() {
		try {

			List<SqlRow> rows = menuDao.findComMenus();
			JSONArray jsonRows = new JSONArray();
			for (SqlRow row : rows) {
				jsonRows.put(new JSONObject(row));
			}

			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("rows", jsonRows);
			return json.toString();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

	@PostMapping(value = "/sys/addComMenu.json")
	public String addComMenu() {
		try {

			Map<String, Object> params = RequestSupport.getParameters();

			// 判断数量是否超过最大数量
			List<SqlRow> rows = menuDao.findComMenus();

			if (rows.size() >= maxComMenu) {
				return updateFailure("添加失败，最多只能置顶" + maxComMenu + "个菜单");
			}

			String url = (String) params.get("url");

			if (url.startsWith("/main")) {
				url = url.substring(6);
			}

			SqlRow menu = menuDao.findMenuByUrl(url);

			if (menu == null) {
				return updateFailure("菜单不存在，地址：" + url);
			}

			menuDao.addComMenu(menu.getString("menuid"));

			JSONObject json = new JSONObject();
			json.put("success", true);
			return json.toString();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

	@PostMapping(value = "/sys/deleteComMenu.json")
	public String deleteComMenu() {
		try {

			Map<String, Object> params = RequestSupport.getParameters();

			String menuid = (String) params.get("menuid");

			menuDao.deleteComMenu(menuid);

			JSONObject json = new JSONObject();
			json.put("success", true);
			return json.toString();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

}
