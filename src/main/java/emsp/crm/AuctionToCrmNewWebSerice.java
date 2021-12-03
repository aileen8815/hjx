package emsp.crm;
/**
 * 
 * @author hehao
 *大白菜+接口
 */
public interface AuctionToCrmNewWebSerice {

    String getCustomerInfo(Object[] cardMac);

    int updatePayNoPWD(int moduleOID, int settlementOID, int customerOID, String cardMac, 
    		int payTypeOID, Long[] feeitemoids, Double[] amounts, int operatorOID, String remark);

    int updatePay(int moduleOID, String password, int settlementOID, int customerOID, String aCardMac, 
    		int payTypeOID, Long[] feeitemoids, Double[] amounts, int operatorOID, String remark);
    
    //不使用，未实现
    int updateFrozenOrThaw(int customerOID, String accountoid, Double amount, int operatorOID, int itype);
    
   /*
    * 1、模块号moduleOID（901 冷库系统 902 拍卖系统 903 水电系统 904 App交易系统 905 其他系统1  906 其他系统2  907 其他系统3 ）
2、支付方式  payTypeOID  1 现金  0  IC卡
3、收费细项金额amounts  方式为IC卡的：正数->加  负数 ->扣款
4、收费项feeitemoids(
1	租金
81	卖方货款
65	卖方服务费
64	买方货款
6647	买方服务费
6731    广告费
1045    管理费
26      水费
27      排污费
28      垃圾处理费
29      电费 等)
5、操作员  operatorOID (90001 冷库系统 90002 拍卖系统 90003 水电系统 90004 App交易系统 90005 其他系统1  90006 其他系统2  90007 其他系统3)

    */
}

