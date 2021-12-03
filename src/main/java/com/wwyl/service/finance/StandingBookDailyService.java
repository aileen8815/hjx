package com.wwyl.service.finance;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.dao.finance.PaymentItemDao;
import com.wwyl.dao.finance.StandingBookDailyDao;
import com.wwyl.dao.store.BookInventoryHisDao;
import com.wwyl.dao.tenancy.StoreContractDao;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.finance.StandingBookDaily;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.BookInventoryHis;
import com.wwyl.entity.store.StockOut;
import com.wwyl.entity.tenancy.StoreContract;
import com.wwyl.entity.tenancy.StoreContractFeeItem;
import com.wwyl.service.ce.CECalculator;
import com.wwyl.service.ce.CERuleService;
import com.wwyl.service.ce.DECalculator;
import com.wwyl.service.ce.StoreCalculator;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.StockOutService;
import com.wwyl.service.tenancy.StoreContractService;

/**
 * @author hehao
 */
@Service
@Transactional(readOnly = true)
public class StandingBookDailyService {

    @Resource
    StandingBookDailyDao standingBookDailyDao;
    @Resource
    StoreCalculator storeCalculator;
    @Resource
    BookInventoryService bookInventoryService;
    @Resource
    StockOutService stockOutService;
    @Resource
    CustomerService customerService;
    @Resource
    StoreContractService storeContractService;
    @Resource
    private CECalculator ceCalculator;
    @Resource
    private CERuleService ceRuleService;
    @Resource
    private DECalculator deCalculator;
    @Resource
    private StoreContractDao storeContractDao;
    @Resource
    private PaymentService paymentService;
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;
    @Resource
    private PaymentItemDao paymentItemDao;
    @Resource
    private BookInventoryHisDao bookInventoryHisDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private StandingBookDaily findByDateTimeAndCustomerId(Date inboundDateTime, Long customerId) {
        return standingBookDailyDao.findByDateTimeAndCustomerId(inboundDateTime, customerId);
    }

    //按日期、客户生成每日台帐;
    @Transactional(readOnly = false)
    public void createStandingBookDaily() {
        List<Customer> customerList = customerService.findAll();
        //上一天
        Date inboundDateTime = new Date();
        inboundDateTime = DateUtils.addDays(inboundDateTime, -1);
        for (Customer customer : customerList) {
            //说明当前日期已经存在了台帐信息
            StandingBookDaily standingBookDailyTemp = this.findByDateTimeAndCustomerId(getDateShort(inboundDateTime), customer.getId());
            if (standingBookDailyTemp != null) {
                continue;
            }
            StandingBookDaily standingBookDaily = new StandingBookDaily();
            standingBookDaily.setInboundDateTime(getDateShort(inboundDateTime));
            logger.debug("开始进行每日台帐生成");
            standingBookDaily.setCustomer(customer);
            //初始化数据
            initStandingBookDaily(standingBookDaily);
            //修改增加了当天出库商品的费用
            setCalculateTodayKnot(standingBookDaily, inboundDateTime, customer.getId());
            setCalculateTodayContractFee(standingBookDaily, inboundDateTime, customer.getId());
            setCustomerTodayPayment(standingBookDaily, inboundDateTime, customer.getId());
            setCustomerTodayPaymentItem(standingBookDaily, inboundDateTime, customer.getId());
            setWareHouseDaily(standingBookDaily, inboundDateTime, customer.getId());
            setCalcReceivableBalance(standingBookDaily, inboundDateTime, customer.getId());
            logger.debug("开始进行每日台帐查询完毕，开始进行保存");
            standingBookDailyDao.save(standingBookDaily);
            logger.debug("开始进行每日台帐保存完毕");
        }
    }
    
