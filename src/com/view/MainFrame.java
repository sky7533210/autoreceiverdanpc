package com.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.alibaba.fastjson.JSON;
import com.bean.User;
import com.main.YcqThread;
import com.server.EsxServer;
import com.server.Server;
import com.server.XbtServer;
import com.server.XkqServer;
import com.server.YcqServer;


public class MainFrame implements ActionListener{
	
	private Font font=new Font("宋体", Font.PLAIN, 24);
	private JFrame frame;
	private JTextField jtfWcq;
	private JTextField jtfPty;
	private JTextField jtfXkq;
	private JTextField jtfXbt;
	private JTextField jtfEsx;
	private JTextField jtfUserid;
	private JPasswordField jpfWcq;
	private JPasswordField jpfPty;
	private JPasswordField jpfXkq;
	private JPasswordField jpfXbt;
	private JPasswordField jpfEsx;
	private JTextField jtfToken;
	private JLabel jLabelWcq;
	private JLabel jLabelPty;
	private JLabel jLabelXkq;
	private JLabel jLabelXbt;
	private JLabel jLabelEsx;
	private JLabel jLabelYcq;
	private JTextField jtfMsgWcq;
	private JTextField jtfMsgPty;
	private JTextField jtfMsgXkq;
	private JTextField jtfMsgXbt;
	private JTextField jtfMsgEsx;
	private JTextField jtfMsgYcq;
	private boolean isReceivingWcq;
	private boolean isReceivingPty;
	private boolean isReceivingXkq;
	private boolean isReceivingXbt;
	private boolean isReceivingYcq;
	private boolean isReceivingEsx;
	private JButton jbtnWcq;
	private JButton jbtnPty;
	private JButton jbtnXkq;
	private JButton jbtnXbt;
	private JButton jbtnYcq;
	private JButton jbtnEsx;
	private Server ycqServer;
	private Server xkServer;
	private Server xbtServer;
	private Server esxServer;
	private Server ptyServer;

