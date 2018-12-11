package com.hanc.mq.core.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "ons")
@Component
public class MqConfigProp {

    private String ak;

    private String sk;

    private String cid = "";

    private String pid = "";

    public void setAk(String ak) {
        this.ak = ak;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAk() {
        return ak;
    }

    public String getSk() {
        return sk;
    }

    public String getCid() {
        return cid;
    }

    public String getPid() {
        return pid;
    }
}
