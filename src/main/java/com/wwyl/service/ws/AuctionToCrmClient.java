package com.wwyl.service.ws;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.xfire.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wwyl.Constants;
import com.wwyl.util.MD5Encryption;

import emsp.crm.AuctionToCrmNewWebSerice;

/**
 * 海吉星一体化平台WebService接口
 * <p/>
 * Created by fyunli on 14-9-27.
 */
@Component
public class AuctionToCrmClient {

    static final Logger LOG = LoggerFactory.getLogger(AuctionToCrmClient.class);

    @Value("${ws.emsp.url}")
    String serviceUrl;
    @Value("${ws.emsp.business_type}")
    int businessType = 1;
    //不需要校验
    //@Value("${ws.emsp.certuser}")
    //String certUser;
    //@Value("${ws.emsp.certpass}")
    //String certPass;

    AuctionToCrmNewWebSerice service;    

    private void fromAnalysisCustomerInfo(Object[] responseArray, CustomerInfo customerInfo) throws Exception {
        try {
        	String info = responseArray[0].toString();
        	String[] sarray = info.split(",");
        	customerInfo.setCustomerOID(sarray[0]);
        	customerInfo.setsKHBH(sarray[1]);
        	customerInfo.setsKHMC(sarray[2]);
        	customerInfo.setmBalance(sarray[3]);
        	customerInfo.setaCardMac(sarray[4]);
        	customerInfo.setAccountOID(sarray[5]);
        	
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new Exception("WS信息转换错误", e);
        }
    }

    /**
     * 获取会员信息
     *
     * @param aCardMac 物理卡号
     * @param sMemberCode 会员卡号
     * @return 会员信息
     */
    public CustomerInfo getCustomer(String aCardMac, String sMemberCode) {
        LOG.info("getCustomer() - aCardMac : {} - sMemberCode : {} ", aCardMac,sMemberCode);
        Object[] cardMac = new Object[1];
        Object[] memberCode = new Object[1];
        Object[] responseArray = new Object[6];
        cardMac[0] = aCardMac;
        memberCode[0] = sMemberCode;
        try {
        	Client client = new Client(new URL(serviceUrl));
        	responseArray = client.invoke("getCustomerInfo", cardMac);//物理卡号获取会员内容
            LOG.info("返回物理卡号查询结果:\r\n{}\r\n", responseArray);
        } catch (Throwable e) {
            try {
            	LOG.error(e.getMessage(), e);
            	Client client = new Client(new URL(serviceUrl));
            	responseArray = client.invoke("getCustomerInfo", memberCode);//会员账号获取会员内容
            	LOG.info("返回会员账号查询结果:\r\n{}\r\n", responseArray);
            } catch(Throwable f) {
            	throw new RuntimeException("获取客户信息失败");
            }
        }
        CustomerInfo customerInfo = new CustomerInfo();
        try {
        this.fromAnalysisCustomerInfo(responseArray, customerInfo);
        }catch (Throwable e) {
        	throw new RuntimeException("一体化接口信息转换错误", e);
        }
        if (customerInfo == null || StringUtils.isBlank(customerInfo.getaCardMac())) {
            throw new RuntimeException("获取客户信息失败");
        }
        return customerInfo;
    }

