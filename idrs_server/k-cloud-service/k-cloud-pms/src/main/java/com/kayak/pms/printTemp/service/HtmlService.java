package com.kayak.pms.printTemp.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HtmlService {


	public class Result {
		private final Map<String, String> keys;
		private final String html;

		public Result(Map<String, String> keys, String html) {
			super();
			this.keys = keys;
			this.html = html;
		}

		public Map<String, String> getKeys() {
			return keys;
		}

		public String getHtml() {
			return html;
		}

	}

	/**
	 * html加工
	 *
	 * @param text
	 * @return
	 */
	public Result process(String text, String jqueryPah) {
		return process("${", "}", text, jqueryPah);
	}

	private Result process(String openToken, String closeToken, String text, String jqueryPah) {
		if (text == null || text.isEmpty()) {
			return new Result(Collections.emptyMap(), text);
		}
		char[] src = text.toCharArray();
		int offset = 0;

		// search open token
		int start = text.indexOf(openToken, offset);
		if (start == -1) {
			return new Result(Collections.emptyMap(), text);
		}
		Map<String, String> keys = new HashMap<>();
		final StringBuilder builder = new StringBuilder();
		StringBuilder expression = null;
		while (start > -1) {
			if (start > 0 && src[start - 1] == '\\') {
				// this open token is escaped. remove the backslash and continue.
				builder.append(src, offset, start - offset - 1).append(openToken);
				offset = start + openToken.length();
			} else {
				// found open token. let's search close token.
				if (expression == null) {
					expression = new StringBuilder();
				} else {
					expression.setLength(0);
				}
				builder.append(src, offset, start - offset);
				offset = start + openToken.length();
				int end = text.indexOf(closeToken, offset);
				while (end > -1) {
					if (end > offset && src[end - 1] == '\\') {
						// this close token is escaped. remove the backslash and continue.
						expression.append(src, offset, end - offset - 1).append(closeToken);
						offset = end + closeToken.length();
						end = text.indexOf(closeToken, offset);
					} else {
						expression.append(src, offset, end - offset);
						break;
					}
				}
				if (end == -1) {
					// close token was not found.
					builder.append(src, start, src.length - start);
					offset = src.length;
				} else {
					String key = expression.toString().trim();
					builder.append("<span name=\"v_");
					builder.append(key);
					builder.append("\" class=\"placeholder\" ");
					builder.append("onclick=\"inputFocus('");
					builder.append(key);
					builder.append("')\">");
					builder.append(key);
					builder.append("</span>");
					keys.put(key, key);
					offset = end + closeToken.length();
				}
			}
			start = text.indexOf(openToken, offset);
		}

		if (offset < src.length) {
			builder.append(src, offset, src.length - offset - (src.length - text.lastIndexOf("</html>")));
			/*去除引入jquery文件<script src=\""+jqueryPah+"\"></script>*/
			builder.append( "<script type=\"text/javascript\">"
					+ "function replace(key, value) {$(\"span[name='v_\"+key+\"']\").each(function (i, e) {$(e).html(value);});};"
					+ "function inputFocus(key) {window.parent.postMessage({key:key},\"*\");};"
					+ "</script>"
					+ "<style>span.placeholder{background: #FFFF00;color: #FF0000;}</style></html>");
		}
		return new Result(keys, builder.toString());
	}

}
