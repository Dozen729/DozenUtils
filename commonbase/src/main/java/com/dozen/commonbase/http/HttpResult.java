package com.dozen.commonbase.http;

/**
 * 返回结果 详情请参照接口说明文档
 */

public class HttpResult extends ResultInfo {

    private static final long serialVersionUID = 5073528584516253975L;

    //返回描述
    public String msg="";
    //返回描述
    public String message="";
    //代码: 1,成功;2,错误;3,授权失败;4,验证失败;5,版本过期;6,无功能权限 ,
    public String code="2";//"00000" code

    @Override
    public void setFailInfo(int httpCode, String msg) {
        this.httpCode = httpCode;
        this.msg = msg;
    }

    @Override
    public boolean isSucceed() {
        return httpCode == 200 && "1".equals(code);
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setMsg(String msg) {
        this.message = msg;
    }

}