    /**
     * 缴费
     *
     * @param moduleOID      业务模块ID
     * @param password       密码(传入原密码后md5加密的密码)
     * @param settlementOID  交易单号关键字ID 
     * @param customerOID    客户编号
     * @param aCardMac       卡物理号
     * @param aCardMac       支付类型ID
     * @param feeitemoids    收费项目
     * @param amounts        金额
     * @param operatorOID    操作员OID
     * @param remark         备注
     * @return
     * password为空则不进行密码校验，直接扣费；
     */
    public String payCashCard(String password, int settlementOID, int customerOID, String aCardMac, String sMemberCode, Double amounts, String remark) {
		String result = "";
		Object[] payStatus = new Object[1];
		Long[] feeitemoids = new Long[1];
		feeitemoids[0] = Constants.feeItemOID;
		try{
			 //是否有密码
			if (password == null || password.isEmpty()){
				try{
					Client client = new Client(new URL(serviceUrl));
					Object[] updatePayNoPWD = new Object[]{Constants.moduleOID, settlementOID, customerOID, aCardMac, Constants.payTypeOID, feeitemoids[0], amounts, Constants.operatorOID, "冷库系统扣费"};
					LOG.info("updatePayNoPWD - updatePayNoPWD: {}, settlementOID: {}, customerOID: {}, aCardMac: {}, payTypeOID: {}, feeitemoids: {}, amounts: {}, operatorOID: {}, remark:{}.", updatePayNoPWD);       
					payStatus = client.invoke("updatePayNoPWD", updatePayNoPWD);
					result = payStatus[0].toString();
				} catch(Throwable e) {
					try{
					Client client = new Client(new URL(serviceUrl));
					Object[] updatePayNoPWD = new Object[]{Constants.moduleOID, settlementOID, customerOID, sMemberCode, Constants.payTypeOID, feeitemoids[0], amounts, Constants.operatorOID, "冷库系统扣费"};
					LOG.info("updatePayNoPWD - updatePayNoPWD: {}, settlementOID: {}, customerOID: {}, sMemberCode: {}, payTypeOID: {}, feeitemoids: {}, amounts: {}, operatorOID: {}, remark:{}.", updatePayNoPWD);       
					payStatus = client.invoke("updatePayNoPWD", updatePayNoPWD);
					result = payStatus[0].toString();
					}catch(Throwable f) {
					
	            	throw new RuntimeException("缴费失败，与一体化平台通信错误",f);
					}
	            }
					
			}else
			{	
				try{
				Client client = new Client(new URL(serviceUrl));
				String desPassword = MD5Encryption.encode(password);
				Object[] updatePay = new Object[]{Constants.moduleOID,desPassword, settlementOID, customerOID, aCardMac, Constants.payTypeOID, feeitemoids[0], amounts, Constants.operatorOID, "冷库系统扣费"};
				LOG.info("updatePay() - updatePay: {}, des: {}, settlementOID: {}, customerOID: {}, aCardMac: {}, payTypeOID: {}, feeitemoids: {}, amounts: {}, operatorOID:{}.", updatePay);
				payStatus = client.invoke("updatePay", updatePay);
				result = payStatus[0].toString();
			} catch(Throwable e) {
				try{
					Client client = new Client(new URL(serviceUrl));
					String desPassword = MD5Encryption.encode(password);
					Object[] updatePay = new Object[]{Constants.moduleOID,desPassword, settlementOID, customerOID, sMemberCode, Constants.payTypeOID, feeitemoids[0], amounts, Constants.operatorOID, "冷库系统扣费"};
					LOG.info("updatePay() - updatePay: {}, des: {}, settlementOID: {}, customerOID: {}, sMemberCode: {}, payTypeOID: {}, feeitemoids: {}, amounts: {}, operatorOID:{}.", updatePay);
					payStatus = client.invoke("updatePay", updatePay);
					result = payStatus[0].toString();
				} catch(Throwable f) {
	            	throw new RuntimeException("缴费失败，与一体化平台通信错误",f);
	            }
			}
			}
			if(result.equals("1")) {
				return result;
			}
			else if(result.equals("-101")){
				throw new RuntimeException("缴费失败，与一体化平台通信错误-101");
			}
					
		} catch(Exception e) {
			if(result.equals("-101")){
				throw new RuntimeException("-101缴费失败:可能原因：1.余额不足；2.密码错误；请检查",e);
			}else
			{
				throw new RuntimeException("缴费失败，与一体化平台通信错误",e);
			}
        }
		return result;
    }
}
