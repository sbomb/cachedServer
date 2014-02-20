package org.xiaotian.start;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.xiaotian.util.HttpRequest;
import org.xiaotian.util.Queue;

public class HandleClient{
	private Logger logger = Logger.getLogger(HandleClient.class) ; 
	protected ByteBuffer buffer;
	HttpRequest req ; 
	public HandleClient(HttpRequest req){
		this.req = req ; 
	}
	public ByteBuffer readBlock(){
		logger.info("info is :\n") ; 
		String str = Queue.get("test", 0, 30) ;
		byte[] b = str.getBytes() ; 
		buffer = ByteBuffer.allocate( b.length ) ; 
		buffer.clear();
		buffer.put(b) ; 
		buffer.flip() ; 
		return buffer;
	}
	public void close(){
		buffer.clear() ; 
	}
}