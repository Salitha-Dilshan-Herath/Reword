package com.iit.reword.model;

public class LanguageDisplay {

    private String name;

    private boolean isSubscribe;

    private String lanCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    public String getLanCode() {
        return lanCode;
    }

    public void setLanCode(String lanCode) {
        this.lanCode = lanCode;
    }

    @Override
    public String toString() {
        return "LanguageDisplay{" +
                "name='" + name + '\'' +
                ", isSubscribe=" + isSubscribe +
                '}';
    }
}
