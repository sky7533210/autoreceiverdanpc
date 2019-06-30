package com.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.bean.Config;

public class Test1 {

	private static MyWebSocketClient myWebSocketClient;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Map<String, String> user = new HashMap<String, String>();	
		user.put("username", "15170166610");
		user.put("pwd", "xrt971017");
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
			//server.printMessage("网络故障,请重新开始");
		}
		String rsString = rs.body();
		System.out.println(rsString);
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
			//server.printMessage("网络故障,请重新开始");
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
		System.out.println(ipPort+"|   |"+uid+"|   |"+guid);		
		String url="ws://"+ipPort+"/Task?"+uid+"&TaskPrice=&DownTaskPoint=0&TaskCategory=0&"+guid;
		try {
			myWebSocketClient= new MyWebSocketClient(url,null);
			myWebSocketClient.setConnectionLostTimeout(0);
			myWebSocketClient.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("位置1");
			e.printStackTrace();
			//server.printMessage(e.getMessage());  02_392cf64e-9c29-42fb-a60b-6c3bcf17aeca
			
}		}
}