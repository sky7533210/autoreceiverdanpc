package com.server;

import javax.swing.JLabel;
import javax.swing.JTextField;

public abstract class Server {
	public Server(JTextField jtf,JLabel jLabel) {
		// TODO Auto-generated constructor stub
		this.jtf=jtf;
		this.jLabel=jLabel;
	}
	private JTextField jtf;
	private JLabel jLabel;
	public abstract void start();
	public abstract void stop();
	public void printMessage(String message) {
		// TODO Auto-generated method stub
		jtf.setText(message);
	}
	public void changeState(String state) {
		// TODO Auto-generated method stub
		jLabel.setText(state);
	}
}
