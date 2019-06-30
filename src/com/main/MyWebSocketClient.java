package com.main;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.Server;

public class MyWebSocketClient extends WebSocketClient {
	private Server server;
	private PlayerMusic pm;
	public MyWebSocketClient(String serverURI,Server server) throws Exception {			
		super(new URI(serverURI));
		this.server=server;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		// TODO Auto-generated method stub
		System.out.println("arg0:"+arg0+"arg1:"+arg1+"arg2:"+arg2);
		server.printMessage("已停止接单");
	}

	@Override
	public void onError(Exception arg0) {
		// TODO Auto-generated method stub
		arg0.printStackTrace();
		server.printMessage(arg0.getMessage());
	}

	@Override
	public void onMessage(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("onmessage:"+arg0);
		JSONObject jsonObject=JSON.parseObject(arg0);
		String message=jsonObject.getString("Description");
		server.printMessage(message);
	
		int rType=jsonObject.getIntValue("RType");
		if(rType==100||rType==101){
		  pm=new PlayerMusic();
		  pm.start();
		}
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		System.out.println("握手"+arg0);
		//this.send("hearbear_p31974");
		server.printMessage("已经开始接单");
	}
	public void closeMusic(){
		if(pm!=null){
		pm.close();
		}
		
	}
}
