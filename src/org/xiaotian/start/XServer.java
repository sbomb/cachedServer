package org.xiaotian.start;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.xiaotian.util.HttpRequest;

public  class XServer implements Runnable {

	private SelectionKey key ; 
	private Selector selector ; 
	static int BLOCK = 4096;
	CharsetDecoder decoder;
	
	public XServer(SelectionKey key ,Selector selector){
		this.key = key ; 
		this.selector = selector ;
		Charset charset = Charset.forName("utf-8");
		decoder = charset.newDecoder();
	}
	
	@Override
	public void run() {
		long start = System.currentTimeMillis() ; 
		try {
			handleKey(key) ;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		long end = System.currentTimeMillis() ;
		System.out.println((end - start)  + "ms");
	}
	
	void handleKey(SelectionKey key) throws IOException {
		if (key.isAcceptable()){ // 接收请求
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel channel = server.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()){ // 读信息
			SocketChannel channel = (SocketChannel) key.channel();
			System.out.println("ip is " + channel.socket().getInetAddress().getHostName()) ; 
			ByteBuffer clientBuffer = ByteBuffer.allocate(BLOCK); 
			try{
				int count = channel.read(clientBuffer);
				if (count >= 0) {
					clientBuffer.flip();
					CharBuffer charBuffer = decoder.decode(clientBuffer);
					SelectionKey wkey = channel.register(selector,
							SelectionKey.OP_WRITE);
					System.out.println("****" + charBuffer.toString());
					HttpRequest http = new HttpRequest(charBuffer.toString()) ;
					wkey.attach(new HandleClient(http));
				} else {
					channel.close();
					clientBuffer.clear();
				}
			}catch(Exception e){
				channel.close();
				clientBuffer.clear();
			}
		} else if (key.isWritable()){  // 写事件
			SocketChannel channel = (SocketChannel) key.channel();
			HandleClient handle = (HandleClient) key.attachment();
			ByteBuffer block = handle.readBlock();
			try{
				if (block != null) {
					channel.write(block);
				} else {
					handle.close();
					channel.close();
				}
			}catch(Exception e){
				
			}
			key.interestOps(SelectionKey.OP_READ);
			handle.close();
			channel.close();
		}
	}

}
