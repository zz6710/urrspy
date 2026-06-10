/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kayak.utils;

import org.joda.time.DateTime;

/**
 * 日期帮助类
 * @author yuqs
 * @since 1.0
 */
public class DateHelper {
	private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT_DATE = "yyyyMMdd";
	private static final String DATE_FORMAT_TIME = "HHmmss";

	/**
	 * 返回标准格式的当前时间
	 * @return
	 */
	public static String getCurrentDateTime() {
		return new DateTime().toString(DATE_FORMAT_DEFAULT);
	}

	public static String getCurrentDate() {
		return new DateTime().toString(DATE_FORMAT_DATE);
	}

	public static String getCurrentTime() {
		return new DateTime().toString(DATE_FORMAT_TIME);
	}
}
