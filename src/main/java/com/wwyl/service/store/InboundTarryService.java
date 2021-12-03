package com.wwyl.service.store;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.StockInStatus;
import com.wwyl.dao.store.InboundTarryDao;
import com.wwyl.entity.store.InboundTarry;
/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class InboundTarryService {

	@Resource
	private InboundTarryDao  inboundTarryDao;
	public List<InboundTarry> findAll() {
		return inboundTarryDao.findAll();
	}

	public InboundTarry findOne(Long id) {
		return inboundTarryDao.findOne(id);
	}
 
	 
	public List<InboundTarry> findInboundTarryByConditions(final String handsetAddress,final StockInStatus  stockInStatus) {
		Specification<InboundTarry> specification = new Specification<InboundTarry>() {
			@Override
			public Predicate toPredicate(Root<InboundTarry> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path handsetAddressPath = root.get("handsetAddress");
				Path stockInStatusPath = root.get("inboundRegister").get("stockInStatus");
				Path registerTimePath = root.get("inboundRegister").get("registerTime");
				
				if (StringUtils.isNotBlank(handsetAddress)) {
					Predicate p1 = criteriaBuilder.equal(handsetAddressPath, handsetAddress);
					list.add(p1);
				}
				 
				if (stockInStatus != null) {
					Predicate p2 = criteriaBuilder.equal(stockInStatusPath, stockInStatus);
					list.add(p2);
				}
				 
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(registerTimePath)));
				return criteriaBuilder.and(list.toArray(p));
			}
		};
		return inboundTarryDao.findAll(specification);
	}

	
	public Map  findHandsetTask(){
		List<Object> result=inboundTarryDao.findHandsetTask();
		Map  map=new LinkedHashMap();
		for (Object object : result) {
			Object[] arr=(Object[]) object;
			map.put(arr[0], arr[1]);
		}
		return map;
	}
	
}
