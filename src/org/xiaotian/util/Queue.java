package org.xiaotian.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * queue
 * 
 * @author ZFLuckyLing
 * @since 0.0.1 2014-02-19
 * 
 */
public class Queue {
	private static Logger logger = Logger.getLogger(Queue.class);
	private static Map<String, TreeMap<String, String>> map
			= new HashMap<String, TreeMap<String, String>>();
	private static PropertiesManager pm = PropertiesManager
			.getManager("common");

	private final static int maxCount = getMaxCount();

	private static int getMaxCount() {
		String count = pm.getString("maxCount");
		try {
			return Integer.parseInt(count);
		} catch (Exception e) {
			return 200000;
		}
	}

	public static int getCount(String queueName) {
		int count = 0;
		TreeMap<String, String> treeMap = map.get(queueName);
		if (treeMap == null || treeMap.isEmpty()) {
			return 0;
		}
		count = treeMap.size();
		return count;
	}

	public static String put(String queueName, String key, String value) {
		TreeMap<String, String> treeMap = map.get(queueName);
		if (treeMap == null || treeMap.isEmpty()) {
			treeMap = new TreeMap<String, String>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o2.compareTo(o1);
				}
			});
			map.put(queueName, treeMap);
		}
		int count = treeMap.size();
		if (count >= maxCount) {
			System.out.println("max count");
			return "max count";
		} else {
			treeMap.put(key, value);
			return "success";
		}
	}

	public static String get(String queueName, String key) {
		TreeMap<String, String> treeMap = map.get(queueName);
		if (treeMap == null || treeMap.isEmpty()) {
			return "";
		}
		return "";
	}

	public static String get(String queueName, int from, int to) {
		long start = System.currentTimeMillis();
		TreeMap<String, String> treeMap = map.get(queueName);
		if (treeMap == null || treeMap.isEmpty()) {
			return "";
		}
		Collection<String> c = treeMap.values();
		List<String> list = new ArrayList<String>(c);
		list = list.subList(from, to);
		String result = JSONUtil.toJSON(list);
		long end = System.currentTimeMillis();
		logger.info("get Queue from " + from + " to " + to + " time is "
				+ (end - start) + "ms");
		return result;
	}
}
