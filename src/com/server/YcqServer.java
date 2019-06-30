package com.server;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.main.YcqThread;


public class YcqServer extends Server {
	private YcqThread ycqThread;
	private String userid;
	private String password;
	public YcqServer(String userid,String password,JTextField jtf,JLabel jLabel) {
		super(jtf,jLabel);
		this.userid=userid;
		this.password=password;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(ycqThread==null){
			ycqThread=new YcqThread(userid,password,this);
			ycqThread.start();
		}else{
			synchronized (ycqThread) {
				ycqThread.notify();
			}
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		ycqThread.stopReceive();
	}

}
