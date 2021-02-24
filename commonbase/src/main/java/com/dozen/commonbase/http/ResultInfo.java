package com.dozen.commonbase.http;

import java.io.Serializable;

public abstract class ResultInfo implements Serializable {
	private static final long serialVersionUID = -4434030422871210097L;
	
	public int httpCode;
	//设置请求失败内容
	public abstract void setFailInfo(int httpCode, String msg);
	//是否请求成功
	public abstract boolean isSucceed();
	//请求反馈描述
	public abstract String getMsg();
	//获取请求 code
	public abstract String getCode();
	//重置反馈描述
	public abstract void setMsg(String msg);
}
