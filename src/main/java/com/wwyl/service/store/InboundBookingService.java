package com.wwyl.service.store;

import java.util.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.wwyl.Constants;
import com.wwyl.dao.settings.BookingMethodDao;
import com.wwyl.entity.settings.BookingMethod;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.TaskService;

import org.apache.commons.lang3.ArrayUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.BookingStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.CommentDao;
import com.wwyl.dao.store.InboundBookingDao;
import com.wwyl.dao.store.InboundBookingItemDao;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.store.InboundBooking;
import com.wwyl.entity.store.InboundBookingItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.InboundRegisterItem;

/**
 * @author yche
 */
@Service
@Transactional(readOnly = true)
public class InboundBookingService {
    @Resource
    private InboundBookingDao inboundBookingDao;
    @Resource
    private InboundBookingItemDao inboundBookingItemDao;
    @Resource
    private CommentDao commentDao;
    @Resource
    private BookingMethodDao bookingMethodDao;
    @Resource
    private TaskService taskService;
    @Resource
    private SerialNumberService serialNumberService;
    @Resource
    private InboundRegisterService inboundRegisterService;

    public List<InboundBooking> findAll() {
        return inboundBookingDao.findAll();
    }

    public InboundBooking findOne(Long id) {
        return inboundBookingDao.findOne(id);
    }


    public InboundBooking findBySerialNo(String serialNo) {
        return inboundBookingDao.findBySerialNo(serialNo);
    }

    @Transactional(readOnly = false)
    public Long save(InboundBooking inboundBooking, String[] productChecks, Long inboundRegisterId) {
        // 新的网路预约发送通知
        if (inboundBooking.isNew()) {
            BookingMethod bookingMethod = bookingMethodDao.findOne(inboundBooking.getBookingMethod().getId());
            if (bookingMethod.getCode().equals(Constants.BOOKING_METHOD_WEB)) {
                taskService.assignInboundBookingNotice(inboundBooking);
            }
        }

        //车型为空时候
        if (inboundBooking.getVehicleType() != null && inboundBooking.getVehicleType().getId() == null) {
            inboundBooking.setVehicleType(null);
        }
        inboundBooking.setSerialNo(serialNumberService.getSerialNumber(TransactionType.InboundBooking));
        inboundBooking.setBookTime(new Date());
        if (BookingStatus.未审核 != inboundBooking.getBookingStatus()) {
            inboundBooking.setBookingStatus(BookingStatus.已受理);
        }

        inboundBookingDao.save(inboundBooking);

        if (productChecks != null && productChecks.length > 0) {
            InboundRegister inboundRegister = inboundRegisterService.findOne(inboundRegisterId);
            Set<InboundBookingItem> inboundBookingItems = inboundBookingDao.findOne(inboundRegister.getInboundBooking().getId()).getInboundBookingItems();
            for (String productCheck : productChecks) {
                String[] productAndamount = productCheck.split(":");//商品id：数量：预估托盘数
                for (InboundBookingItem inboundBookingItem : inboundBookingItems) {
                    if (Long.valueOf(productAndamount[0]).equals(inboundBookingItem.getProduct().getId())) {
                        InboundBookingItem inboundBookingItemNew = new InboundBookingItem();
                        double weitht = inboundRegisterService.weightBudget(inboundBookingItem.getAmount(), inboundBookingItem.getWeight(), productAndamount[1]);
                        inboundBookingItemNew.setAmount(Double.valueOf(productAndamount[1]));//数量
                        inboundBookingItemNew.setStoreContainerCount(Integer.valueOf(productAndamount[2]));//预估托盘数
                        inboundBookingItemNew.setAmountMeasureUnit(inboundBookingItem.getAmountMeasureUnit());
                        inboundBookingItemNew.setWeight(weitht);
                        inboundBookingItemNew.setWeightMeasureUnit(inboundBookingItem.getWeightMeasureUnit());
                        inboundBookingItemNew.setPacking(inboundBookingItem.getPacking());
                        inboundBookingItemNew.setProduct(inboundBookingItem.getProduct());
                        inboundBookingItemNew.setProductionDate(inboundBookingItem.getProductionDate());
                        inboundBookingItemNew.setQualityGuaranteePeriod(inboundBookingItem.getQualityGuaranteePeriod());
                        inboundBookingItemNew.setSpec(inboundBookingItem.getSpec());
                        inboundBookingItemNew.setStoreDuration(inboundBookingItem.getStoreDuration());
                        inboundBookingItemNew.setInboundBooking(inboundBooking);
                        saveItem(inboundBookingItemNew);
                    }
                }
            }
        }
        return inboundBooking.getId();
    }

    @Transactional(readOnly = false)
    public void delete(Long id) {
        inboundBookingDao.delete(id);
    }

