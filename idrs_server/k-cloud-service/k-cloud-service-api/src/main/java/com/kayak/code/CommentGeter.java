package com.kayak.code;

import com.kayak.core.util.Tools;

public abstract class CommentGeter {

	protected String getCommentBySeparator(String comment, String separator) {
		if (Tools.strIsEmpty(separator) || Tools.strIsEmpty(separator)) {
			return comment;
		}

		String[] separators = separator.split(",");

		for (String _separator : separators) {
			if (comment.contains(_separator)) {
				return comment.substring(0, comment.indexOf(_separator));
			}
		}

		return comment;
	}

}
