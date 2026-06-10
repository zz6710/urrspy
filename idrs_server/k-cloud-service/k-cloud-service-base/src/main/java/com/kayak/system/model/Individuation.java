package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "individuationService", table = "sys_user_individuation")
public class Individuation {
	@GraphQLField(key = true, kkhtml = "KFieldText", label = "用户ID", sql = "userid = $S{userid}", field = "userid")
	private String userid;
	@GraphQLField(kkhtml = "KFieldText", label = "主题颜色", sql = "theme_color = $S{themeColor}", field = "theme_color")
	private String themeColor;
	@GraphQLField(kkhtml = "KFieldText", label = "菜单背景颜色", sql = "menu_color = $S{menuColor}", field = "menu_color")
	private String menuColor;
	@GraphQLField(kkhtml = "KFieldText", label = "菜单最小化", sql = "menu_min = $S{menuMin}", field = "menu_min")
	private String menuMin;
	@GraphQLField(kkhtml = "KFieldText", label = "是否显示背景图", sql = "menu_bg_show = $S{menuBgShow}", field = "menu_bg_show")
	private String menuBgShow;
	@GraphQLField(kkhtml = "KFieldText", label = "背景图", sql = "menu_bg = $S{menuBg}", field = "menu_bg")
	private String menuBg;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getThemeColor() {
		return themeColor;
	}

	public void setThemeColor(String themeColor) {
		this.themeColor = themeColor;
	}

	public String getMenuColor() {
		return menuColor;
	}

	public void setMenuColor(String menuColor) {
		this.menuColor = menuColor;
	}

	public String getMenuMin() {
		return menuMin;
	}

	public void setMenuMin(String menuMin) {
		this.menuMin = menuMin;
	}

	public String getMenuBgShow() {
		return menuBgShow;
	}

	public void setMenuBgShow(String menuBgShow) {
		this.menuBgShow = menuBgShow;
	}

	public String getMenuBg() {
		return menuBg;
	}

	public void setMenuBg(String menuBg) {
		this.menuBg = menuBg;
	}

}