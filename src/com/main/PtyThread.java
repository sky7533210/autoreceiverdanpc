package com.main;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.Config;
import com.server.Server;

public class PtyThread extends Thread {
	private Server server;
	private String userid;
	private String password;
	public PtyThread(String userid, String password, Server server) {
		super();
		this.server = server;
		this.userid=userid;
		this.password=password;
	}
	@Override
	public void run() {
		try {
			simulateLogin();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void simulateLogin() throws Exception {
		Map<String, String> cookies=new HashMap<String,String>();
		cookies.put("buyer_notice_read", "1");
		Connection con = Jsoup.connect( "http://hy.pty668.com/buyer/login.jsp");  // 获取connection
        con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);   // 配置模拟浏览器
        Response rs = con.execute();
        Map<String, String> tcookies = rs.cookies();
        for(String s:tcookies.keySet()){
        if(s.equals("JSESSIONID"))
        	cookies.put("JSESSIONID", tcookies.get(s));
        
        }
        System.out.println("进入登入页面======>"+cookies);
        
        Map<String, String> user=new HashMap<String,String>();
         user.put("userName", userid);
         user.put("password", password);
        Connection con2 = Jsoup.connect("http://hy.pty668.com/buyer/do_login.jsp?method=login");
        con2.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);
      
        Response login = con2.ignoreContentType(true).followRedirects(true).method(Method.POST)
                                .data(user).cookies(cookies).execute();
       // System.out.println("登陆====>"+login.body());
       Map<String, String> map = login.cookies();
        for (String s : map.keySet()) {
            if(s.equals("JSESSIONID"))
            	cookies.put("JSESSIONID",map.get(s)); 
            if(s.equals("_wcq_buyer_"))
                	cookies.put("_wcq_buyer_",map.get(s));
        }
        Connection con3 = Jsoup.connect("http://hy.pty668.com/buyer/action/do_index.jsp?method=getTaskCount");
        con3.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);
      
        Response getCount = con3.ignoreContentType(true).followRedirects(true).method(Method.POST)
                                .data().cookies(cookies).execute();
        System.out.println("获取可接单数====>"+getCount.body());
        server.printMessage(getCount.body());
        Map<String, String> map1 = getCount.cookies();
        for (String s : map1.keySet()) {
            if(s.equals("JSESSIONID"))
            	cookies.put("JSESSIONID",map1.get(s)); 
        
        }
        Connection con4 = Jsoup.connect("http://hy.pty668.com/buyer/action/do_index.jsp");
        con4.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);
        Response rs1 = con4.ignoreContentType(true).followRedirects(true).method(Method.POST)
                               .data().cookies(cookies).execute();
        Map<String, String> map2 = rs1.cookies();
        for (String s : map2.keySet()) {
            if(s.equals("JSESSIONID"))
            	cookies.put("JSESSIONID",map2.get(s)); 
        }
        Map<String,String> datas=new HashMap<String,String>();
        datas.put("appTask", "1");
        datas.put("highestPrice", "0");
        datas.put("pcTask", "1");
        datas.put("sekectChks", "0");
        datas.put("stoken", "1");
        datas.put("askType", "0");
        boolean falg=true;
        while(falg){
        Connection con5 = Jsoup.connect("http://hy.pty668.com/buyer/action/do_index.jsp?method=getTask");
        con5.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);
        Response rs2 = con5.ignoreContentType(true).followRedirects(true).method(Method. POST)
                               .data(datas).cookies(cookies).execute();
        JSONObject jsonObject0=JSON.parseObject(rs2.body());
        server.printMessage(jsonObject0.getString("message"));
        System.out.println("getdan====>"+rs2.body());
        
        Map<String, String> map3 = rs2.cookies();
        if(map3.size()>0)
        for (String s : map3.keySet()) {
            if(s.equals("JSESSIONID"))
            	cookies.put("JSESSIONID",map3.get(s)); 
         
        }
        boolean flag2=true;
        while(flag2){
        	 Thread.sleep(5000);
             Connection con6 = Jsoup.connect("http://hy.pty668.com/buyer/action/do_index.jsp?method=getResult&type=2");
             con5.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);
             Response rs3 = con6.ignoreContentType(true).followRedirects(true).cookies(cookies).method(Method. POST).execute();
             System.out.println("getdan====>"+rs3.body());  
             JSONObject jsonObject=JSON.parseObject(rs3.body());
             server.printMessage(jsonObject.getString("message"));
             if(jsonObject.getInteger("status")==-999){
            	 continue;
             }else if(jsonObject.getInteger("status")==-99){
            	 flag2=false;
            	 System.out.println("失败状态："+jsonObject.getInteger("status"));           	  	        			 
             }else{
            	 System.out.println("接到单的状态"+jsonObject.getInteger("status"));
            	 new PlayerMusic().start();
            	 flag2=false;
            	 falg=false;     
             }
        }         
        }
	}
}