	public static void main(String[] args){
		 new MainFrame();
	}
	public MainFrame() {
		
		frame=new JFrame("自动接单助手");
		setUIFont();
		initView();
		initWindow();
		new Thread(new Runnable() {
			public void run() {
				mapToWindow(readConfig());
			}
		}).start();
	}
	public void initWindow(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		
		frame.pack();
		frame.setResizable(false);
		frame.setLocation((screenWidth-frame.getWidth())/2,(screenHeight-frame.getHeight())/2);
	
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				saveConfig();
				System.exit(0);
			}
		});
	}	
	public void initView(){
		
		Box box1=Box.createHorizontalBox();
		JPanel jPanel1=new JPanel();
		jPanel1.setLayout(new GridLayout(0, 1));
		
		jPanel1.add( new JLabel("平台",JLabel.CENTER));
		jPanel1.add(new JLabel("威创圈"));
		jPanel1.add(new JLabel("菩提园"));
		jPanel1.add(new JLabel("信客圈"));
		jPanel1.add(new JLabel("小白兔"));
		jPanel1.add(new JLabel("二师兄"));
		jPanel1.add( new JLabel());
		jPanel1.add(new JLabel("赢创圈"));
		box1.add(jPanel1);
		
		JPanel jPanel2=new JPanel();
		jPanel2.setLayout(new GridLayout(0, 1));
		
		jPanel2.add(new JLabel("账号",JLabel.CENTER));
		jtfWcq=new JTextField(15);
		jPanel2.add(jtfWcq);
		
		jtfPty=new JTextField(15);
		jPanel2.add(jtfPty);
		
		jtfXkq=new JTextField(15);
		jPanel2.add(jtfXkq);
		
		jtfXbt=new JTextField(15);
		jPanel2.add(jtfXbt);
		
		jtfEsx=new JTextField(15);
		jPanel2.add(jtfEsx);
		
		jPanel2.add(new JLabel("userid",JLabel.CENTER));
		jtfUserid=new JTextField(15);
		jPanel2.add(jtfUserid);
		
		box1.add(jPanel2);
		
		JPanel jPanel3=new JPanel();
		jPanel3.setLayout(new GridLayout(0, 1));
		
		jPanel3.add(new JLabel("密码",JLabel.CENTER));
		
		jpfWcq=new JPasswordField(15);
		jPanel3.add(jpfWcq);
		
		jpfPty=new JPasswordField(15);
		jPanel3.add(jpfPty);
		
		jpfXkq=new JPasswordField(15);
		jPanel3.add(jpfXkq);
		
		jpfXbt=new JPasswordField(15);
		jPanel3.add(jpfXbt);
		
		jpfEsx=new JPasswordField(15);
		jPanel3.add(jpfEsx);
		
		jPanel3.add(new JLabel("token",JLabel.CENTER));
		
		jtfToken=new JTextField(15);
		jPanel3.add(jtfToken);

		box1.add(jPanel3);
		
		JPanel jPanel4=new JPanel();
		jPanel4.setLayout(new GridLayout(0, 1));
		
		jPanel4.add(new JLabel("状态",JLabel.CENTER));
		
		jLabelWcq=new JLabel("已停止",JLabel.CENTER);
		jPanel4.add(jLabelWcq);
		
		jLabelPty=new JLabel("已停止",JLabel.CENTER);
		jPanel4.add(jLabelPty);
		
		jLabelXkq=new JLabel("已停止",JLabel.CENTER);
		jPanel4.add(jLabelXkq);
		
		jLabelXbt=new JLabel("已停止",JLabel.CENTER);
		jPanel4.add(jLabelXbt);
		
		jLabelEsx=new JLabel("已停止",JLabel.CENTER);
		jPanel4.add(jLabelEsx);
		
		jPanel4.add(new JLabel(""));
		
		jLabelYcq=new JLabel("已停止",JLabel.CENTER);
		jPanel4.add(jLabelYcq);
		
		box1.add(jPanel4);
		
		
		JPanel jPanel5=new JPanel();
		jPanel5.setLayout(new GridLayout(0, 1));
		
		jPanel5.add(new JLabel("消息",JLabel.CENTER));
		
		jtfMsgWcq=new JTextField(25);
		jPanel5.add(jtfMsgWcq);
		
		jtfMsgPty=new JTextField(25);
		jPanel5.add(jtfMsgPty);
		
		jtfMsgXkq=new JTextField(25);
		jPanel5.add(jtfMsgXkq);
		
		jtfMsgXbt=new JTextField(25);
		jPanel5.add(jtfMsgXbt);
		
		jtfMsgEsx=new JTextField(25);
		jPanel5.add(jtfMsgEsx);
		
		jPanel5.add(new JLabel());
		
		jtfMsgYcq=new JTextField(25);
		jPanel5.add(jtfMsgYcq);
		
		box1.add(jPanel5);
		
		box1.add(Box.createHorizontalStrut(20));
		
		JPanel jPanel6=new JPanel();
		jPanel6.setLayout(new GridLayout(0, 1));
		
		jPanel6.add(new JLabel("操作",JLabel.CENTER));
		
		jbtnWcq=new JButton("开始");
		jbtnWcq.setActionCommand("wcq");
		jbtnWcq.addActionListener(this);
		jPanel6.add(jbtnWcq);
		
		jbtnPty=new JButton("开始");
		jbtnPty.setActionCommand("pty");
		jbtnPty.addActionListener(this);
		jPanel6.add(jbtnPty);
		
		jbtnXkq=new JButton("开始");
		jbtnXkq.setActionCommand("xkq");
		jbtnXkq.addActionListener(this);
		jPanel6.add(jbtnXkq);
		
		jbtnXbt=new JButton("开始");
		jbtnXbt.setActionCommand("xbt");
		jbtnXbt.addActionListener(this);
		jPanel6.add(jbtnXbt);
		
		jbtnEsx=new JButton("开始");
		jbtnEsx.setActionCommand("esx");
		jbtnEsx.addActionListener(this);
		jPanel6.add(jbtnEsx);
		
		jPanel6.add(new JLabel(""));
		
		jbtnYcq=new JButton("开始");
		jbtnYcq.setActionCommand("ycq");
		jbtnYcq.addActionListener(this);
		jPanel6.add(jbtnYcq);
		
		box1.add(jPanel6);
		
		frame.add(box1);
	}
	
	private void setUIFont(){		
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof javax.swing.plaf.FontUIResource){
				UIManager.put(key,font);
			}
		}
	}
	
	public File getPath(){
		Properties prop = System.getProperties();
        File file=new File("C:/Users/"+prop.getProperty("user.name")+"/Documents/jiedan.config");
        if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return file;
	}
	public boolean saveConfig(){
		boolean flag=true;
		FileWriter fw=null;
		try {			
			fw=new FileWriter(getPath());
			fw.write(JSON.toJSONString(getConfig()));
		} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				flag=false;
		}finally {
			try {
				fw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}
	public User readConfig(){
		BufferedReader br=null;
		FileReader fr=null;
		User config=null;
		try {
			fr = new FileReader(getPath());
			br=new BufferedReader(fr);
			String strConfig= br.readLine();
			if(strConfig!=null&&!strConfig.equals("")){
				config= JSON.parseObject(strConfig, User.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fr!=null)
				try {
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return config;
	}
	public User getConfig(){
		User config=new User();
		
		config.setActive(true);
		
		config.setUidWcq(jtfWcq.getText().trim());
		config.setUidPty(jtfPty.getText().trim());
		config.setUidXkq(jtfXkq.getText().trim());
		config.setUidXbt(jtfXbt.getText().trim());
		config.setUidEsx(jtfEsx.getText().trim());
		config.setUserid(jtfUserid.getText().trim());
		
		config.setPwdWcq(new String(jpfWcq.getPassword()).trim());
		config.setPwdPty(new String(jpfPty.getPassword()).trim());
		config.setPwdXkq(new String(jpfXkq.getPassword()).trim());
		config.setPwdXbt(new String(jpfXbt.getPassword()).trim());
		config.setPwdEsx(new String(jpfEsx.getPassword()).trim());
		config.setToken(jtfToken.getText().trim());		
		return config;
	}
	public void mapToWindow(User config){
		if(config!=null){
			jtfWcq.setText(config.getUidWcq());
			jtfPty.setText(config.getUidPty());
			jtfXbt.setText(config.getUidXbt());
			jtfXkq.setText(config.getUidXkq());
			jtfEsx.setText(config.getUidEsx());
			jtfUserid.setText(config.getUserid());
			
			jpfWcq.setText(config.getPwdWcq());
			jpfPty.setText(config.getPwdPty());
			jpfXbt.setText(config.getPwdXbt());
			jpfXkq.setText(config.getPwdXkq());
			jpfEsx.setText(config.getPwdEsx());
			jtfToken.setText(config.getToken());
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "wcq":
			if(isReceivingWcq){
				jbtnWcq.setText("开始");
				
			}else{
				jbtnWcq.setText("停止");
			}
			isReceivingWcq=!isReceivingWcq;
			break;
		case "pty":
			if(isReceivingPty){
				jbtnPty.setText("开始");
				if(ptyServer!=null)
					ptyServer.stop();
			}else{
				jbtnPty.setText("停止");
				if(ptyServer==null)
					ptyServer=new XbtServer(jtfPty.getText(),new String(jpfPty.getPassword()), jtfMsgPty,jLabelPty);
				ptyServer.start();
			}
			isReceivingPty=!isReceivingPty;
			break;
		case "xkq":
			if(isReceivingXkq){
				jbtnXkq.setText("开始");
				if(xkServer!=null)
					xkServer.stop();
			}else{
				jbtnXkq.setText("停止");
				xkServer=new XkqServer(jtfXkq.getText(),new String(jpfXkq.getPassword()),jtfMsgXkq,jLabelXkq);
				xkServer.start();
			}
			isReceivingXkq=!isReceivingXkq;
			break;
		case "xbt":
			if(isReceivingXbt){
				jbtnXbt.setText("开始");
				if(xbtServer!=null)
					xbtServer.stop();
				
			}else{
				jbtnXbt.setText("停止");
				if(xbtServer==null)
				xbtServer=new XbtServer(jtfXbt.getText(),new String(jpfXbt.getPassword()), jtfMsgXbt,jLabelXbt);
				xbtServer.start();
			}
			isReceivingXbt=!isReceivingXbt;
			break;
		case "esx":
			if(isReceivingEsx){
				jbtnEsx.setText("开始");
				if(esxServer!=null)
					esxServer.stop();
			}else{
				jbtnEsx.setText("停止");
				esxServer=new EsxServer(jtfEsx.getText(),new String(jpfEsx.getPassword()),jtfMsgEsx,jLabelEsx);
				esxServer.start();
			}
			isReceivingEsx=!isReceivingEsx;			
			break;
		case "ycq":
			if(isReceivingYcq){
				jbtnYcq.setText("开始");
				ycqServer.stop();
			}else{
				jbtnYcq.setText("停止");
				if(ycqServer==null)
					ycqServer=new YcqServer(jtfUserid.getText().trim(), jtfToken.getText(), jtfMsgYcq,jLabelYcq);
				ycqServer.start();
			}
			isReceivingYcq=!isReceivingYcq;
			break;
		default:
			break;
		}
	}
}
