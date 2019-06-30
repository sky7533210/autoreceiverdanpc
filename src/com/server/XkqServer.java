package com.server;


import javax.swing.JLabel;
import javax.swing.JTextField;

import com.main.MyWebSocketClient;
import com.main.XkqThread;

public class XkqServer extends Server{
	private XkqThread thread;
	private String userid;
	private String password;
	public XkqServer(String userid,String password,JTextField jtf,JLabel jLabel) {
		super(jtf,jLabel);
		this.userid=userid;
		this.password=password;
	}
	@Override
	public void start() {
		thread=new XkqThread(userid,password,this);
		thread.start();
	}

	@Override
	public void stop() {
		if(thread!=null)
			thread.close();
		changeState("ÒÑÍ£Ö¹");
	}
}
