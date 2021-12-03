package com.wwyl.service.ws;

/**
 * Created by hehao on 14-9-28.
 */
public class CustomerInfo {

    String customerOID; // 客户关键字
    String sKHBH; // 客户编号
    String sKHMC; // 客户名称
    String mBalance; // 卡余额
    String aCardMac; // 物理卡号
    String accountOID;//账号OID
    
	public String getCustomerOID() {
		return customerOID;
	}
	public void setCustomerOID(String customerOID) {
		this.customerOID = customerOID;
	}
	public String getsKHBH() {
		return sKHBH;
	}
	public void setsKHBH(String sKHBH) {
		this.sKHBH = sKHBH;
	}
	public String getsKHMC() {
		return sKHMC;
	}
	public void setsKHMC(String sKHMC) {
		this.sKHMC = sKHMC;
	}
	public String getmBalance() {
		return mBalance;
	}
	public void setmBalance(String mBalance) {
		this.mBalance = mBalance;
	}
	public String getaCardMac() {
		return aCardMac;
	}
	public void setaCardMac(String aCardMac) {
		this.aCardMac = aCardMac;
	}
	public String getAccountOID() {
		return accountOID;
	}
	public void setAccountOID(String accountOID) {
		this.accountOID = accountOID;
	}
    
}
