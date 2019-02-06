package com.utils.json;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils {
	public String[] getvalue(String jsonStr, String xPath) {
		if (jsonStr == null || "".equals(jsonStr) || xPath == null || "".equals(xPath))
			return null;

		if (xPath.startsWith("/"))
			xPath = xPath.substring(1);

		String[] paths = xPath.split("/");
		if (paths.length == 0)
			return null;
		int curIndex = 0;
		JSONObject jsonObject = JSON.parseObject(jsonStr);
		if (jsonObject == null)
			return null;
		List<String> resultList = new ArrayList<>();
		// 遍历json
		traversing(jsonObject, paths, curIndex, resultList);
		// 转成string
		if (resultList.size() == 0)
			return null;
		String[] results = new String[resultList.size()];
		for (int i = 0; i < resultList.size(); i++) {
			results[i] = resultList.get(i);
		}
		return results;
	}

	private void traversing(JSONObject jsonObject, String[] paths, int curIndex, List<String> resultList) {
		String curPath = paths[curIndex];
		if (curIndex == paths.length - 1) {// 是最终节点
			if (curPath.endsWith("*")) {
				// 是数组
				JSONArray jsonArray = jsonObject.getJSONArray(curPath.replace("*", ""));
				for (Object object : jsonArray) {
					resultList.add((String) object);
				}
			} else {
				// 不是数组
				String value = jsonObject.getString(curPath);
				resultList.add(value);
			}
		} else {
			// 不是最终节点
			if (curPath.endsWith("*")) {
				// 是数组
				JSONArray jsonArray = jsonObject.getJSONArray(curPath.replace("*", ""));
				for (int i = 0; i < jsonArray.size(); i++) {
					traversing(jsonArray.getJSONObject(i), paths, curIndex + 1, resultList);
				}
			} else {
				// 不是数组
				traversing(jsonObject.getJSONObject(curPath), paths, curIndex + 1, resultList);
			}
		}
	}

	public static void main(String[] args) {
		String json = "{'province':{'name':'guangdong','city':{'company':['huawei','tencent'],'name':'shenzheng','person':[{'name':'1'},{'name':'2'},{'name':'3'}]}}}";
		JsonUtils jsonUtils = new JsonUtils();
		String[] getvalue = jsonUtils.getvalue(json, "/province/city/person*/name");
		for (String value : getvalue) {
			System.out.println(value);
		}
	}
}
