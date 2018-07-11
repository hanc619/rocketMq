package com.hanc.mq.core.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class MqConfigProp {

    @Value("${ons.ak}")
    private String onsAk;

    @Value("${ons.sk}")
    private String onsCk;

    @Value("${ons.cid}")
    private String cid;

    @Value("${ons.pid}")
    private String pid;

    public void setOnsAk(String onsAk) {
        this.onsAk = onsAk;
    }

    public void setOnsCk(String onsCk) {
        this.onsCk = onsCk;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOnsAk() {
        return onsAk;
    }

    public String getOnsCk() {
        return onsCk;
    }

    public String getCid() {
        return cid;
    }

    public String getPid() {
        return pid;
    }
}
