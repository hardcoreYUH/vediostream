package com.yuhang.vediostream.base.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.sf.json.JSONObject;





public class JsonUtil {
	private static final Gson gson = new Gson();

	private JsonUtil() {

	}

	public static <T> T fromJson(String jsonStr, Class<T> c)
			throws JsonSyntaxException {
		return gson.fromJson(jsonStr, c);
	}

	public static <T> T fromJsonList(String jsonStr) throws JsonSyntaxException {
		return gson.fromJson(jsonStr, new TypeToken<T>() {
		}.getType());
	}

	public static String toJson(Object object) {
		return gson.toJson(object);
	}

	public static Map<String, Object> mapFromJson(String jsondata) {
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(jsondata, type);
		return map;
	}

	public static <T> List<T> listFromJson(String jsondata) {
		Type type = new TypeToken<List<T>>() {
		}.getType();
		Gson gson = new Gson();
		List<T> map = gson.fromJson(jsondata, type);
		return map;
	}

	public static Object toBean(String className, String json) {
		JSONObject obj = JSONObject.fromObject(json);

		Class c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Object toBean = null;
		try {
			toBean = c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		toBean = JSONObject.toBean(obj, toBean.getClass());
		return toBean;
	}

	public static String toJson(Object object, String timeType) {
		if (null == timeType || "".equals(timeType.trim())) {
			timeType = "yyyy-MM-dd HH:mm:ss";
		}
		return new GsonBuilder().setDateFormat(timeType).create()
				.toJson(object);
	}
	/**
	 * JSON转List<Class>  已 解决  类型擦除问题
	 * @param jsonString 
	 * @param T
	 */
	public static <T> List<T> jsonToListBean(String json, Class<T> clazz) throws Exception {  
        List<T> list =  new ArrayList<T>();  
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();  
        for(final JsonElement elem : array){  
            list.add(new Gson().fromJson(elem, clazz));  
        }  
        return list;  
    }  

	
}
