package com.wwyl.service.store;

import java.util.LinkedList;

/**
 * Created by fyunli on 14-10-15.
 */
public class AreaStat {

    LinkedList<Object> columnList = new LinkedList<Object>();
    LinkedList<Object> turnoverList = new LinkedList<Object>(); // 周转区

    String areaCode;
    String areaName;
    int layoutCorriderLine;

    int maxRowCount; // 列数

    // 可使用  使用中  预留  维修  未绑定
    int usable;// 可使用
    int used; // 使用中
    int engage; // 预留
    int maintain; // 维修
    int unbind; // 未绑定

    public LinkedList<Object> getColumnList() {
        return columnList;
    }

    public void setColumnList(LinkedList<Object> columnList) {
        this.columnList = columnList;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getUsable() {
        return usable;
    }

    public void setUsable(int usable) {
        this.usable = usable;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getEngage() {
        return engage;
    }

    public void setEngage(int engage) {
        this.engage = engage;
    }

    public int getMaintain() {
        return maintain;
    }

    public void setMaintain(int maintain) {
        this.maintain = maintain;
    }

    public int getUnbind() {
        return unbind;
    }

    public void setUnbind(int unbind) {
        this.unbind = unbind;
    }

    public int getMaxRowCount() {
        return maxRowCount;
    }

    public void setMaxRowCount(int maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    public LinkedList<Object> getTurnoverList() {
        return turnoverList;
    }

    public void setTurnoverList(LinkedList<Object> turnoverList) {
        this.turnoverList = turnoverList;
    }

    public int getLayoutCorriderLine() {
        return layoutCorriderLine;
    }

    public void setLayoutCorriderLine(int layoutCorriderLine) {
        this.layoutCorriderLine = layoutCorriderLine;
    }
}
