package com.kayak.core.desensitized;

import com.kayak.core.util.Tools;

public class IdCardDesensitized implements Desensitized {

	@Override
	public String desensitized(Object value) {
		String _vaule = Tools.obj2Str(value);

		if (Tools.strIsEmpty(_vaule) || _vaule.length() < 18) {
			return "";
		}
		return _vaule.substring(0, 12) + "****" + _vaule.substring(_vaule.length() - 1, _vaule.length())+"*";
	}

}
