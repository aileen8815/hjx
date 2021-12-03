package com.wwyl.service.settings;

import com.wwyl.Constants;
import com.wwyl.Enums.BookingStatus;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.StoreLocation;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.Basic;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.wwyl.service.store.AreaStat;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StoreLocationService {

    @Resource
    private StoreLocationDao storeLocationDao;
    @Resource
    private SerialNumberService serialNumberService;
    @Resource
    private StoreAreaService storeAreaService;

    public List<StoreLocation> findAll() {
        return this.storeLocationDao.findAll();
    }

    public Set<StoreLocation> findByCodeIn(String[] codes) {
        return this.storeLocationDao.findByCodeIn(codes);
    }

    public StoreLocation findByCode(String code) {
        return this.storeLocationDao.findByCode(code);
    }

    public StoreLocation findOne(Long id) {
        return (StoreLocation) this.storeLocationDao.findOne(id);
    }

    @Transactional(readOnly = false)
    public void create(StoreLocation storeLocation) {
        if (StringUtils.isBlank(storeLocation.getLabel())) {
            storeLocation.setLabel(null);
        }
        StoreArea storeArea = storeAreaService.findOne(storeLocation.getStoreArea().getId());
        String storeAreaCode = storeArea.getCode();
        String code = storeAreaCode + "-" + getCode(storeLocation.getCoordX()) + "-" + getCode(storeLocation.getCoordY()) + "-" + getCode(storeLocation.getCoordZ());
        StoreLocation exists = findByCode(code);
        if (exists != null) {
            throw new RuntimeException("储位已经存在");
        }
        storeLocation.setCode(code);
        this.storeLocationDao.save(storeLocation);
    }

    @Transactional(readOnly = false)
    public void update(StoreLocation storeLocation) {
        if (StringUtils.isBlank(storeLocation.getLabel())) {
            storeLocation.setLabel(null);
        }

        this.storeLocationDao.save(storeLocation);
    }

    @Transactional(readOnly = false)
    public void delete(Long id) {
        this.storeLocationDao.delete(id);
    }

    public Page<StoreLocation> findByCodeLike(int page, int rows, String code) {
        return this.storeLocationDao.findByCodeLike("%" + code + "%", RepoUtils.buildPageRequest(page, rows));
    }

    public Page<StoreLocation> findStoreLocationByConditions(int page,
                                                             int rows, final String code, final StoreLocationStatus storeLocationStatus, final Long storeLocationTypeId, final Long storeAreaId) {
        Specification<StoreLocation> specification = new Specification<StoreLocation>() {
            @Override
            public Predicate toPredicate(Root<StoreLocation> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                Path codePath = root.get("code");
                Path storeLocationTypePath = root.get("storeLocationType").get("id");
                Path storeAreaPath = root.get("storeArea");
                Path storeLocationStatusPath = root.get("storeLocationStatus");

                if (storeLocationTypeId != null) {
                    Predicate p1 = criteriaBuilder.equal(storeLocationTypePath, storeLocationTypeId);
                    list.add(p1);
                }
                if (storeLocationStatus != null) {
                    Predicate p1 = criteriaBuilder.equal(storeLocationStatusPath, storeLocationStatus);
                    list.add(p1);
                }
                if (code != null && code.trim().length() > 0) {
                    Predicate p2 = criteriaBuilder.like(codePath, "%" + code + "%");
                    list.add(p2);
                }
                
                if (storeAreaId != null) {
                    Predicate p1 = criteriaBuilder.equal(storeAreaPath, storeAreaId);
                    list.add(p1);
                }

                Predicate[] p = new Predicate[list.size()];
                criteriaQuery.orderBy(criteriaBuilder.asc(storeLocationTypePath), criteriaBuilder.asc(codePath));
                return criteriaBuilder.and(list.toArray(p));

            }
        };
        return storeLocationDao.findAll(specification,
                RepoUtils.buildPageRequest(page, rows));
    }

    public List<StoreLocation> findByArea(Long areaId) {
        return this.storeLocationDao.findByArea(areaId);
    }

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    public List<?> findLocationStat(Long[] areaIds) {
    	Boolean bAreaTypeFlg =false;
        Map<String, Object> params = null;
        if (ArrayUtils.isNotEmpty(areaIds)) {
            List<Long> allAreas = new ArrayList<Long>();
            for (Long areaId : areaIds) {
                StoreArea storeArea = storeAreaService.findOneWithChildren(areaId);
                allAreas.addAll(storeArea.getTreeNodeIds());
                //仓储笼判断
                if (allAreas.size() == 1 && storeArea.getCode().contains("LB")) {
                	bAreaTypeFlg = true;
                }
            }
            params = new HashMap<String, Object>();
            params.put("areaIds", allAreas);
        }
        List<?> locationStat = new ArrayList<Object>();
        if (bAreaTypeFlg) {
        	locationStat = sqlSessionTemplate.selectList("findBAreaStoreLocationStat", params);
        }else
        {
        	locationStat = sqlSessionTemplate.selectList("findStoreLocationStat", params);
        }
        
        List<Object> wholeAreaList = new LinkedList<Object>();
        if (CollectionUtils.isNotEmpty(locationStat)) {
            Map<String, Object> startLocation = (Map<String, Object>) locationStat.get(0);

            long preStoreArea = ((BigDecimal) startLocation.get("STOREAREA")).longValue();
            int preCoordX = ((BigDecimal) startLocation.get("COORDX")).intValue();
            int preCoordY = ((BigDecimal) startLocation.get("COORDY")).intValue();

            AreaStat areaStat = new AreaStat();
            areaStat.setAreaCode((String) startLocation.get("STOREAREACODE"));
            areaStat.setAreaName((String) startLocation.get("STOREAREANAME"));
            areaStat.setLayoutCorriderLine(((BigDecimal) startLocation.get("LAYOUTCORRIDERLINE")).intValue());

            List<Object> column = new LinkedList<Object>();
            List<Object> row = new LinkedList<Object>();
            Map<String, Object> location = null;

            int maxRowCount = 0;
            int usable = 0;
            int used = 0;
            int engage = 0;
            int maintain = 0;
            int unbind = 0;

            for (int i = 0; i < locationStat.size(); i++) {
                location = (Map<String, Object>) locationStat.get(i);

                long curStoreArea = ((BigDecimal) location.get("STOREAREA")).longValue();
                int curCoordX = ((BigDecimal) location.get("COORDX")).intValue();
                int curCoordY = ((BigDecimal) location.get("COORDY")).intValue();

                if ((((BigDecimal) location.get("STORELOCATIONTYPE")).intValue() == Constants.STORE_LOCATION_TYPE_SHELF_ID)
                		|| (((BigDecimal) location.get("STORELOCATIONTYPE")).intValue() == Constants.STORE_LOCATION_TYPE_STORAGE_CAGE_ID)) {// 货架
                    // 分割列
                    if (curCoordY != preCoordY) {
                        column.add(row);
                        preCoordY = curCoordY;
                        row = new LinkedList<Object>();
                    }

                    // 分隔行
                    if (curCoordX != preCoordX) {
                        // 排除空行
                        boolean addToColumnList = false;
                        if (CollectionUtils.isNotEmpty(column)) {
                            for (Object irow : column) {
                                if (CollectionUtils.isNotEmpty((List) irow)) {
                                    addToColumnList = true;
                                    break;
                                }
                            }
                        }
                        if (addToColumnList) {
                            areaStat.getColumnList().add(column);
                            if (maxRowCount < column.size()) {
                                maxRowCount = column.size();
                            }
                        }

                        preCoordX = curCoordX;
                        column = new LinkedList<Object>();
                    }

                    // 分割库区
                    if (curStoreArea != preStoreArea) {
                        areaStat.setMaxRowCount(maxRowCount);
                        areaStat.setUnbind(unbind);
                        areaStat.setUsable(usable);
                        areaStat.setUsed(used);
                        areaStat.setEngage(engage);
                        areaStat.setMaintain(maintain);
                        wholeAreaList.add(areaStat);

                        maxRowCount = 0;
                        unbind = 0;
                        usable = 0;
                        used = 0;
                        engage = 0;
                        maintain = 0;

                        preStoreArea = curStoreArea;
                        areaStat = new AreaStat();
                        areaStat.setAreaCode((String) location.get("STOREAREACODE"));
                        areaStat.setAreaName((String) location.get("STOREAREANAME"));
                        areaStat.setLayoutCorriderLine(((BigDecimal) location.get("LAYOUTCORRIDERLINE")).intValue());
                    }

                    row.add(location);

                    int iStatus;
                    try {
                        iStatus = ((BigDecimal) location.get("STORELOCATIONSTATUS")).intValue();
                    }catch (Exception e){
                        iStatus = ((Long) location.get("STORELOCATIONSTATUS")).intValue(); // FOR MYSQL
                    }
                    switch (iStatus) {
                        case 0:
                            unbind++;
                            break;
                        case 2:
                            used++;
                            break;
                        case 3:
                            engage++;
                            break;
                        case 4:
                            maintain++;
                            break;
                        default:
                            usable++;
                    }
                } else {//周转区
                    areaStat.getTurnoverList().add(location);
                }
            }

            // 加入最后一列
            column.add(row);
            areaStat.getColumnList().add(column);
            areaStat.setMaxRowCount(maxRowCount);
            areaStat.setUnbind(unbind);
            areaStat.setUsable(usable);
            areaStat.setUsed(used);
            areaStat.setEngage(engage);
            areaStat.setMaintain(maintain);
            wholeAreaList.add(areaStat);
        }
        return wholeAreaList;
    }

    public StoreLocation findStoreLocationByCode(final String code) {
        Specification<StoreLocation> specification = new Specification<StoreLocation>() {
            @Override
            public Predicate toPredicate(Root<StoreLocation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("code").as(String.class), code);
            }
        };
        return storeLocationDao.findOne(specification);
    }

    public StoreLocation findStoreLocationByLabel(final String label) {
        Specification<StoreLocation> specification = new Specification<StoreLocation>() {
            @Override
            public Predicate toPredicate(Root<StoreLocation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("label").as(String.class), label);
            }
        };
        return storeLocationDao.findOne(specification);
    }

    private static String storeLocationBindLock = "storeLocationBindLock";

    @Transactional(readOnly = false)
    public void bind(String serialNo, String locationCode) {
        synchronized (storeLocationBindLock) {
            StoreLocation storeLocation = this.findStoreLocationByLabel(serialNo);
            if (storeLocation != null) {
                throw new RuntimeException("此货架序列号已被使用");
            }

            storeLocation = this.findStoreLocationByCode(locationCode);
            if (storeLocation == null) {
                throw new RuntimeException("无效的货架编码");
            }

            //if (StoreLocationStatus.未绑定.equals(storeLocation.getStoreLocationStatus()) || StringUtils.isBlank(storeLocation.getLabel())) {
            storeLocation.setLabel(serialNo);
            storeLocation.setStoreLocationStatus(StoreLocationStatus.可使用);
            storeLocationDao.save(storeLocation);
            storeLocationDao.flush();
            //}
        }
    }
    
    @Transactional(readOnly = false)
    public void unBind(String locationLable) {
        synchronized (storeLocationBindLock) {
            StoreLocation storeLocation = this.findStoreLocationByLabel(locationLable);
            if (storeLocation == null) {
                throw new RuntimeException("无效的参数");
            }
            storeLocation.setLabel(null);
            storeLocation.setStoreLocationStatus(StoreLocationStatus.未绑定);
            storeLocationDao.save(storeLocation);
            storeLocationDao.flush();
        }
    }

    @Transactional(readOnly = false)
    public void batchCreate(StoreLocation storeLocation) {
        StoreArea storeArea = storeAreaService.findOne(storeLocation.getStoreArea().getId());
        String storeAreaCode = storeArea.getCode();
        int row = storeLocation.getCoordX();
        int line = storeLocation.getCoordY();
        int tier = storeLocation.getCoordZ();
        StoreLocation creteobj = null;
        String code = null;
        StoreLocation exists = null;
        List<StoreLocation> storeLocationlist = new ArrayList<StoreLocation>();
        for (int x = 1; x <= row; x++) {
            for (int y = 1; y <= line; y++) {
                for (int z = 1; z <= tier; z++) {
                    creteobj = new StoreLocation();
                    code = storeAreaCode + "-" + getCode(x) + "-" + getCode(y) + "-" + getCode(z);
                    exists = findByCode(code);
                    if (exists != null) {
                        continue;
                    }
                    creteobj.setCode(code);
                    creteobj.setCoordX(x);
                    creteobj.setCoordY(y);
                    creteobj.setCoordZ(z);
                    creteobj.setHeight(storeLocation.getHeight());
                    creteobj.setLength(storeLocation.getLength());
                    creteobj.setWeight(storeLocation.getWeight());
                    creteobj.setWidth(storeLocation.getWidth());
                    creteobj.setRemark(storeLocation.getRemark());
                    creteobj.setStoreArea(storeArea);
                    creteobj.setStoreLocationStatus(StoreLocationStatus.未绑定);
                    creteobj.setStoreLocationType(storeLocation.getStoreLocationType());
                    storeLocationlist.add(creteobj);
                }
            }
        }
        this.storeLocationDao.save(storeLocationlist);
    }

    public String getCode(int code) {
        String codestr = code + "";
        if (code < 10) {
            codestr = "0" + code;
        }/*else if(code<100){
            codestr="0"+code;
		}*/
        return codestr;
    }

    @Transactional(readOnly = false)
    public void updateStoreLocationStatus(Long storeLocationId, StoreLocationStatus storeLocationStatus) {
        storeLocationDao.updateStoreLocationStatus(storeLocationId, storeLocationStatus);
    }
}