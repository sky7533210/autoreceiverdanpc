package com.server;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.main.EsxThread;

public class EsxServer extends Server{
	private EsxThread thread;
	private String userid;
	private String password;
	public EsxServer(String userid,String password,JTextField jtf,JLabel jLabel) {
		super(jtf,jLabel);
		this.userid=userid;
		this.password=password;
	}
	@Override
	public void start() {
		thread=new EsxThread(userid,password,this);
		thread.start();
	}
	@Override
	public void stop() {
		if(thread!=null)
			thread.close();
		changeState("ÒÑÍ£Ö¹");
	}
}
