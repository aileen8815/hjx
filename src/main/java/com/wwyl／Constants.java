package com.wwyl;

/**
 * @author fyunli
 */
public interface Constants {

    public static final String[] DATE_PATTERNS = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "yyyy-MM-dd HH:mm", "yyyy", "yyyyMM", "yyyyMMdd", "yyyy/MM", "yyyy/MM/dd",
            "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss"};
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 操作员SESSION存放KEY
    public static final String SESSION_OPERATOR_KEY = "currentOperator";

    // 操作员SESSION存放KEY
    public static final String SESSION_CUSTOMER_KEY = "currentCustomer";

    static final String BOOKING_METHOD_WEB = "WEB";
    
    static final Long ADMIN_OPERATOR_ID = 1L;
    static final String ROLE_SUPERB = "1";
    static final String ROLE_REGISTER = "2";//开单员
    static final String ROLE_KEEPER = "3";//仓管员
    static final String ROLE_DISPATCHER = "4";//调度员
    static final String ROLE_MANAGER = "5";//冷库主管
    static final String ROLE_FINANCE = "6";//财务
    static final String ROLE_LEADER = "7";//公司领导

    // TODO 被静态引用的收费项目

    //商品状态
    public static final Long PRODUCT_STATUS_QUALIFIED = 1L;//合格

    //费用项ID
    public static final Long CE_FEE_ITEM_GUARANTEEMONEY = 1L;//押金
    public static final Long CE_FEE_ITEM_Handling = 2L;//装卸费
    public static final Long CE_FEE_ITEM_StorageCharges = 3L;//仓储费    
    public static final Long CE_FEE_ITEM_Shipment = 4L;//转运费
    public static final Long CE_FEE_ITEM_Sorting = 5L;//分拣费
    public static final Long CE_FEE_ITEM_BREAKAGE = 6L;//报损费
    public static final Long CE_FEE_ITEM_Transport = 7L;//运输服务费
    public static final Long CE_FEE_ITEM_Disposal = 8L;//处置费1
    public static final Long CE_FEE_ITEM_Colding = 9L;//降温费1
    public static final Long CE_FEE_ITEM_Overdue = 10L;//滞纳金
    public static final Long CE_FEE_ITEM_Unloading = 11L;//倒货费
    public static final Long CE_FEE_ITEM_Shrinkwrap = 12L;//缠膜费
    public static final Long CE_FEE_ITEM_WriteCode = 13L;//抄码费
    public static final Long CE_FEE_ITEM_Ketonehandling = 14L;//酮体装卸费
    public static final Long CE_FEE_ITEM_SideUnloading = 15L;//库间倒货费1
    public static final Long CE_FEE_ITEM_InUnloading = 16L;//库内倒货费1
    
    public static final Long Common_Packing_1 = 1L;//普通
    public static final Long Common_Packing_2 = 2L;//酮体
    public static final Long Common_Packing_3 = 3L;//整托

    public static final int WARNINGTYPE_RETENTIONPERIOD = 2;////保管到期预警提前天数
    public static final int WARNINGTYPE_GUARANTEEDATE = 2;//保质到期预警提前天数

    //计费类型
    public static final Long CHARGETYPE_NOW = 1L;////现付
    public static final Long CHARGETYPE_MONTH = 2L;//月结
    
    //规则类型
    public static final Long CE_RULE_TYPE = 5L;//仓储费计费规则类型

    //货架类型
    public static final String STORE_LOCATION_TYPE_TURNOVER = "3";//周转区
    public static final Long STORE_LOCATION_TYPE_SHELF_ID = 1L;//货架
    public static final Long STORE_LOCATION_TYPE_STORAGE_CAGE_ID = 2L;//仓储笼
   
    public static final boolean  USE_WS_INTERFACE_FOR_PAYMENT = false;//是否启用和一体化平台接口，true连接口，false不连接口
    
    //一体化平台接口（大白菜+）
    public static final int moduleOID = 901;//901冷库系统
    public static final int payTypeOID = 0;//支付方式  payTypeOID  1 现金  0  IC卡 
    public static final int operatorOID = 90001;//冷库操作员  
    public static final Long feeItemOID = 9001L;//冷库费 

    public static final String FAKE_AUTH_TOKEN = "$1EdXquyh4wY";
    public static final String FAKE_SECRET_KEY = "!7W1YfyQtRCU";
    public static final String REPORT_SERVER_URL="http://10.142.103.2:8081";
    public static final String REPORT_SERVER_USER="admin";
    public static final String REPORT_SERVER_PASSWORD="password";
}
