package com.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class KeyWord {
	private String keyword;
	private String keywordid;
	
	public KeyWord() {
		super();
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeywordid() {
		return keywordid;
	}
	public void setKeywordid(String keywordid) {
		this.keywordid = keywordid;
	}
}