    //按指定日期、指定客户生成每日台帐;
    @Transactional(readOnly = false)
    public void createAgainCustomerAndDate(Customer customer, List<Date> datetimes) {
        //删除该客户所有已生成的台帐信息
        for(Date date:datetimes){
        	StandingBookDaily standingBookDailyTemp = this.findByDateTimeAndCustomerId(getDateShort(date), customer.getId());
        	if (standingBookDailyTemp != null) {
        		standingBookDailyDao.delete(standingBookDailyTemp.getId());
        		
        		//开始生成新的台帐信息
                StandingBookDaily standingBookDaily = new StandingBookDaily();
                standingBookDaily.setInboundDateTime(getDateShort(date));
                logger.debug("开始进行每日台帐生成");
                standingBookDaily.setCustomer(customer);
                //初始化数据
                initStandingBookDaily(standingBookDaily);
                setCalculateTodayKnot(standingBookDaily, date, customer.getId());
                setCalculateTodayContractFee(standingBookDaily, date, customer.getId());
                setCustomerTodayPayment(standingBookDaily, date, customer.getId());
                setCustomerTodayPaymentItem(standingBookDaily, date, customer.getId());
                setWareHouseDaily(standingBookDaily, date, customer.getId());
                setCalcReceivableBalance(standingBookDaily, date, customer.getId());
                logger.debug("开始进行每日台帐查询完毕，开始进行保存");
                standingBookDailyDao.save(standingBookDaily);
                logger.debug("开始进行每日台帐保存完毕");
            }else
            {
        		//开始生成新的台帐信息
                StandingBookDaily standingBookDaily = new StandingBookDaily();
                standingBookDaily.setInboundDateTime(getDateShort(date));
                logger.debug("开始进行每日台帐生成");
                standingBookDaily.setCustomer(customer);
                //初始化数据
                initStandingBookDaily(standingBookDaily);
                setCalculateTodayKnot(standingBookDaily, date, customer.getId());
                setCalculateTodayContractFee(standingBookDaily, date, customer.getId());
                setCustomerTodayPayment(standingBookDaily, date, customer.getId());
                setCustomerTodayPaymentItem(standingBookDaily, date, customer.getId());
                setWareHouseDaily(standingBookDaily, date, customer.getId());
                setCalcReceivableBalance(standingBookDaily, date, customer.getId());
                logger.debug("开始进行每日台帐查询完毕，开始进行保存");
                standingBookDailyDao.save(standingBookDaily);
                logger.debug("开始进行每日台帐保存完毕");
            }
        }
        }
    
  //按指定日期、所有客户生成每日台帐;
    @Transactional(readOnly = false)
    public void createAllCustomerAndDate(List<Date> datetimes) {
        //删除该客户所有已生成的台帐信息
    	List<Customer> customers = customerService.findAll();
        for(Date date:datetimes){
        	for (Customer customer : customers) {
        		StandingBookDaily standingBookDailyTemp = this.findByDateTimeAndCustomerId(getDateShort(date), customer.getId());
            	if (standingBookDailyTemp != null) {
            		standingBookDailyDao.delete(standingBookDailyTemp.getId());
                }
            	
        		//开始生成新的台帐信息
                StandingBookDaily standingBookDaily = new StandingBookDaily();
                standingBookDaily.setInboundDateTime(getDateShort(date));
                logger.debug("开始进行每日台帐生成");
                standingBookDaily.setCustomer(customer);
                //初始化数据
                initStandingBookDaily(standingBookDaily);
                setCalculateTodayKnot(standingBookDaily, date, customer.getId());
                setCalculateTodayContractFee(standingBookDaily, date, customer.getId());
                setCustomerTodayPayment(standingBookDaily, date, customer.getId());
                setCustomerTodayPaymentItem(standingBookDaily, date, customer.getId());
                setWareHouseDaily(standingBookDaily, date, customer.getId());
                setCalcReceivableBalance(standingBookDaily, date, customer.getId());
                logger.debug("开始进行每日台帐查询完毕，开始进行保存");
                standingBookDailyDao.save(standingBookDaily);
                logger.debug("开始进行每日台帐保存完毕");
        	}
        }
    }


