package com.server;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.main.WcqThread;

public class WcqServer extends Server {

	public WcqServer(JTextField jtf,JLabel jLabel) {
		super(jtf,jLabel);
		// TODO Auto-generated constructor stub
	}

	private WcqThread wcqThread;

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(wcqThread!=null)
			wcqThread.interrupt();
		wcqThread=new WcqThread(this);
		wcqThread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if(wcqThread!=null)
		wcqThread.interrupt();
	}

}
