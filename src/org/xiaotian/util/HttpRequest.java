package org.xiaotian.util;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private String str;
	private String method;
	private String request;
	private String post ; 
	private Map<String , Object> headers ;

	private String temp[] ; 

	public HttpRequest() {

	}

	public HttpRequest(String str) {
		this.str = str;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
	
	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}
	
	public Map<String , Object> getHeaders(){
		return this.headers ; 
	}

	private boolean testHttp(){
		temp = str.split("\r\n") ; 
		if(temp.length == 0){
			return false ; 
		}else{
			int index = temp[0].toLowerCase().indexOf("http");
			if(index == -1 ){
				return false ;
			}
		}
		return true ; 
	}
	
	public Map<String , Object> getData(){
		String data = "" ; 
		if(this.testHttp()){
			this.parse() ; 
			String url = this.getRequest() ;
			data = BaseUtil.convertUrl(url) ; 
			data = data.replace("/?", "") ; 
		}else{
			data = this.str ; 
		}
		
		return null ; 
	}
	
	private void parse() {
		temp = str.split("\r\n")  ; 
		for (int i = 0; i < temp.length; i++) {
//			System.out.println(i + temp[i]);
			String s = temp[i];
			if (s.toUpperCase().startsWith("GET")) {
				int index = s.toLowerCase().indexOf("http");
				this.setMethod("GET");
				this.setRequest(s.substring(4, index - 1));
			}else if(s.toUpperCase().startsWith("POST")){
				int index = s.toLowerCase().indexOf("http");
				this.setMethod("POST") ; 
				this.setRequest(s.substring(5, index - 1)) ;
			}else if (s.length() == 0 && this.method.equalsIgnoreCase("POST")) {
				this.setPost(temp[i + 1]) ; 
				break ; 
			}else if(s.length() == 0){
				break ; 
			}else{
				if(headers == null || headers.isEmpty()){
					headers = new HashMap<String , Object>() ;
				}
				String header[] = s.split(":") ; 
				String headerName = header[0] ; 
				String headerValue = s.substring(headerName.length() + 1) ; 
//				System.out.println(headerValue.trim());
				headers.put(headerName, headerValue.trim()) ; 
			}
		}
	}

	public static void main(String[] args) {
		String str = "GET /?namesdfskdjl HTTP/1.1\r\nUser-Agent: curl/7.30.0\r\nHost: localhost:1218\r\nAccept: */*\r\n\r\nsdfklsdfkl";
		HttpRequest r = new HttpRequest(str) ; 
//		r.parse() ; 
//		System.out.println( r.getHeaders().get("Host")) ; 
		r.getData() ; 

	}
}
