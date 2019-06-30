package com.bean;

import java.util.List;

public class Data {
	private int status;
	private String msg;
	private List<Task> data;
	public Data() {
		super();
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<Task> getData() {
		return data;
	}
	public void setData(List<Task> data) {
		this.data = data;
	}
	
	
}
