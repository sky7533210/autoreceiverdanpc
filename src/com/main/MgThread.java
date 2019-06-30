package com.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public class MgThread extends Thread {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		new MgThread().start();

	}

	@Override
	public void run() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("userType", "02");
		parameter.put("userName", "18679447632");
		while (true) {
			Date date = new Date();
			parameter.put("reqTime", simpleDateFormat.format(date));

			Connection con = Jsoup
					.connect("http://60.205.186.218:8888/api/shop/myBuy?userType=02&userName=18679447632&reqTime="
							+ simpleDateFormat.format(date));
			con.method(Method.OPTIONS);
			try {
				con.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}

			con = Jsoup.connect("http://60.205.186.218:8888/api/shop/myBuy?userType=02&userName=18679447632&reqTime="
					+ simpleDateFormat.format(date));
			con.method(Method.POST);
			con.ignoreContentType(true);
			con.header("Content-Type", "application/json;charset=UTF-8");
			con.data(parameter);
			try {
				Response rs = con.execute();
				System.out.println(rs.body());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(40*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