    public Page<InboundBooking> findInboundBookingByConditions(int page, int rows, final String serialNo, final BookingStatus bookingStatus, final Long customerId, final String vehicleNumbers) {
        Specification<InboundBooking> specification = new Specification<InboundBooking>() {
            @Override
            public Predicate toPredicate(Root<InboundBooking> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                Path SerialNoPath = root.get("serialNo");
                Path customerPath = root.get("customer").get("id");
                Path bookingStatusPath = root.get("bookingStatus");
                Path vehicleNumbersPath = root.get("vehicleNumbers");
                Path bookTime = root.get("bookTime");
                if (customerId != null) {
                    Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
                    list.add(p1);
                }
                if (bookingStatus != null) {
                    Predicate p1 = criteriaBuilder.equal(bookingStatusPath, bookingStatus);
                    list.add(p1);
                }
                if (serialNo != null && serialNo.trim().length() > 0) {
                    Predicate p2 = criteriaBuilder.like(SerialNoPath, "%" + serialNo + "%");
                    list.add(p2);
                }
                if (vehicleNumbers != null && vehicleNumbers.trim().length() > 0) {
                    Predicate p5 = criteriaBuilder.like(vehicleNumbersPath, "%" + vehicleNumbers + "%");
                    list.add(p5);
                }
                Predicate[] p = new Predicate[list.size()];
                criteriaQuery.orderBy((criteriaBuilder.desc(bookTime)));
                return criteriaBuilder.and(list.toArray(p));


            }
        };
        return inboundBookingDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
    }

    @Transactional(readOnly = false)
    public void update(InboundBooking inboundBooking, Comment comment) {
        // 车型为空时候
        if (inboundBooking.getVehicleType() != null && inboundBooking.getVehicleType().getId() == null) {
            inboundBooking.setVehicleType(null);
        }
        if (comment != null) {
            commentDao.save(comment);
        }
        inboundBookingDao.save(inboundBooking);
    }

    @Transactional(readOnly = false)
    public void deleteItem(Long id) {
        inboundBookingItemDao.delete(id);
    }

    public InboundBookingItem findOneItem(Long id) {
        return inboundBookingItemDao.findOne(id);
    }

    @Transactional(readOnly = false)
    public void saveItem(InboundBookingItem inboundBookingItem) {
    	InboundBookingItem inboundBookingItemobj=inboundBookingItemDao.findBybookingIdAndProId(inboundBookingItem.getInboundBooking().getId(),inboundBookingItem.getProductId());
    	if(inboundBookingItemobj!=null){
    		InboundBookingItem inboundBookingItemOld=null;
    		if(inboundBookingItem.getId()!=null){
    			//编辑验证
    			inboundBookingItemOld=inboundBookingItemDao.findOne(inboundBookingItem.getId());
    		}
    		if(inboundBookingItem.getId()==null||(inboundBookingItemOld!=null&&!inboundBookingItemOld.getProduct().getId().equals(inboundBookingItemobj.getProduct().getId()))){
    			throw new RuntimeException("商品："+inboundBookingItemobj.getProductName()+"已添加，可进行明细修改！");
    		}
    		
    	}
    	if (inboundBookingItem.getPacking() != null && inboundBookingItem.getPacking().getId() == null) {
            inboundBookingItem.setPacking(null);
        }
        inboundBookingItemDao.save(inboundBookingItem);
    }

    public List<InboundBookingItem> findInboundBookingItems(String serialNo) {
        return inboundBookingItemDao.findInboundBookingItems(serialNo);
    }

    public List<InboundBookingItem> findByInboundBookingItems(Long inboundBookingId) {
        return inboundBookingItemDao.findByInboundBookingItems(inboundBookingId);
    }

    public Page<InboundBooking> findBySerialNoLike(String serialNo, int page, int rows) {
        return inboundBookingDao.findBySerialNoLike(BookingStatus.未审核, serialNo, RepoUtils.buildPageRequest(page, rows));
    }

    /**
     * 合并预约商品信息,按商品分类
     *
     * @param bookInventorys
     * @return
     */
    public List<InboundBookingItem> mergeInboundBookingItem(Set<InboundBookingItem> inboundBookingItems) {
        List<InboundBookingItem> resultList = new ArrayList<InboundBookingItem>();
        Map<String, InboundBookingItem> jsonlist = new LinkedHashMap<String, InboundBookingItem>();
        InboundBookingItem obj = null;
        for (InboundBookingItem inboundBookingItem : inboundBookingItems) {
            Long productId = inboundBookingItem.getProduct().getId();
            String key = productId.toString();
            if (!jsonlist.containsKey(key)) {
                obj = inboundBookingItem.cloneInboundBookingItem();
                jsonlist.put(key, obj);
            } else {
                InboundBookingItem putinboundBookingItem = jsonlist.get(key);
                putinboundBookingItem.setWeight(putinboundBookingItem.getWeight() + inboundBookingItem.getWeight());
                putinboundBookingItem.setAmount(putinboundBookingItem.getAmount() + inboundBookingItem.getAmount());
                putinboundBookingItem.setStoreContainerCount(putinboundBookingItem.getStoreContainerCount() + inboundBookingItem.getStoreContainerCount());
 
                jsonlist.put(key, putinboundBookingItem);
            }
        }
        resultList.addAll(jsonlist.values());
        return resultList;
    }

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    public List<Map> findInboundBookingStat(Long[] areaIds, Date startDate, Date endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (ArrayUtils.isNotEmpty(areaIds)) {
            List<Long> allAreas = new ArrayList<Long>();
            for (Long areaId : areaIds) {
                allAreas.add(areaId);
            }
            params.put("areaIds", allAreas);
        }
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return sqlSessionTemplate.selectList("findInboundBookingStat", params);
    }
}
