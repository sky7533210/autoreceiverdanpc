package com.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.Config;
import com.server.Server;

public class XkqThread extends Thread {
	private Server server;
	private String userid;
	private String password;
	private MyWebSocketClient myWebSocketClient;
	public XkqThread(String userid, String password, Server server) {
		// TODO Auto-generated constructor stub
		this.server=server;
		this.userid=userid;
		this.password=password;
	}
	@Override
	public void run() {
		simulate();
	}
	public void simulate() {
		Map<String, String> user = new HashMap<String, String>();	
		user.put("username", userid);
		user.put("pwd", password);
		user.put("code", "");
		user.put("cid", "passport");
		Connection con = Jsoup.connect("http://aaa.698mn.com/passport/login.html");
		con.method(Method.POST);
		con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);
		con.data(user);
		Response rs=null;
		try {
			rs = con.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			server.printMessage("网络故障,请重新开始");
		}
		String rsString = rs.body();
		//System.out.println(rsString);
		Map<String, String> cookie=rs.cookies();
	
		con = Jsoup.connect("http://aaa.698mn.com/site/index.html");
		con.cookies(cookie);
		con.method(Method.GET);
		con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);
		try {
			rs = con.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			server.printMessage("网络故障,请重新开始");
		}
		String body=rs.body();
		
		String ipPortRegEx ="((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?):\\d+";
		String useridRegEx="UserID=\\d+";
		String guidRegEx="guid =\"\\w+\"";
		String ipPort="";
		String uid="";
		String guid="";
		
		Pattern p = Pattern.compile(ipPortRegEx);
		Matcher m = p.matcher(body);
		if (m.find()) {
			ipPort = m.group() ;
			
		}
		p = Pattern.compile(useridRegEx);
		m = p.matcher(body);
		if (m.find()) {
			 uid = m.group() ;
		}
		p = Pattern.compile(guidRegEx);
		m = p.matcher(body);
		if (m.find()) {
			 guid = m.group();
		}
		guid=guid.replace("\"", "");
		guid=guid.replace(" ", "");
		guid=guid.replace("guid", "token");
		//System.out.println(ipPort+"|   |"+uid+"|   |"+guid);
		guid="944a76a2101172d98dbe785d22cee347";
		ipPort="123.207.254.171:9977";
		String url="ws://"+ipPort+"/Task?"+uid+"&TaskPrice=&DownTaskPoint=0&TaskCategory=0&"+guid;
		try {
			myWebSocketClient= new MyWebSocketClient(url,server);
			myWebSocketClient.setConnectionLostTimeout(0);
			myWebSocketClient.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			server.printMessage(e.getMessage());
		}
		
	}
	public void close(){
		if(myWebSocketClient!=null){
			myWebSocketClient.closeConnection(1, "");
		}
	}
}
