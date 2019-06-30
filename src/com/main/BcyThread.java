package com.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.Data;
import com.bean.KeyWord;
import com.bean.Task;

public class BcyThread extends Thread {

	Map<String, String> cookie = new HashMap<String, String>();
	private String taobaoId;
	
	public static void main(String[] args) {
		new BcyThread().start();
	}

	@Override
	public void run() {
		Map<String, String> parameter = new HashMap<String, String>();
		
		parameter.put("c", "ShouYe");
		parameter.put("a", "getApageTask");
		parameter.put("name", "18679447632");
		parameter.put("pass", "kun7533210");
		boolean flag = true;
		int page=0;
		while (flag) {
			int status = 0;
			while (status == 0) {
				++page;
				parameter.put("page", ""+page);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Connection con = Jsoup.connect("http://fz.taodouyu.com/taomessage/index.php");
				con.ignoreContentType(true);
				con.method(Method.GET);
				con.data(parameter);
				con.cookies(cookie);
				try {
					Response rs = con.execute();
					cookie.putAll(rs.cookies());				
					Data data = JSON.parseObject(rs.body(), Data.class);
					status=data.getStatus();
					if(status==0) {
						List<Task> tasks=data.getData();
						for(Task task:tasks) {
							if(task.getCando()==1) {
								
								KeyWord keyWord=clickDan(task.getTrueid());
								int state=receiveDan(task.getTrueid(), keyWord);
								if(state==0) {
									new PlayerMusic().start();
									flag=false;
									status=1;
									break;
								}else if(state==9) {
									flag=false;
									status=1;
									System.out.println("24小时之内能接三单");
									break;
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			page=0;
		}

	}
	
	/**
	 * 点击单
	 * @param taskid
	 * @throws IOException 
	 */
	public KeyWord clickDan(int taskid) throws IOException {
		
		Map<String, String> parameter = new HashMap<String, String>();
		
		parameter.put("c", "ShouYe");
		parameter.put("a", "userandorder");
		parameter.put("name", "18679447632");
		parameter.put("pass", "kun7533210");
		parameter.put("taskid", taskid+"");
		parameter.put("v", "62");
		
		Connection con = Jsoup.connect("http://fz.taodouyu.com/taomessage/index.php");
		con.ignoreContentType(true);
		con.method(Method.GET);
		con.data(parameter);
		con.cookies(cookie);
		
		Response rs=con.execute();
		cookie.putAll(rs.cookies());
		String json=rs.body();		
		JSONObject jsonObject=JSON.parseObject(json);
		taobaoId=jsonObject.getString("buyer1");
		json=jsonObject.getString("keywordInfo");
		return JSON.parseObject(json,KeyWord.class);
	}
	//点击接单按钮
	public int receiveDan(int taskid,KeyWord keyWord) throws IOException {
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("name", "18679447632");
		parameter.put("pass", "kun7533210");
		parameter.put("taskid",taskid+"");
		parameter.put("taobaoaccount", taobaoId);
		parameter.put("keyword", keyWord.getKeyword());
		parameter.put("keywordid", keyWord.getKeywordid());
		
		Connection con = Jsoup.connect("http://fz.taodouyu.com/index.php/TaobaoTask/regtask");
		con.ignoreContentType(true);
		con.method(Method.GET);
		con.data(parameter);
		con.cookies(cookie);
		Response rs= con.execute();
		
		String json=rs.body();
		System.out.println(json);
		JSONObject jsonObject= JSON.parseObject(json);
		int status=jsonObject.getIntValue("status");
		return status;
	}
}