    //日结库存仓储费2
    private StandingBookDaily setCalculateTodayKnot(StandingBookDaily standingBookDaily, Date inboundDateTime, Long customerId) {
        int palletDayAmount = 0;  //总天数（包含合同范围内的）
        int palletDayAmount1 = 0;  //合同范围内总天数

        double weightAmount = 0.00;    //总重量（包含合同范围内的）
        double weightAmount1 = 0.00;    //合同范围内的总重量

        double weightDayAmount = 0.00;//总天数*托盘吨数
        double weightDayAmount1 = 0.00;//总天数*托盘吨数（合同内）
        //当日应收款
        BigDecimal receivableFee = new BigDecimal(0.00);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(inboundDateTime);
        calendar.add(calendar.DATE, 1);
        Date inboundDateTimeAddOneDay = calendar.getTime();
        
        //找出客户库存托盘记录
        //List<BookInventory> list = bookInventoryService.findBookInventoryByCustomerId(customerId);
        List<BookInventoryHis> list = bookInventoryHisDao.findBookInventoryHisByCustomerIdAndDateTime(customerId, getDateShort(inboundDateTimeAddOneDay));
        //找出客户出库信息；
        
        List<StockOut> listStockOut = stockOutService.findDailyStockOutByCustomerId(customerId, getDateShort(inboundDateTime), getDateShort(inboundDateTimeAddOneDay));
            
        Set<CalculatedResult> results = new HashSet<CalculatedResult>();
        if (CollectionUtils.isNotEmpty(list) || CollectionUtils.isNotEmpty(listStockOut)) {
            for (BookInventoryHis item : list) {
                palletDayAmount += 1;
                weightAmount += item.getWeight() / 1000;
                weightDayAmount += weightAmount;
            }
            
            for (StockOut itemStockOut : listStockOut) {
                palletDayAmount += 1;
                weightAmount += itemStockOut.getWeight() / 1000;
                weightDayAmount += weightAmount;
            }
            
        } else {
        		return standingBookDaily;
        }

        //找出客户有效合同的所有库存托盘记录，并对比库存记录，将不收费的托盘删除；
        List<StoreContract> storeContracts = storeContractService.findValidContractByDateTimeAndCustomerId(inboundDateTime, customerId);
        if (storeContracts != null && !storeContracts.isEmpty()) {
            for (BookInventoryHis item : list) {
                for (StoreContract storeContract : storeContracts) {
                    if (item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId()) {
                        weightAmount1 += item.getWeight() / 1000;
                        palletDayAmount1 += 1;
                        weightDayAmount1 += weightAmount1;
                    }
                }
            }
            
            for (StockOut itemStockOut : listStockOut) {
                for (StoreContract storeContract : storeContracts) {
                    if (itemStockOut.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId()) {
                        weightAmount1 += itemStockOut.getWeight() / 1000;
                        palletDayAmount1 += 1;
                        weightDayAmount1 += weightAmount1;
                    }
                }
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        weightAmount = (weightAmount - weightAmount1);
        Map<String, Object> props = new HashMap<String, Object>();
        Customer customer = customerService.findOne(customerId);
        props.put("PalletDayAmount", palletDayAmount - palletDayAmount1);
        props.put("WeightAmount", df.format(weightAmount));
        props.put("CustomerGrade", customer.getCustomerGrade().getId());
        props.put("WeightDayAmount", weightDayAmount - weightDayAmount1);
		//默认一个，虽然后面计算了，但是怕改动没有默认规则；
		props.put("CommonPacking", Constants.Common_Packing_1);        
        //按包装类型计算（仓储费）
		Map<String, Object> totalFeeItemAmount = CalculatePackingKnot(customerId, inboundDateTime);
        
        results = ceCalculator.calculate(props, ceRuleService.findOneType(Constants.CE_RULE_TYPE));
        //赋值库存中的仓储费（托盘，件，重量）
        for (CalculatedResult obj : results) {
            if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)) {
            	
            	obj.setAmount((BigDecimal)totalFeeItemAmount.get("totalStorageChargesAmount"));
				obj.setRuleComment(totalFeeItemAmount.get("totalStorageChargesRuleComment").toString());
            	
                if (obj.getRuleComment().contains("托盘*天数")) {
                    standingBookDaily.setContainerStorage(standingBookDaily.getContainerStorage().add(obj.getAmount()));
                    standingBookDaily.setContainerStorageRuleComment(obj.getRuleComment());
                    receivableFee = receivableFee.add(obj.getAmount());
                } else if (obj.getRuleComment().contains("重量（吨）*天数")) {
                    standingBookDaily.setWeightStorage(standingBookDaily.getWeightStorage().add(obj.getAmount()));
                    standingBookDaily.setWeightStorageRuleComment(obj.getRuleComment());
                    receivableFee = receivableFee.add(obj.getAmount());
                } else if (obj.getRuleComment().contains("件数")) {
                    standingBookDaily.setAmountStorage(standingBookDaily.getAmountStorage().add(obj.getAmount()));
                    standingBookDaily.setAmountStorageRuleComment(obj.getRuleComment());
                    receivableFee = receivableFee.add(obj.getAmount());
                }
            }
        }
        //当日应收款金额
        standingBookDaily.setReceivableFee(standingBookDaily.getReceivableFee().add(receivableFee));
        return standingBookDaily;
    }

