package com.server;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.main.PtyThread;

public class PtyServer extends Server {
	private String userid;
	private String password;
	private PtyThread ptyThread;
	public PtyServer(String userid,String password,JTextField jtf,JLabel jLabel) {
		super(jtf,jLabel);
		this.userid=userid;
		this.password=password;
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(ptyThread!=null)
			ptyThread.interrupt();
		ptyThread=new PtyThread(userid,password,this);
		ptyThread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if(ptyThread!=null)
			ptyThread.interrupt();
	}

}
