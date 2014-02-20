package org.xiaotian.util;

import java.io.IOException;
//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 * JSON tools util
 * @author ZFLuckyLing
 *
 */
public class JSONUtil {

	private static final ObjectMapper mapper = new ObjectMapper() ;
	
	private static Logger logger = Logger.getLogger(JSONUtil.class) ; 
	
	@SuppressWarnings({ "rawtypes"})
	public static String toJSON(Map map){
		try {
			/**
			 * 增加空值不加入到json字符传中.
			 */
			//mapper.getSerializationConfig().disable(Feature.WRITE_NULL_MAP_VALUES );
			String result = mapper.writeValueAsString(map) ;
			return result ; 
		} catch (JsonGenerationException e) {
			logger.info("json error :" + e.getMessage()) ; 
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.info("json error :" + e.getMessage()) ;
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("io error :" + e.getMessage()) ;
			e.printStackTrace();
		}
		return "" ; 
	}
	//@SuppressWarnings("deprecation")
	public static String toJSON(Object obj){
		try {
//			/**
//			 * 增加空值不加入到json字符传中.
//			 */
//			mapper.getSerializationConfig().disable(Feature.WRITE_NULL_MAP_VALUES );
			String result = mapper.writeValueAsString(obj) ;
			return result ; 
		} catch (JsonGenerationException e) {
			logger.info("json error :" + e.getMessage()) ; 
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.info("json error :" + e.getMessage()) ;
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("io error :" + e.getMessage()) ;
			e.printStackTrace();
		}
		return "" ; 
	}
	
	@SuppressWarnings("rawtypes")
	public static Map toMap(String json){
		/**
		 * HashMap 是按照key的HashCode进行排序的，使用KeySet方法后 ， 并不能
		 * 保证按照插入的顺序进行排序.
		 * 
		 * @author ZFLuckyLing
		 * 2013-12-23
		 * 
		 */
		Map map = new LinkedHashMap() ; 
		
		try {
			map = mapper.readValue(json, map.getClass()) ;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map ; 
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object toObject(String json , Class clazz){
		Object obj = null;
		try {
			obj = mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return obj ; 
	}
}
