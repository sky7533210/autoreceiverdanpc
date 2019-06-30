package com.server;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.main.XbtThread;

public class XbtServer extends Server {

	private String uid;
	private String pwd;
	public XbtServer(String uid,String pwd, JTextField jtf,JLabel jLabel) {
		super(jtf,jLabel);
		this.uid=uid;
		this.pwd=pwd;
		// TODO Auto-generated constructor stub
	}

	private XbtThread xbtThread;


	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(xbtThread!=null)
			xbtThread.interrupt();
		xbtThread=new XbtThread(uid,pwd,this);
		xbtThread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if(xbtThread!=null)
			xbtThread.interrupt();
	}

}
