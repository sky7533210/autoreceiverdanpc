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

public class WcqThread extends Thread {
	
	private Server server;
	public WcqThread(Server server) {
		this.server = server;
	}
	@Override
	public void run() {
		try {
			receiver("", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			server.printMessage(e.getMessage());
		}
	}
	public void receiver(String userName, String pwd) throws Exception{
		
		Map<String, String> cookies=new HashMap<String,String>();
		cookies.put("buyer_notice_read", "1");
		Connection con = Jsoup.connect("http://hy.wcq668.com/buyer/login.jsp");  // 获取connection
        con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);   // 配置模拟浏览器
        Response rs = con.execute();
        Map<String, String> tcookies = rs.cookies();
        for(String s:tcookies.keySet()){
        if(s.equals("JSESSIONID"))
        	cookies.put("JSESSIONID", tcookies.get(s));        
        }
        System.out.println("进入登入页面======>"+cookies);
        
        Map<String, String> user=new HashMap<String,String>();
         user.put("userName", userName);
         user.put("password", pwd);
        Connection con2 = Jsoup.connect("http://hy.wcq668.com/buyer/do_login.jsp?method=login");
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
        Connection con3 = Jsoup.connect("http://hy.wcq668.com/buyer/action/do_index.jsp?method=getTaskCount");
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
        Connection con4 = Jsoup.connect("http://hy.wcq668.com/buyer/action/do_index.jsp");
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
        boolean flag=true;
        while(flag){
        	
        Connection con5 = Jsoup.connect("http://hy.wcq668.com/buyer/action/do_index.jsp?method=getTask");
        con5.header(Config.USER_AGENT, Config.USER_AGENT_VALUE);
        Response rs2 = con5.ignoreContentType(true).followRedirects(true).method(Method. POST)
                               .data(datas).cookies(cookies).execute();
      
        JSONObject jsonObject=JSON.parseObject(rs2.body());
        server.printMessage(jsonObject.getString("message"));
      if(jsonObject.getInteger("status")!=-13){
    	  flag=false;
    	  new PlayerMusic().start();
      }
        Map<String, String> map3 = rs2.cookies();
        if(map3.size()>0)
        for (String s : map3.keySet()) {
            if(s.equals("JSESSIONID"))
            	cookies.put("JSESSIONID",map3.get(s)); 
        }
        Thread.sleep(3000);
        }
	}
}
