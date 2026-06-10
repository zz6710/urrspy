package com.kayak.core.desensitized;

import com.kayak.core.util.Tools;

public class DefaultDesensitized implements Desensitized {

	@Override
	public String desensitized(Object value) {
		return Tools.obj2Str(value);
	}

}
