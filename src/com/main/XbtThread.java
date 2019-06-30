package com.main;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.Config;
import com.server.Server;

public class XbtThread extends Thread {
	private Server server;
	private String uid;
	private String pwd;
	public XbtThread(String uid, String pwd, Server server) {
		this.server=server;
		this.uid=uid;
		this.pwd=pwd;
	}
	@Override
		public void run() {
			try {
				receiver();
			} catch (Exception e) {
				e.printStackTrace();
				server.printMessage(e.getMessage());
			}
		}
public void receiver() throws Exception{		
		Map<String, String> user=new HashMap<String,String>();
		System.out.println(uid);
		System.out.println(pwd);
        user.put("mobile", uid);
        user.put("password", pwd);
        user.put("key", "");
		Connection con = Jsoup.connect("http://www.xiaobay.com/api/index/doLogin");
        Response response= con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2).header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        		.header("X-Requested-With", "XMLHttpRequest").ignoreContentType(true)
        		.method(Method.POST).data(user).execute();
        String key= anylizeJson(anylizeJson(response.body(), "data"),"token");
        server.printMessage("登入成功key="+key);
        Connection con1 = Jsoup.connect("http://www.xiaobay.com/api/order/getBuyerAccountBindingList");
        Response response2= con1.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2).header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        		.header("X-Requested-With", "XMLHttpRequest").ignoreContentType(true)
        		.method(Method.POST).data("key", key).execute();
        server.printMessage("账户信息"+response2.body());
        String accountId= anylizeJson2(anylizeJson(anylizeJson(response2.body(), "data"),"list"),"accountId");
        server.printMessage("accountid="+accountId);
        
        Map<String, String> account=new HashMap<String,String>();
        account.put("key", key);
        account.put("accountId", accountId);
        boolean flag=true;      
        while(flag){
        	 Connection con2 = Jsoup.connect("http://www.xiaobay.com/api/order/doStartReceiptTask");
        	 Response response3= con2.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2).header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
             		.header("X-Requested-With", "XMLHttpRequest").ignoreContentType(true)
             		.method(Method.POST).data(account).execute();
        	server.printMessage("开始接单");
        	 if(!anylizeJson(response3.body(), "data").equals("[]")){
        		 flag=false;
        		 server.printMessage("接单成功"+anylizeJson(response3.body(), "data"));
        	 }
        	 Thread.sleep(3000);
        	 Connection con3 = Jsoup.connect("http://www.xiaobay.com/api/order/doStopReceiptTask");
        	 Response response4= con3.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2).header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
             		.header("X-Requested-With", "XMLHttpRequest").ignoreContentType(true)
             		.method(Method.POST).data(account).execute();
        	 server.printMessage("停止接单");
        	 if(!anylizeJson(response4.body(), "data").equals("[]")){
        		 flag=false;
        		 server.printMessage("接单成功"+anylizeJson(response3.body(), "data"));
        	 }
        	 Thread.sleep(10000);
        }
        new PlayerMusic().start();
	}
	public static String anylizeJson(String data,String key){
		System.out.println(data);
		System.out.println(key);
		 JSONObject jsonObject=JSON.parseObject(data);
	     return jsonObject.getString(key);
	}
	public static String anylizeJson2(String data,String key){
		 JSONArray jsonArray=JSON.parseArray(data);		 
	     return jsonArray.getJSONObject(0).getString(key);
	}
}