    //日结租赁合同仓储费3
    private StandingBookDaily setCalculateTodayContractFee(StandingBookDaily standingBookDaily, Date inboundDateTime, Long customerId) {
        //查询客户租赁合同
        List<StoreContract> storeContracts = storeContractDao.findValidContractByDateTimeAndCustomerId(inboundDateTime, customerId);
        BigDecimal receivableFee = new BigDecimal(0.00);
        for (StoreContract contract : storeContracts) {
            //进入收费项目明细
            for (StoreContractFeeItem storeContractFeeItem : contract.getContractFeeItems()) {
                //明细为仓储费
                if (storeContractFeeItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)) {
                    standingBookDaily.setRentalAreaStorage(standingBookDaily.getRentalAreaStorage().add(new BigDecimal(storeContractFeeItem.getAmount(), MathContext.DECIMAL32)));
                    standingBookDaily.setRentalAreaStorageRuleComment(String.valueOf(storeContractFeeItem.getAmount()) + " /天");
                    receivableFee = receivableFee.add(BigDecimal.valueOf(storeContractFeeItem.getAmount()));
                }
            }
        }
        //当日应收款金额
        standingBookDaily.setReceivableFee(standingBookDaily.getReceivableFee().add(receivableFee));
        return standingBookDaily;
    }

    //payment单据收费金额4
    private StandingBookDaily setCustomerTodayPayment(StandingBookDaily standingBookDaily, Date inboundDateTime, Long customerId) {
        BigDecimal receivableFee = new BigDecimal(0.00);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(inboundDateTime);
        calendar.add(calendar.DATE, 1);
        Date inboundDateTimeAddOneDay = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //查询客户当天所有payment单据
        List<Payment> paymentList = paymentService.findPaymentByCustomerAndDateTime(customerId, sdf.format(inboundDateTime).toString(), sdf.format(inboundDateTimeAddOneDay).toString());
        for (Payment payment : paymentList) {
            for (PaymentItem paymentItem : payment.getPaymentItems()) {
                //仓储费
            	//按照系统来说，实际收款金额其实就是应收款金额（实际收款金额在没收款前叫应收金额，在收款后叫实收金额）
                if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)) {
                    //区分仓储费收费标准
                	/**每日已经计算了仓储费，不需要再加进去了；只算入实收金额即可。
                    if (paymentItem.getRuleComment().contains("托盘*天数")) {
                        standingBookDaily.setContainerStorage(standingBookDaily.getContainerStorage().add(paymentItem.getAmount()));
                        standingBookDaily.setContainerStorageRuleComment(paymentItem.getRuleComment());
                    } else if (paymentItem.getRuleComment().contains("重量（吨）*天数")) {
                        standingBookDaily.setWeightStorage(standingBookDaily.getWeightStorage().add(paymentItem.getAmount()));
                        standingBookDaily.setWeightStorageRuleComment(paymentItem.getRuleComment());
                    } else if (paymentItem.getRuleComment().contains("件数")) {
                        standingBookDaily.setAmountStorage(standingBookDaily.getAmountStorage().add(paymentItem.getAmount()));
                        standingBookDaily.setAmountStorageRuleComment(paymentItem.getRuleComment());
                    }**/
                	//应收金额不应该计算该费用；
                    //receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //转运费
                } else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Shipment)) {
                    standingBookDaily.setShipment(standingBookDaily.getShipment().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setShipmentRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //分拣费
                } else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Sorting)) {
                    standingBookDaily.setSorting(standingBookDaily.getSorting().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setSortingRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //装卸费
                } else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Handling)) {
                    standingBookDaily.setHandling(standingBookDaily.getHandling().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setHandlingRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //缠膜费
                } else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Shrinkwrap)) {
                    standingBookDaily.setShrinkwrap(standingBookDaily.getShrinkwrap().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setShrinkwrapRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //倒货费
                } else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Unloading)) {
                    standingBookDaily.setUnloading(standingBookDaily.getUnloading().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setUnloadingRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //酮体装卸费
                } else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Ketonehandling)) {
                    standingBookDaily.setKetonehandling(standingBookDaily.getKetonehandling().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setKetonehandlingRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //抄码费
                } else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_WriteCode)) {
                    standingBookDaily.setWriteCode(standingBookDaily.getWriteCode().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setWriteCodeRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //处置费
                }else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Disposal)) {
                    standingBookDaily.setDisposal(standingBookDaily.getDisposal().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setDisposalRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //降温费
                }else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Colding)) {
                    standingBookDaily.setColding(standingBookDaily.getColding().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setColdingRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //库间倒货费
                }else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_SideUnloading)) {
                    standingBookDaily.setSideUnloading(standingBookDaily.getSideUnloading().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setSideUnloadingRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                    //库内倒货费
                }else if (paymentItem.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_InUnloading)) {
                    standingBookDaily.setInUnloading(standingBookDaily.getInUnloading().add(paymentItem.getActuallyAmount()));
                    standingBookDaily.setInUnloadingRuleComment(paymentItem.getRuleComment());
                    receivableFee = receivableFee.add(paymentItem.getActuallyAmount());
                }
            }
        }
        //当日应收款金额
        standingBookDaily.setReceivableFee(standingBookDaily.getReceivableFee().add(receivableFee));
        return standingBookDaily;
    }

    //paymentItem单据已付款金额汇总5
    private StandingBookDaily setCustomerTodayPaymentItem(StandingBookDaily standingBookDaily, Date inboundDateTime, Long customerId) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(inboundDateTime);
        calendar.add(calendar.DATE, 1);
        Date inboundDateTimeAddOneDay = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //查询客户当天所有payment单据
        BigDecimal actualFee = new BigDecimal(0);
        List<PaymentItem> paymentItemList = paymentItemDao.findPaymentByCustomerAndDateTime(customerId, sdf.format(inboundDateTime).toString(), sdf.format(inboundDateTimeAddOneDay).toString());
        for (PaymentItem paymentItem : paymentItemList) {
            actualFee = actualFee.add(paymentItem.getActuallyAmount());
        }
        //实收款额
        standingBookDaily.setActualFee(actualFee);
        return standingBookDaily;
    }

    //统计出库、入库、库存的重量，托盘，数量6
    private StandingBookDaily setWareHouseDaily(StandingBookDaily standingBookDaily, Date inboundDateTime, Long customerId) {
        Map<String, String> params = null;
        params = new HashMap<String, String>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(inboundDateTime);
        calendar.add(calendar.DATE, 1);
        Date inboundDateTimeAddOneDay = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        params.put("inboundDateTime", sdf.format(inboundDateTime).toString());
        params.put("inboundDateTimeAddOneDay", sdf.format(inboundDateTimeAddOneDay).toString());
        params.put("customerId", customerId.toString());
        Map<String, Object> wareHouseDaily = sqlSessionTemplate.selectOne("findWareHouseDaily", params);
        standingBookDaily.setInboundWeightCount(Double.parseDouble((wareHouseDaily.get("INBOUNDWEIGHTCOUNT").toString())));
        standingBookDaily.setInboundAmountCount(Double.parseDouble((wareHouseDaily.get("INBOUNDAMOUNTCOUNT").toString())));
        standingBookDaily.setInboundContainerCount(Integer.parseInt((wareHouseDaily.get("INBOUNDCONTAINERCOUNT").toString())));

        standingBookDaily.setOutboundWeightCount(Double.parseDouble((wareHouseDaily.get("OUTBOUNDWEIGHTCOUNT").toString())));
        standingBookDaily.setOutboundAmountCount(Double.parseDouble((wareHouseDaily.get("OUTBOUNDAMOUNTCOUNT").toString())));
        standingBookDaily.setOutboundContainerCount(Integer.parseInt((wareHouseDaily.get("OUTBOUNDCONTAINERCOUNT").toString())));

        standingBookDaily.setStockAmountCount(Double.parseDouble((wareHouseDaily.get("STOCKAMOUNTCOUNT").toString())));
        standingBookDaily.setStockWeightCount(Double.parseDouble((wareHouseDaily.get("STOCKWEIGHTCOUNT").toString())));
        standingBookDaily.setStockContainerCount(Integer.parseInt((wareHouseDaily.get("STOCKCONTAINERCOUNT").toString())));

        return standingBookDaily;
    }

    //计算应收余额7
    private StandingBookDaily setCalcReceivableBalance(StandingBookDaily standingBookDaily, Date inboundDateTime, Long customerId) {
        //前一天是否有数据
        BigDecimal receivableBalance;
        Date dayBefore = DateUtils.addDays(inboundDateTime, -1);
        StandingBookDaily standingBookDailyLastDay = this.findByDateTimeAndCustomerId(getDateShort(dayBefore), customerId);
        if (standingBookDailyLastDay != null) {
            //应收余额+当日应收款金额-实收款额
            receivableBalance = (standingBookDailyLastDay.getReceivableBalance().add(standingBookDaily.getReceivableFee())).subtract(standingBookDaily.getActualFee());
            standingBookDaily.setReceivableBalance(receivableBalance);
        } else {
            //应收余额+当日应收款金额-实收款额
            receivableBalance = standingBookDaily.getReceivableFee().subtract(standingBookDaily.getActualFee());
            standingBookDaily.setReceivableBalance(receivableBalance);
        }

        return standingBookDaily;
    }

    /**
     * 获取现在时间
     *
     * @return返回短时间格式 yyyy-MM-dd
     */
    private static Date getDateShort(Date time) {
        try {
            Date currentTime = time;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(currentTime);
            Date currentTime_2 = formatter.parse(dateString);
            return currentTime_2;
        } catch (ParseException e) {
            return time;
        }
    }

    private StandingBookDaily initStandingBookDaily(StandingBookDaily standingBookDaily) {
        standingBookDaily.setInboundWeightCount(0);
        standingBookDaily.setInboundAmountCount(0);
        standingBookDaily.setInboundContainerCount(0);
        standingBookDaily.setOutboundWeightCount(0);
        standingBookDaily.setOutboundAmountCount(0);
        standingBookDaily.setOutboundContainerCount(0);
        standingBookDaily.setStockWeightCount(0);
        standingBookDaily.setStockAmountCount(0);
        standingBookDaily.setStockContainerCount(0);
        standingBookDaily.setContainerStorage(new BigDecimal(0));
        standingBookDaily.setRentalAreaStorage(new BigDecimal(0));
        standingBookDaily.setWeightStorage(new BigDecimal(0));
        standingBookDaily.setAmountStorage(new BigDecimal(0));
        standingBookDaily.setShipment(new BigDecimal(0));
        standingBookDaily.setSorting(new BigDecimal(0));
        standingBookDaily.setHandling(new BigDecimal(0));
        standingBookDaily.setUnloading(new BigDecimal(0));
        standingBookDaily.setShrinkwrap(new BigDecimal(0));
        standingBookDaily.setKetonehandling(new BigDecimal(0));
        standingBookDaily.setWriteCode(new BigDecimal(0));
        standingBookDaily.setDisposal(new BigDecimal(0));
        standingBookDaily.setColding(new BigDecimal(0));
        standingBookDaily.setSideUnloading(new BigDecimal(0));
        standingBookDaily.setInUnloading(new BigDecimal(0));
        standingBookDaily.setReceivableFee(new BigDecimal(0));
        standingBookDaily.setActualFee(new BigDecimal(0));
        standingBookDaily.setReceivableBalance(new BigDecimal(0));
        return standingBookDaily;
    }

    public List<StandingBookDaily> findAccountingByDateRangeAndCustomerId(
            Date startDate, Date endDate, Long customerId, Object rollingDate) {
        Map<String, Object> params = new HashedMap();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("rollingDate", rollingDate);
        params.put("customerId", customerId);
        return sqlSessionTemplate.selectList("findFinanceAccountingByDateRangeAndCustomerId", params);
    }

    public List<StandingBookDaily> findSummaryByDateRangeAndCustomerId(
            Date startDate, Date endDate, Long customerId, Date rollingDate) {
        Map<String, Object> params = new HashedMap();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if (0 != customerId) {
            params.put("customerId", customerId);
        }
        params.put("rollingDate", rollingDate);
        return sqlSessionTemplate.selectList("findFinanceSummaryByDateRangeAndCustomerId", params);
    }

    public StandingBookDaily findFinanceBillingByDateRangeAndCustomerId(Date startDate, Date endDate, Long customerId) {
        Map<String, Object> params = new HashedMap();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("customerId", customerId);
        return sqlSessionTemplate.selectOne("findFinanceBillingByDateRangeAndCustomerId", params);
    }

    //仓储费（包装类型）
	public Map<String, Object> CalculatePackingKnot(Long customerId, Date inboundDateTime) {	
		//区分包装类型来计费（装卸费、倒货费）
		double weightAmountCommon_Packing_1 = 0.00;
		double weightAmountCommon_Packing_2 = 0.00;
		double weightAmountCommon_Packing_3 = 0.00;
		int palletDayAmountCommon_Packing_1 = 0;  //总天数
		int palletDayAmountCommon_Packing_2 = 0;  //总天数
		int palletDayAmountCommon_Packing_3 = 0;  //总天数
		double weightDayAmountCommon_Packing_1 = 0.00;//总天数*托盘吨数
		double weightDayAmountCommon_Packing_2 = 0.00;//总天数*托盘吨数
		double weightDayAmountCommon_Packing_3 = 0.00;//总天数*托盘吨数
		//合同内的包装类型
		double weightAmountStoreContractCommon_Packing_1 = 0.00;
		double weightAmountStoreContractCommon_Packing_2 = 0.00;
		double weightAmountStoreContractCommon_Packing_3 = 0.00;
		int palletDayAmountStoreContractCommon_Packing_1 = 0;  //总天数
		int palletDayAmountStoreContractCommon_Packing_2 = 0;  //总天数
		int palletDayAmountStoreContractCommon_Packing_3 = 0;  //总天数
		double weightDayAmountStoreContractCommon_Packing_1 = 0.00;//总天数*托盘吨数
		double weightDayAmountStoreContractCommon_Packing_2 = 0.00;//总天数*托盘吨数
		double weightDayAmountStoreContractCommon_Packing_3 = 0.00;//总天数*托盘吨数
		
		//仓储费合计
		BigDecimal totalStorageChargesAmount = new BigDecimal(0);
		String totalStorageChargesRuleComment = "";
		Map<String, Object> totalFeeItemAmount = new HashMap<String, Object>();
		
		Calendar calendar = new GregorianCalendar();
        calendar.setTime(inboundDateTime);
        calendar.add(calendar.DATE, 1);
        Date inboundDateTimeAddOneDay = calendar.getTime();
        
		List<BookInventory> list11 = bookInventoryService.findBookInventoryByCustomerId(customerId);
		List<BookInventoryHis> list = bookInventoryHisDao.findBookInventoryHisByCustomerIdAndDateTime(customerId, getDateShort(inboundDateTimeAddOneDay));
        
		//找出客户出库信息；
        
        List<StockOut> listStockOut = stockOutService.findDailyStockOutByCustomerId(customerId, getDateShort(inboundDateTime), getDateShort(inboundDateTimeAddOneDay));
             
		if (CollectionUtils.isNotEmpty(list) || CollectionUtils.isNotEmpty(listStockOut)) {
			for(BookInventoryHis item : list) {
				if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1)){
					palletDayAmountCommon_Packing_1 += 1;
					weightAmountCommon_Packing_1 += item.getWeight()/1000;	
					weightDayAmountCommon_Packing_1 = weightAmountCommon_Packing_1;//每日仓储费
				}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2)){
					palletDayAmountCommon_Packing_2 += 1;
					weightAmountCommon_Packing_2 += item.getWeight()/1000;	
					weightDayAmountCommon_Packing_2 = weightAmountCommon_Packing_2;//每日仓储费
				}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3)){
					palletDayAmountCommon_Packing_3 += 1;
					weightAmountCommon_Packing_3 += item.getWeight()/1000;	
					weightDayAmountCommon_Packing_3 = weightAmountCommon_Packing_3;//每日仓储费
				}
			}
			
			for(StockOut itemStockOut : listStockOut) {
				if(itemStockOut.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1)){
					palletDayAmountCommon_Packing_1 += 1;
					weightAmountCommon_Packing_1 += itemStockOut.getWeight()/1000;	
					weightDayAmountCommon_Packing_1 = weightAmountCommon_Packing_1;//每日仓储费
				}else if(itemStockOut.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2)){
					palletDayAmountCommon_Packing_2 += 1;
					weightAmountCommon_Packing_2 += itemStockOut.getWeight()/1000;	
					weightDayAmountCommon_Packing_2 = weightAmountCommon_Packing_2;//每日仓储费
				}else if(itemStockOut.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3)){
					palletDayAmountCommon_Packing_3 += 1;
					weightAmountCommon_Packing_3 += itemStockOut.getWeight()/1000;	
					weightDayAmountCommon_Packing_3 = weightAmountCommon_Packing_3;//每日仓储费
				}
			}
			
		}else{
			totalFeeItemAmount.put("totalStorageChargesAmount", 0);
			totalFeeItemAmount.put("totalStorageChargesRuleComment", "");
			return totalFeeItemAmount;
		}
		Map<String, Object> props1 = new HashMap<String, Object>();
		Map<String, Object> props2 = new HashMap<String, Object>();
		Map<String, Object> props3 = new HashMap<String, Object>();
		List<StoreContract> storeContracts = storeContractService.findContractByCustomerID(customerId);
		if(storeContracts != null && !storeContracts.isEmpty())
		{
			for(BookInventoryHis item : list) {
				for(StoreContract storeContract: storeContracts )
				{
					if(item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1)){
							weightAmountStoreContractCommon_Packing_1 += item.getWeight()/1000;
							palletDayAmountStoreContractCommon_Packing_1 += 1;
							weightDayAmountStoreContractCommon_Packing_1 = weightAmountStoreContractCommon_Packing_1;
						}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2)){
							weightAmountStoreContractCommon_Packing_2 += item.getWeight()/1000;
							palletDayAmountStoreContractCommon_Packing_2 += 1;
							weightDayAmountStoreContractCommon_Packing_2 = weightAmountStoreContractCommon_Packing_2;
						}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3)){
							weightAmountStoreContractCommon_Packing_3 += item.getWeight()/1000;
							palletDayAmountStoreContractCommon_Packing_3 += 1;
							weightDayAmountStoreContractCommon_Packing_3 = weightAmountStoreContractCommon_Packing_3;
						}
						
					}
				}
			}
			
			for(StockOut itemStockOut : listStockOut) {
				for(StoreContract storeContract: storeContracts )
				{
					if(itemStockOut.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						if(itemStockOut.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1)){
							weightAmountStoreContractCommon_Packing_1 += itemStockOut.getWeight()/1000;
							palletDayAmountStoreContractCommon_Packing_1 += 1;
							weightDayAmountStoreContractCommon_Packing_1 = weightAmountStoreContractCommon_Packing_1;
						}else if(itemStockOut.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2)){
							weightAmountStoreContractCommon_Packing_2 += itemStockOut.getWeight()/1000;
							palletDayAmountStoreContractCommon_Packing_2 += 1;
							weightDayAmountStoreContractCommon_Packing_2 = weightAmountStoreContractCommon_Packing_2;
						}else if(itemStockOut.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3)){
							weightAmountStoreContractCommon_Packing_3 += itemStockOut.getWeight()/1000;
							palletDayAmountStoreContractCommon_Packing_3 += 1;
							weightDayAmountStoreContractCommon_Packing_3 = weightAmountStoreContractCommon_Packing_3;
						}
						
					}
				}
			}
		}		
		DecimalFormat df=new DecimalFormat("0.00");
		Customer customer = customerService.findOne(customerId);
		if (palletDayAmountCommon_Packing_1 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletAmount", palletDayAmountCommon_Packing_1 - palletDayAmountStoreContractCommon_Packing_1);
			props.put("PalletDayAmount", palletDayAmountCommon_Packing_1 - palletDayAmountStoreContractCommon_Packing_1);
			props.put("WeightDayAmount", weightDayAmountCommon_Packing_1 - weightDayAmountStoreContractCommon_Packing_1);
			props.put("WeightAmount", df.format(weightAmountCommon_Packing_1 - weightAmountStoreContractCommon_Packing_1));
			props.put("CustomerGrade", customer.getCustomerGrade().getId());
			props.put("CommonPacking", Constants.Common_Packing_1);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
					totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
					totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
				}
			}
		}
		
		if (palletDayAmountCommon_Packing_2 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletAmount", palletDayAmountCommon_Packing_2 - palletDayAmountStoreContractCommon_Packing_2);
			props.put("PalletDayAmount", palletDayAmountCommon_Packing_2 - palletDayAmountStoreContractCommon_Packing_2);
			props.put("WeightDayAmount", weightDayAmountCommon_Packing_2 - weightDayAmountStoreContractCommon_Packing_2);
			props.put("WeightAmount", df.format(weightAmountCommon_Packing_2 - weightAmountStoreContractCommon_Packing_2));
			props.put("CustomerGrade", customer.getCustomerGrade().getId());		
			props.put("CommonPacking", Constants.Common_Packing_2);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
					totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
					totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
				}
			}
		}
		
		if (palletDayAmountCommon_Packing_3 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletAmount", palletDayAmountCommon_Packing_3 - palletDayAmountStoreContractCommon_Packing_3);
			props.put("PalletDayAmount", palletDayAmountCommon_Packing_3 - palletDayAmountStoreContractCommon_Packing_3);
			props.put("WeightDayAmount", weightDayAmountCommon_Packing_3 - weightDayAmountStoreContractCommon_Packing_3);
			props.put("WeightAmount", df.format(weightAmountCommon_Packing_3 - weightAmountStoreContractCommon_Packing_3));
			props.put("CustomerGrade", customer.getCustomerGrade().getId());		
			props.put("CommonPacking", Constants.Common_Packing_3);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
					totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
					totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
				}
			}
		}
		
		totalFeeItemAmount.put("totalStorageChargesAmount", totalStorageChargesAmount);
		totalFeeItemAmount.put("totalStorageChargesRuleComment", totalStorageChargesRuleComment);
		return totalFeeItemAmount;
	}
}
