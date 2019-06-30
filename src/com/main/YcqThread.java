package com.main;

import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.Config;
import com.server.Server;
public class YcqThread extends Thread {
	private String userid;
	private String token;
	private boolean isReceiving = true;
	private PlayerMusic playerMusic;
	private Server server;
	private byte errorcode=0;
	public YcqThread(String userid, String token,Server server) {
		this.userid = userid;
		this.token = token;
		this.server=server;
	}
	@Override
	public void run() {
		simulate();
	}
	public void stopReceive(){
		isReceiving=false;
		if(playerMusic!=null){
			playerMusic.close();
		}		
	}
	public void simulate() {
		while (isReceiving) {
			try {
				Map<String, String> user = new HashMap<String, String>();
				user.put("token", token);
				user.put("userid", userid);
				user.put("type", "1");
				user.put("masktype", "2");
				user.put("minmoney", "0");
				user.put("maxmoney", "");
				user.put("datetime", "1");
				user.put("recaptchaRsp", "03AOLTBLSb6WF-stzv5I1_VGngIEppjbwkC9iy23wlWgJBB_yyff-PkSTOkicdiVHjmyLxr7cY6vMgv3kYr-umEhjBOB_BwGKvSNQyL3SF7B2WoW6Vv9aOJM0wOpD3WAQUKA6ReDBa380dys67dsaK2xZJz8mbOozilLbJyTcHDUePSzIlZ94yfubsvd5mONoyr8AUBaUUFrc_8-H-8Zmya1tT9uNXfTs6smGQch96nEMnJqHsgnIB0o1uZt1sH-Fsg-ZasOwkkTiQhF7TziTo1hy0mu5VVYjEnX9iDEnd1JnmGCtmWmHaa9O49vgEVHJYrIxMezd1cojORDVTY06y9s8K9K97Wc4H3A");
				Connection con = Jsoup.connect("http://api.xgkst.com/api/brushuser/matchMask");
				con.method(Method.POST);
				con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);
				con.data(user);
				Response rs = con.execute();
				String rsString = rs.body();
				//System.out.println(rsString);
				JSONObject jsonObject;
				jsonObject= JSON.parseObject(rsString);
				if(!jsonObject.getBoolean("errcode")){
					server.changeState("已停止");
					server.printMessage(jsonObject.getString("errmsg"));
					break;
				}
				jsonObject = JSON.parseObject(jsonObject.getString("resultObj"));
				server.changeState("接单中");
				server.printMessage("当前排队人数：" + jsonObject.getString("queueOrder"));
				Map<String, String> requestData = new HashMap<String, String>();
				requestData.put("token", token);
				requestData.put("userid", userid);
				requestData.put("applyid", jsonObject.getString("applyid"));
				int applystatus = 0;
				do {
					try {
						Thread.sleep(5500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					con = Jsoup.connect("http://api.xgkst.com/api/brushuser/queyrMatchResult");
					con.header(Config.USER_AGENT, Config.USER_AGENT_VALUE2);
					con.method(Method.POST);
					con.data(requestData);
					rs = con.execute();				
					rsString = rs.body();
					//System.out.println(rsString);
					jsonObject = JSON.parseObject(rsString);
					jsonObject = JSON.parseObject(jsonObject.getString("resultObj"));
					applystatus = jsonObject.getInteger("applystatus");
					if(applystatus==0){					
					 server.printMessage("当前排队人数：" + jsonObject.get("queueOrder"));
					}
					if (applystatus != 0 && applystatus != 2){
						server.changeState("接到单");
						playerMusic=new PlayerMusic();
						playerMusic.start();
						isReceiving=false;
					}
				} while (applystatus == 0);
				if(!isReceiving){
					synchronized (this) {
						if(applystatus==0||applystatus==2)
							server.changeState("已停止");
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						isReceiving=true;
					}
				}
			}catch (NoRouteToHostException|UnknownHostException e){
				System.out.println("网络中断");			
				errorcode=1;
			}catch(SocketTimeoutException e){
				System.out.println("网络超时");
				errorcode=2;
			}catch (Exception e) {
				System.out.println("未知错误，正在尝试重新接单");
				errorcode=3;
				e.printStackTrace();
			}
			if(errorcode>0){
				server.changeState("已停止");
				switch (errorcode) {
				case 1:
					server.printMessage("网络中断，正在尝试重新接单");
					break;
				case 2:
					server.printMessage("连接超时，正在尝试重新接单");
					break;
				default:
					server.printMessage("未知错误，正在尝试重新接单");
					break;
				}								
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {				
					e1.printStackTrace();
				}
			}
			try {
				Thread.sleep(35000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
