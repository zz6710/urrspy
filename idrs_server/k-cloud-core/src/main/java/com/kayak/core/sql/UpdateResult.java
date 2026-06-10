package com.kayak.core.sql;

public class UpdateResult {

	private int effect;
	private String autoId;

	/**
	 * 获取影响记录数
	 * 
	 * @return
	 */
	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

	/**
	 * 获取自增ID
	 * 
	 * @return
	 */
	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

}
