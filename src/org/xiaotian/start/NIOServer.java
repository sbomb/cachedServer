package org.xiaotian.start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
//import java.util.Set;

import org.apache.log4j.Logger;
import org.xiaotian.util.HttpRequest;
import org.xiaotian.util.PropertiesManager;
import org.xiaotian.util.Queue;

/**
 * @author ZFLuckyLing
 * @since 0.0.1 2014-02-19
 * 
 */
public class NIOServer {

	private static Logger logger = Logger.getLogger(NIOServer.class) ; 
	static int BLOCK = 4096;
	protected Selector selector;
	protected CharsetDecoder decoder;

	public NIOServer(int port) throws IOException {
		selector = this.getSelector(port);
		Charset charset = Charset.forName("utf-8");
		decoder = charset.newDecoder();
	}

	// 获取Selector
	protected Selector getSelector(int port) throws IOException {
		ServerSocketChannel server = ServerSocketChannel.open();
		Selector sel = Selector.open();
		server.socket().bind(new InetSocketAddress(port));
		server.configureBlocking(false);
		server.register(sel, SelectionKey.OP_ACCEPT);
		return sel;
	}

	// 监听端口
	public void listen() {
		try {
			for (;;) {
				selector.select();
				Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					SelectionKey key = (SelectionKey) iter.next();
					iter.remove();
					try{
						handleKey(key);
//						Thread.sleep(100) ; 
					}catch(Exception e){
					}
				}
				
//				
//				for(SelectionKey key : keys){
//					handleKey(key) ; 
//					keys.remove(key) ; 
//				}
			}
		} catch (IOException e) {
		}

	}

	// 处理事件
	protected void handleKey(SelectionKey key) throws IOException {
		if (key.isAcceptable()){ // 接收请求
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel channel = server.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()){ // 读信息
			SocketChannel channel = (SocketChannel) key.channel();
			logger.info("ip is " + channel.socket().getInetAddress().getHostName()) ; 
			ByteBuffer clientBuffer = ByteBuffer.allocate(BLOCK); 
			try{
				int count = channel.read(clientBuffer);
				if (count >= 0) {
					clientBuffer.flip();
					CharBuffer charBuffer = decoder.decode(clientBuffer);
					SelectionKey wkey = channel.register(selector,
							SelectionKey.OP_WRITE);
					HttpRequest http = new HttpRequest(charBuffer.toString()) ;
					wkey.attach(new HandleClient(http));
				} else {
					channel.close();
					clientBuffer.clear();
				}
			}catch(Exception e){
				channel.close();
				clientBuffer.clear();
				logger.info(e.getMessage()) ; 
			}
		} else if (key.isWritable()){  // 写事件
			SocketChannel channel = (SocketChannel) key.channel();
			HandleClient handle = (HandleClient) key.attachment();
			ByteBuffer block = handle.readBlock();
			if (block != null) {
				channel.write(block);
			} else {
				handle.close();
				channel.close();
			}
			key.interestOps(SelectionKey.OP_READ);
			handle.close();
			channel.close();
		}
	}

	public static void main(String[] args) {
		System.out.println( Runtime.getRuntime().totalMemory() / 1024 /1024+ "MB");
		for(int i = 0 ; i < 200002 ; i ++ ){
			Queue.put("test", "key_" + i, "value-"+ i + "中文") ; 
		}
		try {
			int port = 1218;
			PropertiesManager pm = PropertiesManager.getManager("common") ; 
			String p = pm.getString("port") ;
			try{
				port = Integer.parseInt(p) ;
			}catch(Exception e){
			}
			NIOServer server = new NIOServer(port);
			System.out.println("Listening on " + port);
			//while (true) {
				server.listen();
			//}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}