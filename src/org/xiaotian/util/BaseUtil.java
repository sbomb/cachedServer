package org.xiaotian.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author ZFLuckyLing
 * @version 1.0.0.1 2013-11-20
 */
public class BaseUtil {
	private static Logger logger = Logger.getLogger(BaseUtil.class);

	/**
	 //*@deprecated
	 * @return
	 */
	public static Object[] map2Array(Map<String, Object> map) {
		if (map == null || map.isEmpty() || map.size() <= 0) {
			return null;
		}
		ArrayList<Object> array = new ArrayList<Object>() ; 
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
//			System.out.println(iter.next());
			String key = iter.next() ;
			array.add(map.get(key)) ; 
		}

		return array.toArray();
	}

	/**
	 * @deprecated
	 * @return
	 */
	public static String basePath() {
		String userDir = System.getProperty("user.dir");
		PropertiesManager pm = PropertiesManager.getManager("platform");
		userDir = userDir + pm.getString("basePath");
		return "";
	}

	public static String time2String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		return sdf.format(date);
	}

	public static String convertUrl(String param) {
		if (param == null) {
			return null;
		}
		try {
			param = URLDecoder.decode(param, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}

		return param;
	}
}
