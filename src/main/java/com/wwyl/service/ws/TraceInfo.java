package com.wwyl.service.ws;

/**
 * Created by fyunli on 14-9-28.
 */
public class TraceInfo {

    String cdnr; // 磁道号
    String khmc; // 客户名称
    String hyid; // 会员ID
    String gsmc; // 公司名称
    String zkmc; // 主卡名称

    public String getCdnr() {
        return cdnr;
    }

    public void setCdnr(String cdnr) {
        this.cdnr = cdnr;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getHyid() {
        return hyid;
    }

    public void setHyid(String hyid) {
        this.hyid = hyid;
    }

    public String getGsmc() {
        return gsmc;
    }

    public void setGsmc(String gsmc) {
        this.gsmc = gsmc;
    }

    public String getZkmc() {
        return zkmc;
    }

    public void setZkmc(String zkmc) {
        this.zkmc = zkmc;
    }
}
