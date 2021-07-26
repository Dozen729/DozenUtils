package com.dozen.commonbase.bean;

public class EventBusBean {
    private String message;

    private String content;

    public EventBusBean(String message) {
        this.message = message;
    }

    public EventBusBean(String message, String content ) {
        this.content = content;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String paramString) {
        this.message = paramString;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
