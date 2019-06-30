package com.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.bean.Config;
import com.server.General;
import com.server.Server;

public class EsxThread extends Thread {
	private Server server;
	private String userid;
	private String password;
	private MyWebSocketClient myWebSocketClient;
	public EsxThread(String userid, String password, Server server) {
		this.server = server;
		this.userid = userid;
		this.password = password;
	}
	@Override
	public void run() {
		simulate();
	}
	public void simulate() {
		
		Map<String, String> cookie = null;
		Map<String,String> cookieTemp=null;
		Connection con = Jsoup.connect("http://aaa.18zang.com/passport/login.html");
		con.method(Method.GET);
		con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);	
		Response rs = null;
		try {
			rs = con.execute();
			cookie=rs.cookies();
			Document document= rs.parse();
			document.getElementsByTag("title").text();
			StringBuilder sBuilder=new StringBuilder( document.getElementsByTag("img").attr("src"));
			sBuilder.replace(0, 22, "");
			
			String img = General.getCode(sBuilder.toString());
			
			con = Jsoup.connect("http://aaa.18zang.com/index.php?security_verify_img="+img);
			cookie.put("srcurl", "687474703a2f2f6161612e31387a616e672e636f6d2f70617373706f72742f6c6f67696e2e68746d6c");
			con.method(Method.GET);
			con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);
			con.cookies(cookie);
			rs= con.execute();
			
			cookieTemp=rs.cookies();
			cookie.putAll(cookieTemp);
		} catch (IOException e) {
			e.printStackTrace();
			server.printMessage("�������,�����¿�ʼ");
		}
		String rsString;
	
		Map<String, String> user = new HashMap<String, String>();
		user.put("username", userid);
		user.put("pwd", password);
		user.put("code", "");
		user.put("cid", "passport");		
		
		con = Jsoup.connect("http://aaa.18zang.com/passport/login.html");
		con.method(Method.POST);
		con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);	
		con.cookies(cookie);		
		con.data(user);
		
		try {
			rs = con.execute();			
		} catch (IOException e) {
			e.printStackTrace();
			server.printMessage("�������,�����¿�ʼ");
		}
		 rsString = rs.body();
		System.out.println(rsString);
		cookieTemp=rs.cookies();
		cookie.putAll(cookieTemp);		
		
		
		con = Jsoup.connect("http://aaa.18zang.com/site/index.html");
		con.cookies(cookie);
		con.method(Method.GET);
		con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);
		try {
			rs = con.execute();
		} catch (IOException e) {
			e.printStackTrace();
			server.printMessage("�������,�����¿�ʼ");
		}
		String body = rs.body();

		String ipPortRegEx = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?):\\d+";
		String useridRegEx = "UserID=\\d+";
		String guidRegEx = "guid =\"\\w+\"";
		String ipPort = "";
		String uid = "";
		String guid = "";

		Pattern p = Pattern.compile(ipPortRegEx);
		Matcher m = p.matcher(body);
		if (m.find()) {
			ipPort = m.group();

		}
		p = Pattern.compile(useridRegEx);
		m = p.matcher(body);
		if (m.find()) {
			uid = m.group();
		}
		p = Pattern.compile(guidRegEx);
		m = p.matcher(body);
		if (m.find()) {
			guid = m.group();
		}
		guid = guid.replace("\"", "");
		guid = guid.replace(" ", "");
		guid = guid.replace("guid", "token");
		String url = "ws://" + ipPort + "/Task?" + uid + "&TaskPrice=&DownTaskPoint=0&TaskCategory=0&" + guid;
		try {
			myWebSocketClient = new MyWebSocketClient(url, server);
			myWebSocketClient.setConnectionLostTimeout(0);
			myWebSocketClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
			server.printMessage(e.getMessage());

		}
	}
	public void close(){
		if(myWebSocketClient!=null){
			myWebSocketClient.close();
		}
	}
}