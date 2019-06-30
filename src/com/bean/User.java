package com.bean;

public class User {
	private boolean isActive;
	private String uidWcq;
	private String pwdWcq;
	private String uidPty;
	private String pwdPty;
	private String uidXkq;
	private String pwdXkq;
	private String uidXbt;
	private String pwdXbt;
	private String uidEsx;
	private String pwdEsx;
	private String userid;
	private String token;		
	private String musicPath;
	public User() {
		super();
	}
	public User(boolean isActive, String uidWcq, String pwdWcq, String uidPty, String pwdPty, String uidXkq,
			String pwdXkq, String uidXbt, String pwdXbt, String uidEsx, String pwdEsx, String userid, String token,
			String musicPath) {
		super();
		this.isActive = isActive;
		this.uidWcq = uidWcq;
		this.pwdWcq = pwdWcq;
		this.uidPty = uidPty;
		this.pwdPty = pwdPty;
		this.uidXkq = uidXkq;
		this.pwdXkq = pwdXkq;
		this.uidXbt = uidXbt;
		this.pwdXbt = pwdXbt;
		this.uidEsx = uidEsx;
		this.pwdEsx = pwdEsx;
		this.userid = userid;
		this.token = token;
		this.musicPath = musicPath;
	}

	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getUidWcq() {
		return uidWcq;
	}
	public void setUidWcq(String uidWcq) {
		this.uidWcq = uidWcq;
	}
	public String getPwdWcq() {
		return pwdWcq;
	}
	public void setPwdWcq(String pwdWcq) {
		this.pwdWcq = pwdWcq;
	}
	public String getUidPty() {
		return uidPty;
	}
	public void setUidPty(String uidPty) {
		this.uidPty = uidPty;
	}
	public String getPwdPty() {
		return pwdPty;
	}
	public void setPwdPty(String pwdPty) {
		this.pwdPty = pwdPty;
	}
	public String getUidXkq() {
		return uidXkq;
	}
	public void setUidXkq(String uidXkq) {
		this.uidXkq = uidXkq;
	}
	public String getPwdXkq() {
		return pwdXkq;
	}
	public void setPwdXkq(String pwdXkq) {
		this.pwdXkq = pwdXkq;
	}
	public String getUidXbt() {
		return uidXbt;
	}
	public void setUidXbt(String uidXbt) {
		this.uidXbt = uidXbt;
	}
	public String getPwdXbt() {
		return pwdXbt;
	}
	public void setPwdXbt(String pwdXbt) {
		this.pwdXbt = pwdXbt;
	}
	public String getUidEsx() {
		return uidEsx;
	}
	public void setUidEsx(String uidEsx) {
		this.uidEsx = uidEsx;
	}
	public String getPwdEsx() {
		return pwdEsx;
	}
	public void setPwdEsx(String pwdEsx) {
		this.pwdEsx = pwdEsx;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMusicPath() {
		return musicPath;
	}
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
	
	
}
