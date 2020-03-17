package com.yuhang.vediostream.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ApiConstant {
	public static final String API_PARAM_CODE = "code";
	public static final String API_PARAM_ERROR = "msg";
	public static final String API_PARAM_STATUS = "status";

	public final static String RET_FAILED = "-1";// 错误信息缺少参数  可给前台展示
	public final static String RET_ERROR = "-2";// 程序错误 不可给前台展示
	public final static String RET_LOGIN = "-3";// 没有登录 调到登录页面
	public final static String RET_FIELD = "-4";// 无权限

	public static final String API_SUCC = "0";
	public static final String ERP_SUCC = "200";

	private static Map<String, String> errorMap = new HashMap<String, String>();
	static {
		errorMap.put(API_SUCC, "成功");
		errorMap.put(ERP_SUCC, "请求成功");
		errorMap.put(RET_LOGIN, "先登录再进行操作");
		errorMap.put(RET_FAILED, "缺少必要参数");
		errorMap.put(RET_ERROR, "网络异常");
		errorMap.put(RET_FIELD, "无此操作权限");
	}

	public static String getError(String code) {
		return errorMap.get(code);
	}

	public static Map<String, Object> resp(String code) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(ApiConstant.API_PARAM_CODE, code);
		map.put(ApiConstant.API_PARAM_ERROR, getError(code));
		return map;
	}

	public static Map<String, Object> respStatus(String status,String msg) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(ApiConstant.API_PARAM_STATUS, status);
		map.put(ApiConstant.API_PARAM_ERROR, msg);
		return map;
	}

	public static Map<String, Object> resp(String code, String msg) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(ApiConstant.API_PARAM_CODE, code);
		map.put(ApiConstant.API_PARAM_ERROR, msg);
		return map;
	}
}

