package org.xiaotian.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * 
 * Manager properties file .
 * @author ZFLuckyLing 
 * @version 1.0.0.1 2013-11-20
 *
 */
public class PropertiesManager {

	private final Properties properties ;
	private Logger logger =Logger.getLogger(PropertiesManager.class) ;  
	
	private PropertiesManager(String fileName) {
		String bundleName = fileName + ".properties";
		properties = new Properties() ; 
		logger.info("init propertiesManager") ; 
		InputStream input = this.getClass().getResourceAsStream("/" + bundleName) ;
		try {
			properties.load(input) ;
		} catch (IOException e) {
//			e.printStackTrace();
			logger.info("init propertiesManager error :" + e.getMessage()) ; 
		} 

	}

	public String getString(String key) {
		return properties.getProperty(key);
	}	

	 private static final Map<String, PropertiesManager> managers =
		        new Hashtable<String, PropertiesManager>();
	 
	  
	public static final synchronized PropertiesManager getManager(
			String fileName) {
		PropertiesManager pm = managers.get(fileName);
		if (pm == null) {
			pm = new PropertiesManager(fileName);
			managers.put(fileName, pm);
		}

		return pm;
	}
	
	public static void reloadProperties(String fileName){
		PropertiesManager pm = new PropertiesManager(fileName) ; 
		managers.put(fileName, pm) ; 
	}
}
